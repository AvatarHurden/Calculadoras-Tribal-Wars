package property_classes;

import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JPanel;

public interface Property {

	// The methods referring to EditDialog exist so that, in the future, more properties may be created
	// without having to make changes to EditDialog. Just another attempt to turn everything into a module 
	
//	/**
//	 * Object that will be used on EditDialog.
//	 */
//	Object getEditDialogObject();
	
	/**
	 * Creates the panel to be added to EditDialog.
	 * @return JPanel
	 */
	JPanel makeEditDialogPanel(JPanel panel, Method onChange);
	
	/**
	 * Gives the user-friendly name of the property
	 * @return String name of property
	 */
	String getName();

	/**
	 * Gives the user-friendly value of the property
	 * @return String value of property
	 */
	String getValueName();
	
	void setValue();
	
}
