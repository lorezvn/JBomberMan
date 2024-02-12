package view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import model.AnimationState;
import model.Move;
import model.Enemy;
import utilities.Constants;

/**
 * Classe astratta che rappresenta la vista generica di un nemico nel gioco.
 * Gestisce gli sprite, le animazioni e lo stato di un nemico.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche del nemico osservato.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public abstract class EnemyView implements Observer {
	
    /** La coordinata x del nemico. */
	protected int x;
    
    /** La coordinata y del nemico. */
	protected int y;
    
    /** Flag che indica se il nemico e' stato danneggiato. */
	protected boolean damaged;
    
    /** Lo stato corrente dell'animazione del nemico. */
	protected AnimationState animationState;
    
    /** La direzione di movimento del nemico. */
	protected Move direction;
    
    /** Il tempo corrente. */
	protected long currentTime;
    
    /** Cooldown per l'animazione di immortalità dopo essere stato colpito. */
	protected long immortalAnimationCooldown;
    
    /** Tempo tra sprite normale e danneggiato. */
	protected final int IMMORTAL_ANIMATION_COOLDOWN = 50;
    
    /** Flag utilizzato per alternare sprite durante la fase dell'immortalita'. */
	protected boolean damagedAnimation;
    
    /** Indica se l'animazione a ciclo unico del nemico e' terminata. */
	protected boolean animationFinished;
    
    /** Frame corrente dell'animazione. */
	protected double currentFrame;
    
    /** Lista di sprite per l'animazione del nemico. */
	protected List<BufferedImage> sprites;
    
    /** Lista di immagini per l'animazione del giocatore quando e' immortale. */
	protected List<BufferedImage> damagedSprites;
    
    /** Collider del nemico. */
	protected Rectangle collider;
    
    /** Sprite Sheet dei nemici. */
	protected BufferedImage enemiesSheet;
    
    /** Sprite Sheet dei nemici quando danneggiati. */
	protected BufferedImage enemiesDamagedSheet;
    
    /** Array di immagini per l'animazione di morte del nemico. */
	protected BufferedImage[] explosion;
    
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
    /**
     * Costruttore della classe {@code EnemyView}.
     */
	public EnemyView() {
		sprites = new ArrayList<BufferedImage>();
		damagedSprites = new ArrayList<BufferedImage>();
		explosion = new BufferedImage[8];
		
		setSprites();
	}
	
	 /**
     * Si occupa di gestire gli sprite riguardanti l'animazione di morte.
     */
	private void setSprites() {
		
		try {
			enemiesSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/enemies-sheet.png"));	
			enemiesDamagedSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/damaged-enemies-sheet.png"));	
			
			BufferedImage explosionSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/enemy-explosion-sheet.png"));
			
			int tileSize = Constants.TILESIZE;
			
			for (int i = 0; i < 3; i++) {
				explosion[i] = explosionSheet.getSubimage(i*tileSize, 0, tileSize, 3*tileSize);
			}
			
			explosion[3] = explosionSheet.getSubimage(3*tileSize+5, 0, tileSize+6, 3*tileSize);
			explosion[4] = explosionSheet.getSubimage(5*tileSize-2, 0, tileSize+4, 3*tileSize);
			explosion[5] = explosionSheet.getSubimage(6*tileSize+6, 0, tileSize+4, 3*tileSize);
			explosion[6] = explosionSheet.getSubimage(8*tileSize, 0, tileSize, 2*tileSize);
			explosion[7] = explosionSheet.getSubimage(9*tileSize, 0, tileSize, 2*tileSize);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Gestisce la selezione degli sprite in base allo stato dell'animazione.
     */
	protected abstract void manageAnimation();
	
    /**
     * Aggiorna l'animazione del nemico in base alla velocità specificata.
     *
     * @param animationSpeed La velocità di animazione del nemico.
     */
	public void updateAnimation(double animationSpeed) {
		
		if (animationState == AnimationState.DYING && currentFrame >= sprites.size()-1) {
			animationFinished = true;
		}
		
		if (currentFrame >= sprites.size()) {
			currentFrame = 0;
		}
		
		else {
			currentFrame += animationSpeed;
		}
	}
	
	/**
     * Restituisce l'immagine corrente per l'animazione del giocatore.
     *
     * @return L'immagine corrente per l'animazione del giocatore.
     */
	public BufferedImage getCurrentFrame() {
		
		updateAnimation(0.2);
		manageAnimation();
		
		//Cooldown hit
		if (damaged && animationState == AnimationState.WALKING) {
			immortalAnimation();
		}
		
		return sprites.get((int)currentFrame % sprites.size());
	}
	
    /**
     * Gestisce l'animazione dell'immortalità del nemico dopo essere stato colpito.
     */
	public void immortalAnimation() {
		
		if (currentTime - immortalAnimationCooldown > IMMORTAL_ANIMATION_COOLDOWN) {
	        immortalAnimationCooldown = currentTime;
	        damagedAnimation = !damagedAnimation;
	    }
		
		//Sprite alternati
		if (damagedAnimation) {
			sprites = damagedSprites;
		}
	}

	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche del nemico osservato.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Enemy) {
			Enemy enemy = (Enemy)o;
			x = enemy.getX();
			y = enemy.getY();
			damaged = enemy.isDamaged();
			animationState = enemy.getAnimationState();
			direction = enemy.getDirection();
			if (animationState == AnimationState.DYING) {
				currentFrame = 0;
			}
		}
		
		if (arg != null) {
			currentTime = (long)arg;
		}
	}
	
    /**
     * Disegna il nemico.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void draw(Graphics2D g2) {
		int enemyX = x; 
		int enemyY = y - TILESIZE;
		g2.drawImage(getCurrentFrame(), enemyX, enemyY, TILESIZE, 2*TILESIZE, null);
	}
	
    /**
     * Verifica se l'animazione di morte del nemico è terminata.
     *
     * @return {@code true} se l'animazione è terminata, {@code false} altrimenti.
     */
	public boolean animationFinished() {
		return animationFinished;
	}
}
