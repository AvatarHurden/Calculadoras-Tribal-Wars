package io.github.avatarhurden.tribalwarsengine.objects;

import java.util.ArrayList;
import java.util.List;

import database.Edifício;
import database.Unidade;

public class TWServer {

	private World world;
	private List<Unidade> units;
	private List<Edifício> buildings;
	
	public TWServer() {
		world = new World();
		units = new ArrayList<Unidade>();
		buildings = new ArrayList<Edifício>();
	}
	
	public TWServer setWorld(World w) {
		world = w;
		return this;
	}
	
	public TWServer setUnits(List<Unidade> list) {
		units = list;
		return this;
	}
	
	public TWServer setBuildings(List<Edifício> list) {
		buildings = list;
		return this;
	}
	
	public World getWorld() {
		return world;
	}
	
	public List<Unidade> getUnits() {
		return units;
	}
	
	public List<Edifício> getBuildings() {
		return buildings;
	}
	
}
