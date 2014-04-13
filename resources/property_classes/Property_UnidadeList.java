package property_classes;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import custom_components.TroopFormattedTextField;
import database.Unidade;

@SuppressWarnings({ "serial" })
public class Property_UnidadeList 
	extends HashMap<Unidade, BigDecimal> implements Property {
	
	private Map<Unidade, TroopFormattedTextField> textFieldMap;
	
	public String getName() {
		
		return null;
		
	}

	public String toString() {
		
		String s = "{";
		
		for (Unidade i : Unidade.values()) {
			s+=i.nome()+"="+get(i).toString()+",";
		}
		
		s+="}";
		
		return s;
		
	}
	
	/**
	 * This method does not apply to UnidadeList
	 */
	public String getValueName(){
		return null;
	}
	
	/**
	 * Takes a hashMap<Unidade, BigDecimal> as parameter
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object i) {
		
		clear();
		
		putAll((HashMap<Unidade, BigDecimal>) i);
		
	}

	public JPanel makeEditDialogPanel(JPanel defaultPanel, final OnChange onChange) {
		
		JPanel panel = defaultPanel;

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = -1;
		c.gridx = 0;
		c.gridwidth = 1;

		textFieldMap = new HashMap<Unidade, TroopFormattedTextField>();
		
		for (Unidade i : Unidade.values()) {

			c.gridx = 0;
			c.gridy++;
			panel.add(new JLabel(i.nome()), c);

			TroopFormattedTextField txt = new TroopFormattedTextField(9) {
				public void go() {}
			};
			
			txt.setText(get(i).toString());

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
		
		return panel;

		
	}

	@Override
	public void setValue() {
		
		for (Entry<Unidade, TroopFormattedTextField> x 	: textFieldMap.entrySet()) {
			
			// Puts every unit with corresponding value
			put(x.getKey(), x.getValue().getValue());
			
		}
		
	}

}
