package controller;

import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Classe che gestisce la visualizzazione e la transizione tra diverse scene del gioco
 * utilizzando un {@code CardLayout} all'interno di un {@code JPanel}.
 * 
 * Ogni scena è identificata da un nome e può essere mostrata chiamando il metodo corrispondente.
 * 
 * @author Lorenzo Zanda
 * @see CardLayout
 * @see JPanel
 */
public class SceneManagerController {
	
	private CardLayout cardLayout;
	private JPanel cardPanel;
	
	 /**
     * Costruisce un nuovo {@code SceneManagerController}.
     *
     * @param cardLayout Il {@code CardLayout} utilizzato per gestire le diverse scene.
     * @param cardPanel Il {@code JPanel} contenente le diverse scene.
     */
	public SceneManagerController(CardLayout cardLayout, JPanel cardPanel) {
		this.cardLayout = cardLayout;
		this.cardPanel = cardPanel;
	}
	
	/**
     * Mostra la scena del menu principale.
     */
    public void showMenuPanel() {
        cardLayout.show(cardPanel, "Menu");
    }

    /**
     * Mostra la scena di login.
     */
    public void showLoginPanel() {
        cardLayout.show(cardPanel, "Login");
    }

    /**
     * Mostra la scena delle statistiche.
     */
    public void showStatsPanel() {
        cardLayout.show(cardPanel, "Stats");
    }

    /**
     * Mostra la scena del gioco principale.
     */
    public void showGamePanel() {
        cardLayout.show(cardPanel, "Game");
    }

    /**
     * Mostra la scena di game over.
     */
    public void showGameOverPanel() {
        cardLayout.show(cardPanel, "Game Over");
    }

    /**
     * Mostra la scena di vittoria.
     */
    public void showVictoryPanel() {
        cardLayout.show(cardPanel, "Victory");
    }

}
