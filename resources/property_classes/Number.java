package property_classes;

import java.math.BigDecimal;

public class Number implements Property {

	private String name;
	
	private BigDecimal number;
	
	public Number(String name, BigDecimal number) {
		
		this.name = name;
		this.number =  number;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public BigDecimal getValue() {
		
		return number;
		
	}

}
