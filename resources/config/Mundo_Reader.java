package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import database.Mundo;
//import java.util.Properties;

public class Mundo_Reader {

	// Lista das propriedades lidas
	static List<Properties> propertyList = new ArrayList<Properties>();
	
	// Lista dos mundos
	static List<Mundo> mundoList = new ArrayList<Mundo>();
	
	// Mundo Selecionado
	public static Mundo MundoSelecionado;
	
	public static void read(String section) {
	
		try {
			
			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new StringReader(section));
		
			store(in);
			
		// in case the file is corrupt, for any reason (thus we generalize the exception), we use
		// the default file
		} catch (IOException e) {
				System.out.println("bugou geral");
		}
		
	}

	/**
	 * Stores the read information to the worldList
	 * 
	 * @param in
	 * @throws IOException 
	 */
	private static void store(BufferedReader in) throws IOException{
		
		String total = "";
		
		// reads the lines to gather all the properties of each world, running once per world
		// breaks once there are no more worlds to read
		while ((total = in.readLine()) != null) {
		
			String s;
			total +="\n";
		
			// reads the lines to gather all of the properties, breaking once the line
			// contains no more properties (i.e. the world will change)
			while ((s = in.readLine()) != null) {
				if (!s.equals(""))
					total += s+"\n";
				else
					break;
			}
		
			Properties i = new Properties();
			i.load(new StringReader(total));
			propertyList.add(i);
			
			Mundo mundo = new Mundo();
			mundo.setAll(i);
			mundoList.add(mundo);
		}
		
		in.close();
		
		
	}
	
	/**
	 * Saves the configurations into the config files, creating the file if it does not exist
	 */
	public static String getMundosConfig() {
		
		String section ="";
		
		for (Mundo i : mundoList)
			section += i.getConfigText();
			
		return section;	
			
	}
	
	public static void setMundoSelecionado(Mundo mundo) {
		
		MundoSelecionado = mundo;
		
		MundoSelecionado.setTemposDeProdução();
		
		MundoSelecionado.setUnidadeList();
		
	}
	
	public static Properties getProperties(int index) { return propertyList.get(index); }

	public static List<Properties> getPropertiesList() { return propertyList; }
	
	public static Mundo getMundo(int index) { return mundoList.get(index); }
	
	public static List<Mundo> getMundoList() { return mundoList; }
	
}
