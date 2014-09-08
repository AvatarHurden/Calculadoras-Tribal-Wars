package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Wesley Nascimento
 */
public class TWEComboBox<K> extends JComboBox<K>{

    public TWEComboBox() {
        this.setRenderer(new TWEComboBoxRenderer());
        setBackground(Cores.FUNDO_ESCURO);
        setForeground(Color.DARK_GRAY);
    }

    //Render desse combo!
    private class TWEComboBoxRenderer extends JLabel implements ListCellRenderer<K> {

        public TWEComboBoxRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            setText(value.toString());
            setForeground(Color.DARK_GRAY);
            
            if (index % 2 == 0)
                setBackground(Cores.ALTERNAR_CLARO);
            else
                setBackground(Cores.ALTERNAR_ESCURO);
            
            //Altera a fonte caso esteja selecionado
            if (isSelected) {
                setForeground(Cores.hex2Rgb("#603000"));
                setBackground(Cores.FUNDO_ESCURO);
            }

            return this;
        }
    }
}


