package alertas;

import alertas.Alert.Aldeia;
import alertas.Alert.Tipo;
import custom_components.Ferramenta;
import database.Unidade;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GUI extends Ferramenta {

    List<Alert> alertas = new ArrayList<Alert>();
    AlertTable table = new AlertTable(alertas);
    PopupManager popups = new PopupManager();

    public GUI() {

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

            alerta.setHorário(new Date(now.getTime() + (1 + i) * 3000));

            alerta.setRepete((long) (Math.random() * 100000000));

            alerta.setAvisos(new ArrayList<Date>());

            alertas.add(alerta);

            //popups.addAlerta(alerta);
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

                Editor editor = new Editor();

                editor.setModal(true);
                editor.setVisible(true);

                Alert alerta = editor.getAlerta();

                if (alerta != null) {
                    table.addAlert(alerta);
                    popups.addAlerta(alerta);
                }
            }
        });

        JButton editAlerta = new JButton("Editar");
        editAlerta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (table.getSelectedRow() == -1)
                    return;

                Alert selected = (Alert) table.getModel().getValueAt(
                        table.convertRowIndexToModel(table.getSelectedRow()), -1);

                Editor editor = new Editor(selected);

                editor.setModal(true);
                editor.setVisible(true);

                Alert alerta = editor.getAlerta();

                table.changeAlert(alerta, table.convertRowIndexToModel(table.getSelectedRow()));

            }
        });

        JButton deleteAlerta = new JButton("Deletar");
        deleteAlerta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int row = table.convertRowIndexToModel(table.getSelectedRow());

                if (row >= 0)
                    table.removeAlert(row);

            }
        });

        panel.add(addAlerta);
        panel.add(editAlerta);
        panel.add(deleteAlerta);

        return panel;
    }

}
