package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock.BuildingsEditPanel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import config.Lang;

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

        JButton resetButton = new TWSimpleButton(Lang.BtnResetar.toString());
        resetButton.addActionListener(listener);

        resetPanel.add(resetButton);

        return resetPanel;

    }

    /**
     * Cria um panel com um botão com o nome "Reset" que, quando clicado, faz a ação dada
     *
     * @param listener Ação a ser feita no clique
     * @return Um Panel com o botão
     */
    public JPanel addResetPanel(ActionListener action) {

        return makeResetPanel(action);

    }
    
    /**
     * Cria um panel com um botão que, quando clicado, cria um novo Alert com as informações
     * presentes na ferramenta
     * 
     * @param datelbl com a data do alerta
     * @param origem com a aldeia de origem do alerta
     * @param destino com a aldeia de destino do alerta
     * @param tropas mapa ligando as tropas aos seus textFields
     * @return Um JPanel com o botão
     */
    public JPanel addAlertCreatorPanel(TimeFormattedJLabel datelbl, CoordenadaPanel origem,
    		CoordenadaPanel destino, Map<Unit, IntegerFormattedTextField> tropas) {
    	
    	JPanel panel = new JPanel();
    	panel.setOpaque(false);
    	
    	panel.add(new AlertCreatorPanel(datelbl, origem, destino, tropas));
    	
    	return panel;
    }

    /**
     * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, além de editá-los
     *
     * @param edit       Se o painel possui o botão de edição
     * @param textFields Map com IntegerFormattedTextField ligados a unidades, para utilizar os Modelos
     * @return Um JPanel com as coisas faladas
     */
    public JPanel addModelosTropasPanel(boolean edit, ArmyEditPanel... armyPanel) {

        ModeloTropasPanel panel = new ModeloTropasPanel(edit, this, armyPanel);

        tropasPanelList.add(panel);

        return panel;
    }

    /**
     * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, além de editá-los
     *
     * @param edit       Se o painel possui o botão de edição
     * @param textFields Map com IntegerFormattedTextField ligados a unidades, para utilizar os Modelos
     * @return Um JPanel com as coisas faladas
     */
    public JPanel addModelosAldeiasPanel(boolean edit, BuildingsEditPanel buildings) {

        ModeloAldeiasPanel panel = new ModeloAldeiasPanel(edit, buildings, this);

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
