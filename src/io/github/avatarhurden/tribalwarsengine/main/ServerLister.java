package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerLister {

	private static final String phpFunction = "/backend/get_servers.php";
	private static final String fileName = "/servers.json";
	
	public static JSONArray getServerJSON(String folder, String url) {
		try {
			return getServerOnline(url);
		} catch (Exception e) {
			return tryGetServerLocal(folder);
		}
	}
	
	public static JSONArray tryGetServerOnline(String url) {
		try {
			return getServerOnline(url);
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	private static JSONArray getServerOnline(String url) throws Exception{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new URL(url+phpFunction).openStream()));
		StringBuilder builder = new StringBuilder();
		
		int next;
		while ((next = reader.read()) != -1)
			builder.append((char) next);
		
		return decodeOnlineText(builder.toString());
	}
	
	public static JSONArray tryGetServerLocal(String folder) {
		try {
			return getServerLocal(folder);
		} catch (Exception e) {
			displayErrorMessageAndExit();
			return new JSONArray();
		}
	}
	
	private static JSONArray getServerLocal(String folder) throws Exception{
		
		JSONObject object = JSON.getJSON(folder+fileName);
		
		return object.getJSONArray("servers");
	}
	
	private static void displayErrorMessageAndExit() {

		String dialogText = "Não foi possível obter informações dos servidores.\n\nPor favor verifique sua conexão com a Internet e tente novamente.";
		String dialogName = "Erro de Conexão";
		
        JOptionPane.showMessageDialog(MainWindow.getInstance(), dialogText, dialogName, JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
	
	private static JSONArray decodeOnlineText(String content) {
		
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
	
	public static void saveServerJSON(String folder, JSONArray json) {
		
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
