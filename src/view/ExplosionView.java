package view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.imageio.ImageIO;
import model.Explosion;
import utilities.Constants;

/**
 * Classe che rappresenta la vista di un'esplosione nel gioco.
 * Gestisce l'animazione e il disegno dell'esplosione sulla schermata di gioco.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche dell'esplosione osservata.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public class ExplosionView implements Observer {
	
    /** La coordinata x dell'esplosione. */
	private int x;
    
    /** La coordinata y dell'esplosione. */
	private int y;
    
    /** Raggio dell'esplosione. */
	private int radius;
    
    /** Insieme di celle coinvolte nell'esplosione. */
	private Set<Point> explosionTiles;
    
    /** Frame corrente dell'animazione. */
	private double currentFrame;
    
    /** Flag che indica se l'animazione dell'esplosione è terminata. */
	private boolean finished;
	
	/** Numero di frame componenti un ciclo di animazione di una componente dell'esplosione */
	private static final int LENGTH = 9;
    
    /** Matrice di immagini per l'animazione dell'esplosione. */
	private static BufferedImage[][] explosionFrames;
    
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;

    /** Inizializzazione statica degli sprite. */
	static {
		setSprites();
	}
	
	 /**
     * Si occupa di gestire gli sprite riguardanti l'esplosione.
     */
	private static void setSprites() {
		
		int tileSize = Constants.TILESIZE;
		
		try {
			BufferedImage explosionSheet = ImageIO.read(Explosion.class.getResourceAsStream("/sprites/bomb-explosion-sheet.png"));	
			
			explosionFrames = new BufferedImage[ExplosionImage.values().length][LENGTH];
			
			for (int direction=0; direction < explosionFrames.length-1; direction++) {
				for (int frame=0; frame < LENGTH; frame++) {
					explosionFrames[direction][frame] = explosionSheet.getSubimage(frame*tileSize, direction*tileSize, tileSize, tileSize);
				}
			}
			
			for (int i=0; i < LENGTH; i++) {
				explosionFrames[8][i] = explosionSheet.getSubimage(i*tileSize, 7*tileSize, tileSize, tileSize);
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Aggiorna l'animazione dell'esplosione in base alla velocità specificata.
     *
     * @param animationSpeed La velocità di animazione dell'esplosione.
     */
	public void updateAnimation(double animationSpeed) {
		
		if (currentFrame >= LENGTH-1) {
			finished = true;
		}
		
		else {
			currentFrame += animationSpeed;
		}
	}
	
	/**
     * Restituisce l'immagine corrente per l'animazione dell'esplosione.
     *
     * @param image La direzione dell'immagine dell'esplosione.
     * @return L'immagine corrente per l'animazione dell'esplosione.
     */
	public BufferedImage getCurrentFrame(ExplosionImage image) {
		
		updateAnimation(0.1);
		int direction = image.getValue();
		int frameIndex = (int)currentFrame % LENGTH;
        return explosionFrames[direction][frameIndex];
	}
	
    /**
     * Verifica se l'animazione dell'esplosione è terminata.
     *
     * @return {@code true} se l'animazione è terminata, {@code false} altrimenti.
     */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche dell'esplosione osservata.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Explosion) {
			Explosion exp = (Explosion)o;
			x = exp.getX();
			y = exp.getY();
			radius = exp.getBomb().getExplosionRadius();
			explosionTiles = exp.getExplosionTiles();
		}	
	}
	
    /**
     * Disegna l' esplosione.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void draw(Graphics2D g2) {
		
		for (Point tile : explosionTiles) {
			
			int tileX = (int)tile.getX();
			int tileY = (int)tile.getY();
			
			ExplosionImage img = getExplosionImage(x, y, tileX, tileY, radius);
			
		    g2.drawImage(getCurrentFrame(img), tileX*TILESIZE, tileY*TILESIZE, TILESIZE, TILESIZE, null);
		}
	}
	
	/**
	 * Restituisce il tipo di immagine di esplosione in base alle posizioni relative dell'esplosione (il centro dell'esplosione) 
	 * e della cella.
	 *
	 * @param expX    La coordinata x dell'esplosione.
	 * @param expY    La coordinata y dell'esplosione.
	 * @param tileX   La coordinata x della cella.
	 * @param tileY   La coordinata y della cella.
	 * @param radius  Il raggio dell'esplosione.
	 * @return        Il tipo di immagine di esplosione corrispondente.
	 */
	private ExplosionImage getExplosionImage(int expX, int expY, int tileX, int tileY, int radius) {
		
	    if (tileX == expX && tileY == expY) return ExplosionImage.CENTRAL;
	    if (tileX < expX && tileX == expX - radius) return ExplosionImage.LEFT;
	    if (tileX > expX && tileX == expX + radius) return ExplosionImage.RIGHT;
	    if (tileY < expY && tileY == expY - radius) return ExplosionImage.UP;
	    if (tileY > expY && tileY == expY + radius) return ExplosionImage.DOWN;
	    if (tileX < expX) return ExplosionImage.C_LEFT;
	    if (tileX > expX) return ExplosionImage.C_RIGHT;
	    if (tileY < expY) return ExplosionImage.C_UP;
	    if (tileY > expY) return ExplosionImage.C_DOWN;

	    return ExplosionImage.CENTRAL;
	}
}
