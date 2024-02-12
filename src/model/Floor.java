package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import controller.AudioManager;
import utilities.Constants;

/**
 * Classe che rappresenta il terreno di un livello nel gioco.
 * Si occupa di gestire tutti gli elementi presenti sul terreno.
 * Inoltre, gestisce le collisioni del giocatore e dei nemici sul terreno.
 * 
 * @author Lorenzo Zanda
 */
public class Floor {
	
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	private Tile[][] tiles;
	private int width;
	private int height;
	private List<Tile> tilesExploded;
	private List<Bomb> bombsPlaced;
	private List<Explosion> explosions;
	private List<PowerUp> powerUps;
	private List<Enemy> enemies;
	private Exit exit;
	
    /**
     * Costruisce un nuovo oggetto {@code Floor} con le dimensioni specificate.
     * 
     * @param width La larghezza del terreno.
     * @param height L'altezza del terreno.
     */
	public Floor(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
		tilesExploded = new ArrayList<Tile>();
		bombsPlaced = new ArrayList<Bomb>();
		explosions = new ArrayList<Explosion>();
		powerUps = new ArrayList<PowerUp>();
		enemies = new ArrayList<Enemy>();
	}
	
    /**
     * Verifica se la posizione specificata è una cella del pavimento.
     * 
     * @param x La coordinata x della cella.
     * @param y La coordinata y della cella.
     * @return {@code true} se la cella è una cella del pavimento, {@code false} altrimenti.
     */
	public boolean checkFloor(int x, int y) {

	    if (x >= 0 && x < width && y >= 0 && y < height) {
	        return tiles[y][x].getType().equals(TileType.FLOOR);
	    }
	    return false;
	}
	
    /**
     * Verifica se il collider dell'entita' entra in collisione con i blocchi solidi (indistruttibili o distruttibili) del terreno.
     * 
     * @param entityCollider Il collider del quale verificare le collisioni.
     * @return {@code true} se il collider entra in collisione con i blocchi solidi, {@code false} altrimenti.
     */
	public boolean collidesWithBlocks(Rectangle entityCollider) {
		
		int left   = entityCollider.x / TILESIZE;
	    int right  = (entityCollider.x + entityCollider.width - 1) / TILESIZE;
	    int top    = entityCollider.y / TILESIZE;
	    int bottom = (entityCollider.y + entityCollider.height - 1) / TILESIZE;
	    
	    //Controlla collisione in ogni direzione
	    for (int y = top; y <= bottom; y++) {
	        for (int x = left; x <= right; x++) {
	            
                TileType tile = tiles[y][x].getType();
                if (tile.equals(TileType.UNBREAKABLE) || tile.equals(TileType.BREAKABLE)) {
                    return true; 
                }
	        }
	    } 
		return false;
	}
	
    /**
     * Verifica se il collider del giocatore entra in collisione con le bombe piazzate.
     * Se il giocatore ha piazzato la bomba e ci si trova sopra, allora la collisione del giocatore
     * con la bomba rimane disabilitata fin quando il giocatore non si allontana a sufficienza.
     * 
     * @param playerCollider Il collider del quale verificare le collisioni.
     * @return {@code true} se il collider entra in collisione con la bombe, {@code false} altrimenti.
     */
	public boolean playerCollidesWithBombs(Rectangle playerCollider) {
		
		for (Bomb bomb : bombsPlaced) {
			
			//Check distanza giocatore dalla bomba -> se il giocatore e' lontano a sufficienza allora collisione attiva
			bomb.enableCollision(playerCollider);
			if (bomb.getCollisionEnabled() && bomb.getCollider().intersects(playerCollider)) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * Verifica se il collider del nemico entra in collisione con le bombe piazzate.
     * 
     * @param enemyCollider Il collider del quale verificare le collisioni.
     * @return {@code true} se il collider entra in collisione con la bombe, {@code false} altrimenti.
     */
	public boolean enemyCollidesWithBombs(Rectangle enemyCollider) {
		
		return bombsPlaced.stream()
						  .anyMatch(bomb -> bomb.getCollider().intersects(enemyCollider));
	}
	
	/**
	 * Verifica se il collider specificato entra in collisione con i nemici presenti sulla mappa.
	 * Se il {@code Player} invoca questo metodo allora {@code invokingEnemy} e' {@code null}.
	 * Se un {@code Enemy} invoca questo metodo allora {@code invokingEnemy} sara' l'{@code Enemy} stesso.
	 * Nel caso in cui un {@code Enemy} invochi questo metodo, la collisione sara' verificata con i nemici diversi da lui.
	 * 
	 * @param entityCollider Il collider del quale verificare le collisioni.
	 * @param invokingEnemy Il nemico che invoca il metodo (se il giocatore invoca il metodo, questo parametro e' null)
	 * @return {@code true} se il collider entra in collisione con i nemici, {@code false} altrimenti.
	 */
	public boolean collidesWithEnemies(Rectangle entityCollider, Enemy invokingEnemy) {
		
		return enemies.stream()
					  .filter(Enemy::isAlive)
					  .anyMatch(enemy -> !enemy.equals(invokingEnemy) && enemy.getCollider().intersects(entityCollider));
	}
	
    /**
     * Verifica se il collider specificato entra in collisione con le esplosioni.
     * 
     * @param entityCollider Il collider del quale verificare le collisioni.
     * @return {@code true} se il collider entra in collisione con esplosioni, {@code false} altrimenti.
     */
	public boolean collidesWithExplosions(Rectangle entityCollider) {
		
		int entityX = (entityCollider.x + entityCollider.width / 2) / TILESIZE;
		int entityY = (entityCollider.y + entityCollider.height / 2) / TILESIZE;
		
		return explosions.stream()
						 .anyMatch(exp -> exp.getExplosionTiles().contains(new Point(entityX, entityY)));
	}
	
    /**
     * Verifica se il collider del giocatore entra in collisione con i power-up.
     * 
     * @param playerCollider Il collider del quale verificare le collisioni.
     * @return Il power-up con cui il collider entra in collisione, altrimenti {@code null}.
     */
	public PowerUp collidesWithPowerUps(Rectangle playerCollider) {
	
		for (PowerUp powerUp : powerUps) {
			if (powerUp.getCollider().intersects(playerCollider)) {
				powerUp.setCollected(true);
				return powerUp;
			}
		}
		return null;	
	}

    /**
     * Rimuove i power-up colpiti da un'esplosione.
     * 
     * @param explosion L'esplosione.
     */
	public void removePowerUps(Explosion explosion) {
		
		for (PowerUp powerUp : powerUps) {
			int powerUpX = powerUp.getX();
			int powerUpY = powerUp.getY();
			if (explosion.getExplosionTiles().contains(new Point(powerUpX, powerUpY))) {
				powerUp.setCollected(true);
			}
		}
	}
	
    /**
     * Verifica se il collider del giocatore entra in collisione con l'uscita.
     * 
     * @param boxCollider Il collider del quale verificare le collisione.
     * @return {@code true} se il collider entra in collisione con l'uscita, {@code false} altrimenti.
     */
	public boolean collidesWithExit(Rectangle boxCollider) {
		
		//Se il player ha ucciso tutti i nemici e collide con l'uscita -> vittoria 
		if (enemies.isEmpty() && exit.getCollider().equals(boxCollider)) {
			return true;
		}
		
		return false;
	}
	
	/**
     * In seguito all'esplosione di una bomba, distrugge i blocchi nel raggio di esplosione.
     * 
     * @param explosion L'esplosione generata dalla bomba.
     */
	public void explodeBomb(Explosion explosion) {
		
		Bomb bomb = explosion.getBomb();
		
		int x = bomb.getX();
		int y = bomb.getY();
		int radius = bomb.getExplosionRadius();
		
		destroyBlock(explosion, x-1, y, radius, -1, 0); //left
		destroyBlock(explosion, x+1, y, radius, +1, 0); //right
		destroyBlock(explosion, x, y-1, radius, 0, -1); //up
		destroyBlock(explosion, x, y+1, radius, 0, +1); //down
	}
	
    /**
     * Distrugge un blocco nel raggio di esplosione della bomba.
     * 
     * @param explosion L'esplosione generata dalla bomba.
     * @param x La coordinata x del blocco da distruggere.
     * @param y La coordinata y del blocco da distruggere.
     * @param radius Il raggio di esplosione della bomba.
     * @param dx La direzione x in cui distruggere i blocchi.
     * @param dy La direzione y in cui distruggere i blocchi.
     */
	public void destroyBlock(Explosion explosion, int x, int y, int radius, int dx, int dy) {
		
		while (radius > 0) {
			
			if (x >= 0 && x < width && y >= 0 && y < height) {
				
				Tile tile = tiles[y][x];
				
				if (tile.getType().equals(TileType.BREAKABLE)) {
					tilesExploded.add(tile);
					tile.setHit(true);
					break;
				}
				
				else if (tile.getType().equals(TileType.UNBREAKABLE)) break;
				
				else {
					x += dx;
					y += dy;
					radius--;
				}
			}	
		}
	}
	
	/**
	 * Restituisce la mappa del terreno di gioco.
	 *
	 * @return La mappa del terreno di gioco.
	 */
	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Restituisce l'altezza del terreno di gioco.
	 *
	 * @return L'altezza del terreno di gioco.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Restituisce la larghezza del terreno di gioco.
	 *
	 * @return La larghezza del terreno di gioco.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Restituisce la cella alla posizione specificata.
	 *
	 * @param i Coordinata y della cella.
	 * @param j Coordinata x della cella.
	 * @return La cella alla posizione specificata.
	 */
	public Tile getCell(int i, int j) {
		return tiles[i][j];
	}
	
	/**
	 * Imposta la cella del terreno con il valore della cella fornita.
	 *
	 * @param tile La cella da impostare.
	 */
	public void setCell(Tile tile) {
		tiles[tile.getY()][tile.getX()] = tile;
	}
	
	/**
	 * Restituisce la lista delle bombe piazzate sul terreno di gioco.
	 *
	 * @return La lista delle bombe piazzate sul terreno di gioco.
	 */
	public List<Bomb> getBombsPlaced() {
		return bombsPlaced;
	}
	
	/**
	 * Restituisce la lista delle esplosioni presenti sul terreno di gioco.
	 *
	 * @return La lista delle bombe presenti sul terreno di gioco.
	 */
	public List<Explosion> getExplosions() {
		return explosions;
	}
	
	/**
	 * Restituisce la lista dei power-up presenti sul terreno di gioco.
	 *
	 * @return La lista delle power-up presenti sul terreno di gioco.
	 */
	public List<PowerUp> getPowerUps() {
		return powerUps;
	}
	
	/**
	 * Restituisce la lista dei nemici presenti sul terreno di gioco.
	 *
	 * @return La lista delle nemici presenti sul terreno di gioco.
	 */
	public List<Enemy> getEnemies() {
		return enemies;
	}
	
	/**
	 * Restituisce la lista delle celle esplose ancora presenti sul terreno di gioco.
	 *
	 * @return La lista delle celle esplose ancora presenti sul terreno di gioco.
	 */
	public List<Tile> getTilesExploded() {
		return tilesExploded;
	}
	
	/**
	 * Restituisce l'uscita sul terreno di gioco.
	 *
	 * @return L'uscita sul terreno di gioco.
	 */
	public Exit getExit() {
		return exit;
	}
	
	/**
	 * Imposta l'uscita sul terreno di gioco.
	 * 
	 * @param exit La nuova uscita da impostare
	 */
	public void setExit(Exit exit) {
		this.exit = exit; 
	}
	
	
    /**
     * Aggiunge una bomba al terreno.
     * 
     * @param bomb La bomba da aggiungere.
     * @return {@code true} se la bomba è stata aggiunta con successo, {@code false} altrimenti.
     */
	public boolean addBomb(Bomb bomb) {
		if (!(bombsPlaced.contains(bomb))) {
			AudioManager.getInstance().play("/audio/bomb-place.wav");
			bombsPlaced.add(bomb);
			bomb.tick(); //start timer
			return true;
		}
		return false;
	}
	
	/**
	 * Aggiunge una esplosione alla lista delle esplosioni presenti sul terreno di gioco.
	 *
	 * @param explosion L'esplosione da aggiungere.
	 */
	public void addExplosion(Explosion explosion) {
		explosions.add(explosion);
	}
	
	/**
	 * Aggiunge un power-up alla lista dei power-up presenti sul terreno di gioco.
	 *
	 * @param powerUp Il power-up da aggiungere.
	 */
	public void addPowerUp(PowerUp powerUp) {
		powerUps.add(powerUp);
	}
	
	/**
	 * Aggiunge un nemico alla lista dei nemici presenti sul terreno di gioco.
	 *
	 * @param enemy Il nemico da aggiungere.
	 */
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}
	
	/**
	 * Aggiunge una cella alla lista delle celle esplose ancora presenti sul terreno di gioco.
	 *
	 * @param tile La cella da aggiungere.
	 */
	public void addTileExploded(Tile tile) {
		tilesExploded.add(tile);
	}
	
	/**
	 * Libera il terreno di gioco, rimuovendo tutte le bombe, esplosioni, power-up, nemici e tiles esplosi.
	 */
	public void clear() {
		bombsPlaced.clear();
		explosions.clear();
		powerUps.clear();
		enemies.clear();
		tilesExploded.clear();
	}
}
