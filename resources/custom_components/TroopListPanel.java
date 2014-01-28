package custom_components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.border.LineBorder;

import selecionar_mundo.GUI;
import config.ModeloTropas_Reader;
import config.Mundo_Reader;
import database.Cores;
import database.ModeloTropas;
import database.Mundo;
import database.Unidade;

/**
 * 
 * JPanel that contains buttons referring to saving and opening of
 * troopModels, to be inserted in any GUI.
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class TroopListPanel extends JPanel{

	private Map<Unidade, TroopFormattedTextField> mapTextFields;
	
	private JPopupMenu popup;
	
	public TroopListPanel(Map<Unidade, TroopFormattedTextField> textFields) {
		
		mapTextFields = textFields;
		
		popup = makePopupMenu();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {54, 20, 20};
		layout.rowHeights = new int[] {20};
		layout.columnWeights = new double[]{0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		setLayout(layout);
		
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		setBackground(Cores.FUNDO_ESCURO);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		
		c.insets = new Insets(0,2,0,0);
		add(makeNameLabel(), c);
		
		c.gridx = 1;
		c.insets = new Insets(0,0,0,0);
		add(makeSelectionButton(), c);
		
		c.gridx = 2;
		c.insets = new Insets(0,0,0,2);
		add(makeSaveButton(), c);
		
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
	
		button.setPreferredSize(new Dimension(20,20));
		
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				int x = button.getLocation().x + button.getPreferredSize().width/2 
						- popup.getPreferredSize().width/2;
				
				int y = button.getLocation().y + button.getPreferredSize().height;
				
				popup.show(button.getParent(), x, y);
				
			}
		});
		
		return button;
		
	}
	
	private JPopupMenu makePopupMenu() {
		
		JPopupMenu popup = new JPopupMenu();
		
		// Adds all the models to the dropdown menu
		for (final ModeloTropas i : ModeloTropas_Reader.getListModelos()) {
			JMenuItem item = new JMenuItem(i.getNome());
			
			item.setName(i.getNome());
			
			item.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent a) {
				
					// Edits all the textfields according to the model
					for (Entry<Unidade, TroopFormattedTextField> e : mapTextFields.entrySet())
						if (i.getQuantidade(e.getKey()).equals(BigDecimal.ZERO))
							e.getValue().setText("");
						else
							e.getValue().setText(
								i.getQuantidade(e.getKey()).toString());
					
					// puts the focus on the first textfield (for consistency)
					mapTextFields.get(Unidade.LANCEIRO).requestFocus();
					
				}
			});
			
			popup.add(item);
			
		}
		
		return popup;
		
	}
	
	private JButton makeSaveButton() {
		
		final JButton button = new JButton();
		
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						GUI.class.getResource("/images/save_icon.png"))));
		
		button.setPreferredSize(new Dimension(20,20));
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// Map with values in the 'ferramenta' at the time of clicking
				Map<Unidade, BigDecimal> toSave = new HashMap<Unidade, BigDecimal>();
				
				for (Unidade i : Unidade.values())
					if (mapTextFields.containsKey(i))
						toSave.put(i, mapTextFields.get(i).getValue());
					else
						toSave.put(i, BigDecimal.ZERO);
				
				// Modelo that will be used as parameter for dialog
				ModeloTropas modelo = null;
				
				for (ModeloTropas i : ModeloTropas_Reader.getListModelos()) {
					
					if (i.getList().equals(toSave)) {
						modelo = i;
						break;
					} else
						modelo = new ModeloTropas(null, toSave);
						
				}
				
				JDialog dialog = makeSaveDialog(modelo);
				
				int x = button.getLocationOnScreen().x + button.getPreferredSize().width/2 
						- dialog.getPreferredSize().width/2;
				
				int y = button.getLocationOnScreen().y + button.getPreferredSize().height;
				
				dialog.setLocation(x, y);
				
				dialog.setVisible(true);
				
			}
		});
		
		return button;
		
	}
	
	private JDialog makeEditDialog() {
		
		JDialog dialog = new JDialog();
		
		
		
		return null;
		
	}
	
	private JDialog makeSaveDialog(final ModeloTropas modelo) {
		
		//TODO Change this whole thing. The save dialog opens a divided window.
		// On the left side, all existing models and a button to create a new one
		// On the right, basically what we had as the full dialog.
		
		//TODO ModeloTropas as parameter, checking on button if it matches an existing one
		// If it does, use this as an editor. If not, a creator. Somehow not allow 2 with
		// same name, or same units (checking when clicking save)
		
		// TextFields
		final JTextField name = new JTextField(16);
		final Map<Unidade, TroopFormattedTextField> saveMap = new HashMap<Unidade, TroopFormattedTextField>();
		JButton save, cancel;
		
		final JDialog dialog = new JDialog();
		
		dialog.getContentPane().setBackground(Cores.FUNDO_CLARO);
	
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {100, 120};
		layout.rowHeights = new int[] {20};
		layout.columnWeights = new double[]{0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		dialog.setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 2;
		
		// Creating JLabel that describes the action
		
		JLabel infoPanel = new JLabel();
//		System.out.println(infoPanel.getFont().getName());
		infoPanel.setFont(new Font("Dialog", 0, 16));
		
		if (ModeloTropas_Reader.getListModelos().contains(modelo))
			infoPanel.setText("Editando \""+modelo.getNome()+"\"");
		else
			infoPanel.setText("Criando novo modelo");
		
		dialog.add(infoPanel,c);
		
		// Creating name place
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(layout);
		namePanel.setOpaque(false);
		namePanel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		
		GridBagConstraints nameC = new GridBagConstraints();
		nameC.insets = new Insets(5,5,5,5);
		nameC.gridy = 0;
		nameC.gridx = 0;
		nameC.anchor = GridBagConstraints.WEST;
		
		namePanel.add(new JLabel("Nome"), nameC);
		
		if (modelo.getNome() != null)
			name.setText(modelo.getNome());
		
		nameC.anchor = GridBagConstraints.EAST;
		nameC.gridy++;
		nameC.gridwidth = 2;
		namePanel.add(name, nameC);
		
		c.gridy++;
		dialog.add(namePanel,c);
		
		// Creating unit panels
		
		JPanel unitsPanel = new JPanel(new GridBagLayout());
		unitsPanel.setOpaque(false);
		unitsPanel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		
		for (Unidade i : Unidade.values()) {
			
			JPanel unitPanel = new JPanel();
			unitPanel.setLayout(layout);
			unitPanel.setOpaque(false);
			
			GridBagConstraints unitC = new GridBagConstraints();
			unitC.gridy = 0;
			unitC.gridx = 0;
			
			unitPanel.add(new JLabel(i.nome()), unitC);
			
			saveMap.put(i, new TroopFormattedTextField(9) {
				public void go() {}
			});
		
			// puting the right value in jtextfield
			if (!modelo.getList().isEmpty() && !modelo.getList().get(i).equals(BigDecimal.ZERO))
				saveMap.get(i).setText(modelo.getList().get(i).toString());
			
			unitC.gridx++;
			unitPanel.add(saveMap.get(i), unitC);
			
			if (i.equals(Unidade.LANCEIRO))
				c.insets = new Insets(5,5,2,5);
			else if (i.equals(Unidade.NOBRE) || 
					(Mundo_Reader.MundoSelecionado.hasMilícia() && i.equals(Unidade.MILÍCIA)))
				c.insets = new Insets(2,5,5,5);
			else
				c.insets = new Insets(2,5,2,5);
			
			c.gridy++;
			unitsPanel.add(unitPanel, c);
			
		}
		
		c.gridy++;
		c.insets = new Insets(10,10,10,10);
		dialog.add(unitsPanel, c);
		
		// Adding save and cancel buttons
		
		save = new JButton("Salvar");
		
		save.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				// Para trabalhar como editor
				if (ModeloTropas_Reader.getListModelos().contains(modelo)) {
					
					
					
				}
				
				
				Map<Unidade,BigDecimal> map = new HashMap<Unidade,BigDecimal>();
				
				for (Unidade i : Unidade.values())
					if (mapTextFields.containsKey(i))
						map.put(i, saveMap.get(i).getValue());
					else
						map.put(i, BigDecimal.ZERO);
				
				ModeloTropas modelo = new ModeloTropas(name.getText(),map);
				
				ModeloTropas_Reader.addModelo(modelo);
				
				popup = makePopupMenu();
				
				dialog.dispose();
				
			}
		});
		
		c.gridwidth = 1;
		c.gridy++;
		dialog.add(save,c);
		
		dialog.setModal(true);
		
		dialog.pack();
		
		return dialog;
		
	}
	
	private JButton makeHelpButton() {
		
		final JButton button = new JButton();
		
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						GUI.class.getResource("/images/help_icon.png"))));
		
		button.setPreferredSize(new Dimension(32,20));
		
		return button;
		
		
	}
	
}
