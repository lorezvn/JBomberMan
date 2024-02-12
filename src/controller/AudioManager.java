package controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Classe che gestisce la riproduzione, l'interruzione e il loop
 * di file audio.
 * Utilizza il pattern "singleton" per garantire un'unica istanza globale.
 *
 * @author Stefano Faralli
 * @author Lorenzo Zanda
 */
public class AudioManager {

	private static AudioManager instance;
	private Map<String, Clip> clipsMap;

    /**
     * Restituisce l'istanza singola di {@code AudioManager} (singleton pattern).
     *
     * @return L'istanza di {@code AudioManager}.
     */
	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

    /**
     * Costruttore privato della classe {@code AudioManager} che inizializza la mappa che conterra' le clip.
     */
	private AudioManager() {
		clipsMap = new HashMap<String, Clip>();
	}

    /**
     * Riproduce un file audio specificato.
     *
     * @param filename Il percorso del file audio da riprodurre.
     */
	public void play(String filename) {

		try {
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(filename));
			AudioFormat targetFormat = new AudioFormat(
		                AudioFormat.Encoding.PCM_SIGNED,
		                audioIn.getFormat().getSampleRate(),
		                16,
		                audioIn.getFormat().getChannels(),
		                audioIn.getFormat().getChannels() * 2,
		                audioIn.getFormat().getSampleRate(),
		                false);

	        AudioInputStream pcmSignedStream = AudioSystem.getAudioInputStream(targetFormat, audioIn);
	        Clip clip = AudioSystem.getClip();
	        clip.open(pcmSignedStream);
	        clipsMap.put(filename, clip);
	        clip.start();
	        
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
    /**
     * Riproduce un file audio in loop continuo.
     *
     * @param filename Il percorso del file audio da riprodurre in loop.
     */
	public void playLoop(String filename) {

		try {
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(filename));
			AudioFormat targetFormat = new AudioFormat(
		                AudioFormat.Encoding.PCM_SIGNED,
		                audioIn.getFormat().getSampleRate(),
		                16,
		                audioIn.getFormat().getChannels(),
		                audioIn.getFormat().getChannels() * 2,
		                audioIn.getFormat().getSampleRate(),
		                false);

	        AudioInputStream pcmSignedStream = AudioSystem.getAudioInputStream(targetFormat, audioIn);
	        Clip clip = AudioSystem.getClip();
	        clip.open(pcmSignedStream);
	        clipsMap.put(filename, clip);
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	        
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
    /**
     * Interrompe la riproduzione di un file audio specificato.
     *
     * @param filename Il percorso del file audio da interrompere.
     */
	public void stop(String filename) {
		if (clipsMap.containsKey(filename)) {
			Clip clip = clipsMap.get(filename);
			if (clip.isRunning()) {
				clip.stop();
			}
		}
	}
}