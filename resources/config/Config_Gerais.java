package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Lida com todas as informa��es gerais sobre a ferramenta, independemente do servidor escolhido.
 * <br> Atualmente, gerencia as seguintes configura��es:
 * <br>- O que fazer quando o bot�o de fechar � clicado (coloca no tray ou fecha)
 * 
 * @author Arthur
 *
 */
public class Config_Gerais {
	
	private static boolean onCloseDefined = false;
	private static boolean onClose;
	
	/**
	 * Reads the configuration file for the program. If it is corrupt, uses default configurations
	 * and creates a new valid file
	 */
	public static void read() {
		
		// Limpa as vari�veis para ter certeza que n�o haver� valores duplicados
		
		try {

			File config = new File("TWEconfig.txt");
			
			Files.setAttribute(config.toPath(), "dos:hidden", false);
			
			// read the user-alterable config file
			BufferedReader in = new BufferedReader(new FileReader(config));

			process(in);
			
			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (Exception e) {
			
		}

	}
	
	private static void process(BufferedReader in) throws IOException {

		String s = "";

		do {
			s = in.readLine();

			if (s != null) {
				
				if (s.contains("XFechaDefined="))
					onCloseDefined = Boolean.valueOf(s.replace("XFechaDefined=", ""));

				if (s.contains("XFecha="))
					onClose = Boolean.valueOf(s.replace("XFecha=", ""));

			}

		} while (s != null);

		in.close();

	}
	
	public static void save() {

		try {

			File configura��es = new File("TWEconfig.txt");

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
			
			// Writes close operation
			
			out.write("XFechaDefined=" + onCloseDefined + "\n");
			
			if (onCloseDefined)
				out.write("XFecha=" + onClose);
			
			Files.setAttribute(configura��es.toPath(), "dos:hidden", true);
			
			out.close();

		} catch (IOException e) {

			e.printStackTrace();
				
			System.out.println("Could not save");

		}

	}
	
	/**
	 * Returns what is to be done when the close button is pressed.
	 * If there is no saved configuration, throws an Exception;
	 * @return True if the program is to be closed. False if not.
	 * @throws Exception if there is no saved configuration.
	 */
	public static boolean getOnClose() throws Exception {
		
		System.out.println(onClose);
		
		if (onCloseDefined)
			return onClose;
		else
			throw new Exception();
	}
	
	public static void setOnClose(boolean bool) {
		onClose = bool;
		onCloseDefined = true;
	}

}
