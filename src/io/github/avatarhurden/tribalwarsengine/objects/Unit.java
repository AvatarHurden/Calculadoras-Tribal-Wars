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
	
}
