package custom_components;

import config.Lang;
import config.ModeloAldeias_Reader;
import config.ModeloTropas_Reader;
import config.Mundo_Reader;
import database.*;
import selecionar_mundo.selectWorldFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Panel on top of every ferramenta with the following functionality:
 * <br>- Reset the values
 * <br>- Apply and edit ModeloTropas
 *
 * @author Arthur
 */
public class ToolPanel {

    // The class will contain a panel for every function, and the caller will
    // decide where each one will be placed
    private JPanel bandeiraPanel;

    private List<ModeloTropasPanel> tropasPanelList = new ArrayList<ModeloTropasPanel>();
    private List<ModeloAldeiasPanel> aldeiasPanelList = new ArrayList<ModeloAldeiasPanel>();

    public ToolPanel() {
    }

    private JPanel makeResetPanel(ActionListener listener) {

        JPanel resetPanel = new JPanel();
        resetPanel.setOpaque(false);

        JButton resetButton = new JButton(Lang.BtnResetar.toString());
        resetButton.addActionListener(listener);

        resetPanel.add(resetButton);

        return resetPanel;

    }

    /**
     * Cria um panel com um botão com o nome "Reset" que, quando clicado, faz a ação dada
     *
     * @param listener Ação a ser feita no clique
     * @return Um Panel com o botão
     */
    public JPanel addResetPanel(ActionListener action) {

        return makeResetPanel(action);

    }

    /**
     * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, além de editá-los
     *
     * @param edit       Se o painel possui o botão de edição
     * @param textFields Map com IntegerFormattedTextField ligados a unidades, para utilizar os Modelos
     * @return Um JPanel com as coisas faladas
     */
    public JPanel addModelosTropasPanel(boolean edit, Map<Unidade, IntegerFormattedTextField> textFields) {

        ModeloTropasPanel panel = new ModeloTropasPanel(edit, textFields);

        tropasPanelList.add(panel);

        return panel;

    }

    /**
     * Cria um panel que fornece a habilidade de utilizar os ModelosTropas, além de editá-los
     *
     * @param edit       Se o painel possui o botão de edição
     * @param textFields Map com IntegerFormattedTextField ligados a unidades, para utilizar os Modelos
     * @return Um JPanel com as coisas faladas
     */
    public JPanel addModelosAldeiasPanel(boolean edit,
                                         Map<Edifício, EdifícioFormattedTextField> textFields, CoordenadaPanel coord) {

        ModeloAldeiasPanel panel = new ModeloAldeiasPanel(edit, textFields, coord);

        aldeiasPanelList.add(panel);

        return panel;

    }

    /**
     * Refreshes the option menu to reflect all changes that might have occurred
     */
    public void refresh() {

        for (ModeloTropasPanel i : tropasPanelList)
            i.makePopupMenu();

        for (ModeloAldeiasPanel i : aldeiasPanelList)
            i.makePopupMenu();

    }

    @SuppressWarnings("serial")
    private class ModeloTropasPanel extends JPanel {

        private Map<Unidade, IntegerFormattedTextField> mapTextFields;

        private JPopupMenu popup;

        public ModeloTropasPanel(boolean edit, Map<Unidade, IntegerFormattedTextField> textFields) {

            mapTextFields = textFields;

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

            JLabel label = new JLabel("Tropas:");

            label.setPreferredSize(new Dimension(54, getPreferredSize().height));

            return label;

        }

        private JButton makeSelectionButton() {

            final JButton button = new JButton();

            button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                    selectWorldFrame.class.getResource("/images/down_arrow.png"))));

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

        private void makePopupMenu() {

            popup = new JPopupMenu();

            // Adds all the models to the dropdown menu
            for (final ModeloTropas i : ModeloTropas_Reader.getListModelosAtivos()) {

                popup.add(makeMenuItem(i));

            }

        }

        private JMenuItem makeMenuItem(final ModeloTropas i) {

            JMenuItem item = new JMenuItem(i.getNome());

            item.setName(i.getNome());

            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent a) {

                    // Edits all the textfields according to the model
                    for (Entry<Unidade, IntegerFormattedTextField> e : mapTextFields
                            .entrySet())
                        if (i.getQuantidade(e.getKey()).equals(BigDecimal.ZERO))
                            e.getValue().setText("");
                        else
                            e.getValue().setText(
                                    i.getQuantidade(e.getKey()).toString());

                    // puts the focus on the first textfield (for consistency)
                    mapTextFields.get(Unidade.LANCEIRO).requestFocus();

                }
            });

            return item;

        }

        private JButton makeEditButton() {

            final JButton button = new JButton();

            button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                    selectWorldFrame.class.getResource("/images/edit_icon.png"))));

            button.setPreferredSize(new Dimension(20, 20));

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    try {

                        Map<Unidade, BigDecimal> map = new HashMap<Unidade, BigDecimal>();
                        for (Unidade i : Unidade.values())
                            if (mapTextFields.containsKey(i))
                                map.put(i, mapTextFields.get(i).getValue());
                            else
                                map.put(i, BigDecimal.ZERO);

                        ModeloTropas modelo = new ModeloTropas(null, map, Mundo_Reader.MundoSelecionado);

                        new EditDialog(ModeloTropas.class,
                                ModeloTropas_Reader.getListModelos(),
                                "variableList", 0, modelo);

                        ModeloTropas_Reader.checkAtivos();

                        makePopupMenu();

                        refresh();

                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }

                }
            });

            return button;

        }

    }


    @SuppressWarnings("serial")
    private class ModeloAldeiasPanel extends JPanel {

        private Map<Edifício, EdifícioFormattedTextField> mapTextFields;
        private CoordenadaPanel coord;

        private JPopupMenu popup;

        public ModeloAldeiasPanel(boolean edit,
                                  Map<Edifício, EdifícioFormattedTextField> textFields, CoordenadaPanel coord) {

            mapTextFields = textFields;
            this.coord = coord;

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
                    selectWorldFrame.class.getResource("/images/down_arrow.png"))));

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

        private void makePopupMenu() {

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
                        coord.x.setText(String.valueOf(i.getCoordenadaX()));
                        coord.y.setText(String.valueOf(i.getCoordenadaY()));
                    }

                    // If there are coordinates, focus them. If not, focus the first building
                    if (coord != null)
                        coord.x.requestFocus();
                    else
                        mapTextFields.get(Edifício.EDIFÍCIO_PRINCIPAL).requestFocus();

                }
            });

            return item;

        }

        private JButton makeEditButton() {

            final JButton button = new JButton();

            button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                    selectWorldFrame.class.getResource("/images/edit_icon.png"))));

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

                        refresh();

                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }

                }
            });

            return button;

        }


    }
}
