package io.github.avatarhurden.tribalwarsengine.components;

import javax.swing.SwingConstants;

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
		
		super(initial, 3, ed.getNívelMáximo());
		
		setHorizontalAlignment(SwingConstants.CENTER);
		
	}
	
	abstract public void go();
		
	public int getValueInt() {
		
		return getValue().intValue();
		
	}
	
}
