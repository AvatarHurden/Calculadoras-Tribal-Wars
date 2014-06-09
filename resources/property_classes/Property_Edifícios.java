package property_classes;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import custom_components.EdifícioFormattedTextField;
import custom_components.IntegerFormattedTextField;
import database.Edifício;
import database.Unidade;

public class Property_Edifícios extends HashMap<Edifício, Integer> implements Property {
	
	private Map<Edifício, EdifícioFormattedTextField> textFieldMap;
	
	public JPanel makeEditDialogPanel(JPanel panel, final OnChange change) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = -1;
		c.gridx = 0;
		c.gridwidth = 1;

		textFieldMap = new HashMap<Edifício, EdifícioFormattedTextField>();
		
		for (Edifício i : Edifício.values()) {

			c.gridx = 0;
			c.gridy++;
			panel.add(new JLabel(i.nome()), c);

			EdifícioFormattedTextField txt = new EdifícioFormattedTextField(i, get(i)) {
				public void go() {}
			};
			
			txt.setText(get(i).toString());

			txt.getDocument().addDocumentListener(new DocumentListener() {
				
				public void removeUpdate(DocumentEvent arg0) {
					change.run();
				}
				
				public void insertUpdate(DocumentEvent arg0) {
					change.run();
				}
				
				public void changedUpdate(DocumentEvent arg0) {}
			});

			c.gridx = 1;
			panel.add(txt, c);
			
			textFieldMap.put(i, txt);

		}
		
		return panel;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue() {
		// TODO Auto-generated method stub
		
	}

}
