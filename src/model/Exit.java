package model;

import java.awt.Rectangle;
import java.util.Observable;
import utilities.Constants;

/**
 * Classe che rappresenta l'uscita di un livello nel gioco. 
 * Questa classe gestisce la posizione e la collisione dell'uscita.
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 * 
 * @author Lorenzo Zanda
 * @see Observable
 */
public class Exit extends Observable {
	
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	private int x, y;
	private Rectangle collider;
	
	/**
	 * Costruisce un nuovo oggetto {@code Exit} con la posizione specificata.
	 * 
	 * @param x La coordinata x dell'uscita.
	 * @param y La coordinata y dell'uscita.
	 */
	public Exit(int x, int y) {
		this.x = x;
		this.y = y;
		collider = new Rectangle(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
	}
	
    /**
     * Notifica gli osservatori riguardo alle modifiche dell'uscita.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Restituisce la coordinata x dell'uscita.
	 * 
	 * @return La coordinata x dell'uscita.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Restituisce la coordinata y dell'uscita.
	 * 
	 * @return La coordinata y dell'uscita.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Restituisce il collider dell'uscita.
	 * 
	 * @return Il collider dell'uscita.
	 */
	public Rectangle getCollider() {
		return collider;
	}

}
