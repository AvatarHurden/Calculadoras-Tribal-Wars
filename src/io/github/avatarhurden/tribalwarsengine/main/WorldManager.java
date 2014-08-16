package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.objects.World;
import org.json.JSONObject;

import javax.swing.*;
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

    private static WorldManager instance;

    /* Poderia usar o metodo safe, mais quero manter sempre a mesma instancia do manager no projeto para que seja tudo
    sincronomo */
    public static WorldManager get() {
        if (instance == null) {
            instance = new WorldManager();
        }
        return instance;
    }

    public WorldManager() {
        load();
    }

    /**
     * Converte cada objeto JSON salvo nas configurações em um objeto World
     */
    public void load() {
        //Pega o objeto Worlds, que contem todos os mundos :)
        JSONObject worlds = config.getConfig("worlds", new JSONObject("{}"));

        Iterator<JSONObject> iterator = worlds.keys();

        while (iterator.hasNext()) {
            add(new World(iterator.next()));
        }

        //Server somente para debug
        if (this.worlds.size() < 1) {
            JSONObject teste = new JSONObject();
            teste.put("name", "Mundo de teste");
            add(new World(teste));

            teste = new JSONObject();
            teste.put("name", "Mundo de teste1");
            add(new World(teste));
        }
    }

    /**
     * Adiciona mundos ao Gerenciador de mundos
     *
     * @param world
     */
    public void add(World world) {
        //Preve mundo duplicados
        if (!worlds.contains(world)) {
            this.worlds.add(world);
        }
    }

    /**
     * Adiciona todos os Worlds em um comboBox
     *
     * @param combo - TWEComboBox ou Qualquer JComboBox
     */
    public void setAddItens(JComboBox combo) {
        combo.removeAllItems();

        for (World world : this.worlds) {
            combo.addItem(world);
        }

        World def = getDefaultWorld();
        if (worlds.size() > 0 && def != null) {
            combo.setSelectedItem(def);
        }
    }

    /**
     * Retorna null se não tiver um mundo padrão definido, ou se os mundo nao for encontrado no Manager
     *
     * @return world
     */
    public World getDefaultWorld() {
        String def = config.getConfig("default_world", "");
        if (def == null) return null;
        return getWorldByName(def);
    }

    public World getWorldByName(String name) {
        for (World world : worlds) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        return null;
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
