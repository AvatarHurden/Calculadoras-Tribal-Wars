package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.ArmyModel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ModeloTropasPanel extends JPanel {

	private ArmyEditPanel[] armyEdit;
    private JPopupMenu popup;
    private ToolManager manager;

    public ModeloTropasPanel(boolean edit, ToolManager manager, ArmyEditPanel... armyEdit) {

        this.armyEdit = armyEdit;
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
        for (ArmyModel i : WorldManager.getSelectedWorld().getArmyModelList())
            popup.add(makeMenuItem(i));
        

    }

    private JMenuItem makeMenuItem(final ArmyModel i) {

        JMenuItem item = new JMenuItem(i.getName());

        item.setName(i.getName());

        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
            	
            	for (ArmyEditPanel p : armyEdit)
            		p.setValues(i.getArmy());
            	
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
                	
                	ArmyModel modelo = new ArmyModel();
                	for (ArmyEditPanel p : armyEdit)
                		modelo.setArmy(p.getArmy());
                    
                    new EditDialog(WorldManager.getSelectedWorld().getArmyModelList(),
                    		modelo.getFieldNames(), 0, modelo);
                    
                    WorldManager.getSelectedWorld().saveArmyModels();
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
