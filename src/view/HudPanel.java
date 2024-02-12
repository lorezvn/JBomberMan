package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.Player;
import model.Enemy;
import model.PowerUp;
import model.PowerUpType;
import utilities.Constants;

/**
 * Classe che rappresenta il pannello per l'interfaccia utente (HUD) che mostra informazioni sullo stato del gioco.
 * Include il punteggio totale, la vita del giocatore e i potenziamenti attivi.
 * Implementa l'interfaccia {@code Observer} per ricevere notifiche sugli aggiornamenti degli oggetti osservati.
 * 
 * @see JPanel
 * @see Observer
 */
public class HudPanel extends JPanel implements Observer {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6689534511948204956L;

	/** Numero totale di vite del giocatore. */
    private int totalHp;

    /** Punteggio totale del giocatore. */
    private int totalScore = 0;

    /** Punteggio del livello corrente. */
    private int levelScore = 0;

    /** Numero totale di bombe a disposizione del giocatore. */
    private int totalBombs;

    /** Numero totale di potenziamenti della velocità del giocatore. */
    private int totalSpeed;

    /** Numero totale di potenziamenti della potenza di fuoco del giocatore. */
    private int totalFire;

    /** Sprite sheet contenente le immagini dei numeri. */
    private BufferedImage numbersSheet;

    /** Mappa associativa tra caratteri numerici e relative immagini. */
    private Map<Character, BufferedImage> numbersMap;

    /** Immagine per l'HUD. */
    private Image hudImage;
	
    /**
     * Costruttore della classe {@code HudPanel}.
     * Inizializza gli elementi grafici, le dimensioni e carica le immagini necessarie.
     */
	public HudPanel() {
		
		try {
			numbersSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/numbers-sheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		numbersMap = loadNumbersMap();
		setPreferredSize(new Dimension(Constants.WIDTH, Constants.SCALED_TILESIZE * 2));
	}
	
    /**
     * Carica le immagini dei numeri dalla sprite sheet e le associa a un carattere.
     * 
     * @return Una mappa che associa caratteri a immagini di numeri.
     */
	public Map<Character, BufferedImage> loadNumbersMap() {
		
		Map<Character, BufferedImage> numbersMap = new HashMap<Character, BufferedImage>();
		for (int i=0; i <= 9; i++) {
			BufferedImage number = numbersSheet.getSubimage((i*Constants.TILESIZE)+4, 2, 8, 12);
			numbersMap.put((char)('0' + i), number);
		}
		return numbersMap;
	}
	
    /**
     * Metodo ausiliario per convertire un numero in elemento grafico usando il font corretto.
     * 
     * @param g2 Oggetto Graphics2D utilizzato per disegnare.
     * @param padding Spazio necessario per disegnare il numero correttamente.
     * @param itemToDisplay Numero da visualizzare.
     */
	public void toDisplay(Graphics2D g2, int padding, int itemToDisplay) {
		String score = new StringBuffer(itemToDisplay + "").reverse().toString();
		// Disegna lo score 
		for (int i=0, xPos=padding; i < score.length(); i++, xPos-=8) {
			char numberChar = score.charAt(i);
			BufferedImage number = numbersMap.get(numberChar);

			int x = xPos * Constants.SCALE;
			int y = 10 * Constants.SCALE;
			int width  = number.getWidth() * Constants.SCALE;
			int height = number.getHeight() * Constants.SCALE;
			
			g2.drawImage(number, x, y, width, height, null);
		}
	}
	
    /**
     * Override del metodo paintComponent per disegnare l'HUD.
     * 
     * @param g Oggetto Graphics utilizzato per disegnare.
     */
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		try {
			hudImage = ImageIO.read(getClass().getResourceAsStream("/images/hud.png"));
			g2.drawImage(hudImage, 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//safe score
		if (totalScore + levelScore >= 99999999) {
			totalScore = 99999999;
			levelScore = 0;
		}
		
		toDisplay(g2, 112, totalScore + levelScore);
		toDisplay(g2, 32, totalHp);
		toDisplay(g2, 180, totalBombs);
		toDisplay(g2, 212, totalSpeed);
		toDisplay(g2, 244, totalFire);
	}
	
    /**
     * Gestisce l'effetto di un power-up sull'HUD.
     * 
     * @param player Giocatore che colleziona il power-up.
     * @param powerUp Power-up attivato.
     */
	private void managePowerUp(Player player, PowerUp powerUp) {
		PowerUpType type = powerUp.getType();
		switch(type) {
			case BOMBUP      -> totalBombs = player.getBombExplosions();
			case ACCELERATOR -> totalSpeed = player.getSpeed();
			case FIRE        -> totalFire = player.getExplosionRadius();
			case BOMBERMAN   -> totalHp = player.getHp();
		}
		levelScore += powerUp.getScorePoints();
	}

    /**
     * Metodo chiamato quando un oggetto osservato notifica un cambiamento.
     * 
     * @param o Oggetto osservato.
     * @param arg Argomento passato con la notifica.
     */
	@Override
	public void update(Observable o, Object arg) {
		
		//Enemy dead
		if (o instanceof Enemy) {
			Enemy enemy = (Enemy)o;
			if (!enemy.isAlive()) {
				levelScore += enemy.getScorePoints();
			}
			
		}
		//Player power-up
		else if (o instanceof Player) {
			Player player = (Player)o;
			if (arg != null && arg instanceof PowerUp) {
				PowerUp powerUp = (PowerUp)arg;
				managePowerUp(player, powerUp);
			}
			else {
				totalHp = player.getHp();
				totalSpeed = player.getSpeed();
			}
		}
	}
	
	/**
	 * Restituisce il punteggio totale.
	 * 
	 * @return Il punteggio totale.
	 */
	public int getTotalScore() {
		return totalScore;
	}
	
	/**
	 * Imposta il punteggio totale.
	 * 
	 * @param totalScore Il punteggio totale da impostare.
	 */
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
	/**
	 * Restituisce il punteggio del livello corrente.
	 * 
	 * @return Il punteggio del livello corrente.
	 */
	public int getLevelScore() {
		return levelScore;
	}
	
	/**
	 * Imposta il punteggio del livello corrente.
	 * 
	 * @param levelScore Il punteggio del livello corrente da impostare.
	 */
	public void setLevelScore(int levelScore) {
		this.levelScore = levelScore;
	}
	
	/**
	 * Imposta la vita totale del giocatore.
	 * 
	 * @param totalHp La vita totale del giocatore da impostare.
	 */
	public void setTotalHp(int totalHp) {
		this.totalHp = totalHp;
	}

	/**
	 * Imposta il numero totale di bombe.
	 * 
	 * @param totalBombs Il numero totale di bombe da impostare.
	 */
	public void setTotalBombs(int totalBombs) {
		this.totalBombs = totalBombs;
	}

	/**
	 * Imposta la velocità totale del giocatore.
	 * 
	 * @param totalSpeed La velocità totale del giocatore da impostare.
	 */
	public void setTotalSpeed(int totalSpeed) {
		this.totalSpeed = totalSpeed;
	}

	/**
	 * Imposta il raggio di esplosione totale del giocatore.
	 * 
	 * @param totalFire Il raggio di esplosione totale del giocatore da impostare.
	 */
	public void setTotalFire(int totalFire) {
		this.totalFire = totalFire;
	}
}
