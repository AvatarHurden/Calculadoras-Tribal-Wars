package custom_components;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import property_classes.Property;
import property_classes.Property_Boolean;
import property_classes.Property_Escolha;
import property_classes.Property_Number;
import property_classes.Property_UnidadeList;
import config.File_Manager;
import config.Mundo_Reader;
import database.Cores;
import database.Mundo;
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

	// Pass the list of objects as one parameter: they will be used for the name
	// and to be
	// edited

	// Pass another list with the properties of every object: this will be used
	// to make the
	// right panel
	// OR
	// Interpret the varialbes for every object, creating the right panel
	// according to it

	List<Object> objects;

	Field variableList;
	
//	Map<Object, JComponent> mapping;

	// Used for setting the visibility of the information panels
	List<ObjectInterface> interfaceList = new ArrayList<ObjectInterface>();
	
	JPanel namePanel;
	
	JPanel informationPanel = new JPanel();
	
	// TODO create an interface called variable. WIthin it, different classes
	// with types
	// that I want (2-choices, boolean, map, etc). Have each class contain a
	// list of
	// all the variables that they have, using that as a parameter for this
	// dialog;

	public EditDialog(List objects, Field variableList) {
		
		this.objects = objects;
		
		this.variableList = variableList;
		
		setLayout(new GridBagLayout());

		setBackground(Cores.FUNDO_ESCURO);
		getContentPane().setBackground(Cores.FUNDO_ESCURO);
		
//		JPanel test = new JPanel(new GridBagLayout());
//		
//		test.setBackground(Cores.FUNDO_CLARO);
//		
//		GridBagConstraints c = new GridBagConstraints();
//		c.gridy = 0;
//		
//		try {
//			for (Property i : ((ArrayList<Property>) 
//					variableList.get(objects.get(0)))) {
//				
//				ObjectInterface obj = new ObjectInterface();
//				
//			}
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		ObjectInterface obj = null;
//		
//		try {
//			obj = new ObjectInterface
//					(objects.get(0),
//							((ArrayList<Property>)variableList.get(objects.get(0))));
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,0,0,0);
		
		makeScrollPanel(c);
		
		for (Object o : objects)
			addObject(o);
		
		informationPanel.setBackground(Cores.ALTERNAR_ESCURO);
		
		c.gridy++;
		add(informationPanel,c);
		
		interfaceList.get(0).setSelected(true);
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		

	}
	
	private void addObject(Object o) {
		
		try {
			ObjectInterface oi = new ObjectInterface(o,
					(ArrayList<Property>)variableList.get(o));
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTH;
			c.gridy = objects.indexOf(o);
			
			interfaceList.add(oi);
			
			namePanel.add(oi.objectName,c);
			
			c.gridy = 0;
			informationPanel.add(oi.objectInformation, c);
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
//	private void makeObjectInfoPanel(Object object) {
//		
//	
//		
//	}

	private void makeScrollPanel(GridBagConstraints c) {
		
		namePanel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 140 };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 1, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		namePanel.setLayout(layout);
		
		
		JScrollPane scroll = new JScrollPane(namePanel);
		scroll.setPreferredSize(new Dimension(160,
//				informationPanel.getPreferredSize().height));
				300));
		
		GridBagConstraints scrollC = new GridBagConstraints();
		scrollC.anchor = GridBagConstraints.NORTH;
		
//		scroll.setBackground(Cores.SEPARAR_ESCURO);
		
//		JPanel panel = new JPanel();
//	
//		panel.setBackground(Cores.ALTERNAR_ESCURO);
//		
//		panel.add(namePanel);
//		
//		scroll.add(namePanel, scrollC);
		
		add(scroll,c);
		
	}
	
	
	private class ObjectInterface {
		
		/**
		 * The object that this interface references.
		 * <br> All editing and saving of the object is done in the <class>Object
		 * Interface</class>.
		 */
		private Object object;
		
		private JPanel objectName;
		
		private JPanel objectInformation;
		
		/**
		 * Maps the objects properties to an object that contains the property's value
		 * <br>This is the mapping that is made depending on the type of property:
		 * <br>
		 * <br> Boolean : JCheckBox
		 * <br> Escolha : ButtonGroup
		 * <br> Number : JTextField
		 * <br> UnidadeList : HashMap<\Unidade, TroopFormattedTextField>
		 */
		private Map<Property, Object> variableMap = new HashMap<Property, Object>();
		
		public ObjectInterface(Object object, List<Property> list) {
			
			this.object = object;
			
			createNamePanel(object.toString());
			
			createInformationPanel(object.toString(), list);
			
		}
		
		private void createNamePanel(String s) {
			
			objectName = new JPanel();
			objectName.add(new JLabel(s));
			
			objectName.setBackground(Cores.FUNDO_CLARO);
			objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
			
			objectName.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent arg0) {}
				
				public void mousePressed(MouseEvent arg0) {}
				
				public void mouseExited(MouseEvent arg0) {}
				
				public void mouseEntered(MouseEvent arg0) {}
				
				public void mouseClicked(MouseEvent arg0) {
					
					for (ObjectInterface i : interfaceList)
						i.setSelected(false);
					
					setSelected(true);
					
//					informationPanel.removeAll();
//					
//					informationPanel.add(objectInformation);
				
				}
			});
			
		}
		
		private void createInformationPanel(String name, List<Property> list) {
			
			objectInformation = new JPanel(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0, 10, 5, 10);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridy = 0;
			
			objectInformation.add(makeNamePanel(name), c);
			c.gridy++;
			
			for (Property i : list) {
				
				if (i.getClass().equals(Property_Boolean.class)) {
					
				objectInformation.add(makeBooleanPanel((Property_Boolean) i), c);	
				c.gridy++;	
				
				} else if (i.getClass().equals(Property_Escolha.class)) {
					
				objectInformation.add(makeEscolhaPanel((Property_Escolha) i), c);		
				c.gridy++;
				
				} else if (i.getClass().equals(Property_Number.class)) {
					
				objectInformation.add(makeNumberPanel((Property_Number) i), c);	
				c.gridy++;
				
				} else if (i.getClass().equals(Property_UnidadeList.class)) {
				
				objectInformation.add(makeUnidadeListPanel((Property_UnidadeList) i), c);		
				c.gridy++;
				
				}
				
			}
			
			JButton savebutton = new JButton("Save");
			
			savebutton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					saveObejct();
					
					int i = objects.indexOf(object);
					
					System.out.println(Mundo_Reader.getMundoList().get(i)
							.getConfigText());
					
				}
			});
			
			objectInformation.add(savebutton, c);
			
			objectInformation.setBackground(Cores.ALTERNAR_ESCURO);
			objectInformation.setVisible(false);
			
		}
		
		private JPanel makeNamePanel(String s) {
			
			JPanel panel = makeDefaultPanel();
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridy = 0;
			c.gridx = 0;
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.WEST;
			
			panel.add(new JLabel("Nome"), c);
			
			JTextField nameField = new JTextField(16);
			nameField.setText(s);
			
			c.anchor = GridBagConstraints.EAST;
			c.gridy++;
			c.gridwidth = 2;
			panel.add(nameField, c);
			
			return panel;
			
		}

		private JPanel makeBooleanPanel(Property_Boolean variable) {

			JPanel panel = makeDefaultPanel();

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridy = 0;
			c.gridx = 0;
			c.gridwidth = 1;

			JLabel name = new JLabel(variable.getName());
			panel.add(name, c);

			JCheckBox checkBox = new JCheckBox();
			checkBox.setOpaque(false);
			checkBox.setSelected(variable.getValue());
			
			c.gridx++;
			panel.add(checkBox, c);

			variableMap.put(variable, checkBox);
			
			return panel;

		}

		private JPanel makeEscolhaPanel(Property_Escolha variable) {

			JPanel panel = makeDefaultPanel();

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridy = 0;
			c.gridx = 0;
			c.gridwidth = 1;

			JLabel name = new JLabel(variable.getName());
			panel.add(name, c);

			ButtonGroup buttonGroup = new ButtonGroup();

			JPanel buttonPanel = new JPanel(new GridLayout(0,1));
			buttonPanel.setOpaque(false);
			
			for (String s : variable.getOptions()) {
				
				JRadioButton button = new JRadioButton(s);
				button.setOpaque(false);
				
				if (variable.isOption(s))
					button.setSelected(true);

				buttonGroup.add(button);
				buttonPanel.add(button);

			}

			// Code for retrieving the selected value

			// Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
			//
			// while (enumeration.hasMoreElements()) {
			//
			// AbstractButton b = enumeration.nextElement();
			//
			// if (b.isSelected())
			// System.out.println(b.getText());
			//
			// }

			c.gridx++;
			panel.add(buttonPanel, c);

			variableMap.put(variable, buttonGroup);
			
			return panel;

		}

		private JPanel makeNumberPanel(Property_Number variable) {

			JPanel panel = makeDefaultPanel();

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridy = 0;
			c.gridx = 0;
			c.gridwidth = 1;

			JLabel name = new JLabel(variable.getName());
			panel.add(name, c);

			//TODO make it only accept numbers as input
			
			JTextField txt = new JTextField(variable.getValue().toString());
			txt.setColumns(5);
			
			c.gridx++;
			panel.add(txt, c);

			variableMap.put(variable, txt);
			
			return panel;

		}

		@SuppressWarnings("serial")
		private JPanel makeUnidadeListPanel(Property_UnidadeList variable) {

			JPanel panel = makeDefaultPanel();

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridy = -1;
			c.gridx = 0;
			c.gridwidth = 1;

			Map<Unidade, TroopFormattedTextField> map = new HashMap<Unidade, TroopFormattedTextField>();
			
			for (Unidade i : Unidade.values()) {

				c.gridx = 0;
				c.gridy++;
				panel.add(new JLabel(i.nome()), c);

				TroopFormattedTextField txt = new TroopFormattedTextField(9) {
					public void go() {
					}
				};

				txt.setText(variable.get(i).toString());

				c.gridx = 1;
				panel.add(txt, c);
				
				map.put(i, txt);

			}
			
			variableMap.put(variable, map);

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
			layout.columnWidths = new int[] { ,  };
			layout.rowHeights = new int[] { 20 };
			layout.columnWeights = new double[] { 1, Double.MIN_VALUE };
			layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
			panel.setLayout(layout);

			return panel;

		}

		public void setSelected(boolean isSelected) {
			
			if (isSelected) {
				objectInformation.setVisible(true);
				objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				objectName.setBackground(Cores.FUNDO_ESCURO);
			} else {
				objectInformation.setVisible(false);
				objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				objectName.setBackground(Cores.FUNDO_CLARO);
			}
		}
		
		private void saveObejct() {
			
			for (Entry<Property, Object> i : variableMap.entrySet()) {
				
				// Boolean case
				if (i.getValue().getClass().equals(JCheckBox.class)) {
					
				i.getKey().setValue(((JCheckBox) i.getValue()).isSelected());
					
				// Escolha case
				} else if (i.getValue().getClass().equals(ButtonGroup.class)) {
					
					 Enumeration<AbstractButton> enumeration = 
							 ((ButtonGroup) i.getValue()).getElements();
					
					 while (enumeration.hasMoreElements()) {
					
						 AbstractButton b = enumeration.nextElement();
					
						 if (b.isSelected())
							 i.getKey().setValue(b.getText());
					 
					 }
					
				// Number case
				} else if (i.getValue().getClass().equals(JTextField.class)) {
					
					i.getKey().setValue(((JTextField) i.getValue()).getText());
				
				// UnidadeList case 
				} else if (i.getValue().getClass().equals(HashMap.class)) {
					
					Map<Unidade, BigDecimal> map = new HashMap<Unidade, BigDecimal>();
					
					for (Entry<Unidade, TroopFormattedTextField> x 
							: ((HashMap<Unidade, TroopFormattedTextField>) i .getValue()).entrySet()) {
						
						map.put(x.getKey(), x.getValue().getValue());
						
					}
					
					i.getKey().setValue(map);
					
				}
			
				
			}
			
		}
		
	}
	
	private JPanel makeFieldPanel(Field field) {

		JPanel panel = new JPanel();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 100, 120 };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));

		GridBagConstraints c = new GridBagConstraints();

		if (field.getType().equals(Map.class)) {

			// Mapping the entries to the first object, since only the name will
			// be used
			for (Entry<Object, Object> x : ((Map<Object, Object>) field
					.get(objects.get(0))).entrySet()) {

			}

		}

		if (field.getType().equals(boolean.class)) {

			GridBagConstraints nameC = new GridBagConstraints();
			nameC.insets = new Insets(5, 5, 5, 5);
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

	
	
	public static void main(String args[]) {

		File_Manager.read();

		File_Manager.defineMundos();

		Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundo(23));

		File_Manager.defineModelos();
		
		try {
			EditDialog test = new EditDialog(Mundo_Reader.getMundoList(), 
					Mundo.class.getDeclaredField("variableList"));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
