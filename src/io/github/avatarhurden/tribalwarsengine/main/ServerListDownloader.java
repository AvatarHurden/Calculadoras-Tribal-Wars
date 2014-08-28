package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerListDownloader {

	private final String phpFunction = "/backend/get_servers.php";
	private final String fileName = "/servers.json";
	
	public JSONArray getServerJSON(String folder, String url) {
		try {
			return getServerOnline(url);
		} catch (Exception e) {
			return tryGetServerLocal(folder);
		}
	}
	
	private JSONArray getServerOnline(String url) throws Exception{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new URL(url+phpFunction).openStream()));
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
			displayErrorMessageAndExit();
			return new JSONArray();
		}
	}
	
	private JSONArray getServerLocal(String folder) throws Exception{
		
		JSONObject object = JSON.getJSON(folder+fileName);
		
		return object.getJSONArray("servers");
	}
	
	private void displayErrorMessageAndExit() {

		String dialogText = "Não foi possível obter informações dos servidores.\n\nPor favor verifique sua conexão com a Internet e tente novamente.";
		String dialogName = "Erro de Conexão";
		
        JOptionPane.showMessageDialog(MainWindow.getInstance(), dialogText, dialogName, JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
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
		object.put("servers", json);
		
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
