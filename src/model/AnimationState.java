package model;

/**
 * Enumerazione che rappresenta gli stati di animazione possibili
 * per un personaggio.
 *
 * @author Lorenzo Zanda
 */
public enum AnimationState {
	
	/**
	 * Stato di idling
	 */
	IDLE, 
	/**
	 * Stato di movimento
	 */
	WALKING, 
	/**
	 * Stato di morte
	 */
	DYING, 
	/**
	 * Stato di danneggiamento
	 */
	HIT, 
	/** 
	 * Stato di vittoria
	 */
	WINNING;
}
