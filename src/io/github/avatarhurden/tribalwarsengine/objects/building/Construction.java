package io.github.avatarhurden.tribalwarsengine.objects.building;

import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;

public class Construction {
	
	private transient Building edif�cio;
	private String name;
	private int nivel;
	
	protected Construction(Building ed, int nivel) {
		this.edif�cio = ed;
		this.nivel = nivel;
		name = ed.getName();
	}
	
	public String getName() {
		return name;
	}
	
	public int getN�vel() {
		return nivel;
	}
	
	public Building getEdif�cio() {
		if (edif�cio == null)
			for (Building u : BuildingBlock.getAvailableBuildings())
				if (u.getName().equals(name))
					edif�cio = u;
		return edif�cio;
	}
	
	public int getPontos() {
		int base;
		switch (edif�cio.getName()) {
		case "main":
			base = 10;
			break;
		case "barracks":
			base = 16;
			break;
		case "stable":
			base = 20;
			break;
		case "garage":
			base = 24;
			break;
		case "church":
			base = 10;
			break;
		case "church_f":
			base = 10;
			break;
		case "snob":
			base = 512;
			break;
		case "smith":
			base = 19;
			break;
		case "place":
			base = 0;
			break;
		case "statue":
			base = 24;
			break;
		case "market":
			base = 10;
			break;
		case "wood":
			base = 6;
			break;
		case "stone":
			base = 6;
			break;
		case "iron":
			base = 6;
			break;
		case "farm":
			base = 5;
			break;
		case "storage":
			base = 6;
			break;
		case "hide":
			base = 5;
			break;
		case "wall":
			base = 8;
			break;
		default:
			base = 0;
			break;
		}
		return (int) Math.round(base * Math.pow(1.2, nivel-1));
	}
	
	public int getPopula��o() {
		return (int) Math.round(edif�cio.getPopulation() * 
				Math.pow(edif�cio.getFactorPopulation(), nivel-1));
	}
	
}