package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.Unidade;

public class AlertasPanel extends Ferramenta {

    List<Alert> alertas = new ArrayList<Alert>();
    AlertTable table = new AlertTable(alertas);
    PopupManager popups = new PopupManager();

    public AlertasPanel() {

        super("Alertas");

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{400};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        setLayout(gridBagLayout);

        for (int i = 0; i < 4; i++) {

            Alert alerta = new Alert();

            alerta.setNome("Nome" + i);

            alerta.setNotas(i + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pellentesque rhoncus dignissim. Phasellus pulvinar ut nunc non congue. Quisque lacus eros, porta malesuada tempor quis, luctus in est. Maecenas in congue tellus, eu rhoncus nulla. Maecenas metus neque, varius in vulputate id, sodales a ante. Donec sit amet laoreet ligula. Vestibulum blandit commodo volutpat.");
            alerta.setTipo(Tipo.values()[i % 4]);
            alerta.setOrigem(new Aldeia("Origem" + i, i * 111, i * 55));
            alerta.setDestino(new Aldeia("Destino" + i, i * 11, i * 555));

            if (i > 0) {
                Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
                for (Unidade u : Unidade.values())
                    map.put(u, (int) (Math.random() * 100 + 1));
                alerta.setTropas(map);
            } else {
                Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
                map.put(Unidade.LANCEIRO, 34);
                map.put(Unidade.ESPADACHIM, 342);
                map.put(Unidade.ARCOCAVALO, 32);
                alerta.setTropas(map);

            }

            Date now = new Date();

            alerta.setHorário(new Date(now.getTime() + (7-i) * 10000));

            alerta.setRepete((long) (Math.random() * 100000000));
            
            ArrayList<Date> avisos = new ArrayList<Date>();
            avisos.add(new Date(now.getTime() + (7-i) * 3000));
            alerta.setAvisos(avisos);

            alertas.add(alerta);

            popups.addAlert(alerta);
        }

        Dimension d = MainWindow.getInstance().getPreferredSize();
        d.setSize(d.getWidth(), 570);

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

        JButton addAlerta = new JButton("Criar Novo");
        addAlerta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                AlertEditor alertEditor = new AlertEditor();

                alertEditor.setModal(true);
                alertEditor.setVisible(true);

                Alert alerta = alertEditor.getAlerta();

                if (alerta != null) {
                    table.addAlert(alerta);
                    popups.addAlert(alerta);
                }
            }
        });

        JButton editAlerta = new JButton("Editar");
        editAlerta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (table.getSelectedRow() == -1)
                    return;
                
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                
                Alert selected = table.getAlert(row);

                AlertEditor alertEditor = new AlertEditor(selected);

                alertEditor.setModal(true);
                alertEditor.setVisible(true);

                Alert alerta = alertEditor.getAlerta();

                table.changeAlert(alerta, row);
                popups.changeAlert(alerta);
                
            }
        });

        JButton deleteAlerta = new JButton("Deletar");
        deleteAlerta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int row = table.convertRowIndexToModel(table.getSelectedRow());

                if (row >= 0) {
                    popups.removeAlert(table.getAlert(row));
                    table.removeAlert(row);
                }
            }
        });

        panel.add(addAlerta);
        panel.add(editAlerta);
        panel.add(deleteAlerta);

        return panel;
    }

}
