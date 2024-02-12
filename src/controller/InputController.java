package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;

import model.Player;

/**
 * Classe che gestisce gli input da tastiera per il giocatore nel gioco.
 * Si occupa di rispondere agli eventi di pressione e rilascio dei tasti, gestendo il movimento e l'azione di posizionamento delle bombe.
 *
 * La classe implementa l'interfaccia {@code KeyListener}.
 * Utilizza un timer per gestire il suono del passo durante il movimento del personaggio.
 *
 * @author Lorenzo Zanda
 * @see KeyListener
 */
public class InputController implements KeyListener{
	
	private GameController controller;
	
	private Player player;
	private Timer walkTimer;
	
	private Set<Integer> keysPressed;
	private boolean pressed;
	
    /**
     * Costruisce un nuovo {@code InputController}.
     *
     * @param controller Il {@code GameController} associato a questo {@code InputController}.
     */
	public InputController(GameController controller) {
		this.controller = controller;
		keysPressed = new HashSet<Integer>();
		walkTimer = controller.getWalkTimer();
		player = Player.getInstance();
	}
	
	/**
     * Gestisce l'evento di pressione di un tasto della tastiera.
     *
     * @param e L'evento di pressione del tasto.
     */
	@Override
	public void keyPressed(KeyEvent e) {
		
	    int code = e.getKeyCode();
	    
	    if (code == KeyEvent.VK_SPACE && !pressed) {
	    	controller.placeBomb(); 
	    	pressed = true;
	    }
	    else {
		    keysPressed.add(code);
		    handlePlayerMovement();	
	    }
	}
	
    /**
     * Gestisce l'evento di rilascio di un tasto della tastiera.
     *
     * @param e L'evento di rilascio del tasto.
     */
	@Override
	public void keyReleased(KeyEvent e) {
	    int code = e.getKeyCode();
	    
	    if (code == KeyEvent.VK_SPACE) {
	    	pressed = false;
	    }
	    else {
		    keysPressed.remove(code);
		    handlePlayerMovement();
	    }
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
     * Gestisce il movimento del personaggio in base ai tasti premuti.
     */
	private void handlePlayerMovement() {
		
		if (player.isAlive()) {
		    int dx = 0;
		    int dy = 0;
	
		    if (keysPressed.contains(KeyEvent.VK_LEFT))  dx -= 1;
		    if (keysPressed.contains(KeyEvent.VK_RIGHT)) dx += 1;
		    if (keysPressed.contains(KeyEvent.VK_UP))    dy -= 1;
		    if (keysPressed.contains(KeyEvent.VK_DOWN))  dy += 1;
	
		    // Imposta lo stato di movimento del personaggio
		    if (dx == 0 && dy == 0) {
		    	player.stop();
		    	walkTimer.stop();
		    }
		    else {
		    	walkTimer.start();
			    if      (dx < 0) player.moveLeft();
			    else if (dx > 0) player.moveRight();
			    else if (dy < 0) player.moveUp();
			    else if (dy > 0) player.moveDown();
		    }
		}
	}
}
