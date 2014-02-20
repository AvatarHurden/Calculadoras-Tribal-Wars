package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import property_classes.Property;
import property_classes.Property_Boolean;
import property_classes.Property_Escolha;
import property_classes.Property_Number;

/**
 * Classe para a cria��o de diferentes mundos
 * 
 * @author Arthur
 */
public class Mundo {

	private String nome;

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

		this.nome = nome;

	}

	public Mundo() {
	}

	public void setAll(Properties prop) {

		nome = prop.getProperty("nome");

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

		sistemaDePesquisa = new Property_Escolha("Sistema de Pesquisa",
				prop.getProperty("pesquisaDeNiveis"), "Pesquisa Simples", 
				"Pesquisa de 3 N�veis");

		String speed = prop.getProperty("velocidade");
		speed = speed.replaceAll(",", ".");
		velocidade = new Property_Number("Velocidade", new BigDecimal(speed));

		String modifier = prop.getProperty("modificador");
		modifier = modifier.replaceAll(",", ".");
		modificarUnidades = new Property_Number("Modificador de Unidade",
				new BigDecimal(modifier));

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

		s += ("\tnome=" + nome + "\n");
		s += ("\tvelocidade=" + velocidade.toString() + "\n");
		s += ("\tmodificador=" + modificarUnidades.toString() + "\n");

		s += ("\tmoral=" + hasMoral + "\n");
		s += ("\tpesquisaDeNiveis=" + sistemaDePesquisa + "\n");
		s += ("\tigreja=" + hasIgreja + "\n");
		s += ("\tbonusNoturno=" + hasBonusNoturno + "\n");
		s += ("\tbandeira=" + hasBandeira + "\n");
		s += ("\tarqueiro=" + hasArqueiro + "\n");
		s += ("\tpaladino=" + hasPaladino + "\n");
		s += ("\titensAprimorados=" + hasItensAprimorados + "\n");
		s += ("\tmilicia=" + hasMil�cia + "\n");
		s += ("\tcunhagemDeMoedas=" + cunhagemDeMoedas + "\n");

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
		this.nome = nome;
	}

	// public void setHasMil�cia(boolean hasMil�cia) {
	// this.hasMil�cia = new property_classes.Boolean("Mil�cia", hasMil�cia);
	// }
	//
	// public void setHasPaladino(boolean hasPaladino) {
	// this.hasPaladino = hasPaladino;
	// }
	//
	// public void setHasItemnsprimorados(boolean hasItemAprimorado) {
	// this.hasItensAprimorados = hasItemAprimorado;
	// }
	//
	// public void setHasIgreja(boolean hasIgreja) {
	// this.hasIgreja = hasIgreja;
	// }
	//
	// public void setAcademiaDeN�veis(boolean academiaDeN�veis) {
	// this.academiaDeN�veis = academiaDeN�veis;
	// }
	//
	// public void setPesquisaDeN�veis(boolean pesquisaDeN�veis) {
	// this.pesquisaDeN�veis = pesquisaDeN�veis;
	// }
	//
	// public void setHasMoral(boolean hasMoral) {
	// this.hasMoral = hasMoral;
	// }
	//
	// public void setHasBandeira(boolean hasBandeira) {
	// this.hasBandeira = hasBandeira;
	// }
	//
	// public void setHasBonusNoturno(boolean hasBonusNoturno) {
	// this.hasBonusNoturno = hasBonusNoturno;
	// }
	//
	// public void setVelocidade(BigDecimal velocidade) {
	// this.velocidade = velocidade;
	// }
	//
	// public void setModificarUnidaes(BigDecimal modificarUnidaes) {
	// this.modificarUnidades = modificarUnidaes;
	// }

	public String toString() {
		return nome;
	}

	public String getNome() {
		return nome;
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
		return cunhagemDeMoedas.getValue();
	}

	public boolean isPesquisaDeN�veis() {
		return sistemaDePesquisa.isOption("Pesquisa de 3 N�veis");
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
	 * @param declared
	 *            variable name
	 * @return User-friendly variable name
	 */
	public static String getVariableName(String s) {

		switch (s) {
		case "nome":
			return "Nome";
		case "hasArqueiro":
			return "Arqueiros";
		case "hasMil�cia":
			return "Mil�cia";
		case "hasPaladino":
			return "Paladino";
		case "hasItensAprimorados":
			return "Itens Aprimorados";
		case "hasIgreja":
			return "Igreja";
		case "academiaDeN�veis":
			return "Cunhagem de Moedas";
		case "pesquisaDeN�veis":
			return "Pesquisa de 3 n�veis";
		case "hasMoral":
			return "Moral";
		case "hasBandeira":
			return "Bandeiras";
		case "hasBonusNoturno":
			return "B�nus Noturno";
		case "velocidade":
			return "Velocidade";
		case "modificarUnidades":
			return "Modificador de Unidade";
		default:
			return null;
		}

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
