package property_classes;

import java.math.BigDecimal;
import java.util.HashMap;

import database.Unidade;

@SuppressWarnings({ "serial" })
public class Property_UnidadeList extends HashMap<Unidade, BigDecimal> implements Property {
	
	public String getName() {
		
		return null;
		
	}

	/**
	 * Takes a hashMap<Unidade, BigDecimal> as parameter
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object i) {
		
		clear();
		
		putAll((HashMap<Unidade, BigDecimal>) i);
		
	} 

}
