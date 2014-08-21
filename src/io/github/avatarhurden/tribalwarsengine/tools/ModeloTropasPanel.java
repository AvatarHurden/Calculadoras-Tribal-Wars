package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.managers.ArmyModelManager;
import io.github.avatarhurden.tribalwarsengine.objects.Army;
import io.github.avatarhurden.tribalwarsengine.objects.ArmyModel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class ModeloTropasPanel extends JPanel {

    private Map<Unidade, IntegerFormattedTextField> mapTextFields;

    private JPopupMenu popup;
    private ToolManager manager;

    public ModeloTropasPanel(boolean edit, Map<Unidade, IntegerFormattedTextField> textFields, ToolManager manager) {

        mapTextFields = textFields;
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

        JLabel label = new JLabel("Tropas:");

        label.setPreferredSize(new Dimension(54, getPreferredSize().height));

        return label;

    }

    private JButton makeSelectionButton() {

        final JButton button = new TWSimpleButton();

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
        for (final ArmyModel i : ArmyModelManager.get().getActiveList())
            popup.add(makeMenuItem(i));
        

    }

    private JMenuItem makeMenuItem(final ArmyModel i) {

        JMenuItem item = new JMenuItem(i.getName());

        item.setName(i.getName());

        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {

                // Edits all the textfields according to the model
                for (Entry<Unidade, IntegerFormattedTextField> e : mapTextFields
                        .entrySet())
                    if (i.getArmy().getQuantidade((e.getKey())) == 0)
                        e.getValue().setText("");
                    else
                        e.getValue().setText(String.valueOf(
                                i.getArmy().getQuantidade(e.getKey())));

                // puts the focus on the first textfield (for consistency)
                mapTextFields.get(Unidade.LANCEIRO).requestFocus();

            }
        });

        return item;

    }

    private JButton makeEditButton() {

        final JButton button = new TWSimpleButton();

        button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/edit_icon.png"))));

        button.setPreferredSize(new Dimension(20, 20));

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                try {
                	
                	Army army = new Army();
                    for (Unidade i : Unidade.values())
                        if (mapTextFields.containsKey(i))
                            army.addTropa(i, 1, mapTextFields.get(i).getValue().intValue());
                        else
                            army.addTropa(i, 1, 0);

                    ArmyModel modelo = new ArmyModel();
                    modelo.setArmy(army);
                    
                    new EditDialog(ArmyModelManager.get().getList(),
                    		ArmyModelManager.get().getFieldNames(),
                            0, modelo);

                    ArmyModelManager.get().setActives();

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
