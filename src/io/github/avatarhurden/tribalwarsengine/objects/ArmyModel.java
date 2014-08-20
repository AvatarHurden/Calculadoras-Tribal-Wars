package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa modelos de exércitos
 * 
 * @author Arthur
 *
 */
public class ArmyModel implements EditableObject {

	private JSONObject json;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * Construtor com as informações do modelo já criadas. Para gerar um modelo vazio,
	 * usar o construtor sem parâmetros
	 * 
	 * @param json com as informações do modelo
	 */
	public ArmyModel(JSONObject json) {
		this.json = json;
	}
	
	public ArmyModel() {
		this(new JSONObject());
		
		setStartingValues();
	}
	
	private void setStartingValues() {
		
		setName("Novo Modelo");
		setArmy(new Army());
		
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
    
    public Army getArmy() {
    	return gson.fromJson(get("army", "").toString(), Army.class);
    }
    
    // Setters
    
    public ArmyModel setName(String name) {
    	set("name", name);
    	return this;
    }
    
    public ArmyModel setArmy(Army army) {
    	set("army", new JSONObject(gson.toJson(army)));
    	return this;
    }
	
}
