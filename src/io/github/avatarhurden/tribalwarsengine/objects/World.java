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
            save();
        }
    }

    /**
     * Salva este mundo no Objeto Worlds dentro do arquivo de configuração
     */
    public void save() {
        Configuration config = Configuration.get();

        JSONObject worlds = config.getConfig("worlds", new JSONObject("{}"));

        worlds.put(json.getString("name"), this.json);
        config.setConfig("worlds", worlds);
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
        return (boolean) get("church", false);
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

    public double getUnitModifier() {
        return (Double) get("unit_modifier", 1);
    }

    public SearchSystem getSearchSystem() {
        return SearchSystem.ConvertInteger((Integer) get("searchsystem", 0));
    }

    public String getName() {
        return (String) get("name", "BRXX");
    }

    public Object getCustomProp(String chave, Object def) {
        return get(chave, def);
    }

    /* SETTERS */
    public void setArcherWorld(boolean boo) {
        this.set("archer", boo);
    }

    public void setPaladinoWorld(boolean boo) {
        this.set("paladino", boo);
    }

    public void setChurchWorld(boolean boo) {
        this.set("church", boo);
    }

    public void setMoralWorld(boolean boo) {
        this.set("moral", boo);
    }

    public void setFlogWorld(boolean boo) {
        this.set("flag", boo);
    }

    // MADI BOO
    public void setNightBonusWorld(boolean boo) {
        this.set("nightbonus", boo);
    }

    public void setBetterItensWorld(boolean boo) {
        this.set("betterworld", boo);
    }

    public void setIsCoiningWorld(boolean boo) {
        this.set("coining", boo);
    }

    public void setWorldSpeed(int integer) {
        this.set("speed", integer);
    }

    public void setUnitModifier(double unitModifier) {
        this.set("unit_modifier", unitModifier);
    }

    public void setSearchSystem(SearchSystem ss) {
        this.set("searchsystem", ss.getResearch());
    }

    public void setName(String name) {
        this.set("name", name);
    }
}
