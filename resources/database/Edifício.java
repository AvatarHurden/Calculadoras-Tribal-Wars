package database;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Edif�cio {
	
	//						Nome		     n�velMax ft.Pop  pop    pontos
 	
	EDIF�CIO_PRINCIPAL  ("Edif�cio Principal",  30,  "1.17",   5, 	  10),
	QUARTEL			    ("Quartel",			   	25,  "1.17",   7, 	  16),
	EST�BULO			("Est�bulo", 			20,	 "1.17",   8,	  20),
	OFICINA				("Oficina", 			15,	 "1.17",   8,	  24),
	IGREJA				("Igreja", 				3,	 "1.17",   5000,  10),
	PRIMEIRA_IGREJA		("Primeira Igreja", 	1,	 "1.17",   5,	  10),
	ACADEMIA_3N�VEIS	("Academia",			3,	 "1.17",   80,	  512),
	ACADEMIA_1N�VEL		("Academia",			1,	 "1.17",   80,	  512),
	FERREIRO			("Ferreiro", 			20,	 "1.17",   20,	  19),
	PRA�A_DE_REUNI�O	("Pra��o de Reuni�o", 	1,	 "1.17",   0,	  0),
	EST�TUA				("Est�tua", 			1,	 "1.17",   10,	  24),
	MERCADO				("Mercado", 			25,	 "1.17",   20,	  10),
	BOSQUE				("Bosque", 				30,	 "1.155",  5,	  6),
	PO�O_DE_ARGILA		("Po�o de Argila", 		30,	 "1.14",   10,	  6),
	MINA_DE_FERRO		("Mina de Ferro", 		30,	 "1.17",   10,	  6),	
	FAZENDA				("Fazenda", 			30,	 "1.172103",   240,   5),
	ARMAZ�M				("Armaz�m", 			30,  "1.15",   0,     6),	
	ESCONDERIJO			("Esconderijo", 		10,	 "1.17",   2,	  5),
	MURALHA				("Muralha", 			20,	 "1.17",   5,	  8),
	NULL				(null, 					1,	 "0",      0,	  0);
	
		private final String nome;
		private final int[] popula��o;
		private final int[] pontos;	
		private final int n�velM�ximo;
		
		
		/**
		 * B�nus de porcentagem de defesa da muralha
		 */
		private final BigDecimal[] b�nus1 = new BigDecimal[21];
		
		/**
		 * B�nus flat de defesa da muralha
		 */
		private final BigDecimal[] b�nus2 = new BigDecimal[21];
		
		
		private final BigDecimal raz�oPontos = new BigDecimal("1.2");
		private final BigDecimal raz�oPopula��o;

		private Edif�cio (String nome,int n�velM�ximo, String raz�oPopula��o, int popula��oInicial, int pontosInicial) {
			this.nome = nome;
			this.n�velM�ximo = n�velM�ximo;
			this.raz�oPopula��o = new BigDecimal(raz�oPopula��o);
			
			this.popula��o = createList(popula��oInicial, this.raz�oPopula��o, n�velM�ximo);
			this.pontos = createList(pontosInicial, this.raz�oPontos, n�velM�ximo);
			
			if (nome != null && nome.equals("Muralha"))
				setB�nusDeMuralha();
			
		}
		
		/**
		 * Coloca os valores para o b�nus da muralha
		 */
		private void setB�nusDeMuralha() {
			
			for (int i = 0; i < 21; i++)
				b�nus1[i] = new BigDecimal("1.037").pow(i);
			
			for (int i = 0; i < 21; i++)
				b�nus2[i] = new BigDecimal(String.valueOf(20+50*i));
			
		}
		
		/**
		 * Cria um int array, com o primeiro elemento 0, seguindo uma progress�o geom�trica a partir
		 * do segundo elemento. O array possui componentes+1 elementos, pois inicia no 0 
		 * 
		 * @param int segundo elemento do conjunto
		 * @param int raz�o geom�trica
		 * @param int n�mero de componentes, a partir do segundo
		 * @return
		 */
		private int[] createList(int inicial, BigDecimal raz�o, int componentes) {
			
			int[] array = new int[componentes+1];
			
			array[0] = 0;
			array[1] = inicial;
			
			BigDecimal valor = new BigDecimal(inicial);
			
			for (int i = 2; i < array.length; i++) {
				valor = valor.multiply(raz�o);
				array[i] = valor.setScale(0,RoundingMode.HALF_UP).intValue();
			}
			
			return array;
		}
			
		public int pontos(int n�vel) { return pontos[n�vel]; }
		
		public int popula��o(int n�vel) { return popula��o[n�vel]; }
		
		public String nome() { return nome; }
		
		public int n�velM�ximo() { return n�velM�ximo; }

		public BigDecimal b�nusPercentual(int n�vel) { return b�nus1[n�vel]; } 
		
		public BigDecimal b�nusFlat(int n�vel) { return b�nus2[n�vel]; } 
}
