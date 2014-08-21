package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.objects.ArmyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArmyModelManager {

    private List<ArmyModel> models = new ArrayList<ArmyModel>();
    private List<ArmyModel> activeModels = new ArrayList<ArmyModel>();
    private Configuration config = Configuration.get();

    private static ArmyModelManager instance;

    /* Poderia usar o metodo safe, mais quero manter sempre a mesma instancia do manager no projeto para que seja tudo
    sincronomo */
    public static ArmyModelManager get() {
        if (instance == null) {
            instance = new ArmyModelManager();
        }
        return instance;
    }

    public ArmyModelManager() {
        load();
    }

    /**
     * Converte cada objeto JSON salvo nas configurações em um objeto World
     */
    public void load() {
        //Pega o objeto Worlds, que contem todos os mundos
        JSONArray models = config.getArmyModelConfig();

        for (int i = 0; i < models.length(); i++)
            add(new ArmyModel(models.getJSONObject(i)));
        
        setActives();
    }

    /**
     * Adiciona modelos
     *
     * @param VillageModel
     */
    public void add(ArmyModel model) {
        if (!models.contains(model))
            models.add(model);
    }

    public List<ArmyModel> getList() {
        return models;
    }
    
    public List<ArmyModel> getActiveList() {
        return activeModels;
    }
    
    public LinkedHashMap<String, String> getFieldNames() {
    	
    	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    	
        map.put("name", "Nome");
        map.put("army", "");
        map.put("scope", "");
        
        return map;
    }

    /**
     * Salva todos os mundo no arquivo de configuração
     */
    public void save() {
        JSONArray models = new JSONArray();

        for (ArmyModel model : this.models) {
            JSONObject j = model.getJson();

            models.put(j);
        }
        config.setConfig("armyModels", models);
    }
    
    public void setActives() {
    	
    	 // Zera a lista para checagem
        activeModels.removeAll(activeModels);

        for (ArmyModel modelo : models)
            if (modelo.getScope().contains(WorldManager.get().getSelectedWorld()))
            	activeModels.add(modelo);
            else
            	activeModels.remove(modelo);
    	
    }
 
}
