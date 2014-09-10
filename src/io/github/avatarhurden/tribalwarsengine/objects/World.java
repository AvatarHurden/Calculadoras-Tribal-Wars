package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.managers.WorldFileManager;
import io.github.avatarhurden.tribalwarsengine.objects.building.Building;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class World {

	private WorldFileManager downloader;
	
	private WorldInfo world;
	private List<Unit> units;
	private List<Building> buildings;
	private List<BuildingBlockModel> buildingModels;
	private List<ArmyModel> armyModels;
	
	// JSONObject with the information to be displayed in the selectionPanel
	private JSONObject basicInfo;
	
	private JSONObject worldInfo;
	
	public World(JSONObject worldInfo) {
		this.worldInfo = worldInfo;
		downloader = new WorldFileManager("br", worldInfo);
		
		// Caso o mundo seja speed, carrega todas as informações para atualização,
		// devido à curta duração de cada mundo
		if (worldInfo.getString("name").substring(2).contains("s"))
			setInfo();
		
		basicInfo = setBasicInfo();
	}

	public void setInfo() {
		if (world == null)
			world = new WorldInfo(downloader.getWorldConfig());
		if (units == null)
			setUnits(downloader.getUnitConfig());
		if (buildings == null)
			setBuildings(downloader.getBuildingConfig());
		if (buildingModels == null)
			setBuildingModels(downloader.getBuildingModels());
		if (armyModels == null)
			setArmyModels(downloader.getArmyModels());
		
		basicInfo = makeBasicInfo();
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
	
	private void setBuildingModels(JSONArray array) {
		buildingModels = new ArrayList<BuildingBlockModel>();
		for (int i = 0; i < array.length(); i++)
			buildingModels.add(new BuildingBlockModel(array.getJSONObject(i)));
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
	
	public void addBuildingModel(BuildingBlockModel model) {
		buildingModels.add(model);
	}
	
	public void addArmyModel(ArmyModel model) {
		armyModels.add(model);
	}
	
	public String getName() {
		return worldInfo.getString("name");
	}
	
	public String getPrettyName() {
		if (basicInfo != null)
			return basicInfo.getString("name");
		else {
			String name = worldInfo.getString("name").substring(2);
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
	
	public WorldInfo getWorld() {
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
	
	public List<BuildingBlockModel> getBuildingModelList() {
		return buildingModels;
	}
	
	public JSONObject getBasicConfig() {
		return basicInfo;
	}
	
	public JSONObject getWorldInfo() {
		return worldInfo;
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
	
	public void saveBuildingModels() {
		JSONArray array = new JSONArray();
		for (BuildingBlockModel model : buildingModels)
			array.put(model.getJson());
		
		downloader.saveBuildingModelConfig(array);
	}
	
}
