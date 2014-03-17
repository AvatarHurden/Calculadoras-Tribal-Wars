package custom_components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import property_classes.Property;
import property_classes.Property_Boolean;
import property_classes.Property_Escolha;
import property_classes.Property_Nome;
import property_classes.Property_Number;
import property_classes.Property_UnidadeList;
import selecionar_mundo.GUI;
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

	Map<Object, ArrayList<Property>> variableMap;

	Field variableField;
	
//	Map<Object, JComponent> mapping;

	// Used for setting the visibility of the information panels
	List<ObjectInterface> interfaceList = new ArrayList<ObjectInterface>();
	
	private ObjectInterface selectedInterface;
	
	JPanel namePanel;
	
	JScrollPane scroll;
	
	JPanel informationPanel = new JPanel();
	
	int listNumber = 0;
	
	// TODO create an interface called variable. WIthin it, different classes
	// with types
	// that I want (2-choices, boolean, map, etc). Have each class contain a
	// list of
	// all the variables that they have, using that as a parameter for this
	// dialog;

	public EditDialog(List objects, Field variableField) {
		
		this.objects = objects;
		
		this.variableField = variableField;
		
		variableMap = new HashMap<Object, ArrayList<Property>>();
		
		for(Object o : objects)
			try {
				variableMap.put(o, (ArrayList<Property>)variableField.get(o));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		
		setLayout(new GridBagLayout());

		getContentPane().setBackground(Cores.ALTERNAR_ESCURO);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,0,0,0);
		
		makeScrollPanel(c);
		
		for (Object o : objects){
			createInterface(o);
			addInterfaceToScroll(interfaceList.get(objects.indexOf(o)), listNumber++);
		}
		
		informationPanel.setBackground(Cores.ALTERNAR_ESCURO);
		
		c.gridy++;
		add(informationPanel,c);
		
		interfaceList.get(0).setSelected(true);
		
		c.gridy++;
		c.gridwidth = 2;
//		c.insets = new Insets(10,0,0,0);
		add(makeEditPanel(), c);
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void createInterface(Object o) {
		
		ObjectInterface oi = new ObjectInterface(o, variableMap.get(o));
		
		interfaceList.add(oi);
		
	}
	
	private void addInterfaceToScroll(ObjectInterface oi, int position) {
		
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTH;
			c.gridy = position;		
			
			namePanel.add(oi.objectName,c);
			
			c.gridy = 0;
			informationPanel.add(oi.objectInformation, c);
		
	}
	
	private void removeInterfaceFromScroll(ObjectInterface oi){
		
		namePanel.remove(oi.objectName);
		informationPanel.remove(oi.objectInformation);
		
	}

	private void makeScrollPanel(GridBagConstraints c) {
		
		namePanel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 140 };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 1, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		namePanel.setLayout(layout);
		
		
		scroll = new JScrollPane(namePanel);
		scroll.setPreferredSize(new Dimension(160,
				informationPanel.getPreferredSize().height));
		
		add(scroll,c);
		
	}
	
	private JPanel makeEditPanel() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_CLARO);
		panel.setBorder(new MatteBorder(3, 0, 0, 0, Cores.SEPARAR_ESCURO));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 160, informationPanel.getPreferredSize().width };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 1, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,0,10,0);
		c.gridy = 0;
		c.gridx = 0;
		
		JButton newButton = new JButton("Novo");
		
		newButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					Object obj = objects.get(0).getClass().newInstance();
						
					variableMap.put(obj, (ArrayList<Property>)variableField.get(obj));
					
					createInterface(obj);
					
					addInterfaceToScroll(interfaceList.get(interfaceList.size()-1),
							interfaceList.size()-1);
					
//					repaint();
					pack();
					
					scroll.getVerticalScrollBar().setValue(
							scroll.getVerticalScrollBar().getMaximum());
					
					selectedInterface.setSelected(false);
					
					interfaceList.get(interfaceList.size()-1).setSelected(true);
					
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		panel.add(newButton,c);
		
		JPanel rightPanel = new JPanel();
		
		JButton saveButton = new JButton("Salvar");
		
		saveButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
		
				selectedInterface.saveObejct();
				
			}
		});
		
		c.gridx++;
		rightPanel.add(saveButton, c);
		
		JButton upButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				GUI.class.getResource("/images/up_arrow.png"))));
		
		upButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
				int position = objects.indexOf(selectedInterface.object);
				
				if (position > 0) {
				
					Collections.swap(objects, position, position-1);
				
					Collections.swap(interfaceList, position, position-1);
				
					removeInterfaceFromScroll(interfaceList.get(position));
					removeInterfaceFromScroll(interfaceList.get(position-1));
				
					addInterfaceToScroll(selectedInterface, position-1);
					addInterfaceToScroll(interfaceList.get(position), position);
			
					revalidate();
					
				}
				
			}
		});
		
		rightPanel.add(upButton, c);
		
		JButton downButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				GUI.class.getResource("/images/down_arrow.png"))));
		
		downButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				int position = objects.indexOf(selectedInterface.object);

				if (position < interfaceList.size()-1) {
				
					Collections.swap(objects, position, position+1);
				
					Collections.swap(interfaceList, position, position+1);
					
					removeInterfaceFromScroll(interfaceList.get(position));
					removeInterfaceFromScroll(interfaceList.get(position+1));
				
					addInterfaceToScroll(selectedInterface, position+1);
					addInterfaceToScroll(interfaceList.get(position), position);
					
					scroll.getVerticalScrollBar().setValue(32*38);
					
					revalidate();
					
				}
				
			}
		});
		
		c.gridx++;
		rightPanel.add(downButton, c);
		
		c.gridx = 1;
		panel.add(rightPanel,c);
		
		return panel;
		
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
		 * <br> Nome : JTextField
		 * <br> Boolean : JCheckBox
		 * <br> Escolha : ButtonGroup
		 * <br> Number : JTextField
		 * <br> UnidadeList : HashMap<\Unidade, TroopFormattedTextField>
		 */
		private Map<Property, Object> variableMap = new HashMap<Property, Object>();
		
		public ObjectInterface(Object object, List<Property> list) {
			
			this.object = object;
			
			createNamePanel(object.toString());
			
			createInformationPanel(list);
			
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
					
					selectedInterface.setSelected(false);
					
					setSelected(true);
				
				}
			});
			
		}
		
		private void createInformationPanel(List<Property> list) {
			
			objectInformation = new JPanel(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0, 10, 5, 10);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridy = 0;
			
			for (Property i : list) {
				
				if (i.getClass().equals(Property_Nome.class)) {
				
				objectInformation.add(makeNamePanel((Property_Nome) i), c);
				c.gridy++;	
					
				} else if (i.getClass().equals(Property_Boolean.class)) {
					
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
			
			objectInformation.setBackground(Cores.ALTERNAR_ESCURO);
			objectInformation.setVisible(false);
			
		}
		
		private JPanel makeNamePanel(Property_Nome variable) {
			
			JPanel panel = makeDefaultPanel();
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridy = 0;
			c.gridx = 0;
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.WEST;
			
			panel.add(new JLabel("Nome"), c);
			
			JTextField nameField = new JTextField(16);
			nameField.setText(variable.getName());
			
			c.anchor = GridBagConstraints.EAST;
			c.gridy++;
			c.gridwidth = 2;
			panel.add(nameField, c);
			
			variableMap.put(variable, nameField);
			
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

			JTextField txt = new JTextField(variable.getValue().toString());
			
			txt.setDocument(new PlainDocument() {

				@Override
				public void insertString(int offset, String str, AttributeSet attr)
						throws BadLocationException {
					if (str == null)
						return;

					// Only does anything if it is a comma, period or number
					if (str.charAt(0) == '.' || str.charAt(0) == ',' ||
							 Character.isDigit(str.charAt(0))) {
						
						// If it is a comma or perido, check if it already has one
						if (!Character.isDigit(str.charAt(0)) && 
								super.getText(0, getLength()).contains(".")) {}
						else
							// If it does not and the inserted is a comma, add a period
							if (str.charAt(0) == ',')
								super.insertString(offset, ".", attr);
							else
								// Else, add the inserted character (period or number)
								super.insertString(offset, str, attr);
					}


				}

			});
			
			txt.addFocusListener(new FocusListener() {
			
				public void focusLost(FocusEvent f) {
					
					if (((JTextField) f.getSource()).getText().equals("")) {
						((JTextField) f.getSource()).setText("1");
					}
					
				}
				
				public void focusGained(FocusEvent arg0) {}
			});
			
			txt.setText(variable.getValue().toString());
			
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
				
				selectedInterface = this;
				
			} else {
				objectInformation.setVisible(false);
				objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				objectName.setBackground(Cores.FUNDO_CLARO);
			}
		}
		
		private void saveObejct() {
			
			for (Entry<Property, Object> i : variableMap.entrySet()) {
				
				// Nome case
				if (i.getKey().getClass().equals(Property_Nome.class)) {
			
					// only does this if the name is different
					if (!i.getKey().getName().equals(((JTextField) i.getValue()).getText())) {
						
						i.getKey().setValue(((JTextField) i.getValue()).getText());	
						
						objectName.removeAll();
						objectName.add(new JLabel(object.toString()));
						
						objectName.revalidate();
						objectName.repaint();
					
					}
					
				// Boolean case
				} else if (i.getKey().getClass().equals(Property_Boolean.class)) {
					
				i.getKey().setValue(((JCheckBox) i.getValue()).isSelected());
					
				// Escolha case
				} else if (i.getKey().getClass().equals(Property_Escolha.class)) {
					
					 Enumeration<AbstractButton> enumeration = 
							 ((ButtonGroup) i.getValue()).getElements();
					
					 while (enumeration.hasMoreElements()) {
					
						 AbstractButton b = enumeration.nextElement();
					
						 if (b.isSelected())
							 i.getKey().setValue(b.getText());
					 
					 }
					
				// Number case
				} else if (i.getKey().getClass().equals(Property_Number.class)) {
					
					i.getKey().setValue(((JTextField) i.getValue()).getText());
				
				// UnidadeList case 
				} else if (i.getKey().getClass().equals(Property_UnidadeList.class)) {
					
					Map<Unidade, BigDecimal> map = new HashMap<Unidade, BigDecimal>();
					
					for (Entry<Unidade, TroopFormattedTextField> x 
							: ((HashMap<Unidade, TroopFormattedTextField>) i .getValue()).entrySet()) {
						
						map.put(x.getKey(), x.getValue().getValue());
						
					}
					
					i.getKey().setValue(map);
					
				}
				
			}
			
			for (Object o : objects) {
				
				if (o.toString() == object.toString())
					JOptionPane.showMessageDialog(null, "Esse nome já está sendo utilizado.\nFavor escolher outro.");
				
			}
			if (!objects.contains(object))
				objects.add(object);
			
					
		}
		
		public String toString(){
			return object.toString();
		}
		
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
