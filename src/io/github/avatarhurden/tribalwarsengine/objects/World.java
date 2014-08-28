package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.enums.ResearchSystem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe que representa as configurações de um mundo.
 */
public class World implements EditableObject {

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
        
        setStartingValues();
    }
    
    private void setStartingValues() {
    	
    	setName("Novo Mundo");
    	setWorldSpeed(1.0);
    	setUnitModifier(1.0);
    	setArcherWorld(false);
    	setFlagWorld(false);
    	setMoralWorld(false);
    	setChurchWorld(false);
    	setPaladinWorld(false);
    	setCoiningWorld(false);
    	setMilitiaWorld(false);
    	setBetterItemsWorld(false);
    	setNightBonusWorld(false);
    	setResearchSystem(ResearchSystem.SIMPLE);
    	
    }

    /**
     * Retorna uma informação especifica sobre o mundo
     *
     * @param chave - Nome da informação
     * @return
     */
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
    	 return (json.getJSONObject("game").getString("church").equals("0")) ? false : true;
    }

    /* GETTERS */
    public boolean isArcherWorld() {
    	 return (json.getJSONObject("game").getString("archer").equals("0")) ? false : true;
    }

    public boolean isPaladinWorld() {
    	 return (json.getJSONObject("game").getString("knight").equals("0")) ? false : true;
    }

    public boolean isChurchWorld() {
        return (json.getJSONObject("game").getString("church").equals("0")) ? false : true;
    }

    public boolean isMoralWorld() {
        return ((String) get("moral", "0")).equals("0") ? false : true;
    }

    public boolean isFlagWorld() {
        return (boolean) get("flag", false);
    }

    public boolean isNightBonusWorld() {
    	 return (json.getJSONObject("night").getString("active").equals("0")) ? false : true;
    }

    public boolean isBetterItemsWorld() {
    	 return (json.getJSONObject("game").getString("knight_new_items").equals("0")) ? false : true;
    }

    public boolean isCoiningWorld() {
    	 return (json.getJSONObject("snob").getString("gold").equals("0")) ? false : true;
    }

    public double getWorldSpeed() {
        return Double.parseDouble((String) get("speed", "1.0"));
    }

    public double getUnitModifier() {
        return Double.parseDouble((String) get("unit_speed", "1.0"));
    }

    public ResearchSystem getResearchSystem() {
    	switch (Integer.parseInt(json.getJSONObject("game").getString("tech"))) {
		case 0:
			return ResearchSystem.TEN_LEVELS;
		case 1:
			return ResearchSystem.THREE_LEVELS;
		case 2:
			return ResearchSystem.SIMPLE;
		default:
			return ResearchSystem.SIMPLE;
		}
    }

    public String getName() {
        return (String) get("name", "BRXX");
    }

    public Object getCustomProp(String chave, Object def) {
        return get(chave, def);
    }
    
    public String toString() {
    	return getName();
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

    public World setFlagWorld(boolean boo) {
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
