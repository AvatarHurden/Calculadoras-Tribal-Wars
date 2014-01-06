package database;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Properties;

/**
 * Classe para a cria��o de diferentes mundos
 * 
 * @author Arthur
 */
public class Mundo {

	private String nome;
	private boolean hasArqueiro;
	private boolean hasMil�cia;
	private boolean hasPaladino;
	private boolean hasItensAprimorados;
	private boolean hasIgreja;
	private boolean academiaDeN�veis;
	private boolean pesquisaDeN�veis;
	private boolean hasMoral;
	private boolean hasBandeira;
	private boolean hasBonusNoturno;
	private BigDecimal velocidade;
	private BigDecimal modificarUnidaes;
	
	private Unidade[] unidades = new Unidade[13];
	
	private BigDecimal[] porcentagemDeProdu��o = new BigDecimal[26];
	
	
	public Mundo(String nome) {
		
		this.nome = nome;
		
	}
	
	public Mundo() {}
	
	public void setAll(Properties prop) {
		
		nome = prop.getProperty("nome");
		
		hasArqueiro = Boolean.parseBoolean(prop.getProperty("arqueiro"));
		
		hasMil�cia = Boolean.parseBoolean(prop.getProperty("milicia"));
		
		hasPaladino = Boolean.parseBoolean(prop.getProperty("paladino"));
		
		hasItensAprimorados = Boolean.parseBoolean(prop.getProperty("itensAprimorados"));
		
		hasIgreja = Boolean.parseBoolean(prop.getProperty("igreja"));
		
		academiaDeN�veis = Boolean.parseBoolean(prop.getProperty("academiaDeNiveis"));
		
		pesquisaDeN�veis = Boolean.parseBoolean(prop.getProperty("pesquisaDeNiveis"));
		
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
		System.out.println("Mil�cia: "+hasMil�cia);
		System.out.println("Moral: "+hasMoral);
		System.out.println("Paladino: "+hasPaladino);
		System.out.println("Itens Aprimorados: "+hasItensAprimorados);
		System.out.println("Igreja: "+hasIgreja);
		System.out.println("Academia de N�veis: "+academiaDeN�veis);
		System.out.println("Pesquisa de N�veis: "+pesquisaDeN�veis);
		System.out.println("Bandeira: "+hasBandeira);
		System.out.println("B�nus Noturno: "+hasBonusNoturno);
		
	}
	
	/**
	 * Define a diferen�a entre o tempo padr�o de produ��o de cada unidade e o tempo real, levando em considera��o o n�vel do edif�cio e a velocidade do mundo.
	 * 
	 * Tempo real = tempo padr�o * fator de tempo (n�vel do edif�cio) / velocidade do mundo
	 */
	public void setTemposDeProdu��o() {

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
	public void setUnidadeList() {
		
		unidades[0] = Unidade.LANCEIRO;
		unidades[1] = Unidade.ESPADACHIM;
		unidades[2] = Unidade.B�RBARO;
		
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
		unidades[8] = Unidade.AR�ETE;
		unidades[9] = Unidade.CATAPULTA;
		
		if (hasPaladino)
			unidades[10] = Unidade.PALADINO;
		
		unidades[11] = Unidade.NOBRE;
		
		if (hasMil�cia)
		unidades[12] = Unidade.MIL�CIA;
		
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
	 * @param boolean possui mil�cia
	 */
	public void setHasMil�cia(boolean hasMil�cia) {
		this.hasMil�cia = hasMil�cia;
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
	 * @param boolean possui academia de n�veis (armazenamento)
	 */
	public void setAcademiaDeN�veis(boolean academiaDeN�veis) {
		this.academiaDeN�veis = academiaDeN�veis;
	}
	
	public void setPesquisaDeN�veis(boolean pesquisaDeN�veis) {
		this.pesquisaDeN�veis = pesquisaDeN�veis;
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
	 * @param boolean possui b�nus noturno
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

	public boolean hasMil�cia() {
		return hasMil�cia;
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

	public boolean isAcademiaDeN�veis() {
		return academiaDeN�veis;
	}

	public boolean isPesquisaDeN�veis() {
		return pesquisaDeN�veis;
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
	 * @param i int n�vel do edif�cio
	 * @return porcentagem de tempo total de produ��o no n�vel dado
	 */
	public BigDecimal getPorcentagemDeProdu��o(int i) {
		return porcentagemDeProdu��o[i];
	}

	/**
	 * @return Array com todas as unidades presentes no mundo. Sempre com tamanho 13,
	 *  retorna null para a posi��o da unidade se ela n�o estiver ligada no mundo.
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
	public int getN�meroDeTropas(){
		
		int i = 0;
		for (Unidade u : unidades)
			if (u != null) 
				i++;
		
		return i;
	}
	

	
}
