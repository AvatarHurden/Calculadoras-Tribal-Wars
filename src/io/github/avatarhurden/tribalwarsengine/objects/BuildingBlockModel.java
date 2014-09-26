package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.objects.building.Building;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock;

import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BuildingBlockModel implements EditableObject {
	
	private JSONObject json;
	private Gson gson = new GsonBuilder().create();
	
	/**
	 * Construtor com as informações do modelo já criadas. Para gerar um modelo vazio,
	 * usar o construtor sem parâmetros
	 * 
	 * @param json com as informações do modelo
	 */
	public BuildingBlockModel(JSONObject json) {
		this.json = json;
	}
	
	public BuildingBlockModel() {
		this(new JSONObject());
	
		setStartingValues();
	}
	
	private void setStartingValues() {
		
		setName("Novo Modelo");
		setBuildings(new BuildingBlock());
		
	}
	
    private Object get(String chave, Object def) {
        try {
            return json.get(chave);
        } catch (JSONException e) {
            return def;
        }
    }

    private void set(String chave, Object valor) {
        json.put(chave, valor);
    }
    
    public JSONObject getJson() {
        return json;
    }
    
    public String toString() {
    	return getName();
    }
    
    // Getters
    
    public String getName() {
    	return (String) get("name", "");
    }
    
    public BuildingBlock getBuildings() {
    	return gson.fromJson(get("buildings", "").toString(), BuildingBlock.class);
    }
    
    // Setters
    
    public BuildingBlockModel setName(String name) {
    	set("name", name);
    	return this;
    }
    
    public BuildingBlockModel setBuildings(BuildingBlock buildings) {
    	
    	BuildingBlock toSave = new BuildingBlock();
    	for (Building u : toSave.getEdifícios()) {
    		if (buildings.contains(u))
    			toSave.addConstruction(buildings.getConstruction(u));
    	}
    	
    	set("buildings", new JSONObject(gson.toJson(toSave)));
    	return this;
        
    }
    
    public LinkedHashMap<String, String> getFieldNames() {
    	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    	
        map.put("name", "Nome");
        map.put("buildings", "");
        
        return map;
    }
    
}
