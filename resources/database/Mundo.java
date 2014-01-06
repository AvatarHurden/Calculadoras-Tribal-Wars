package database;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Properties;

/**
 * Classe para a criação de diferentes mundos
 * 
 * @author Arthur
 */
public class Mundo {

	private String nome;
	private boolean hasArqueiro;
	private boolean hasMilícia;
	private boolean hasPaladino;
	private boolean hasItensAprimorados;
	private boolean hasIgreja;
	private boolean academiaDeNíveis;
	private boolean pesquisaDeNíveis;
	private boolean hasMoral;
	private boolean hasBandeira;
	private boolean hasBonusNoturno;
	private BigDecimal velocidade;
	private BigDecimal modificarUnidaes;
	
	private Unidade[] unidades = new Unidade[13];
	
	private BigDecimal[] porcentagemDeProdução = new BigDecimal[26];
	
	
	public Mundo(String nome) {
		
		this.nome = nome;
		
	}
	
	public Mundo() {}
	
	public void setAll(Properties prop) {
		
		nome = prop.getProperty("nome");
		
		hasArqueiro = Boolean.parseBoolean(prop.getProperty("arqueiro"));
		
		hasMilícia = Boolean.parseBoolean(prop.getProperty("milicia"));
		
		hasPaladino = Boolean.parseBoolean(prop.getProperty("paladino"));
		
		hasItensAprimorados = Boolean.parseBoolean(prop.getProperty("itensAprimorados"));
		
		hasIgreja = Boolean.parseBoolean(prop.getProperty("igreja"));
		
		academiaDeNíveis = Boolean.parseBoolean(prop.getProperty("academiaDeNiveis"));
		
		pesquisaDeNíveis = Boolean.parseBoolean(prop.getProperty("pesquisaDeNiveis"));
		
		hasMoral = Boolean.parseBoolean(prop.getProperty("moral"));
		
		hasBandeira = Boolean.parseBoolean(prop.getProperty("bandeira"));
		
		hasBonusNoturno = Boolean.parseBoolean(prop.getProperty("bonusNoturno"));
		
		String speed = prop.getProperty("velocidade");
		speed = speed.replaceAll(",", ".");
		velocidade = new BigDecimal(speed);
		
		String modifier = prop.getProperty("modificador");
		modifier = modifier.replaceAll(",", ".");
		modificarUnidaes = new BigDecimal(modifier);
		
	}
	
	public void printAll() {
		
		System.out.println("Nome: "+nome);
		System.out.println("Velocidade: "+velocidade.toString());
		System.out.println("Modificador: "+modificarUnidaes.toString());
		
		System.out.println("Arqueiros: "+hasArqueiro);
		System.out.println("Milícia: "+hasMilícia);
		System.out.println("Moral: "+hasMoral);
		System.out.println("Paladino: "+hasPaladino);
		System.out.println("Itens Aprimorados: "+hasItensAprimorados);
		System.out.println("Igreja: "+hasIgreja);
		System.out.println("Academia de Níveis: "+academiaDeNíveis);
		System.out.println("Pesquisa de Níveis: "+pesquisaDeNíveis);
		System.out.println("Bandeira: "+hasBandeira);
		System.out.println("Bônus Noturno: "+hasBonusNoturno);
		
	}
	
	/**
	 * Define a diferença entre o tempo padrão de produção de cada unidade e o tempo real, levando em consideração o nível do edifício e a velocidade do mundo.
	 * 
	 * Tempo real = tempo padrão * fator de tempo (nível do edifício) / velocidade do mundo
	 */
	public void setTemposDeProdução() {

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
	public void setUnidadeList() {
		
		unidades[0] = Unidade.LANCEIRO;
		unidades[1] = Unidade.ESPADACHIM;
		unidades[2] = Unidade.BÁRBARO;
		
		if (hasArqueiro)
			unidades[3] = Unidade.ARQUEIRO;
		else
			unidades[3] = null;
		
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
	
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * @param boolean possui arqueiros
	 */
	public void setHasArqueiro(boolean hasArqueiro) {
		this.hasArqueiro = hasArqueiro;
	}
	/**
	 * @param boolean possui milícia
	 */
	public void setHasMilícia(boolean hasMilícia) {
		this.hasMilícia = hasMilícia;
	}
	/**
	 * @param boolean possui paladino
	 */
	public void setHasPaladino(boolean hasPaladino) {
		this.hasPaladino = hasPaladino;
	}
	/**
	 * @param boolean possui itens aprimorados
	 */
	public void setHasItemnsprimorados(boolean hasItemAprimorado) {
		this.hasItensAprimorados = hasItemAprimorado;
	}
	/**
	 * @param  boolean possui igreja
	 */
	public void setHasIgreja(boolean hasIgreja) {
		this.hasIgreja = hasIgreja;
	}
	/**
	 * @param boolean possui academia de níveis (armazenamento)
	 */
	public void setAcademiaDeNíveis(boolean academiaDeNíveis) {
		this.academiaDeNíveis = academiaDeNíveis;
	}
	
	public void setPesquisaDeNíveis(boolean pesquisaDeNíveis) {
		this.pesquisaDeNíveis = pesquisaDeNíveis;
	}

	/**
	 * @param boolean possui moral
	 */
	public void setHasMoral(boolean hasMoral) {
		this.hasMoral = hasMoral;
	}
	
	/**
	 * @param boolean possui bandeira
	 */
	public void setHasBandeira(boolean hasBandeira) {
		this.hasBandeira = hasBandeira;
	}
	
	/**
	 * @param boolean possui bônus noturno
	 */
	public void setHasBonusNoturno(boolean hasBonusNoturno) {
		this.hasBonusNoturno = hasBonusNoturno;
	}
	
	/**
	 * @param BigDecimal velocidade do mundo
	 */
	public void setVelocidade(BigDecimal velocidade) {
		this.velocidade = velocidade;
	}
	/**
	 * @param BigDecimal modificador de unidades do mundo
	 */
	public void setModificarUnidaes(BigDecimal modificarUnidaes) {
		this.modificarUnidaes = modificarUnidaes;
	}

	public String getNome() {
		return nome;
	}

	public boolean hasArqueiro() {
		return hasArqueiro;
	}

	public boolean hasMilícia() {
		return hasMilícia;
	}

	public boolean hasPaladino() {
		return hasPaladino;
	}
	
	public boolean hasItensAprimorados() {
		return hasItensAprimorados;
	}

	public boolean hasIgreja() {
		return hasIgreja;
	}

	public boolean isAcademiaDeNíveis() {
		return academiaDeNíveis;
	}

	public boolean isPesquisaDeNíveis() {
		return pesquisaDeNíveis;
	}

	public boolean hasMoral() {
		return hasMoral;
	}
	
	public boolean hasBandeira() {
		return hasBandeira;
	}
	
	public boolean hasBonusNoturno() {
		return hasBonusNoturno;
	}

	public BigDecimal getVelocidade() {
		return velocidade;
	}

	public BigDecimal getModificarUnidaes() {
		return modificarUnidaes;
	}
	
	/**
	 * @param i int nível do edifício
	 * @return porcentagem de tempo total de produção no nível dado
	 */
	public BigDecimal getPorcentagemDeProdução(int i) {
		return porcentagemDeProdução[i];
	}

	/**
	 * @return Array com todas as unidades presentes no mundo. Sempre com tamanho 13,
	 *  retorna null para a posição da unidade se ela não estiver ligada no mundo.
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
	 * @return Número de tropas disponíveis no mundo.
	 */
	public int getNúmeroDeTropas(){
		
		int i = 0;
		for (Unidade u : unidades)
			if (u != null) 
				i++;
		
		return i;
	}
	

	
}
