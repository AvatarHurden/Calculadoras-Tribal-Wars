package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.main.ServerListDownloader;
import io.github.avatarhurden.tribalwarsengine.objects.TWServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;

public class ServerManager {

	private static ServerManager instance;
	private ServerListDownloader downloader;
	
	private List<TWServer> servers;
	private TWServer selectedServer;
	
	public static ServerManager get() {
		if (instance == null)
			instance = new ServerManager();
		return instance;
	}
	
	private ServerManager() {
		downloader = new ServerListDownloader();
		loadConfigs();
	}
	
	private void loadConfigs() {
		JSONArray json = downloader.getServerJSON("br", "http://tribalwars.com.br");
		servers = new ArrayList<TWServer>();
		for (int i = 0; i < json.length(); i++)
			servers.add(new TWServer(json.getJSONObject(i)));
	}
	
	public TWServer getDefaultServer() {
		String def = Configuration.get().getConfig("default_server_br", "");
			if (def.equals(""))
				return servers.get(0);
	        return getServerByName(def);
	}
	
	public void setDefaultServer(TWServer server) {
		Configuration.get().setConfig("default_server_br", server.getName());
	}
	
    public TWServer getServerByName(String name) {
        for (TWServer server : servers) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }
    
    public void setSelectedServer(TWServer server) {
    	selectedServer = server;
    	selectedServer.setInfo();
    }
    
    public void setSelectedServer(String server) {
    	setSelectedServer(getServerByName(server));
    }
    
    public TWServer getSelectedServer() {
    	if (selectedServer == null)
    		selectedServer = getDefaultServer();
    	return selectedServer;
    }
    
    public List<TWServer> getList() {
    	return servers;
    }
}
