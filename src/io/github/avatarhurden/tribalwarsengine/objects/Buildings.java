package io.github.avatarhurden.tribalwarsengine.objects;

import java.util.ArrayList;
import java.util.Iterator;

import database.Edifício;

/**
 * Essa classe mantém tudo relacionado a um conjunto de edifícios
 * 
 * @author Arthur
 *
 */
public class Buildings {
	
	private ArrayList<Building> buildings;
	
	public void addBuilding(Edifício ed, int nível) {
		
		Iterator<Building> iter = buildings.iterator();
		while (iter.hasNext())
			if (iter.next().ed.equals(ed))
				iter.remove();
		
		buildings.add(new Building(ed, nível));
		
	}
	
	public int getLevel(Edifício ed) {
		
		for (Building b : buildings)
			if (b.ed.equals(ed))
				return b.nivel;
		
		return -1;
	}
	
	private class Building {
		
		private Edifício ed;
		private int nivel;
		
		private Building(Edifício ed, int nivel) {
			this.ed = ed;
			this.nivel = nivel;
		}
		
		private int getCustoFerroNext() {
			return ed.getCustoFerro(nivel+1);
		}
		
		private int getCustoArgilaNext() {
			return ed.getCustoArgila(nivel+1);
		}
		
		private int getCustoMadeiraNext() {
			return ed.getCustoMadeira(nivel+1);
		}
		
	}
	
}
