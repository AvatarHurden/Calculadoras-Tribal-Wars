package objects;

import org.json.JSONObject;

/**
 * Classe que representa as configurações de um mundo.
 */
public class World {

    private JSONObject json;

    public World(JSONObject json) {
        this.json = json;
    }

    /**
     * Retorna uma informação especifica sobreo mundo
     *
     * @param chave - Nome da informação
     * @return
     */
    public Object get(String chave) {
        return null;
    }

    public void set(String chave, Object valor) {

    }

    /**
     * Retorna o JSON referente a este mundo!
     *
     * @return json
     */
    public JSONObject getJson() {
        return json;
    }
}
