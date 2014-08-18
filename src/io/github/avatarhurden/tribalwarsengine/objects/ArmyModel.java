package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe que representa modelos de ex�rcitos
 * 
 * @author Arthur
 *
 */
public class ArmyModel {

	private JSONObject json;
	
	/**
	 * Construtor com as informa��es do modelo j� criadas. Para gerar um modelo vazio,
	 * usar o construtor sem par�metros
	 * 
	 * @param json com as informa��es do modelo
	 */
	public ArmyModel(JSONObject json) {
		this.json = json;
	}
	
	public ArmyModel() {
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
    
    // Getters
    
    public String getName() {
    	return (String) get("name", "");
    }
    
    public Army getArmy() {
    	return (Army) get("army", null);
    }
    
    // Setters
    
    public ArmyModel setName(String name) {
    	set("name", name);
    	return this;
    }
    
    public ArmyModel setArmy(Army army) {
    	set("army", army);
    	return this;
    }
	
}
