package io.github.avatarhurden.tribalwarsengine.panels;

import config.File_Manager;
import config.Lang;
import config.Mundo_Reader;
import database.Mundo;
import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.main.Main;
import io.github.avatarhurden.tribalwarsengine.tools.EditDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class SelectWorldPanel extends JPanel implements ActionListener {

    private JComboBox<String> selectionBox;
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

        selectionBox = new JComboBox<String>();

        setSelectionBox();

        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        add(selectionBox, constraints);

        padrãoButton = new TWSimpleButton(Lang.BtnPadrao.toString());

        padrãoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                // Define o mundo padrão a ser usado
                File_Manager.setMundoPadrão(selectionBox.getSelectedItem().toString());

                changePadrãoButton();
            }
        });

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
        selectionBox.setFocusable(false);
        selectionBox.setFocusable(true);

        startButton.requestFocusInWindow();

    }

    private void setSelectionBox() {

        // Adiciona o nome de todos os mundos para a lista que será utilizada
        // no comboBox
        for (Mundo m : Mundo_Reader.getMundoList()) {
            selectionBox.addItem(m.toString());
        }

        selectionBox.setSelectedItem(File_Manager.getMundoPadrão());
       /*
        selectionBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {

                // Cada vez que o mundo selecionado é alterado, altera as informações da tabela

            }
        });
        */
        selectionBox.addActionListener(this);
        selectionBox.setActionCommand("select_box");
    }

    private void changePadrãoButton() {

        if (selectionBox.getSelectedItem() != null && selectionBox.getSelectedItem().toString()
                .equals(File_Manager.getMundoPadrão()))
            padrãoButton.setEnabled(false);
        else
            padrãoButton.setEnabled(true);

    }

    /**
     * @return The selected index of the comboBox
     */
    public int getSelectedIndex() {

        return selectionBox.getSelectedIndex();

    }

    public JButton getStartButton() {
        return startButton;
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "select_box":
                selectWorldFrame.changeInformationPanel();
                changePadrãoButton();
                break;
            case "edit_button":

                try {
                    new EditDialog(Mundo.class, Mundo_Reader.getMundoList(), "variableList", getSelectedIndex(), null);
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                }

                selectionBox.removeItemListener(selectionBox.getItemListeners()[0]);
                selectionBox.removeAllItems();
                setSelectionBox();
                selectWorldFrame.changeInformationPanel();
                break;
            case "start_button":
                Mundo_Reader.setMundoSelecionado(
                        Mundo_Reader.getMundoList().get(selectionBox.getSelectedIndex()));

                Main.openMainFrame();
                break;
        }
    }
}
