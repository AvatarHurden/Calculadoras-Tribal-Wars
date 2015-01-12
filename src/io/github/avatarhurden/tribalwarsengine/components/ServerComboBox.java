package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ServerComboBox extends TWEComboBox<Server> {

	private int widest = -1;
	
	public ServerComboBox() {
		super();
		setRenderer(new ServerComboBoxRenderer());
		for (Server server : Server.values())
			addItem(server);
		
		setPopupWidth();
	}
	
	public void setPopupWidth() {
    	widest = 0;
    	FontMetrics metrics = getFontMetrics(getFont());
    	for (int i = 0; i < getItemCount(); i++)
    		widest = Math.max(widest, metrics.stringWidth(getItemAt(i).getShortURL()) + 50);
    	revalidate();
	}

    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (widest > 0)
        	dim.width = widest;
        return dim;
    }
    
    public Point getLocationOnScreen() {
    	int x = super.getLocationOnScreen().x;
    	if (widest > 0)
    		x -= widest / 2;
    	return new Point(x, super.getLocationOnScreen().y);
    }

    private class ServerComboBoxRenderer extends JLabel implements ListCellRenderer<Server> {

        public ServerComboBoxRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Server> list, Server value, int index, boolean isSelected, boolean cellHasFocus) {

        	setForeground(Color.DARK_GRAY);
            setIcon(new ImageIcon(value.getFlag()));
            
            if (index == -1)
            	setText("");
            else 
            	setText(value.getShortURL());
            
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
