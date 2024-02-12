package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import model.Tile;
import model.TileType;
import utilities.Constants;

/**
 * Classe che rappresenta la vista generica di una cella nel gioco.
 * Gestisce gli sprite, le animazioni e lo stato della cella.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche della cella osservata.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public class TileView implements Observer {
	
	/** Coordinata x della cella. */
	private int x;
    
    /** Coordinata y della cella. */
	private int y;
    
    /** Tipo della cella. */
	private TileType type;
    
    /** Flag che indica se la cella è stata colpita. */
	private boolean hit;
	
    /** Frame corrente dell'animazione. */
	private double currentFrame;
    
    /** Flag che indica se l'animazione è attiva. */
	private boolean animate;
    
    /** Flag che indica se l'animazione è terminata. */
	private boolean animationFinished;
	
    /** Array di frame per l'animazione di scomparsa di una cella. */
	private static BufferedImage[] disappearingAnimation;
    
    /** Array di frame per le celle animate. */
	private static BufferedImage[] animatedTiles;
    
    /** Array di frame per le celle distruttibili. */
	private static BufferedImage[] breakableSprites;
    
    /** Array di frame per le celle indistruttibili. */
	private static BufferedImage[] unbreakableSprites;
    
    /** Array di frame per le celle del pavimento. */
	private static BufferedImage[] floorSprites;
    
    /** Lista di frame correnti per l'animazione. */
	private List<BufferedImage> sprites;
	
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
    /** Inizializzazione statica degli sprite. */
	static {
		setSprites();
	}
	
	/**
	 * Costruttore della classe {@code TileView}.
	 */
	public TileView() {
		sprites = new ArrayList<BufferedImage>();
	}
	
	/**
	 * Imposta gli sprite del tile in base al tipo.
	 */
	private static void setSprites() {
		
		try {
			int tileSize = Constants.TILESIZE;
			BufferedImage floorSheet = ImageIO.read(Tile.class.getResourceAsStream("/sprites/floor-sheet.png"));	
			
			animatedTiles = new BufferedImage[4];
			disappearingAnimation = new BufferedImage[6*Constants.MAX_LEVEL];
			breakableSprites = new BufferedImage[Constants.MAX_LEVEL];
			unbreakableSprites = new BufferedImage[Constants.MAX_LEVEL];
			floorSprites = new BufferedImage[Constants.MAX_LEVEL];
			
			for (int i=0; i < Constants.MAX_LEVEL; i++) {
				 unbreakableSprites[i] = floorSheet.getSubimage(0, i*tileSize, tileSize, tileSize);
				 floorSprites[i]       = floorSheet.getSubimage(1*tileSize, i*tileSize, tileSize, tileSize);
				 breakableSprites[i]   = floorSheet.getSubimage(2*tileSize, i*tileSize, tileSize, tileSize);
			}
			
			for (int i=0, j=9; i < animatedTiles.length; i++, j++) {
				animatedTiles[i] = floorSheet.getSubimage(j*tileSize, tileSize, tileSize, tileSize);
			}
			
			for (int i=0, j=0, k=0; k < disappearingAnimation.length; i++, k++) {
				if (k != 0 && k % 6 == 0) {
					i=0;
					j++;
				}
				disappearingAnimation[k] = floorSheet.getSubimage((i+3)*tileSize, j*tileSize, tileSize, tileSize);
			}
				
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Gestisce l'animazione in base al tipo di cella.
     *
     * @param level Il livello del gioco.
     */
	public void manageAnimation(int level) {
		
		switch(type) {
		
			case BREAKABLE -> {
				
				if (hit) {
					sprites = Arrays.asList(Arrays.copyOfRange(disappearingAnimation, 6*level, 6*level+6));
					animate = true;
					updateAnimation(0.05);
					break;
				}
				
				if (level == 1) {
					sprites = Arrays.asList(animatedTiles);
					animate = true;
					updateAnimation(0.1);
					break;
				}
				
				else {
					sprites = Arrays.asList(breakableSprites);
				}
			}
				
			case UNBREAKABLE -> sprites = Arrays.asList(unbreakableSprites);
			case FLOOR -> sprites = Arrays.asList(floorSprites);

		}
	}
	
    /**
     * Aggiorna l'animazione della cella in base alla velocita' specificata.
     *
     * @param animationSpeed La velocità dell'animazione.
     */
	public void updateAnimation(double animationSpeed) {
		
		if (hit && currentFrame >= sprites.size()-1) {
			animationFinished = true;
		}
		else {
			
			if (currentFrame > sprites.size()) {
				currentFrame = 0;
			}
			
			else {
				currentFrame += animationSpeed;
			}
		}
	}
	
    /**
     * Restituisce l'immagine corrente della cella in base al livello del gioco.
     *
     * @param level Il livello del gioco.
     * @return L'immagine corrente della cella.
     */
	public BufferedImage getCurrentFrame(int level) {
		manageAnimation(level);
		if (animate) {
			return sprites.get((int)currentFrame % sprites.size());
		}
		return sprites.get(level);
	}

	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche della cella osservata.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Tile) {
			Tile tile = (Tile)o;
			x = tile.getX();
			y = tile.getY();
			type = tile.getType();
			hit = tile.isHit();
		}
	}
	
	/**
	 * Disegna la cella
	 * 
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
	 * @param level Livello corrente del livello.
	 */
	public void draw(Graphics2D g2, int level) {
		
		int cellX = x * TILESIZE;
		int cellY = y * TILESIZE;
		
		g2.drawImage(getCurrentFrame(level), cellX, cellY, TILESIZE, TILESIZE, null);
	}
	
    /**
     * Verifica se l'animazione della cella è terminata.
     *
     * @return {@code true} se l'animazione è terminata, {@code false} altrimenti.
     */
	public boolean animationFinished() {
		return animationFinished;
	}

}
