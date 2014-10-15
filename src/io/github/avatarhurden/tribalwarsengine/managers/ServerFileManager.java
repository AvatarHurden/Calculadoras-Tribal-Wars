package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.enums.Server;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.main.JSON;
import io.github.avatarhurden.tribalwarsengine.main.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerFileManager {

	private final String phpFunction = "/backend/get_servers.php";
	private final String fileName = "/worlds.json";
	
	public JSONArray getServerJSON(Server server) {
		try {
			return getServerConfigOnline(server);
		} catch (Exception e) {
			return tryGetServerConfigLocal(server);
		}
	}
	
	public JSONArray getServerConfigOnline(Server server) throws Exception{
		
		URLConnection conn = new URL(server.getURL() + phpFunction).openConnection();
		
		if (new File("config/servers/" + server.getName() + fileName).exists()) {
			conn.setConnectTimeout(2 * 1000);
			conn.setReadTimeout(5 * 1000);
		}
		conn.connect();
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder builder = new StringBuilder();
		
		int next;
		while ((next = reader.read()) != -1)
			builder.append((char) next);
		
		JSONArray array = decodeOnlineText(builder.toString());
		
		saveServerJSON("config/servers/" + server.getName(), array);
		
		return array;
	}
	
	public JSONArray tryGetServerConfigLocal(Server server) {
		try {
			return getServerLocal("config/servers/" + server.getName());
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorMessageAndExit();
			return new JSONArray();
		}
	}
	
	private JSONArray getServerLocal(String folder) throws Exception{
		
		JSONObject object = JSON.getJSON(folder+fileName);
		
		return object.getJSONArray("worlds");
	}
	
	private void displayErrorMessageAndExit() {

		String dialogText = "Não foi possível obter informações dos servidores.\n\nPor favor verifique sua conexão com a Internet e tente novamente.";
		String dialogName = "Erro de Conexão";
		
        JOptionPane.showMessageDialog(MainWindow.getInstance(), dialogText, dialogName, JOptionPane.ERROR_MESSAGE);
		Main.exitProgram();;
	}
	
	private JSONArray decodeOnlineText(String content) {
		String formattedContent = content.replaceAll(".:\\d+:", "").replaceAll("\"", "");
		formattedContent = formattedContent.substring(1, formattedContent.length()-1);
		String[] values = formattedContent.split(";");

		JSONArray servers = new JSONArray();
		
		for (int i = 0; i < values.length - 1; i += 2) {
			JSONObject json = new JSONObject();
			json.put("name", values[i]);
			json.put("address", values[i+1]);
			servers.put(json);
		}
		
		return servers;
	}
	
	public void saveServerJSON(String folder, JSONArray json) {
		
		JSONObject object = new JSONObject();
		object.put("worlds", json);
		
		if (!new File(folder).exists())
			new File(folder).mkdir();
		
		try {
			File servers = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), folder+fileName);
			JSON.createJSONFile(object, servers, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
