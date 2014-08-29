package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONObject;

public class Building {

	private JSONObject json;
	
	public Building(JSONObject json) {
		this.json = json;
	}
	
	public JSONObject getJSON() {
		return json;
	}
	
	public int getMinLevel() {
		return json.getInt("min_level");
	}
	
	public int getMaxLevel() {
		return json.getInt("max_level");
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

	public double getBuildTime() {
		return json.getDouble("build_time");
	}
	
	public double getFactorPopulation() {
		return json.getDouble("pop_factor");
	}
	
	public double getFactorWood() {
		return json.getDouble("wood_factor");
	}
	
	public double getFactorClay() {
		return json.getDouble("stone_factor");
	}
	
	public double getFactorIron() {
		return json.getDouble("iron_factor");
	}
	
	public double getFactorBuild() {
		return json.getDouble("build_time_factor");
	}
	
	public String getName() {
		switch (json.getString("name")) {
		case "main":
			return "Edifício Principal";
		case "barracks":
			return "Quartel";
		case "stable":
			return "Estábulo";
		case "garage":
			return "Oficina";
		case "snob":
			return "Academia";
		case "smith":
			return "Ferreiro";
		case "place":
			return "Praça de Reunião";
		case "statue":
			return "Estátua";
		case "market":
			return "Mercado";
		case "wood":
			return "Bosque";
		case "stone":
			return "Poço de Argila";
		case "iron":
			return "Mina de Ferro";
		case "farm":
			return "Fazenda";
		case "storage":
			return "Armazém";
		case "hide":
			return "Esconderijo";
		case "wall":
			return "Muralha";
		default:
			return "";
		}
	}
	
}
