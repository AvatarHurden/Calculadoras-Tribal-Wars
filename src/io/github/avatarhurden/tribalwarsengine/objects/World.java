package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.enums.ResearchSystem;
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
     * Construtor que gera o mundo com todos os padroes
     */
    public World() {
        this(new JSONObject("{}"));
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

    public boolean isPaladinWorld() {
        return (boolean) get("paladin", false);
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

    public boolean isBetterItemsWorld() {
        return (boolean) get("betteritems", false);
    }

    public boolean isCoiningWorld() {
        return (boolean) get("coining", false);
    }

    public double getWorldSpeed() {
        return (Double) get("speed", 1.0);
    }

    public double getUnitModifier() {
        return (Double) get("unit_modifier", 1.0);
    }

    public ResearchSystem getResearchSystem() {
        return ResearchSystem.ConvertInteger((Integer) get("researchsystem", 0));
    }

    public String getName() {
        return (String) get("name", "BRXX");
    }

    public Object getCustomProp(String chave, Object def) {
        return get(chave, def);
    }

    /* SETTERS com self return*/
    public World setMilitiaWorld(boolean boo) {
    	this.set("militia", boo);
    	return this;
    }
    
    public World setArcherWorld(boolean boo) {
        this.set("archer", boo);
        return this;
    }

    public World setPaladinWorld(boolean boo) {
        this.set("paladin", boo);
        return this;
    }

    public World setChurchWorld(boolean boo) {
        this.set("church", boo);
        return this;
    }

    public World setMoralWorld(boolean boo) {
        this.set("moral", boo);
        return this;
    }

    public World setFlogWorld(boolean boo) {
        this.set("flag", boo);
        return this;
    }

    // MADI BOO
    public World setNightBonusWorld(boolean boo) {
        this.set("nightbonus", boo);
        return this;
    }

    public World setBetterItemsWorld(boolean boo) {
        this.set("betteritems", boo);
        return this;
    }

    public World setCoiningWorld(boolean boo) {
        this.set("coining", boo);
        return this;
    }

    public World setWorldSpeed(double speed) {
        this.set("speed", speed);
        return this;
    }

    public World setUnitModifier(double unitModifier) {
        this.set("unit_modifier", unitModifier);
        return this;
    }

    public World setResearchSystem(ResearchSystem ss) {
        this.set("researchsystem", ss.getResearch());
        return this;
    }

    public World setName(String name) {
        this.set("name", name);
        return this;
    }
}
