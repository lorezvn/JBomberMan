package model;

import java.util.Observable;
import java.util.Random;

/**
 * Classe che gestisce le informazioni relative all'utente nel gioco.
 * Estende {@code Observable} per notificare gli {@code Observer} quando il suo stato viene modificato.
 * 
 * @author Lorenzo Zanda
 * @see Observable
 */
public class User extends Observable {
	
	private String username;
	private String avatar;
	
	private int level;
	private int expPoints;
	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	private int highscore;
	
    /**
     * Costruttore della classe {@code User}.
     * Usato nel caso in cui l'utente non abbia mai giocato.
     *
     * @param username Nome dell'utente.
     * @param avatar   Avatar dell'utente.
     */
	public User(String username, String avatar) {
		this.username = username;
		this.avatar = avatar;
		this.level = 1;
	}
	
    /**
     * Costruttore della classe {@code User} con parametri completi.
     * Usato nel caso in cui un utente abbia gia' giocato.
     *
     * @param username    Nome dell'utente.
     * @param avatar      Avatar dell'utente.
     * @param level       Livello dell'utente.
     * @param expPoints   Punti esperienza dell'utente.
     * @param gamesPlayed Partite giocate dall'utente.
     * @param gamesWon    Partite vinte dall'utente.
     * @param gamesLost   Partite perse dall'utente.
     * @param highscore   Punteggio massimo dell'utente.
     */
	public User(String username, String avatar, int level, int expPoints, int gamesPlayed, int gamesWon, int gamesLost, int highscore) {
		this.username = username;
		this.avatar = avatar;
		this.level = level;
		this.expPoints = expPoints;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.highscore = highscore;
	}
	
    /**
     * Aggiunge punti esperienza all'utente in base al livello superato.
     *
     * @param level Livello corrente dell'utente.
     */
	public void addExpPoints(int level) {
		int levelExp = new Random().nextInt(1, ((level+1)*100)+1);
		expPoints += new Random().nextInt(100, 301) + levelExp;
		checkLevelIncreased();
		setChanged();
		notifyObservers();
	}

    /**
     * Aggiunge una partita persa all'utente.
     */
	public void addGamesLost() {
		gamesPlayed++;
		gamesLost++;
		setChanged();
		notifyObservers();
	}
	
    /**
     * Aggiunge una partita vinta all'utente.
     */
	public void addGamesWon() {
		gamesPlayed++;
		gamesWon++;
		expPoints += new Random().nextInt(1000, 1501);
		checkLevelIncreased();
		setChanged();
		notifyObservers();
	}
	
    /**
     * Aumenta il livello dell'utente se ha ottenuto la corretta quantita' di punti esperienza.
     */
	private void checkLevelIncreased() {
		if (expPoints >= level*1000) {
			expPoints -= level*1000;
			level++;
		}
	}

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Nome dell'utente.
     */
	public String getUsername() {
		return username;
	}

    /**
     * Imposta il nome dell'utente.
     *
     * @param username Nuovo nome dell'utente.
     */
	public void setUsername(String username) {
		this.username = username;
	}

    /**
     * Restituisce l'avatar dell'utente.
     *
     * @return Avatar dell'utente.
     */
	public String getAvatar() {
		return avatar;
	}

    /**
     * Imposta l'avatar dell'utente.
     *
     * @param avatar Nuovo avatar dell'utente.
     */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

    /**
     * Restituisce il numero di partite giocate dall'utente.
     *
     * @return Numero di partite giocate dall'utente.
     */
	public int getGamesPlayed() {
		return gamesPlayed;
	}

    /**
     * Imposta il numero di partite giocate dall'utente.
     *
     * @param gamesPlayed Nuovo numero di partite giocate dall'utente.
     */
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

    /**
     * Restituisce il numero di partite vinte dall'utente.
     *
     * @return Numero di partite vinte dall'utente.
     */
	public int getGamesWon() {
		return gamesWon;
	}

    /**
     * Imposta il numero di partite vinte dall'utente.
     *
     * @param gamesWon Nuovo numero di partite vinte dall'utente.
     */
	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}

    /**
     * Restituisce il numero di partite perse dall'utente.
     *
     * @return Numero di partite perse dall'utente.
     */
	public int getGamesLost() {
		return gamesLost;
	}

    /**
     * Imposta il numero di partite perse dall'utente.
     *
     * @param gamesLost Nuovo numero di partite perse dall'utente.
     */
	public void setGamesLost(int gamesLost) {
		this.gamesLost = gamesLost;
	}

    /**
     * Restituisce il livello dell'utente.
     *
     * @return Livello dell'utente.
     */
	public int getLevel() {
		return level;
	}

    /**
     * Imposta il livello dell'utente.
     *
     * @param level Nuovo livello dell'utente.
     */
	public void setLevel(int level) {
		this.level = level;
	}

    /**
     * Restituisce i punti esperienza dell'utente.
     *
     * @return Punti esperienza dell'utente.
     */
	public int getExpPoints() {
		return expPoints;
	}

    /**
     * Imposta i punti esperienza dell'utente.
     *
     * @param expPoints Nuovi punti esperienza dell'utente.
     */
	public void setExpPoints(int expPoints) {
		this.expPoints = expPoints;
	}
	
    /**
     * Restituisce il punteggio massimo dell'utente.
     *
     * @return Punteggio massimo dell'utente.
     */
	public int getHighscore() {
		return highscore;
	}

    /**
     * Imposta un nuovo punteggio massimo all'utente, se superiore all'attuale.
     *
     * @param score Punteggio massimo da impostare.
     */
	public void setHighscore(int score) {
		if (score > highscore) this.highscore = score;
		setChanged();
		notifyObservers();
	}

    /**
     * Restituisce il percorso dell'immagine dell'avatar.
     *
     * @return Percorso dell'immagine dell'avatar.
     */
	public String getAvatarPath() {
		
		String path = "/images/";
		switch(avatar) {
			case "White Avatar" -> path += "white-avatar.png";
			case "Black Avatar" -> path += "black-avatar.png";
			case "Blue Avatar"  -> path += "blue-avatar.png";
			case "Red Avatar"   -> path += "red-avatar.png";
		}
		
		return path;
	}
	
    /**
     * Restituisce una rappresentazione testuale dell'utente.
     *
     * @return Una stringa rappresentante l'utente.
     */
	@Override
	public String toString() {
		return "User{" + "Name: " + username + ", " + "Avatar: " + avatar + ", " 
				       + "Level: "+ level + " (" + expPoints + "/" + level*1000 + "), " 
				       + "Games Played: " + gamesPlayed + " (" + "Won: " + gamesWon + ", " + "Lost: " + gamesLost + "), "
				       + "Highscore: " + highscore + "}"; 
	}
}
