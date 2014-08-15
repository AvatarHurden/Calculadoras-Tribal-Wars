package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import java.util.ArrayList;

import database.Mundo;

public class Escopo extends ArrayList<Mundo>{

	private boolean isGlobal;
	
	public Escopo() {
		super();
	}
	
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	public boolean getGlobal() {
		return isGlobal;
	}
	
}
