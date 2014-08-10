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
import database.Edifício;

@SuppressWarnings("serial")
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
			
			if (!i.equals(Edifício.NULL)) {

				c.gridx = 0;
				c.gridy++;
				if (i.equals(Edifício.ACADEMIA_1NÍVEL))
					panel.add(new JLabel(i.nome() + " (1 nível)"), c);
				else if (i.equals(Edifício.ACADEMIA_3NÍVEIS))
					panel.add(new JLabel(i.nome() + " (3 níveis)"), c);
				else
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

		}
		
		return panel;
		
	}

	public String getName() {
		return null;
	}

	public String getValueName() {
		return null;
	}

	@Override
	public void setValue() {
		
		for (Map.Entry<Edifício, EdifícioFormattedTextField> x : textFieldMap.entrySet()) {
			
			// Puts every unit with corresponding value
			put(x.getKey(), x.getValue().getValue().intValue());
			
		}
		
	}

}
