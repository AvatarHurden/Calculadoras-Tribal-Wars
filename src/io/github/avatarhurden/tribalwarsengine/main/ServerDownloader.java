package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServerDownloader {
	
	private final String configFunction = "/interface.php?func=get_config";
	private final String unitFunction = "/interface.php?func=get_unit_info";
	private final String buildingFunction = "/interface.php?func=get_building_info";
	
	private final String basicFile = "/basicConfig.json";
	private final String configFile = "/config.json";
	private final String unitFile = "/unit_info.json";
	private final String buildingFile = "/building_info.json";
	
	private final String villageModelFile = "/villageModels.json";
	private final String armyModelFile = "/armyModels.json";
	
	private String folder;
	private JSONObject json;
	
	public ServerDownloader(String parentFolder, JSONObject json) {
		this.json = json;
	
		folder = parentFolder + "/" + json.getString("name");
		if (!new File(folder).exists())
			new File(folder).mkdir();
	}
	
	public JSONObject getBasicConfig() throws Exception {
		JSONObject object = JSON.getJSON(folder+basicFile);
		return object;
	}
	
	// World info section
	
	public JSONObject getServerWorldConfig() {
		try {
			return getConfigLocal();
		} catch (Exception e) {
			return tryGetConfigOnline();
		}
	}
	
	private JSONObject getConfigLocal() throws Exception {
		JSONObject object = JSON.getJSON(folder+configFile);
		
		return object;
	}
	
	private JSONObject tryGetConfigOnline() {
		try {
			return getConfigOnline();
		} catch (Exception e) {
			displayErrorMessageAndExit();
			return null;
		}
	}
	
	private JSONObject getConfigOnline() throws Exception {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.parse(new URL(json.getString("address")+configFunction).openStream());
		
		JSONObject worldJson = getJSONFromNode(doc.getDocumentElement());
		saveWorldConfig(worldJson);
		
		return worldJson;
	}
	
	private JSONObject getJSONFromNode(Node node) {
		
		JSONObject json = new JSONObject();
		NodeList children = node.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getChildNodes().getLength() == 1)
					json.put(child.getNodeName(), child.getTextContent().trim());
				else
					json.put(child.getNodeName(), getJSONFromNode(child));
			}
		}
		
		return json;
	}
	
	// Unit info section
	
	public JSONArray getServerUnitConfig() {
		try {
			return getLocalUnitConfig();
		} catch (Exception e) {
			return tryGetOnlineUnitConfig();
		}
	}
	
	private JSONArray getLocalUnitConfig() throws Exception {
		JSONArray array = JSON.getJSON(folder+unitFile).getJSONArray("units");
		
		return array;
	}
	
	private JSONArray tryGetOnlineUnitConfig() {
		try {
			return getOnlineUnitConfig();
		} catch (Exception e) {
			displayErrorMessageAndExit();
			return null;
		}
	}
	
	private JSONArray getOnlineUnitConfig() throws Exception {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.parse(new URL(json.getString("address")+unitFunction).openStream());
		
		JSONArray array = getJSONArrayFromNode(doc.getDocumentElement());
		
		saveUnitConfig(array);
		
		return array;
	}
	
	private JSONArray getJSONArrayFromNode(Node node) {
		JSONArray json = new JSONArray();
		NodeList children = node.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
			JSONObject item = getJSONFromNode(children.item(i));
			item.put("name", children.item(i).getNodeName().trim());
			json.put(item);
			}
		}
		
		return json;
	}
	
	// Building info section
	
	public JSONArray getServerBuildingConfig() {
		try {
			return getLocalBuildingConfig();
		} catch (Exception e) {
			return tryGetOnlineBuildingConfig();
		}
	}
	
	private JSONArray getLocalBuildingConfig() throws Exception {
		JSONArray array = JSON.getJSON(folder+buildingFile).getJSONArray("buildings");
		
		return array;
	}
	
	private JSONArray tryGetOnlineBuildingConfig() {
		try {
			return getOnlineBuildingConfig();
		} catch (Exception e) {
			displayErrorMessageAndExit();
			return null;
		}
	}
	
	private JSONArray getOnlineBuildingConfig() throws Exception {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.parse(new URL(json.getString("address")+buildingFunction).openStream());
		
		JSONArray array = getJSONArrayFromNode(doc.getDocumentElement());
		
		saveBuildingConfig(array);
		
		return array;
	}
	
	// Village model section
	
	public JSONArray getServerVillageModels() {
		try {
		File file = new File(folder+villageModelFile);
		if (!file.exists())
			saveVillageModelConfig(new JSONArray());
			
		return JSON.getJSON(file).getJSONArray("villageModels");
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	public JSONArray getServerArmyModels() {
		try {
		File file = new File(folder+armyModelFile);
		if (!file.exists())
			saveArmyModelConfig(new JSONArray());
			
		return JSON.getJSON(file).getJSONArray("armyModels");
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	private void displayErrorMessageAndExit() {

		String dialogText = "Não foi possível obter informações dos servidores.\n\nPor favor verifique sua conexão com a Internet e tente novamente.";
		String dialogName = "Erro de Conexão";
		
        JOptionPane.showMessageDialog(MainWindow.getInstance(), dialogText, dialogName, JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
	
	public void saveBasicConfig(JSONObject json) {
		try {
			File config = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), folder+basicFile);
			JSON.createJSONFile(json, config);
		} catch (Exception e) {}
	}
	
	private void saveWorldConfig(JSONObject json) {
		try {
			File config = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), folder+configFile);
			JSON.createJSONFile(json, config);
		} catch (Exception e) {}
	}
	
	private void saveUnitConfig(JSONArray json) {
		try {
			JSONObject unitObject = new JSONObject();
			unitObject.put("units", json);
			File config = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), folder+unitFile);
			JSON.createJSONFile(unitObject, config);
		} catch (Exception e) {}
	}
	
	public void saveBuildingConfig(JSONArray building) {
		try {
			JSONObject buildingObject = new JSONObject();
			buildingObject.put("buildings", building);	
			File buildingConfig = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), folder+buildingFile);
			JSON.createJSONFile(buildingObject, buildingConfig);
		} catch (Exception e) {}
	}
	
	public void saveArmyModelConfig(JSONArray json) {
		JSONObject obj = new JSONObject();
		obj.put("armyModels", json);
		
		try {
			JSON.createJSONFile(obj, new File(folder+armyModelFile));
		} catch (Exception e) {}
	}
	
	public void saveVillageModelConfig(JSONArray json) {
		JSONObject obj = new JSONObject();
		obj.put("villageModels", json);
		
		try {
			JSON.createJSONFile(obj, new File(folder+villageModelFile));
		} catch (Exception e) {}
	}
	
}
