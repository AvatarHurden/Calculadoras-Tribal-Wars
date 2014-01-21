package custom_components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	
	private Map<Unidade, BigDecimal> mapQuantities;
	
	public TroopListPanel(Map<Unidade, TroopFormattedTextField> textFields) {
		
		mapTextFields = textFields;
		
		addSampleModeloTropas();
		
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
		add(makeSelectionButton(makePopupMenu()), c);
		
		c.gridx = 2;
		c.insets = new Insets(0,0,0,2);
		add(makeSaveButton(), c);
		
	}
	
	private JLabel makeNameLabel() {
		
		JLabel label = new JLabel("Modelos:");
		
		label.setPreferredSize(new Dimension(54, getPreferredSize().height));
		
		return label;
		
	}
	
	private JButton makeSelectionButton(final JPopupMenu popup) {
		
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
		
		JButton button = new JButton();
		
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						GUI.class.getResource("/images/save_icon.png"))));
		
		button.setPreferredSize(new Dimension(20,20));
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				for (Entry<Unidade, TroopFormattedTextField> i : mapTextFields.entrySet()) {
					
					System.out.println(i.getKey()+": "+i.getValue().getValue());
					
					System.out.println("hello");
					
				}
				
			}
		});
		
		return button;
		
	}
	
	private void addSampleModeloTropas() {
		
		Map<Unidade, BigDecimal> map1 = new HashMap<Unidade, BigDecimal>();
		
		for (Unidade i : Unidade.values())
			map1.put(i, new BigDecimal("10"));
		
		ModeloTropas_Reader.getListModelos().add(new ModeloTropas("10", map1));
		
		Map<Unidade, BigDecimal> map2 = new HashMap<Unidade, BigDecimal>();
		
		for (Unidade i : Unidade.values())
			map2.put(i, new BigDecimal("20"));
		
		ModeloTropas_Reader.getListModelos().add(new ModeloTropas("20", map2));
		
		Map<Unidade, BigDecimal> map3 = new HashMap<Unidade, BigDecimal>();
		
		for (Unidade i : Unidade.values())
			map3.put(i, new BigDecimal("30"));
		
		ModeloTropas_Reader.getListModelos().add(new ModeloTropas("30", map3));
		
		Map<Unidade, BigDecimal> map4 = new HashMap<Unidade, BigDecimal>();
		
		for (Unidade i : Unidade.values())
			map4.put(i, new BigDecimal("1000"));
		
		ModeloTropas_Reader.getListModelos().add(new ModeloTropas("Tudo mil", map4));
		
		ModeloTropas_Reader.setMap();
		
		
		
	}
	
	private JButton makeHelpButton() {
		
		final JButton button = new JButton();
		
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						GUI.class.getResource("/images/help_icon.png"))));
		
		button.setPreferredSize(new Dimension(32,20));
		
		return button;
		
		
	}
	
}
