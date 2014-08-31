package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.objects.building.Building;

import javax.swing.SwingConstants;

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
	
	
	public EdifícioFormattedTextField(final Building ed, int initial) {
		
		super(initial, 3, ed.getMaxLevel());
		
		setHorizontalAlignment(SwingConstants.CENTER);
		
	}
	
	abstract public void go();
		
	public int getValueInt() {
		
		return getValue().intValue();
		
	}
	
}
