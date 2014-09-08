package io.github.avatarhurden.tribalwarsengine.managers;

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
	
	public JSONArray getServerJSON(String folder, String url) {
		try {
			return getServerOnline(folder, url);
		} catch (Exception e) {
			e.printStackTrace();
			return tryGetServerLocal(folder);
		}
	}
	
	private JSONArray getServerOnline(String folder, String url) throws Exception{
		
		URLConnection conn = new URL(url+phpFunction).openConnection();
		
		if (new File(folder+fileName).exists()) {
			conn.setConnectTimeout(2 * 1000);
			conn.setReadTimeout(10 * 1000);
		}
		conn.connect();
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder builder = new StringBuilder();
		
		int next;
		while ((next = reader.read()) != -1)
			builder.append((char) next);
		
		JSONArray array = decodeOnlineText(builder.toString());
		
		saveServerJSON("br", array);
		
		return array;
	}
	
	private JSONArray tryGetServerLocal(String folder) {
		try {
			return getServerLocal(folder);
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
			JSON.createJSONFile(object, servers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
