package view;

/**
 * Enumerazione che rappresenta diverse immagini utilizzate nell'animazione delle esplosioni delle bombe.
 * Ogni costante in questa enumerazione corrisponde a una specifica direzione dell'immagine per l'esplosione.
 * 
 * @author Lorenzo Zanda
 */
public enum ExplosionImage {
	/**
	 * L'immagine centrale.
	 */
	CENTRAL(0), 
	/**
	 * L'immagine sinistra.
	 */
	LEFT(1), 
	/**
	 * L'immagine destra.
	 */
	RIGHT(2), 
	/**
	 * L'immagine in alto.
	 */
	UP(3), 
	/**
	 * L'immagine in basso.
	 */
	DOWN(4),
	/**
	 * L'immagine che collega l' immagine centrale a quella a sinistra.
	 */
	C_LEFT(5), 
	/**
	 * L'immagine che collega l' immagine centrale a quella a destra.
	 */
	C_RIGHT(6), 
	/**
	 * L'immagine che collega l' immagine centrale a quella in alto.
	 */
	C_UP(7), 
	/**
	 * L'immagine che collega l' immagine centrale a quella in basso.
	 */
	C_DOWN(8);
	
	private int value;
	
    /**
     * Costruttore privato per inizializzare le immagini con un valore intero.
     *
     * @param value Il valore intero associato all'immagine.
     */
	private ExplosionImage(int value) {
		this.value = value;
	}
	
    /**
     * Restituisce il valore intero associato all'immagine dell'esplosione.
     *
     * @return Il valore intero associato all'immagine dell'esplosione.
     */
	public int getValue() {
		return value;
	}
}