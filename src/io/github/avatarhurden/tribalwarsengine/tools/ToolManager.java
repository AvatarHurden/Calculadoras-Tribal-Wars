package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.Edif�cioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import config.Lang;
import database.Edif�cio;
import database.Unidade;

/**
 * Panel on top of every ferramenta with the following functionality:
 * <br>- Reset the values
 * <br>- Apply and edit ModeloTropas
 *
 * @author Arthur
 */
public class ToolManager {

    // The class will contain a panel for every function, and the caller will
    // decide where each one will be placed
    private JPanel bandeiraPanel;

    private List<ModeloTropasPanel> tropasPanelList = new ArrayList<ModeloTropasPanel>();
    private List<ModeloAldeiasPanel> aldeiasPanelList = new ArrayList<ModeloAldeiasPanel>();

    public ToolManager() {
    }

    private JPanel makeResetPanel(ActionListener listener) {

        JPanel resetPanel = new JPanel();
        resetPanel.setOpaque(false);

        JButton resetButton = new JButton(Lang.BtnResetar.toString());
        resetButton.addActionListener(listener);

        resetPanel.add(resetButton);

        return resetPanel;

    }

    /**
     * Cria um panel com um bot�o com o nome "Reset" que, quando clicado, faz a a��o dada
     *
     * @param listener A��o a ser feita no clique
     * @return Um Panel com o bot�o
     */
    public JPanel addResetPanel(ActionListener action) {

        return makeResetPanel(action);

    }

    /**
     * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, al�m de edit�-los
     *
     * @param edit       Se o painel possui o bot�o de edi��o
     * @param textFields Map com IntegerFormattedTextField ligados a unidades, para utilizar os Modelos
     * @return Um JPanel com as coisas faladas
     */
    public JPanel addModelosTropasPanel(boolean edit, Map<Unidade, IntegerFormattedTextField> textFields) {

        ModeloTropasPanel panel = new ModeloTropasPanel(edit, textFields, this);

        tropasPanelList.add(panel);

        return panel;

    }

    /**
     * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, al�m de edit�-los
     *
     * @param edit       Se o painel possui o bot�o de edi��o
     * @param textFields Map com IntegerFormattedTextField ligados a unidades, para utilizar os Modelos
     * @return Um JPanel com as coisas faladas
     */
    public JPanel addModelosAldeiasPanel(boolean edit,
                                         Map<Edif�cio, Edif�cioFormattedTextField> textFields, CoordenadaPanel coord) {

        ModeloAldeiasPanel panel = new ModeloAldeiasPanel(edit, textFields, coord, this);

        aldeiasPanelList.add(panel);

        return panel;

    }

    /**
     * Refreshes the option menu to reflect all changes that might have occurred
     */
    public void refresh() {

        for (ModeloTropasPanel i : tropasPanelList)
            i.makePopupMenu();

        for (ModeloAldeiasPanel i : aldeiasPanelList)
            i.makePopupMenu();

    }

}
