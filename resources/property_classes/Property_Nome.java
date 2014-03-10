package property_classes;

public class Property_Nome implements Property{

	private String name;
	
	public Property_Nome(String s) {
		name = s;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Takes a string as parameter
	 */
	public void setValue(Object i) {
		
		name = (String) i;
		
	}

	
	
}
