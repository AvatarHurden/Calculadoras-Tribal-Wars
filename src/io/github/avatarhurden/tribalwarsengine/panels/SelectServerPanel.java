package io.github.avatarhurden.tribalwarsengine.panels;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWEComboBox;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.main.Main;
import io.github.avatarhurden.tribalwarsengine.managers.ServerManager;
import io.github.avatarhurden.tribalwarsengine.objects.TWServer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import config.Lang;

public class SelectServerPanel extends JPanel implements ActionListener {

    private TWEComboBox<TWServer> selectionBox;
    private TWButton startButton;
    private TWSimpleButton padrãoButton;
    private TWSimpleButton editButton;

    private SelectWorldFrame selectWorldFrame;

    /**
     * JPanel com um comboBox para escolher o mundo e um botão para iniciar o frame de ferramentas
     *
     * @param selectWorldFrame Frame em que será inserido
     */
    public SelectServerPanel(SelectWorldFrame selectWorldFrame) {

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

        selectionBox = new TWEComboBox<TWServer>();

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
        //add(editButton, constraints);

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
        ServerManager manager = ServerManager.get();
        for (TWServer s : manager.getList())
        	selectionBox.addItem(s);
        
        selectionBox.setSelectedItem(manager.getDefaultServer());
        
        selectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED && event.getItem() != null) {
					ServerManager.get().setSelectedServer((TWServer)event.getItem());
					selectWorldFrame.updateWorldInfoPanel();
					changePadrãoButton();
				}
			}
		});
    }

    private void changePadrãoButton() {
    	ServerManager manager = ServerManager.get();
        if (manager.getDefaultServer().equals(manager.getSelectedServer()))
        	padrãoButton.setEnabled(false);
        else {
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
            	
//            	try {
//                    new EditDialog(WorldManager.get().getGenericList(), WorldManager.get().getFieldNames(),
//                    		selectionBox.getSelectedIndex(), new World());
//                } catch (NoSuchFieldException e1) {
//                    e1.printStackTrace();
//                } catch (IllegalAccessException e1) {
//                    e1.printStackTrace();
//                } catch (InstantiationException e1) {
//                    e1.printStackTrace();
//                }
//
//                setSelectionBox();
//                selectWorldFrame.updateWorldInfoPanel();
//                break;
            case "start_button":
            	ServerManager.get().getSelectedServer().setInfo();
                Main.openMainFrame();
                break;
            case "default_button":
                ServerManager.get().setDefaultServer(ServerManager.get().getSelectedServer());
                changePadrãoButton();
                break;
        }
    }
}
