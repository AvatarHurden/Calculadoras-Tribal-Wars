package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import property_classes.Property;
import property_classes.Property_Boolean;
import property_classes.Property_Escolha;
import property_classes.Property_Nome;
import property_classes.Property_Number;

/**
 * Classe para a cria��o de diferentes mundos
 * 
 * @author Arthur
 */
public class Mundo {

	private Property_Nome nome;

	private Property_Boolean hasArqueiro;
	private Property_Boolean hasMil�cia;
	private Property_Boolean hasPaladino;
	private Property_Boolean hasItensAprimorados;
	private Property_Boolean hasIgreja;
	private Property_Boolean hasMoral;
	private Property_Boolean hasBandeira;
	private Property_Boolean hasBonusNoturno;
	private Property_Boolean cunhagemDeMoedas;

	private Property_Escolha sistemaDePesquisa;

	private Property_Number velocidade;
	private Property_Number modificarUnidades;

	// List of properties in the order they are to be shown
	public List<Property> variableList = new ArrayList<Property>();

	private Unidade[] unidades = new Unidade[13];

	private BigDecimal[] porcentagemDeProdu��o = new BigDecimal[26];
	
	public Mundo(String nome) {

		this.nome = new Property_Nome(nome);

	}

	/**
	 * Constructor for a default world
	 */
	public Mundo() {
		
		nome = new Property_Nome("Novo Mundo");
		
		hasArqueiro = new Property_Boolean("Arqueiro", false);
		
		hasMil�cia = new Property_Boolean("Mil�cia", false);

		hasPaladino = new Property_Boolean("Paladino", false);

		hasItensAprimorados = new Property_Boolean("Itens Aprimorados", false);

		hasIgreja = new Property_Boolean("Igreja", false);

		hasMoral = new Property_Boolean("Moral", false);

		hasBandeira = new Property_Boolean("Bandeiras", false);

		hasBonusNoturno = new Property_Boolean("B�nus Noturno", false);

		// mudar para escolha com "moeda" ou "nivel"
		// chamar de "sistemaDeAcademia"
		
		cunhagemDeMoedas = new Property_Boolean("Cunhagem de Moedas", false);

		// mudar para "simples" ou "nivel"
		
		sistemaDePesquisa = new Property_Escolha("Sistema de Pesquisa",
			"Pesquisa Simples", "Pesquisa Simples", "Pesquisa de 3 N�veis", "Pesquisa de 10 N�veis");

		velocidade = new Property_Number("Velocidade", BigDecimal.ONE);

		modificarUnidades = new Property_Number("Modificador de Unidade", BigDecimal.ONE);

		variableList.add(nome);
		variableList.add(velocidade);
		variableList.add(modificarUnidades);
		variableList.add(hasMoral);
		variableList.add(sistemaDePesquisa);
		variableList.add(hasIgreja);
		variableList.add(hasBonusNoturno);
		variableList.add(hasBandeira);
		variableList.add(hasArqueiro);
		variableList.add(hasPaladino);
		variableList.add(hasItensAprimorados);
		variableList.add(hasMil�cia);
		variableList.add(cunhagemDeMoedas);
		
	}
	/**
	 * Constructor for a personalized world
	 * @param the Properties of the world
	 */
	public Mundo(Properties prop) {
		
		nome = new Property_Nome(prop.getProperty("nome"));

		hasArqueiro = new Property_Boolean("Arqueiro",
				Boolean.parseBoolean(prop.getProperty("arqueiro")));

		hasMil�cia = new Property_Boolean("Mil�cia", Boolean.parseBoolean(prop
				.getProperty("milicia")));

		hasPaladino = new Property_Boolean("Paladino",
				Boolean.parseBoolean(prop.getProperty("paladino")));

		hasItensAprimorados = new Property_Boolean("Itens Aprimorados",
				Boolean.parseBoolean(prop.getProperty("itensAprimorados")));

		hasIgreja = new Property_Boolean("Igreja", Boolean.parseBoolean(prop
				.getProperty("igreja")));

		hasMoral = new Property_Boolean("Moral", Boolean.parseBoolean(prop
				.getProperty("moral")));

		hasBandeira = new Property_Boolean("Bandeiras",
				Boolean.parseBoolean(prop.getProperty("bandeira")));

		hasBonusNoturno = new Property_Boolean("B�nus Noturno",
				Boolean.parseBoolean(prop.getProperty("bonusNoturno")));

		cunhagemDeMoedas = new Property_Boolean("Cunhagem de Moedas",
				Boolean.parseBoolean(prop.getProperty("cunhagemDeMoedas")));
		
		String selected = null;
		switch (prop.getProperty("sistemaDePesquisa")) {
		case "simples" :
			selected = "Pesquisa Simples";
			break;
		case "3niveis" :
			selected = "Pesquisa de 3 N�veis";
			break;
		case "10niveis":
			selected = "Pesquisa de 10 N�veis";
			break;
		default:
			selected = "Pesquisa de 3 N�veis";
		}
		
		sistemaDePesquisa = new Property_Escolha("Sistema de Pesquisa",
				selected,"Pesquisa Simples", "Pesquisa de 3 N�veis", "Pesquisa de 10 N�veis");
		
		String speed = prop.getProperty("velocidade");
		speed = speed.replaceAll(",", ".");
		velocidade = new Property_Number("Velocidade", new BigDecimal(speed));

		String modifier = prop.getProperty("modificador");
		modifier = modifier.replaceAll(",", ".");
		modificarUnidades = new Property_Number("Modificador de Unidade",
				new BigDecimal(modifier));

		variableList.add(nome);
		variableList.add(velocidade);
		variableList.add(modificarUnidades);
		variableList.add(hasMoral);
		variableList.add(sistemaDePesquisa);
		variableList.add(hasIgreja);
		variableList.add(hasBonusNoturno);
		variableList.add(hasBandeira);
		variableList.add(hasArqueiro);
		variableList.add(hasPaladino);
		variableList.add(hasItensAprimorados);
		variableList.add(hasMil�cia);
		variableList.add(cunhagemDeMoedas);
		
	}
	
	public String getConfigText() {

		// the "\t" is for indentation purposes

		String s = "\n";

		s += ("\tnome=" + nome.getValueName() + "\n");
		s += ("\tvelocidade=" + velocidade.getValue().toString() + "\n");
		s += ("\tmodificador=" + modificarUnidades.getValue().toString() + "\n");

		s += ("\tmoral=" + hasMoral.getValue() + "\n");
		
		String selected = null;
		switch (sistemaDePesquisa.getSelected()) {
		case "Pesquisa_Simples" :
			selected = "simples";
			break;
		case "Pesquisa_de_3_N�veis" :
			selected = "3niveis";
			break;
		case "Pesquisa_de_10_N�veis":
			selected = "10niveis";
			break;
		default:
			selected = "simples";
		}
		
		s += ("\tsistemaDePesquisa=" + selected + "\n");
		
		s += ("\tigreja=" + hasIgreja.getValue() + "\n");
		s += ("\tbonusNoturno=" + hasBonusNoturno.getValue() + "\n");
		s += ("\tbandeira=" + hasBandeira.getValue() + "\n");
		s += ("\tarqueiro=" + hasArqueiro.getValue() + "\n");
		s += ("\tpaladino=" + hasPaladino.getValue() + "\n");
		s += ("\titensAprimorados=" + hasItensAprimorados.getValue() + "\n");
		s += ("\tmilicia=" + hasMil�cia.getValue() + "\n");
		s += ("\tcunhagemDeMoedas=" + cunhagemDeMoedas.getValue() + "\n");

		return s;

	}

	/**
	 * Define a diferen�a entre o tempo padr�o de produ��o de cada unidade e o
	 * tempo real, levando em considera��o o n�vel do edif�cio e a velocidade do
	 * mundo.
	 * 
	 * Tempo real = tempo padr�o * fator de tempo (n�vel do edif�cio) /
	 * velocidade do mundo
	 */
	public void setTemposDeProdu��o() {

		// Os n�meros representam o tempo, em segundos, que leva para criar
		// 10.000 lanceiros em cada n�vel do quartel
		// Esses valores ser�o dividos pelo tempo te�rico, no n�vel 0, dando
		// assim a redu��o do tempo de produ��o causada
		// pelo n�vel do edificio.

		porcentagemDeProdu��o[0] = new BigDecimal(6800000);
		porcentagemDeProdu��o[1] = new BigDecimal(6414094);
		porcentagemDeProdu��o[2] = new BigDecimal(6051976);
		porcentagemDeProdu��o[3] = new BigDecimal(5709411);
		porcentagemDeProdu��o[4] = new BigDecimal(5386237);
		porcentagemDeProdu��o[5] = new BigDecimal(5081356);
		porcentagemDeProdu��o[6] = new BigDecimal(4793732);
		porcentagemDeProdu��o[7] = new BigDecimal(4522388);
		porcentagemDeProdu��o[8] = new BigDecimal(4266404);
		porcentagemDeProdu��o[9] = new BigDecimal(4024910);

		porcentagemDeProdu��o[10] = new BigDecimal(3797084);
		porcentagemDeProdu��o[11] = new BigDecimal(3582155);
		porcentagemDeProdu��o[12] = new BigDecimal(3379392);
		porcentagemDeProdu��o[13] = new BigDecimal(3188105);
		porcentagemDeProdu��o[14] = new BigDecimal(3007647);
		porcentagemDeProdu��o[15] = new BigDecimal(2837402);
		porcentagemDeProdu��o[16] = new BigDecimal(2676795);
		porcentagemDeProdu��o[17] = new BigDecimal(2525278);
		porcentagemDeProdu��o[18] = new BigDecimal(2382338);
		porcentagemDeProdu��o[19] = new BigDecimal(2247488);

		porcentagemDeProdu��o[20] = new BigDecimal(2120272);
		porcentagemDeProdu��o[21] = new BigDecimal(2000257);
		porcentagemDeProdu��o[22] = new BigDecimal(1887035);
		porcentagemDeProdu��o[23] = new BigDecimal(1780221);
		porcentagemDeProdu��o[24] = new BigDecimal(1679454);
		porcentagemDeProdu��o[25] = new BigDecimal(1584391);

		for (int i = 0; i < 26; i++)
			porcentagemDeProdu��o[i] = porcentagemDeProdu��o[i].divide(
					new BigDecimal(6800000), 30, BigDecimal.ROUND_HALF_EVEN);

		// Dividindo a redu��o de tempo pela velocidade do mundo, haver� o real
		// tempo de produ��o para cada situa��o.

		for (int i = 0; i < 26; i++)
			porcentagemDeProdu��o[i] = porcentagemDeProdu��o[i].divide(
					velocidade.getValue(), 30, BigDecimal.ROUND_HALF_EVEN);

	}

	/**
	 * Cria uma lista com todas as unidades presentes no mundo
	 */
	public void setUnidadeList() {

		unidades[0] = Unidade.LANCEIRO;
		unidades[1] = Unidade.ESPADACHIM;
		unidades[2] = Unidade.B�RBARO;

		if (hasArqueiro.getValue())
			unidades[3] = Unidade.ARQUEIRO;
		else
			unidades[3] = null;

		unidades[4] = Unidade.EXPLORADOR;
		unidades[5] = Unidade.CAVALOLEVE;

		if (hasArqueiro.getValue())
			unidades[6] = Unidade.ARCOCAVALO;
		else
			unidades[6] = null;

		unidades[7] = Unidade.CAVALOPESADO;
		unidades[8] = Unidade.AR�ETE;
		unidades[9] = Unidade.CATAPULTA;

		if (hasPaladino.getValue())
			unidades[10] = Unidade.PALADINO;

		unidades[11] = Unidade.NOBRE;

		if (hasMil�cia.getValue())
			unidades[12] = Unidade.MIL�CIA;

	}

	public void setNome(String nome) {
		this.nome = new Property_Nome(nome);
	}

	public String toString() {
		return nome.getValueName();
	}

	public boolean hasArqueiro() {
		return hasArqueiro.getValue();
	}

	public boolean hasMil�cia() {
		return hasMil�cia.getValue();
	}

	public boolean hasPaladino() {
		return hasPaladino.getValue();
	}

	public boolean hasItensAprimorados() {
		return hasItensAprimorados.getValue();
	}

	public boolean hasIgreja() {
		return hasIgreja.getValue();
	}

	public boolean isAcademiaDeN�veis() {
		return !cunhagemDeMoedas.getValue();
	}
	
	public int getQuanN�veis() {
		if (sistemaDePesquisa.isOption("Pesquisa de 3 N�veis"))
			return 3;
		else if (sistemaDePesquisa.isOption("Pesquisa de 10 N�veis"))
			return 10;
		else
			return 1;
	}

	public boolean hasMoral() {
		return hasMoral.getValue();
	}

	public boolean hasBandeira() {
		return hasBandeira.getValue();
	}

	public boolean hasBonusNoturno() {
		return hasBonusNoturno.getValue();
	}

	public BigDecimal getVelocidade() {
		return velocidade.getValue();
	}

	public BigDecimal getModificarUnidaes() {
		return modificarUnidades.getValue();
	}

	/**
	 * @param i
	 *            int n�vel do edif�cio
	 * @return porcentagem de tempo total de produ��o no n�vel dado
	 */
	public BigDecimal getPorcentagemDeProdu��o(int i) {
		return porcentagemDeProdu��o[i];
	}

	/**
	 * @return Array com todas as unidades presentes no mundo. Sempre com
	 *         tamanho 13, retorna null para a posi��o da unidade se ela n�o
	 *         estiver ligada no mundo.
	 */
	public Unidade[] getUnidades() {
		return unidades;
	}

	public boolean containsUnidade(Unidade unidade) {
		if (Arrays.asList(unidades).contains(unidade))
			return true;
		else
			return false;
	}

	/**
	 * @return N�mero de tropas dispon�veis no mundo.
	 */
	public int getN�meroDeTropas() {

		int i = 0;
		for (Unidade u : unidades)
			if (u != null)
				i++;

		return i;
	}

}
