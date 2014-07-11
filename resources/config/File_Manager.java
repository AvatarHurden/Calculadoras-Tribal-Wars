package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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
	private static String modeloAldeias = "";

	/**
	 * Reads the configuration file for the world informations If the file is
	 * corrupt, for any reason, use the default world configurations, which has
	 * the worlds lauched until the time of publishing
	 */
	public static void read() {
		
		// Limpa as vari�veis para ter certeza que n�o haver� valores duplicados
		mundoPadr�o = "";
		mundos = "";
		modeloTropas = "";
		modeloAldeias = "";
		
		try {

			File config = new File("configura��es.txt");

			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new FileReader(config));

			process(in);
			
			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (Exception e) {

			mundoPadr�o = "";
			mundos = "";
			modeloTropas = "";
			modeloAldeias = "";

			// TODO criar di�logo para avisar corrup��o
			// TODO mostrar onde no arquivo est� o erro
			// TODO carregar tudo que for poss�vel na letura, identificando erros

			System.out.println("O arquivo salvo est� corrompido");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					File_Manager.class.getResourceAsStream("default_c" +
							"onfig")));

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
				
				if (s.contains("Modelos_de_Aldeias {")) {
					
					while ((s = in.readLine()) == null || !s.contains("}"))
						modeloAldeias += s + "\n";
					
				}

			}

		} while (s != null);

		in.close();

	}


	public static void defineMundos() {

		Mundo_Reader.read(mundos);

	}

	public static void defineModelos() {

		ModeloTropas_Reader.read(modeloTropas);
		ModeloAldeias_Reader.read(modeloAldeias);

	}

	public static void save() {

		try {

			File configura��es = new File(
					"configura��es.txt");

			// In case the file does not exist, create it
			if (!configura��es.exists())
				configura��es.createNewFile();

			FileWriter out = new FileWriter(configura��es);
			
			// avisos ao usu�rio
			
			out.write("=========== AVISO ===========\n\n");
			out.write("Apenas edite esse arquivo se souber o que est� fazendo.\n");
			out.write("Altera��es podem corromper o arquivo e n�o permitir a abertura do programa.\n");
			out.write("Caso isso ocorra, delete esse arquivo.\n\n");
			out.write("=============================\n\n");
			
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

			out.write(ModeloTropas_Reader.getModelosConfig());

			out.write("\n");
			out.write("}");
			out.write("\n");
			
			// save ModeloAldeias
			
			out.write("\n");
			out.write("Modelos_de_Aldeias {");
			out.write("\n");

			out.write(ModeloAldeias_Reader.getModelosConfig());

			out.write("\n");
			out.write("}");
			out.write("\n");
			

			out.close();

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
	
	public static String getModeloAldeias() {
		return modeloAldeias;
	}

}
