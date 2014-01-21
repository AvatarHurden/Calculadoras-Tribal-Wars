package database;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Edifício {
	
	//						Nome		     nívelMax ft.Pop  pop    pontos
 	
	EDIFÍCIO_PRINCIPAL  ("Edifício Principal",  30,  "1.17",   5, 	  10),
	QUARTEL			    ("Quartel",			   	25,  "1.17",   7, 	  16),
	ESTÁBULO			("Estábulo", 			20,	 "1.17",   8,	  20),
	OFICINA				("Oficina", 			15,	 "1.17",   8,	  24),
	IGREJA				("Igreja", 				3,	 "1.17",   5000,  10),
	PRIMEIRA_IGREJA		("Primeira Igreja", 	1,	 "1.17",   5,	  10),
	ACADEMIA_3NÍVEIS	("Academia",			3,	 "1.17",   80,	  512),
	ACADEMIA_1NÍVEL		("Academia",			1,	 "1.17",   80,	  512),
	FERREIRO			("Ferreiro", 			20,	 "1.17",   20,	  19),
	PRAÇA_DE_REUNIÃO	("Pração de Reunião", 	1,	 "1.17",   0,	  0),
	ESTÁTUA				("Estátua", 			1,	 "1.17",   10,	  24),
	MERCADO				("Mercado", 			25,	 "1.17",   20,	  10),
	BOSQUE				("Bosque", 				30,	 "1.155",  5,	  6),
	POÇO_DE_ARGILA		("Poço de Argila", 		30,	 "1.14",   10,	  6),
	MINA_DE_FERRO		("Mina de Ferro", 		30,	 "1.17",   10,	  6),	
	FAZENDA				("Fazenda", 			30,	 "1.172103",   240,   5),
	ARMAZÉM				("Armazém", 			30,  "1.15",   0,     6),	
	ESCONDERIJO			("Esconderijo", 		10,	 "1.17",   2,	  5),
	MURALHA				("Muralha", 			20,	 "1.17",   5,	  8),
	NULL				(null, 					1,	 "0",      0,	  0);
	
		private final String nome;
		private final int[] população;
		private final int[] pontos;	
		private final int nívelMáximo;
		
		
		/**
		 * Bônus de porcentagem de defesa da muralha
		 */
		private final BigDecimal[] bônus1 = new BigDecimal[21];
		
		/**
		 * Bônus flat de defesa da muralha
		 */
		private final BigDecimal[] bônus2 = new BigDecimal[21];
		
		
		private final BigDecimal razãoPontos = new BigDecimal("1.2");
		private final BigDecimal razãoPopulação;

		private Edifício (String nome,int nívelMáximo, String razãoPopulação, int populaçãoInicial, int pontosInicial) {
			this.nome = nome;
			this.nívelMáximo = nívelMáximo;
			this.razãoPopulação = new BigDecimal(razãoPopulação);
			
			this.população = createList(populaçãoInicial, this.razãoPopulação, nívelMáximo);
			this.pontos = createList(pontosInicial, this.razãoPontos, nívelMáximo);
			
			if (nome != null && nome.equals("Muralha"))
				setBônusDeMuralha();
			
		}
		
		/**
		 * Coloca os valores para o bônus da muralha
		 */
		private void setBônusDeMuralha() {
			
			for (int i = 0; i < 21; i++)
				bônus1[i] = new BigDecimal("1.037").pow(i);
			
			for (int i = 0; i < 21; i++)
				bônus2[i] = new BigDecimal(String.valueOf(20+50*i));
			
		}
		
		/**
		 * Cria um int array, com o primeiro elemento 0, seguindo uma progressão geométrica a partir
		 * do segundo elemento. O array possui componentes+1 elementos, pois inicia no 0 
		 * 
		 * @param int segundo elemento do conjunto
		 * @param int razão geométrica
		 * @param int número de componentes, a partir do segundo
		 * @return
		 */
		private int[] createList(int inicial, BigDecimal razão, int componentes) {
			
			int[] array = new int[componentes+1];
			
			array[0] = 0;
			array[1] = inicial;
			
			BigDecimal valor = new BigDecimal(inicial);
			
			for (int i = 2; i < array.length; i++) {
				valor = valor.multiply(razão);
				array[i] = valor.setScale(0,RoundingMode.HALF_UP).intValue();
			}
			
			return array;
		}
			
		public int pontos(int nível) { return pontos[nível]; }
		
		public int população(int nível) { return população[nível]; }
		
		public String nome() { return nome; }
		
		public int nívelMáximo() { return nívelMáximo; }

		public BigDecimal bônusPercentual(int nível) { return bônus1[nível]; } 
		
		public BigDecimal bônusFlat(int nível) { return bônus2[nível]; } 
}
