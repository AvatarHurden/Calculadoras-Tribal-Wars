package distância;

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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import database.Cores;

@SuppressWarnings("serial")
public class PanelAldeia extends JPanel {

	private JLabel labelNome;
	private JPanel coordenadas;
	private JTextField x, y;

	private GUI gui;

	/**
	 * Panel com as coordenadas da aldeia
	 * 
	 * @param nome
	 *            Nome da Aldeia (origem ou destino)
	 * @param gui
	 */
	public PanelAldeia(String nome, GUI gui) {

		this.gui = gui;

		setBackground(Cores.FUNDO_ESCURO);
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;

		labelNome = new JLabel(nome);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 5, 3, 5);
		add(labelNome, constraints);

		addPanelCoordenadas(constraints);

	}

	public void addPanelCoordenadas(GridBagConstraints c) {

		coordenadas = new JPanel(new GridBagLayout());
		coordenadas.setBackground(Cores.ALTERNAR_ESCURO);
		coordenadas
				.setBorder(new MatteBorder(1, 0, 0, 0, Cores.SEPARAR_ESCURO));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(coordenadas, c);

		GridBagConstraints constraints_coordenadas = new GridBagConstraints();

		x = new JTextField(3);
		x.setHorizontalAlignment(SwingConstants.CENTER);
		x.setDocument(new PlainDocument() {

			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// permite no máximo 3 dígitos

				if ((getLength() + str.length()) <= 3
						&& Character.isDigit(str.charAt(0))) {
					super.insertString(offset, str, attr);
				}
			}

		});
		x.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
			}

			public void insertUpdate(DocumentEvent arg0) {
				gui.calculateDistanceAndTimes();
			}

			public void removeUpdate(DocumentEvent arg0) {
				gui.calculateDistanceAndTimes();
			}
		});

		constraints_coordenadas.gridwidth = 1;
		constraints_coordenadas.gridy = 0;
		constraints_coordenadas.gridx = 0;
		constraints_coordenadas.anchor = GridBagConstraints.CENTER;
		constraints_coordenadas.insets = new Insets(5, 5, 5, 5);
		coordenadas.add(x, constraints_coordenadas);

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);

		constraints_coordenadas.fill = GridBagConstraints.VERTICAL;
		constraints_coordenadas.gridx = 1;
		constraints_coordenadas.insets = new Insets(5, 0, 5, 0);
		coordenadas.add(separator, constraints_coordenadas);

		y = new JTextField(3);
		y.setHorizontalAlignment(SwingConstants.CENTER);
		y.setDocument(new PlainDocument() {

			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// permite no máximo 3 dígitos

				if ((getLength() + str.length()) <= 3
						&& Character.isDigit(str.charAt(0))) {
					super.insertString(offset, str, attr);
				}
			}

		});
		y.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
			}

			public void insertUpdate(DocumentEvent arg0) {
				gui.calculateDistanceAndTimes();
			}

			public void removeUpdate(DocumentEvent arg0) {
				gui.calculateDistanceAndTimes();
			}
		});

		constraints_coordenadas.gridx = 2;
		constraints_coordenadas.anchor = GridBagConstraints.CENTER;
		constraints_coordenadas.insets = new Insets(5, 5, 5, 5);
		coordenadas.add(y, constraints_coordenadas);

	}

	/**
	 * Confere se a aldeia possui 3 dígitos em cada coordenada
	 */
	protected boolean hasCompleteCoordinates() {

		if (x.getText().length() < 3 || y.getText().length() < 3)
			return false;
		else
			return true;

	}

	protected int getCoordenadaX() {
		return Integer.parseInt(x.getText());
	}

	protected int getCoordenadaY() {
		return Integer.parseInt(y.getText());
	}
	
	public void reset() {
		
		x.setText("");
		y.setText("");
		
	}

}
