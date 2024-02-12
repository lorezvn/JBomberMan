package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 * Classe che rappresenta una label personalizzata utilizzata nell'interfaccia grafica.
 * Questa classe estende {@code JLabel} e imposta alcune proprietà specifiche.
 * 
 * @author Lorenzo Zanda
 * @see JLabel
 */
public class BomberManLabel extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2109559551198123096L;

	/**
	 * Costruttore della classe {@code BomberManLabel}
	 * @param name Il testo visualizzato.
	 */
	public BomberManLabel(String name) {
		super(name);
        setFont(new Font("Arial", Font.BOLD, 20));
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setOpaque(true);
	}
}
