package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.components.TWEComboBox;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.objects.World;
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

    private World selectWorld;

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
            add(new World().setName("Teste 1"));
            add(new World().setName("Teste 2"));
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
    public void setAddItens(TWEComboBox combo) {
        combo.removeAllItems();

        for (World world : this.worlds) {
            combo.addItem(world);
        }

        World def = getDefaultWorld();
        if (worlds.size() > 0 && def != null) {
            combo.setSelectedItem(def);
        }
    }

    public World getSelectWorld() {
        if (selectWorld == null) {
            selectWorld = getDefaultWorld();
        }
        return selectWorld;
    }

    public void setSelectWorld(World world) {
        this.selectWorld = world;
        SelectWorldFrame selectWorldFrame = SelectWorldFrame.getInstance();
        if (selectWorldFrame != null) {
            selectWorldFrame.updateWorldInfoPanel();
        }
    }

    /**
     * Seta o mundo selecionado como padrão!
     */
    public void setSelectWorldSelected() {
        setDefaultWorld(getSelectWorld());
    }

    /**
     * Seta um mundo como padrão, só faz isso se esse mundo estiver na lista de mundo.
     *
     * @param world
     */
    public void setDefaultWorld(World world) {
        if (getWorld(world) != null) {
            config.setConfig("default_world", world.getName());
        }
    }

    /**
     * Retorna null se não tiver um mundo padrão definido, ou se os mundo nao for encontrado no Manager
     *
     * @return world
     */
    public World getDefaultWorld() {
        String def = config.getConfig("default_world", "");
        if (def == null) {
            return new World();
        }
        return getWorldByName(def);
    }

    /**
     * Procura por um World pelo nome
     *
     * @param name - O nome do mundo!
     * @return World ou null caso nao encontre
     */
    public World getWorldByName(String name) {
        for (World world : worlds) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        return null;
    }

    /**
     * Procura por um mundo na lista
     *
     * @return World ou null caso nao encontre
     */
    public World getWorld(World w) {
        for (World world : worlds) {
            if (world.equals(w)) {
                return world;
            }
        }
        return null;
    }

    public List<World> getList() {
        return worlds;
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
