package database;

public enum ItemPaladino {

	ALABARDA		("Alabarda de Guan Yu", 		  "Aumenta ataque de lanceiros em 30% e defesa em 20%",		     Unidade.LANCEIRO,     1.3, 1.2),
	MACHADO			("Machado de Guerra de Thogard",  "Aumenta ataque de bárbaros em 30% e defesa em 20%",   		 Unidade.BÁRBARO,      1.3, 1.2),
	ESPADA			("Espada Longa de Ullrich", 	  "Aumenta ataque de espadachins em 30% e defesa em 20%", 		 Unidade.ESPADACHIM,   1.3, 1.2),
	ARCO			("Arco Longo de Nimrod", 		  "Aumenta ataque de arqueiros em 30% e defesa em 20%", 		 Unidade.ARQUEIRO,     1.3, 1.2),
	TELESCÓPIO		("Telescópio de Kalid", 	 	  "Exploradores sempre veem as tropas de uma aldeia",			 Unidade.EXPLORADOR,   1.0, 1.0),
	LANÇA			("Lança de Miesko", 			  "Aumenta ataque de cavalaria leve em 30% e defesa em 20%", 	 Unidade.CAVALOLEVE,   1.3, 1.2),
	ESTANDARTE		("Estandarte de Baptiste", 		  "Aumenta ataque de cavalaria pesada em 30% e defesa em 20%",   Unidade.CAVALOPESADO, 1.3, 1.2),
	ARCO_COMPOSTO	("Arco Composto de Nimrod", 	  "Aumenta ataque de arqueiro a cavalo em 30% e defesa em 20%",  Unidade.ARCOCAVALO,   1.3, 1.2),
	ESTRELA			("Estrela de Manhã de Carol", 	  "Aumenta ataque de aríetes em 100%", 							 Unidade.ARÍETE,       2.0, 1.0),
	FOGUEIRA		("Fogueira de Aletheia", 		  "Aumenta ataque de catapultas em 100% e defesa em 1000%", 		 Unidade.CATAPULTA,    2.0, 11.0),
	CETRO			("Cetro de Vasco", "Aumenta ataque de nobres em 100% e defesa em 1000%", Unidade.NOBRE, 1.0, 1.0);
	
	private final String name;
	private final String description;
	private final Unidade unit;
	private final double modifierAtk;
	private final double modifierDef;
	
	private ItemPaladino(String nome, String descrição, Unidade unidade, double modificadorAtk, double modificadorDef) {
		
		this.name = nome;
		description = descrição;
		unit = unidade;
		modifierAtk = modificadorAtk;
		modifierDef = modificadorDef;
		
	}
	
	public String getName() {
		return name;
	}
	
}
