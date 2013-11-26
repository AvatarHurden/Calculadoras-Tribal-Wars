package database;

public enum ItemPaladino {

	ALABARDA		("Alabarda de Guan Yu", 		  "Aumenta ataque de lanceiros em %.0f%% e defesa em %.0f%%",		   Unidade.LANCEIRO,     1.3, 1.2, 1.3, 1.2),
	MACHADO			("Machado de Guerra de Thogard",  "Aumenta ataque de bárbaros em %.0f%% e defesa em %.0f%%",   		   Unidade.BÁRBARO,      1.3, 1.2, 1.4, 1.3),
	ESPADA			("Espada Longa de Ullrich", 	  "Aumenta ataque de espadachins em %.0f%% e defesa em %.0f%%", 	   Unidade.ESPADACHIM,   1.3, 1.2, 1.4, 1.3),
	ARCO			("Arco Longo de Nimrod", 		  "Aumenta ataque de arqueiros em %.0f%% e defesa em %.0f%%", 		   Unidade.ARQUEIRO,     1.3, 1.2, 1.3, 1.2),
	TELESCÓPIO		("Telescópio de Kalid", 	 	  "Exploradores sempre veem as tropas de uma aldeia",			       Unidade.EXPLORADOR,   1.0, 1.0, 1.0, 1.0),
	LANÇA			("Lança de Miesko", 			  "Aumenta ataque de cavalaria leve em %.0f%% e defesa em %.0f%%", 	   Unidade.CAVALOLEVE,   1.3, 1.2, 1.3, 1.2),
	ESTANDARTE		("Estandarte de Baptiste", 		  "Aumenta ataque de cavalaria pesada em %.0f%% e defesa em %.0f%%",   Unidade.CAVALOPESADO, 1.3, 1.2, 1.3, 1.2),
	ARCO_COMPOSTO	("Arco Composto de Nimrod", 	  "Aumenta ataque de arqueiro a cavalo em %.0f%% e defesa em %.0f%%",  Unidade.ARCOCAVALO,   1.3, 1.2, 1.3, 1.2),
	ESTRELA			("Estrela de Manhã de Carol", 	  "Aumenta ataque de aríetes em %.0f%%", 							   Unidade.ARÍETE,       2.0, 1.0, 2.0, 1.0),
	FOGUEIRA		("Fogueira de Aletheia", 		  "Aumenta ataque de catapultas em %.0f%% e defesa em %.0f%%", 	 	   Unidade.CATAPULTA,    2.0, 1.0, 2.0, 11.0),
	CETRO			("Cetro de Vasco", 				  "Nobres reduzem a lealdade por, no mínimo, 30", 		 	   		   Unidade.NOBRE,        1.0, 1.0, 1.0, 1.0),
	NULL			("", 							  "", 																   null,				 1.0, 1.0, 1.0, 1.0);	
	
	private final String name;
	private final String description;
	private final Unidade unit;
	private final double modifierAtk;
	private final double modifierDef;
	
	private ItemPaladino(String nome, String descrição, Unidade unidade, double modificadorAtk,
			double modificadorDef, double advancedAtk, double advancedDef) {
		
		this.name = nome;
		this.unit = unidade;
		if (MundoSelecionado.hasItensAprimorados()) {
			this.modifierAtk = advancedAtk;
			this.modifierDef = advancedDef;
		} else {
			this.modifierDef = modificadorDef;
			this.modifierAtk = modificadorAtk;
		}
		
		this.description = String.format(descrição, (modifierAtk-1)*100, (modifierDef-1)*100 );
		
	}
	
	@Override
    public String toString() {
        return name;
    }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the unit
	 */
	public Unidade getUnit() {
		return unit;
	}

	/**
	 * @return the attack modifier
	 */
	public double getModifierAtk() {
		return modifierAtk;
	}

	/**
	 * @return the defense modifier
	 */
	public double getModifierDef() {
		return modifierDef;
	}

	
	
	
}
