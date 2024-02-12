package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import model.PowerUp;
import model.PowerUpType;
import utilities.Constants;

/**
 * Classe che rappresenta la vista di un power-up nel gioco.
 * Gestisce l'animazione e il disegno del power-up sulla schermata di gioco.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche del power-up osservato.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public class PowerUpView implements Observer {
	
    /** La coordinata x del power-up. */
	private int x;
    
    /** La coordinata y del power-up. */
	private int y;
    
    /** Tipo del power-up. */
	private PowerUpType type;
    
    /** Array di frame per l'animazione del power-up. */
	private BufferedImage[] sprites;
    
    /** Frame corrente dell'animazione. */
	private double currentFrame;
	
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	/**
	 * Costruttore della classe {@code PowerUpView}.
	 */
	public PowerUpView() {
		sprites = new BufferedImage[2];
	}
	
	/**
	 * Imposta gli sprite del power-up in base al tipo.
	 */
	private void setSprites() {
		
		//Gestione Sprites
		
		int tileSize = 16;
		
		try {
			BufferedImage powerUpSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/powerups-sheet.png"));
			
			switch(type) {
				case BOMBUP -> {
					sprites[0] = powerUpSheet.getSubimage(0, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(0, tileSize, tileSize, tileSize);
				}
				case ACCELERATOR  -> {
					sprites[0] = powerUpSheet.getSubimage(tileSize, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(tileSize, tileSize, tileSize, tileSize);
				}
				case FIRE   -> {
					sprites[0] = powerUpSheet.getSubimage(2*tileSize, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(2*tileSize, tileSize, tileSize, tileSize);
				}
				case BOMBERMAN  -> {
					sprites[0] = powerUpSheet.getSubimage(3*tileSize, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(3*tileSize, tileSize, tileSize, tileSize);
				}
				case RICE_BALL -> {
					sprites[0] = powerUpSheet.getSubimage(4*tileSize, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(4*tileSize, tileSize, tileSize, tileSize);
				}
				case APPLE ->  {
					sprites[0] = powerUpSheet.getSubimage(5*tileSize, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(5*tileSize, tileSize, tileSize, tileSize);
				}
				case ICE_CREAM_CONE -> {
					sprites[0] = powerUpSheet.getSubimage(6*tileSize, 0, tileSize, tileSize);
					sprites[1] = powerUpSheet.getSubimage(6*tileSize, tileSize, tileSize, tileSize);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce l'immagine corrente del power-up per l'animazione.
	 * 
	 * @return l'immagine corrente del power-up.
	 */
	public BufferedImage getCurrentFrame() {
		updateAnimation(0.5);
		return sprites[(int)currentFrame % sprites.length];
	}
	
    /**
     * Aggiorna l'animazione del power-up in base alla velocità specificata.
     *
     * @param animationSpeed La velocità di animazione del power-up.
     */
	public void updateAnimation(double animationSpeed) {
		
		if (currentFrame >= sprites.length) {
			currentFrame = 0;
		}
		
		else {
			currentFrame += animationSpeed;
		}
	}

	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche del power-up osservato.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PowerUp) {
			PowerUp powerUp = (PowerUp)o;
			x = powerUp.getX();
			y = powerUp.getY();
			type = powerUp.getType();
			setSprites();
		}
	}
	
    /**
     * Disegna il power-up.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void draw(Graphics2D g2) {
		
		int powerUpX = x * TILESIZE;
		int powerUpY = y * TILESIZE;
		
		g2.drawImage(getCurrentFrame(), powerUpX, powerUpY, TILESIZE, TILESIZE, null);
	}
}

