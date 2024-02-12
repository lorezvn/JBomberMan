package view;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import model.AnimationState;
import utilities.Constants;

/**
 * Classe che rappresenta la vista di un nemico "Puropen" nel gioco.
 * Gestisce gli sprite e le animazioni associate al "Puropen".
 * Estende la classe {@code EnemyView}.
 * 
 * @author Lorenzo Zanda
 * @see EnemyView
 */
public class PuropenView extends EnemyView {
	
    /** Array di immagini che compongono l'animazione di camminata del "Puropen". */
	private BufferedImage[] walkingFrames;
    
    /** Array di immagini che compongono l'animazione di camminata del "Puropen" quando e' immortale. */
	private BufferedImage[] damagedWalkingFrames;
	
	/**
	 * Costruttore della classe {@code PuropenView}.
	 */
	public PuropenView() {
		walkingFrames = new BufferedImage[16];
		damagedWalkingFrames = new BufferedImage[16];
		setSprites();
	}

    /**
     * Imposta gli sprite del Puropen.
     */
	private void setSprites() {
		
		int tileSize = Constants.TILESIZE;
		
		for (int i = 0; i < walkingFrames.length; i++) {
			walkingFrames[i] = enemiesSheet.getSubimage(i*tileSize, 0, tileSize, 2*tileSize);
			damagedWalkingFrames[i] = enemiesDamagedSheet.getSubimage(i*tileSize, 0, tileSize, 2*tileSize);
		}
	}

    /**
     * Gestisce le animazioni in base allo stato di animazione e alla direzione di movimento di Puropen.
     */
	@Override
	protected void manageAnimation() {
		
		if (animationState == AnimationState.DYING) {
			sprites = Arrays.asList(explosion);
		}
		
		else {
			switch(direction) {
				case LEFT  -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 0, 4));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 0, 4));
				}
				case DOWN  -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 4, 8));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 4, 8));
				}
				case UP    -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 8, 12));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 8, 12));
				}
				case RIGHT -> {
					sprites = Arrays.asList(Arrays.copyOfRange(walkingFrames, 12, 16));
					damagedSprites = Arrays.asList(Arrays.copyOfRange(damagedWalkingFrames, 12, 16));
				}
			}
		}	
	}
}
