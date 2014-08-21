package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class VillageModel implements EditableObject {
	
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
	
		setStartingValues();
	}
	
	private void setStartingValues() {
		
		setName("Novo Modelo");
		setBuildings(new Buildings());
		setScope(new Scope());
		
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
    
    public Buildings getBuildings() {
    	return gson.fromJson(get("buildings", "").toString(), Buildings.class);
    }
    
    public Scope getScope() {
    	return gson.fromJson(get("scope", "").toString(), Scope.class);
    }
    
    // Setters
    
    public VillageModel setName(String name) {
    	set("name", name);
    	return this;
    }
    
    public VillageModel setBuildings(Buildings builds) {
    	set("buildings", new JSONObject(gson.toJson(builds)));
    	return this;
    }
    
    public VillageModel setScope(Scope escopo) {
    	set("scope", new JSONObject(gson.toJson(escopo)));
    	return this;
    }
    
}
