package custom_components;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * JTextField that formats the number to add thousands separator
 * Unlike JFormattedTextField, the display is updated dynamically as the
 * user types, not allowing the insertion of invalid characters (letter or symbols)
 * 
 * <br>Abstract method is called on every change of text
 * 
 * <br>By default, contains: 
 * <br>- Left horizontal text alignment
 * <br>- DocumentListener with abstract class that activates on every change
 * <br>- DocumentFilter to only allow <code>maxLength</code> digits and up to <code>upperLimit</code>
 * 
 * @author Arthur
 * 
 */
@SuppressWarnings("serial")
public abstract class IntegerFormattedTextField extends JTextField {

	// Location on format
	// TODO make location changeable, depending on language;
	public NumberFormat numberFormat = NumberFormat
			.getNumberInstance(Locale.GERMANY);

	/**
	 * @param length
	 *            Maximum number of digits allowed
	 * @param upperLimit
	 * 			  Maximum value of the textField
	 */
	public IntegerFormattedTextField(final int maxLength,final int upperLimit) {
		
		setColumns(3+(maxLength-2)/2);
		
		setHorizontalAlignment(SwingConstants.LEFT);
		
		((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
			
		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String inputTextValue,
		            AttributeSet attrs) throws BadLocationException {
				
		        Document oldDoc = fb.getDocument();
		        // Gets the whole string that would be in the textField
		        String textValue = oldDoc.getText(0, oldDoc.getLength()) + inputTextValue;
		        int level;
	
		        
		        if (textValue.length() < maxLength){
		        	try {
		        		level = Integer.parseInt(getUnformattedNumber(textValue));
		        	} catch (NumberFormatException e) {
		        		try {
		        			level = Integer.parseInt(textValue.replace(inputTextValue, ""));
		        		} catch (NumberFormatException ex) {
		        			// A non digit character was inserted, so no number will be placed
		        			level = -1;
		        		}
		        		
		        	}
		        	
		        	if (level > upperLimit) 
		        		level = upperLimit;
		        	
		        	// Replaces everything that was in the textField
		        	if (level == -1)
		        		// In case of the error
		        		fb.replace(0, oldDoc.getLength(), "", attrs);
		        	else
		        		fb.replace(0, oldDoc.getLength(), getFormattedNumber(String.valueOf(level)), attrs);
		        }
		    }
		    
		    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) {
		    	
		    	try {
		    		
		    		String oldString = fb.getDocument().getText(0, fb.getDocument().getLength());
		    		
		    		String s = oldString.substring(0, offset);
		    				    		
		    		fb.replace(0, oldString.length(), getFormattedNumber(getUnformattedNumber(s)), null);
				} catch (BadLocationException e) {}
		    	
		    }
		});
		

		getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent arg0) {
				go();
			}

			public void insertUpdate(DocumentEvent arg0) {
				go();
			}

			public void changedUpdate(DocumentEvent arg0) {}

		});
		
	}

	// Returns the unformatted value of a formatted string
	private String getUnformattedNumber(String input) {

		String unformated = "";
		try {
			unformated = numberFormat.parse(input).toString();
		} catch (ParseException e) {}
		
		return unformated;

	}

	// Returns the formatted value of an unformatted string
	private String getFormattedNumber(String input) {
		
		try {
			String formated = numberFormat.format(Integer.parseInt(input));
		
			return formated;
		} catch (NumberFormatException e) {
			return "";
		}
	}

	public abstract void go();

	/**
	 * @return BigDecimal the value of the text
	 */
	public BigDecimal getValue() {
		try {
			return new BigDecimal(getUnformattedNumber(getText()));
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

}
