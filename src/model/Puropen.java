package model;

/**
 * Classe che rappresenta un nemico specifico di tipo "Puropen". Estende la classe astratta Enemy.
 *
 * @author Lorenzo Zanda
 * @see Enemy
 */
public class Puropen extends Enemy {
	
	private static final int SPEED = 2;
	private static final int SCORE_POINTS = 100;
	private static final int HP = 1;

    /**
     * Costruisce un nemico di tipo "Puropen" con posizione iniziale, velocità, punti e punti vita specificati.
     *
     * @param x     La coordinata x iniziale.
     * @param y     La coordinata y iniziale.
     * @param floor Il terreno di gioco.
     */
	public Puropen(int x, int y, Floor floor) {
		super(x, y, SPEED, SCORE_POINTS, HP, floor);
	}
	
    /**
     * Restituisce una rappresentazione testuale del Puropen che usa la rappresentazione testuale della superclasse.
     *
     * @return Una stringa contenente le coordinate x e y del Puropen.
     */
	@Override
	public String toString() {
		return "Puropen: " + super.toString();
	}
}
