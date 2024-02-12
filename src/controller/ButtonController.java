package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.User;
import view.BomberManFrame;
import view.GameOverPanel;
import view.LoginPanel;
import view.MenuPanel;
import view.StatsPanel;
import view.VictoryPanel;

/**
 * Classe che gestisce le azioni dei pulsanti dei vari pannelli.
 * Implementa l'interfaccia {@code ActionListener} per rispondere ai clic sui pulsanti.
 *
 * @author Lorenzo Zanda
 * @see ActionListener
 */
public class ButtonController implements ActionListener{
	
	private BomberManFrame frame;
	private SceneManagerController sceneManagerController;
	private GameController controller;
	
	/**
     * Costruttore della classe {@code ButtonController}.
     *
     * @param frame                  L'istanza di {@code BomberManFrame} che rappresenta il frame principale del gioco.
     * @param sceneManagerController {@code SceneManagerController} responsabile della gestione delle scene nel gioco.
     */
	public ButtonController(BomberManFrame frame, SceneManagerController sceneManagerController) {
		
		this.frame = frame;
		this.sceneManagerController = sceneManagerController;
	
		setActionListeners();
	}
	
	/**
	 * Metodo che si occupa di impostare gli {@code ActionListener} per i bottoni.
	 */
	public void setActionListeners() {
		
		//Menu Panel Buttons
		frame.getMenuPanel().getStartButton().addActionListener(this);
		frame.getMenuPanel().getLoginButton().addActionListener(this);
		frame.getMenuPanel().getStatsButton().addActionListener(this);
		frame.getMenuPanel().getChangeUserButton().addActionListener(this);
		
		//Login Panel Buttons
		frame.getLoginPanel().getLoginButton().addActionListener(this);
		frame.getLoginPanel().getBackButton().addActionListener(this);
		frame.getLoginPanel().getConfirmButton().addActionListener(this);
		
		//Stats Panel Buttons
		frame.getStatsPanel().getBackButton().addActionListener(this);
		
		//Game Over Panel Buttons
		frame.getGameOverPanel().getRestartButton().addActionListener(this);
		frame.getGameOverPanel().getExitButton().addActionListener(this);
		
		//Win Panel Buttons
		frame.getVictoryPanel().getMenuButton().addActionListener(this);
		frame.getVictoryPanel().getQuitButton().addActionListener(this);
	}
	
	/**
	 * Mostra il pannello principale del gioco.
	 */
	public void startGame() {
		sceneManagerController.showGamePanel();
		frame.getGamePanel().requestFocus();
	}
	
	/**
	 * Mostra il pannello di game over.
	 */
	public void endGame() {
		sceneManagerController.showGameOverPanel();
	}
	
	/**
	 * Reimposta il gioco e mostra il pannello principale del gioco.
	 */
	public void resetGame() {
		controller.restartGame();
		startGame();
	}
	
	/**
	 * Mostra il pannello di vittoria.
	 */
	public void winGame() {
		sceneManagerController.showVictoryPanel();
	}

    /**
     * Risponde ai clic sui pulsanti.
     *
     * @param e {@code ActionEvent} dal quale e' possibile ottenere il pulsante cliccato.
     * 
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton buttonPressed = (JButton)e.getSource();
		
		MenuPanel menuPanel = frame.getMenuPanel();
		LoginPanel loginPanel = frame.getLoginPanel();
		StatsPanel statsPanel = frame.getStatsPanel();
		GameOverPanel gameOverPanel = frame.getGameOverPanel();
		VictoryPanel victoryPanel = frame.getVictoryPanel();
			
		//Menu Panel Buttons Pressed
		if (buttonPressed.equals(menuPanel.getStartButton())) resetGame();
		if (buttonPressed.equals(menuPanel.getLoginButton())) sceneManagerController.showLoginPanel();
		if (buttonPressed.equals(menuPanel.getStatsButton())) sceneManagerController.showStatsPanel();
		if (buttonPressed.equals(menuPanel.getChangeUserButton())) sceneManagerController.showLoginPanel();
		
		//Login Panel Buttons Pressed
		if (buttonPressed.equals(loginPanel.getLoginButton())) {
			
			String username = loginPanel.getFieldUsername().getText().trim();
			
			//Se username vuoto -> Warning
			if (username.equals("")) {
				loginPanel.getFieldUsername().setText("");
				JOptionPane.showMessageDialog(frame, "Insert a correct username!", "WARNING!", JOptionPane.WARNING_MESSAGE);
			}
			
			else {
				
				//Se l'User non ha gia' giocato in precedenza -> Mostra schermata di selezione avatar
				if (!controller.getUsers().containsKey(username)) {
					loginPanel.getUsernamePanel().setVisible(false);
					loginPanel.getLoginButton().setVisible(false);
					loginPanel.getBackButton().setVisible(false);
					loginPanel.getAvatarPanel().setVisible(true);
					loginPanel.getConfirmButton().setVisible(true);

					JOptionPane.showMessageDialog(frame, "Please select your avatar", "SELECT AVATAR", JOptionPane.INFORMATION_MESSAGE);
				}
				
				//Altrimenti -> Mostra schermata del Menu' aggiornata con tasto START
				else {
					User existingUser = controller.getUsers().get(username);
					controller.setUserPlaying(existingUser);
					JOptionPane.showMessageDialog(frame, "Welcome back " + username + "!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
					sceneManagerController.showMenuPanel();
					loginPanel.getFieldUsername().setText("");
				}
				
				menuPanel.getLoginButton().setVisible(false);
				menuPanel.getStartButton().setVisible(true);
				menuPanel.getStatsButton().setVisible(true);
				menuPanel.getChangeUserButton().setVisible(true);
			}	
		}
		
		if (buttonPressed.equals(loginPanel.getBackButton())) {
			loginPanel.getFieldUsername().setText("");
			sceneManagerController.showMenuPanel();
		}
		
		//Il giocatore ha scelto l'avatar -> Mostra schermata del Menu' aggiornata con tasto START
		if (buttonPressed.equals(loginPanel.getConfirmButton())) {
			String username = loginPanel.getFieldUsername().getText();
			String avatar = loginPanel.getActionCommand();
			User newUser = new User(username, avatar);
			controller.setUserPlaying(newUser);
			controller.addUser(newUser);
			JOptionPane.showMessageDialog(frame, "Welcome " + username + "!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
			sceneManagerController.showMenuPanel();
			
			//Reset LoginPanel
			loginPanel.getFieldUsername().setText("");
			loginPanel.getUsernamePanel().setVisible(true);
			loginPanel.getLoginButton().setVisible(true);
			loginPanel.getBackButton().setVisible(true);
			loginPanel.getAvatarPanel().setVisible(false);
			loginPanel.getConfirmButton().setVisible(false);
		}
		
		//Stats Panel Buttons Pressed
		if (buttonPressed.equals(statsPanel.getBackButton())) sceneManagerController.showMenuPanel();
		
		//Game Over Panel Buttons Pressed
		if (buttonPressed.equals(gameOverPanel.getRestartButton()))  resetGame();
		if (buttonPressed.equals(gameOverPanel.getExitButton()))     System.exit(0);
		
		//Victory Panel Buttons Pressed
		if (buttonPressed.equals(victoryPanel.getMenuButton())) {
			sceneManagerController.showMenuPanel();
			controller.setLevel(0);
			AudioManager.getInstance().stop("/audio/ending.wav");
			AudioManager.getInstance().play("/audio/main-title.wav");
		}
		if (buttonPressed.equals(victoryPanel.getQuitButton()))  System.exit(0);	
	}
	
	/**
	 * Imposta il {@code GameController} per questo {@code ButtonController}.
	 * 
	 * @param controller L'istanza del {@code GameController}.
	 */
	public void setController(GameController controller) {
		this.controller = controller;
	}
}
