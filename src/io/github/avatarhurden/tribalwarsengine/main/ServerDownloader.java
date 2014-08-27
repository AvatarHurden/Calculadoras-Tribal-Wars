package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.objects.TWServer;
import io.github.avatarhurden.tribalwarsengine.objects.World;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServerDownloader {
	
	private final String configFunction = "/interface.php?func=get_config";
	private final String unitFunction = "/interface.php?func=get_unit_info";
	private final String buildingFunction = "/interface.php?func=get_building_info";
	
	private final String configFile = "/config.json";
	private final String unitFile = "/unit_info.json";
	private final String buildingFile = "/building_info.json";
	
	private String folder;
	private JSONObject json;
	
	public ServerDownloader(String parentFolder, JSONObject json) {
		this.json = json;
	
		folder = parentFolder + "/" + json.getString("name");
	}
	
	public TWServer getServerWorldConfig() {
		try {
			return getConfigLocal();
		} catch (Exception e) {
			return tryGetConfigOnline();
		}
	}
	
	private TWServer getConfigLocal() throws Exception {
		
		JSONObject object = JSON.getJSON(folder+configFile);
		
		return new TWServer().setWorld(new World(object));
	}
	
	private TWServer getConfigOnline() throws Exception {
		
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document doc = docBuilder.parse(new URL(json.getString("address")+configFunction).openStream());
		
		JSONObject worldJson = getJSONFromNode(doc.getDocumentElement());
		worldJson.put("name", json.getString("name"));
		
		return new TWServer().setWorld(new World(worldJson));
	}
	
	private TWServer tryGetConfigOnline() {
		try {
			return getConfigOnline();
		} catch (Exception e) {
			displayErrorMessageAndExit();
			return new TWServer();
		}
	}
	
	private void displayErrorMessageAndExit() {

		String dialogText = "Não foi possível obter informações dos servidores.\n\nPor favor verifique sua conexão com a Internet e tente novamente.";
		String dialogName = "Erro de Conexão";
		
        JOptionPane.showMessageDialog(MainWindow.getInstance(), dialogText, dialogName, JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
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
	
	public void saveWorldJSON(World world) {
		
		if (!new File(folder).exists())
			new File(folder).mkdir();
		
		try {
			File config = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), folder+configFile);
			JSON.createJSONFile(world.getJson(), config);
		} catch (Exception e) {
		}
	}
	
}
