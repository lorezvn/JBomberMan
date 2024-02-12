package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import model.Bomb;
import utilities.Constants;

/**
 * Classe che rappresenta la vista di una bomba nel gioco.
 * Gestisce l'animazione e il disegno della bomba sulla schermata di gioco.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche della bomba osservata.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public class BombView implements Observer {
	
    /** La coordinata x della bomba nel gioco. */
	private int x;
	
    /** La coordinata y della bomba nel gioco. */
	private int y;
	
    /** Array di immagini che compongono l'animazione della bomba. */
	private BufferedImage[] bombAnim;
	
    /** Frame corrente dell'animazione della bomba. */
	private double currentFrame;
	
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
    /**
     * Costruttore della classe {@code BombView}.
     */
	public BombView() {
		bombAnim = new BufferedImage[6];
		setSprites();
	}
	
	 /**
     * Si occupa di gestire gli sprite riguardanti la bomba.
     */
	private void setSprites() {
		
		int tileSize = Constants.TILESIZE;
		
		try {
			BufferedImage bombSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/bomb-sheet.png"));	
			for (int i = 0; i < 3; i++) {
				bombAnim[i] = bombSheet.getSubimage((2-i)*tileSize, 0, tileSize, tileSize+2);
				bombAnim[i+3] = bombSheet.getSubimage(i*tileSize, 0, tileSize, tileSize+2);
			}
			Collections.reverse(Arrays.asList(bombAnim));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Aggiorna l'animazione della bomba in base alla velocità specificata.
     *
     * @param animationSpeed La velocità di animazione della bomba.
     */
	public void updateAnimation(double animationSpeed) {
		
		if (currentFrame >= bombAnim.length) {
			currentFrame = 0;
		}
		else {
			currentFrame += animationSpeed;
		}
	}
	
	/**
     * Restituisce l'immagine corrente per l'animazione della bomba.
     *
     * @return L'immagine corrente per l'animazione della bomba.
     */
	public BufferedImage getCurrentFrame() {
		updateAnimation(0.1);
		return bombAnim[(int)currentFrame % bombAnim.length];
	}
	
	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche della bomba osservata.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Bomb) {
			Bomb bomb = (Bomb)o;
			x = bomb.getX();
			y = bomb.getY();
		}
	}
	
    /**
     * Disegna la bomba.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void draw(Graphics2D g2) {
		
		int bombX = x * TILESIZE;
		int bombY = y * TILESIZE;
		g2.drawImage(getCurrentFrame(), bombX, bombY, TILESIZE, TILESIZE, null);
	}
}
