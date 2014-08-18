package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import io.github.avatarhurden.tribalwarsengine.components.EdifícioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.enums.ResearchSystem;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.Scope.ScopeSelectionPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.JSONObject;

import database.Cores;
import database.Edifício;
import database.Unidade;

public class PropertyPanelCreator extends JPanel{
	
	JSONObject object;
	HashMap<String, Object> componentMap;
	LinkedHashMap<String, String> namesMap;
	OnChange onChange;
	
	public PropertyPanelCreator(JSONObject object, 
			LinkedHashMap<String, String> namesMap, OnChange onChange) {
		this.object = object;
		this.namesMap = namesMap;
		
		setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 5, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;

		
		for (String key : namesMap.keySet()) {
			
			if (!object.has(key))
				continue;
			
			JPanel panel = null;
			
			Object o = object.get(key);
			if (o instanceof Boolean)
				panel = makeBooleanPanel(key, (Boolean) o);
			else if (o instanceof Double)
				panel = makeNumberPanel(key, (Double) o);
			else if (o instanceof String)
				panel = makeStringPanel(key, (String) o);
			else if (o instanceof ResearchSystem)
				panel = makeResearchPanel(key, (ResearchSystem) o);
			else if (o instanceof Scope)
				panel = makeEscopoPanel(key, (Scope) o);
			else if (o instanceof EdifíciosMap) 
				panel = makeEdifíciosPanel(key, (EdifíciosMap) o);
			else if (o instanceof TropasMap)
				panel = makeTropasPanel(key, (TropasMap) o);
			
			add(panel, c);
			c.gridy++;
		}
			
	}
	
	private JPanel makeBooleanPanel(String key, boolean isTrue) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;

		JLabel namelbl = new JLabel(namesMap.get(key));
		panel.add(namelbl, c);

		JCheckBox checkBox = new JCheckBox();
		checkBox.setOpaque(false);
		checkBox.setSelected(isTrue);
		
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onChange.run();
			}
		});
		
		c.gridx++;
		panel.add(checkBox, c);
		
		componentMap.put(key, checkBox);

		return panel;
		
	}
	
	private JPanel makeNumberPanel(String key, double number) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;

		JLabel namelbl = new JLabel(namesMap.get(key));
		panel.add(namelbl, c);

		JTextField txtNumber = new JTextField();
		
		txtNumber.setDocument(new PlainDocument() {

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
		
		txtNumber.addFocusListener(new FocusListener() {
		
			public void focusLost(FocusEvent f) {
				
				if (((JTextField) f.getSource()).getText().equals("")) {
					((JTextField) f.getSource()).setText("1");
				}
				
			}
			
			public void focusGained(FocusEvent arg0) {}
		});
		
		txtNumber.setText(String.valueOf(number));
		
		txtNumber.setColumns(5);
		
		txtNumber.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent arg0) {
				onChange.run();
			}
			
			public void insertUpdate(DocumentEvent arg0) {
				onChange.run();
			}
			
			public void changedUpdate(DocumentEvent arg0) {}
		});
		
		c.gridx++;
		panel.add(txtNumber, c);

		componentMap.put(key, txtNumber);
		
		return panel;
	}
	
	private JPanel makeStringPanel(String key, String string) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		
		panel.add(new JLabel(namesMap.get(key)), c);
		
		JTextField txtName = new JTextField(16);
		
		txtName.setDocument(new PlainDocument() {
			
			  @Override
		        public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
		            if (str == null) return;

		            if ((getLength() + str.length()) <= 25) 
		                super.insertString(offset, str, attr);
		            
		        } 
			
		});
		
		txtName.setText(string);
		
		txtName.getDocument().addDocumentListener(new DocumentListener() {
		
			public void removeUpdate(DocumentEvent arg0) {
				onChange.run();
			}				
			
			public void insertUpdate(DocumentEvent arg0) {
				onChange.run();
			}
			
			public void changedUpdate(DocumentEvent arg0) {}
		});
		
		c.anchor = GridBagConstraints.EAST;
		c.gridy++;
		c.gridwidth = 2;
		panel.add(txtName, c);
		
		componentMap.put(key, txtName);
		
		return panel;
	}

	private JPanel makeResearchPanel(String key, ResearchSystem search) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;

		JLabel nameLabel = new JLabel(namesMap.get(key));
		panel.add(nameLabel, c);

		ButtonGroup buttons = new ButtonGroup();
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
		for (ResearchSystem s : ResearchSystem.values()) {
			
			JRadioButton button = new JRadioButton(s.toString());
			button.setOpaque(false);
			
			button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					onChange.run();
				}
			});
			
			if (s.equals(search))
				button.setSelected(true);

			buttons.add(button);
			buttonPanel.add(button);

		}

		c.gridx++;
		panel.add(buttonPanel, c);

		componentMap.put(key, buttons);
		
		return panel;
	}
	
	private JPanel makeTropasPanel(String key, TropasMap map) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = -1;
		c.gridx = 0;
		c.gridwidth = 1;

		HashMap<Unidade, IntegerFormattedTextField> textFieldMap = 
				new HashMap<Unidade, IntegerFormattedTextField>();
		
		for (Unidade i : Unidade.values()) {

			c.gridx = 0;
			c.gridy++;
			panel.add(new JLabel(i.nome()), c);

			IntegerFormattedTextField txt = new IntegerFormattedTextField(9, Integer.MAX_VALUE) {
				public void go() {}
			};
			
			if (map.containsKey(i))
				txt.setText(map.get(i).toString());

			txt.getDocument().addDocumentListener(new DocumentListener() {
				
				public void removeUpdate(DocumentEvent arg0) {
					onChange.run();
				}
				
				public void insertUpdate(DocumentEvent arg0) {
					onChange.run();
				}
				
				public void changedUpdate(DocumentEvent arg0) {}
			});

			c.gridx = 1;
			panel.add(txt, c);
			
			textFieldMap.put(i, txt);

		}
		
		componentMap.put(key, textFieldMap);
		
		return panel;
	}
	
	private JPanel makeEdifíciosPanel(String key, EdifíciosMap map) {
		
		JPanel panel = makeDefaultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = -1;
		c.gridx = 0;
		c.gridwidth = 1;

		HashMap<Edifício, EdifícioFormattedTextField> textFieldMap 
		= new HashMap<Edifício, EdifícioFormattedTextField>();
		
		for (Edifício i : Edifício.values()) {
			
			if (!i.equals(Edifício.NULL)) {

				c.gridx = 0;
				c.gridy++;
				if (i.equals(Edifício.ACADEMIA_1NÍVEL))
					panel.add(new JLabel(i.nome() + " (1 nível)"), c);
				else if (i.equals(Edifício.ACADEMIA_3NÍVEIS))
					panel.add(new JLabel(i.nome() + " (3 níveis)"), c);
				else
					panel.add(new JLabel(i.nome()), c);
	
				EdifícioFormattedTextField txt = new EdifícioFormattedTextField(i, map.get(i)) {
					public void go() {}
				};
				
				txt.getDocument().addDocumentListener(new DocumentListener() {
					
					public void removeUpdate(DocumentEvent arg0) {
						onChange.run();
					}
					
					public void insertUpdate(DocumentEvent arg0) {
						onChange.run();
					}
					
					public void changedUpdate(DocumentEvent arg0) {}
				});
	
				c.gridx = 1;
				panel.add(txt, c);
				
				textFieldMap.put(i, txt);
			}

		}
		
		componentMap.put(key, textFieldMap);
		
		return panel;
	}
	
	private JPanel makeEscopoPanel(String key, final Scope escopo) {
		
		JPanel panel = makeDefaultPanel();
		
		final JPanel[] panels = new JPanel[2];
		
		final ScopeSelectionPanel selection = 
				escopo.new ScopeSelectionPanel(onChange);
		
		panels[0] = new JPanel();
		panels[0].add(new JLabel("Todos"));
		
		panels[1] = new JPanel();
		panels[1].add(new JLabel("Escolher Mundos"));
		
		for (JPanel l : panels) {
			l.setBackground(Cores.FUNDO_CLARO);
			l.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
			l.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					for (JPanel p : panels) {
						p.setBackground(Cores.FUNDO_CLARO);
						p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
					}
						
					((JPanel) e.getSource()).setBackground(Cores.FUNDO_ESCURO);
					((JPanel) e.getSource()).setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
					
					if (((JPanel) e.getSource()).equals(panels[0])) {
						escopo.setGlobal(true);
						selection.setVisible(false);
					} else {
						escopo.setGlobal(false);
						selection.setVisible(true);
					}
					onChange.run();
				}
			});
		}
		
		int width = selection.getPreferredSize().width;
		((GridBagLayout)panel.getLayout()).columnWidths = new int[] {width/2+5, width/2+5};
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		
		// Add "escopo" label
		JLabel label = new JLabel("Mundos em que é visível");

		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		c.insets = new Insets(5, 5, 5, 0);
		panel.add(label, c);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		panel.add(panels[0], c);
		
		c.gridx++;
		panel.add(panels[1], c);
		
		// Add selectionList
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		panel.add(selection, c);
		
		// Initial formatting
		if (escopo.isEmpty()) {
			selection.setVisible(false);
			panels[0].setBackground(Cores.FUNDO_ESCURO);
			panels[0].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		} else {
			panels[1].setBackground(Cores.FUNDO_ESCURO);
			panels[1].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		}
		
		componentMap.put(key, escopo);
			
		return panel;
	}
	
	private JPanel makeDefaultPanel() {
		
		JPanel panel = new JPanel();

        panel.setBackground(Cores.FUNDO_CLARO);

        panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{,};
        layout.rowHeights = new int[]{20};
        layout.columnWeights = new double[]{1, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        panel.setLayout(layout);

        return panel;

	}
	
	
	private Object getValue(Object component) {
		
		if (component instanceof JCheckBox)
			return ((JCheckBox) component).isSelected();
		else if (component instanceof JTextField)
			return ((JTextField) component).getText();
		else if (component instanceof Scope)
			return component;
		
		return null;
	}
}
