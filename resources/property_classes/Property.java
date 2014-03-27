package property_classes;

public interface Property {

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
	
	void setValue(Object i);
	
}
