package utilities;

/**
 * Classe contenente costanti utilizzate nel gioco.
 * 
 * @author Lorenzo Zanda
 */
public class Constants {
	
	/**
	 * Frame per secondo.
	 */
	public static final int FPS = 60;
	/**
	 * Dimensioni della cella.
	 */
	public static final int TILESIZE = 16;
	/**
	 * Righe del terreno di gioco.
	 */
	public static final int ROWS = 13;
	/**
	 * Colonne del terreno di gioco.
	 */
	public static final int COLS = 17;
	/**
	 * Fattore di scaling.
	 */
	public static final int SCALE = 3;
	/**
	 * Dimensioni della cella con applicato il fattore di scaling.
	 */
	public static final int SCALED_TILESIZE = TILESIZE * SCALE;
	/**
	 * Altezza usata per i pannelli.
	 */
	public static final int HEIGHT = SCALED_TILESIZE * ROWS;
	/**
	 * Larghezza usata per i pannelli.
	 */
	public static final int WIDTH = SCALED_TILESIZE  * COLS;
	/**
	 * Massimo raggio di esplosione.
	 */
	public static final int MAX_RADIUS = 4;
	/**
	 * Massimo numero di bombe piazzabili.
	 */
	public static final int MAX_BOMBS = 6;
	/**
	 * Massimo numero di punti vita.
	 */
	public static final int MAX_HP = 9;
	/**
	 * Massima velocita'.
	 */
	public static final int MAX_SPEED = 6;
	/**
	 * Livello massimo raggiungibile.
	 */
	public static final int MAX_LEVEL = 3;
	/**
	 * Array contenente un valore intero che corrisponde al numero di nemici per livello.
	 */
	public static final int[] NUMBER_OF_ENEMIES = new int[] {3, 4, 5};
}
