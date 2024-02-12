package model;

import java.util.Observable;

/**
 * Classe che rappresenta una cella all'interno del terreno di gioco.
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 * 
 * @author Lorenzo Zanda
 * @see Observable
 */
public class Tile extends Observable{

	private int x, y;
	private TileType type;
	private boolean hit;
	
    /**
     * Costruisce un nuovo oggetto {@code Tile} con le coordinate e il tipo specificati.
     *
     * @param x    La coordinata x della cella.
     * @param y    La coordinata y della cella.
     * @param type Il tipo di cella.
     */
	public Tile(int x, int y, TileType type) {
		
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	
    /**
     * Notifica gli osservatori riguardo alle modifiche del tile.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
    /**
     * Restituisce la coordinata x della cella.
     *
     * @return La coordinata x della cella.
     */
	public int getX() {
		return x;
	}
	
    /**
     * Imposta la coordinata x della cella.
     *
     * @param x La nuova coordinata x della cella.
     */
	public void setX(int x) {
		this.x = x;
	}
	
    /**
     * Restituisce la coordinata y della cella.
     *
     * @return La coordinata y della cella.
     */
	public int getY() {
		return y;
	}
	
    /**
     * Imposta la coordinata y della cella.
     *
     * @param y La nuova coordinata y della cella.
     */
	public void setY(int y) {
		this.y = y;
	}

    /**
     * Restituisce il tipo di cella.
     *
     * @return Il tipo di cella.
     */
	public TileType getType() {
		return type;
	}

    /**
     * Imposta il tipo di cella.
     * 
     * @param type Il nuovo tipo di cella.
     */
	public void setType(TileType type) {
		this.type = type;
	}
	
    /**
     * Restituisce lo stato della cella colpita.
     *
     * @return {@code true} se la cella è stata colpita, {@code false} altrimenti.
     */
	public boolean isHit() {
		return hit;
	}
	
    /**
     * Imposta lo stato della cella colpita.
     *
     * @param hit Il nuovo stato della cella.
     */
	public void setHit(boolean hit) {
		this.hit = hit;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Restituisce una rappresentazione testuale della cella.
	 * 
	 * @return Una stringa contenente le coordinate e il tipo della cella.
	 */
	@Override
	public String toString() {
		return type + "("+x+", "+y+")";
	}
	
    /**
     * Verifica se due celle sono uguali confrontando le loro coordinate e tipo.
     *
     * @param obj L'oggetto da confrontare con questa cella.
     * @return {@code true} se le celle sono uguali, {@code false} altrimenti.
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Tile otherTile = (Tile) obj;
	    return x == otherTile.getX() && y == otherTile.getY() && type == otherTile.getType();
	}
}
