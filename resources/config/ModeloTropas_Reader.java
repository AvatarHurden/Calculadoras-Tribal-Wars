package config;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import database.ModeloTropas;

/**
 * Class that reads the troop models file and creates objects for each model
 * 
 * @author Arthur
 * 
 */
public class ModeloTropas_Reader {
	
	// Lista de modelos ativos no mundo
	static List<ModeloTropas> listModelosAtivos = new ArrayList<ModeloTropas>();

	// Lista de todos os modelos disponíveis
	static List<ModeloTropas> listModelos = new ArrayList<ModeloTropas>();
	
	public static void read(String section) {

		try {

			// read the user-alterable config file
			Scanner in = new Scanner(new StringReader(section));

			store(in);

			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (IOException e) {
			System.out.println("bugou geral");
		}

	}

	private static void store(Scanner in) throws IOException {

		String total = "";

		// reads the lines to gather all the properties of each world, running
		// once per world
		// breaks once there are no more worlds to read
		while (in.hasNextLine()) {

			String s;
			total += in.nextLine()+"\n";

			// reads the lines to gather all of the properties, breaking once
			// the line
			// contains no more properties (i.e. the world will change)
			while (in.hasNextLine()) {
				
				s = in.nextLine().trim();
				if (!s.equals(""))
					total += s + "\n";
				else
					break;
			}

			Properties i = new Properties();
			i.load(new StringReader(total));
			
			if (!i.isEmpty()) {
				ModeloTropas modelo = new ModeloTropas(i);
				addModelo(modelo);
			}
		}

		in.close();

	}

	
	/**
	 * Caso o ModeloTropas fornecido estiver no escopo atual (Global ou no mundo selecionado),
	 * adiciona-o à lista. Caso contrário, não faz nada.
	 * 
	 * @param modelo a ser adicionado
	 */
	public static void addModelo(ModeloTropas modelo) {
		
		listModelos.add(modelo);
		
		if (modelo.getEscopo().contains(Mundo_Reader.MundoSelecionado))
			listModelosAtivos.add(modelo);

	}
	
	/**
	 * Passa por todos os modelos existentes, colocando os que tiverem escopo
	 * na lista de ativos
	 */
	public static void checkAtivos() {
		
		for (ModeloTropas modelo : listModelos)
			if (modelo.getEscopo().contains(Mundo_Reader.MundoSelecionado) && !listModelosAtivos.contains(modelo))
				listModelosAtivos.add(modelo);
		
	}

	/**
	 * Retorna uma lista dos modelos ativos no mundo
	 */
	public static List<ModeloTropas> getListModelos() {
		return listModelos;
	}
	
	public static List<ModeloTropas> getListModelosAtivos() {
		return listModelosAtivos;
	}
	
	public static String getModelosConfig() {
		
		// Provavelmente temporário, preciso porque passo os ativos para EditDialog
		for (ModeloTropas i : listModelosAtivos)
			if (!listModelos.contains(i))
				listModelos.add(i);
		
		String section = "";

		for (ModeloTropas i : listModelos)
			section += i.getConfigText();
		
		return section;
		
	}
	
}
