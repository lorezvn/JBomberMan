package view;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import model.AnimationState;
import utilities.Constants;

/**
 * Classe che rappresenta la vista di un nemico "Denkyun" nel gioco.
 * Gestisce gli sprite e le animazioni associate a "Denkyun".
 * Estende la classe {@code EnemyView}.
 * 
 * @author Lorenzo Zanda
 * @see EnemyView
 */
public class DenkyunView extends EnemyView {
	
    /** Array di immagini che compongono l'animazione di camminata del "Denkyun". */
	private BufferedImage[] walkingFrames;
    
    /** Array di immagini che compongono l'animazione di camminata del "Denkyun" quando e' immortale. */
	private BufferedImage[] damagedWalkingFrames;

    /**
     * Costruttore della classe {@code DenkyunView}.
     */
	public DenkyunView() {
		walkingFrames = new BufferedImage[12];
		damagedWalkingFrames = new BufferedImage[12];
		setSprites();
	}
	
    /**
     * Imposta gli sprite del Denkyun.
     */
	private void setSprites() {
		
		int tileSize = Constants.TILESIZE;
		
		for (int i = 0; i < walkingFrames.length/2; i++) {
			walkingFrames[i] = enemiesSheet.getSubimage(i*tileSize, 2*tileSize, tileSize, 2*tileSize);
			damagedWalkingFrames[i] = enemiesDamagedSheet.getSubimage(i*tileSize, 2*tileSize, tileSize, 2*tileSize);
			
		}
		
		for (int i = walkingFrames.length/2; i > 0; i--) {
			int length = walkingFrames.length;
			walkingFrames[length-i] = enemiesSheet.getSubimage((i-1)*tileSize, 2*tileSize, tileSize, 2*tileSize);
			damagedWalkingFrames[length-i] = enemiesDamagedSheet.getSubimage((i-1)*tileSize, 2*tileSize, tileSize, 2*tileSize);
		}
	}

    /**
     * Gestisce le animazioni in base allo stato di animazione e alla direzione di movimento del "Denkyun".
     */
	@Override
	protected void manageAnimation() {
		
		if (animationState == AnimationState.DYING) {
			sprites = Arrays.asList(explosion);
		}
		else {
			sprites = Arrays.asList(walkingFrames);
			damagedSprites = Arrays.asList(damagedWalkingFrames);
		}
	}
}
