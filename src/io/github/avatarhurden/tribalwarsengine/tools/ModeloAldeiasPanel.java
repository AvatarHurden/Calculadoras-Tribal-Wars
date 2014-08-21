package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.EdifícioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.managers.VillageModelManager;
import io.github.avatarhurden.tribalwarsengine.objects.Buildings;
import io.github.avatarhurden.tribalwarsengine.objects.VillageModel;

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
import database.Edifício;

@SuppressWarnings("serial")
public class ModeloAldeiasPanel extends JPanel {

    private Map<Edifício, EdifícioFormattedTextField> mapTextFields;

    private JPopupMenu popup;
    private ToolManager manager;

    public ModeloAldeiasPanel(boolean edit,Map<Edifício, EdifícioFormattedTextField> textFields,
    		ToolManager manager) {

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

        JLabel label = new JLabel("Aldeias:");

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
        for (final VillageModel i : VillageModelManager.get().getList())
            popup.add(makeMenuItem(i));


    }

    private JMenuItem makeMenuItem(final VillageModel i) {

        JMenuItem item = new JMenuItem(i.getName());

        item.setName(i.getName());

        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
            	
                if (mapTextFields != null) {
                		
                    // Edits all the textfields according to the model
                    for (Entry<Edifício, EdifícioFormattedTextField> e : mapTextFields.entrySet()) {
                        System.out.println(i.getBuildings().getLevel(e.getKey()));
                    	e.getValue().setText(
                                String.valueOf(i.getBuildings().getLevel(e.getKey())));
                    }

                }

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
                	
                	Buildings predios = new Buildings();
                    for (Edifício i : Edifício.values())
                        if (mapTextFields != null && mapTextFields.containsKey(i))
                        	predios.addBuilding(i, mapTextFields.get(i).getValue().intValue());
                        else
                        	predios.addBuilding(i, 0);
                    
                    VillageModel modelo = new VillageModel();
                    modelo.setBuildings(predios);
                    
                    new EditDialog(VillageModelManager.get().getList(),
                    		VillageModelManager.get().getFieldNames(),
                            0, modelo);
                    
                    VillageModelManager.get().setActives();

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
