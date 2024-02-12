package model;

/**
 * Classe che rappresenta un nemico specifico di tipo "Denkyun". Estende la classe astratta {@code Enemy}.
 *
 * @author Lorenzo Zanda
 * @see Enemy
 */
public class Denkyun extends Enemy {
	
	private static final int SPEED = 2;
	private static final int SCORE_POINTS = 400;
	private static final int HP = 2;

    /**
     * Costruisce un nemico di tipo "Denkyun" con posizione iniziale, velocità, punti e punti vita specificati.
     *
     * @param x     La coordinata x iniziale.
     * @param y     La coordinata y iniziale.
     * @param floor Il terreno di gioco.
     */
	public Denkyun(int x, int y, Floor floor) {
		super(x, y, SPEED, SCORE_POINTS, HP, floor);
	}
	
    /**
     * Restituisce una rappresentazione testuale del Denkyun che usa la rappresentazione testuale della superclasse.
     *
     * @return Una stringa contenente le coordinate x e y del Denkyun.
     */
	@Override
	public String toString() {
		return "Denkyun: " + super.toString();
	}
}
