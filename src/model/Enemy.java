package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import utilities.Constants;

/**
 * Classe che rappresenta un nemico nel gioco. 
 * Gestisce la posizione, lo stato di animazione, la logica di movimento,
 * i punti vita e l'interazione con gli elementi del gioco.
 * 
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 *
 * @author Lorenzo Zanda
 * @see Observable
 */
public abstract class Enemy extends Observable{
	
	protected long currentTime;
	protected long collisionCooldown;
	protected final int COLLISION_COOLDOWN = 2000;
	
	protected int x, y;
	protected int speed;
	protected int scorePoints;
	protected int hp;
	protected boolean moving;
	protected Move direction;
	protected AnimationState animationState;
	protected boolean alive;
	protected boolean damaged;
	protected Rectangle collider;
	protected Floor floor;
	
	protected final int CHANGE_DIRECTION = 1;

    /**
     * Costruttore utilizzato dalle sottoclassi in quanto la classe {@code Enemy} e' {@code abstract}.
     *
     * @param x      La coordinata x iniziale.
     * @param y      La coordinata y iniziale.
     * @param speed  La velocità di movimento.
     * @param scorePoints I punti guadagnati quando viene sconfitto.
     * @param hp     I punti vita.
     * @param floor  Il terreno di gioco.
     */
	public Enemy(int x, int y, int speed, int scorePoints, int hp, Floor floor) {
		
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.scorePoints = scorePoints;
		this.hp = hp;
		this.floor = floor;
		
		//Enemy Statistics
		moving = true;
		alive = true;
		animationState = AnimationState.WALKING;
		collider = new Rectangle(x, y, Constants.SCALED_TILESIZE-12, Constants.SCALED_TILESIZE-15);
		
		randomDirection();
	}
	
    /**
     * Notifica gli osservatori riguardo alle modifiche del nemico.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
    /**
     * Riduce i punti vita del nemico.
     * Se i punti vita raggiungono zero, il nemico muore.
     */
	public void damage() {
		hp--;
		damaged = true;
		if (hp <= 0) {
			die();
		}
	}
	
	/**
     * Gestisce la morte del nemico. 
     * Imposta lo stato dell'animazione a DYING
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
     * Aggiorna la posizione del nemico in base alla direzione di movimento e rileva collisioni.
     */
	public void updatePosition() {
		
		int dx = direction.getX();
		int dy = direction.getY();
		
		currentTime = System.currentTimeMillis();
		if (currentTime - collisionCooldown >= COLLISION_COOLDOWN) {
			damaged = false;
			if (floor.collidesWithExplosions(collider)) {
				damage();
				collisionCooldown = currentTime;
			}
		}
		
		if (moving) {
	        Rectangle nextCollisionBox = new Rectangle(collider);
	        nextCollisionBox.translate(dx * speed, dy * speed);
	        //Se non vengono rilevate collisioni con Blocchi o Bombe -> Movimento
	        
	        boolean collide = floor.collidesWithEnemies(nextCollisionBox, this)
	    	        	  ||  floor.enemyCollidesWithBombs(nextCollisionBox)
	    	        	  ||  floor.collidesWithBlocks(nextCollisionBox);
	        
	        if (collide || new Random().nextInt(301) <= CHANGE_DIRECTION) {
	        	randomDirection();
	        }
	        
	        else {
	            x += dx * speed;
	            y += dy * speed;
	        }
	        
	        collider.setLocation(x+7, y);
	        
			setChanged();
			notifyObservers(currentTime);
		}	
	}
	
    /**
     * Cambia casualmente la direzione di movimento del nemico.
     * La nuova direzione e' diversa da quella precedentemente seguita dal nemico.
     */
	public void randomDirection() {
		
		List<Move> moves = new ArrayList<Move>(Arrays.asList(Move.values()));
		
		if (direction != null) {
			moves.remove(direction);
		}
		
		int randomIndex = new Random().nextInt(moves.size());
		direction = moves.get(randomIndex);	
	}
	
    /**
     * Interrompe il movimento del nemico.
     */
	public void stop() {
		moving = false;
	}

    /**
     * Ottiene la coordinata x del nemico.
     *
     * @return La coordinata x del nemico.
     */
	public int getX() {
		return x;
	}
	
    /**
     * Imposta la coordinata x del nemico.
     *
     * @param x La nuova coordinata x del nemico.
     */
	public void setX(int x) {
		this.x = x;
	}

    /**
     * Ottiene la coordinata y del nemico.
     *
     * @return La coordinata y del nemico.
     */
	public int getY() {
		return y;
	}
	
    /**
     * Imposta la coordinata y del nemico.
     *
     * @param y La nuova coordinata y del nemico.
     */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Restituisce la velocita' di movimento del nemico.
	 * 
	 * @return velocita' di movimento del nemico.
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Restituisce il numero di punti che il giocatore ottiene una volta ucciso il nemico.
	 * 
	 * @return numero di punti.
	 */
	public int getScorePoints() {
		return scorePoints;
	}

    /**
     * Restituisce i punti vita del nemico.
     *
     * @return I punti vita del nemico.
     */
	public int getHp() {
		return hp;
	}

	/**
	 * Restituisce la direzione corrente del nemico.
	 * 
	 * @return direzione corrente del nemico.
	 */
	public Move getDirection() {
		return direction;
	}
	
    /**
     * Restituisce collider del nemico.
     *
     * @return Il collider del nemico.
     */
	public Rectangle getCollider() {
		return collider;
	}
	
    /**
     * Restituisce lo stato di animazione del nemico.
     *
     * @return Lo stato di animazione del nemico.
     */
	public AnimationState getAnimationState() {
		return animationState;
	}
	
    /**
     * Restituisce lo stato di danneggiamento del nemico.
     *
     * @return {@code true} se il nemico e' stato danneggiato, {@code false} altrimenti.
     */
	public boolean isDamaged() {
		return damaged;
	}

    /**
     * Restituisce lo stato di vita del nemico.
     *
     * @return {@code true} se il nemico e' vivo, {@code false} altrimenti.
     */
	public boolean isAlive() {
		return alive;
	}
	
    /**
     * Restituisce una rappresentazione testuale del nemico.
     *
     * @return Una stringa contenente le coordinate x e y del nemico.
     */
	@Override
	public String toString() {
		return x + " " + y;
	}
	
    /**
     * Verifica se due nemici sono uguali confrontando le loro coordinate.
     *
     * @param obj L'oggetto da confrontare con questo nemico.
     * @return {@code true} se i nemici sono uguali, {@code false} altrimenti.
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Enemy otherEnemy = (Enemy) obj;
	    return x == otherEnemy.getX() && y == otherEnemy.getY();
	}
}
