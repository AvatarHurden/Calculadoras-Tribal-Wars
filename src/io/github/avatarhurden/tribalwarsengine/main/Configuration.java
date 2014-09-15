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
	
	public static final String folder = "config";
    public static final String worldFolder = "config/servers";
    public static final String alertFolder = "config/alerts";
	private static final String configString = "config/config.json";
    

    private static Configuration instance;
    //Armazena as configurações
    private JSONObject config;
    private File configFile;
    private JFrame frame = MainWindow.getInstance();

    /**
     * Só é possivel usar esta classe atravez do objeto estatico.
     */
    private Configuration() {
    	configFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), configString);
    	
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
        	
        	createFolders();
        	
            JSON.createJSONFile(config, configFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Não foi possivel salvar o arquivo de configuração.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadConfigJSON() {
        try {
        	createFolders();
        	
            if (!configFile.exists())
            	JSON.createJSONFile(new JSONObject("{}"), configFile);
            
            config = JSON.getJSON(configFile);
           
        } catch (IOException e) {
            JOptionPane.showMessageDialog( frame, "Não foi possível carregar o arquivo de configurações.\n Favor reiniciar o programa.", "Erro", JOptionPane.ERROR_MESSAGE);
            config = new JSONObject("{}");
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

    /**
     * Setter unico para valores
     *
     * @param chave - Chave do keyset
     * @param valor - Objeto qualquer
     */
    public void setConfig(String chave, Object valor) {
        try {
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
    
    private void createFolders() {
    	if (!new File(folder).exists())
    		new File(folder).mkdir();
    	
    	if (!new File(worldFolder).exists())
    		new File(worldFolder).mkdir();
    	
    	if (!new File(alertFolder).exists())
    		new File(alertFolder).mkdir();
    }
}
