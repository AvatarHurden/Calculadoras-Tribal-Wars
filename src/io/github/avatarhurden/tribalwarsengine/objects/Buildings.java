package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.EdifícioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import config.Lang;
import database.Cores;
import database.Edifício;

/**
 * Essa classe mantém tudo relacionado a um conjunto de edifícios
 * 
 * @author Arthur
 *
 */
public class Buildings {
	
	private ArrayList<Building> buildings = new ArrayList<Building>();;
	
	public Buildings() {
		
		for (Edifício e : Edifício.values())
			buildings.add(new Building(e, 0));
		
	}
	
	public void addBuilding(Edifício ed, int nível) {
		
		Iterator<Building> iter = buildings.iterator();
		while (iter.hasNext())
			if (iter.next().ed.equals(ed))
				iter.remove();
		
		buildings.add(new Building(ed, nível));
		
	}
	
	public int getLevel(Edifício ed) {
		
		for (Building b : buildings)
			if (b.ed.equals(ed))
				return b.nivel;
		
		return -1;
	}
	
	private class Building {
		
		private Edifício ed;
		private int nivel;
		
		private Building(Edifício ed, int nivel) {
			this.ed = ed;
			this.nivel = nivel;
		}
		
		private int getCustoFerroNext() {
			return ed.getCustoFerro(nivel+1);
		}
		
		private int getCustoArgilaNext() {
			return ed.getCustoArgila(nivel+1);
		}
		
		private int getCustoMadeiraNext() {
			return ed.getCustoMadeira(nivel+1);
		}
		
	}
	
	public static boolean isBuildingJson(JSONObject json) {
		return json.has("buildings");
	}
	
	public class BuildingsEditPanel extends JPanel {
		
		private HashMap<Edifício, EdifícioFormattedTextField> map;
		GridBagLayout layout;
		
		public BuildingsEditPanel(final OnChange onChange, boolean hasHeader, boolean hasNames,
				boolean hasLevels, Edifício... edifícios) {
			
			map = new HashMap<Edifício, EdifícioFormattedTextField>();
			
		    layout = new GridBagLayout();
	        layout.columnWidths = new int[]{};
	        layout.rowHeights = new int[]{ 20
	        		
	        };
	        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
	        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
	        setLayout(layout);
	        
	        GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
	        c.gridy = 0;
	        c.fill = GridBagConstraints.HORIZONTAL;
	        
	        if (hasHeader) {
	        	c.insets = new Insets(0, 0, 5, 0);
	        	add(headerPanel(), c);
	        	c.gridy++;
	        	c.insets = new Insets(0, 0, 0, 0);
	        }
	        
	        List<Edifício> values = new ArrayList<Edifício>(Arrays.asList(edifícios));
	        if (values.isEmpty())
	        	values.addAll(Arrays.asList(Edifício.values()));
	        
	        for (Edifício i : values) {

				JPanel panel = new JPanel(layout);
				panel.setBackground(Cores.getAlternar(c.gridy));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets(5, 5, 5, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(i.toString());
				
				if (values.contains(Edifício.ACADEMIA_1NÍVEL) && values.contains(Edifício.ACADEMIA_3NÍVEIS)) {
				
					if (i.equals(Edifício.ACADEMIA_1NÍVEL))
						label.setText(i.toString() + " (1 nível)");
					else if (i.equals(Edifício.ACADEMIA_3NÍVEIS))
						label.setText(i.toString() + " (3 níveis)");
						
				}
				
				if (hasNames) {
					panel.add(label, panelC);
					panelC.gridx++;
				}
				
				EdifícioFormattedTextField txt = new EdifícioFormattedTextField(i, getLevel(i)) {
					public void go() {
						onChange.run();
					}
				};
				
				if (hasLevels) {
					panel.add(txt, panelC);
					panelC.gridx++;
				}
				map.put(i, txt);
			
				add(panel, c);
				c.gridy++;
			}
	        
		}
		
		public void setValue() {
			
			for (Edifício i : map.keySet()) 
				addBuilding(i, map.get(i).getValueInt());
			
		}
		
		private JPanel headerPanel() {
			
			JPanel panel = new JPanel();
			panel.setLayout(layout);
			panel.setBackground(Cores.FUNDO_ESCURO);
			panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.CENTER;
			
			panel.add(new JLabel("Edifício"), c);

			c.anchor = GridBagConstraints.WEST;
			c.gridx++;
			panel.add(new JLabel("Nível"), c);
			
			return panel;
		}
		
	}
}
