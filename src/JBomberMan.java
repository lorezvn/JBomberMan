import controller.MainController;
import view.BomberManFrame;

/**
 * Classe per l'avvio dell'applicazione.
 */
public class JBomberMan {
    /**
     * Metodo main che avvia l'applicazione.
     * 
     * @param args Gli argomenti della riga di comando (non utilizzati).
     */
	public static void main(String[] args) {
		System.out.println("Avvio JBomberman...");
		new MainController(new BomberManFrame());
	}
}
