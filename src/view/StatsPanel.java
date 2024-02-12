package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utilities.Constants;
import model.User;

/**
 * Classe che rappresenta il pannello per visualizzare le statistiche di un utente.
 * Utilizza un'immagine di sfondo e fornisce informazioni come username, livello, esperienza, partite giocate, risultati e punteggio più alto.
 * 
 * @author Lorenzo Zanda
 * @see JPanel
 */
public class StatsPanel extends JPanel implements Observer {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1685114245100953604L;

	/** Username dell'utente. */
    private String username;
    
    /** Percorso dell'immagine dell'avatar dell'utente. */
    private String path;
    
    /** Livello dell'utente. */
    private int level;
    
    /** Punti esperienza dell'utente. */
    private int expPoints;
    
    /** Numero di partite giocate dall'utente. */
    private int gamesPlayed;
    
    /** Numero di partite vinte dall'utente. */
    private int gamesWon;
    
    /** Numero di partite perse dall'utente. */
    private int gamesLost;
    
    /** Punteggio massimo raggiunto dall'utente. */
    private int highscore;

    /** Etichetta per visualizzare l'username dell'utente. */
    private BomberManLabel labelUsername;

    /** Etichetta per visualizzare l'immagine dell'avatar dell'utente. */
    private JLabel labelAvatarImage;

    /** Etichetta per visualizzare il livello dell'utente. */
    private BomberManLabel labelLevel;

    /** Etichetta per visualizzare il numero di partite giocate dall'utente. */
    private BomberManLabel labelGames;

    /** Etichetta per visualizzare il punteggio massimo raggiunto dall'utente. */
    private BomberManLabel labelHighscore;

    /** Pulsante per tornare al menu principale. */
    private JButton backButton;

    /** Immagine di sfondo del pannello delle statistiche. */
    private Image backgroundImage;
	
    /**
     * Costruttore della classe {@code StatsPanel}.
     * Inizializza il pulsante di ritorno.
     */
	public StatsPanel() {
		backButton = new JButton("BACK");
		backButton.setFont(new Font("Arial", Font.BOLD, 15));
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
     * Restituisce il pulsante per tornare indietro.
     * 
     * @return Pulsante Back.
     */
	public JButton getBackButton() {
		return backButton;
	}
	
    /**
     * Aggiorna il pannello con le statistiche dell'utente.
     */
	public void updateStats() {
		
		this.removeAll();

		String expPointsString = " (" + expPoints + "<font color='green'>XP</font> / " + level*1000 + "<font color='green'>XP</font>)";
		String gamesWonString = "<font color='green'>" + gamesWon + "</font>";
		String gamesLostString = "<font color='red'>" + gamesLost + "</font>";
		
		labelUsername = new BomberManLabel("USERNAME: " + username);
		
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream(path)).getScaledInstance(Constants.SCALED_TILESIZE, Constants.SCALED_TILESIZE, Image.SCALE_SMOOTH);
	        labelAvatarImage = new JLabel(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        labelLevel = new BomberManLabel("<html>LEVEL: " + level + expPointsString + "</html>");
        labelGames = new BomberManLabel("<html>GAMES PLAYED: " + gamesPlayed + " (WON: " + gamesWonString +  " / LOST: " + gamesLostString + ")</html>");
        
        labelHighscore = new BomberManLabel("HIGHSCORE: " + highscore);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(400, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelAvatarImage, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelUsername, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelLevel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(labelGames, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(labelHighscore, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;        
        gbc.ipadx = 50;
        gbc.ipady = 20;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(backButton, gbc);
	}

	
	 /**
     * Imposta l'username dell'utente.
     *
     * @param username L'username dell'utente.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Imposta il percorso dell'immagine dell'avatar dell'utente.
     *
     * @param path Il percorso dell'immagine dell'avatar.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Imposta il livello dell'utente.
     *
     * @param level Il livello dell'utente.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Imposta i punti esperienza dell'utente.
     *
     * @param expPoints I punti esperienza dell'utente.
     */
    public void setExpPoints(int expPoints) {
        this.expPoints = expPoints;
    }


    /**
     * Imposta il numero di partite giocate dall'utente.
     *
     * @param gamesPlayed Il numero di partite giocate dall'utente.
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * Imposta il numero di partite vinte dall'utente.
     *
     * @param gamesWon Il numero di partite vinte dall'utente.
     */
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    /**
     * Imposta il numero di partite perse dall'utente.
     *
     * @param gamesLost Il numero di partite perse dall'utente.
     */
    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    /**
     * Imposta il punteggio massimo raggiunto dall'utente.
     *
     * @param highscore Il punteggio massimo dell'utente.
     */
    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    /**
     * Metodo chiamato quando l'oggetto osservato viene modificato.
     * 
     * @param o L'oggetto osservato.
     * @param arg Argomento opzionale passato dall'oggetto osservato.
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof User) {
            User user = (User) o;
            username = user.getUsername();
            path = user.getAvatarPath();
            level = user.getLevel();
            expPoints = user.getExpPoints();
            gamesPlayed = user.getGamesPlayed();
            gamesWon = user.getGamesWon();
            gamesLost = user.getGamesLost();
            highscore = user.getHighscore();
            updateStats();
        }
    }
}
