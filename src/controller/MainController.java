package controller;

import view.BomberManFrame;

/**
 * Classe che gestisce l'inizializzazione e la coordinazione di diversi controller e la configurazione degli elementi principali del gioco.
 *
 * @author Lorenzo Zanda
 */

public class MainController {
	
	private SceneManagerController sceneManagerController;
	private FloorController floorController;
	private ButtonController buttonController;
	private GameController controller;
	
    /**
     * Costruisce un nuovo {@code MainController}.
     *
     * @param frame Il frame principale del gioco.
     */
	public MainController(BomberManFrame frame) {
		
		sceneManagerController = new SceneManagerController(frame.getCardLayout(), frame.getCardPanel());
		buttonController = new ButtonController(frame, sceneManagerController);
		floorController = new FloorController(frame);
		controller = new GameController(frame, floorController, buttonController);
		
		buttonController.setController(controller);
		
		frame.getGamePanel().addKeyListener(controller.getInputController());
		frame.getGamePanel().setController(controller);
	}
}
