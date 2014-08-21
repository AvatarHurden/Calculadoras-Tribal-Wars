package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.objects.VillageModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Classe que armazena os modelos de aldeias
 * 
 * @author Arthur
 */
public class VillageModelManager {
	
    private List<VillageModel> models = new ArrayList<VillageModel>();
    private List<VillageModel> activeModels = new ArrayList<VillageModel>();
    private Configuration config = Configuration.get();

    private static VillageModelManager instance;

    /* Poderia usar o metodo safe, mais quero manter sempre a mesma instancia do manager no projeto para que seja tudo
    sincronomo */
    public static VillageModelManager get() {
        if (instance == null) {
            instance = new VillageModelManager();
        }
        return instance;
    }

    public VillageModelManager() {
        load();
    }

    /**
     * Converte cada objeto JSON salvo nas configurações em um objeto World
     */
    public void load() {
        //Pega o objeto Worlds, que contem todos os mundos
        JSONArray models = config.getVillageModelConfig();

        for (int i = 0; i < models.length(); i++)
            add(new VillageModel(models.getJSONObject(i)));

    }

    /**
     * Adiciona modelos
     *
     * @param VillageModel
     */
    public void add(VillageModel model) {
        if (!models.contains(model))
            models.add(model);
    }

    public List<VillageModel> getList() {
        return models;
    }
    
    public LinkedHashMap<String, String> getFieldNames() {
    	
    	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    	
        map.put("name", "Nome");
        map.put("buildings", "");
        map.put("scope", "");
        
        return map;
    }

    /**
     * Salva todos os mundo no arquivo de configuração
     */
    public void save() {
        JSONArray models = new JSONArray();

        for (VillageModel model : this.models) {
            JSONObject j = model.getJson();

            models.put(j);
        }
        config.setConfig("villageModels", models);
    }
    
    public void setActives() {
    	
   	 // Zera a lista para checagem
       activeModels.removeAll(activeModels);

       for (VillageModel modelo : models)
           if (modelo.getScope().contains(WorldManager.get().getSelectedWorld()))
           	activeModels.add(modelo);
           else
           	activeModels.remove(modelo);
   	
   }
 
}
