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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import database.Cores;
import database.Unidade;

/**
 * Creates a dialog that enables the user to edit a list of objects (that are compatible with the EditDialog)
 * <br>It remains always on top, and can allow the user to create new objects, delete objects and
 * change the order of the objects, besides editing existing ones.
 * <br>This class edits the original <code>List<//Object></code> provided, accessing the objects variables
 * through the <variable>variableName</variable> <code>Field</code>.
 * 
 * @author Arthur
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditDialog extends JDialog {

	//TODO find out why it changes size when "NEW" is pressed
	
	//TODO make a way to allow no objects
	// - On startup, store one object as a template for future ones
	// - Somehow extract which object was supposed to be in the list
	
	List<Object> objects;

	Map<Object, ArrayList<Property>> variableMap;

	Field variableField;

	// Used for setting the visibility of the information panels
	List<ObjectInterface> interfaceList = new ArrayList<ObjectInterface>();
	
	private ObjectInterface selectedInterface;
	
	JPanel namePanel;
	
	JScrollPane scroll;
	int listNumber = 0;
	
	JPanel informationPanel = new JPanel();
	
	JButton saveButton, upButton, downButton;

	//TODO reduce time to run program
	// 1 object = 991 milissegundos
	// 56 objects = 2218 milissegundos
	// teoricamente, a contrução sem objetos demora 968,69 milissegundos
	/**
	 * Creates a dialog that enables the user to edit a list of objects (that are compatible with the EditDialog)
	 * <br>It remains always on top, and can allow the user to create new objects, delete objects and
	 * change the order of the objects, besides editing existing ones.
	 * <br>This class edits the original <code>List<//Object></code> provided, accessing the object's variables
	 * through the <variable>variableName</variable> <code>Field</code>.
	 * 
	 * @param objects 
	 * 			<br><code>List<//Object></code> with the objects to be edited.
	 * @param variableName
	 * 			<br>The name of the <code>Field</code> that contains the object's properties.
	 * @param selected
	 * 			<br>The object that will be selected on start.
	 * @throws NoSuchFieldException
	 * 			<br>If the provided <code>String</code> does not correspond to a <code>Field</code> of the
	 * 				objects
	 * @throws SecurityException
	 * 			<br>If the <code>Field</code> cannot be accessed.
	 */
	public EditDialog(List objects, String variableName, int selected) 
			throws NoSuchFieldException, SecurityException {
		
		this.objects = objects;
		
		variableField = objects.get(0).getClass().getDeclaredField(variableName);
		
		variableMap = new HashMap<Object, ArrayList<Property>>();
		
		// Puts every object and their variable in a map, for reference
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
		
		// Create and add the right-side Panel
		getContentPane().add(makeScrollPanel(),c);
		
		// Creates an ObjectInterface for every object, adding it to the scrollPanel
		for (Object o : objects){
			createInterface(o);
			addInterfaceToScroll(interfaceList.get(objects.indexOf(o)), listNumber++);
		}
		
		// Makes the information panel as thick as any given objectInformation,
		// and also makes it as tall as necessary to fit an integer number of namePanels
		informationPanel.setPreferredSize(new Dimension(
				interfaceList.get(0).objectInformation.getPreferredSize().width,
				(int)Math.ceil(interfaceList.get(0).
						objectInformation.getPreferredSize().height/32+1)*32));
		
		informationPanel.setBackground(Cores.ALTERNAR_ESCURO);
		
		c.gridy++;
		getContentPane().add(informationPanel,c);

		// Makes and adds the bottom panel, with the buttons
		c.gridy++;
		c.gridwidth = 2;
		getContentPane().add(makeEditPanel(), c);
		
		// Packs the dialog and solidifies its size
		pack();
		setResizable(false);
		
		// Selects the desired object and puts the scroll in the necessary position
		interfaceList.get(selected).setSelected(true);		
		scroll.getVerticalScrollBar().setValue(selected*32);
		
		// Puts it always on top of the program
		setModal(true);
		
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

	private JScrollPane makeScrollPanel() {
		
		namePanel = new JPanel();
		namePanel.setBackground(Cores.FUNDO_CLARO);
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 140 };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 1, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		namePanel.setLayout(layout);
		
		
		scroll = new JScrollPane(namePanel);
		scroll.setPreferredSize(new Dimension(160,
				informationPanel.getPreferredSize().height));
		
		// Half the size of a namePanel per unit
		scroll.getVerticalScrollBar().setUnitIncrement(11);
		
		return scroll;		
	}
	
	private JPanel makeEditPanel() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_CLARO);
		panel.setBorder(new MatteBorder(3, 0, 0, 0, Cores.SEPARAR_ESCURO));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 160, informationPanel.getPreferredSize().width };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 0, Double.MIN_VALUE };
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
					
					// Puts object in the list
					objects.add(obj);
					
					// Creates the interface for the object
					createInterface(obj);
					
					// Adds interface to the scroll
					addInterfaceToScroll(interfaceList.get(interfaceList.size()-1),
							interfaceList.size()-1);
					
					pack();
					
					scroll.getVerticalScrollBar().setValue(
							scroll.getVerticalScrollBar().getMaximum());
					
					selectedInterface.setSelected(false);
					
					interfaceList.get(interfaceList.size()-1).setSelected(true);
					
					selectedInterface.saveObejct();
					
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		panel.add(newButton,c);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		
		saveButton = new JButton("Salvar");
		
		saveButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				selectedInterface.saveObejct();
				
			}
		});
		
		c.gridx++;
		rightPanel.add(saveButton, c);
		
		upButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
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
			
					setScrollPosition(scroll.getVerticalScrollBar(), true);
					
					revalidate();
					
					changeButtons();
					
				}
				
			}
		});
		
		rightPanel.add(upButton, c);
		
		downButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
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
					
					setScrollPosition(scroll.getVerticalScrollBar(), false);
					
//					scroll.getVerticalScrollBar().setValue(32*2);
					
					revalidate();
					
					changeButtons();
					
				}
				
			}
		});
		
		c.gridx++;
		rightPanel.add(downButton, c);
		
		JButton deleteButton = new JButton("Delete");
		
		deleteButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				String[] options = {"Sim", "Não" };
				
				int delete = JOptionPane.showOptionDialog(null, 
						new JLabel("<html>Tem certeza que deseja deletar?<br>Essa ação não " +
								"pode ser desfeita.</html>"),
						null, JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				
				if (delete == JOptionPane.YES_OPTION) {
					
					int position = interfaceList.indexOf(selectedInterface);
					
					removeInterfaceFromScroll(selectedInterface);
					
					interfaceList.remove(selectedInterface);
					objects.remove(selectedInterface.object);
					
					for (int i = position; i < interfaceList.size(); i++) {
						removeInterfaceFromScroll(interfaceList.get(i));
						addInterfaceToScroll(interfaceList.get(i), i);
					}
					
					// Se o último objeto foi deletado, seleciona o
					// anterior. Caso contrário, o seguinte
					
					
					if (position == interfaceList.size())
						interfaceList.get(position-1).setSelected(true);
					else
						interfaceList.get(position).setSelected(true);
					
					revalidate();
					repaint();
					
					changeButtons();
					
				}
				
			}
		});
		
		c.gridx++;
		rightPanel.add(deleteButton, c);
		
		c.gridx = 1;
		panel.add(rightPanel,c);
		
		return panel;
		
	}
	
	private void setScrollPosition(JScrollBar scrollBar, boolean up) {
		
		// This is the size of a single panel in the scroll
		final int SIZE = 32;
		
		// If the UP button was pressed and the interface's top is on top of the scrollbar
		if (up && (selectedInterface.objectName.getLocation().y <= scrollBar.getValue()))
			// Puts the scrollbar at the position of the interface, putting it up by a
			// full "page" of the scroll and then down by a panel
			scrollBar.setValue(interfaceList.indexOf(selectedInterface)*SIZE
					-informationPanel.getPreferredSize().height+SIZE);
		
		// If the DOWN button was pressed and the interface's bottom is under the
		// bottom of the scroll (value+height)
		if (!up && (selectedInterface.objectName.getLocation().y+SIZE
				>= scrollBar.getValue()+informationPanel.getPreferredSize().height)) 
			// Sets the scrollbar at the top of the interface
			scrollBar.setValue(interfaceList.indexOf(selectedInterface)*SIZE);
			
	}
	
	// Turns the save, up and down buttons on or off as needed
	private void changeButtons() {
		
		saveButton.setEnabled(!selectedInterface.isSaved());
		
		upButton.setEnabled(interfaceList.indexOf(selectedInterface) != 0);

		downButton.setEnabled(interfaceList.indexOf(selectedInterface) 
				!= interfaceList.size()-1);
		
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
		
		// JTextField that contains the name of the object. Needed because name is very important
		private JTextField nameTextField;
		
		// Boolean that says if the object has been saved after any changes
		private boolean isSaved = true;
		
		// The symbol to be added to the objectName when it is unsaved
		private JLabel unsavedSignal = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				GUI.class.getResource("/images/asterísco_vermelho.png"))));
		
		/**
		 * Maps the object's properties to an object that contains the property's value
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
			
			nameTextField = new JTextField(16);
			nameTextField.setText(variable.getValueName());
			
			nameTextField.getDocument().addDocumentListener(new DocumentListener() {
			
				public void removeUpdate(DocumentEvent arg0) {
					setSaved(false);
				}				
				
				public void insertUpdate(DocumentEvent arg0) {
					setSaved(false);
				}
				
				public void changedUpdate(DocumentEvent arg0) {}
			});
			
			c.anchor = GridBagConstraints.EAST;
			c.gridy++;
			c.gridwidth = 2;
			panel.add(nameTextField, c);
			
			variableMap.put(variable, nameTextField);
			
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
			
			checkBox.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					setSaved(false);
				}
			});
			
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
				
				button.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						setSaved(false);
					}
				});
				
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
			
			txt.getDocument().addDocumentListener(new DocumentListener() {
				
				public void removeUpdate(DocumentEvent arg0) {
					setSaved(false);
				}
				
				public void insertUpdate(DocumentEvent arg0) {
					setSaved(false);
				}
				
				public void changedUpdate(DocumentEvent arg0) {}
			});
			
			c.gridx++;
			panel.add(txt, c);

			variableMap.put(variable, txt);
			
			return panel;

		}

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
					public void go() {}
				};
				
				txt.setText(variable.get(i).toString());

				txt.getDocument().addDocumentListener(new DocumentListener() {
					
					public void removeUpdate(DocumentEvent arg0) {
						setSaved(false);
					}
					
					public void insertUpdate(DocumentEvent arg0) {
						setSaved(false);
					}
					
					public void changedUpdate(DocumentEvent arg0) {}
				});

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
			
			//Adds a listener to turn 'isSaved' false
			

			return panel;

		}
		
		private void setSaved(boolean saved) {
			
			isSaved = saved;
			
			if (saved)
				objectName.remove(unsavedSignal);
			else
				objectName.add(unsavedSignal,0);
			
			objectName.revalidate();
			objectName.repaint();
			
			changeButtons();
		}

		public void setSelected(boolean isSelected) {
			
			if (isSelected) {
				objectInformation.setVisible(true);
				objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				objectName.setBackground(Cores.FUNDO_ESCURO);
				
				selectedInterface = this;
				changeButtons();
				
			} else {
				objectInformation.setVisible(false);
				objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				objectName.setBackground(Cores.FUNDO_CLARO);
			}
		}
		
		private void saveObejct() {
			
			if (isUniqueName(nameTextField.getText())) {
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
						
					} // else if (UnidadeList)
					
				} // for (EntrySet)
				
				setSaved(true);
				
			} else {
				
				String s = nameTextField.getText();
				
				do {					
//				JOptionPane.showMessageDialog(null, 
//						 nameTextField, "t", 1);
					
				s = (String)(JOptionPane.showInputDialog(null, 
						new JLabel("<html>Esse nome já está sendo utilizado.<br>Favor escolher outro.</html>"),
						"Nome já utilizado", JOptionPane.ERROR_MESSAGE, null, null, s));
				
				} while (!isUniqueName(s));
				
				if (s != null) {
					nameTextField.setText(s);
					saveObejct();
				} else
					setSaved(false);
			
//				JOptionPane.showInputDialog(null);
//				JOptionPane.showMessageDialog(null, "Esse nome já está sendo utilizado.\nFavor escolher outro.");
//				nameTextField.requestFocus();
//				nameTextField.selectAll();
				
			}
			
					
		}
		
		private boolean isUniqueName(String s) {
			
			for (Object o : objects)
				if (o.toString().equals(s) && o != object)
					return false;
			
			return true;
			
		}
		
		private boolean isSaved() {
			return isSaved;
		}
		
		public String toString(){
			return object.toString();
		}
		
	}

}
