package property_classes;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Property_Nome implements Property{

	private String name;
	
	private JTextField txtName;
	
	public Property_Nome(String s) {
		name = s;
	}
	
	public String getName() {
		return "Nome";
	}

	public String getValueName() {
		return name;
	}
	
	/**
	 * Takes a string as parameter
	 */
	public void setValue(Object i) {
		
		name = (String) i;
		
	}

	public JPanel makeEditDialogPanel(JPanel defaultPanel, ActionListener listener) {
		
		JPanel panel = defaultPanel;
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		
		panel.add(new JLabel("Nome"), c);
		
		txtName = new JTextField(16);
		txtName.setText(name);
		
		txtName.getDocument().addDocumentListener(new DocumentListener() {
		
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

	public void setValue() {
		
		// only does this if the name is different
		if (!name.equals(txtName.getText()))
			name = txtName.getText();	
		
	}

	
	
}
