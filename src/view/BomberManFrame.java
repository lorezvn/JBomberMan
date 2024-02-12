package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.*;
import controller.AudioManager;

/**
 * Classe che rappresenta il frame principale del gioco.
 * Gestisce il layout e la visualizzazione dei pannelli di gioco.
 * 
 * @author Lorenzo Zanda
 * @see JFrame
 */
public class BomberManFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6112152567751530621L;

	/** Pannello del menu principale. */
    private MenuPanel menuPanel;

    /** Pannello delle statistiche. */
    private StatsPanel statsPanel;

    /** Pannello di gioco principale. */
    private BomberManPanel gamePanel;

    /** Pannello di game over. */
    private GameOverPanel gameOverPanel;

    /** Pannello dell'HUD. */
    private HudPanel hudPanel;

    /** Pannello di login degli utenti. */
    private LoginPanel loginPanel;

    /** Pannello della schermata di vittoria. */
    private VictoryPanel victoryPanel;

    /** Pannello contenitore che contiene il pannello di gioco e l'HUD. */
    private JPanel container;

    /** Card layout utilizzato per gestire la visualizzazione dei pannelli. */
    private CardLayout cardLayout;

    /** Pannello per visualizzare i vari pannelli. */
    private JPanel cardPanel;
	/**
	 * Costruttore della classe {@code BomberManFrame}.
	 * Inizializza il frame con i pannelli necessari e imposta alcune proprieta'.
	 */
	public BomberManFrame() {
		
		super("JBomberMan");
		setLayout(new BorderLayout());
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		
		menuPanel = new MenuPanel();
		statsPanel = new StatsPanel();
		gameOverPanel = new GameOverPanel();
		gamePanel = new BomberManPanel();
		hudPanel = new HudPanel();
		container = new JPanel();
		loginPanel = new LoginPanel();
		victoryPanel = new VictoryPanel();
		
		container.setLayout(new BorderLayout());
		container.add(gamePanel, BorderLayout.CENTER);
		container.add(hudPanel, BorderLayout.NORTH);
		
		cardPanel.add(menuPanel, "Menu");
		cardPanel.add(statsPanel, "Stats");
		cardPanel.add(loginPanel, "Login");
		cardPanel.add(container, "Game");
		cardPanel.add(gameOverPanel, "Game Over");
		cardPanel.add(victoryPanel, "Victory");
		
		add(cardPanel, BorderLayout.CENTER);
		pack();
		
		AudioManager.getInstance().play("/audio/main-title.wav");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("resources/images/bomberman-icon.png").getImage());
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	/**
	 * Restituisce il card layout utilizzato nel frame.
	 * 
	 * @return il card layout.
	 */
	public CardLayout getCardLayout() {
		return cardLayout;
	}
	
	/**
	 * Restituisce il pannello che utilizza il card layout.
	 * 
	 * @return il pannello.
	 */
	public JPanel getCardPanel() {
		return cardPanel;
	}

	/**
	 * Restituisce il pannello del menu.
	 * 
	 * @return il pannello del menu.
	 */
	public MenuPanel getMenuPanel() {
		return menuPanel;
	}
	
	/**
	 * Restituisce il pannello delle statistiche.
	 * 
	 * @return il pannello delle statistiche.
	 */
	public StatsPanel getStatsPanel() {
		return statsPanel;
	}

	/**
	 * Restituisce il pannello di gioco.
	 * 
	 * @return il pannello di gioco.
	 */
	public BomberManPanel getGamePanel() {
		return gamePanel;
	}

	/**
	 * Restituisce il pannello di game over.
	 * 
	 * @return il pannello di game over.
	 */
	public GameOverPanel getGameOverPanel() {
		return gameOverPanel;
	}	
	
	/**
	 * Restituisce il pannello HUD (Heads-Up Display).
	 * 
	 * @return il pannello HUD.
	 */
	public HudPanel getHudPanel() {
		return hudPanel;
	}
	
	/**
	 * Restituisce il pannello di login.
	 * 
	 * @return il pannello di login.
	 */
	public LoginPanel getLoginPanel() {
		return loginPanel;
	}
	
	/**
	 * Restituisce il pannello di vittoria.
	 * 
	 * @return il pannello di vittoria.
	 */
	public VictoryPanel getVictoryPanel() {
		return victoryPanel;
	}
}


