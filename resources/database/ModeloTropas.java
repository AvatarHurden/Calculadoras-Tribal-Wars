package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import property_classes.Property;
import property_classes.Property_UnidadeList;

/**
 * Class that stores a specific number of every unit to be used in different
 * tools and reused after closing program.
 * 
 * @author Arthur
 * 
 */
public class ModeloTropas {

	private String nome;

	// private Map<Unidade, BigDecimal> quantidades = new HashMap<Unidade,
	// BigDecimal>();

	private Property_UnidadeList quantidades = new Property_UnidadeList();

	// This list will declare the order in which the map will be displayed
	public List<Property> variableList = new ArrayList<Property>();

	public ModeloTropas() {

		setVariableList();

	}

	public ModeloTropas(Properties p) {

		nome = p.getProperty("nome");

		// for (Unidade i : Unidade.values()){
		// String nome = i.nome().toLowerCase().replace(' ', '_');
		// quantidades.put(i, new BigDecimal(p.getProperty(nome)));
		// }

		for (Unidade i : Unidade.values()) {
			String nome = i.nome().toLowerCase().replace(' ', '_');
			quantidades.put(i, new BigDecimal(p.getProperty(nome)));
		}

		setVariableList();

	}

	public ModeloTropas(String nome, Map<Unidade, BigDecimal> map) {

		this.nome = nome;

		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.put(i.getKey(), i.getValue());

		setVariableList();

	}

	private void setVariableList() {

		variableList.add(quantidades);

	}

	public String toString() {
		return nome;
	}

	public void setNome(String s) {
		nome = s;
	}

	/**
	 * @param map
	 *            A map<Unidade, BigDecimal>
	 */
	public void setMap(Map<Unidade, BigDecimal> map) {

		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.put(i.getKey(), i.getValue());

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
