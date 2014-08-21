package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import io.github.avatarhurden.tribalwarsengine.components.EdifícioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TroopLevelComboBox;
import io.github.avatarhurden.tribalwarsengine.enums.ResearchSystem;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.Army;
import io.github.avatarhurden.tribalwarsengine.objects.Buildings;
import io.github.avatarhurden.tribalwarsengine.objects.Scope;
import io.github.avatarhurden.tribalwarsengine.objects.Scope.ScopeSelectionPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.AbstractButton;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.Cores;
import database.Edifício;
import database.Unidade;

@SuppressWarnings("serial")
public class EditPanelCreator extends JPanel {
	
	private JSONObject json;
	private LinkedHashMap<String, String> objectNames;
	private OnChange onChange;
	
	private Gson gson;
	
	private ArrayList<DefaultPanel> panels;
	
	// The name of an editable object must be unique
	private JTextField nameTextField;
	
	public EditPanelCreator(JSONObject json, LinkedHashMap<String, String> names,
			OnChange onChange) {
		this.json = json;
		objectNames = names;
		this.onChange = onChange;
		
		gson = new GsonBuilder().setPrettyPrinting().create();
		panels = new ArrayList<DefaultPanel>();
		
		setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 5, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        
		for (String key : names.keySet()) {
			
			Object value = json.get(key);
			
			if (value instanceof Double)
				panels.add(new DoublePanel(key));
			else if (value instanceof String)
				panels.add(new StringPanel(key));
			else if (value instanceof Boolean)
				panels.add(new BooleanPanel(key));
			 // ResearchSystem is saved as Integer
			else if (value instanceof Integer)
				panels.add(new ResearchPanel(key));
			else {
				
				// É um dos objetos compostos
				if (Army.isArmyJson(json.getJSONObject(key)))
					panels.add(new ArmyPanel(key));
				else if (Buildings.isBuildingJson(json.getJSONObject(key)))
					panels.add(new BuildingsPanel(key));
				else if (Scope.isScopeJson(json.getJSONObject(key)))
					panels.add(new ScopePanel(key));
				
			}
		}
		
		for (JPanel panel : panels) {
			add(panel, c);
			c.gridy++;
		}
			
	}
	
	public void setValues() {
		for (DefaultPanel panel : panels)
			panel.setValue();
	}
	
	public JTextField getNameTextField() {
		return nameTextField;
	}
	
	private class DoublePanel extends DefaultPanel {
		
		private JTextField textNumber;
		
		private DoublePanel(String key) {
			super(key);
			
			JLabel namelbl = new JLabel(objectNames.get(key));
			add(namelbl, c);

			textNumber = new JTextField();
			textNumber.setDocument(new PlainDocument() {

				@Override
				public void insertString(int offset, String str, AttributeSet attr)
						throws BadLocationException {
					if (str == null)
						return;

					// Only does anything if it is a comma, period or number
					if (str.charAt(0) == '.' || str.charAt(0) == ',' ||
							 Character.isDigit(str.charAt(0))) {
						
						// If it is a comma or period, check if it already has one
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
			
			textNumber.addFocusListener(new FocusAdapter() {
			
				public void focusLost(FocusEvent f) {
					if (((JTextField) f.getSource()).getText().equals("")) 
						((JTextField) f.getSource()).setText("1");
				
				}
			});
			
			textNumber.setText(String.valueOf(json.getDouble(key)));
			
			textNumber.setColumns(5);
			
			textNumber.getDocument().addDocumentListener(new DocumentListener() {
				
				public void removeUpdate(DocumentEvent arg0) {
					onChange.run();
				}
				
				public void insertUpdate(DocumentEvent arg0) {
					onChange.run();
				}
				
				public void changedUpdate(DocumentEvent arg0) {}
			});
			
			c.gridx++;
			add(textNumber, c);

		}
		
		protected void setValue() {
			json.put(key, Double.valueOf(textNumber.getText()));
		}
		
	}
	
	private class StringPanel extends DefaultPanel {
		
		private JTextField textField;
		
		private StringPanel(String key) {
			super(key);
			
			c.anchor = GridBagConstraints.WEST;
			
			add(new JLabel(objectNames.get(key)), c);
			
			textField = new JTextField(16);
			
			textField.setDocument(new PlainDocument() {
				
				  @Override
			        public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
			            if (str == null) return;

			            if ((getLength() + str.length()) <= 25) 
			                super.insertString(offset, str, attr);
			            
			        } 
				
			});
			
			textField.setText(json.getString(key));
			
			textField.getDocument().addDocumentListener(new DocumentListener() {
			
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
			add(textField, c);
			
			if (key.equals("name"))
				nameTextField = textField;
			
		}
		
		protected void setValue() {
			json.put(key, textField.getText());
		}
		
	}
	
	private class BooleanPanel extends DefaultPanel {
		
		private JCheckBox checkBox;
		
		private BooleanPanel(String key) {
			super(key);
			
			JLabel namelbl = new JLabel(objectNames.get(key));
			add(namelbl, c);

			checkBox = new JCheckBox();
			checkBox.setOpaque(false);
			checkBox.setSelected(json.getBoolean(key));
			
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					onChange.run();
				}
			});
			
			c.gridx++;
			add(checkBox, c);
			
		}
		
		protected void setValue() {
			json.put(key, checkBox.isSelected());
		}
		
	}
	
	private class ResearchPanel extends DefaultPanel {
		
		private ResearchSystem system;
		private ButtonGroup group;
		
		private ResearchPanel(String key) {
			super(key);
			
			system = ResearchSystem.ConvertInteger(json.getInt(key));
			
			JLabel nameLabel = new JLabel(objectNames.get(key));
			add(nameLabel, c);

			group = new ButtonGroup();
			
			JPanel buttonPanel = new JPanel(new GridLayout(0,1));
			buttonPanel.setOpaque(false);
			
			for (ResearchSystem s : ResearchSystem.values()) {
				
				JRadioButton button = new JRadioButton(s.getName());
				button.setOpaque(false);
				
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						onChange.run();
					}
				});
				
				if (s.equals(system))
					button.setSelected(true);

				group.add(button);
				buttonPanel.add(button);

			}

			c.gridx++;
			add(buttonPanel, c);

		}
		
		protected void setValue() {
			
			Enumeration<AbstractButton> enumeration = group.getElements();
			while (enumeration.hasMoreElements()) {
			
				AbstractButton b = enumeration.nextElement();
				if (b.isSelected())
					for (ResearchSystem ss : ResearchSystem.values())
						if (b.getText().equals(ss.getName()))
							system = ss;
			}
			
			json.put(key, system.getResearch());
			
		}
		
	}
	
	private class ArmyPanel extends DefaultPanel {
		
		private Army army;
		private HashMap<Unidade, IntegerFormattedTextField> quantidades;
		private HashMap<Unidade, TroopLevelComboBox> levels;
		
		private ArmyPanel(String key) {
			super(key);
			
			quantidades = new HashMap<Unidade, IntegerFormattedTextField>();
			levels = new HashMap<Unidade, TroopLevelComboBox>();
			army = gson.fromJson(json.get(key).toString(), Army.class);
			
			c.gridy = -1;
			
			for (Unidade i : Unidade.values()) {
				
				c.gridx = 0;
				c.gridy++;
				add(new JLabel(i.toString()), c);

				IntegerFormattedTextField txt = new IntegerFormattedTextField(9, Integer.MAX_VALUE) {
					public void go() {}
				};
				
				if (army.contains(i))
					txt.setText(String.valueOf(army.getQuantidade(i)));
				
				// This is made because the editing of the textField fires the listener
				txt.getDocument().addDocumentListener(new DocumentListener() {
					
					public void removeUpdate(DocumentEvent arg0) {
						onChange.run();
					}
					
					public void insertUpdate(DocumentEvent arg0) {
						onChange.run();
					}
					
					public void changedUpdate(DocumentEvent arg0) {}
				});
				
				quantidades.put(i, txt);

				c.gridx++;
				add(txt, c);
				
				TroopLevelComboBox combo = new TroopLevelComboBox(
						WorldManager.get().getSelectedWorld().getResearchSystem().getResearch());
				levels.put(i, combo);
					
				combo.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (combo.getItemCount() > 1) {
					c.gridx++;
					add(combo, c);
				}
			}
		}
		
		protected void setValue() {
			
			for (Unidade i : Unidade.values())
				army.addTropa(i, (int) levels.get(i).getSelectedItem(), 
						quantidades.get(i).getValue().intValue());
			
			json.put(key, new JSONObject(gson.toJson(army)));
		}
		
	}
	
	private class BuildingsPanel extends DefaultPanel {
		
		private Buildings builds;
		private HashMap<Edifício, EdifícioFormattedTextField> texts;
		
		private BuildingsPanel(String key) {
			super(key);
			
			texts = new HashMap<Edifício, EdifícioFormattedTextField>();
			builds = gson.fromJson(json.get(key).toString(), Buildings.class);
			
			c.gridy = -1;
			
			for (Edifício i : Edifício.values()) {
				
				c.gridx = 0;
				c.gridy++;
				if (i.equals(Edifício.ACADEMIA_1NÍVEL))
					add(new JLabel(i.toString() + " (1 nível)"), c);
				else if (i.equals(Edifício.ACADEMIA_3NÍVEIS))
					add(new JLabel(i.toString() + " (3 níveis)"), c);
				else
					add(new JLabel(i.toString()), c);
				
				EdifícioFormattedTextField txt = new EdifícioFormattedTextField(i, builds.getLevel(i)) {
					public void go() {
						onChange.run();
					}
				};
				
				c.gridx = 1;
				add(txt, c);
				
				texts.put(i, txt);
			
			}
		}
		
		protected void setValue() {
			
			for (Edifício i : Edifício.values()) 
				builds.addBuilding(i, texts.get(i).getValueInt());
			
			json.put(key, new JSONObject(gson.toJson(builds)));
		}
		
	}
	
	private class ScopePanel extends DefaultPanel{
		
		private Scope escopo;
		private JPanel[] panels;
		
		private ScopePanel(String key) {
			super(key);
			
			escopo = gson.fromJson(json.get(key).toString(), Scope.class);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			
			panels = new JPanel[2];
			
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
			((GridBagLayout) getLayout()).columnWidths = new int[] {width/2+5, width/2+5};
			
			// Add "escopo" label
			JLabel label = new JLabel("Mundos em que é visível");

			c.anchor = GridBagConstraints.WEST;
			c.gridwidth = 2;
			c.insets = new Insets(5, 5, 5, 0);
			add(label, c);
			
			c.insets = new Insets(0, 0, 0, 0);
			c.gridy++;
			c.anchor = GridBagConstraints.CENTER;
			c.gridwidth = 1;
			add(panels[0], c);
			
			c.gridx++;
			add(panels[1], c);
			
			// Add selectionList
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy++;
			add(selection, c);
			
			// Initial formatting
			if (escopo.isGlobal()) {
				selection.setVisible(false);
				panels[0].setBackground(Cores.FUNDO_ESCURO);
				panels[0].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			} else {
				panels[1].setBackground(Cores.FUNDO_ESCURO);
				panels[1].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			}
			
		}
		
		protected void setValue() {
			json.put(key, new JSONObject(gson.toJson(escopo)));
		}
		
	}
	
	private abstract class DefaultPanel extends JPanel {
		
		GridBagConstraints c;
		String key;
		
		private DefaultPanel(String key) {
			
			this.key = key;
			
	        setBackground(Cores.FUNDO_CLARO);

	        setBorder(new LineBorder(Cores.SEPARAR_CLARO));

	        GridBagLayout layout = new GridBagLayout();
	        layout.columnWidths = new int[]{,};
	        layout.rowHeights = new int[]{20};
	        layout.columnWeights = new double[]{1, Double.MIN_VALUE};
	        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
	        setLayout(layout);
	        
	        c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
	        c.gridx = 0;
	        c.gridy = 0;
	        c.gridwidth = 1;

		}
		
		protected abstract void setValue();
		
	}
	
}
