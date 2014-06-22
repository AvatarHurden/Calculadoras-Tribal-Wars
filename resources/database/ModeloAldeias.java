package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import property_classes.Property;
import property_classes.Property_Coordenada;
import property_classes.Property_Edifícios;
import property_classes.Property_Escopo;
import property_classes.Property_Nome;
import property_classes.Property_UnidadeList;
import config.Mundo_Reader;

/**
 * Class that stores information about a specific village (coordinates and building levels)
 * 
 * @author Arthur
 * 
 */
public class ModeloAldeias {
	
	
	private Property_Nome nome;

	private Property_Coordenada coordenadas;
	
	private Property_Edifícios edifícios = new Property_Edifícios();;
	
	private Property_Escopo escopo;

	// This list will declare the order in which the map will be displayed
	public List<Property> variableList = new ArrayList<Property>();

	public ModeloAldeias() {

		nome = new Property_Nome("Nova Aldeia");
		
		coordenadas = new Property_Coordenada(0, 0);
		
		for (Edifício i : Edifício.values())
			edifícios.put(i, 0);
		
		escopo = new Property_Escopo(Mundo_Reader.MundoSelecionado);
		
		setVariableList();

	}
	
	public ModeloAldeias(Properties p) {

		nome = new Property_Nome(p.getProperty("nome"));
		
		String[] coord = p.getProperty("coordenadas").split("|");
		
		coordenadas = new Property_Coordenada(Integer.parseInt(coord[0]), Integer.parseInt(coord[0]));
		
		for (Edifício e : Edifício.values()) {
			
			if (!e.equals(Edifício.NULL)) {
				String nome = e.nome().toLowerCase().replace(' ', '_');
				if (e.equals(Edifício.ACADEMIA_1NÍVEL)) nome += "_1_nível";
				else if (e.equals(Edifício.ACADEMIA_3NÍVEIS)) nome += "_3_níveis";
			
				edifícios.put(e, Integer.parseInt(p.getProperty(nome)));
			}
		}
		
		String[] worlds = p.getProperty("escopo").split(" \",\"");
		
		List<Mundo> mundos = new ArrayList<Mundo>();
		for (String s : worlds)
			mundos.add(Mundo_Reader.getMundo(s));
		
		escopo = new Property_Escopo(mundos);

		setVariableList();

	}

	public ModeloAldeias(String nome, Map<Edifício, Integer> map, int x, int y, Mundo... mundos) {

		this.nome = new Property_Nome(nome);

		coordenadas = new Property_Coordenada(x, y);
		
		for (Entry<Edifício, Integer> i : map.entrySet())
			edifícios.put(i.getKey(), i.getValue());
		
		escopo = new Property_Escopo(mundos);
		
		setVariableList();

	}

	private void setVariableList() {

		variableList.add(nome);
		variableList.add(coordenadas);
		variableList.add(edifícios);
		variableList.add(escopo);

	}

	public String toString() {
		return nome.getValueName();
	}

	
	public String getNome() {
		return nome.getValueName();
	}

	public int getNível(Edifício e) {
		return edifícios.get(e);
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
		
		s += "\tcoordenadas=" + coordenadas.getValueName() + "\n";
		
		for (Edifício e : Edifício.values()) {
			
			if (!e.equals(Edifício.NULL)) {
				if (e.equals(Edifício.ACADEMIA_1NÍVEL)) s += "\tacademia_1_nível="+ edifícios.get(e)+"\n";
				else if (e.equals(Edifício.ACADEMIA_3NÍVEIS)) s += "\tacademia_3_níveis="+ edifícios.get(e)+"\n";
				else s += "\t"+e.nome().toLowerCase().replace(' ', '_')+"="+ edifícios.get(e)+"\n";
			}
			
		}

		return s;
		
	}
	
}
