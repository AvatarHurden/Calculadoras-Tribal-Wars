package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONObject;

public class Unit {
	
	private JSONObject json;

	public Unit(JSONObject json) {
		this.json = json;
	}
	
	public JSONObject getJSON() {
		return json;
	}
	
	public int getAttack() {
		return json.getInt("attack");
	}
	
	public int getDefense() {
		return json.getInt("defense");
	}
	
	public int getDefenseCavalry() {
		return json.getInt("defense_cavalry");
	}
	
	public int getDefenseArcher() {
		return json.getInt("defense_archer");
	}
	
	public int getHaul() {
		return json.getInt("carry");
	}
	
	public int getPopulation() {
		return json.getInt("pop");
	}
	
	public int getCostWood() {
		return json.getInt("wood");
	}
	
	public int getCostClay() {
		return json.getInt("stone");
	}
	
	public int getCostIron() {
		return json.getInt("iron");
	}
	
	public double getSpeed() {
		return json.getDouble("speed");
	}
	
	public double getProductionTime() {
		return json.getDouble("build_time");
	}
	
	public String getName() {
		switch (json.getString("name")) {
		case "spear":
			return "Lanceiro";
		case "sword":
			return "Espadachim";
		case "axe":
			return "B�rbaro";
		case "archer":
			return "Arqueiro";
		case "spy":
			return "Explorador";
		case "light":
			return "Cavalaria Leve";
		case "marcher":
			return "Arqueiro a Cavalor";
		case "heavy":
			return "Cavalaria Pesada";
		case "ram":
			return "Ar�ete";
		case "catapult":
			return "Catapulta";
		case "knight":
			return "Paladino";
		case "snob":
			return "Nobre";
		case "militia":
			return "Mil�cia";
		default:
			return "";
		}
	}
	
	public UnitType getType() {
		switch (json.getString("name")) {
		case "spear":
		case "sword":
		case "axe":
		case "ram":
		case "catapult":
		case "snob":
			return UnitType.General;
		case "light":
		case "heavy":
		case "knight":
			return UnitType.Cavalry;
		case "archer":
		case "marcher":
			return UnitType.Archer;
		default:
			return UnitType.unspecified;
		}
	}
	
	public enum UnitType {
		General, Cavalry, Archer, unspecified;
	}
	
}
