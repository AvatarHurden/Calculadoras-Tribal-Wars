package database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public enum Unidade{


	//                Nome				   tipo            mad	  arg    fer   pop   atk   dg    dc    da    mov   saq  tempo
	
	LANCEIRO	  ("Lanceiro", 			Type.Geral,        50,    30,	  10,	 1,	  10,   15,   45,   20,	  18,   25,  680),
	ESPADACHIM	  ("Espadachim",		Type.Geral,        30,    30,	  70,	 1,	  25,   50,   15,   40,	  22,   15,  1000),
	ARQUEIRO	  ("Arqueiro",			Type.Arqueiro,     100,   30,	  60,	 1,	  15,   50,   40,   5, 	  18,   10,  1200),
	BÁRBARO		  ("Bárbaro", 			Type.Geral,        60,    30,	  40,	 1,	  40,   10,   5,    10,	  18,   10,  880),
	EXPLORADOR	  ("Explorador",		Type.unspecified,  50,    50,	  20,	 2,	  0,	2,    1,    2,    9,    0,   600),
	CAVALOLEVE	  ("Cavalaria Leve",	Type.Cavalo,       125,   100,	  250,	 4,	  130,  30,   40,   30,   10,   80,  1200),
	ARCOCAVALO	  ("Arqueiro a Cavalo", Type.Arqueiro,     250,   100,	  150,	 5,	  120,  40,   30,   50,   10,   50,  1800),
	CAVALOPESADO  ("Cavalaria Pesada",  Type.Cavalo,       200,   150,	  600,	 6,	  150,  200,  80,   180,  11,   50,  2400),
	ARÍETE		  ("Aríete", 			Type.Geral,        300,   200,	  200,	 5,	  2,	20,   50,   20,   30,   0,   3200),
	CATAPULTA	  ("Catapulta", 		Type.Geral,        320,   400,	  100,	 8,	  100,  100,  50,   100,  30,   0,   4800),
	PALADINO	  ("Paladino", 			Type.Cavalo,       20,    20,	  40,	 10,  150,  250,  400,  150,  10,   100, 21600),
	NOBRE         ("Nobre", 		    Type.Geral,        40000, 50000,  50000, 100, 30,   100,  50,   100,  35,   0,   12000),
	MILÍCIA		  ("Milícia", 			Type.unspecified,  0,     0,	  0,	 0,	  0,	15,   45,   25,   0.02, 0,   0);
	
	private final static BigDecimal lvl2 = new BigDecimal("1.25");
	private final static BigDecimal lvl3 = new BigDecimal("1.4");
	
	private final String nome;
	private final Type type;
	private final BigDecimal madeira;
	private final BigDecimal argila;
	private final BigDecimal ferro;
	private final BigDecimal população;
	private final BigDecimal ataque;
	private final BigDecimal defGeral;
	private final BigDecimal defCav;
	private final BigDecimal defArq;
	private final BigDecimal velocidade;
	private final BigDecimal saque;
	private final BigDecimal tempoProdução;

	public enum Type {
		Geral, Arqueiro, Cavalo, unspecified;
	}
	
	/**
	 * @param nome da unidade
	 * @param custo de madeira
	 * @param custo de argila
	 * @param custo de ferro
	 * @param população utilizada
	 * @param força de ataque
	 * @param defesa geral
	 * @param defesa cavalaria
	 * @param defesa arqueiros
	 * @param velocidade base
	 * @param saque
	 * @param tempo de produção em segundos (100%)
	 */
	private Unidade(String nome, Type tipo, int madeira, int argila, int ferro, int população,
			int ataque, int defGeral, int defCav, int defArq, double velocidade,
			int saque, int tempo) {
		this.nome = nome;
		this.type = tipo;
		this.madeira = new BigDecimal(String.valueOf(madeira));
		this.argila = new BigDecimal(String.valueOf(argila));
		this.ferro = new BigDecimal(String.valueOf(ferro));
		this.população = new BigDecimal(String.valueOf(população));
		this.ataque = new BigDecimal(String.valueOf(ataque));
		this.defGeral = new BigDecimal(String.valueOf(defGeral));
		// Def cavalo
		this.defArq = new BigDecimal(String.valueOf(defArq));
		this.velocidade = new BigDecimal(String.valueOf(velocidade));
		this.saque = new BigDecimal(String.valueOf(saque));
		tempoProdução = new BigDecimal(String.valueOf(tempo));
		
		// Alterações para unidades específicas
		// As características flutuantes são adicionadas depois, visto que são final
		
		if (!MundoSelecionado.hasArqueiro() && nome.equals("Espadachim"))
			this.defCav = new BigDecimal("25");
		else	
			this.defCav = new BigDecimal(String.valueOf(defCav));
	}

	/**
	 * @return tipo da unidade, na classe "Type"
	 */
	public Type type() { return type;}
	
	/**
	 * @return custo de madeira da unidade
	 */
	public BigDecimal madeira() { return madeira; }

	/**
	 * @return custo de argila da unidade
	 */
	public BigDecimal argila() { return argila; }

	/**
	 * @return custo de ferro da unidade
	 */
	public BigDecimal ferro() { return ferro; }

	/**
	 * @return custo de população da unidade
	 */
	public BigDecimal população() { return população; }	

	/**
	 * @return força de ataque da unidade
	 */
	public BigDecimal ataque() { return ataque; }

	public BigDecimal ataque(int nível) { return upgrade( nível, ataque ); }
	
	public BigDecimal ataque(int nível, ItemPaladino item) {
		
		if (item.getUnit() == this )
			return upgrade( nível, ataque ).multiply(new BigDecimal(item.getModifierAtk()));
		else
			return upgrade( nível, ataque );
		
		}
	
	/**
	 * @return defesa geral da unidade
	 */
	public BigDecimal defGeral() { return defGeral; }

	public BigDecimal defGeral(int nível) { return upgrade( nível, defGeral ); }
	
	public BigDecimal defGeral(int nível, ItemPaladino item) {
		
		if (item.getUnit() == this )
			return upgrade( nível, defGeral ).multiply(new BigDecimal(item.getModifierDef())); 
		else
			return upgrade( nível, defGeral );
		
		}
	
	/**
	 * @return defesa de cavalaria da unidade
	 */
	public BigDecimal defCav() { return defCav; }

	public BigDecimal defCav(int nível) { return upgrade( nível, defCav ); }
	
	public BigDecimal defCav(int nível, ItemPaladino item) { 
		
		if (item.getUnit() == this )
			return upgrade( nível, defCav ).multiply(new BigDecimal(item.getModifierDef())); 
		else
			return upgrade( nível, defCav );
					
		}
	
	/**
	 * @return defesa de arqueiro da unidade
	 */
	public BigDecimal defArq() { return defArq; }

	public BigDecimal defArq(int nível) { return upgrade( nível, defArq ); }
	
	public BigDecimal defArq(int nível, ItemPaladino item) { 
		
		if (item.getUnit() == this ){
			return upgrade( nível, defArq ).multiply(new BigDecimal(item.getModifierDef()));
		}
		else
			return upgrade( nível, defArq );
					
		}
	
	/**
	 * @return velocidade base da unidade
	 */
	public BigDecimal velocidade() { return velocidade; }

	/**
	 * @return capacidade de saque da unidade
	 */
	public BigDecimal saque() { return saque; }
	
	/**
	 * @return tempo de produção da unidade em segundos (100%)
	 */
	public BigDecimal tempoDeProdução() { return tempoProdução; }
	
	/**
	 * @return nome da unidade, com primeira letra da cada palavra maiúscula
	 */
	public String nome() { return nome;}
	
	/**
	 * Aumenta a quantidade dada pela razão do nível fornecido. 
	 * Se o nível for diferente de 2 ou 3, retorna o valor dado
	 */
	public BigDecimal upgrade(int lvl, BigDecimal value) {
		
		BigDecimal total = new BigDecimal(value.toString());
		
		if (lvl == 2)
			total = total.multiply(lvl2);
		else if (lvl == 3)
			total = total.multiply(lvl3);
		else
			return value;
		
		return total.setScale(0,RoundingMode.HALF_UP);

	}
	
}
