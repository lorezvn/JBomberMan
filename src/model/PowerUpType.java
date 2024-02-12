package model;

/**
 * Enumerazione che rappresenta i diversi tipi di power-up nel gioco.
 * 
 * @author Lorenzo Zanda
 */
public enum PowerUpType {
	
    /**
     * Power-up "Ice Cream Cone" con 1% di percentuale di spawn e 50000 punti associati.
     */
	ICE_CREAM_CONE(1, 50000), 
    /**
     * Power-up "Apple" con 3% di percentuale di spawn e 8000 punti associati.
     */
	APPLE(3, 8000), 
    /**
     * Power-up "Rice Ball" con 3.5% di percentuale di spawn e 5000 punti associati.
     */
	RICE_BALL(3.5, 5000),  
    /**
     * Power-up "Bomberman" con 7.5% di percentuale di spawn e 500 punti associati.
     */
	BOMBERMAN(7.5, 500), 
    /**
     * Power-up "Fire" con 15% di percentuale di spawn e 200 punti associati.
     */
	FIRE(15, 200), 
    /**
     * Power-up "Accelerator" con 25% di percentuale di spawn e 400 punti associati.
     */
	ACCELERATOR(25, 400), 
    /**
     * Power-up "Bomb Up" con 45% di percentuale di spawn e 10 punti associati.
     */
	BOMBUP(45, 10);
	
	private double spawnPercent;
	private int scorePoints;
	
	/**
	 * Costruttore privato per inizializzare i tipi di power-up con la percentuale di spawn e ii punti associati.
	 * 
	 * @param spawnPercent Percentuale di spawn del power-up.
	 * @param scorePoints Punti associati al power-up.
	 */
	private PowerUpType(double spawnPercent, int scorePoints) {
		this.spawnPercent = spawnPercent;
		this.scorePoints = scorePoints;
	}
	
    /**
     * Restituisce la percentuale di spawn del power-up.
     * 
     * @return Percentuale di spawn del power-up.
     */
	public double getSpawnPercent() {
		return spawnPercent;
	}
	
    /**
     * Restituisce i punti associati al power-up.
     * @return Punti associati al power-up.
     */
	public int getScorePoints() {
		return scorePoints;
	}
}
