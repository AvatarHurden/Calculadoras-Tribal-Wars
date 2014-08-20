package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class VillageModel {
	
	private JSONObject json;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * Construtor com as informações do modelo já criadas. Para gerar um modelo vazio,
	 * usar o construtor sem parâmetros
	 * 
	 * @param json com as informações do modelo
	 */
	public VillageModel(JSONObject json) {
		this.json = json;
	}
	
	public VillageModel() {
		this(new JSONObject());
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
    
    // Getters
    
    public String getName() {
    	return (String) get("name", "");
    }
    
    public Buildings getBuildings() {
    	return gson.fromJson(get("buildings", "").toString(), Buildings.class);
    }
    
    // Setters
    
    public VillageModel setName(String name) {
    	set("name", name);
    	return this;
    }
    
    public VillageModel setArmy(Army army) {
    	set("buildings", new JSONObject(gson.toJson(army)));
    	return this;
    }
    
}
