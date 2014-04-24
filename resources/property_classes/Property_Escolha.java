package property_classes;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Property_Escolha implements Property {
	
	private String name;
	
	int selectedOption;
	
	private String[] options;
	
	// "Object" to be returned on getEditDialogObject
	private ButtonGroup buttons;
	
	public Property_Escolha (String name, String selected, String ... options) {
		
		this.name = name;
		
		this.options = options;
		
		for (int i = 0; i < options.length; i++)
			if (options[i].equals(selected.replaceAll("_", " ")))
				selectedOption = i; 
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getValueName() {
		return options[selectedOption];
	}
	
	/**
	 * @return The selected object with no whitespace, to be written to the file
	 */
	public String getSelected() {
		return options[selectedOption].replaceAll(" ", "_");
	}
	
	public boolean isOption(String s) {
		
		if (options[selectedOption].equals(s))
			return true;
		else
			return false;
		
	}
	
	public String[] getOptions() {
		return options;
	}
	
	/**
	 * Takes a string as parameter
	 */
	public void setValue(Object i) {
		
		for (int x = 0; x < options.length;x++)
			if ((String)i == options[x])
				selectedOption = x;
		
	}
	
	// i.e Make the class itself define the value, since it will contain the object that has
	// the information
	public void setValue() {
		
		Enumeration<AbstractButton> enumeration = buttons.getElements();
		
		while (enumeration.hasMoreElements()) {
		
			AbstractButton b = enumeration.nextElement();
		
			if (b.isSelected())
				for (int x = 0; x < options.length;x++)
					if (b.getText() == options[x])
						selectedOption = x;
		}
		
		
	}
	
	public JPanel makeEditDialogPanel(JPanel defaultPanel, final OnChange onChange) {
		
		JPanel panel = defaultPanel;
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;

		JLabel nameLabel = new JLabel(name);
		panel.add(nameLabel, c);

		buttons = new ButtonGroup();
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
		for (String s : options) {
			
			JRadioButton button = new JRadioButton(s);
			button.setOpaque(false);
			
			button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					onChange.run();
				}
			});
			
			if (isOption(s))
				button.setSelected(true);

			buttons.add(button);
			buttonPanel.add(button);

		}

		c.gridx++;
		panel.add(buttonPanel, c);

		return panel;

		
	}
	
}
