package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.objects.building.Building;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import javax.swing.SwingConstants;

/**
 * 
 * Extends <class>IntegerFormattedTextField</class>, with the edition of center alignment
 * and an initial value determined on constructor
 * 
 * @author Arthur
 *
 */
public class EdifícioFormattedTextField extends IntegerFormattedTextField {
	
	public EdifícioFormattedTextField(final Building ed, int initial, OnChange onChange) {
		
		super(initial, 3, ed.getMaxLevel(), onChange);
		
		setHorizontalAlignment(SwingConstants.CENTER);
		
	}

}
