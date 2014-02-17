package property_classes;

import java.math.BigDecimal;

import database.Unidade;

public class Property_Quantidade implements Property{
	
	private Unidade unidade;
	
	private BigDecimal quantidade;
	
	public Property_Quantidade(Unidade unidade, BigDecimal quantidade) {
		
		this.unidade = unidade;
		this.quantidade = quantidade;
		
	}
	
	public String getName() {
		return unidade.nome();
	}
	
	public Unidade getUnidade() {
		return unidade;
	}
	
	public BigDecimal getValue(){
		return quantidade;
	}

}
