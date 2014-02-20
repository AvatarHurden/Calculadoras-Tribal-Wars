package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import database.ModeloTropas;

/**
 * Class that reads the troop models file and creates objects for each model
 * 
 * @author Arthur
 * 
 */
public class ModeloTropas_Reader {

	static Map<String, ModeloTropas> mapModelos = new HashMap<String, ModeloTropas>();

	static List<ModeloTropas> listModelos = new ArrayList<ModeloTropas>();

	public static void read(String section) {

		try {

			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new StringReader(section));

			store(in);

			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (IOException e) {
			System.out.println("bugou geral");
		}

	}

	private static void store(BufferedReader in) throws IOException {

		String total = "";

		// reads the lines to gather all the properties of each world, running
		// once per world
		// breaks once there are no more worlds to read
		while ((total = in.readLine()) != null) {

			String s;
			total += "\n";

			// reads the lines to gather all of the properties, breaking once
			// the line
			// contains no more properties (i.e. the world will change)
			while ((s = in.readLine()) != null) {
				if (!s.equals(""))
					total += s + "\n";
				else
					break;
			}

			Properties i = new Properties();
			i.load(new StringReader(total));

			ModeloTropas modelo = new ModeloTropas(i);
			listModelos.add(modelo);
		}

		in.close();

		setMap();

	}

	public static void setMap() {

		for (ModeloTropas i : listModelos)
			mapModelos.put(i.getNome(), i);

	}

	public static void addModelo(ModeloTropas modelo) {

		listModelos.add(modelo);

		mapModelos.put(modelo.getNome(), modelo);

	}

	public static Map<String, ModeloTropas> getMapModelos() {
		return mapModelos;
	}

	public static List<ModeloTropas> getListModelos() {
		return listModelos;
	}
}
