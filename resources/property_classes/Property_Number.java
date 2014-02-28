package property_classes;

import java.math.BigDecimal;

public class Property_Number implements Property {

	private String name;
	
	private BigDecimal number;
	
	
	public Property_Number(String name, BigDecimal number) {
		
		this.name = name;
		this.number =  number;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public BigDecimal getValue() {
		
		return number;
		
	}

	/**
	 * Takes a string as parameter
	 */
	public void setValue(Object i) {
		
		number = new BigDecimal((String) i );
		
	}

}
