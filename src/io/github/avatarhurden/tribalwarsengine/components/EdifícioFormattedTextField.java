package io.github.avatarhurden.tribalwarsengine.components;

import javax.swing.SwingConstants;

import database.Edif�cio;

@SuppressWarnings("serial")
/**
 * 
 * Extends <class>IntegerFormattedTextField</class>, with the edition of center alignment
 * and an initial value determined on constructor
 * 
 * @author Arthur
 *
 */
public abstract class Edif�cioFormattedTextField extends IntegerFormattedTextField {
	
	
	public Edif�cioFormattedTextField(final Edif�cio ed, int initial) {
		
		super(initial, 3, ed.getN�velM�ximo());
		
		setHorizontalAlignment(SwingConstants.CENTER);
		
	}
	
	abstract public void go();
		
	public int getValueInt() {
		
		return getValue().intValue();
		
	}
	
}
