package property_classes;

public class Property_Boolean implements Property{

	private String name;
	
	private boolean isTrue;
	
	public Property_Boolean(String name, boolean isTrue) {
		
		this.name = name;
		this.isTrue = isTrue;
		
	}
	
	public String getName() {
	
		return name;
		
	}
	
	public String getValueName() {
		
		if (isTrue)
			return "Ativado";
		else
			return "Desativado";
		
	}
	
	public boolean getValue() {
		return isTrue;
	}

	/**
	 * Takes a boolean as parameter
	 */
	public void setValue(Object i) {
		
		isTrue = (boolean)i;
	}
	
}
