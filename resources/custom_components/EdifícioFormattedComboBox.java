package custom_components;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import database.Edifício;

public abstract class EdifícioFormattedComboBox extends JTextField {
	
	int max;
	
	public EdifícioFormattedComboBox(final Edifício ed, int initial) {
		
		super(3);
		
		max = ed.nívelMáximo();
		
		setHorizontalAlignment(SwingConstants.CENTER);
		
		setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de números e no máximo 3 dígitos
				if ((getLength() + str.length()) <= 2
						&& (Character.isDigit(str.charAt(0))))
					super.insertString(offset, str, attr);
	
			}
			
		});
		
		((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
			
			@Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String inputTextValue,
		            AttributeSet attrs) throws BadLocationException {
		        Document oldDoc = fb.getDocument();
		        String textValue = oldDoc.getText(0, oldDoc.getLength()) + inputTextValue;
		        Integer basePrice = 0;
		        if (length <= 2){
		        	try {
		        		basePrice = Integer.parseInt(textValue);
		        	} catch (NumberFormatException e) { 
		        		basePrice = 0;
		        	}
		        	if (basePrice > max) 
		        		basePrice = max;
		        	fb.replace(0, oldDoc.getLength(), basePrice.toString(), attrs);
		        }
		    }
		});
		
		getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				go();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				go();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				go();
			}

		}); 
		
	}
	
	private void setMaximum() {
		
	}

	abstract public void go();
		
	public int getValueInt() {
		
		if (!getText().equals(""))
			return Integer.parseInt(getText());
		else
			return 0;
		
	}
	
}
