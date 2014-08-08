package main;

import frames.MainWindow;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe responsavel por manipular as configura��es baseadas em JSON
 *
 * @author Wesley Nascimento
 * @date 08/08/2014
 */
public class Configuration {

    private static final String configFile = "config.json";
    private static final Configuration instance = new Configuration();
    //Armazena as configura��es
    private JSONObject config;
    private File file;
    private JFrame frame = MainWindow.getInstance();

    /**
     * S� � possivel usar esta classe atravez do objeto estatico.
     */
    private Configuration() {
        file = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), configFile);
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
        return instance;
    }

    /**
     *
     */
    public void savaConfigJSON() {
        try {
            JSON.createJSONFile(config, file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "N�o foi possivel salvar o arquivo de configura��o.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadConfigJSON() {
        try {
            //Se n�o existe, pega o padr�o e o cria!
            if (!file.exists()) {
                JSONObject defaultJson = JSON.getJSON(this.getClass().getResource("/config/default_config.json").getFile());
                JSON.createJSONFile(defaultJson, file);
            }
            config = JSON.getJSON(file);
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog( frame, "N�o foi possivel criar um arquivo de configura��o.\nSuas modifica��es n�o ser�o visiveis no proximo uso.", "Erro", JOptionPane.ERROR_MESSAGE);
            config = new JSONObject("{}");
            return;
        }
    }

    public JSONObject getConfig() {
        return config;
    }

    /**
     * Getter padr�o de configura��es.
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
     * Retorna uma String de configura��o
     *
     * @param chave - A chave do subobjeto
     * @param def   - Valor padr�o, caso n�o tenha nada salvo!
     * @return String -
     */
    public String getConfig(String chave, String def) {
        try {
            return config.getString(chave);
        }
        //Retorna o padr�o, caso n�o encontre o valor no arquivo
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
            savaConfigJSON();
        }
    }
}
