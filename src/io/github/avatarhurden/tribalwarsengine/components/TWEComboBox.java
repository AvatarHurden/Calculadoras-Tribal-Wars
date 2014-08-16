package io.github.avatarhurden.tribalwarsengine.components;

import database.Cores;
import io.github.avatarhurden.tribalwarsengine.objects.World;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

/**
 * Created by Administrador on 11/08/2014.
 */
public class TWEComboBox extends JComboBox {


    public TWEComboBox() {
        this.setRenderer(new TWEComboBoxRender());
        this.setEditor(new TWEComboBoxEditor());
    }

    //Editor desse Combo
    private class TWEComboBoxEditor extends BasicComboBoxEditor {
        private JLabel label = new JLabel();
        private JPanel panel = new JPanel();
        private World selectedItem;

        public TWEComboBoxEditor() {
            /*
            label.setOpaque(false);
            label.setForeground(Color.BLACK);

            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
            panel.add(label);
            panel.setBackground(Color.GREEN);
            */
        }

        /*
        public Component getEditorComponent() {
            return this.panel;
        }
        */

        /**
         * @return world - O mundo atualmente selecionado
         */
        public Object getItem() {
            return selectedItem;
        }

        public World getWorld() {
            return selectedItem;
        }

        public void setItem(Object item) {
            this.selectedItem = (World) item;
            //label.setText( selectedItem.getName() );
        }
    }

    //Render desse combo!
    private class TWEComboBoxRender extends JLabel implements ListCellRenderer {

        private boolean changeColor = true;

        public TWEComboBoxRender() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            World world = (World) value;
            setText(world.getName());

            if (changeColor) {
                setBackground(Cores.ALTERNAR_CLARO);
                changeColor = false;
            }
            //
            else {
                setBackground(Cores.ALTERNAR_ESCURO);
                changeColor = true;
            }

            return this;
        }
    }
}


