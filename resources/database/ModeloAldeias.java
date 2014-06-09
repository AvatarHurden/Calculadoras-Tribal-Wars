package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import property_classes.Property;
import property_classes.Property_Coordenada;
import property_classes.Property_Nome;

/**
 * Class that stores information about a specific village (coordinates and building levels)
 * 
 * @author Arthur
 * 
 */
public class ModeloAldeias {
	
	
	private Property_Nome nome;

	private Property_Coordenada coordenadas;
	
	private Property_Edifícios edifícios;

	// This list will declare the order in which the map will be displayed
	public List<Property> variableList = new ArrayList<Property>();

	public ModeloAldeias() {

		nome = new Property_Nome("Nova Aldeia");
		
		for (Unidade i : Unidade.values())
			quantidades.put(i, BigDecimal.ZERO);
		
		setVariableList();

	}
	
	public ModeloAldeias(Properties p) {

		nome = new Property_Nome(p.getProperty("nome"));

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

		this.nome = new Property_Nome(nome);

		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.put(i.getKey(), i.getValue());

		setVariableList();

	}

	private void setVariableList() {

		variableList.add(nome);
		variableList.add(quantidades);

	}

	public String toString() {
		return nome.getValueName();
	}

	public void setNome(String s) {
		nome = new Property_Nome(s);
	}

	/**
	 * @param map A map<Unidade, BigDecimal>
	 */
	public void setMap(Map<Unidade, BigDecimal> map) {

		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.put(i.getKey(), i.getValue());

	}

	public String getNome() {
		return nome.getValueName();
	}

	public BigDecimal getQuantidade(Unidade i) {

		return quantidades.get(i);

	}

	

	public String getConfigText() {
		
		
	}
	
}
