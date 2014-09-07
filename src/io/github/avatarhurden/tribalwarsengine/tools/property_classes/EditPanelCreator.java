package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import io.github.avatarhurden.tribalwarsengine.enums.ResearchSystem;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock.BuildingsEditPanel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.Cores;

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
				else if (BuildingBlock.isBuildingJson(json.getJSONObject(key)))
					panels.add(new BuildingsPanel(key));
				
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
		private ArmyEditPanel panel;
		
		private ArmyPanel(String key) {
			super(key);
			
			army = gson.fromJson(json.get(key).toString(), Army.class);
			panel = army.getEditPanelFullNoHeader(onChange, 30);
			
			add(panel);
			
		}
		
		protected void setValue() {
			
			panel.saveValues();
			json.put(key, new JSONObject(gson.toJson(army)));
			
		}
		
	}
	
	private class BuildingsPanel extends DefaultPanel {
		private BuildingBlock builds;
		private BuildingsEditPanel panel;
		
		private BuildingsPanel(String key) {
			super(key);
			
			builds = gson.fromJson(json.get(key).toString(), BuildingBlock.class);
			panel = builds.new BuildingsEditPanel(onChange, false, true, true);
			
			add(panel);
			
		}
		
		protected void setValue() {
			
			panel.saveValues();
			json.put(key, new JSONObject(gson.toJson(builds)));
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
	        layout.columnWidths = new int[]{};
	        layout.rowHeights = new int[]{20};
	        layout.columnWeights = new double[]{1, 1};
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
