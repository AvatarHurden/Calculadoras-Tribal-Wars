package io.github.avatarhurden.tribalwarsengine.components;

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
	private NumberFormat numberFormat = NumberFormat
			.getNumberInstance(Locale.GERMANY);

	/**
	 * @param startingValue
	 * 			  Value that the textField starts with
	 * @param maxLength
	 *            Maximum number of digits allowed
	 * @param upperLimit
	 * 			  Maximum value of the textField
	 */
	public IntegerFormattedTextField(int startingValue, final int maxLength, final int upperLimit) {
		
		setColumns(3+(maxLength-2)/2);
		
		setHorizontalAlignment(SwingConstants.LEFT);
		
		if (startingValue > 0)
			setText(String.valueOf(startingValue));
		
		((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
			
		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String inputTextValue,
		            AttributeSet attrs) throws BadLocationException {
				
		    
		        Document oldDoc = fb.getDocument();
		        
		        String oldString = fb.getDocument().getText(0, fb.getDocument().getLength());
		        
		        String string = oldString.substring(0, offset) + inputTextValue + 
	    				oldString.substring(offset+length, fb.getDocument().getLength());
		        
		        string = getUnformattedNumber(string);
		        
		        int amount;
		        
		        if (string.length() <= maxLength){
		        	try {
		        		amount = Integer.parseInt(getUnformattedNumber(string));
		        	} catch (NumberFormatException e) {
		        		try {
		        			amount = Integer.parseInt(string.replace(inputTextValue, ""));
		        		} catch (NumberFormatException ex) {
		        			// A non digit character was inserted, so no number will be placed
		        			amount = -1;
		        		}
		        		
		        	}
		        	
		        	if (amount > upperLimit) 
		        		amount = upperLimit;
		        	
		        	// Replaces everything that was in the textField
		        	if (amount == -1)
		        		// In case of the error
		        		fb.replace(0, oldDoc.getLength(), "", attrs);
		        	else
		        		fb.replace(0, oldDoc.getLength(), getFormattedNumber(String.valueOf(amount)), attrs);
		        }
		    }
		    
		    public void insertString (DocumentFilter.FilterBypass fb, int offset, String string, 
		    		AttributeSet attr) throws BadLocationException {
		    	
		    	 Document oldDoc = fb.getDocument();
			        // Gets the whole string that would be in the textField
			        String textValue = oldDoc.getText(0, oldDoc.getLength()) + string;
			        int level;
		
			        
			        if (textValue.length() < maxLength){
			        	try {
			        		level = Integer.parseInt(getUnformattedNumber(textValue));
			        	} catch (NumberFormatException e) {
			        		try {
			        			level = Integer.parseInt(textValue.replace(string, ""));
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
			        		fb.replace(0, oldDoc.getLength(), "", attr);
			        	else
			        		fb.replace(0, oldDoc.getLength(), getFormattedNumber(String.valueOf(level)), attr);
			        }
		    	
		    }
		    
		    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) {
		    	
		    	try {
		    		
		    		// Full string
		    		String oldString = fb.getDocument().getText(0, fb.getDocument().getLength());
		    		
		    		String s = oldString.substring(0, offset) + 
		    				oldString.substring(offset+length, fb.getDocument().getLength());
		    				    		
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

	public IntegerFormattedTextField(int startingValue, int maxLength) {
		this(startingValue, maxLength, Integer.MAX_VALUE);
	}
	
	public IntegerFormattedTextField(int startingValue) {
		this(startingValue, 9, Integer.MAX_VALUE);
	}
	
	public IntegerFormattedTextField() {
		this(0, 9, Integer.MAX_VALUE);
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
