package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.Edif�cioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import database.Cores;
import database.Edif�cio;

/**
 * Essa classe mant�m tudo relacionado a um conjunto de edif�cios
 * 
 * @author Arthur
 *
 */
public class Buildings {
	
	private ArrayList<Building> buildings = new ArrayList<Building>();;
	
	public Buildings() {
		this(Arrays.asList(Edif�cio.values()));
	}
	
	public Buildings(List<Edif�cio> list) {
		
		for (Edif�cio e : list)
			buildings.add(new Building(e, 0));
		
	}
	
	public void addBuilding(Edif�cio ed, int n�vel) {
		addBuilding(new Building(ed, n�vel));
	}
	
	public void addBuilding(Building b) {
		int position = buildings.size()-1;
		
		Iterator<Building> iter = buildings.iterator();
		while (iter.hasNext())
			if (iter.next().edif�cio.equals(b.edif�cio)) {
				position = buildings.indexOf(getBuilding(b.edif�cio));
				iter.remove();
			}
		
		buildings.add(position, b);
		
	}
	
	public boolean contains(Edif�cio u) {
		for (Building t : buildings)
			if (t.edif�cio.equals(u))
				return true;
		
		return false;
	}
	
	public Building getBuilding(Edif�cio ed) {
		for (Building b : buildings)
			if (b.edif�cio.equals(ed))
				return b;
		
		return null;
	}
	
	public int getLevel(Edif�cio ed) {
		
		for (Building b : buildings)
			if (b.edif�cio.equals(ed))
				return b.nivel;
		
		return -1;
	}
	
	public List<Edif�cio> getEdif�cios() {
		List<Edif�cio> list = new ArrayList<>();
		for (Building t : buildings)
			list.add(t.edif�cio);
		
		return list;
	}
	
	/**
	 * Se o objeto possui Bosque, retorna a produ��o de madeira do mesmo, 
	 * em recursos/hora.
	 * <p> Caso n�o possua, retorna 0.
	 */
	public int getWoodProduction() {
		if (buildings.contains(getBuilding(Edif�cio.BOSQUE))) {
			if (getLevel(Edif�cio.BOSQUE) == 0)
				return 5;
			else
				return (int) Math.round(
					30 * Math.pow(1.163118, getLevel(Edif�cio.BOSQUE)-1));
		} else
			return 0;
	}
	
	/**
	 * Se o objeto possui Po�o de Argila, retorna a produ��o de argila do mesmo, 
	 * em recursos/hora.
	 * <p> Caso n�o possua, retorna 0.
	 */
	public int getClayProduction() {
		if (buildings.contains(getBuilding(Edif�cio.PO�O_DE_ARGILA))) {
			if (getLevel(Edif�cio.PO�O_DE_ARGILA) == 0)
				return 5;
			else
				return (int) Math.round(
					30 * Math.pow(1.163118, getLevel(Edif�cio.PO�O_DE_ARGILA)-1));
		} else
			return 0;
	}
	
	/**
	 * Se o objeto possui Mina de Ferro, retorna a produ��o de ferro do mesmo, 
	 * em recursos/hora.
	 * <p> Caso n�o possua, retorna 0.
	 */
	public int getIronProduction() {
		if (buildings.contains(getBuilding(Edif�cio.MINA_DE_FERRO))) {
			if (getLevel(Edif�cio.MINA_DE_FERRO) == 0)
				return 5;
			else
				return (int) Math.round(
					30 * Math.pow(1.163118, getLevel(Edif�cio.MINA_DE_FERRO)-1));
		} else
			return 0;
	}
	
	/**
	 * Retorna todo o armazenamento da aldeia. Caso <code>usaEsconderijo</code>
	 * seja verdadeiro, desconta a quantidade que fica escondida.
	 * @param usaEsconderijo
	 */
	public int getArmazenamento(boolean usaEsconderijo) {
		
		int armazenamento;
		
		if (buildings.contains(getBuilding(Edif�cio.ARMAZ�M))) {
			if (getLevel(Edif�cio.ARMAZ�M) == 0)
				armazenamento = 0;
			else
				armazenamento = (int) Math.round(
					1000 * Math.pow(1.2294934, getLevel(Edif�cio.ARMAZ�M)-1));
		} else
			return 0;
		
		if (usaEsconderijo && buildings.contains(getBuilding(Edif�cio.ESCONDERIJO))
				&& getLevel(Edif�cio.ESCONDERIJO) > 0)
			armazenamento -= (int) Math.round(
					150 * Math.pow(1.3333333, getLevel(Edif�cio.ESCONDERIJO)-1));
		
		return armazenamento;
	}
	
	private class Building {
		
		private Edif�cio edif�cio;
		private int nivel;
		
		private Building(Edif�cio ed, int nivel) {
			this.edif�cio = ed;
			this.nivel = nivel;
		}
		
		private int getCustoFerroNext() {
			return edif�cio.getCustoFerro(nivel+1);
		}
		
		private int getCustoArgilaNext() {
			return edif�cio.getCustoArgila(nivel+1);
		}
		
		private int getCustoMadeiraNext() {
			return edif�cio.getCustoMadeira(nivel+1);
		}
		
	}
	
	public static boolean isBuildingJson(JSONObject json) {
		return json.has("buildings");
	}
	
	public class BuildingsEditPanel extends JPanel {
		
		private HashMap<Edif�cio, Edif�cioFormattedTextField> map;
		private GridBagLayout layout;
		
		private boolean hasHeader, hasNames, hasLevels;
		private OnChange onChange;
		
		public BuildingsEditPanel(OnChange onChange, boolean hasHeader, boolean hasNames,
				boolean hasLevels, Edif�cio... edif�cios) {
			
			map = new HashMap<Edif�cio, Edif�cioFormattedTextField>();
			
			this.onChange = onChange;
			this.hasHeader = hasHeader;
			this.hasNames = hasNames;
			this.hasLevels = hasLevels;
			
			setOpaque(false);
			
			setLayout();

			setLayout(new GridBagLayout());
	        
	        GridBagConstraints c = new GridBagConstraints();
	        c.gridx = 0;
			c.gridy = 0;
	        c.gridwidth = 1;
	        c.anchor = GridBagConstraints.EAST;
	        c.fill = GridBagConstraints.BOTH;
	
	        if (hasHeader) {
	        	c.insets = new Insets(0, 0, 5, 0);
	        	add(headerPanel(), c);
		        c.gridy++;
	        }
	        
	        c.insets = new Insets(0, 0, 0, 0);
	        add(mainPanel(edif�cios), c);
	       
		}
		
		private void setLayout() {
			
			layout = new GridBagLayout();
		    layout.columnWidths = new int[] {};
		    layout.rowHeights = new int[]{20};
		    layout.columnWeights = new double[]{1, Double.MIN_VALUE};
		    layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			
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
			
			panel.add(new JLabel("Edif�cio"), c);

			c.anchor = GridBagConstraints.WEST;
			c.gridx++;
			panel.add(new JLabel("N�vel"), c);
			
			return panel;
		}
		
		private JPanel mainPanel(Edif�cio... edif�cios) {
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setOpaque(false);
			if (hasHeader)
				panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			Edif�cio[] values = edif�cios;
			if (values.length == 0) {
				values = new Edif�cio[buildings.size()];
				for (int i = 0; i < buildings.size(); i++)
					values[i] = buildings.get(i).edif�cio;
			}
			
			for (int i = 0; i < values.length; i++) {

				Edif�cio ed = values[i];
				
				JPanel edPanel = new JPanel(layout);
				edPanel.setBackground(Cores.getAlternar(i));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets(5, 5, 5, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(ed.toString());
				
				if (Buildings.this.contains(Edif�cio.ACADEMIA_1N�VEL) 
						&& Buildings.this.contains(Edif�cio.ACADEMIA_3N�VEIS)) {
				
					if (ed.equals(Edif�cio.ACADEMIA_1N�VEL))
						label.setText(ed.toString() + " (1 n�vel)");
					else if (ed.equals(Edif�cio.ACADEMIA_3N�VEIS))
						label.setText(ed.toString() + " (3 n�veis)");
						
				}
				
				if (hasNames) {
					edPanel.add(label, panelC);
					panelC.gridx++;
				}
				
				Edif�cioFormattedTextField txt = new Edif�cioFormattedTextField(ed, getLevel(ed)) {
					public void go() {
						onChange.run();
					}
				};
				
				if (hasLevels) {
					edPanel.add(txt, panelC);
					panelC.gridx++;
				}
				map.put(ed, txt);
			
				panel.add(edPanel);
			}
		       
			return panel;
			
		}
		
		public void saveValues() {
			
			for (Edif�cio i : map.keySet()) 
				addBuilding(i, map.get(i).getValueInt());
			
		}
		
		public void resetComponents() {
			
			for (Edif�cioFormattedTextField t : map.values())
				t.setText("");
			
		}
		
		public void setValues(Buildings buildings) {
			
			resetComponents();
			
			for (Building t : buildings.buildings)
				if (t.nivel > 0)
					map.get(t.edif�cio).setText(String.valueOf(t.nivel));	
				
			saveValues();
			
		}
		
		public Buildings getBuildings() {
			return Buildings.this;
		}
		
	}
}
