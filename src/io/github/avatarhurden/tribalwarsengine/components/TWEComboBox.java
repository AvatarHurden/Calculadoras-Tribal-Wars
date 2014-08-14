package io.github.avatarhurden.tribalwarsengine.components;

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
        private Object selectedItem;

        public TWEComboBoxEditor() {
            label.setOpaque(false);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.BLACK);

            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
            panel.add(label);
            panel.setBackground(Color.GREEN);
        }

        public Component getEditorComponent() {
            return this.panel;
        }

        public Object getItem() {
            return "[" + this.selectedItem.toString() + "]";
        }

        public void setItem(Object item) {
            this.selectedItem = item;
            label.setText(item.toString());
        }
    }

    //Render desse combo!
    private class TWEComboBoxRender extends JLabel implements ListCellRenderer {

        public TWEComboBoxRender() {
            setOpaque(true);
            setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
            setBackground(Color.BLUE);
            setForeground(Color.YELLOW);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());

            return this;
        }
    }
}


