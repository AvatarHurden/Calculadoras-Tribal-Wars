package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.objects.World;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import database.Cores;

/**
 * @author Wesley Nascimento
 */
public class TWEComboBox extends JComboBox{

    public TWEComboBox() {
        this.setRenderer(new TWEComboBoxRenderer());
        setBackground(Cores.FUNDO_ESCURO);
        setForeground(Color.DARK_GRAY);
    }

    //Render desse combo!
    private class TWEComboBoxRenderer extends JLabel implements ListCellRenderer {

        public TWEComboBoxRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            World world = (World) value;
            setText(world.getName());
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


