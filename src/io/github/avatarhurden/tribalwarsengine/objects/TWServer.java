package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.main.ServerDownloader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TWServer {

	private ServerDownloader downloader;
	
	private World world;
	private List<Unit> units;
	private List<Building> buildings;
	
	// JSONObject with the information to be displayed in the selectionPanel
	private JSONObject basicInfo;
	
	private JSONObject serverInfo;
	
	public TWServer(JSONObject serverInfo) {
		this.serverInfo = serverInfo;
		downloader = new ServerDownloader("br", serverInfo);
		basicInfo = setBasicInfo();
	}

	public void setInfo() {
		if (world == null)
			world = new World(downloader.getServerWorldConfig());
		if (units == null)
			setUnits(downloader.getServerUnitConfig());
		if (buildings == null)
			setBuildings(downloader.getServerBuildingConfig());
	}

	private void setUnits(JSONArray array) {
		units = new ArrayList<Unit>();
		for (int i = 0; i < array.length(); i++)
			units.add(new Unit(array.getJSONObject(i)));
	}
	
	private void setBuildings(JSONArray array) {
		buildings = new ArrayList<Building>();
		for (int i = 0; i < array.length(); i++)
			buildings.add(new Building(array.getJSONObject(i)));
	}
	
	
	private JSONObject setBasicInfo() {
		try {
			return downloader.getBasicConfig();
		} catch (Exception e) {
			return makeBasicInfo();
		}
	}
	
	private JSONObject makeBasicInfo() {
		setInfo();
		
		JSONObject json = new JSONObject();
		json.put("speed", world.getWorldSpeed());
		json.put("unit_speed", world.getUnitModifier());
		json.put("moral", world.isMoralWorld());
		json.put("tech", world.getResearchSystem().getName());
		json.put("church", world.isChurchWorld());
		json.put("night", world.isNightBonusWorld());
		json.put("archer", world.isArcherWorld());
		json.put("knight", world.isPaladinWorld());
		json.put("better_items", world.isBetterItemsWorld());
		json.put("coins", world.isCoiningWorld());
		json.put("militia", false);
		
		for (Unit i : units)
			if (i.getJSON().getString("name").equals("militia"))
				json.put("militia", true);
		
		downloader.saveBasicConfig(json);
		
		return json;
	}
	
	public String getName() {
		return serverInfo.getString("name");
	}
	
	public World getWorld() {
		return world;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public List<Building> getBuildings() {
		return buildings;
	}
	
	public JSONObject getBasicConfig() {
		return basicInfo;
	}
	
	public JSONObject getServerInfo() {
		return serverInfo;
	}
	
	public String toString() {
		return getName();
	}
	
}
