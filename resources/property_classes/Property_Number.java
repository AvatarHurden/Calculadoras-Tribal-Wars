package property_classes;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Property_Number implements Property {

	private String name;
	
	private BigDecimal number;
	
	private JTextField txtNumber;
	
	public Property_Number(String name, BigDecimal number) {
		
		this.name = name;
		this.number =  number;
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getValueName() {
		return number.toString();
	}
	
	public BigDecimal getValue() {
		return number;
	}

	/**
	 * Takes a string as parameter
	 */
	public void setValue(Object i) {
		
		number = new BigDecimal((String) i );
		
	}

	@SuppressWarnings("serial")
	public JPanel makeEditDialogPanel(JPanel defaultPanel, final OnChange onChange) {

		JPanel panel = defaultPanel;

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;

		JLabel namelbl = new JLabel(name);
		panel.add(namelbl, c);

		txtNumber = new JTextField();
		
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
		
		txtNumber.setText(number.toString());
		
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

		return panel;

	}

	public void setValue() {

		number = new BigDecimal(txtNumber.getText());
		
	}
	
}
