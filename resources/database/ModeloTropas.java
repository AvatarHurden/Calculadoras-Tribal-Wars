package database;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class that stores a specific number of every unit to be used in different
 * tools and reused after closing program.
 * 
 * @author Arthur
 *
 */
public class ModeloTropas {
	
	private String nome;
	
	public Map<Unidade, BigDecimal> list = new HashMap<Unidade, BigDecimal>();
	

	public ModeloTropas() {
		
	}
	
	public ModeloTropas(Properties p) {
		
		nome = p.getProperty("nome");
		
		for (Unidade i : Unidade.values()){
			String nome = i.nome().toLowerCase().replace(' ', '_');
			list.put(i, new BigDecimal(p.getProperty(nome)));
		}
		
	}
	
	public ModeloTropas(String nome, Map<Unidade, BigDecimal> list) {
		
		this.nome = nome;
		this.list = list;
		
	}
	
	public String toString() {
		return nome;
	}
	
	public void setNome(String s) {
		nome = s;
	}
	
	public void setList(Map<Unidade, BigDecimal> list) {
		this.list = list;
	}
	
	public String getNome() {
		return nome;
	}
	
	public BigDecimal getQuantidade(Unidade i) {
		return list.get(i);
	}
	
	public Map<Unidade, BigDecimal> getList() {
		return list;
	}
	
}
