package database;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
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
	
	private Map<Unidade, BigDecimal> quantidades = new HashMap<Unidade, BigDecimal>();
	
	// This list will declare the order in which the map will be displayed
	private static List<Unidade> quantidadesList = new ArrayList<Unidade>(); 
	
	public ModeloTropas() {
		
	}
	
	public ModeloTropas(Properties p) {
		
		nome = p.getProperty("nome");
		
		for (Unidade i : Unidade.values()){
			String nome = i.nome().toLowerCase().replace(' ', '_');
			quantidades.put(i, new BigDecimal(p.getProperty(nome)));
		}
		
	}
	
	public ModeloTropas(String nome, Map<Unidade, BigDecimal> list) {
		
		this.nome = nome;
		this.quantidades = list;
		
	}
	
	public String toString() {
		return nome;
	}
	
	public void setNome(String s) {
		nome = s;
	}
	
	public void setList(Map<Unidade, BigDecimal> list) {
		this.quantidades = list;
	}
	
	public String getNome() {
		return nome;
	}
	
	public BigDecimal getQuantidade(Unidade i) {
		return quantidades.get(i);
	}
	
	public Map<Unidade, BigDecimal> getList() {
		return quantidades;
	}
	
}
