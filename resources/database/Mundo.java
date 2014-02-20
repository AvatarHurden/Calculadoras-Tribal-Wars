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
 * Classe para a criação de diferentes mundos
 * 
 * @author Arthur
 */
public class Mundo {

	private String nome;

	private Property_Boolean hasArqueiro;
	private Property_Boolean hasMilícia;
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

	private BigDecimal[] porcentagemDeProdução = new BigDecimal[26];

	public Mundo(String nome) {

		this.nome = nome;

	}

	public Mundo() {
	}

	public void setAll(Properties prop) {

		nome = prop.getProperty("nome");

		hasArqueiro = new Property_Boolean("Arqueiro",
				Boolean.parseBoolean(prop.getProperty("arqueiro")));

		hasMilícia = new Property_Boolean("Milícia", Boolean.parseBoolean(prop
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

		hasBonusNoturno = new Property_Boolean("Bônus Noturno",
				Boolean.parseBoolean(prop.getProperty("bonusNoturno")));

		cunhagemDeMoedas = new Property_Boolean("Cunhagem de Moedas",
				Boolean.parseBoolean(prop.getProperty("cunhagemDeMoedas")));

		sistemaDePesquisa = new Property_Escolha("Sistema de Pesquisa",
				prop.getProperty("pesquisaDeNiveis"), "Pesquisa Simples", 
				"Pesquisa de 3 Níveis");

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
		variableList.add(hasMilícia);
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
		s += ("\tmilicia=" + hasMilícia + "\n");
		s += ("\tcunhagemDeMoedas=" + cunhagemDeMoedas + "\n");

		return s;

	}

	/**
	 * Define a diferença entre o tempo padrão de produção de cada unidade e o
	 * tempo real, levando em consideração o nível do edifício e a velocidade do
	 * mundo.
	 * 
	 * Tempo real = tempo padrão * fator de tempo (nível do edifício) /
	 * velocidade do mundo
	 */
	public void setTemposDeProdução() {

		// Os números representam o tempo, em segundos, que leva para criar
		// 10.000 lanceiros em cada nível do quartel
		// Esses valores serão dividos pelo tempo teórico, no nível 0, dando
		// assim a redução do tempo de produção causada
		// pelo nível do edificio.

		porcentagemDeProdução[0] = new BigDecimal(6800000);
		porcentagemDeProdução[1] = new BigDecimal(6414094);
		porcentagemDeProdução[2] = new BigDecimal(6051976);
		porcentagemDeProdução[3] = new BigDecimal(5709411);
		porcentagemDeProdução[4] = new BigDecimal(5386237);
		porcentagemDeProdução[5] = new BigDecimal(5081356);
		porcentagemDeProdução[6] = new BigDecimal(4793732);
		porcentagemDeProdução[7] = new BigDecimal(4522388);
		porcentagemDeProdução[8] = new BigDecimal(4266404);
		porcentagemDeProdução[9] = new BigDecimal(4024910);

		porcentagemDeProdução[10] = new BigDecimal(3797084);
		porcentagemDeProdução[11] = new BigDecimal(3582155);
		porcentagemDeProdução[12] = new BigDecimal(3379392);
		porcentagemDeProdução[13] = new BigDecimal(3188105);
		porcentagemDeProdução[14] = new BigDecimal(3007647);
		porcentagemDeProdução[15] = new BigDecimal(2837402);
		porcentagemDeProdução[16] = new BigDecimal(2676795);
		porcentagemDeProdução[17] = new BigDecimal(2525278);
		porcentagemDeProdução[18] = new BigDecimal(2382338);
		porcentagemDeProdução[19] = new BigDecimal(2247488);

		porcentagemDeProdução[20] = new BigDecimal(2120272);
		porcentagemDeProdução[21] = new BigDecimal(2000257);
		porcentagemDeProdução[22] = new BigDecimal(1887035);
		porcentagemDeProdução[23] = new BigDecimal(1780221);
		porcentagemDeProdução[24] = new BigDecimal(1679454);
		porcentagemDeProdução[25] = new BigDecimal(1584391);

		for (int i = 0; i < 26; i++)
			porcentagemDeProdução[i] = porcentagemDeProdução[i].divide(
					new BigDecimal(6800000), 30, BigDecimal.ROUND_HALF_EVEN);

		// Dividindo a redução de tempo pela velocidade do mundo, haverá o real
		// tempo de produção para cada situação.

		for (int i = 0; i < 26; i++)
			porcentagemDeProdução[i] = porcentagemDeProdução[i].divide(
					velocidade.getValue(), 30, BigDecimal.ROUND_HALF_EVEN);

	}

	/**
	 * Cria uma lista com todas as unidades presentes no mundo
	 */
	public void setUnidadeList() {

		unidades[0] = Unidade.LANCEIRO;
		unidades[1] = Unidade.ESPADACHIM;
		unidades[2] = Unidade.BÁRBARO;

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
		unidades[8] = Unidade.ARÍETE;
		unidades[9] = Unidade.CATAPULTA;

		if (hasPaladino.getValue())
			unidades[10] = Unidade.PALADINO;

		unidades[11] = Unidade.NOBRE;

		if (hasMilícia.getValue())
			unidades[12] = Unidade.MILÍCIA;

	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	// public void setHasMilícia(boolean hasMilícia) {
	// this.hasMilícia = new property_classes.Boolean("Milícia", hasMilícia);
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
	// public void setAcademiaDeNíveis(boolean academiaDeNíveis) {
	// this.academiaDeNíveis = academiaDeNíveis;
	// }
	//
	// public void setPesquisaDeNíveis(boolean pesquisaDeNíveis) {
	// this.pesquisaDeNíveis = pesquisaDeNíveis;
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

	public boolean hasMilícia() {
		return hasMilícia.getValue();
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

	public boolean isAcademiaDeNíveis() {
		return cunhagemDeMoedas.getValue();
	}

	public boolean isPesquisaDeNíveis() {
		return sistemaDePesquisa.isOption("Pesquisa de 3 Níveis");
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
	 *            int nível do edifício
	 * @return porcentagem de tempo total de produção no nível dado
	 */
	public BigDecimal getPorcentagemDeProdução(int i) {
		return porcentagemDeProdução[i];
	}

	/**
	 * @return Array com todas as unidades presentes no mundo. Sempre com
	 *         tamanho 13, retorna null para a posição da unidade se ela não
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
		case "hasMilícia":
			return "Milícia";
		case "hasPaladino":
			return "Paladino";
		case "hasItensAprimorados":
			return "Itens Aprimorados";
		case "hasIgreja":
			return "Igreja";
		case "academiaDeNíveis":
			return "Cunhagem de Moedas";
		case "pesquisaDeNíveis":
			return "Pesquisa de 3 níveis";
		case "hasMoral":
			return "Moral";
		case "hasBandeira":
			return "Bandeiras";
		case "hasBonusNoturno":
			return "Bônus Noturno";
		case "velocidade":
			return "Velocidade";
		case "modificarUnidades":
			return "Modificador de Unidade";
		default:
			return null;
		}

	}

	/**
	 * @return Número de tropas disponíveis no mundo.
	 */
	public int getNúmeroDeTropas() {

		int i = 0;
		for (Unidade u : unidades)
			if (u != null)
				i++;

		return i;
	}

}
