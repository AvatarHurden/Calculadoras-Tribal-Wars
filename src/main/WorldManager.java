package main;

import objects.World;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Resposavel por armazenar os Objetos World e fazer as operações cabiveis
 * <p/>
 * Nunca manter duas instancias deste objeto no projeto.
 *
 * @author Wesley Nascimento
 * @date 08/08/2014
 */
public class WorldManager {

    private List<World> worlds = new ArrayList<World>();
    private Configuration config = Configuration.get();

    public WorldManager() {
        load();
    }

    /**
     * Carrega todos os mundo salvos no arquivo de configuração
     */
    public void load() {
        //Pega o objeto Worlds, que contem todos os mundos :)
        JSONObject worlds = config.getConfig("worlds", new JSONObject("{}"));

        Iterator<JSONObject> iterator = worlds.keys();

        while (iterator.hasNext()) {
            add(new World(iterator.next()));
        }
    }

    public void add(World world) {
        //Preve mundo duplicados
        if (!worlds.contains(world)) {
            this.worlds.add(world);
        }
    }

    /**
     * Salva todos os mundo no arquivo de configuração
     */
    public void save() {
        JSONObject worlds = config.getConfig("worlds", new JSONObject("{}"));

        String name;

        for (World world : this.worlds) {
            JSONObject j = world.getJson();

            name = j.getString("nome");
            worlds.put(name, j);
        }
        config.setConfig("worlds", worlds);
    }
}
