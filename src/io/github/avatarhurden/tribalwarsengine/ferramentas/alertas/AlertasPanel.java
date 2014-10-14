package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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

        for (int i = 0; i < 10; i++) {

            Alert alerta = new Alert();

            alerta.setNome("Nome" + i);

            alerta.setNotas(i + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pellentesque rhoncus dignissim. Phasellus pulvinar ut nunc non congue. Quisque lacus eros, porta malesuada tempor quis, luctus in est. Maecenas in congue tellus, eu rhoncus nulla. Maecenas metus neque, varius in vulputate id, sodales a ante. Donec sit amet laoreet ligula. Vestibulum blandit commodo volutpat.");
            alerta.setNotas("https://en.tribalwars2.com/game.php?world=en1&character_id=60369");
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

            alerta.setHorário(new Date(now.getTime() + (1+i) * 100000));

            alerta.setRepete((long) 10000);
            
            ArrayList<Date> avisos = new ArrayList<Date>();
            avisos.add(new Date(alerta.getHorário().getTime() - 10000));
            alerta.setAvisos(new ArrayList<Date>());
            
            AlertManager.getInstance().addAlert(alerta);
        }
    }
    
    protected void makeGUI() {

        Dimension d = MainWindow.getInstance().getPreferredSize();
        d.setSize(d.getWidth() - 50, 520);
        
        JScrollPane scrollPane = new JScrollPane();
        AlertTableFilter filter = new AlertTableFilter(scrollPane);
        
        table = new AlertTable();
        table.setFillsViewportHeight(true);
        AlertManager.getInstance().setTable(table);
        filter.setSorter((TableRowSorter<? extends TableModel>) table.getRowSorter());
        
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(d);
        table.setStartingPosition(scrollPane);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        
        c.anchor = GridBagConstraints.WEST;
        add(filter, c);
        
        c.gridx++;
        c.anchor = GridBagConstraints.NORTHEAST;
        add(makeOptionsButton(), c);
        
        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;
        add(scrollPane, c);

        c.gridy++;
        add(makeButtonPanels(), c);

    }
    
    private JButton makeOptionsButton() {
    	
    	JButton button = new TWSimpleButton();
        button.setPreferredSize(new Dimension(20, 20));
        
    	button.setIcon(new ImageIcon(Imagens.getImage("edit_icon.png")));
    	
    	button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AlertConfigEditor();
			
				table.managePast();
			}
		});
    
    	return button;
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
				int row;
				try {
					row = table.convertRowIndexToModel(table.getSelectedRow());
				} catch (Exception exc) {
					row = -1;
				}
				
				switch (e.getActionCommand()) {
				case "add":
					AlertManager.getInstance().createAlert();
					break;
				case "edit":
					if (row == -1)
						return;
                
					Alert selected = table.getAlert(row);

					selected = AlertManager.getInstance().createAlert(selected, true);
					table.editAlert(row, selected);
					break;
				case "delete":
					if (row == -1)
						return;	
						
					AlertManager.getInstance().removeAlert(table.getAlert(row));
					table.removeAlert(row);
					break;
				default:
					break;
				}
			}
		};
    	
    }

}
