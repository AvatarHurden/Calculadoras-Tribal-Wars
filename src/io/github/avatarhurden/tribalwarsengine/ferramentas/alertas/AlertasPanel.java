package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AlertasPanel extends Ferramenta {

    AlertTable table;
    AlertManager manager;

    public AlertasPanel() {
        super("Alertas");

    }
    
    protected void makeGUI() {

        Dimension d = MainWindow.getInstance().getPreferredSize();
        d.setSize(d.getWidth() - 50, 520);
        
        JScrollPane scrollPane = new JScrollPane();
        AlertTableFilter filter = new AlertTableFilter(this);
        
        table = new AlertTable();
        table.setFillsViewportHeight(true);
        AlertManager.getInstance().setTable(table);
        filter.setSorter(table.getRowSorter());
        
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(d);
        table.setStartingPosition(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(11);
        
    	GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{400};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        setLayout(gridBagLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        
        c.anchor = GridBagConstraints.NORTHWEST;
        add(filter.getEditor(), c);
        
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
