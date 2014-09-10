package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/**
 * A panel for village coordinates.
 * It has a label with the name of the village (inputed on constructor)
 * and two JTextFields for x and y coordinates.
 * 
 * @author Arthur
 *
 */
public class CoordenadaPanel extends JPanel{

	// X and Y coordinates
	private IntegerFormattedTextField x, y;
	
	private OnChange onChange;
	
	JSeparator separator;
	
	/**
	 * Creates a panel with 2 spaces for coordinates (x and y).
	 * <br>Above these spaces there is a header with the name of choice
	 * @param String Name of choice
	 */
	public CoordenadaPanel(String nome, OnChange onChange) {	
		this(nome, 0, 0, onChange);
	}
	
	/**
	 * Creates a panel with 2 spaces for coordinates (x and y).
	 * <br>Above these spaces there is a header with the name of choice
	 * @param String Name of choice
	 * @param x, y Valores iniciais
	 */
	public CoordenadaPanel(String nome, int x, int y, OnChange onChange) {
		
		if (onChange == null)
			this.onChange = new OnChange() {
				public void run() {}
			};
		else
			this.onChange = onChange;
		
		// Creates the textFields
		this.x = makeCoordinateTextField(x);
		this.y = makeCoordinateTextField(y);
		
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
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 0;
		c.gridx = 0;

		// Adds the name coordinate

		if (nome != null) {

			JPanel namePanel = new JPanel();
			namePanel.add(new JLabel(nome));

			namePanel.setBackground(Cores.FUNDO_ESCURO);
			namePanel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));

			add(namePanel, c);

		}
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
		
		separator = new JSeparator(SwingConstants.VERTICAL);
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
	private IntegerFormattedTextField makeCoordinateTextField(int value) {
		IntegerFormattedTextField coordinate = new IntegerFormattedTextField(value, 3, onChange);
		
		coordinate.setHorizontalAlignment(SwingConstants.CENTER);
	
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
	
	public void setCoordenadas(int x, int y) {
		
		this.x.setText(String.valueOf(x));
		this.y.setText(String.valueOf(y));
		
	}
	
	public void reset() {
		
		y.setText("");
		x.setText("");
		
	}
	
	public void setEnabled(boolean enabled) {
		
		y.setEnabled(enabled);
		x.setEnabled(enabled);
		
		separator.setForeground(enabled ? Color.BLACK : Color.gray);
		
	}
}
