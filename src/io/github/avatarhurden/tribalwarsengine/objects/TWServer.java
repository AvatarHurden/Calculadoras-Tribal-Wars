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
	private List<VillageModel> villageModels;
	private List<ArmyModel> armyModels;
	
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
		if (villageModels == null)
			setVillageModels(downloader.getServerVillageModels());
		if (armyModels == null)
			setArmyModels(downloader.getServerArmyModels());
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
	
	private void setVillageModels(JSONArray array) {
		villageModels = new ArrayList<VillageModel>();
		for (int i = 0; i < array.length(); i++)
			villageModels.add(new VillageModel(array.getJSONObject(i)));
	}
	
	private void setArmyModels(JSONArray array) {
		armyModels = new ArrayList<ArmyModel>();
		for (int i = 0; i < array.length(); i++)
			armyModels.add(new ArmyModel(array.getJSONObject(i)));
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
		json.put("name", getPrettyName());
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
	
	public void addVillageModel(VillageModel model) {
		villageModels.add(model);
	}
	
	public void addArmyModel(ArmyModel model) {
		armyModels.add(model);
	}
	
	public String getName() {
		return serverInfo.getString("name");
	}
	
	public String getPrettyName() {
		if (basicInfo != null)
			return basicInfo.getString("name");
		else {
			String name = serverInfo.getString("name").substring(2);
			if (name.indexOf("c") != -1)
				return "Clássico " + name.substring(1);
			else if (name.indexOf("s") != -1)
				return "Speed " + name.substring(1);
			else if (name.indexOf("p") != -1)
				return "Casual " + name.substring(1);
			else
				return "Mundo " + name;
		}
		
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
	
	public List<ArmyModel> getArmyModelList() {
		return armyModels;
	}
	
	public List<VillageModel> getVillageModelList() {
		return villageModels;
	}
	
	public JSONObject getBasicConfig() {
		return basicInfo;
	}
	
	public JSONObject getServerInfo() {
		return serverInfo;
	}
	
	public String toString() {
		return getPrettyName();
	}
	
	public void saveArmyModels() {
		JSONArray array = new JSONArray();
		for (ArmyModel model : armyModels)
			array.put(model.getJson());
		
		downloader.saveArmyModelConfig(array);
	}
	
	public void saveVillageModels() {
		JSONArray array = new JSONArray();
		for (VillageModel model : villageModels)
			array.put(model.getJson());
		
		downloader.saveVillageModelConfig(array);
	}
	
}
