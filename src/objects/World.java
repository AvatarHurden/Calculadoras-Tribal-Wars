package objects;

import org.json.JSONObject;

/**
 * Classe que representa as configura��es de um mundo.
 */
public class World {

    private JSONObject json;

    public World(JSONObject json) {
        this.json = json;
    }

    /**
     * Retorna uma informa��o especifica sobreo mundo
     *
     * @param chave - Nome da informa��o
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
