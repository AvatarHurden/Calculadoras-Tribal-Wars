package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Properties;

import database.Mundo;

/**
 * Reads from the config file and stores separate parts for use
 * 
 * Includes:
 * - Mundo padrão
 * - Mundos
 * - ModeloTropas
 * 
 * 
 * @author Arthur
 *
 */
public class File_Reader {

	private static String mundoPadrão = "";
	private static String mundos = "";
	private static String modeloTropas = "";

	/**
	 * Reads the configuration file for the world informations
	 * If the file is corrupt, for any reason, use the default world configurations, which has the worlds
	 * lauched until the time of publishing
	 */
	public static void read() {
	
		try {
			
			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new FileReader(new File("config.txt")));
		
			process(in);
			
		// in case the file is corrupt, for any reason (thus we generalize the exception), we use
		// the default file
		} catch (IOException | NullPointerException e) {
			
			mundoPadrão = "";
			mundos = "";
			modeloTropas = "";
			
			// TODO criar diálogo para avisar corrupção
			
			System.out.println("O arquivo salvo está corrompido");
			
			BufferedReader in = new BufferedReader(
						new InputStreamReader(File_Reader.class.getResourceAsStream("default_mundos")));
			
			try {
				process(in);
			} catch (IOException exc) {
				System.out.println("bugou geral");
			}
			 
		}
		
		// Usar o que foi conseguido nos readers adequados
		
		Mundo_Reader.read(mundos);
		
	}
	
	private static void process(BufferedReader in) throws IOException {
		
		// Find "mundopadrão"
		
		String s = "";
		
		do {
			s = in.readLine();
			
			if (s != null) {
				
				if (s.contains("Mundo_Padrão="))
					mundoPadrão = s;
				
				if (s.contains("Mundos {")){
					
					while ((s = in.readLine()) == null || !s.contains("}"))
						mundos += s+"\n";
						
				}
				
				if (s.contains("Modelos_de_Tropas {")){
					
					while ((s = in.readLine()) == null || !s.contains("}"))
						modeloTropas += s+"\n";
						
				}
				
				
			}
			
		} while (!retrievalComplete());
		
//		while ((s = in.readLine()) == null)
//			in.readLine();
//		
//		mundoPadrão = s;
//		
//		// Find "mundos"
//		
//		s = "";
//		
//		while ((s = in.readLine()) == null && !s.contains("Mundos {"))
//			in.readLine();
//		
//		while ((s = in.readLine()) != null && !s.contains("}")){
//			System.out.println(mundos);
//			mundos += s+"\n";
//		}
//		
//		// Find "modeloTropas"
//		
//		s = "";
//				
//		while ((s = in.readLine()) != null && !s.contains("Modelos_de_Tropas {"))
//			in.readLine();
//				
//		while ((s = in.readLine()) != null && !s.contains("}"))
//			mundos += s+"\n";
//		
		
		
		in.close();
		
		
		
	}

	
	private static boolean retrievalComplete() {
		
		if (mundoPadrão != "" && mundos != "" && modeloTropas != "")
			return true;
		else
			return false;
		
		
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
