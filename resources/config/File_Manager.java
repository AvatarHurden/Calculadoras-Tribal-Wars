package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

/**
 * Reads from the config file and stores separate parts for use
 * 
 * Includes: - Mundo padr�o - Mundos - ModeloTropas
 * 
 * @author Arthur
 * 
 */
public class File_Manager {

	private static String mundoPadr�o = "";
	private static String mundos = "";
	private static String modeloTropas = "";

	/**
	 * Reads the configuration file for the world informations If the file is
	 * corrupt, for any reason, use the default world configurations, which has
	 * the worlds lauched until the time of publishing
	 */
	public static void read() {

		try {

			File config = new File("configura��es_CalculadoraTribalWars.txt");

			Files.setAttribute(config.toPath(), "dos:hidden", false);

			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new FileReader(config));

			process(in);

			Files.setAttribute(config.toPath(), "dos:hidden", true);

			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (IOException | NullPointerException e) {

			mundoPadr�o = "";
			mundos = "";
			modeloTropas = "";

			// TODO criar di�logo para avisar corrup��o

			System.out.println("O arquivo salvo est� corrompido");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					File_Manager.class.getResourceAsStream("default_config")));

			try {
				process(in);
			} catch (IOException exc) {
				System.out.println("bugou geral");
			}

		}

	}

	private static void process(BufferedReader in) throws IOException {

		String s = "";

		do {
			s = in.readLine();

			if (s != null) {

				if (s.contains("Mundo_Padr�o="))
					mundoPadr�o = s.replace("Mundo_Padr�o=", "");

				if (s.contains("Mundos {")) {

					while ((s = in.readLine()) == null || !s.contains("}"))
						mundos += s + "\n";

				}

				if (s.contains("Modelos_de_Tropas {")) {

					while ((s = in.readLine()) == null || !s.contains("}"))
						modeloTropas += s + "\n";
					

				}

			}

		} while (!retrievalComplete());

		in.close();

	}

	private static boolean retrievalComplete() {

		if (mundoPadr�o == "" || mundos == "" || modeloTropas == "")
			return false;
		else
			return true;

	}

	public static void defineMundos() {

		Mundo_Reader.read(mundos);

	}

	public static void defineModelos() {

		ModeloTropas_Reader.read(modeloTropas);

	}

	public static void save() {

		try {

			File configura��es = new File(
					"configura��es_CalculadoraTribalWars.txt");

			// In case the file does not exist, create it
			if (!configura��es.exists())
				configura��es.createNewFile();

			Files.setAttribute(configura��es.toPath(), "dos:hidden", false);
			
			FileWriter out = new FileWriter(configura��es);

			// save mundoPadr�o

			out.write("\n");
			out.write("Mundo_Padr�o=" + mundoPadr�o);
			out.write("\n");

			// save Mundos

			out.write("\n");
			out.write("Mundos {");
			out.write("\n");

			out.write(Mundo_Reader.getMundosConfig());

			out.write("\n");
			out.write("}");
			out.write("\n");

			// save ModeloTropas

			out.write("\n");
			out.write("Modelos_de_Tropas {");
			out.write("\n");

			// TODO add modelo mesmo

			out.write(ModeloTropas_Reader.getModelosConfig());

			out.write("\n");
			out.write("}");
			out.write("\n");

			out.close();

			Files.setAttribute(configura��es.toPath(), "dos:hidden", true);

		} catch (IOException e) {

			e.printStackTrace();
				
			System.out.println("Could not save");

		}

	}

	public static void setMundoPadr�o(String s) {
		mundoPadr�o = s;
	}

	public static String getMundoPadr�o() {
		return mundoPadr�o;
	}

	public static String getMundos() {
		return mundos;
	}

	public static String getModeloTropas() {
		return modeloTropas;
	}

}
