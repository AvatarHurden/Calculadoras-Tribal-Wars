package custom_components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
	private JPanel bandeiraPanel;

	//TODO check if this is necessary
	private List<JPanel> resetPanelList = new ArrayList<JPanel>();
	
	private List<ModeloTropasPanel> modelosPanelList = new ArrayList<ModeloTropasPanel>();
	
	
	public ToolPanel() {}
	
	private JPanel makeResetPanel(ActionListener listener) {
		
		JPanel resetPanel = new JPanel();
		resetPanel.setOpaque(false);
		
		JButton resetButton = new JButton("Resetar");
		resetButton.addActionListener(listener);
		
		resetPanel.add(resetButton);
		resetPanelList.add(resetPanel);
		
		return resetPanel;
		
	}
	
	/**
	 * Cria um panel com um botão com o nome "Reset" que, quando clicado, faz a ação dada
	 * 
	 * @param listener Ação a ser feita no clique
	 * @return Um Panel com o botão 
	 */
	public JPanel addResetPanel(ActionListener action) {
		
		return makeResetPanel(action);
		
	}
	
	/**
	 * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, além de editá-los
	 * 
	 * @param edit Se o painel possui o botão de edição
	 * @param textFields  Map com TroopFormattedTextField ligados a unidades, para utilizar os Modelos
	 * @return Um JPanel com as coisas faladas
	 */
	public JPanel addModelosPanel(boolean edit, Map<Unidade, TroopFormattedTextField> textFields) {
		
		ModeloTropasPanel panel = new ModeloTropasPanel(edit, textFields);
		
		modelosPanelList.add(panel);
		
		return panel;
		
	}
	
	/**
	 * Refreshes the option menu to reflect all changes that might have occurred
	 */
	public void refresh() {
		
		for (ModeloTropasPanel i : modelosPanelList)
			i.makePopupMenu();
		
	}
	
	@SuppressWarnings("serial")
	private class ModeloTropasPanel extends JPanel {

		private Map<Unidade, TroopFormattedTextField> mapTextFields;

		private JPopupMenu popup;

		public ModeloTropasPanel(boolean edit, Map<Unidade, TroopFormattedTextField> textFields) {

			mapTextFields = textFields;

			makePopupMenu();

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
			
			if (!edit)
				c.gridwidth = 2;
			c.insets = new Insets(0, 0, 0, 0);
			c.gridx = 1;
			add(makeSelectionButton(), c);

			if (edit) {
				c.insets = new Insets(0, 0, 0, 2);
				c.gridx = 2;
				add(makeEditButton(), c);
			}
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

		private void makePopupMenu() {

			popup = new JPopupMenu();

			// Adds all the models to the dropdown menu
			for (final ModeloTropas i : ModeloTropas_Reader.getListModelos()) {
				
				popup.add(makeMenuItem(i));

			}

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
						
						new EditDialog(ModeloTropas.class,
								ModeloTropas_Reader.getListModelos(), "variableList", 0);
						
						makePopupMenu();
						
						refresh();
						
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
						e.printStackTrace();
					}

				}
			});

			return button;

		}
		
	}
	
}
