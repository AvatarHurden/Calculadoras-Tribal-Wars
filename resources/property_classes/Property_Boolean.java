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
	
	public boolean getValue() {
		return isTrue;
	}

	
	
}
