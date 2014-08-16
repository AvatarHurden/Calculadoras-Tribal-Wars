package io.github.avatarhurden.tribalwarsengine.panels;

import database.Cores;
import io.github.avatarhurden.tribalwarsengine.main.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.World;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

@SuppressWarnings("serial")
public class WorldInfoPanel extends JPanel {
    private GridBagConstraints gbc;

    /**
     * Muda as informações da tabela de informações
     * As propriedades mostradas são escolinhas dinamicamente de acordo com o mundo!
     */
    public void changeProperties() {
        World world = WorldManager.get().getSelectWorld();

        removeAll();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{290};
        gridBagLayout.rowHeights = new int[]{};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        setOpaque(false);

        setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

        gbc = new GridBagConstraints();
        gbc.gridy = 0;

        addProp("Nome", world.getName());
        addProp("Velocidade", world.getWorldSpeed());
        addProp("Velocidade das unidades", world.getUnitModifier());
        addProp("Moral", world.isMoralWorld());
        addProp("Sistema de pesquisa", world.getSearchSystem().getName());
        addProp("Igreja", world.isChurchWorld());
        addProp("Bonus Noturno", world.isNightBonusWorld());
        addProp("Bandeiras", world.isFlagWorld());
        addProp("Archeiros", world.isArcherWorld());
        addProp("Paladino", world.isPaladinoWorld());
        addProp("Itens Aprimorados", world.isBetterItensWorld());
        addProp("Milícia", world.isMilitiaWorld());
        addProp("Cunhagem de moedas", world.isCoiningWorld());

        this.revalidate();
    }

    /**
     * Converte os valores para Strings que possam ser entendidas mais facilmente,
     * cria o subpanel e adiciona ao superPanel
     */
    public void addProp(String propName, Object value) {
        String propValue;

        //Se é um boleano
        if (value instanceof Boolean) {

            if ((Boolean) value) {
                propValue = "Ativado";
            } else {
                propValue = "Desativado";
            }
        }
        //Se é qualquer outra coisa!
        else {
            propValue = String.valueOf(value);
        }

        //Adiciona uma lina ao grid
        gbc.gridy++;

        add(createPropPanel(propName, propValue), gbc);
    }

    public JPanel createPropPanel(String propName, String propValue) {
        JPanel panel = new JPanel();
        Color bg = Cores.ALTERNAR_ESCURO;

        //Se for uma linha par, cor clara!
        if (gbc.gridy % 2 == 0) {
            bg = Cores.ALTERNAR_CLARO;
        }

        panel.setBackground(bg);

        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{150, 20, 140};
        gbl_panel.rowHeights = new int[]{20};
        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        panel.setLayout(gbl_panel);

        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(5, 5, 5, 5);

        JLabel lblName = new JLabel(propName);

        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        panel.add(lblName, gbc_panel);

        JLabel lblValue = new JLabel(propValue);

        gbc_panel.gridx = 2;
        gbc_panel.gridy = 0;
        panel.add(lblValue, gbc_panel);

        return panel;
    }
}