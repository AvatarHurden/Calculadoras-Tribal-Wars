package database;

import io.github.avatarhurden.tribalwarsengine.tools.property_classes.Property;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.Property_Escopo;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.Property_Nome;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.Property_UnidadeList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import config.Mundo_Reader;

/**
 * Class that stores a specific number of every unit to be used in different
 * tools and reused after closing program.
 * O
 * @author Arthur
 * 
 */
public class ModeloTropas {

	private Property_Nome nome;

	private Property_UnidadeList quantidades = new Property_UnidadeList();
	
	private Property_Escopo escopo;
	
	// This list will declare the order in which the map will be displayed
	public List<Property> variableList = new ArrayList<Property>();

	public ModeloTropas() {

		nome = new Property_Nome("Novo Modelo");
		
		for (Unidade i : Unidade.values())
			quantidades.put(i, BigDecimal.ZERO);
		
		escopo = new Property_Escopo(Mundo_Reader.MundoSelecionado);
		
		setVariableList();

	}
	
	public ModeloTropas(Properties p) {

		nome = new Property_Nome(p.getProperty("nome"));

		for (Unidade i : Unidade.values()) {
			String nome = i.nome().toLowerCase().replace(' ', '_');
			quantidades.put(i, new BigDecimal(p.getProperty(nome)));
		}
		
		String[] worlds = {""};
		// For compatibility with older versions
		try {
			worlds = p.getProperty("escopo").split(" \",\"");
		} catch (NullPointerException e) {}
		
		List<Mundo> mundos = new ArrayList<Mundo>();
		for (String s : worlds)
			mundos.add(Mundo_Reader.getMundo(s));
		
		escopo = new Property_Escopo(mundos);

		setVariableList();

	}

	public ModeloTropas(String nome, Map<Unidade, BigDecimal> map, Mundo... mundos) {

		if (nome == null)
			this.nome = new Property_Nome("Novo Modelo");
		else
			this.nome = new Property_Nome(nome);

		for (Entry<Unidade, BigDecimal> i : map.entrySet())
			quantidades.put(i.getKey(), i.getValue());
		
		escopo = new Property_Escopo(mundos);
		
		setVariableList();

	}

	private void setVariableList() {

		variableList.add(nome);
		variableList.add(quantidades);
		variableList.add(escopo);
		
	}

	public String toString() {
		return nome.getValueName();
	}

	public String getNome() {
		return nome.getValueName();
	}

	public BigDecimal getQuantidade(Unidade i) {

		return quantidades.get(i);

	}
	
	/**
	 * Retorna uma lista com todos os mundos nos quais o modelo está disponível.
	 */
	public List<Mundo> getEscopo() {
		
		if (escopo.getListMundos().isEmpty())
			return Mundo_Reader.getMundoList();
		else
			return escopo.getListMundos();
		
	}

	public String getConfigText() {
		
		String s = "\n";
		
		s += ("\tnome=" + nome.getValueName() + "\n");

		s += ("\tescopo=");
		for (Mundo m : escopo.getListMundos())
			s += m.toString() + " \",\"";
		s += "\n";
		
		for (Unidade i : Unidade.values())
			s += "\t" + i.nome().toLowerCase().replace(' ', '_') + "=" + quantidades.get(i) + "\n";

		return s;
	}
	
}
