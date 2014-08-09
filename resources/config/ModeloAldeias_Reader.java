package config;

import database.ModeloAldeias;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Class that reads the troop models file and creates io.github.avatarhurden.tribalwarsengine.objects for each model
 *
 * @author Arthur
 */
public class ModeloAldeias_Reader {

    // Lista de modelos ativos no mundo
    static Set<ModeloAldeias> setModelosAtivos = new HashSet<ModeloAldeias>();

    // Lista de todos os modelos disponíveis
    static List<ModeloAldeias> listModelos = new ArrayList<ModeloAldeias>();

    public static void read(String section) {

        // Limpa os modelos existentes
        listModelos.removeAll(listModelos);
        setModelosAtivos.removeAll(setModelosAtivos);

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
            total += in.nextLine() + "\n";

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
                ModeloAldeias modelo = new ModeloAldeias(i);
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
    public static void addModelo(ModeloAldeias modelo) {

        listModelos.add(modelo);

        if (modelo.getEscopo().contains(Mundo_Reader.MundoSelecionado))
            setModelosAtivos.add(modelo);

    }

    /**
     * Passa por todos os modelos existentes, colocando os que tiverem escopo
     * na lista de ativos
     */
    public static void checkAtivos() {

        // Zera a lista para checagem
        setModelosAtivos.removeAll(setModelosAtivos);

        for (ModeloAldeias modelo : listModelos)
            if (modelo.getEscopo().contains(Mundo_Reader.MundoSelecionado))
                setModelosAtivos.add(modelo);
            else
                setModelosAtivos.remove(modelo);

    }

    /**
     * Retorna uma lista dos modelos ativos no mundo
     */
    public static List<ModeloAldeias> getListModelos() {
        return listModelos;
    }

    public static List<ModeloAldeias> getListModelosAtivos() {
        return new ArrayList<ModeloAldeias>(setModelosAtivos);
    }

    public static String getModelosConfig() {

        // Provavelmente temporário, preciso porque passo os ativos para EditDialog
        for (ModeloAldeias i : setModelosAtivos)
            if (!listModelos.contains(i))
                listModelos.add(i);

        String section = "";

        for (ModeloAldeias i : listModelos)
            section += i.getConfigText();

        return section;

    }

}