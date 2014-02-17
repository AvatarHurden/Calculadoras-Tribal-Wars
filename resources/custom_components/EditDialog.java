package custom_components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import property_classes.Property_Boolean;
import property_classes.Property_Escolha;
import property_classes.Property_Number;
import property_classes.Property_UnidadeList;
import config.File_Manager;
import config.Mundo_Reader;
import database.Cores;
import database.Unidade;

/**
 * Dialog that contains info to edit a list (Mundos and ModeloTropas)
 * 
 * 
 * 
 * @author Arthur
 *
 */
public class EditDialog extends JDialog {
	
	// Pass the list of objects as one parameter: they will be used for the name and to be
	// edited
	
	// Pass another list with the properties of every object: this will be used to make the
	// right panel
	// OR
	// Interpret the varialbes for every object, creating the right panel according to it
	
	List<Object> objects;

	List<Properties> propertiesList;
	
	Map<Object, JComponent> mapping;
	
	//TODO create an interface called variable. WIthin it, different classes with types
	// that I want (2-choices, boolean, map, etc). Have each class contain a list of
	// all the variables that they have, using that as a parameter for this dialog;
	
	public EditDialog(List objects, Field variableList) {
		
		
	}
	
	private JPanel makeBooleanPanel(Property_Boolean variable) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		
		JLabel name = new JLabel(variable.getName());
		panel.add(name, c);
		
		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(variable.getValue());
		
		c.gridx++;
		panel.add(checkBox,c);
		
		return panel;
		
	}
	
	private JPanel makeEscolhaPanel(Property_Escolha variable) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		
		JLabel name = new JLabel(variable.getName());
		panel.add(name, c);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JPanel buttonPanel = new JPanel();
		
		for (String  s : variable.getOptions()) {
		
			JRadioButton button = new JRadioButton(s);
			
			if (variable.isOption(s))
				button.setSelected(true);
			
			buttonGroup.add(button);
			buttonPanel.add(button);
			
		}
		
		// Code for retrieving the selected value
		
//		Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
//		
//		while (enumeration.hasMoreElements()) {
//			
//			AbstractButton b = enumeration.nextElement();
//			
//			if (b.isSelected())
//				System.out.println(b.getText());
//			
//		}
		
		c.gridx++;
		panel.add(buttonPanel,c);
		
		return panel;
		
	}
	
	private JPanel makeNumberPanel(Property_Number variable) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		
		JLabel name = new JLabel(variable.getName());
		panel.add(name, c);
		
		JTextField txt = new JTextField(variable.getValue().toString());
		
		c.gridx++;
		panel.add(txt,c);
		
		return panel;
		
	}
	
	@SuppressWarnings("serial")
	private JPanel makeUnidadeListPanel(Property_UnidadeList variable) {
			
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		
		for (Entry<Unidade, BigDecimal> i : variable.entrySet()) {
			
			c.gridx = 0;
			panel.add(new JLabel(i.getKey().nome()), c);
			
			TroopFormattedTextField txt = new TroopFormattedTextField(9) {
				public void go() {}
			};
			
			txt.setText(i.getValue().toString());
			
			c.gridx = 1;
			panel.add(txt, c);
			
		}
		
		return panel;
		
	}
	
	/**
	 * Creates the default panel, with adequate colors, borders and layout
	 */
	private JPanel makeDefaultPanel() {
		
		JPanel panel = new JPanel();
		
		panel.setBackground(Cores.FUNDO_CLARO);
		
		panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {100, 120};
		layout.rowHeights = new int[] {20};
		layout.columnWeights = new double[]{0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(layout);
		
		return panel;
		
	}
	
	private JPanel makeFieldPanel(Field field) {
		
		JPanel panel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {100, 120};
		layout.rowHeights = new int[] {20};
		layout.columnWeights = new double[]{0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(layout);

		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		
		GridBagConstraints c = new GridBagConstraints();
		
		if (field.getType().equals(Map.class)) {
			
			// Mapping the entries to the first object, since only the name will be used
			for (Entry<Object, Object> x : ((Map<Object, Object>) field.get(objects.get(0))).entrySet()) {
				
				
				
				
			}
			
		}
		
		if (field.getType().equals(boolean.class)) {
			
			
			
			GridBagConstraints nameC = new GridBagConstraints();
			nameC.insets = new Insets(5,5,5,5);
			nameC.gridy = 0;
			nameC.gridx = 0;
			nameC.anchor = GridBagConstraints.WEST;
			
			panel.add(new JLabel("Nome"), nameC);
			
			if (modelo.getNome() != null)
				name.setText(modelo.getNome());
			
			nameC.anchor = GridBagConstraints.EAST;
			nameC.gridy++;
			nameC.gridwidth = 2;
			panel.add(name, nameC);

			
		}
		
		return panel;
	}
	
	public static void main (String args[]) {
		
		File_Manager.read();
		
		File_Manager.defineMundos();
		
		Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundo(23));
		
		File_Manager.defineModelos();
		
		try {
			EditDialog test = new EditDialog(Mundo_Reader.getMundoList());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
