package custom_components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import selecionar_mundo.GUI;
import config.ModeloTropas_Reader;
import database.Cores;
import database.ModeloTropas;
import database.Unidade;

/**
 * Panel on top of every ferramenta with the following functionality:
 * <br>- Reset the values
 * <br>- Apply and edit ModeloTropas
 * 
 * @author Arthur
 *
 */
public class ToolPanel {
	
	// The class will contain a panel for every function, and the caller will
	// decide where each one will be placed
	private JPanel resetPanel, modelosPanel, bandeiraPanel;
	
	/**
	 * 
	 * @param reset The actionListener that will be added to the reset button
	 * @param textFields A Map that contains TroopFormattedTextFields to be used in the ModeloTropas editor
	 */
	public ToolPanel(ActionListener reset, Map<Unidade, TroopFormattedTextField> textFields) {
		
		makeResetPanel(reset);
		
		modelosPanel = new ModeloTropasPanel(textFields);
		
	}
	
	private void makeResetPanel(ActionListener listener) {
		
		resetPanel = new JPanel();
		resetPanel.setOpaque(false);
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(listener);
		
		resetPanel.add(resetButton);
		
	}
	
	public JPanel getResetPanel() {
		return resetPanel;
	}
	
	public JPanel getModelosPanel() {
		return modelosPanel;
	}
	
	@SuppressWarnings("serial")
	private class ModeloTropasPanel extends JPanel {

		private Map<Unidade, TroopFormattedTextField> mapTextFields;

		private JPopupMenu popup;

		public ModeloTropasPanel(Map<Unidade, TroopFormattedTextField> textFields) {

			mapTextFields = textFields;

			popup = makePopupMenu();

			GridBagLayout layout = new GridBagLayout();
			layout.columnWidths = new int[] { 54, 20, 20 };
			layout.rowHeights = new int[] { 20 };
			layout.columnWeights = new double[] { 0, Double.MIN_VALUE };
			layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
			setLayout(layout);

			setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

			setBackground(Cores.FUNDO_ESCURO);

			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0;
			c.gridy = 0;

			c.insets = new Insets(0, 2, 0, 0);
			add(makeNameLabel(), c);

			c.gridx = 1;
			c.insets = new Insets(0, 0, 0, 0);
			add(makeSelectionButton(), c);

			c.gridx = 2;
			c.insets = new Insets(0, 0, 0, 2);
			add(makeEditButton(), c);

		}

		private JLabel makeNameLabel() {

			JLabel label = new JLabel("Modelos:");

			label.setPreferredSize(new Dimension(54, getPreferredSize().height));

			return label;

		}

		private JButton makeSelectionButton() {

			final JButton button = new JButton();

			button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/down_arrow.png"))));

			button.setPreferredSize(new Dimension(20, 20));

			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {

					int x = button.getLocation().x
							+ button.getPreferredSize().width / 2
							- popup.getPreferredSize().width / 2;

					int y = button.getLocation().y
							+ button.getPreferredSize().height;

					popup.show(button.getParent(), x, y);

				}
			});

			return button;

		}

		private JPopupMenu makePopupMenu() {

			JPopupMenu popup = new JPopupMenu();

			// Adds all the models to the dropdown menu
			for (final ModeloTropas i : ModeloTropas_Reader.getListModelos()) {
				
				popup.add(makeMenuItem(i));

			}

			return popup;

		}
		
		private JMenuItem makeMenuItem(final ModeloTropas i) {
			
			JMenuItem item = new JMenuItem(i.getNome());

			item.setName(i.getNome());

			item.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent a) {

					// Edits all the textfields according to the model
					for (Entry<Unidade, TroopFormattedTextField> e : mapTextFields
							.entrySet())
						if (i.getQuantidade(e.getKey()).equals(BigDecimal.ZERO))
							e.getValue().setText("");
						else
							e.getValue().setText(
									i.getQuantidade(e.getKey()).toString());

					// puts the focus on the first textfield (for consistency)
					mapTextFields.get(Unidade.LANCEIRO).requestFocus();
					
				}
			});
			
			return item;
			
		}

		private JButton makeEditButton() {

			final JButton button = new JButton();

			button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/edit_icon.png"))));

			button.setPreferredSize(new Dimension(20, 20));

			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					try {
						EditDialog dialog = new EditDialog(ModeloTropas_Reader.getListModelos(),
								"variableList", 0);
						
						int x = button.getLocation().x
								+ button.getPreferredSize().width / 2
								- popup.getPreferredSize().width / 2;

						int y = button.getLocation().y
								+ button.getPreferredSize().height;
						
						dialog.setLocation(x, y);
						
						popup = makePopupMenu();
						
					} catch (NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}

					

				}
			});

			return button;

		}
		
		
	}
	
}
