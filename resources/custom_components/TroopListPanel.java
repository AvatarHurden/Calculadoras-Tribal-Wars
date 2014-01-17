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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import selecionar_mundo.GUI;
import database.Cores;
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
		
		System.out.println(getPreferredSize());
		
	}
	
	private JLabel makeNameLabel() {
		
		JLabel label = new JLabel("Modelos:");
		
		label.setPreferredSize(new Dimension(54, getPreferredSize().height));
		
		return label;
		
	}
	
	private JComboBox<String> makeSelectionButton() {
		
		String[] strings = new String[2];
		
		strings[0] = "Hello";
		strings[1] = "This is a long string";
		
		WiderDropDownCombo button = new WiderDropDownCombo(strings);
		
		button.setWide(true);
		
		button.setPreferredSize(new Dimension(20,20));
		
		return button;
		
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
					
				}
				
			}
		});
		
		return button;
		
	}
	
	private JButton makeHelpButton() {
		
		final JButton button = new JButton();
		
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						GUI.class.getResource("/images/help_icon.png"))));
		
		button.setPreferredSize(new Dimension(32,20));
		
		return button;
		
		
	}
	

	
	public static void main(String args[]) {
		
		JFrame test = new JFrame();
		
		test.setVisible(true);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		test.add(new TroopListPanel(null));
		
		test.setResizable(false);
		
		test.pack();
		
		
	}
	
}
