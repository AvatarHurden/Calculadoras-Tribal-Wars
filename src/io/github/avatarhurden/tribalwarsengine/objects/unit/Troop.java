package io.github.avatarhurden.tribalwarsengine.objects.unit;

import io.github.avatarhurden.tribalwarsengine.enums.ItemPaladino;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit.UnitType;

/**
	 * Classe que representa uma tropa específica, com a unidade, nível e quantidade
	 * @author Arthur
	 *
	 */
	public class Troop {
		
		private transient Unit unidade;
		private String name;
		private int quantidade;
		private int nivel;
		
		protected Troop(Unit unidade, int quantidade, int nivel) {
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel = nivel;
			name = unidade.getName();
		}
		
		protected Troop(Unit unidade, int quantidade) {
			this(unidade, quantidade, 1);
		}
		
		public Unit getUnit() {
			if (unidade == null)
				for (Unit u : Army.getAvailableUnits())
					if (u.getName().equals(name))
						unidade = u;
			return unidade;
		}
		
		public String getName() {
			return name;
		}
		
		public int getQuantity() {
			return quantidade;
		}
		
		public int getLevel() {
			return nivel;
		}
		
		public UnitType getType() {
			return unidade.getType();
		}
		
		public int getAttack() {
			return getAttack(ItemPaladino.NULL);
		}
		
		public int getAttack(ItemPaladino item) {
			
			return upgradeAttack(nivel, unidade.getAttack(), item) * quantidade;
					
		}
		
		public int getDefense() {
			return getDefense(ItemPaladino.NULL);
		}
	
		public int getDefense(ItemPaladino item) {
			return upgradeDefense(nivel, unidade.getDefense(), item) * quantidade;
		}
		
		public int getDefenseCavalry() {
			return getDefenseCavalry(ItemPaladino.NULL);
		}
		
		public int getDefenseCavalry(ItemPaladino item) {
			return upgradeDefense(nivel, unidade.getDefenseCavalry(), item) * quantidade;
		}
		
		public int getDefenseArcher() {
			return getDefenseArcher(ItemPaladino.NULL);
		}
		
		public int getDefenseArcher(ItemPaladino item) {
			return upgradeDefense(nivel, unidade.getDefenseArcher(), item) * quantidade;
		}
		
		public int getCostWood() {
			return unidade.getCostWood() * quantidade;
		}
		
		public int getCostClay() {
			return unidade.getCostClay() * quantidade;
		}
		
		public int getCostIron() {
			return unidade.getCostIron() * quantidade;
		}
		
		public int getPopulation() {
			return unidade.getPopulation() * quantidade;
		}
		
		public int getHaul() {
			return unidade.getHaul() * quantidade;
		}
		
		public double getProductionTime() {
			return unidade.getProductionTime() * quantidade;
		}
		
		public int getODAttacker() {
			int base;
			switch (unidade.getName()) {
			case "spear":
				base = 4;
				break;
			case "sword":
				base = 5;
				break;
			case "axe":
				base = 1;
				break;
			case "archer":
				base = 5;
				break;
			case "spy":
				base = 1;
				break;
			case "light":
				base = 5;
				break;
			case "marcher":
				base = 6;
				break;
			case "heavy":
				base = 23;
				break;
			case "ram":
				base = 4;
				break;
			case "catapult":
				base = 12;
				break;
			case "knight":
				base = 40;
				break;
			case "snob":
				base = 200;
				break;
			case "militia":
				base = 4;
				break;
			default:
				base = 0;
				break;
			}
			return base * quantidade;
		}
		
		public int getODDefender() {
			int base;
			switch (unidade.getName()) {
			case "spear":
				base = 1;
				break;
			case "sword":
				base = 2;
				break;
			case "axe":
				base = 4;
				break;
			case "archer":
				base = 2;
				break;
			case "spy":
				base = 2;
				break;
			case "light":
				base = 13;
				break;
			case "marcher":
				base = 12;
				break;
			case "heavy":
				base = 15;
				break;
			case "ram":
				base = 8;
				break;
			case "catapult":
				base = 10;
				break;
			case "knight":
				base = 20;
				break;
			case "snob":
				base = 200;
				break;
			case "militia":
				base = 0;
				break;
			default:
				base = 0;
				break;
			}
			return base * quantidade;

		}
		
		public double getSpeed() {
			if (quantidade == 0)
				return 0;
			else
				return unidade.getSpeed();
		}
		
		private int upgradeAttack(int level, int value, ItemPaladino item) {
			int upgraded = upgradeValue(level, value);

			if (item.isUnit(unidade))
				upgraded *= item.getModifierAtk();
			
			return upgraded;
		}
		
		private int upgradeDefense(int level, int value, ItemPaladino item) {
			int upgraded = upgradeValue(level, value);

			if (item.isUnit(unidade))
				upgraded *= item.getModifierDef();
			
			return upgraded;
		}
		
		private int upgradeValue(int level, int value) {
			double total = value;
			
			if (WorldManager.getSelectedWorld().getWorld().getResearchSystemLevels() == 3) {
				if (level == 2)
					total = total * 1.25;
				else if (level == 3)
					total = total * 1.4;
			} else if (WorldManager.getSelectedWorld().getWorld().getResearchSystemLevels() == 10)
				total = total * Math.pow(1.04605, level-1);

			return (int) Math.round(total);

		}

	}