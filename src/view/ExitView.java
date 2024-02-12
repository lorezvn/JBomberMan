package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import model.Exit;
import utilities.Constants;

/**
 * Classe che rappresenta la vista dell'uscita nel gioco.
 * Gestisce l'animazione e il disegno dell'uscita sulla schermata di gioco.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche dell'uscita osservata.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public class ExitView implements Observer {
	
    /** La coordinata x dell'uscita. */
	private int x;
    
    /** La coordinata y dell'uscita. */
	private int y;
    
    /** Lista di sprite per l'animazione. */
	private List<BufferedImage> sprites;
    
    /** Frame corrente dell'animazione. */
	private double currentFrame;
    
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	/**
	 * Costruttore della classe {@code ExitView}.
	 */
	public ExitView() {
		sprites = new ArrayList<BufferedImage>();
		setSprites();
	}
	 /**
     * Si occupa di gestire gli sprite riguardanti l'uscita.
     */
	private void setSprites() {
		
		int tileSize = Constants.TILESIZE;
		
		try {
			BufferedImage exitSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/exit-sheet.png"));	
			sprites.add(exitSheet.getSubimage(0, 0, tileSize, tileSize));
			sprites.add(exitSheet.getSubimage(tileSize, 0, tileSize, tileSize));
			
		}
		catch(IOException e) {
			 e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce l'immagine corrente dell'uscita per l'animazione.
	 * 
	 * @return L'immagine corrente dell'uscita.
	 */
	public BufferedImage getCurrentFrame() {
		updateAnimation(0.7);
		return sprites.get((int)currentFrame % sprites.size());
	}
	
	/**
	 * Aggiorna l'animazione dell'uscita in base alla velocita' specificata.
	 * 
	 * @param animationSpeed La velocità dell'animazione.
	 */
	public void updateAnimation(double animationSpeed) {
		
		if (currentFrame >= sprites.size()) {
			currentFrame = 0;
		}
		
		else {
			currentFrame += animationSpeed;
		}
	}
	
	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche dell' uscita osservata.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Exit) {
			Exit exit = (Exit)o;
			x = exit.getX();
			y = exit.getY();
		}
	}
	
    /**
     * Disegna l'uscita.
     * 
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void draw(Graphics2D g2) {
		
		int exitX = x * TILESIZE;
		int exitY = y * TILESIZE;
		
		g2.drawImage(getCurrentFrame(), exitX, exitY, TILESIZE, TILESIZE, null);
	}
}
