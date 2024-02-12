package model;

/**
 * Enumerazione che rappresenta tutte le direzioni possibili in cui un personaggio può muoversi.
 * Ad ogni costante sono associati coordinate x e y che descrivono meglio in che modo il personaggio deve comportarsi 
 * a seconda della direzione presa.
 * 
 * @author Lorenzo Zanda
 */
public enum Move {
    /**
     * Rappresenta il movimento verso sinistra.
     */
	LEFT(-1, 0), 
    /**
     * Rappresenta il movimento verso l'alto.
     */
	UP(0, -1), 
    /**
     * Rappresenta il movimento verso destra.
     */
	RIGHT(1, 0), 
    /**
     * Rappresenta il movimento verso il basso.
     */
	DOWN(0, 1);
	
	private int x, y;
	
    /**
     * Costruttore privato per inizializzare le direzioni con le coordinate x e y.
     *
     * @param x La coordinata x associata alla direzione di movimento.
     * @param y La coordinata y associata alla direzione di movimento.
     */
	private Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
    /**
     * Restituisce la coordinata x associata alla direzione di movimento.
     *
     * @return La coordinata x associata alla direzione di movimento.
     */
	public int getX() {
		return x;
	}
	
    /**
     * Restituisce la coordinata y associata alla direzione di movimento.
     *
     * @return La coordinata y associata alla direzione di movimento.
     */
	public int getY() {
		return y;
	}
}
