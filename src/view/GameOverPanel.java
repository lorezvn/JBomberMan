package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utilities.Constants;

/**
 * Classe che rappresenta il pannello per la schermata di Game Over.
 * Fornisce opzioni per continuare o uscire dal gioco.
 * 
 * Utilizza un'immagine di sfondo e fornisce pulsanti per la scelta dell'utente.
 * 
 * @author Lorenzo Zanda
 * @see JPanel
 */
public class GameOverPanel extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2966779310070960028L;

	/** Etichetta che visualizza il messaggio di game over. */
    private JLabel gameOverField;

    /** Pulsante per ricominciare il gioco. */
    private JButton restartButton;

    /** Pulsante per uscire dal gioco. */
    private JButton exitButton;

    /** Immagine di sfondo del pannello di game over. */
    private Image backgroundImage;
	
    /**
     * Costruttore della classe {@code GameOverPanel}.
     * Inizializza gli elementi grafici, la disposizione e le dimensioni.
     */
	public GameOverPanel() {
		
		gameOverField = new JLabel("CONTINUE?");
        gameOverField.setFont(new Font("Arial", Font.ITALIC, 48));
        gameOverField.setForeground(Color.YELLOW);
        
		restartButton = new JButton("YES");
		exitButton = new JButton("NO");
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Game Over Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START; 
        gbc.insets = new Insets(200, 10, 0, 10); 
        gbc.weightx = 1;
        gbc.weighty = 0.5; 
        gbc.gridwidth = 2; 
        add(gameOverField, gbc);
        
        //Restart Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.ipadx = 50;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 80, 150, 0);
        gbc.gridwidth = 1; 
        add(restartButton, gbc);
        
        //Exit Button
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 150, 80);
        gbc.anchor = GridBagConstraints.CENTER; 
        add(exitButton, gbc);
        
		setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
	}
	
    /**
     * Override del metodo paintComponent per disegnare l'immagine di sfondo.
     * 
     * @param g Oggetto Graphics utilizzato per disegnare.
     */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/game-over.png"));
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Restituisce il pulsante di Restart.
     * 
     * @return Pulsante di Restart.
     */
	public JButton getRestartButton() {
		return restartButton;
	}

    /**
     * Restituisce il pulsante di Exit.
     * 
     * @return Pulsante di Exit.
     */
	public JButton getExitButton() {
		return exitButton;
	}
}
