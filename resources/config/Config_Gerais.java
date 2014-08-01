package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Lida com todas as informações gerais sobre a ferramenta, independemente do servidor escolhido.
 * <br> Atualmente, gerencia as seguintes configurações:
 * <br>- O que fazer quando o botão de fechar é clicado (coloca no tray ou fecha)
 * 
 * @author Arthur
 *
 */
public class Config_Gerais {
	
	// Closing actions
	private static boolean onCloseDefined = false;
	private static boolean onClose;
	
	// Alert table configurations
	private static int[] columnOrder = new int[8];
	private static int[] columWidth = new int[8];
	
	/**
	 * Reads the configuration file for the program. If it is corrupt, uses default configurations
	 * and creates a new valid file
	 */
	public static void read() {
		
		// Limpa as variáveis para ter certeza que não haverá valores duplicados
		
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
				
				if (s.contains("OrdemColuna=")) {
					String temp = s.replace("OrdemColuna=", "");
					String[] itens = temp.replaceAll("\\[", "").replaceAll("\\]", "").split(", "); 
					
					
					for (int i = 0; i < itens.length; i++)
						columnOrder[i] = Integer.parseInt(itens[i]);
					
				}
				
				if (s.contains("TamanhoColuna=")) {
					String temp = s.replace("TamanhoColuna=", "");
					String[] itens = temp.replaceAll("\\[", "").replaceAll("\\]", "").split(", "); 
					
					
					for (int i = 0; i < itens.length; i++)
						columWidth[i] = Integer.parseInt(itens[i]);
					
				}
					
				
			}

		} while (s != null);

		in.close();

	}
	
	public static void save() {

		try {
			
			File configurações = new File("TWEconfig.txt");

			Files.setAttribute(configurações.toPath(), "dos:hidden", false);
			
			// In case the file does not exist, create it
			if (!configurações.exists())
				configurações.createNewFile();

			FileWriter out = new FileWriter(configurações);
			
			// avisos ao usuário
			
			out.write("=========== AVISO ===========\n\n");
			out.write("Apenas edite esse arquivo se souber o que está fazendo.\n");
			out.write("Alterações podem corromper o arquivo e não permitir a abertura do programa.\n");
			out.write("Caso isso ocorra, delete esse arquivo.\n\n");
			out.write("=============================\n\n");
			
			// Writes close operation
			
			out.write("XFechaDefined=" + onCloseDefined + "\n");
			
			if (onCloseDefined)
				out.write("XFecha=" + onClose + "\n");
			
			out.write("\n");
			
			out.write("OrdemColuna=" + Arrays.toString(columnOrder) + "\n");
			out.write("TamanhoColuna=" + Arrays.toString(columWidth) + "\n");
			
			Files.setAttribute(configurações.toPath(), "dos:hidden", true);
			
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
		
		if (onCloseDefined)
			return onClose;
		else
			throw new Exception();
	}
	
	public static void setOnClose(boolean bool) {
		onClose = bool;
		onCloseDefined = true;
	}
	
	/**
	 * Returns the width of every column in the AlertTable class, ordered by row Index
	 * @return int[]
	 */
	public static int[] getColumnWidths() {
		return columWidth;
	}
	
	/**
	 * Returns the order of every column in the AlertTable class, where the number indicates the modelView
	 * @return int[]
	 */
	public static int[] getColumnOrder() {
		return columnOrder;
	}
	
	public static void setColumnWidths(int[] widths){
		columWidth = widths;
	}
	
	public static void setColumnOrder(int[] order){
		columnOrder = order;
	}

}
