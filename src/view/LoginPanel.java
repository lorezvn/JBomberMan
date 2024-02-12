package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import utilities.Constants;

/**
 * Classe che rappresenta il pannello per la gestione del login degli utenti.
 * Utilizza un'immagine di sfondo e consente agli utenti di inserire un nome utente e selezionare un avatar prima di iniziare il gioco.
 * 
 * @author Lorenzo Zanda
 * @see JPanel
 */
public class LoginPanel extends JPanel{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 325924290163998913L;

	/** Pannello per l'inserimento del nome utente. */
    private JPanel usernamePanel;

    /** Pannello per la selezione dell'avatar. */
    private JPanel avatarPanel;

    /** Etichetta per il campo del nome utente. */
    private BomberManLabel labelUsername;

    /** Campo di inserimento del nome utente. */
    private JTextField fieldUsername;

    /** Etichetta per la selezione dell'avatar. */
    private BomberManLabel labelAvatar;

    /** Lista di etichette per gli avatar disponibili. */
    private List<JLabel> avatarLabels;

    /** Lista di bottoni opzione per gli avatar disponibili. */
    private List<JRadioButton> avatarButtons;

    /** Gruppo di bottoni per garantire la selezione singola dell'avatar. */
    private ButtonGroup avatarButtonGroup;

    /** Pulsante per avviare la procedura di login. */
    private JButton loginButton;

    /** Pulsante per tornare indietro nella procedura di login. */
    private JButton backButton;

    /** Pulsante per confermare l'avatar e completare la procedura di login. */
    private JButton confirmButton;

    /** Immagine di sfondo del pannello di login. */
    private Image backgroundImage;

    /**
     * Costruttore della classe {@code LoginPanel}.
     * Inizializza tutti gli elementi necessari per il pannello di login.
     */
    public LoginPanel() {
    	
        setLayout(new GridBagLayout());

        avatarLabels = new ArrayList<>();
        avatarButtons = new ArrayList<>();
        avatarButtonGroup = new ButtonGroup();
        
        addAvatar("White Avatar", "/images/white-avatar.png");
        addAvatar("Black Avatar", "/images/black-avatar.png");
        addAvatar("Blue Avatar", "/images/blue-avatar.png");
        addAvatar("Red Avatar", "/images/red-avatar.png");

        avatarButtons.get(0).setSelected(true);
        
        fieldUsername = new JTextField(20);
        fieldUsername.setFont(new Font("Arial", Font.PLAIN, 15));

        labelUsername = new BomberManLabel("INSERT USERNAME");
        labelUsername.setLabelFor(fieldUsername);
        
        labelAvatar = new BomberManLabel("SELECT AVATAR");

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setMnemonic(KeyEvent.VK_ENTER);

        backButton = new JButton("BACK");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        
        confirmButton = new JButton("CONFIRM");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 15));
        confirmButton.setMnemonic(KeyEvent.VK_ENTER);
        confirmButton.setVisible(false);

        // Creazione del pannello per labelUsername e fieldUsername
        usernamePanel = new JPanel(new GridBagLayout());
        usernamePanel.setOpaque(false);
        
        // Creazione del pannello per labelAvatar, radioButtons e immagini
        avatarPanel = new JPanel(new GridBagLayout());
        avatarPanel.setOpaque(false);
        avatarPanel.setVisible(false);

        //Layout usernamePanel
        GridBagConstraints gbcUsername = new GridBagConstraints();
        gbcUsername.gridx = 0;
        gbcUsername.gridy = 0;
        gbcUsername.insets = new Insets(0, 0, 0, 20);
        gbcUsername.anchor = GridBagConstraints.LINE_START;
        usernamePanel.add(labelUsername, gbcUsername);

        gbcUsername.gridx = 1;
        gbcUsername.gridy = 0;
        gbcUsername.anchor = GridBagConstraints.LINE_START;
        usernamePanel.add(fieldUsername, gbcUsername);

        //Layout avatarPanel
        GridBagConstraints gbcAvatar = new GridBagConstraints();
        gbcAvatar.gridx = 0;
        gbcAvatar.gridy = 0;
        gbcAvatar.insets = new Insets(0, 0, 0, 110);
        gbcAvatar.anchor = GridBagConstraints.LINE_START;
        avatarPanel.add(labelAvatar, gbcAvatar);
        avatarPanel.setVisible(false);

        int col = 1;
        gbcAvatar.gridy = 0;
        gbcAvatar.anchor = GridBagConstraints.LINE_END;
        gbcAvatar.insets = new Insets(0, 0, 0, 0);
        for (int i = 0; i < avatarLabels.size(); i++) {
            JLabel label = avatarLabels.get(i);
            JRadioButton radioButton = avatarButtons.get(i);
            col++;
            gbcAvatar.gridx = col;
            avatarPanel.add(label, gbcAvatar);
            col++;
            gbcAvatar.gridx = col;
            avatarPanel.add(radioButton, gbcAvatar);
        }

        //Layout LoginPanel
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(500, 100, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernamePanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(500, 100, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(avatarPanel, gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.ipadx = 50;
        gbc.ipady = 20;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(backButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(loginButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(confirmButton, gbc);
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
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/cover.png"));
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Metodo di utilita' per aggiunge un avatar selezionabile al pannello.
     * 
     * @param command   Stringa associata all'avatar.
     * @param imagePath Percorso dell'immagine dell'avatar.
     */
    private void addAvatar(String command, String imagePath) {
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream(imagePath)).getScaledInstance(Constants.SCALED_TILESIZE, Constants.SCALED_TILESIZE, Image.SCALE_SMOOTH);
	        JLabel avatarLabel = new JLabel(new ImageIcon(img));
	        avatarLabels.add(avatarLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}

        JRadioButton avatarButton = new JRadioButton();
        avatarButton.setActionCommand(command);
        avatarButtons.add(avatarButton);

        avatarButtonGroup.add(avatarButton);
    }

    /**
     * Restituisce la label per l'inserimento dell'username.
     * 
     * @return Label per l'username.
     */
	public JLabel getLabelUsername() {
		return labelUsername;
	}

    /**
     * Restituisce il campo di testo per l'inserimento dell'username.
     * 
     * @return Campo di testo per l'username.
     */
	public JTextField getFieldUsername() {
		return fieldUsername;
	}
	
	 /**
     * Restituisce il pulsante di login.
     * 
     * @return Pulsante di login.
     */
	public JButton getLoginButton() {
		return loginButton;
	}
	
    /**
     * Restituisce il pulsante di ritorno.
     * 
     * @return Pulsante di ritorno.
     */
	public JButton getBackButton() {
		return backButton;
	}
	
    /**
     * Restituisce il pulsante di conferma.
     * 
     * @return Pulsante di conferma.
     */
	public JButton getConfirmButton() {
		return confirmButton;
	}
	
    /**
     * Restituisce il pannello per l'inserimento dell'username.
     * 
     * @return Pannello dell'username.
     */
	public JPanel getUsernamePanel() {
		return usernamePanel;
	}
	
    /**
     * Restituisce il pannello per la selezione dell'avatar.
     * 
     * @return Pannello dell'avatar.
     */
	public JPanel getAvatarPanel() {
		return avatarPanel;
	}
	
    /**
     * Restituisce la lista di pulsanti per la selezione dell'avatar.
     * 
     * @return Lista di pulsanti per l'avatar.
     */
	public List<JRadioButton> getAvatarButtons() {
		return avatarButtons;
	}
	
    /**
     * Restituisce l'action command del pulsante selezionato per l'avatar.
     * 
     * @return Action command del pulsante avatar selezionato.
     */
	public String getActionCommand() {
		return avatarButtons.stream()	
			         		.filter(JRadioButton::isSelected)
			         		.map(button -> button.getActionCommand())
			         		.findFirst()
			         		.orElse(null);
	}
}
