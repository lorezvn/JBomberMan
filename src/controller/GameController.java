package controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;
import utilities.Constants;
import model.User;
import model.Enemy;
import model.Floor;
import model.PowerUp;
import model.Tile;
import model.TileType;
import model.Bomb;
import model.Explosion;
import view.BombView;
import view.EnemyView;
import view.ExplosionView;
import view.TileView;
import model.Player;
import view.BomberManFrame;
import view.BomberManPanel;
import view.HudPanel;
import view.StatsPanel;

/**
 * Classe che funge da controller centrale per il gioco.
 * Gestisce la logica di gioco, l'input dell'utente e coordina le interazioni tra diversi
 * componenti come il {@code GameLoop}, il {@code FloorController} e il {@code ButtonController}.
 *
 * La classe include metodi per avviare, fermare e riavviare il gioco, gestire le azioni del giocatore
 * come il posizionamento di bombe, aggiornare lo stato del gioco e rispondere a eventi come la fine
 * del gioco o il completamento di un livello. 
 * 
 * Inoltre, gestisce l'utente che sta giocando, tenendo conto del suo progresso.
 *
 * @author Lorenzo Zanda
 */
public class GameController {
	
	private GameLoop gameLoop;
	private InputController inputController;
	private FloorController floorController;
	private ButtonController buttonController;
	
	private Map<String, User> users;
	private User userPlaying;
	
	private BomberManFrame frame;
	private BomberManPanel panel;
	private Floor floor;
	private Player player;
	private Timer walkTimer; 
	private int level = 0;
	
	private final int TILESIZE = Constants.SCALED_TILESIZE;

	/**
     * Costruttore della classe {@code GameController}.
     *
     * @param frame             Il {@code BomberManFrame} associato a questo controller.
     * @param floorController   Il {@code FloorController} associato a questo controller.
     * @param buttonController  Il {@code ButtonController} associato a questo controller.
     */
	public GameController(BomberManFrame frame, FloorController floorController, ButtonController buttonController) {
		
		this.frame = frame;
		this.floorController = floorController;
		this.buttonController = buttonController;
		
		users = LoginDataManager.loadUsers();
		
		floor = floorController.getFloor();
		panel = frame.getGamePanel();
		walkTimer = new Timer(300, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioManager.getInstance().play("/audio/walking1.wav");
			}
		});
		
		player = Player.getInstance();
		gameLoop = new GameLoop(this);
		inputController = new InputController(this);
	}
	
    /**
     * Piazza una bomba al centro del {@code Tile} in cui si trova il giocatore.
     * La bomba è piazzata solo se il giocatore ha ancora bombe disponibili e il {@code Tile} corrente è di tipo {@code FLOOR}.
     */
	public void placeBomb() {
		
		int x = player.getX();
		int y = player.getY();
		Rectangle boxCollider = player.getCollider();
		
		//Bomba piazzata al centro del Tile in cui il Player la piazza
    	int centerX = (x + boxCollider.width / 2) / TILESIZE * TILESIZE;
        int centerY = (y + boxCollider.height / 2) / TILESIZE * TILESIZE;	 
        
		int normalizedX = ((centerX / TILESIZE) * TILESIZE) / TILESIZE;
    	int normalizedY = ((centerY / TILESIZE) * TILESIZE) / TILESIZE;
    	
    	//Se puo' piazzare la Bomba e il Tile corrente e' un FLOOR
    	if (player.getCurrentBombs() > 0 && floor.checkFloor(normalizedX, normalizedY)) {
 
    		Bomb bomb = new Bomb(normalizedX, normalizedY, player.getExplosionRadius());
    		BombView bombView = new BombView();
    		
    		if (floor.addBomb(bomb)) {
    			
    			//Il giocatore puo' piazzare una bomba in meno
    			player.setCurrentBombs(player.getCurrentBombs()-1);
    	
    			bomb.addObserver(bombView);
    			bomb.modified();
    			panel.addBombToDraw(bomb, bombView);
    			
    			//Bomba appena piazzata -> collisioni con la Bomba disattivate fin quando il Player non si allontana abbastanza
    			bomb.setCollisionEnabled(false);
    		}
    	}  	
	}
	
    /**
     * Aggiorna lo stato degli elementi che possono essere piazzati sul pavimento.
     */
	public void updatePlaceable() {
		
		// Rimuovi Tiles esplosi
		Iterator<Tile> tileIterator = floor.getTilesExploded().iterator();
		while (tileIterator.hasNext()) {
		    Tile tile = tileIterator.next();
		    TileView tileView = panel.getTilesMap().get(tile);
		    if (tileView.animationFinished()) {
		    	Tile newTile = new Tile(tile.getX(), tile.getY(), TileType.FLOOR);
		        floor.setCell(newTile);
		        
		    	TileView newTileView = new TileView();
		    	newTile.addObserver(newTileView);
		    	newTile.modified();
		        panel.addTileToDraw(newTile, newTileView);
		        tileIterator.remove();
		    }
		}

		// Rimuovi le Bombe esplose e crea le Esplosioni
		Iterator<Bomb> bombIterator = floor.getBombsPlaced().iterator();
		while (bombIterator.hasNext()) {
		    Bomb bomb = bombIterator.next();
		    if (bomb.isExploded()) {
		        Explosion explosion = new Explosion(bomb, floor); 
			    ExplosionView explosionView = new ExplosionView();
			    explosion.addObserver(explosionView);
			    explosion.modified();
		        floor.addExplosion(explosion);
			    panel.addExplosionToDraw(explosion, explosionView);
		        floor.explodeBomb(explosion);
		        AudioManager.getInstance().play("/audio/bomb-explodes.wav");
		        bombIterator.remove();
		    	//il giocatore può ripiazzare una bomba in più
		    	player.setCurrentBombs(player.getCurrentBombs()+1);
		    }
		}

		// Rimuovi le Esplosioni concluse
		Iterator<Explosion> explosionIterator = floor.getExplosions().iterator();
		while (explosionIterator.hasNext()) {
			
		    Explosion explosion = explosionIterator.next();
		    ExplosionView explosionView = panel.getExplosionsMap().get(explosion);
		    if (explosionView.isFinished()) {
		        explosionIterator.remove();
		    }
		}

		// Rimuovi i PowerUps raccolti
		Iterator<PowerUp> powerUpIterator = floor.getPowerUps().iterator();
		while (powerUpIterator.hasNext()) {
		    PowerUp powerUp = powerUpIterator.next();
		    if (powerUp.isCollected()) {
		        powerUpIterator.remove();
		    }
		}

		// Rimuovi gli Enemies morti con animazione conclusa
		Iterator<Enemy> enemyIterator = floor.getEnemies().iterator();
		while (enemyIterator.hasNext()) {
		    Enemy enemy = enemyIterator.next();
		    EnemyView enemyView = panel.getEnemiesMap().get(enemy);
		    if (!enemy.isAlive() && enemyView.animationFinished()) {
		        enemyIterator.remove();
		    }
		}
	}
	
    /**
     * Aggiorna la posizione del giocatore.
     */
	public void updatePlayer() {
		player.updatePosition();
	}
	
    /**
     * Aggiorna la posizione dei nemici.
     */
	public void updateEnemies() {
		for (Enemy enemy : floor.getEnemies()) {
			enemy.updatePosition();
		}
	}
	
	/**
	 * Aggiorna lo stato del pavimento, del giocatore e dei nemici.
	 */
	public void updateFloor() {
		updatePlaceable();
		updatePlayer();
		updateEnemies();
	}
	
	/**
     * Aggiorna la logica di gioco in base alle condizioni attuali.
     */
	public void update() {
    	if (!player.isAlive() && panel.getPlayerView().animationFinished()) gameOver();
		if (player.isLevelFinished() && panel.getPlayerView().animationFinished()) nextLevel();
		if (panel.isVisible()) {
			updateFloor();
			panel.repaint();
			frame.getHudPanel().repaint();
		}
	}
	 
	/**
     * Avvia il gioco.
     */
	public void startGame() {
		System.out.println("INIZIO LIVELLO: " + level);
		System.out.println("Utente che sta giocando: " + userPlaying);
		AudioManager.getInstance().stop("/audio/main-title.wav");
		AudioManager.getInstance().playLoop("/audio/world1.wav");
		setObservers();
		player.setImmortal();
		gameLoop.start();
	}
	
	/**
	 * Ferma il gioco.
	 */
	public void stopGame() {
		walkTimer.stop();
		AudioManager.getInstance().stop("/audio/game-over.wav");
		AudioManager.getInstance().stop("/audio/world1.wav");
		gameLoop.stop();
	}
	
	/**
	 * Resetta e riavvia il gioco.
	 */
	public void restartGame() {
		
		stopGame();
		
		//Reset mappa di gioco
		floorController.setLevel(level);
		panel.setLevel(level);
		floorController.createMap();

		startGame();
	}
	
	/**
     * Gestisce la situazione di game over.
     */
	public void gameOver() {
		
		stopGame();
		
		//Game Over -> tutti i punti ottenuti in questo livello vengono azzerati
		frame.getHudPanel().setLevelScore(0);
		
		//Schermata di Game Over
		AudioManager.getInstance().play("/audio/game-over.wav");
		buttonController.endGame();
		
		//Update statistiche dell'User 
		userPlaying.addGamesLost();
		LoginDataManager.saveUsers(users);
		
		System.out.println("GAME OVER");
	}
	
	/**
     * Gestisce la situazione di vittoria.
     */
	public void winGame() {
		
		//Schermata di Victory
		buttonController.winGame();
		AudioManager.getInstance().playLoop("/audio/ending.wav");
		
		//Update statistiche dell'User
		int score = frame.getHudPanel().getTotalScore();
		userPlaying.addGamesWon();
		userPlaying.setHighscore(score);
		LoginDataManager.saveUsers(users);
		
		//Partita terminta -> Total Score azzerato in caso si volesse riniziare a giocare
		frame.getHudPanel().setTotalScore(0);
		
		System.out.println("VITTORIA");
	}
	
	/**
     * Gestisce il passaggio al livello successivo nel gioco.
     */
	public void nextLevel() {
		
		//Next level -> Incrementa exp points
		userPlaying.addExpPoints(level);
		
		//Livello incrementato
		level++;
		
		//Next level -> Total Score += Level Score, Level Score azzerato 
		int score = frame.getHudPanel().getTotalScore() + frame.getHudPanel().getLevelScore();
		frame.getHudPanel().setTotalScore(score);
		frame.getHudPanel().setLevelScore(0);
		
		System.out.println("Score: "+ score);
		stopGame();
		
		//Se non si trattava dell'ultimo livello -> Continua a giocare
		if (level < Constants.MAX_LEVEL) {
			new Timer(3000, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//Update statistiche dell'User
					LoginDataManager.saveUsers(users);
					restartGame();
					((Timer) e.getSource()).stop();
				}
			}).start();
		}
		
		//Altrimenti -> Win Game
		else {
			new Timer(3000, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					winGame();
					((Timer) e.getSource()).stop();
				}
			}).start();
		}
	}
	
	/**
     * Imposta gli observers del gioco per aggiornare l'interfaccia utente.
     */
	public void setObservers() {
		
		userPlaying.addObserver(frame.getStatsPanel());
		
		player.addObserver(frame.getHudPanel());
		
		floor.getEnemies().forEach(e -> e.addObserver(frame.getHudPanel()));
		
		initHudPanel(frame.getHudPanel());
	}
	
	/**
     * Imposta le statistiche iniziali per l'HudPanel.
     * 
     * @param hudPanel il pannello di riferimento.
     */
	public void initHudPanel(HudPanel hudPanel) {
		hudPanel.setTotalHp(player.getHp());
		hudPanel.setTotalBombs(player.getBombExplosions());
		hudPanel.setTotalSpeed(player.getSpeed());
		hudPanel.setTotalFire(player.getExplosionRadius());
	}
	/**
     * Imposta le statistiche iniziali per lo StatsPanel.
     * 
     * @param statsPanel il pannello di riferimento.
     */
	public void initStatsPanel(StatsPanel statsPanel) {
		statsPanel.setUsername(userPlaying.getUsername());
		statsPanel.setPath(userPlaying.getAvatarPath());
		statsPanel.setLevel(userPlaying.getLevel());
		statsPanel.setExpPoints(userPlaying.getExpPoints());
		statsPanel.setGamesPlayed(userPlaying.getGamesPlayed());
		statsPanel.setGamesWon(userPlaying.getGamesWon());
		statsPanel.setGamesLost(userPlaying.getGamesLost());
		statsPanel.setHighscore(userPlaying.getHighscore());
		statsPanel.updateStats();
	}
	
	/**
     * Aggiunge un nuovo utente alla mappa degli utenti.
     *
     * @param user L'utente da aggiungere.
     */
	public void addUser(User user) {
		users.put(user.getUsername(), user);
		LoginDataManager.saveUsers(users);
	}
	
	/**
     * Restituisce la mappa degli utenti registrati.
     *
     * @return La mappa degli utenti.
     */
	public Map<String, User> getUsers() {
		return users;
	}
	
	
    /**
     * Restituisce l'utente corrente che sta giocando.
     *
     * @return L'utente corrente che sta giocando.
     */
	public User getUserPlaying() {
		return userPlaying;
	}
	
	/**
     * Imposta l'utente corrente che sta giocando.
     *
     * @param userPlaying L'utente che sta giocando.
     */
	public void setUserPlaying(User userPlaying) {
		this.userPlaying = userPlaying;
		userPlaying.addObserver(frame.getStatsPanel());
		initStatsPanel(frame.getStatsPanel());
	}
	
	 /**
     * Restituisce il livello corrente del gioco.
     *
     * @return Il livello del gioco.
     */
	public int getLevel() {
		return level;
	}
	
	/**
     * Imposta il livello del gioco.
     *
     * @param level Il livello del gioco.
     */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
     * Restituisce l'oggetto {@code Floor} associato al gioco.
     *
     * @return L'oggetto {@code Floor}.
     */
	public Floor getFloor() {
		return floor;
	}
	
	/**
     * Restituisce il timer associato all'animazione del movimento del giocatore.
     *
     * @return Il timer di movimento.
     */
	public Timer getWalkTimer() {
		return walkTimer;
	}
	
	/**
     * Restituisce l'{@code InputController} associato al gioco.
     *
     * @return Il controller degli input.
     */
	public InputController getInputController() {
		return inputController;
	}
	
	/**
	 * Restituisce la cella alla posizione specificata.
	 * Utilizza il metodo della classe {@code Floor}.
	 *
	 * @param i Coordinata y della cella.
	 * @param j Coordinata x della cella.
	 * @return La cella alla posizione specificata.
	 */
	public Tile getCell(int i, int j) {
		return floor.getCell(i, j);
	}

	/**
	 * Restituisce la lista delle bombe piazzate sul terreno di gioco.
	 * Utilizza il metodo della classe {@code Floor}.
	 *
	 * @return La lista delle bombe piazzate sul terreno di gioco.
	 */
    public List<Bomb> getBombsPlaced() {
        return floor.getBombsPlaced();
    }

	/**
	 * Restituisce la lista delle esplosioni presenti sul terreno di gioco.
	 * Utilizza il metodo della classe {@code Floor}.
	 *
	 * @return La lista delle bombe presenti sul terreno di gioco.
	 */
    public List<Explosion> getExplosions() {
        return floor.getExplosions();
    }
    
	/**
	 * Restituisce la lista dei power-up presenti sul terreno di gioco.
	 * Utilizza il metodo della classe {@code Floor}.
	 *
	 * @return La lista dei power-up presenti sul terreno di gioco.
	 */
    public List<PowerUp> getPowerUps() {
        return floor.getPowerUps();
    }

	/**
	 * Restituisce la lista dei nemici presenti sul terreno di gioco.
	 * Utilizza il metodo della classe {@code Floor}.
	 *
	 * @return La lista dei nemici presenti sul terreno di gioco.
	 */
    public List<Enemy> getEnemies() {
        return floor.getEnemies();
    }
}
