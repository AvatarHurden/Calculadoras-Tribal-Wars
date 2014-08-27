package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Edifício {

	//						Nome		     nívelMax ft.Pop pop  pontos    mad     arg     fer
	EDIFÍCIO_PRINCIPAL  ("Edifício Principal",  30,  1.17,   5, 	10,		90,		80, 	70),
	ARMAZÉM				("Armazém", 			30,  1.15,   0,     6,		60,		50,		40),
	QUARTEL			    ("Quartel",			   	25,  1.17,   7, 	16,		200,	170,	90,
		new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 3)),
	FERREIRO			("Ferreiro", 			20,	 1.17,   20,	19,		220,	180,	240,
		new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 5), new Requirement(Edifício.QUARTEL, 1)),
	ESTÁBULO			("Estábulo", 			20,	 1.17,   8,	  	20,		270,	240,	260,
		new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 10), new Requirement(Edifício.FERREIRO, 5), new Requirement(Edifício.QUARTEL, 5)),
	OFICINA				("Oficina", 			15,	 1.17,   8,	  	24,		300,	240,	260,
		new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 10), new Requirement(Edifício.FERREIRO, 10)),
	MERCADO				("Mercado", 			25,	 1.17,   20,	10,		100,	100,	100,
		new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 3), new Requirement(Edifício.ARMAZÉM, 2)),
	ACADEMIA_3NÍVEIS	("Academia",			3,	 1.17,   80,	512,	15000,	25000,	10000,
		new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 20), new Requirement(Edifício.FERREIRO, 20), new Requirement(Edifício.MERCADO, 10)),
	ACADEMIA_1NÍVEL		("Academia",			1,	 1.17,   80,	512,	15000,	25000,	10000,
			new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 20), new Requirement(Edifício.FERREIRO, 20), new Requirement(Edifício.MERCADO, 10)),
	PRAÇA_DE_REUNIÃO	("Praça de Reunião", 	1,	 1.17,   0,		0,		10,		40,		30),
	ESTÁTUA				("Estátua", 			1,	 1.17,   10,	24,		220,	220,	220),
	BOSQUE				("Bosque", 				30,	 1.155,  5,	  	6,		50,		60,		40),
	POÇO_DE_ARGILA		("Poço de Argila", 		30,	 1.14,   10,	6,		65,		50,		40),
	MINA_DE_FERRO		("Mina de Ferro", 		30,	 1.17,   10,	6,		75,		65,		70),	
	FAZENDA				("Fazenda", 			30,	 1.17,   240,   5,		45,		40,		30),
	ESCONDERIJO			("Esconderijo", 		10,	 1.17,   2,	  	5,		50,		60,		50),
	MURALHA				("Muralha", 			20,	 1.17,   5,	  	8,		50,		100,	20,
			new Requirement(Edifício.QUARTEL, 1)),
	IGREJA				("Igreja", 				3,	 1.17,   5000,  10,		16000,	20000,	5000,
			new Requirement(Edifício.EDIFÍCIO_PRINCIPAL, 5), new Requirement(Edifício.FAZENDA, 5)),
	PRIMEIRA_IGREJA		("Primeira Igreja", 	1,	 1.17,   5,	  	10,		160,	200,	50);
	
	private final String nome;
	private final int populaçãoInicial;
	private final int pontosInicial;
	private final int nívelMáximo;
	private final int custoMadeira;
	private final int custoArgila;
	private final int custoFerro;
	
	private final List<Requirement> requisitos;

	private final double razãoPontos = 1.2;
	private final double razãoMadeira = 1.26;
	private final double razãoArgila = 1.275;
	private final double razãoFerro = 1.26;
	private final double razãoPopulação;
	
	/**
	 * Bônus de porcentagem de defesa da muralha
	 */
	private final BigDecimal[] bônus1 = new BigDecimal[21];

	/**
	 * Bônus flat de defesa da muralha
	 */
	private final BigDecimal[] bônus2 = new BigDecimal[21];
	
	private Edifício(String nome, int nívelMáximo, double razãoPopulação,
			int populaçãoInicial, int pontosInicial, int custoMadeira,
			int custoArgila, int custoFerro, Requirement... requisitos) {
		this.nome = nome;
		this.nívelMáximo = nívelMáximo;
		this.razãoPopulação = razãoPopulação;
		this.pontosInicial = pontosInicial;
		this.populaçãoInicial = populaçãoInicial;
		this.custoMadeira = custoMadeira;
		this.custoArgila = custoArgila;
		this.custoFerro = custoFerro;
		
		this.requisitos = new ArrayList<Requirement>(Arrays.asList(requisitos));

		if (nome.equals("Muralha"))
			setBônusDeMuralha();

	}

	/**
	 * Coloca os valores para o bônus da muralha
	 */
	private void setBônusDeMuralha() {

		for (int i = 0; i < 21; i++)
			bônus1[i] = new BigDecimal("1.037").pow(i);

		for (int i = 0; i < 21; i++)
			bônus2[i] = new BigDecimal(String.valueOf(20 + 50 * i));

	}


	public int getPontos(int nível) {
		if (nível == 0)
			return 0;
		else
			return (int) Math.round(pontosInicial 
				* Math.pow(razãoPontos, nível-1));
	}

	public int getPopulação(int nível) {
		if (nível == 0)
			return 0;
		else
			return (int) Math.round(populaçãoInicial 
				* Math.pow(razãoPopulação, nível-1));
	}
	
	/**
	 * Retorna o custo de argila para expandir o edifício para o nível passado
	 * como parâmetro, assumindo que ele esteja no nível anterior;
	 * 
	 * @param nível
	 * @return custo de argila
	 */
	public int getCustoArgila(int nível) {
		return (int) Math.round(custoArgila 
				* Math.pow(razãoArgila, nível-1));
	}

	/**
	 * Retorna o custo de madeira para expandir o edifício para o nível passado
	 * como parâmetro, assumindo que ele esteja no nível anterior;
	 * 
	 * @param nível
	 * @return custo de madeira
	 */
	public int getCustoMadeira(int nível) {
		return (int) Math.round(custoMadeira 
				* Math.pow(razãoMadeira, nível-1));
	}
	
	/**
	 * Retorna o custo de ferro para expandir o edifício para o nível passado
	 * como parâmetro, assumindo que ele esteja no nível anterior;
	 * 
	 * @param nível
	 * @return custo de ferro
	 */
	public int getCustoFerro(int nível) {
		return (int) Math.round(custoFerro 
				* Math.pow(razãoFerro, nível-1));
	}
	
	public List<Requirement> getRequisitos() {
		return requisitos;
	}
	
	public String toString() {
		return nome;
	}

	public int getNívelMáximo() {
		return nívelMáximo;
	}

	public BigDecimal bônusPercentual(int nível) {
		return bônus1[nível];
	}

	public BigDecimal bônusFlat(int nível) {
		return bônus2[nível];
	}
	
	private static class Requirement {
		
		Edifício ed;
		int nível;
		
		private Requirement(Edifício ed, int nível) {
			this.ed = ed;
			this.nível = nível;
		}
		
		public Edifício getEdifício() {
			return ed;
		}
		
		public int getNível() {
			return nível;
		}
		
	}
}
