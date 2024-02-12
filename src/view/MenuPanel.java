package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import utilities.Constants;

/**
 * Classe che rappresenta il pannello del menu del gioco.
 * Utilizza un'immagine di sfondo e fornisce pulsanti per avviare il gioco, effettuare il login, visualizzare le statistiche e cambiare utente.
 * 
 * @author Lorenzo Zanda
 * @see JPanel
 */
public class MenuPanel extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8209053678229796524L;

	/** Pulsante per avviare il gioco. */
    private JButton startButton;

    /** Pulsante per accedere al pannello di login. */
    private JButton loginButton;

    /** Pulsante per visualizzare le statistiche di gioco. */
    private JButton statsButton;

    /** Pulsante per cambiare utente. */
    private JButton changeUserButton;

    /** Immagine di sfondo del pannello del menu. */
    private Image backgroundImage;
	
    /**
     * Costruttore della classe {@code MenuPanel}.
     * Inizializza i pulsanti e imposta il layout del pannello.
     */
	public MenuPanel() {
		
		loginButton = new JButton("LOGIN");
		loginButton.setFont(new Font("Arial", Font.BOLD, 15));
		
		startButton = new JButton("START");
		startButton.setFont(new Font("Arial", Font.BOLD, 15));
		startButton.setVisible(false);
		
		statsButton = new JButton("STATS");
		statsButton.setFont(new Font("Arial", Font.BOLD, 15));
		statsButton.setVisible(false);
		
		changeUserButton = new JButton("CHANGE USER");
		changeUserButton.setFont(new Font("Arial", Font.BOLD, 15));
		changeUserButton.setVisible(false);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(450, 0, 10, 0); 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipadx = 50;
        gbc.ipady = 20;
        add(loginButton, gbc);

		gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(550, 50, 10, 0); 
        add(startButton, gbc);	
        
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10); 
        add(statsButton, gbc);
        
		gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(changeUserButton, gbc);	
              
		setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT+2*Constants.SCALED_TILESIZE));
		setFocusable(true);
	}
	
    /**
     * Override del metodo paintComponent per disegnare lo sfondo.
     * 
     * @param g Oggetto Graphics utilizzato per disegnare.
     */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream(("/images/cover.png")));
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Restituisce il pulsante per avviare il gioco.
     * 
     * @return Pulsante Start.
     */
	public JButton getStartButton() {
		return startButton;
	}
	
    /**
     * Restituisce il pulsante per effettuare il login.
     * 
     * @return Pulsante Login.
     */
	public JButton getLoginButton() {
		return loginButton;
	}
	
    /**
     * Restituisce il pulsante per visualizzare le statistiche.
     * 
     * @return Pulsante Stats.
     */
	public JButton getStatsButton() {
		return statsButton;
	}
	
    /**
     * Restituisce il pulsante per cambiare utente.
     * 
     * @return Pulsante Change User.
     */
	public JButton getChangeUserButton() {
		return changeUserButton;
	}
}
