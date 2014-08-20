package io.github.avatarhurden.tribalwarsengine.panels;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWEComboBox;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.main.Main;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.World;
import io.github.avatarhurden.tribalwarsengine.tools.EditDialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import config.Lang;

@SuppressWarnings("serial")
public class SelectWorldPanel extends JPanel implements ActionListener {

    private TWEComboBox selectionBox;
    private TWButton startButton;
    private TWSimpleButton padrãoButton;
    private TWSimpleButton editButton;

    private SelectWorldFrame selectWorldFrame;

    /**
     * JPanel com um comboBox para escolher o mundo e um botão para iniciar o frame de ferramentas
     *
     * @param selectWorldFrame Frame em que será inserido
     */
    public SelectWorldPanel(SelectWorldFrame selectWorldFrame) {

        this.selectWorldFrame = selectWorldFrame;

        int height = selectWorldFrame.informationTable.getPreferredSize().height - 80;
        
        setOpaque(false);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{300};
        layout.rowHeights = new int[]{height / 2, 40, 40, height / 2};
        layout.columnWeights = new double[]{0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;

        selectionBox = new TWEComboBox();

        setSelectionBox();

        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        add(selectionBox, constraints);
        
        padrãoButton = new TWSimpleButton(Lang.BtnPadrao.toString());

        padrãoButton.addActionListener(this);
        padrãoButton.setActionCommand("default_button");

        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        add(padrãoButton, constraints);

        editButton = new TWSimpleButton(Lang.BtnEditar.toString());

        editButton.addActionListener(this);
        editButton.setActionCommand("edit_button");

        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        add(editButton, constraints);

        startButton = new TWButton(Lang.BtnIniciar.toString());

        startButton.addActionListener(this);
        startButton.setActionCommand("start_button");

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(startButton, constraints);

        changePadrãoButton();

        editButton.setFocusable(false);

        startButton.requestFocusInWindow();

    }

    private void setSelectionBox() {
        WorldManager worldManager = WorldManager.get();
        worldManager.setAddItens(selectionBox);
    }

    private void changePadrãoButton() {
        WorldManager worldManager = WorldManager.get();

        if (worldManager.getDefaultWorld() == worldManager.getDefaultWorld()) {
            padrãoButton.setEnabled(false);
        } else {
            padrãoButton.setEnabled(true);
        }
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "edit_button":
            	
            	try {
                    new EditDialog(WorldManager.get().getGenericList(), WorldManager.get().getFieldNames(),
                    		selectionBox.getSelectedIndex(), new World());
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                }

                setSelectionBox();
                selectWorldFrame.updateWorldInfoPanel();
                break;
            case "start_button":
                Main.openMainFrame();
                break;
            case "default_button":
                WorldManager.get().setDefaultWorldSelected();
                changePadrãoButton();
                break;
        }
    }
}
