package custom_components;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import database.Edifício;

@SuppressWarnings("serial")
/**
 * 
 * Extends <class>IntegerFormattedTextField</class>, with the edition of center alignment
 * and an initial value determined on constructor
 * 
 * @author Arthur
 *
 */
public abstract class EdifícioFormattedTextField extends IntegerFormattedTextField {
	
	
	public EdifícioFormattedTextField(final Edifício ed, int initial) {
		
		super(3, ed.nívelMáximo());
		
		DocumentListener doc = ((AbstractDocument) getDocument()).getDocumentListeners()[0];
		
		// Removes document to allow edition of text without invoking go().
		getDocument().removeDocumentListener(doc);
		
		setText(""+initial);
		
		getDocument().addDocumentListener(doc);
		
		setHorizontalAlignment(SwingConstants.CENTER);
		
		
	}
	
	abstract public void go();
		
	public int getValueInt() {
		
		return getValue().intValue();
		
	}
	
}
