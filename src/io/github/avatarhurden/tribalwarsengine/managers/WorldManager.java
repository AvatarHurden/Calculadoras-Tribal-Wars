package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.components.TWEComboBox;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.objects.EditableObject;
import io.github.avatarhurden.tribalwarsengine.objects.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import database.Unidade;

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
        JSONArray worlds = config.getWorldConfig();

        for (int i = 0; i < worlds.length(); i++)
            add(new World(worlds.getJSONObject(i)));

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

    /**
     * Seta o mundo selecionado como padrão!
     */
    public void setDefaultWorldSelected() {
        setDefaultWorld(getSelectedWorld());
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
        if (def.equals(""))
            return worlds.get(0);
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
        if (worlds.contains(w))
        	return w;
    	return null;
    }

    public List<World> getList() {
        return worlds;
    }
    
    public List<? extends EditableObject> getGenericList() {
    	return (List<? extends EditableObject>) worlds;
    }
    
    public List<JSONObject> getJsonList() {
    	List<JSONObject> list = new ArrayList<JSONObject>();
    	
    	for (World w : worlds)
    		list.add(w.getJson());
    	
    	return list;
    }
    
    public LinkedHashMap<String, String> getFieldNames() {
    	
    	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    	
        map.put("name", "Nome");
        map.put("speed", "Velocidade");
        map.put("unit_modifier", "Modificador");
        map.put("moral", "Moral");
        map.put("researchsystem", "Sistema de Pesquisa");
        map.put("church", "Igreja");
        map.put("nightbonus", "Bônus Noturno");
        map.put("flag", "Bandeiras");
        map.put("archer", "Arqueiros");
        map.put("paladin", "Paladino");
        map.put("betteritems", "Itens Aprimorados");
        map.put("militia", "Milícia");
        map.put("coining", "Cunhagem de Moedas");
        
        return map;
    }

    /**
     * Salva todos os mundo no arquivo de configuração
     */
    public void save() {
        JSONArray worlds = new JSONArray();

        for (World world : this.worlds) {
            JSONObject j = world.getJson();
            worlds.put(j);
        }
        
        config.setConfig("worlds", worlds);
    }
    
    // Métodos relacionados ao mundo selecionado
    
    public World getSelectedWorld() {
        if (selectWorld == null) {
            selectWorld = getDefaultWorld();
        }
        return selectWorld;
    }

    public void setSelectedWorld(World world) {
        this.selectWorld = world;
        SelectWorldFrame selectWorldFrame = SelectWorldFrame.getInstance();
        if (selectWorldFrame != null) {
            selectWorldFrame.updateWorldInfoPanel();
        }
    }
    
    /**
     * Retorna uma lista ordenada com todas as unidades disponíveis no mundo
     * 
     * @return ArrayList com as unidades
     */
    public ArrayList<Unidade> getAvailableUnits() {
    	
    	ArrayList<Unidade> list = new ArrayList<Unidade>();
    	
    	for (Unidade u : Unidade.values())
    		list.add(u);
    	
    	if (!getSelectedWorld().isArcherWorld()) {
    		list.remove(Unidade.ARQUEIRO);
    		list.remove(Unidade.ARCOCAVALO);
    	}
    	if (!getSelectedWorld().isPaladinWorld())
    		list.remove(Unidade.PALADINO);
    	if (!getSelectedWorld().isMilitiaWorld())
    		list.remove(Unidade.MILÍCIA);
    	
    	return list;
    }
    
}
