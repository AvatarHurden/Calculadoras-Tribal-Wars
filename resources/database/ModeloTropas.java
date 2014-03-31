package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import property_classes.Property;
import property_classes.Property_Nome;
import property_classes.Property_UnidadeList;

/**
 * Class that stores a specific number of every unit to be used in different
 * tools and reused after closing program.
 * 
 * @author Arthur
 * 
 */
public class ModeloTropas {

	private Property_Nome nome;

	// private Map<Unidade, BigDecimal> quantidades = new HashMap<Unidade,
	// BigDecimal>();

	private Property_UnidadeList quantidades = new Property_UnidadeList();

	// This list will declare the order in which the map will be displayed
	public List<Property> variableList = new ArrayList<Property>();

	public ModeloTropas() {

		nome = new Property_Nome("Novo Modelo");
		
		for (Unidade i : Unidade.values())
			quantidades.put(i, BigDecimal.ZERO);
		
		setVariableList();

	}
	
	public ModeloTropas(Properties p) {

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

	public Map<Unidade, BigDecimal> getList() {

		return quantidades;

	}

	public String getConfigText() {
		
		String s = "";
		
		s += ("\tnome=" + nome.getName() + "\n");

		s += ("\tlanceiro=" + quantidades.get(Unidade.LANCEIRO) + "\n");
		s += ("\tespadachim=" + quantidades.get(Unidade.ESPADACHIM) + "\n");
		s += ("\tbárbaro=" + quantidades.get(Unidade.BÁRBARO) + "\n");
		s += ("\tarqueiro=" + quantidades.get(Unidade.ARQUEIRO) + "\n");
		s += ("\texplorador=" + quantidades.get(Unidade.EXPLORADOR) + "\n");
		s += ("\tcavalaria_leve=" + quantidades.get(Unidade.CAVALOLEVE) + "\n");
		s += ("\tarqueiro_a_cavalo=" + quantidades.get(Unidade.ARCOCAVALO) + "\n");
		s += ("\tcavalaria_pesada=" + quantidades.get(Unidade.CAVALOPESADO) + "\n");
		s += ("\taríete=" + quantidades.get(Unidade.ARÍETE) + "\n");
		s += ("\tcatapulta=" + quantidades.get(Unidade.CATAPULTA) + "\n");
		s += ("\tpaladino=" + quantidades.get(Unidade.PALADINO) + "\n");
		s += ("\tnobre=" + quantidades.get(Unidade.NOBRE) + "\n");
		s += ("\tmilícia=" + quantidades.get(Unidade.MILÍCIA) + "\n");

		return s;
	}
	
}
