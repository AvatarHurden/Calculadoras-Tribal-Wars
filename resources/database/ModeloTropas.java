package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import property_classes.Quantidade;

/**
 * Class that stores a specific number of every unit to be used in different
 * tools and reused after closing program.
 * 
 * @author Arthur
 *
 */
public class ModeloTropas {
	
	private String nome;
	
//	private Map<Unidade, BigDecimal> quantidades = new HashMap<Unidade, BigDecimal>();
	
	private List<Quantidade> quantidades = new ArrayList<Quantidade>();
	
//	// This list will declare the order in which the map will be displayed
//	private static List<Unidade> quantidadesList = new ArrayList<Unidade>(); 
	
	public ModeloTropas() {
		
	}
	
	public ModeloTropas(Properties p) {
		
		nome = p.getProperty("nome");
		
//		for (Unidade i : Unidade.values()){
//			String nome = i.nome().toLowerCase().replace(' ', '_');
//			quantidades.put(i, new BigDecimal(p.getProperty(nome)));
//		}
		
		for (Unidade i : Unidade.values()) {
			String nome = i.nome().toLowerCase().replace(' ', '_');
			quantidades.add(new Quantidade(i, new BigDecimal(p.getProperty(nome))));
		}
		
	}
	
	public ModeloTropas(String nome, Map<Unidade, BigDecimal> map) {
		
		this.nome = nome;
		
		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.add(new Quantidade(i.getKey(), i.getValue()));
		
	}
	
	public String toString() {
		return nome;
	}
	
	public void setNome(String s) {
		nome = s;
	}
	
	/**
	 * @param map A map<Unidade, BigDecimal>
	 */
	public void setList(Map<Unidade, BigDecimal> map) {
	
		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.add(new Quantidade(i.getKey(), i.getValue()));
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public BigDecimal getQuantidade(Unidade i) {
		
		for (Quantidade q : quantidades)
			if (q.getUnidade().equals(i))
				return q.getValue();
		
		return null;
		
	}
	
	public Map<Unidade, BigDecimal> getList() {
		
		Map<Unidade, BigDecimal> map = new HashMap<Unidade, BigDecimal>();
		
		for (Quantidade i : quantidades)
			map.put(i.getUnidade(), i.getValue());

		return map;
	}
	
}
