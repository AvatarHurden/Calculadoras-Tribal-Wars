package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.enums.SearchSystem;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe que representa as configurações de um mundo.
 */
public class World {

    private JSONObject json;

    /**
     * @param json - Quando cria um objeto World, atribui a ele as propriedades de um objeto World que estavam salvo
     *             em consigurações. Quando estiver criando um World do zero, basta enviar Null como argumento, que ira "gerar" um
     *             mundo com as configuções padroes.
     */
    public World(JSONObject json) {
        this.json = json;
    }

    /**
     * Retorna uma informação especifica sobre o mundo
     *
     * @param chave - Nome da informação
     * @return
     */
    public Object get(String chave, Object def) {
        try {
            return json.get(chave);
        } catch (JSONException e) {
            return def;
        }
    }

    public void set(String chave, Object valor) {
        this.set(chave, valor, false);
    }

    /**
     * Altera a configuração de um mundo
     *
     * @param chave
     * @param valor -
     * @param save  - is to save or not
     */
    public void set(String chave, Object valor, boolean save) {
        json.put(chave, valor);

        if (save) {
            Configuration config = Configuration.get();
            JSONObject worlds = config.getConfig("worlds", new JSONObject("{}"));
            worlds.put(json.getString("name"), this.json);
            config.setConfig("worlds", worlds);
        }
    }

    /**
     * Retorna o JSON referente a este mundo!
     *
     * @return json
     */
    public JSONObject getJson() {
        return json;
    }

    /* METODOS DE RETORNO DE PROPRIEDADOS DO OBJETO */

    public boolean isMilitiaWorld() {
        return (boolean) get("militia", false); //Esse cast pode causar uma exceptions caso alguem modifique as configurações manualmente.
    }

    /* GETTERS */

    public boolean isArcherWorld() {
        return (boolean) get("archer", false);
    }

    public boolean isPaladinoWorld() {
        return (boolean) get("paladino", false);
    }

    public boolean isChurchWorld() {
        return (boolean) get("paladino", false);
    }

    public boolean isMoralWorld() {
        return (boolean) get("moral", false);
    }

    public boolean isFlagWorld() {
        return (boolean) get("flag", false);
    }

    public boolean isNightBonusWorld() {
        return (boolean) get("nightbonus", false);
    }

    public boolean isBetterItensWorld() {
        return (boolean) get("betterworld", false);
    }

    public boolean isCoiningWorld() {
        return (boolean) get("coining", false);
    }

    public int getWorldSpeed() {
        return (Integer) get("speed", 1);
    }

    public int getUnitModifier() {
        return (Integer) get("unit_modifier", 1);
    }

    public SearchSystem getSearchSystem() {
        return SearchSystem.ConvertInteger((Integer) get("searchsystem", 0));
    }

    public String getName() {
        return (String) get("name", "BRXX");
    }

    /* SETTERS */
}
