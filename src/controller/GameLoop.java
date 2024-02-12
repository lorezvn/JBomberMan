package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utilities.Constants;

/**
 * Classe che implementa il ciclo di gioco.
 * Si occupa di eseguire ciclicamente l'aggiornamento del controller del gioco a una frequenza specifica (FPS).
 *
 * La classe utilizza un {@code ExecutorService} per eseguire il ciclo di gioco in un thread separato.
 * Il ciclo di gioco è controllato da un timer che determina il tempo tra i frame, garantendo una frequenza costante.
 *
 * @author Lorenzo Zanda
 * @see Runnable
 * @see ExecutorService
 */
public class GameLoop implements Runnable {
	
	private GameController controller;
	private ExecutorService executorService;
	private boolean running = false;
	
	/**
     * Costruisce un nuovo {@code GameLoop}.
     *
     * @param controller Il {@code GameController} associato a questo loop.
     */
	public GameLoop(GameController controller) {
		this.controller = controller;	
	}

    /**
     * Implementa il ciclo di gioco.
     */
	@Override
	public void run() {
		//Game loop
	    double timePerFrame = 1000000000.0 / Constants.FPS; 
	    long lastFrame = System.nanoTime();
	    long now = System.nanoTime();
	    
	    int frames = 0;
	    long lastCheck = System.currentTimeMillis();

	    while (running && !Thread.interrupted()) {
	        now = System.nanoTime();
	        if (now - lastFrame >= timePerFrame) {
	        	controller.update();
	        	lastFrame = now;
	        	frames++;
	        }

	        if (System.currentTimeMillis() - lastCheck >= 1000) {
	            lastCheck = System.currentTimeMillis();
	            frames = 0;
	        }
	    }
	}
	
    /**
     * Avvia il ciclo di gioco.
     */
	public void start() {
		running = true;
		executorService = Executors.newSingleThreadExecutor();
		executorService.execute(this);
	}
	
    /**
     * Ferma l'esecuzione del ciclo.
     */
	public void stop() {
		if (executorService != null) {
			running = false;
			executorService.shutdownNow();
		}
	}

}
