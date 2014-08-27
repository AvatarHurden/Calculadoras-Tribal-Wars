package io.github.avatarhurden.tribalwarsengine.objects;

import java.util.ArrayList;
import java.util.List;

import database.Edif�cio;
import database.Unidade;

public class TWServer {

	private World world;
	private List<Unidade> units;
	private List<Edif�cio> buildings;
	
	public TWServer() {
		world = new World();
		units = new ArrayList<Unidade>();
		buildings = new ArrayList<Edif�cio>();
	}
	
	public TWServer setWorld(World w) {
		world = w;
		return this;
	}
	
	public TWServer setUnits(List<Unidade> list) {
		units = list;
		return this;
	}
	
	public TWServer setBuildings(List<Edif�cio> list) {
		buildings = list;
		return this;
	}
	
	public World getWorld() {
		return world;
	}
	
	public List<Unidade> getUnits() {
		return units;
	}
	
	public List<Edif�cio> getBuildings() {
		return buildings;
	}
	
}
