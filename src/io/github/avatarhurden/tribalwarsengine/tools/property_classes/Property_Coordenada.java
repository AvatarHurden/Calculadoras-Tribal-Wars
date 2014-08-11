package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Property_Coordenada implements Property {
	
	private CoordenadaPanel coordenadaPanel;
	
	private int x = 0, y = 0;
	
	public Property_Coordenada(int x, int y) {
	
		this.x = x;
		this.y = y;
		
	}
	
	@SuppressWarnings("serial")
	public JPanel makeEditDialogPanel(JPanel panel, final OnChange change) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		
		panel.add(new JLabel("Coordenadas"), c);
		
		coordenadaPanel = new CoordenadaPanel(null, x, y) {
			public void go() {
				change.run();
			}
		};
		
		coordenadaPanel.setBorder(null);
		coordenadaPanel.setBackground(panel.getBackground());
		
		c.gridy++;
		panel.add(coordenadaPanel, c);
		
		return panel;
		
	}

	public String getName() {
		return null;
	}
	
	
	public String getValueName() {
		
		return String.format("%03d|%03d",x,y);
		
	}

	
	public void setValue() {
		
		x = coordenadaPanel.getCoordenadaX();
		y = coordenadaPanel.getCoordenadaY();
		
	}
	
	public int getCoordenadaX() {
		return x;
	}
	
	public int getCoordenadaY() {
		return y;
	}

}
