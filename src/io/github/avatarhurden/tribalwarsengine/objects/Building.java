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
	
}
