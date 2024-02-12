package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import controller.GameController;
import model.Bomb;
import model.Explosion;
import model.TileType;
import utilities.Constants;
import model.PowerUp;
import model.Tile;
import model.Enemy;
import javax.swing.*;

/**
 * Classe che rappresenta il pannello principale del gioco.
 * Questo pannello è responsabile di disegnare gli elementi del gioco sulla schermata.
 * 
 * @author Lorenzo Zanda
 * @see JPanel
 */
public class BomberManPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7823690053368347423L;
	
	/** Numero di colonne nel terreno di gioco. */
	private int cols = Constants.COLS;
	
    /** Numero di righe nella terreno di gioco. */
	private int rows = Constants.ROWS;
	
	/** Livello corrente del gioco. */
	private int level;

	/** Mappa che associa le bombe da disegnare alle rispettive viste. */
	private Map<Bomb, BombView> bombsMap;

	/** Mappa che associa le esplosioni da disegnare alle rispettive viste. */
	private Map<Explosion, ExplosionView> explosionsMap;

	/** Mappa che associa i nemici da disegnare alle rispettive viste. */
	private Map<Enemy, EnemyView> enemiesMap;

	/** Mappa che associa i power-up da disegnare alle rispettive viste. */
	private Map<PowerUp, PowerUpView> powerUpsMap;

	/** Mappa che associa le celle (tiles) da disegnare alle rispettive viste. */
	private Map<Tile, TileView> tilesMap;

	/** Vista associata all'uscita da disegnare. */
	private ExitView exitView;

	/** Vista associata al giocatore da disegnare. */
	private PlayerView playerView;

	/** Controller del gioco associato al pannello. */
	private GameController controller;
	
    /**
     * Costruttore della classe {@code BomberManPanel}.
     * Inizializza il layout.
     */
	public BomberManPanel() {
		
		bombsMap = new HashMap<Bomb, BombView>();
		explosionsMap = new HashMap<Explosion, ExplosionView>();
		enemiesMap = new HashMap<Enemy, EnemyView>();
		powerUpsMap = new HashMap<PowerUp, PowerUpView>();
		tilesMap = new HashMap<Tile, TileView>();
		
		setLayout(new BorderLayout());
		setFocusable(true);
	}
	
    /**
     * Override del metodo paintComponent per disegnare gli elementi di gioco sul pannello.
     *
     * @param g Oggetto Graphics utilizzato per disegnare.
     */
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		drawGround(g2);
		drawPowerUps(g2);
		drawExit(g2);
		drawBreakable(g2);
		drawBomb(g2);
		drawExplosions(g2);
		drawEnemies(g2);
		drawPlayer(g2);
	}
	
    /**
     * Disegna sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawGround(Graphics2D g2) {
		
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				
				Tile tile = controller.getCell(i, j);
				tilesMap.get(tile).draw(g2, level);
			}	
		}
	}
	
    /**
     * Disegna i blocchi distruttibili sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawBreakable(Graphics2D g2) {
		
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				
				Tile tile = controller.getCell(i, j);
				
				if (tile.getType() == TileType.BREAKABLE) {
					tilesMap.get(tile).draw(g2, level);
				}
			}	
		}
	}
	
    /**
     * Disegna i power-up sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawPowerUps(Graphics2D g2) {
		
		List<PowerUp> powerUps = new ArrayList<>(controller.getPowerUps());
		for (PowerUp powerUp : powerUps) {
			powerUpsMap.get(powerUp).draw(g2);
		}
	}
	
    /**
     * Disegna l'uscita sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawExit(Graphics2D g2) {
		exitView.draw(g2);
	}
	
    /**
     * Disegna le bombe piazzate sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawBomb(Graphics2D g2) {
		
		List<Bomb> bombs = new ArrayList<>(controller.getBombsPlaced());
		for (Bomb bomb : bombs) {
			bombsMap.get(bomb).draw(g2);
		}
	}
	
    /**
     * Disegna le esplosioni sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawExplosions(Graphics2D g2) {
		
		List<Explosion> explosions = new ArrayList<>(controller.getExplosions());
		for (Explosion explosion : explosions) {
			explosionsMap.get(explosion).draw(g2);
		}
	}
	
    /**
     * Disegna il giocatore sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawPlayer(Graphics2D g2) {
		playerView.draw(g2);
	}
	
    /**
     * Disegna i nemici sul pannello.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void drawEnemies(Graphics2D g2) {
		List<Enemy> enemies = new ArrayList<>(controller.getEnemies());
		for (Enemy enemy : enemies) {
			enemiesMap.get(enemy).draw(g2);
		}
	}
	
	/**
	 * Imposta il livello corrente del gioco.
	 * 
	 * @param level Il livello da impostare.
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Aggiunge una bomba da disegnare al pannello.
	 *
	 * @param bomb La bomba da aggiungere al disegno.
	 * @param bombView La vista associata alla bomba da aggiungere.
	 */
	public void addBombToDraw(Bomb bomb, BombView bombView) {
		bombsMap.put(bomb, bombView);
	}
	
	/**
	 * Aggiunge un'esplosione da disegnare al pannello.
	 *
	 * @param explosion L'esplosione da aggiungere al disegno.
	 * @param explosionView La vista associata all'esplosione da aggiungere.
	 */
	public void addExplosionToDraw(Explosion explosion, ExplosionView explosionView) {
		explosionsMap.put(explosion, explosionView);
	}
	
	/**
	 * Aggiunge un nemico da disegnare al pannello.
	 *
	 * @param enemy Il nemico da aggiungere al disegno.
	 * @param enemyView La vista associata al nemico da aggiungere.
	 */
	public void addEnemyToDraw(Enemy enemy, EnemyView enemyView) {
		enemiesMap.put(enemy, enemyView);
	}
	
	/**
	 * Aggiunge un power-up da disegnare al pannello.
	 *
	 * @param powerUp Il power-up da aggiungere al disegno.
	 * @param powerUpView La vista associata al power-up da aggiungere.
	 */
	public void addPowerUpToDraw(PowerUp powerUp, PowerUpView powerUpView) {
		powerUpsMap.put(powerUp, powerUpView);
	}
	
	/**
	 * Aggiunge una cella da disegnare al pannello.
	 *
	 * @param tile La cella da aggiungere al disegno.
	 * @param tileView La vista associata alla cella da aggiungere.
	 */
	public void addTileToDraw(Tile tile, TileView tileView) {
		tilesMap.put(tile, tileView);
	}
	
	/**
	 * Imposta la vista dell'uscita da disegnare sul pannello.
	 *
	 * @param exitView La vista dell'uscita da impostare.
	 */
	public void setExitView(ExitView exitView) {
		this.exitView = exitView;
	}
	
	/**
	 * Imposta la vista del giocatore da disegnare sul pannello.
	 *
	 * @param playerView La vista del giocatore da impostare.
	 */
	public void setPlayerView(PlayerView playerView) {
		this.playerView = playerView;
	}

	/**
	 * Restituisce la mappa delle bombe da disegnare.
	 *
	 * @return La mappa delle bombe da disegnare.
	 */
	public Map<Bomb, BombView> getBombsMap() {
		return bombsMap;
	}

	/**
	 * Restituisce la mappa delle esplosioni da disegnare.
	 *
	 * @return La mappa delle esplosioni da disegnare.
	 */
	public Map<Explosion, ExplosionView> getExplosionsMap() {
		return explosionsMap;
	}	
	
	/**
	 * Restituisce la mappa dei nemici da disegnare.
	 *
	 * @return La mappa dei nemici da disegnare.
	 */
	public Map<Enemy, EnemyView> getEnemiesMap() {
		return enemiesMap;
	}	
	
	/**
	 * Restituisce la mappa dei power-up da disegnare.
	 *
	 * @return La mappa dei power-up da disegnare.
	 */
	public Map<PowerUp, PowerUpView> getPowerUpsMap() {
		return powerUpsMap;
	}
	
	/**
	 * Restituisce la mappa delle celle (tiles) da disegnare.
	 *
	 * @return La mappa delle celle (tiles) da disegnare.
	 */
	public Map<Tile, TileView> getTilesMap() {
		return tilesMap;
	}

	/**
	 * Restituisce la vista dell'uscita da disegnare.
	 *
	 * @return La vista dell'uscita da disegnare.
	 */
	public ExitView getExitView() {
		return exitView;
	}

	/**
	 * Restituisce la vista del giocatore da disegnare.
	 *
	 * @return La vista del giocatore da disegnare.
	 */
	public PlayerView getPlayerView() {
		return playerView;
	}
	
	/**
	 * Imposta il controller associato al pannello.
	 *
	 * @param controller Il controller da associare al pannello.
	 */
	public void setController(GameController controller) {
		this.controller = controller;
	}
	
	/**
	 * Svuota tutte le mappe dei disegni (bombe, esplosioni, nemici, power-up, celle).
	 */
	public void clear() {
		bombsMap.clear();
		explosionsMap.clear();
		enemiesMap.clear();
		powerUpsMap.clear();
		tilesMap.clear();
	}
}


