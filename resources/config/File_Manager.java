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
 * Includes: - Mundo padrão - Mundos - ModeloTropas
 * 
 * @author Arthur
 * 
 */
public class File_Manager {

	private static String mundoPadrão = "";
	private static String mundos = "";
	private static String modeloTropas = "";

	/**
	 * Reads the configuration file for the world informations If the file is
	 * corrupt, for any reason, use the default world configurations, which has
	 * the worlds lauched until the time of publishing
	 */
	public static void read() {

		try {

			File config = new File("configurações_CalculadoraTribalWars.txt");

			Files.setAttribute(config.toPath(), "dos:hidden", false);

			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new FileReader(config));

			process(in);

			Files.setAttribute(config.toPath(), "dos:hidden", true);

			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (IOException | NullPointerException e) {

			mundoPadrão = "";
			mundos = "";
			modeloTropas = "";

			// TODO criar diálogo para avisar corrupção

			System.out.println("O arquivo salvo está corrompido");

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

				if (s.contains("Mundo_Padrão="))
					mundoPadrão = s.replace("Mundo_Padrão=", "");

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

		if (mundoPadrão == "" || mundos == "" || modeloTropas == "")
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

			File configurações = new File(
					"configurações_CalculadoraTribalWars.txt");

			// In case the file does not exist, create it
			if (!configurações.exists())
				configurações.createNewFile();

			Files.setAttribute(configurações.toPath(), "dos:hidden", false);
			
			FileWriter out = new FileWriter(configurações);

			// save mundoPadrão

			out.write("\n");
			out.write("Mundo_Padrão=" + mundoPadrão);
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

			Files.setAttribute(configurações.toPath(), "dos:hidden", true);

		} catch (IOException e) {

			e.printStackTrace();
				
			System.out.println("Could not save");

		}

	}

	public static void setMundoPadrão(String s) {
		mundoPadrão = s;
	}

	public static String getMundoPadrão() {
		return mundoPadrão;
	}

	public static String getMundos() {
		return mundos;
	}

	public static String getModeloTropas() {
		return modeloTropas;
	}

}
