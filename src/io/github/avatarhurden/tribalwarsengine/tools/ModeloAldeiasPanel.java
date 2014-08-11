package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.EdifícioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import config.ModeloAldeias_Reader;
import config.Mundo_Reader;
import database.Cores;
import database.Edifício;
import database.ModeloAldeias;

@SuppressWarnings("serial")
public class ModeloAldeiasPanel extends JPanel {

    private Map<Edifício, EdifícioFormattedTextField> mapTextFields;
    private CoordenadaPanel coord;

    private JPopupMenu popup;
    private ToolManager manager;

    public ModeloAldeiasPanel(boolean edit,Map<Edifício, EdifícioFormattedTextField> textFields,
    		CoordenadaPanel coord, ToolManager manager) {

        mapTextFields = textFields;
        this.coord = coord;
        this.manager = manager;

        makePopupMenu();

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{54, 20, 20};
        layout.rowHeights = new int[]{20};
        layout.columnWeights = new double[]{0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        setLayout(layout);

        setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

        setBackground(Cores.FUNDO_ESCURO);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;

        c.insets = new Insets(0, 2, 0, 0);
        add(makeNameLabel(), c);

        if (!edit)
            c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        add(makeSelectionButton(), c);

        if (edit) {
            c.insets = new Insets(0, 0, 0, 2);
            c.gridx = 2;
            add(makeEditButton(), c);
        }
    }

    private JLabel makeNameLabel() {

        JLabel label = new JLabel("Aldeias:");

        label.setPreferredSize(new Dimension(54, getPreferredSize().height));

        return label;

    }

    private JButton makeSelectionButton() {

        final JButton button = new JButton();

        button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/down_arrow.png"))));

        button.setPreferredSize(new Dimension(20, 20));

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                int x = button.getLocation().x
                        + button.getPreferredSize().width / 2
                        - popup.getPreferredSize().width / 2;

                int y = button.getLocation().y
                        + button.getPreferredSize().height;

                popup.show(button.getParent(), x, y);

            }
        });

        return button;

    }

    protected void makePopupMenu() {

        popup = new JPopupMenu();

        // Adds all the models to the dropdown menu
        for (final ModeloAldeias i : ModeloAldeias_Reader.getListModelosAtivos()) {

            popup.add(makeMenuItem(i));

        }

    }

    private JMenuItem makeMenuItem(final ModeloAldeias i) {

        JMenuItem item = new JMenuItem(i.getNome());

        item.setName(i.getNome());

        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {

                if (mapTextFields != null) {

                    // Edits all the textfields according to the model
                    for (Entry<Edifício, EdifícioFormattedTextField> e : mapTextFields.entrySet())
                        e.getValue().setText(
                                String.valueOf(i.getNível(e.getKey())));

                }

                if (coord != null) {
                    coord.getXField().setText(String.valueOf(i.getCoordenadaX()));
                    coord.getYField().setText(String.valueOf(i.getCoordenadaY()));
                }

                // If there are coordinates, focus them. If not, focus the first building
                if (coord != null)
                    coord.getXField().requestFocus();
                else
                    mapTextFields.get(Edifício.EDIFÍCIO_PRINCIPAL).requestFocus();

            }
        });

        return item;

    }

    private JButton makeEditButton() {

        final JButton button = new JButton();

        button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/edit_icon.png"))));

        button.setPreferredSize(new Dimension(20, 20));

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                try {

                    int x = 0, y = 0;

                    Map<Edifício, Integer> map = new HashMap<Edifício, Integer>();
                    for (Edifício i : Edifício.values())
                        if (mapTextFields != null && mapTextFields.containsKey(i))
                            map.put(i, mapTextFields.get(i).getValue().intValue());
                        else
                            map.put(i, 0);

                    if (coord != null) {
                        x = coord.getCoordenadaX();
                        y = coord.getCoordenadaY();
                    }

                    ModeloAldeias modelo = new ModeloAldeias(null, map, x, y,
                            Mundo_Reader.MundoSelecionado);

                    new EditDialog(ModeloAldeias.class,
                            ModeloAldeias_Reader.getListModelos(),
                            "variableList", 0, modelo);

                    ModeloAldeias_Reader.checkAtivos();

                    makePopupMenu();

                    manager.refresh();

                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }

            }
        });

        return button;

    }


}
