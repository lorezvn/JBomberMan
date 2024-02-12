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
import model.AnimationState;
import model.Move;
import model.Player;
import utilities.Constants;

/**
 * Classe che rappresenta la vista del giocatore nel gioco.
 * Gestisce l'animazione e il disegno del giocatore sulla schermata di gioco.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sulle modifiche del giocatore osservato.
 * 
 * @author Lorenzo Zanda
 * @see Observer
 */
public class PlayerView implements Observer {
	
 	/** La coordinata x del giocatore. */
	private int x;
    
    /** La coordinata y del giocatore. */
	private int y;
    
    /** Flag che indica se il giocatore e' stato danneggiato. */
	private boolean damaged;
    
    /** Stato corrente dell'animazione del giocatore. */
	private AnimationState animationState;
    
    /** Direzione di movimento del giocatore. */
	private Move direction;
    
    /** Tempo corrente. */
	private long currentTime;
	
    /** Tempo di cooldown per l'animazione dell'immortalita'. */
	private long immortalAnimationCooldown;
    
    /** Tempo tra sprite normale e danneggiato. */
	private final long IMMORTAL_ANIMATION_COOLDOWN = 50;
    
    /** Flag utilizzato per alternare sprite durante la fase dell'immortalita'. */
	private boolean damagedAnimation;
	
    /** Flag che indica se una delle animazioni del giocatore a ciclo unico e' terminata. */
	private boolean animationFinished;
    
    /** Array di frame per l'animazione quando il giocatore e' fermo. */
	private BufferedImage[] idleFrames;
    
    /** Array di frame per l'animazione quando il giocatore e' fermo ed e' immortale.*/
	private BufferedImage[] damagedIdleFrames;
    
    /** Array di frame per l'animazione di movimento del giocatore. */
	private BufferedImage[] walkingFrames;
    
    /** Array di frame per l'animazione di movimento del giocatore ed e' immortale. */
	private BufferedImage[] damagedWalkingFrames;
    
    /** Array di frame per l'animazione della morte del giocatore. */
	private BufferedImage[] dyingFrames;
    
    /** Array di frame per l'animazione della vittoria del giocatore. */
	private BufferedImage[] winningFrames;
    
    /** Lista di immagini correnti per l'animazione del giocatore. */
	private List<BufferedImage> sprites;
    
    /** Lista di immagini per l'animazione del giocatore quando e' immortale. */
	private List<BufferedImage> damagedSprites;
    
    /** Frame corrente dell'animazione. */
	private double currentFrame;
    
    /** Frame dell'animazione quando il giocatore è fermo. */
	private int idleFrame;
    
    /** Dimensione di ogni cella nel gioco. */
	private final int TILESIZE = Constants.SCALED_TILESIZE;
	
	/**
	 * Costruttore dell classe {@code PlayerView}.
	 */
	public PlayerView() {

		idleFrames = new BufferedImage[4];
		damagedIdleFrames = new BufferedImage[4];
		walkingFrames = new BufferedImage[16];
		damagedWalkingFrames = new BufferedImage[16];
		dyingFrames = new BufferedImage[7];
		winningFrames = new BufferedImage[13];
		sprites = new ArrayList<BufferedImage>();
		damagedSprites = new ArrayList<BufferedImage>();
		
		setSprites();
	}
	
	 /**
     * Imposta gli sprite del giocatore utilizzati per le diverse animazioni.
     * Carica e seleziona gli sprite dallo sprite sheet corrispondente e li assegna agli array a seconda dell'animazione a cui fanno riferimento.
     */
	private void setSprites() {
		
		//Gestione Sprites
		
		int tileSize = Constants.TILESIZE;
		
		try {
			BufferedImage playerSheet  = ImageIO.read(getClass().getResourceAsStream("/sprites/player-sheet.png"));
			BufferedImage damagedSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/damaged-player-sheet.png"));
			
			
			idleFrames[0] = playerSheet.getSubimage(tileSize, 0, tileSize, 2*tileSize);
			idleFrames[1] = playerSheet.getSubimage(5*tileSize, 0, tileSize, 2*tileSize);
			idleFrames[2] = playerSheet.getSubimage(9*tileSize, 0, tileSize, 2*tileSize);
			idleFrames[3] = playerSheet.getSubimage(13*tileSize, 0, tileSize, 2*tileSize);
			damagedIdleFrames[0] = damagedSheet.getSubimage(tileSize, 0, tileSize, 2*tileSize);
			damagedIdleFrames[1] = damagedSheet.getSubimage(5*tileSize, 0, tileSize, 2*tileSize);
			damagedIdleFrames[2] = damagedSheet.getSubimage(9*tileSize, 0, tileSize, 2*tileSize);
			damagedIdleFrames[3] = damagedSheet.getSubimage(13*tileSize, 0, tileSize, 2*tileSize);
			
			
			for (int i = 0; i < walkingFrames.length; i++) {
				walkingFrames[i] = playerSheet.getSubimage(i*tileSize, 0, tileSize, 2*tileSize);
				damagedWalkingFrames[i] = damagedSheet.getSubimage(i*tileSize, 0, tileSize, 2*tileSize);
			}
			
			for(int i = 0; i < dyingFrames.length; i++) {
				dyingFrames[i] = playerSheet.getSubimage(i*tileSize, 4*tileSize, tileSize, 2*tileSize);
			}
			
			for (int i = 0; i < winningFrames.length; i++) {
				winningFrames[i] = playerSheet.getSubimage(i*tileSize, 6*tileSize, tileSize, 2*tileSize);	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	  /**
     * Aggiorna l'animazione del giocatore in base alla velocità specificata.
     *
     * @param animationSpeed La velocità di animazione del giocatore.
     */
	public void updateAnimation(double animationSpeed) {
		
		//Animazioni a ciclo unico
		if ((animationState == AnimationState.WINNING || animationState == AnimationState.DYING)  
		&& currentFrame >= sprites.size()-1) {
			animationFinished = true;
		}

		if (animationState != AnimationState.IDLE) {
			
			if (currentFrame >= sprites.size()) {
				currentFrame = 0;
			}
			
			else {
				currentFrame += animationSpeed;
			}
		}
	}
	
	/**
     * Restituisce l'immagine corrente per l'animazione del giocatore.
     *
     * @return L'immagine corrente per l'animazione del giocatore.
     */
	public BufferedImage getCurrentFrame() {
		
		manageAnimation();
		if (animationState == AnimationState.WINNING) {
			updateAnimation(0.2);
		}
		else {
			updateAnimation(0.1);
		}
		
		//Se il Player e' stato colpito -> immortalita' per qualche secondo
		if (damaged && (animationState == AnimationState.WALKING || animationState == AnimationState.IDLE)) {
			immortalAnimation();
		}
		
		if (animationState != AnimationState.IDLE) {
			return sprites.get((int)currentFrame % sprites.size());
		}
		
		else {
			return sprites.get(idleFrame);
		}
	}
	
    /**
     * Gestisce l'animazione dell'immortalità del giocatore dopo essere stato colpito.
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
     * Gestisce la selezione degli sprite in base allo stato dell'animazione.
     */
	public void manageAnimation() {
		
		if (animationState == AnimationState.WALKING) {
			switch(direction) {
				case DOWN   -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 0, 4));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 0, 4));
					idleFrame = 0;
				}
				case LEFT   -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 4, 8));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 4, 8));
					idleFrame = 1;
				}
				case RIGHT  -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 8, 12));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 8, 12));
					idleFrame = 2;
				}
				case UP     -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 12, 16));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 12, 16));
					idleFrame = 3;
				}
			}	
		}
		
		if (animationState == AnimationState.IDLE) {
			sprites = Arrays.asList(idleFrames);
			damagedSprites = Arrays.asList(damagedIdleFrames);
		}
		
		if (animationState == AnimationState.DYING) {
			sprites = Arrays.asList(dyingFrames);
		}
		
		if (animationState == AnimationState.WINNING) {
			sprites = Arrays.asList(winningFrames);
		}
	}

	/**
	 * Override del metodo update della classe {@code Observer} per ricevere notifiche sulle modifiche del giocatore osservato.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player) {
			Player player = (Player)o;
			x = player.getX();
			y = player.getY();
			damaged = player.isDamaged();
			animationState = player.getAnimationState();
			direction = player.getDirection();
			if (animationState == AnimationState.DYING) {
				currentFrame = 0;
			}
		}
		
		if (arg != null && arg instanceof Long) {
			currentTime = (long)arg;
		}
	}
	
    /**
     * Disegna il giocatore.
     *
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     */
	public void draw(Graphics2D g2) {
		
		int playerX = x; 
		int playerY = y - TILESIZE;
		g2.drawImage(getCurrentFrame(), playerX, playerY, TILESIZE, 2*TILESIZE, null);
	}
	
    /**
     * Restituisce lo stato di una delle animazioni a ciclo unico.
     *
     * @return {@code true} se l'animazione e' terminata, {@code false} altrimenti.
     */
	public boolean animationFinished() {
		return animationFinished;
	}
}
