package database;

import java.math.BigDecimal;
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


	
}
