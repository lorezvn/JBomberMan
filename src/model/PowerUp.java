package model;

import java.awt.Rectangle;
import java.util.Observable;
import utilities.Constants;

/** Classe che rappresenta un power-up nel gioco.
 * 
 * Un power-up e' un oggetto bonus che il giocatore puo' raccogliere nel gioco per ottenere potenziamenti e/o punteggio.
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 * 
 * @author Lorenzo Zanda
 * @see Observable
 */
public class PowerUp extends Observable {
	
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	private int x;
	private int y;
	private Rectangle collider;
	private PowerUpType type;
	private boolean collected = false;
	
	/**
	 * Costruisce un nuovo oggetto {@code PowerUp} con la posizione iniziale specificata.
	 * 
	 * @param x coordinata x del power-up.
	 * @param y coordinata y del power-up.
	 */
	public PowerUp(int x, int y) {
		this.x = x;
		this.y = y;
		collider = new Rectangle(x * TILESIZE, y * TILESIZE, TILESIZE, TILESIZE);
		generateRandomType();
	}
	
    /**
     * Notifica gli osservatori riguardo alle modifiche del power-up.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Genera casualmente il tipo di power-up.
	 */
	public void generateRandomType() {
		double randomValue = Math.random() * 100;
		double cumulativeProbability = 0;
		for (PowerUpType type : PowerUpType.values()) {
			cumulativeProbability += type.getSpawnPercent();
			if (randomValue <= cumulativeProbability) {
				this.type = type;
				break;
			}
		}
	}

	/**
	 * Restituisce la coordinata x del power-up.
	 * 
	 * @return la coordinata x del power-up.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Restituisce la coordinata y del power-up.
	 * 
	 * @return la coordinata y del power-up.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Restituisce il collider del power-up.
	 * 
	 * @return il collider del power-up.
	 */
	public Rectangle getCollider() {
		return collider;
	}

	/**
	 * Restituisce il tipo del power-up.
	 * 
	 * @return il tipo del power-up.
	 */
	public PowerUpType getType() {
		return type;
	}
	
	/**
	 * Restituisce la percentuale di spawn del power-up.
	 * 
	 * @return la percentuale di spawn del power-up.
	 */
	public double getSpawnPercent() {
		return type.getSpawnPercent();
	}
	
	/**
	 * Restituisce il punteggio associato al power-up in base al tipo.
	 * 
	 * @return i punti del punteggio associati al power-up.
	 */
	public int getScorePoints() {
		return type.getScorePoints();
	}
	
	/**
	 * Verifica se il power-up è stato raccolto.
	 * 
	 * @return {@code true} se il power-up è stato raccolto, {@code false} altrimenti.
	 */
	public boolean isCollected() {
		return collected;
	}
	
	/**
	 * Imposta lo stato di raccolta del power-up.
	 * 
	 * @param collected {@code true} se il power-up è stato raccolto, {@code false} altrimenti.
	 */
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	
	/**
	 * Restituisce una rappresentazione testuale del power-up.
	 * 
	 * @return Una stringa contenente le coordinate x e y del power-up.
	 */
	@Override
	public String toString() {
		return type + "("+x+", "+y+")";
	}
	
    /**
     * Verifica se due power-up sono uguali confrontando le loro coordinate.
     *
     * @param obj L'oggetto da confrontare con questo power-up.
     * @return {@code true} se i power-up sono uguali, {@code false} altrimenti.
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    PowerUp otherPowerUp = (PowerUp) obj;
	    return x == otherPowerUp.getX() && y == otherPowerUp.getY();
	}
}
