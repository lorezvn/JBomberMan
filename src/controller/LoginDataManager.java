package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import model.User;

/**
 * Classe che gestisce il salvataggio e il caricamento degli utenti tramite file di testo.
 * La classe fornisce metodi statici per salvare e caricare la mappa degli utenti.
 * 
 * @author Lorenzo Zanda
 * @see User
 */
public class LoginDataManager {
	
	private static final String FILE_PATH = "resources/login_data.txt";
	
    /**
     * Salva la mappa degli utenti su file.
     *
     * @param users La mappa degli utenti da salvare.
     */
	public static void saveUsers(Map<String, User> users) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," 
                		   + user.getAvatar() + ","
                		   + user.getLevel() + ","
                		   + user.getExpPoints() + ","
                		   + user.getGamesPlayed() + "," 
                		   + user.getGamesWon() + ","
                		   + user.getGamesLost() + ","
                		   + user.getHighscore());
                writer.newLine();
            }
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Carica la mappa degli utenti da file.
     *
     * @return La mappa degli utenti caricata da file.
     */
	public static Map<String, User> loadUsers() {
		 
        Map<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    String username = data[0];
                    String avatar = data[1];
                    int level = Integer.parseInt(data[2]);
                    int expPoints = Integer.parseInt(data[3]);
                    int gamesPlayed = Integer.parseInt(data[4]);
                    int gamesWon = Integer.parseInt(data[5]);
                    int gamesLost = Integer.parseInt(data[6]);
                    int highscore = Integer.parseInt(data[7]);
                   
                    users.put(username, new User(username, avatar, level, expPoints, gamesPlayed, gamesWon, gamesLost, highscore));
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }	
        
        return users;
   }
}
