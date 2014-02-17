package property_classes;

public class Property_Escolha implements Property {

	private String name;
	
	int selectedOption;
	
	private String[] options;
	
	public Property_Escolha (String name, String selected, String ... options) {
		
		this.name = name;
		
		this.options = options;
		
		for (int i = 0; i < options.length; i++)
			if (options[i].equals(selected))
				selectedOption = i; 
		
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOption(String s) {
		
		if (options[selectedOption].equals(s))
			return true;
		else
			return false;
		
	}
	
	public String[] getOptions() {
		return options;
	}

}
