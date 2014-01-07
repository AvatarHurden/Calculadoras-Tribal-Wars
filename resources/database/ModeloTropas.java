package database;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that stores a specific number of every unit to be used in different
 * tools and reused after closing program.
 * 
 * @author Arthur
 *
 */
public class ModeloTropas {
	
	private String nome;
	
	private Map<Unidade, BigDecimal> list = new HashMap<Unidade, BigDecimal>();
	

	public ModeloTropas(String nome, Map<Unidade, BigDecimal> list) {
		
		this.nome = nome;
		this.list = list;
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public Map<Unidade, BigDecimal> getList() {
		return list;
	}
	
}
