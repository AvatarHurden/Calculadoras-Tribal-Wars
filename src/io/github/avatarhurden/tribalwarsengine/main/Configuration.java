package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe responsavel por manipular as configurações baseadas em JSON
 *
 * @author Wesley Nascimento
 * @date 08/08/2014
 */
public class Configuration {
	
	private static final String villageModelString = "villageModels.json";
	private static final String armyModelString = "armyModels.json";
	
    private static final String configString = "config.json";

    private static Configuration instance;
    //Armazena as configurações
    private JSONObject config, villageConfig, armyConfig;
    private File configFile, villageFile, armyFile;
    private JFrame frame = MainWindow.getInstance();

    /**
     * Só é possivel usar esta classe atravez do objeto estatico.
     */
    private Configuration() {
    	configFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), configString);
    	villageFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), villageModelString);
    	armyFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), armyModelString);
        
        loadConfigJSON();
    }

    /**
     * Retorna este objeto
     *
     * @return instance - O objeto
     */
    public static Configuration getInstance() {
        return get();
    }

    /**
     * Retorna este objeto
     *
     * @return instance - O objeto
     */
    public static Configuration get() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
     *
     */
    public void salvaConfigJSON() {
        try {
            JSON.createJSONFile(config, configFile);
            JSON.createJSONFile(villageConfig, villageFile);
            JSON.createJSONFile(armyConfig, armyFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Não foi possivel salvar o arquivo de configuração.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadConfigJSON() {
        try {
        	
            if (!configFile.exists())
            	JSON.createJSONFile(new JSONObject("{}"), configFile);
            if (!villageFile.exists())
            	JSON.createJSONFile(new JSONObject().put("villageModels", new JSONArray()), villageFile);
            if (!armyFile.exists())
            	JSON.createJSONFile(new JSONObject().put("armyModels", new JSONArray()), armyFile);
            
            config = JSON.getJSON(configFile);
            armyConfig = JSON.getJSON(armyFile);
            villageConfig = JSON.getJSON(villageFile);
            
            } catch (IOException e) {
            JOptionPane.showMessageDialog( frame, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            config = new JSONObject("{}");
            armyConfig = new JSONObject("{}");
            villageConfig = new JSONObject("{}");
            return;
        }
    }

    public JSONObject getConfig() {
        return config;
    }

    /**
     * Getter padrão de configurações.
     *
     * @param chave - Chave do subobjeto
     * @return Objeto - Um objeto java
     */
    public Object getConfig(String chave) {
        try {
            return config.getJSONObject(chave);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Retorna uma String de configuração
     *
     * @param chave - A chave do subobjeto
     * @param def   - Valor padrão, caso não tenha nada salvo!
     * @return String -
     */
    public String getConfig(String chave, String def) {
        try {
            return config.getString(chave);
        }
        //Retorna o padrão, caso não encontre o valor no arquivo
        catch (JSONException e) {
            return def;
        }
    }

    public double getConfig(String chave, double def) {
        try {
            return config.getDouble(chave);
        } catch (JSONException e) {
            return def;
        }
    }

    public int getConfig(String chave, int def) {
        try {
            return config.getInt(chave);
        } catch (JSONException e) {
            return def;
        }
    }

    public boolean getConfig(String chave, boolean def) {
        try {
            return config.getBoolean(chave);
        } catch (JSONException e) {
            return def;
        }
    }

    public JSONObject getConfig(String chave, JSONObject def) {
        try {
            return config.getJSONObject(chave);
        } catch (JSONException e) {
            return def;
        }
    }
    
    public JSONArray getConfig(String chave, JSONArray def) {
    	try {
    		return config.getJSONArray(chave);
    	} catch (JSONException e) {
    		return def;
    	}
    }

    
    public JSONArray getVillageModelConfig() {
    	return villageConfig.getJSONArray("villageModels");
    }
    
    public JSONArray getArmyModelConfig() {
    	return armyConfig.getJSONArray("armyModels");
    }

    /**
     * Setter unico para valores
     *
     * @param chave - Chave do keyset
     * @param valor - Objeto qualquer
     */
    public void setConfig(String chave, Object valor) {
        try {
        	if (chave.equals("villageModels"))
        		villageConfig.put(chave, valor);
        	else if (chave.equals("armyModels"))
        		armyConfig.put(chave, valor);
        	else
            config.put(chave, valor);
        }
        //Faz nada se der erro :(
        catch (JSONException e) {

        }
        //salva o arquivo :)
        finally {
            salvaConfigJSON();
        }
    }
}
