package controller;

import java.util.Random;

import model.Player;
import model.Denkyun;
import model.Enemy;
import model.Puropen;
import model.Exit;
import model.Floor;
import model.PowerUp;
import model.Tile;
import model.TileType;
import utilities.Constants;
import view.BomberManFrame;
import view.BomberManPanel;
import view.DenkyunView;
import view.EnemyView;
import view.ExitView;
import view.PlayerView;
import view.PowerUpView;
import view.PuropenView;
import view.TileView;

/**
 * Classe che gestisce la logica di generazione e disposizione degli elementi 
 * all'interno del gioco.
 * 
 * @author Lorenzo Zanda
 */
public class FloorController {
	
	private boolean firstTime;
	
	private BomberManPanel panel;
	
	private Floor floor;
	private int height;
	private int width;
	private int level;
	
	private final double BREAKABLE_CHANCE = 0.4;
	private final double POWERUP_CHANCE = 0.4;
	private final double ENEMY_CHANCE = 0.1;
	
	/**
	 * Costruttore della classe {@code FloorController}.
	 * 
	 * @param frame L'istanza di {@code BomberManFrame} che rappresenta il frame principale del gioco.
	 */
	public FloorController(BomberManFrame frame) {
		this.panel = frame.getGamePanel();
		this.width = Constants.COLS;
		this.height = Constants.ROWS;
		floor = new Floor(width, height);
		firstTime = true;
	}
	
	 /**
     * Metodo che genera la mappa del livello corrente.
     * Si occupa di generare le celle del livello corrente, piazzare il giocatore, i power-up, i nemici e l'uscita.
     * Si serve dei metodi specializzati che si occupano di creare le singoli componenti.
     */
	public void createMap() {
		floor.clear();
		panel.clear();
		generateMap();
		placePlayer();
		placePowerUps();
		placeExit();
		placeEnemies();
		firstTime = false;
	}
	
	 /**
     * Genera le celle della mappa del livello corrente basandosi su probabilità e posizioni fissate.
     */
	private void generateMap() {
		
		Random random = new Random();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				
				if (isUnbreakable(i, j)) {
					floor.setCell(new Tile(j, i, TileType.UNBREAKABLE));
				}
				else if (random.nextDouble() < BREAKABLE_CHANCE) {
					floor.setCell(new Tile(j, i, TileType.BREAKABLE));
				}
				else {
					floor.setCell(new Tile(j, i, TileType.FLOOR));
				}
			}
		}
		
		clearSpawn();
		setTileViews();
	}
	
    /**
     * Garantisce che lo spawn del giocatore sia uno spawn pulito 
     * (il giocatore si trova al di sopra di celle pavimento e ha spazio a sufficienza per muoversi e piazzare bombe).
     */
	private void clearSpawn() {
		floor.setCell(new Tile(1, 1, TileType.FLOOR));
		floor.setCell(new Tile(1, 2, TileType.FLOOR));
		floor.setCell(new Tile(2, 1, TileType.FLOOR));
		floor.setCell(new Tile(1, 3, TileType.BREAKABLE));
		floor.setCell(new Tile(3, 1, TileType.BREAKABLE));	
	}
	
	/**
	 * Imposta le viste delle celle per la rappresentazione grafica.
	 */
	private void setTileViews() {

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				
				Tile tile = floor.getCell(i, j);
				TileView tileView = new TileView();
				tile.addObserver(tileView);
				tile.modified();
				panel.addTileToDraw(tile, tileView);
			}
		}
	}
	
    /**
     * Piazza il giocatore nella mappa.
     */
	private void placePlayer() {
		
		Player player = Player.getInstance();
		PlayerView playerView = new PlayerView();
		player.addObserver(playerView);
		player.modified();
		panel.setPlayerView(playerView);

		// Se non e' la prima volta che viene creata la mappa -> resetta valori del giocatore
		if (!firstTime) {
			player.setValues();
		}
		else {
			player.setFloor(floor);
			
		}
	}
	
    /**
     * Piazza i power-up in posizioni casuali in corrispondenza dei blocchi distruttibili della mappa.
     * La probabilita' che un blocco distruttibile contenga un power-up è basata su {@code POWERUP_CHANCE}.
     */
	private void placePowerUps() {
		
		Random random = new Random();
		
	    for (int i = 0; i < height; i++) {
	        for (int j = 0; j < width; j++) {
	            if (floor.getCell(i, j).getType() == TileType.BREAKABLE && random.nextDouble() < POWERUP_CHANCE) {
	            	PowerUp powerUp = new PowerUp(j, i);
	            	if (new Random().nextDouble() < powerUp.getSpawnPercent())
	            		floor.addPowerUp(powerUp);
	            		PowerUpView powerUpView= new PowerUpView();
	            		powerUp.addObserver(powerUpView);
	            		powerUp.modified();
	            		panel.addPowerUpToDraw(powerUp, powerUpView);
	            }
	        }
	    }	
	}
	
    /**
     * Piazza l'uscita del livello in una posizione casuale sotto un blocco distruttibile,
     * evitando posizioni già occupate da power-up e limitando la posizione a una certa area della mappa (che va dalla cella (6,6) a (width, height)).
     */
	private void placeExit() {
		
		int exitX, exitY;
		Random random = new Random();
		
		do {
			exitX = random.nextInt(width);
			exitY = random.nextInt(height);
		} while(floor.getCell(exitY, exitX).getType() != TileType.BREAKABLE
				|| floor.getPowerUps().contains(new PowerUp(exitX, exitY))
				|| (exitX <= 5 || exitY <= 5));
		
		Exit exit = new Exit(exitX, exitY);
		floor.setExit(new Exit(exitX, exitY));
		
		ExitView exitView = new ExitView();
		exit.addObserver(exitView);
		exit.modified();
		panel.setExitView(exitView);
		
	}
	
	 /**
     * Piazza nemici casualmente nella mappa, evitando alcune posizioni specifiche.
     */
	private void placeEnemies() {
		
		Random random = new Random();
		int maximumEnemies = Constants.NUMBER_OF_ENEMIES[level];
		
	    for (int i = 0; i < height; i++) {
	        for (int j = 0; j < width; j++) {
	        	
	        	//Nemici non spawnano sulle celle (1, 1), (1, 2), (2, 1)
	        	
	        	if ((i == 1 && j == 1) 
	        	||  (i == 1 && j == 2)
	        	||  (i == 2 && j == 1)) {
	        		continue;
	        	}
	        	
	            if (floor.checkFloor(j, i)
	            && random.nextDouble() < ENEMY_CHANCE
	            && floor.getEnemies().size() < maximumEnemies) {
	            	
	            	int randomType = random.nextInt(2);
	            	int enemyX = j * Constants.SCALED_TILESIZE;
	            	int enemyY = i * Constants.SCALED_TILESIZE;
	            	
	            	Enemy enemy = null;
	            	EnemyView enemyView = null;
	            	
	            	switch(randomType) {
	            		case 0 -> {
	            			enemy = new Puropen(enemyX, enemyY, floor); 
	            			enemyView = new PuropenView();
	            		}
	            		case 1 -> {
	            			enemy = new Denkyun(enemyX, enemyY, floor);
	            			enemyView = new DenkyunView();
	            		}
	            	}
	            	
	            	floor.addEnemy(enemy);
	            
	            	enemy.addObserver(enemyView);
	            	enemy.modified();
	            	panel.addEnemyToDraw(enemy, enemyView);
	            }
	        }
	    }	
	}
	
    /**
     * Verifica se una cella in una posizione specifica deve essere resa indistruttibile.
     * 
     * @param i Riga della cella.
     * @param j Colonna della cella.
     * @return {@code true} se la cella deve essere indistruttibile, {@code false} altrimenti.
     */
	private boolean isUnbreakable(int i, int j) {
		return ((i == 0) || (j == 0) || (i == height-1) || (j == width-1) || (i % 2 == 0 && j % 2 == 0));
	}
	
	/**
	 * Restituisce il pavimento di gioco.
	 * 
	 * @return Il pavimento di gioco.
	 */
	public Floor getFloor() {
		return floor;
	}
	
	/**
	 * Imposta il livello corrente.
	 * 
	 * @param level Il livello da impostare.
	 */
	public void setLevel(int level) {
		this.level = level;
	}
}
