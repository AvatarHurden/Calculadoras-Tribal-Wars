package custom_components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import database.Cores;

/**
 * A panel for village coordinates.
 * It has a label with the name of the village (inputed on constructor)
 * and two JTextFields for x and y coordinates.
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class CoordenadaPanel extends JPanel{

	// X and Y coordinates
	JTextField x, y;
	
	/**
	 * Creates a panel with 2 spaces for coordinates (x and y).
	 * <br>Above these spaces there is a header with the name of choice
	 * @param String Name of choice
	 */
	public CoordenadaPanel(String nome) {
		
		// Creates the textFields
		x = makeCoordinateTextField();
		y = makeCoordinateTextField();
		
		setBackground(Cores.ALTERNAR_ESCURO);
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0};
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0, 0, 0 };
		gridBagLayout.rowWeights = new double[] { 0 };
		setLayout(gridBagLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 0;
		c.gridx = 0;
		
		// Adds the name coordinate
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel(nome));
		
		namePanel.setBackground(Cores.FUNDO_ESCURO);
		namePanel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		add(namePanel, c);
		
		// Adds the coordinates
		JPanel coordinatePanel = makeCoordinatesPanel();	
		c.gridy++;
		add(coordinatePanel, c);
	}
	
	private JPanel makeCoordinatesPanel() {
		
		JPanel coordinatePanel = new JPanel(new GridBagLayout());
		coordinatePanel.setOpaque(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		
		c.insets = new Insets(5, 5, 5, 5);
		coordinatePanel.add(x, c);
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx++;
		coordinatePanel.add(separator, c);
		
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx++;
		coordinatePanel.add(y, c);

		return coordinatePanel;
	}
	
	/**
	 * Makes a coordinate field to be assigned to both x and y, since they
	 * are basically the same
	 * @return JTextField with proper formatting
	 */
	private JTextField makeCoordinateTextField() {
		
		JTextField coordinate = new JTextField(3);
		coordinate.setHorizontalAlignment(SwingConstants.CENTER);
		coordinate.setDocument(new PlainDocument() {

			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// permite no máximo 3 dígitos

				if ((getLength() + str.length()) <= 3
						&& Character.isDigit(str.charAt(0)))
					super.insertString(offset, str, attr);
				
			}
		});
		
		return coordinate;
		
	}
	
	public int getCoordenadaX() {
		if (x.getText().equals(""))
			return 0;
		else
			return Integer.parseInt(x.getText());
	}
	
	public int getCoordenadaY() {
		if (y.getText().equals(""))
			return 0;
		else
			return Integer.parseInt(y.getText());
	}
}
