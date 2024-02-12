package model;

import java.awt.Rectangle;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import utilities.Constants;

/**
 * Classe che rappresenta una bomba piazzata dal giocatore.
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 *
 * La bomba ha un timer che determina quando esplodera', generando un'esplosione
 * che colpisce le celle vicine in base al raggio di esplosione della bomba.
 *
 * @author Lorenzo Zanda
 * @see Observable
 */
public class Bomb extends Observable {
	
	private int x, y, explosionRadius;
	private int timeToExplode;
	private boolean exploded;
	private Rectangle bombCollider;
	private boolean collisionEnabled = true;
	private Timer timer = new Timer();
	
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	 /**
     * Costruisce un nuovo oggetto {@code Bomb} con le coordinate e il raggio di esplosione specificati.
     *
     * @param x              La coordinata x della posizione della bomba.
     * @param y              La coordinata y della posizione della bomba.
     * @param explosionRadius Il raggio di esplosione della bomba.
     */
	public Bomb(int x, int y, int explosionRadius) {
		this.x = x;
		this.y = y;
		this.explosionRadius = explosionRadius;
		timeToExplode = 3;
		bombCollider = new Rectangle(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
	}
	
    /**
     * Notifica gli osservatori riguardo alle modifiche della bomba.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
    /**
     * Programma l'esplosione della bomba dopo un certo periodo di tempo.
     */
	public void tick() {
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				explode();
				
			}
		}, timeToExplode * 800);
	}
	
    /**
     * Espode la bomba, notifica gli osservatori e interrompe il timer.
     */
	public void explode() {
		
		timer.cancel();
	    exploded = true;
	    modified();
	}
	
	/**
	 * Abilita la collisione con la bomba se non collide con il collider del {@code Player}. 
	 * Inizialmente le collisioni sono disabilitate per permettere al {@code Player} di sostare sulla bomba.
	 * 
	 * @param playerCollider Il collider del {@code Player}.
	 */
	public void enableCollision(Rectangle playerCollider) {
		
		if (!collisionEnabled && !bombCollider.intersects(playerCollider)) {
			collisionEnabled = true;
		}
	}
	
    /**
     * Restituisce la coordinata x della bomba.
     *
     * @return La coordinata x della bomba.
     */
	public int getX() {
		return x;
	}
	
    /**
     * Restituisce la coordinata y della bomba.
     *
     * @return La coordinata y della bomba.
     */
	public int getY() {
		return y;
	}
	
    /**
     * Restituisce lo stato di esplosione della bomba.
     *
     * @return {@code true} se la bomba e' esplosa, {@code false} altrimenti.
     */
	public boolean isExploded() {
		return exploded;
	}
	
    /**
     * Restituisce il raggio di esplosione della bomba.
     *
     * @return Il raggio di esplosione.
     */
	public int getExplosionRadius() {
		return explosionRadius;
	}
	
    /**
     * Restituisce collider della bomba.
     *
     * @return Il collider della bomba.
     */
	public Rectangle getCollider() {
		return bombCollider;
	}
	
    /**
     * Restituisce lo stato di abilitazione della collisione della bomba.
     *
     * @return {@code true} se la collisione è abilitata, {@code false} altrimenti.
     */
	public boolean getCollisionEnabled() {
		return collisionEnabled;
	}
	
    /**
     * Imposta lo stato di abilitazione della collisione della bomba.
     *
     * @param collisionEnabled {@code true} per abilitare la collisione, {@code false} altrimenti.
     */
	public void setCollisionEnabled(boolean collisionEnabled) {
		this.collisionEnabled = collisionEnabled;
	}

    /**
     * Verifica se due bombe sono uguali confrontando le loro coordinate.
     *
     * @param obj L'oggetto da confrontare con questa bomba.
     * @return {@code true} se le bombe sono uguali, {@code false} altrimenti.
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Bomb otherBomb = (Bomb) obj;
	    return x == otherBomb.getX() && y == otherBomb.getY();
	}
}
