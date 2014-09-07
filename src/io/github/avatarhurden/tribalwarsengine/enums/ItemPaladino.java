package io.github.avatarhurden.tribalwarsengine.enums;

import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;


public enum ItemPaladino {

	NULL			("Nenhum item",					  "", 																   "",			1.0, 1.0, 1.0, 1.0),
	ALABARDA		("Alabarda de Guan Yu", 		  "Aumenta ataque de lanceiros em %.0f%% e defesa em %.0f%%",		   "spear",     1.3, 1.2, 1.3, 1.2),
	ESPADA			("Espada Longa de Ullrich", 	  "Aumenta ataque de espadachins em %.0f%% e defesa em %.0f%%", 	   "sword",   	1.3, 1.2, 1.4, 1.3),
	MACHADO			("Machado de Guerra de Thogard",  "Aumenta ataque de bárbaros em %.0f%% e defesa em %.0f%%",   		   "axe",     	1.3, 1.2, 1.4, 1.3),
	ARCO			("Arco Longo de Nimrod", 		  "Aumenta ataque de arqueiros em %.0f%% e defesa em %.0f%%", 		   "archer",    1.3, 1.2, 1.3, 1.2),
	TELESCÓPIO		("Telescópio de Kalid", 	 	  "Exploradores sempre veem as tropas de uma aldeia",			       "spy",   	1.0, 1.0, 1.0, 1.0),
	LANÇA			("Lança de Miesko", 			  "Aumenta ataque de cavalaria leve em %.0f%% e defesa em %.0f%%", 	   "light",   	1.3, 1.2, 1.3, 1.2),
	ESTANDARTE		("Estandarte de Baptiste", 		  "Aumenta ataque de cavalaria pesada em %.0f%% e defesa em %.0f%%",   "heavy",		1.3, 1.2, 1.3, 1.2),
	ARCO_COMPOSTO	("Arco Composto de Nimrod", 	  "Aumenta ataque de arqueiro a cavalo em %.0f%% e defesa em %.0f%%",  "marcher",   1.3, 1.2, 1.3, 1.2),
	ESTRELA			("Estrela de Manhã de Carol", 	  "Aumenta ataque de aríetes em %.0f%%", 							   "ram",       2.0, 1.0, 2.0, 1.0),
	FOGUEIRA		("Fogueira de Aletheia", 		  "Aumenta ataque de catapultas em %.0f%% e defesa em %.0f%%", 	 	   "catapult",  2.0, 1.0, 2.0, 11.0),
	CETRO			("Cetro de Vasco", 				  "Nobres reduzem a lealdade por, no mínimo, 30", 		 	   		   "snob",      1.0, 1.0, 1.0, 1.0);
	;	

	private final String name;
	private final String description;
	private final String unitName;
	private final double modifierAtk;
	private final double modifierDef;

	private ItemPaladino(String nome, String descrição, String unitName,
			double modificadorAtk, double modificadorDef, double advancedAtk,
			double advancedDef) {

		this.name = nome;
		this.unitName = unitName;
		if (WorldManager.getSelectedWorld().getWorld().isBetterItemsWorld()) {
			this.modifierAtk = advancedAtk;
			this.modifierDef = advancedDef;
		} else {
			this.modifierDef = modificadorDef;
			this.modifierAtk = modificadorAtk;
		}

		this.description = String.format(descrição, (modifierAtk - 1) * 100,
				(modifierDef - 1) * 100);

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
	public boolean isUnit(Unit unit) {
		return unit.getName().equals(unitName);
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
