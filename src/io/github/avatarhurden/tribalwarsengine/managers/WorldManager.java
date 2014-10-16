package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.components.ProgressStatus;
import io.github.avatarhurden.tribalwarsengine.enums.Server;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.objects.World;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class WorldManager {

	private static WorldManager instance;
	private ServerFileManager downloader;
	
	private List<World> worlds;
	private World selectedWorld;
	
	public static void initialize(ProgressStatus bar) {
		instance = new WorldManager();
		instance.loadConfigs(bar);
	}
	
	public static WorldManager get() {
		if (instance == null)
			instance = new WorldManager();
		return instance;
	}
	
	private WorldManager() {
		downloader = new ServerFileManager();
	}
	
	private void loadConfigs(ProgressStatus bar) {
		Server server = Server.getServer(Configuration.get().getConfig("server", "br"));
		Configuration.get().setConfig("server", server.getName());
		
		bar.setMessage("Baixando lista de Mundos");
		bar.setProgress(20);
		
		JSONArray worldList;
		try {
			worldList = downloader.getServerConfigOnline(server);
		} catch (Exception e) {
			worldList = downloader.tryGetServerConfigLocal(server);
		}
		
		worlds = new ArrayList<World>();
		for (int i = 0; i < worldList.length(); i++) {
			bar.listIncrement(i, worldList.length(), 100);
			bar.setMessage("Carregando " + worldList.getJSONObject(i).getString("name"));
			worlds.add(new World(worldList.getJSONObject(i)));
		}
		
		bar.endSubProgress();
	}
	
	public World getDefaultWorld() {
		String def = Configuration.get().getConfig("default_world_br", "");
			if (def.equals(""))
				return worlds.get(0);
	        return getWorldByName(def);
	}
	
	public void setDefaultWorld(World world) {
		Configuration.get().setConfig("default_world_br", world.getName());
	}
	
    public World getWorldByName(String name) {
        for (World server : worlds) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }
    
    public void setSelectedWorld(World world) {
    	selectedWorld = world;
    }
    
    public void setSelectedWorld(String world) {
    	setSelectedWorld(getWorldByName(world));
    }
    
    public static World getSelectedWorld() {
    	if (get().selectedWorld == null)
    		get().selectedWorld = get().getDefaultWorld();
    	return get().selectedWorld;
    }
    
    public List<World> getList() {
    	return worlds;
    }
}
