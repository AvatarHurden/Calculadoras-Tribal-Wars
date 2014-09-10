package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AlertasPanel extends Ferramenta {

    AlertTable table;
    AlertManager manager;

    public AlertasPanel() {

        super("Alertas");

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{400};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        setLayout(gridBagLayout);

        for (int i = 0; i < 5; i++) {

            Alert alerta = new Alert();

            alerta.setNome("Nome" + i);

            alerta.setNotas(i + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pellentesque rhoncus dignissim. Phasellus pulvinar ut nunc non congue. Quisque lacus eros, porta malesuada tempor quis, luctus in est. Maecenas in congue tellus, eu rhoncus nulla. Maecenas metus neque, varius in vulputate id, sodales a ante. Donec sit amet laoreet ligula. Vestibulum blandit commodo volutpat.");
            alerta.setTipo(Tipo.values()[i % 4]);
            alerta.setOrigem(new Aldeia("Origem" + i, i * 111, i * 55));
            alerta.setDestino(new Aldeia("Destino" + i, i * 11, i * 555));

            if (i > 0) {
                Army map = new Army();
                for (Unit u : Army.getAttackingUnits())
                    map.addTropa(u, (int) (Math.random() * 100 + 1), 1);
                alerta.setArmy(map);
            } else {
                Army map = new Army();
                map.addTropa(map.getUnit("spear"), 100, 1);
                map.addTropa(map.getUnit("sword"), 100, 1);
                map.addTropa(map.getUnit("axe"), 100, 1);
                alerta.setArmy(map);

            }

            Date now = new Date();

            alerta.setHorário(new Date(now.getTime() + (7-i) * 1000));

            alerta.setRepete((long) (Math.random() * 100000000));
            
            ArrayList<Date> avisos = new ArrayList<Date>();
            avisos.add(new Date(alerta.getHorário().getTime() - 10000));
            alerta.setAvisos(new ArrayList<Date>());
            
            AlertManager.getInstance().addAlert(alerta);
        }

    }
    
    protected void makeGUI() {

        Dimension d = MainWindow.getInstance().getPreferredSize();
        d.setSize(d.getWidth() - 50, 570);

        table = new AlertTable();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(d);
        table.setFillsViewportHeight(true);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        add(scrollPane, c);

        c.gridy++;
        add(makeButtonPanels(), c);

    }

    private JPanel makeButtonPanels() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);

        ActionListener action = makeActionListener();
        
        JButton addAlerta = new TWButton("Criar Novo");
        addAlerta.addActionListener(action);
        addAlerta.setActionCommand("add");

        JButton editAlerta = new TWSimpleButton("Editar");
        editAlerta.addActionListener(action);
        editAlerta.setActionCommand("edit");
        
        JButton deleteAlerta = new TWSimpleButton("Deletar");
        deleteAlerta.addActionListener(action);
        deleteAlerta.setActionCommand("delete");
        
        panel.add(addAlerta);
        panel.add(editAlerta);
        panel.add(deleteAlerta);

        return panel;
    }
    
    private ActionListener makeActionListener() {
    	
    	return new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				int row = table.convertRowIndexToModel(table.getSelectedRow());
				
				switch (e.getActionCommand()) {
				case "add":
					AlertManager.getInstance().createAlert();
					break;
				case "edit":
					if (row == -1)
						return;
                
					Alert selected = table.getAlert(row);

					AlertManager.getInstance().createAlert(selected, true);  
				case "delete":
	                AlertManager.getInstance().removeAlert(table.getAlert(row));
				default:
					break;
				}
				
				table.changedAlert();
				
			}
		};
    	
    }

}
