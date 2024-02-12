package view;

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

/**
 * Classe che rappresenta il pannello di vittoria.
 * Utilizza un'immagine di sfondo e fornisce pulsanti per tornare al menu principale o uscire dal gioco.
 * 
 * @author Lorenzo Zanda
 * @see JPanel
 */
public class VictoryPanel extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5142214097189275274L;

	/** Pulsante per tornare al menu principale. */
    private JButton menuButton;

    /** Pulsante per uscire dal gioco. */
    private JButton quitButton;

    /** Immagine di sfondo del pannello della vittoria. */
    private Image backgroundImage;
	
    /**
     * Costruttore della classe {@code VictoryPanel}.
     * Inizializza i pulsanti e imposta il layout del pannello.
     */
	public VictoryPanel() {
		
		menuButton = new JButton("MENU");
		menuButton.setFont(new Font("Arial", Font.BOLD, 15));
		
		quitButton = new JButton("QUIT");
		quitButton.setFont(new Font("Arial", Font.BOLD, 15));
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.ipadx = 50;
        gbc.ipady = 20;
        add(menuButton, gbc);

		gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(quitButton, gbc);	
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
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/victory.png"));
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Restituisce il pulsante per tornare al menu principale.
     * 
     * @return Pulsante per il menu.
     */
	public JButton getMenuButton() {
		return menuButton;
	}

    /**
     * Restituisce il pulsante per uscire dal gioco.
     * 
     * @return Pulsante per uscire.
     */
	public JButton getQuitButton() {
		return quitButton;
	}
}
