package model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
/**
 * Classe che rappresenta l'esplosione generata da una bomba esplosa.
 * Estende la classe {@code Observable} per notificare gli osservatori riguardo le sue modifiche.
 *
 * @author Lorenzo Zanda
 * @see Observable
 */
public class Explosion extends Observable{
	
	private Bomb bomb;
	private Floor floor;
	private Set<Point> explosionTiles;
	
	/**
	 * Crea un nuovo oggetto {@code Explosion} associato alla bomba appena esplosa.
	 * 
	 * @param bomb La bomba che ha generato l'esplosione.
	 * @param floor Il terreno di gioco in cui si verifica l'esplosione.
	 */
	public Explosion(Bomb bomb, Floor floor) {
		this.bomb = bomb;
		this.floor = floor;
		explosionTiles = new HashSet<Point>();
		
		explosionTiles.add(new Point(bomb.getX(), bomb.getY()));
		obtainExplosionTiles();
	}
	
    /**
     * Notifica gli osservatori riguardo alle modifiche dell'esplosione.
     */
	public void modified() {
		setChanged();
		notifyObservers();
	}
	
    /**
     * Ottiene le celle coinvolte nell'esplosione in base alla posizione e al raggio della bomba.
     * Rimuove i power-up che si trovano sulle celle coinvolte.
     */
	public void obtainExplosionTiles() {
	    
	    int x = bomb.getX();
	    int y = bomb.getY();
	    int radius = bomb.getExplosionRadius();
	    
	    addExplosionTiles(x-1, y, radius, -1, 0); 	//left
	    addExplosionTiles(x+1, y, radius, +1, 0);	//right
	    addExplosionTiles(x, y-1, radius, 0, -1);	//up
	    addExplosionTiles(x, y+1, radius, 0, +1);	//down
	    floor.removePowerUps(this);	
	}

    /**
     * Metodo di utilita' per aggiungere le celle coinvolte nell'esplosione in una direzione specifica.
     *
     * @param x      La coordinata x di partenza.
     * @param y      La coordinata y di partenza.
     * @param radius Il raggio dell'esplosione.
     * @param dx     La variazione in x per ogni passo.
     * @param dy     La variazione in y per ogni passo.
     */
	private void addExplosionTiles(int x, int y, int radius, int dx, int dy) {
		
		while (radius > 0) {
			if (x >= 0 && x < floor.getWidth() && y >= 0 && y < floor.getHeight()) {
				Tile tile = floor.getCell(y, x);
				
				if (tile.getType() == TileType.BREAKABLE || tile.getType() == TileType.UNBREAKABLE) {
					break;
				}
				
				//Altrimenti -> e' un FLOOR che rientra nell'esplosione della bomba
				else {
				    explosionTiles.add(new Point(x, y));
					x += dx;
					y += dy;
					radius--;
				}
			}
		}
	}
	
    /**
     * Ottiene l'insieme di celle coinvolte nell'esplosione.
     *
     * @return L'insieme di oggetti {@code Point} che rappresentano le celle coinvolte nell'esplosione.
     */
	public Set<Point> getExplosionTiles() {
		return explosionTiles;
	}

    /**
     * Ottiene la bomba associata all'esplosione.
     *
     * @return L'oggetto {@code Bomb} associato all'esplosione.
     */
	public Bomb getBomb() {
		return bomb;
	}
	
    /**
     * Ottiene la coordinata x della bomba associata all'esplosione.
     *
     * @return La coordinata x della bomba.
     */
	public int getX() {
		return bomb.getX();
	}
	
    /**
     * Ottiene la coordinata y della bomba associata all'esplosione.
     *
     * @return La coordinata y della bomba.
     */
	public int getY() {
		return bomb.getY();
	}
	
    /**
     * Verifica se questa esplosione è uguale a un altro oggetto.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se gli oggetti sono uguali, {@code false} altrimenti.
     */
	@Override
	public boolean equals(Object obj) {
		return bomb.equals(obj);
	}
}
