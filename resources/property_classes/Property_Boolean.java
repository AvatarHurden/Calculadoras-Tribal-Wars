package property_classes;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Property_Boolean implements Property{

	private String name;
	
	private boolean isTrue;
	
	private JCheckBox checkBox;
	
	public Property_Boolean(String name, boolean isTrue) {
		
		this.name = name;
		this.isTrue = isTrue;
		
	}
	
	public String getName() {
	
		return name;
		
	}
	
	public String getValueName() {
		
		if (isTrue)
			return "Ativado";
		else
			return "Desativado";
		
	}
	
	public boolean getValue() {
		return isTrue;
	}

	/**
	 * Takes a boolean as parameter
	 */
	public void setValue(Object i) {
		
		isTrue = (boolean)i;
	}

	public JPanel makeEditDialogPanel(JPanel defaultPanel, final OnChange onChange) {
		
		JPanel panel = defaultPanel;

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;

		JLabel namelbl = new JLabel(name);
		panel.add(namelbl, c);

		checkBox = new JCheckBox();
		checkBox.setOpaque(false);
		checkBox.setSelected(isTrue);
		
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onChange.run();
			}
		});
		
		c.gridx++;
		panel.add(checkBox, c);

		return panel;

		
	}

	public void setValue() {
		
		isTrue = checkBox.isSelected();
		
	}

}
