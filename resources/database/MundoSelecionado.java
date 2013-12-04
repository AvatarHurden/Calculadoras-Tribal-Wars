package database;

import java.math.BigDecimal;
import java.util.Arrays;


/**
 * CLasse est�tica com as configura��es do mundo atualmente selecionado
 * 
 * @author Arthur
 */
public class MundoSelecionado {

	private static boolean hasArqueiro;
	private static boolean hasMil�cia;
	private static boolean hasPaladino;
	private static boolean hasItensAprimorados;
	private static boolean hasIgreja;
	private static boolean academiaDeN�veis;
	private static boolean pesquisaDeN�veis;
	private static boolean hasMoral;
	private static boolean hasBandeira;
	private static boolean hasBonusNoturno;
	private static BigDecimal velocidade;
	private static BigDecimal modificarUnidaes;
	
	private static Unidade[] unidades = new Unidade[13];
	
	private static BigDecimal[] porcentagemDeProdu��o = new BigDecimal[26];
	
	
	public static void setMundo(Mundo mundo) {
		
		MundoSelecionado.hasArqueiro = mundo.hasArqueiro();
		MundoSelecionado.hasMil�cia = mundo.hasMil�cia();
		MundoSelecionado.hasPaladino = mundo.hasPaladino();
		MundoSelecionado.hasItensAprimorados = mundo.hasItensAprimorados();
		MundoSelecionado.hasIgreja = mundo.hasIgreja();
		MundoSelecionado.academiaDeN�veis = mundo.isAcademiaDeN�veis();
		MundoSelecionado.pesquisaDeN�veis = mundo.isPesquisaDeN�veis();
		MundoSelecionado.hasMoral = mundo.hasMoral();
		MundoSelecionado.hasBandeira = mundo.hasBandeira();
		MundoSelecionado.hasBonusNoturno = mundo.hasBonusNoturno();
		MundoSelecionado.velocidade = mundo.getVelocidade();
		MundoSelecionado.modificarUnidaes = mundo.getModificarUnidaes();
		
		setTemposDeProdu��o();
		
		setUnidadeList();
	}
	
	/**
	 * @param boolean possui arqueiros
	 * @param boolean possui mil�cia
	 * @param booelan possui paladino
	 * @param boolean possui igreja
	 * @param boolean academia por armazenamento
	 * @param boolean ferreiro com n�veis de tropas
	 * @param boolean possui moral
	 * @param BigDecimal velocidade do mundo
	 * @param BigDecimal modificar de unidades
	 */
	public static void setDados(boolean hasArqueiro, boolean hasMil�cia, boolean hasPaladino, 
			boolean hasItensAprimorados, boolean hasIgreja, boolean academiaDeN�veis,
			boolean pesquisaDeN�veis, boolean hasMoral, boolean hasBandeira, boolean hasBonusNoturno,
			BigDecimal velocidade, BigDecimal modificarUnidaes) {
		MundoSelecionado.hasArqueiro = hasArqueiro;
		MundoSelecionado.hasMil�cia = hasMil�cia;
		MundoSelecionado.hasPaladino = hasPaladino;
		MundoSelecionado.hasItensAprimorados = hasItensAprimorados;
		MundoSelecionado.hasIgreja = hasIgreja;
		MundoSelecionado.academiaDeN�veis = academiaDeN�veis;
		MundoSelecionado.pesquisaDeN�veis = pesquisaDeN�veis;
		MundoSelecionado.hasMoral = hasMoral;
		MundoSelecionado.hasBandeira = hasBandeira;
		MundoSelecionado.hasBonusNoturno = hasBonusNoturno;
		MundoSelecionado.velocidade = velocidade;
		MundoSelecionado.modificarUnidaes = modificarUnidaes;
		
		setTemposDeProdu��o();
		
		setUnidadeList();
	}

	/**
	 * Define a diferen�a entre o tempo padr�o de produ��o de cada unidade e o tempo real, levando em considera��o o n�vel do edif�cio e a velocidade do mundo.
	 * 
	 * Tempo real = tempo padr�o * fator de tempo (n�vel do edif�cio) / velocidade do mundo
	 */
	private static void setTemposDeProdu��o() {
		
		// Os n�meros representam o tempo, em segundos, que leva para criar 10.000 lanceiros em cada n�vel do quartel
		// Esses valores ser�o dividos pelo tempo te�rico, no n�vel 0, dando assim a redu��o do tempo de produ��o causada
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
			porcentagemDeProdu��o[i] = 
			porcentagemDeProdu��o[i].divide(new BigDecimal(6800000), 30, BigDecimal.ROUND_HALF_EVEN);
		
		// Dividindo a redu��o de tempo pela velocidade do mundo, haver� o real tempo de produ��o para cada situa��o.
		
		for (int i = 0; i < 26; i++)
			porcentagemDeProdu��o[i] = 
			porcentagemDeProdu��o[i].divide(velocidade, 30, BigDecimal.ROUND_HALF_EVEN);

	}
	
	/**
	 * Cria uma lista com todas as unidades presentes no mundo
	 */
	private static void setUnidadeList() {
		
		unidades[0] = Unidade.LANCEIRO;
		unidades[1] = Unidade.ESPADACHIM;
		
		if (hasArqueiro)
			unidades[2] = Unidade.ARQUEIRO;
		else
			unidades[2] = null;
		
		unidades[3] = Unidade.B�RBARO;
		unidades[4] = Unidade.EXPLORADOR;
		unidades[5] = Unidade.CAVALOLEVE;
		
		if (hasArqueiro)
			unidades[6] = Unidade.ARCOCAVALO;
		else
			unidades[6] = null;
		
		unidades[7] = Unidade.CAVALOPESADO;
		unidades[8] = Unidade.AR�ETE;
		unidades[9] = Unidade.CATAPULTA;
		
		if (hasPaladino)
			unidades[10] = Unidade.PALADINO;
		
		unidades[11] = Unidade.NOBRE;
		
		if (hasMil�cia)
		unidades[12] = Unidade.MIL�CIA;
		
		
	}
	
	/**
	 * @return boolean possui arqueiros
	 */
	public static boolean hasArqueiro() {
		return hasArqueiro;
	}

	/**
	 * @return boolean possui mil�cia
	 */
	public static boolean hasMil�cia() {
		return hasMil�cia;
	}

	/**
	 * @return boolean possui paladino
	 */
	public static boolean hasPaladino() {
		return hasPaladino;
	}
	
	/**
	 * @return boolean possui itens aprimorados
	 */
	public static boolean hasItensAprimorados() {
		return hasItensAprimorados;
	}

	/**
	 * @return boolean possui igreja
	 */
	public static boolean hasIgreja() {
		return hasIgreja;
	}

	/**
	 * @return boolean academia de n�veis
	 */
	public static boolean isAcademiaDeN�veis() {
		return academiaDeN�veis;
	}

	/**
	 * @return boolean possui ferreiro de n�veis
	 */
	public static boolean isPesquisaDeN�veis() {
		return pesquisaDeN�veis;
	}
	
	/**
	 * @return boolean possui moral
	 */
	public static boolean hasMoral() {
		return hasMoral;
	}
	
	/**
	 * @return boolean possui bandeira
	 */
	public static boolean hasBandeira() {
		return hasBandeira;
	}
	
	/**
	 * @return boolean possui bandeira
	 */
	public static boolean hasBonusNoturno() {
		return hasBonusNoturno;
	}

	/**
	 * @return BigDecimal velocidade do mundo
	 */
	public static BigDecimal getVelocidade() {
		return velocidade;
	}

	/**
	 * @return BigDecimal modificador de movimento da velocidade
	 */
	public static BigDecimal getModificarUnidaes() {
		return modificarUnidaes;
	}
	
	/**
	 * @param i int n�vel do edif�cio
	 * @return porcentagem de tempo total de produ��o no n�vel dado
	 */
	public static BigDecimal getPorcentagemDeProdu��o(int i) {
		return porcentagemDeProdu��o[i];
	}
	
	/**
	 * @return Array com todas as unidades presentes no mundo. Sempre com tamanho 13,
	 *  retorna null para a posi��o da unidade se ela n�o estiver ligada no mundo.
	 */
	public static Unidade[] getUnidades() {
		return unidades;
	}
	
	public static boolean containsUnidade(Unidade unidade) {
		if (Arrays.asList(unidades).contains(unidade))
			return true;
		else
			return false;
	}
	
	/**
	 * @return N�mero de tropas dispon�veis no mundo.
	 */
	public static int getN�meroDeTropas(){
		
		int i = 0;
		for (Unidade u : unidades)
			if (u != null) 
				i++;
		
		return i;
	}
	
}
