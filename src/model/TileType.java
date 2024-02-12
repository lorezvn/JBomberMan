package model;

/**
 * Enumerazione che rappresenta i diversi tipi di celle nel terreno di gioco.
 * 
 * @author Lorenzo Zanda
 */
public enum TileType {
	
    /**
     * Tipo di cella rappresentante il pavimento libero di ostacoli.
     */
	FLOOR, 
    /**
     * Tipo di cella rappresentante un blocco distruttibile.
     */
	BREAKABLE, 
    /**
     * Tipo di cella rappresentante un blocco indistruttibile.
     */
	UNBREAKABLE;
}
