package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.main.JSON;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;


public class AlertFileManager {
	
	private final String fileName = "/alerts.json";
	private final String pastFileName = "/pastAlerts.json";
	
	private String parentFolder;
	
	public AlertFileManager(String folder) {
		parentFolder = folder;
	}
	
	public void saveAlertList(JSONArray alerts) {
		String file = parentFolder + fileName;
		
		JSONObject json = new JSONObject();
		json.put("alerts", alerts);
		
		try {
			JSON.createJSONFile(json, new File(file));
		} catch (Exception e) {}
	}
	
	public JSONArray getAlertList() {
		String file = parentFolder + fileName;
		
		try {
			JSONObject json = JSON.getJSON(new File(file));
			return json.getJSONArray("alerts");
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	public void savePastAlertList(JSONArray alerts) {
		String file = parentFolder + pastFileName;
		
		JSONObject json = new JSONObject();
		json.put("alerts", alerts);
		
		try {
			JSON.createJSONFile(json, new File(file));
		} catch (Exception e) {}
	}
	
	public JSONArray getPastAlertList() {
		String file = parentFolder + pastFileName;
		
		try {
			JSONObject json = JSON.getJSON(new File(file));
			return json.getJSONArray("alerts");
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
}
