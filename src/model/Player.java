package model;

import java.awt.Rectangle;
import java.util.Observable;
import controller.AudioManager;
import utilities.Constants;

/**
 * Classe che rappresenta il personaggio controllato dal giocatore.
 * Questa classe gestisce il movimento del giocatore, le statistiche e interagisce
 * con il resto del gioco.
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 * Utilizza il pattern "singleton" per garantire un'unica istanza globale.
 * 
 * @author Lorenzo Zanda
 * @see Observable
 */
public class Player extends Observable {
	
	private static Player instance;
	
	private long currentTime;
	private long collisionCooldown;
	private final long COLLISION_COOLDOWN = 6000;
	
	private int x;
	private int y;
	private int hp;
	private int playerCenterX;
	private int playerCenterY;
	private int bombExplosions;
	private int currentBombs;
	private int speed;
	private int explosionRadius;
	private Move direction;
	private Rectangle boxCollider;
	private boolean moving;
	private boolean alive;
	private boolean damaged;
	
	private AnimationState animationState;
	
	private boolean levelFinished;
	private Floor floor;
	
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
    /**
     * Restituisce l'istanza singola di {@code Player} (singleton pattern).
     *
     * @return L'istanza di Player.
     */
	public static Player getInstance() {
		if (instance == null)
			instance = new Player();
		return instance;
	}
	
    /**
     * Costruttore privato della classe {@code Player}.
     */
	private Player() {
		setValues();
	}
	
	/**
	 * Inizializza i valori predefiniti per il {@code Player}.
	 */
	public void setValues() {
		
		this.x = TILESIZE;
		this.y = TILESIZE;
		
		//Player Statistics
		alive = true;
		moving = false;
		levelFinished = false;
		
		hp = 5;
		speed = 3;
		bombExplosions = 1;
		explosionRadius = 1;
		currentBombs = bombExplosions;
		direction = Move.DOWN;
		animationState = AnimationState.IDLE;
		boxCollider = new Rectangle(x, y, TILESIZE, TILESIZE);
	}
	
	/**
	 * Rende il giocatore immortale
	 */
	public void setImmortal() {
		collisionCooldown = System.currentTimeMillis();
		damaged = true;
	}
	
    /**
     * Notifica gli osservatori riguardo alle modifiche del giocatore.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
    /**
     * Si occupa di impostare correttamente la direzione corrente in modo che il personaggio si muova verso il basso.
     * Imposta lo stato dell'animazione a {@code WALKING}.
     */
	public void moveDown() {
		
		moving = true;
		direction = Move.DOWN;
		animationState = AnimationState.WALKING;
	}
	
    /**
     * Si occupa di impostare correttamente la direzione corrente in modo che il personaggio si muova verso sinistra.
     * Imposta lo stato dell'animazione a {@code WALKING}.
     */
	public void moveLeft() {
		
		moving = true;
		direction = Move.LEFT;
		animationState = AnimationState.WALKING;
	}
	
    /**
     * Si occupa di impostare correttamente la direzione corrente in modo che il personaggio si muova verso destra.
     * Imposta lo stato dell'animazione a {@code WALKING}.
     */
	public void moveRight() {
		
		moving = true;
		direction = Move.RIGHT;
		animationState = AnimationState.WALKING;
		
	}
	
    /**
     * Si occupa di impostare correttamente la direzione corrente in modo che il personaggio si muova verso l'alto.
     * Imposta lo stato dell'animazione a {@code WALKING}.
     */
	public void moveUp() {
		
		moving = true;
	    direction = Move.UP;
		animationState = AnimationState.WALKING;
	}
	
    /**
     * Ferma il movimento del giocatore.
     * Imposta lo stato dell'animazione a {@code IDLE}.
     */
	public void stop() {
		
		moving = false;
		animationState = AnimationState.IDLE;
	}
	
    /**
     * Riduce i punti vita del giocatore e gestisce gli effetti collaterali come la diminuzione della velocità.
     * Se i punti vita raggiungono zero, il giocatore muore.
     */
	public void damage() {
		
		if (!levelFinished) {
			//Quando il Player viene danneggiato -> hp-1
			hp--;
			modified();
			
			//speed-1 se la speed e' > 3
			if (speed > 3)  {
				speed--;
				modified();
			}
			
			damaged = true;
			
			// Morte
			if (hp <= 0) {
				AudioManager.getInstance().play("/audio/bomberman-dies.wav");
				die();
			}
		}
	}
	
	/**
	 * Gestisce la morte del giocatore.
	 * Imposta lo stato dell'animazione a {@code DYING}
	 */
	public void die() {
		if (alive) {
			stop();
			animationState = AnimationState.DYING;
			alive = false;
			modified();
		}
	}
	
    /**
     * Completa il livello corrente.
     * Imposta lo stato dell'animazione a {@code WINNING}
     */
	public void finishLevel() {
		if (alive) {
			stop();
			animationState = AnimationState.WINNING;
			levelFinished = true;
		}
	}
	
    /**
     * Aggiorna la posizione del giocatore sulla mappa, gestendo collisioni e interazioni con oggetti come uscita, nemici e power-up.
     */
	public void updatePosition() {
		
    	playerCenterX = (x + boxCollider.width / 2) / TILESIZE * TILESIZE;
        playerCenterY = (y + boxCollider.height / 2) / TILESIZE * TILESIZE;
		
        //Collisione con l'uscita
		if (floor.collidesWithExit(boxCollider)) {
			if (!levelFinished) {
				AudioManager.getInstance().stop("/audio/world1.wav");
				AudioManager.getInstance().play("/audio/stage-clear.wav");
			}
			finishLevel();
		}
		
		
		//Sistema di cooldown dopo aver subito una hit da Enemy o Explosion
		currentTime = System.currentTimeMillis();
		if (currentTime - collisionCooldown >= COLLISION_COOLDOWN) {
			damaged = false;
			if (floor.collidesWithEnemies(boxCollider, null) || floor.collidesWithExplosions(boxCollider)) {
				damage();
				collisionCooldown = currentTime;
			}
		}
        
	    //Collisione con PowerUp
	    PowerUp powerUp = floor.collidesWithPowerUps(boxCollider);
        if (powerUp != null) {
        	AudioManager.getInstance().play("/audio/item-get.wav");
        	handlePowerUpEffect(powerUp);
        }
	    
	    int dx = direction.getX();
	    int dy = direction.getY();
	    
        //Sistema di movimento
	    if (moving) {
	        Rectangle nextCollisionBox = new Rectangle(boxCollider);
	        nextCollisionBox.translate(dx * speed, dy * speed);
	        //Se non vengono rilevate collisioni con Blocchi o Bombe -> Movimento
	        if (!floor.playerCollidesWithBombs(nextCollisionBox) 
	         && !floor.collidesWithBlocks(nextCollisionBox)) {
	            x += dx * speed;
	            y += dy * speed;
	        }
	        
	        //Altrimenti -> Player messo al centro del Tile in cui si trova (snap to grid)
	        else {	        
	            
	            x = playerCenterX;
	            y = playerCenterY;
	        }
	        boxCollider.setLocation(x, y);
	    }
	    
	    if (alive) {
		    setChanged();
		    notifyObservers(currentTime);
	    }
	}

	
    /**
     * Gestisce gli effetti dei power-up raccolti dal giocatore.
     * Aggiorna le statistiche del giocatore in base al tipo di power-up.
     * Notifica gli osservatori ({@code Observer}) in caso di aggiornamenti delle statistiche.
     *
     * @param powerUp Il power-up raccolto dal giocatore.
     */
	public void handlePowerUpEffect(PowerUp powerUp) {
		
		//Gestione PowerUp collezionati
		PowerUpType type = powerUp.getType();
		
		switch(type) {
			case BOMBUP -> {
				if (bombExplosions < Constants.MAX_BOMBS) {
					bombExplosions++;
					currentBombs++;
				}
			}
			case ACCELERATOR  -> {
				if (speed < Constants.MAX_SPEED) speed++;
			}
			case FIRE   -> {
				if (explosionRadius < Constants.MAX_RADIUS) explosionRadius++;
			}
			case BOMBERMAN  -> {
				if (hp < Constants.MAX_HP) hp++;
			}
			default -> {}
		}
		
		// Update Statistics
		setChanged();
		notifyObservers(powerUp);
	}
	
	/**
	 * Restituisce la coordinata x del giocatore.
	 *
	 * @return La coordinata x del giocatore.
	 */
	public int getX() {
		return x;
	}
	
    /**
     * Imposta la coordinata x del giocatore.
     *
     * @param x La nuova coordinata x del giocatore.
     */
	public void setX(int x) {
		this.x = x;
	}
	
    /**
     * Imposta il terreno di gioco.
     *
     * @param floor Il terreno di gioco.
     */
	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	/**
	 * Restituisce la coordinata y del giocatore.
	 *
	 * @return La coordinata y del giocatore.
	 */
	public int getY() {
		return y;
	}
	
    /**
     * Imposta la coordinata y del giocatore.
     *
     * @param y La nuova coordinata y del giocatore.
     */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Restituisce il numero corrente di bombe piazzabili.
	 * 
	 * @return il numero corrente di bombe.
	 */
	public int getCurrentBombs() {
		return currentBombs;
	}
	
	/**
	 * Imposta il numero corrente di bombe piazzabili.
	 * 
	 * @param currentBombs il numero corrente di bombe piazzabili.
	 */
	public void setCurrentBombs(int currentBombs) {
		this.currentBombs = currentBombs;
	}
	
    /**
     * Restituisce il massimo numero di bombe piazzabili dal giocatore.
     *
     * @return il numero massimo di bombe.
     */
	public int getBombExplosions() {
		return bombExplosions;
	}
	
    /**
     * Restituisce il raggio di esplosione delle bombe piazzabili dal giocatore.
     *
     * @return il raggio di esplosione delle bombe.
     */
	public int getExplosionRadius() {
		return explosionRadius;
	}
	
    /**
     * Restituisce i punti vita del giocatore.
     *
     * @return I punti vita del giocatore.
     */
	public int getHp() {
		return hp;
	}

    /**
     * Restituisce la velocita' del giocatore.
     *
     * @return la velocita'.
     */
	public int getSpeed() {
		return speed;
	}	
	
    /**
     * Restituisce collider del giocatore.
     *
     * @return Il collider del giocatore.
     */
	public Rectangle getCollider() {
		return boxCollider;
	}
	
    /**
     * Restituisce lo stato di danneggiamento del giocatore.
     *
     * @return {@code true} se il giocatore e' stato danneggiato, {@code false} altrimenti.
     */
	public boolean isDamaged() {
		return damaged;
	}
	
    /**
     * Restituisce lo stato di vita del giocatore.
     *
     * @return {@code true} se il giocatore e' vivo, {@code false} altrimenti.
     */
	public boolean isAlive() {
		return alive;
	}
	
    /**
     * Restituisce un booleano che indica se il giocatore ha raggiunto o no la fine del livello.
     *
     * @return {@code true} se il giocatore ha raggiunto l'uscita ({@code Exit}), {@code false} altrimenti.
     */
	public boolean isLevelFinished() {
		return levelFinished;
	}
	
    /**
     * Restituisce lo stato di animazione del giocatore.
     *
     * @return Lo stato di animazione del giocatore.
     */
	public AnimationState getAnimationState() {
		return animationState;
	}
	
    /**
     * Imposta lo stato di animazione del giocatore.
     *
     * @param animationState Il nuovo stato di animazione del giocatore.
     */
	public void setAnimationState(AnimationState animationState) {
		this.animationState = animationState;
	}
	
	/**
	 * Restituisce la direzione corrente del giocatore.
	 * 
	 * @return direzione corrente del giocatore.
	 */
	public Move getDirection() {
		return direction;
	}
	
    /**
     * Imposta la direzione di movimento del giocatore.
     *
     * @param move La nuova direzione di movimento del giocatore.
     */
	public void setDirection(Move move) {
		this.direction = move;
	}
}
