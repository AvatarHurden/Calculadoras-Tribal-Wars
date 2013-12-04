package database;

import java.math.BigDecimal;
import java.util.Arrays;


/**
 * CLasse estática com as configurações do mundo atualmente selecionado
 * 
 * @author Arthur
 */
public class MundoSelecionado {

	private static boolean hasArqueiro;
	private static boolean hasMilícia;
	private static boolean hasPaladino;
	private static boolean hasItensAprimorados;
	private static boolean hasIgreja;
	private static boolean academiaDeNíveis;
	private static boolean pesquisaDeNíveis;
	private static boolean hasMoral;
	private static boolean hasBandeira;
	private static boolean hasBonusNoturno;
	private static BigDecimal velocidade;
	private static BigDecimal modificarUnidaes;
	
	private static Unidade[] unidades = new Unidade[13];
	
	private static BigDecimal[] porcentagemDeProdução = new BigDecimal[26];
	
	
	public static void setMundo(Mundo mundo) {
		
		MundoSelecionado.hasArqueiro = mundo.hasArqueiro();
		MundoSelecionado.hasMilícia = mundo.hasMilícia();
		MundoSelecionado.hasPaladino = mundo.hasPaladino();
		MundoSelecionado.hasItensAprimorados = mundo.hasItensAprimorados();
		MundoSelecionado.hasIgreja = mundo.hasIgreja();
		MundoSelecionado.academiaDeNíveis = mundo.isAcademiaDeNíveis();
		MundoSelecionado.pesquisaDeNíveis = mundo.isPesquisaDeNíveis();
		MundoSelecionado.hasMoral = mundo.hasMoral();
		MundoSelecionado.hasBandeira = mundo.hasBandeira();
		MundoSelecionado.hasBonusNoturno = mundo.hasBonusNoturno();
		MundoSelecionado.velocidade = mundo.getVelocidade();
		MundoSelecionado.modificarUnidaes = mundo.getModificarUnidaes();
		
		setTemposDeProdução();
		
		setUnidadeList();
	}
	
	/**
	 * @param boolean possui arqueiros
	 * @param boolean possui milícia
	 * @param booelan possui paladino
	 * @param boolean possui igreja
	 * @param boolean academia por armazenamento
	 * @param boolean ferreiro com níveis de tropas
	 * @param boolean possui moral
	 * @param BigDecimal velocidade do mundo
	 * @param BigDecimal modificar de unidades
	 */
	public static void setDados(boolean hasArqueiro, boolean hasMilícia, boolean hasPaladino, 
			boolean hasItensAprimorados, boolean hasIgreja, boolean academiaDeNíveis,
			boolean pesquisaDeNíveis, boolean hasMoral, boolean hasBandeira, boolean hasBonusNoturno,
			BigDecimal velocidade, BigDecimal modificarUnidaes) {
		MundoSelecionado.hasArqueiro = hasArqueiro;
		MundoSelecionado.hasMilícia = hasMilícia;
		MundoSelecionado.hasPaladino = hasPaladino;
		MundoSelecionado.hasItensAprimorados = hasItensAprimorados;
		MundoSelecionado.hasIgreja = hasIgreja;
		MundoSelecionado.academiaDeNíveis = academiaDeNíveis;
		MundoSelecionado.pesquisaDeNíveis = pesquisaDeNíveis;
		MundoSelecionado.hasMoral = hasMoral;
		MundoSelecionado.hasBandeira = hasBandeira;
		MundoSelecionado.hasBonusNoturno = hasBonusNoturno;
		MundoSelecionado.velocidade = velocidade;
		MundoSelecionado.modificarUnidaes = modificarUnidaes;
		
		setTemposDeProdução();
		
		setUnidadeList();
	}

	/**
	 * Define a diferença entre o tempo padrão de produção de cada unidade e o tempo real, levando em consideração o nível do edifício e a velocidade do mundo.
	 * 
	 * Tempo real = tempo padrão * fator de tempo (nível do edifício) / velocidade do mundo
	 */
	private static void setTemposDeProdução() {
		
		// Os números representam o tempo, em segundos, que leva para criar 10.000 lanceiros em cada nível do quartel
		// Esses valores serão dividos pelo tempo teórico, no nível 0, dando assim a redução do tempo de produção causada
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
			porcentagemDeProdução[i] = 
			porcentagemDeProdução[i].divide(new BigDecimal(6800000), 30, BigDecimal.ROUND_HALF_EVEN);
		
		// Dividindo a redução de tempo pela velocidade do mundo, haverá o real tempo de produção para cada situação.
		
		for (int i = 0; i < 26; i++)
			porcentagemDeProdução[i] = 
			porcentagemDeProdução[i].divide(velocidade, 30, BigDecimal.ROUND_HALF_EVEN);

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
		
		unidades[3] = Unidade.BÁRBARO;
		unidades[4] = Unidade.EXPLORADOR;
		unidades[5] = Unidade.CAVALOLEVE;
		
		if (hasArqueiro)
			unidades[6] = Unidade.ARCOCAVALO;
		else
			unidades[6] = null;
		
		unidades[7] = Unidade.CAVALOPESADO;
		unidades[8] = Unidade.ARÍETE;
		unidades[9] = Unidade.CATAPULTA;
		
		if (hasPaladino)
			unidades[10] = Unidade.PALADINO;
		
		unidades[11] = Unidade.NOBRE;
		
		if (hasMilícia)
		unidades[12] = Unidade.MILÍCIA;
		
		
	}
	
	/**
	 * @return boolean possui arqueiros
	 */
	public static boolean hasArqueiro() {
		return hasArqueiro;
	}

	/**
	 * @return boolean possui milícia
	 */
	public static boolean hasMilícia() {
		return hasMilícia;
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
	 * @return boolean academia de níveis
	 */
	public static boolean isAcademiaDeNíveis() {
		return academiaDeNíveis;
	}

	/**
	 * @return boolean possui ferreiro de níveis
	 */
	public static boolean isPesquisaDeNíveis() {
		return pesquisaDeNíveis;
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
	 * @param i int nível do edifício
	 * @return porcentagem de tempo total de produção no nível dado
	 */
	public static BigDecimal getPorcentagemDeProdução(int i) {
		return porcentagemDeProdução[i];
	}
	
	/**
	 * @return Array com todas as unidades presentes no mundo. Sempre com tamanho 13,
	 *  retorna null para a posição da unidade se ela não estiver ligada no mundo.
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
	 * @return Número de tropas disponíveis no mundo.
	 */
	public static int getNúmeroDeTropas(){
		
		int i = 0;
		for (Unidade u : unidades)
			if (u != null) 
				i++;
		
		return i;
	}
	
}
