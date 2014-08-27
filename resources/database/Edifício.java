package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Edif�cio {

	//						Nome		     n�velMax ft.Pop pop  pontos    mad     arg     fer
	EDIF�CIO_PRINCIPAL  ("Edif�cio Principal",  30,  1.17,   5, 	10,		90,		80, 	70),
	ARMAZ�M				("Armaz�m", 			30,  1.15,   0,     6,		60,		50,		40),
	QUARTEL			    ("Quartel",			   	25,  1.17,   7, 	16,		200,	170,	90,
		new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 3)),
	FERREIRO			("Ferreiro", 			20,	 1.17,   20,	19,		220,	180,	240,
		new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 5), new Requirement(Edif�cio.QUARTEL, 1)),
	EST�BULO			("Est�bulo", 			20,	 1.17,   8,	  	20,		270,	240,	260,
		new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 10), new Requirement(Edif�cio.FERREIRO, 5), new Requirement(Edif�cio.QUARTEL, 5)),
	OFICINA				("Oficina", 			15,	 1.17,   8,	  	24,		300,	240,	260,
		new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 10), new Requirement(Edif�cio.FERREIRO, 10)),
	MERCADO				("Mercado", 			25,	 1.17,   20,	10,		100,	100,	100,
		new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 3), new Requirement(Edif�cio.ARMAZ�M, 2)),
	ACADEMIA_3N�VEIS	("Academia",			3,	 1.17,   80,	512,	15000,	25000,	10000,
		new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 20), new Requirement(Edif�cio.FERREIRO, 20), new Requirement(Edif�cio.MERCADO, 10)),
	ACADEMIA_1N�VEL		("Academia",			1,	 1.17,   80,	512,	15000,	25000,	10000,
			new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 20), new Requirement(Edif�cio.FERREIRO, 20), new Requirement(Edif�cio.MERCADO, 10)),
	PRA�A_DE_REUNI�O	("Pra�a de Reuni�o", 	1,	 1.17,   0,		0,		10,		40,		30),
	EST�TUA				("Est�tua", 			1,	 1.17,   10,	24,		220,	220,	220),
	BOSQUE				("Bosque", 				30,	 1.155,  5,	  	6,		50,		60,		40),
	PO�O_DE_ARGILA		("Po�o de Argila", 		30,	 1.14,   10,	6,		65,		50,		40),
	MINA_DE_FERRO		("Mina de Ferro", 		30,	 1.17,   10,	6,		75,		65,		70),	
	FAZENDA				("Fazenda", 			30,	 1.17,   240,   5,		45,		40,		30),
	ESCONDERIJO			("Esconderijo", 		10,	 1.17,   2,	  	5,		50,		60,		50),
	MURALHA				("Muralha", 			20,	 1.17,   5,	  	8,		50,		100,	20,
			new Requirement(Edif�cio.QUARTEL, 1)),
	IGREJA				("Igreja", 				3,	 1.17,   5000,  10,		16000,	20000,	5000,
			new Requirement(Edif�cio.EDIF�CIO_PRINCIPAL, 5), new Requirement(Edif�cio.FAZENDA, 5)),
	PRIMEIRA_IGREJA		("Primeira Igreja", 	1,	 1.17,   5,	  	10,		160,	200,	50);
	
	private final String nome;
	private final int popula��oInicial;
	private final int pontosInicial;
	private final int n�velM�ximo;
	private final int custoMadeira;
	private final int custoArgila;
	private final int custoFerro;
	
	private final List<Requirement> requisitos;

	private final double raz�oPontos = 1.2;
	private final double raz�oMadeira = 1.26;
	private final double raz�oArgila = 1.275;
	private final double raz�oFerro = 1.26;
	private final double raz�oPopula��o;
	
	/**
	 * B�nus de porcentagem de defesa da muralha
	 */
	private final BigDecimal[] b�nus1 = new BigDecimal[21];

	/**
	 * B�nus flat de defesa da muralha
	 */
	private final BigDecimal[] b�nus2 = new BigDecimal[21];
	
	private Edif�cio(String nome, int n�velM�ximo, double raz�oPopula��o,
			int popula��oInicial, int pontosInicial, int custoMadeira,
			int custoArgila, int custoFerro, Requirement... requisitos) {
		this.nome = nome;
		this.n�velM�ximo = n�velM�ximo;
		this.raz�oPopula��o = raz�oPopula��o;
		this.pontosInicial = pontosInicial;
		this.popula��oInicial = popula��oInicial;
		this.custoMadeira = custoMadeira;
		this.custoArgila = custoArgila;
		this.custoFerro = custoFerro;
		
		this.requisitos = new ArrayList<Requirement>(Arrays.asList(requisitos));

		if (nome.equals("Muralha"))
			setB�nusDeMuralha();

	}

	/**
	 * Coloca os valores para o b�nus da muralha
	 */
	private void setB�nusDeMuralha() {

		for (int i = 0; i < 21; i++)
			b�nus1[i] = new BigDecimal("1.037").pow(i);

		for (int i = 0; i < 21; i++)
			b�nus2[i] = new BigDecimal(String.valueOf(20 + 50 * i));

	}


	public int getPontos(int n�vel) {
		if (n�vel == 0)
			return 0;
		else
			return (int) Math.round(pontosInicial 
				* Math.pow(raz�oPontos, n�vel-1));
	}

	public int getPopula��o(int n�vel) {
		if (n�vel == 0)
			return 0;
		else
			return (int) Math.round(popula��oInicial 
				* Math.pow(raz�oPopula��o, n�vel-1));
	}
	
	/**
	 * Retorna o custo de argila para expandir o edif�cio para o n�vel passado
	 * como par�metro, assumindo que ele esteja no n�vel anterior;
	 * 
	 * @param n�vel
	 * @return custo de argila
	 */
	public int getCustoArgila(int n�vel) {
		return (int) Math.round(custoArgila 
				* Math.pow(raz�oArgila, n�vel-1));
	}

	/**
	 * Retorna o custo de madeira para expandir o edif�cio para o n�vel passado
	 * como par�metro, assumindo que ele esteja no n�vel anterior;
	 * 
	 * @param n�vel
	 * @return custo de madeira
	 */
	public int getCustoMadeira(int n�vel) {
		return (int) Math.round(custoMadeira 
				* Math.pow(raz�oMadeira, n�vel-1));
	}
	
	/**
	 * Retorna o custo de ferro para expandir o edif�cio para o n�vel passado
	 * como par�metro, assumindo que ele esteja no n�vel anterior;
	 * 
	 * @param n�vel
	 * @return custo de ferro
	 */
	public int getCustoFerro(int n�vel) {
		return (int) Math.round(custoFerro 
				* Math.pow(raz�oFerro, n�vel-1));
	}
	
	public List<Requirement> getRequisitos() {
		return requisitos;
	}
	
	public String toString() {
		return nome;
	}

	public int getN�velM�ximo() {
		return n�velM�ximo;
	}

	public BigDecimal b�nusPercentual(int n�vel) {
		return b�nus1[n�vel];
	}

	public BigDecimal b�nusFlat(int n�vel) {
		return b�nus2[n�vel];
	}
	
	private static class Requirement {
		
		Edif�cio ed;
		int n�vel;
		
		private Requirement(Edif�cio ed, int n�vel) {
			this.ed = ed;
			this.n�vel = n�vel;
		}
		
		public Edif�cio getEdif�cio() {
			return ed;
		}
		
		public int getN�vel() {
			return n�vel;
		}
		
	}
}
