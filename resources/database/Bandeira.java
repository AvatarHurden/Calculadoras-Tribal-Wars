package database;

public enum Bandeira {

	RECURSOS 	 (Tipo.Produtivo, 4, 	2),
	RECRUTAMENTO (Tipo.Produtivo, 6, 	2),
	ATAQUE   	 (Tipo.Militar,   2, 	1),
	DEFESA 		 (Tipo.Militar,   2, 	1),
	SORTE	 	 (Tipo.Sorte,     6, 	2),
	POPULAÇÃO 	 (Tipo.Produtivo, 2,    1),
	MOEDAS 		 (Tipo.Produtivo, -10, -2),
	SAQUE 		 (Tipo.Produtivo, 2,    1);
	
	private enum Tipo {
		Militar, Produtivo, Sorte;
	}
	
	private final Tipo tipo;
	private final int inicial;
	private final int aumento;
	
	
	private Bandeira (Tipo tipo, int inicial, int aumento) {
		
		this.tipo = tipo;
		this.inicial = inicial;
		this.aumento = aumento;
		
	}
	
	public double getValue(int level) {
		
		if (tipo.equals(Tipo.Sorte))
			return inicial+level*aumento;
		else return ((inicial+level*aumento)+100)/100;
		
	}
	
}
