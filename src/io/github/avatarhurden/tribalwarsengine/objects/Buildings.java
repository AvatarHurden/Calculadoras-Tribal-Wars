package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.EdifícioFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
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
import database.Edifício;

/**
 * Essa classe mantém tudo relacionado a um conjunto de edifícios
 * 
 * @author Arthur
 *
 */
public class Buildings {
	
	private ArrayList<Building> buildings = new ArrayList<Building>();;
	
	public static boolean isBuildingJson(JSONObject json) {
		return json.has("buildings");
	}
	
	public static List<Edifício> getAvailableBuildings() {
		ArrayList<Edifício> list = new ArrayList<Edifício>();
		
    	for (Edifício ed : Edifício.values())
    		list.add(ed);
    	
    	if (!WorldManager.get().getSelectedWorld().isChurchWorld()) {
    		list.remove(Edifício.IGREJA);
    		list.remove(Edifício.PRIMEIRA_IGREJA);
    	}
    	if (WorldManager.get().getSelectedWorld().isCoiningWorld())
    		list.remove(Edifício.ACADEMIA_3NÍVEIS);
    	else
    		list.remove(Edifício.ACADEMIA_1NÍVEL);
    	
    	if (!WorldManager.get().getSelectedWorld().isPaladinWorld())
    		list.remove(Edifício.ESTÁTUA);
    	
    	return list;
	}
	
	public Buildings() {
		this(Arrays.asList(Edifício.values()));
	}

	public Buildings(Edifício... ed) {
		this(Arrays.asList(ed));
	}
	
	public Buildings(List<Edifício> list) {
		
		for (Edifício e : list)
			buildings.add(new Building(e, 0));
		
	}
	
	public void addBuilding(Edifício ed, int nível) {
		addBuilding(new Building(ed, nível));
	}
	
	public void addBuilding(Building b) {
		int position = buildings.size()-1;
		
		Iterator<Building> iter = buildings.iterator();
		while (iter.hasNext())
			if (iter.next().edifício.equals(b.edifício)) {
				position = buildings.indexOf(getBuilding(b.edifício));
				iter.remove();
			}
		
		buildings.add(position, b);
		
	}
	
	public boolean contains(Edifício u) {
		for (Building t : buildings)
			if (t.edifício.equals(u))
				return true;
		
		return false;
	}
	
	public Building getBuilding(Edifício ed) {
		for (Building b : buildings)
			if (b.edifício.equals(ed))
				return b;
		
		return null;
	}
	
	public int getLevel(Edifício ed) {
		
		for (Building b : buildings)
			if (b.edifício.equals(ed))
				return b.nivel;
		
		return -1;
	}
	
	public List<Building> getBuildings() {
		return buildings;
	}
	
	public List<Edifício> getEdifícios() {
		List<Edifício> list = new ArrayList<>();
		for (Building t : buildings)
			list.add(t.edifício);
		
		return list;
	}
	
	public int getPontos() {
		int points = 0;
		for (Building b : buildings)
			points += b.getPontos();
		return points;
	}
	
	public int getPopulaçãoTotal() {
		if (contains(Edifício.FAZENDA))
			return getBuilding(Edifício.FAZENDA).getPopulação();
		else
			return 0;
	}
	
	public int getPopulaçãoUsada() {
		int pop = 0;
		for (Building b : buildings)
			if (!b.edifício.equals(Edifício.FAZENDA))
				pop += b.getPopulação();
		return pop;
	}
	
	public int getPopulaçãoDisponível() {
		return getPopulaçãoTotal() - getPopulaçãoUsada();
	}
	
	/**
	 * Se o objeto possui Bosque, retorna a produção de madeira do mesmo, 
	 * em recursos/hora.
	 * <p> Caso não possua, retorna 0.
	 */
	public int getProduçãoMadeira() {
		if (contains(Edifício.BOSQUE)) {
			if (getLevel(Edifício.BOSQUE) == 0)
				return 5;
			else
				return (int) Math.round(
					30 * Math.pow(1.163118, getLevel(Edifício.BOSQUE)-1));
		} else
			return 0;
	}
	
	/**
	 * Se o objeto possui Poço de Argila, retorna a produção de argila do mesmo, 
	 * em recursos/hora.
	 * <p> Caso não possua, retorna 0.
	 */
	public int getProduçãoArgila() {
		if (contains(Edifício.POÇO_DE_ARGILA)) {
			if (getLevel(Edifício.POÇO_DE_ARGILA) == 0)
				return 5;
			else
				return (int) Math.round(
					30 * Math.pow(1.163118, getLevel(Edifício.POÇO_DE_ARGILA)-1));
		} else
			return 0;
	}
	
	/**
	 * Se o objeto possui Mina de Ferro, retorna a produção de ferro do mesmo, 
	 * em recursos/hora.
	 * <p> Caso não possua, retorna 0.
	 */
	public int getProduçãoFerro() {
		if (contains(Edifício.MINA_DE_FERRO)) {
			if (getLevel(Edifício.MINA_DE_FERRO) == 0)
				return 5;
			else
				return (int) Math.round(
					30 * Math.pow(1.163118, getLevel(Edifício.MINA_DE_FERRO)-1));
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
		
		if (contains(Edifício.ARMAZÉM)) {
			if (getLevel(Edifício.ARMAZÉM) == 0)
				armazenamento = 0;
			else
				armazenamento = (int) Math.round(
					1000 * Math.pow(1.2294934, getLevel(Edifício.ARMAZÉM)-1));
		} else
			return 0;
		
		if (usaEsconderijo && buildings.contains(getBuilding(Edifício.ESCONDERIJO))
				&& getLevel(Edifício.ESCONDERIJO) > 0)
			armazenamento -= (int) Math.round(
					150 * Math.pow(1.3333333, getLevel(Edifício.ESCONDERIJO)-1));
		
		return armazenamento;
	}
	
	public double getFatorProduçãoUnidade(Edifício ed) {
		
		if (!contains(ed))
			return 1;
		else
			return 2.0/3.0*Math.pow(1.06, -getLevel(ed)) / 
					WorldManager.get().getSelectedWorld().getWorldSpeed();
		
	}
	
	public BuildingsEditPanel getEditPanelFull(OnChange onChange) {
		return new BuildingsEditPanel(onChange, true, true, true);
	}
	
	public BuildingsEditPanel getEditPanelFullNoHeader(OnChange onChange) {
		return new BuildingsEditPanel(onChange, false, true, true);
	}
	
	public class Building {
		
		private Edifício edifício;
		private int nivel;
		
		private Building(Edifício ed, int nivel) {
			this.edifício = ed;
			this.nivel = nivel;
		}
		
		public int getNível() {
			return nivel;
		}
		
		public Edifício getEdifício() {
			return edifício;
		}
		
		public int getPontos() {
			return edifício.getPontos(nivel);
		}
		
		public int getPopulação() {
			return edifício.getPopulação(nivel);
		}
		
		private int getCustoFerroNext() {
			return edifício.getCustoFerro(nivel+1);
		}
		
		private int getCustoArgilaNext() {
			return edifício.getCustoArgila(nivel+1);
		}
		
		private int getCustoMadeiraNext() {
			return edifício.getCustoMadeira(nivel+1);
		}
		
	}
	
	
	
	public class BuildingsEditPanel extends JPanel {
		
		private List<JPanel> panels;
		private HashMap<Edifício, EdifícioFormattedTextField> map;
		private GridBagLayout layout;
		
		private boolean hasHeader, hasNames, hasLevels;
		private OnChange onChange;
		
		public BuildingsEditPanel(OnChange onChange, boolean hasHeader, 
				boolean hasNames, boolean hasLevels) {
			
			map = new HashMap<Edifício, EdifícioFormattedTextField>();
			panels = new ArrayList<JPanel>();
			
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
	        add(mainPanel(), c);
	       
		}
		
		private void setLayout() {
			
			layout = new GridBagLayout();
		    layout.columnWidths = new int[] {};
		    layout.rowHeights = new int[]{ 26 };
		    layout.columnWeights = new double[]{1, Double.MIN_VALUE};
		    layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			
		}
		
		private JPanel headerPanel() {
			
			JPanel panel = new JPanel();
			panel.setBackground(Cores.FUNDO_ESCURO);
			panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

			GridBagLayout layout = new GridBagLayout();
			layout.rowHeights = new int[] { 30 };
			layout.columnWeights = new double[]{1, Double.MIN_VALUE};
			panel.setLayout(layout);

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
		
		private JPanel mainPanel() {
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setOpaque(false);
			if (hasHeader)
				panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			for (int i = 0; i < buildings.size(); i++) {

				Edifício ed = buildings.get(i).edifício;
				
				JPanel edPanel = new JPanel(layout);
				edPanel.setBackground(Cores.getAlternar(i+1));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets(3, 5, 3, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(ed.toString());
				
				if (Buildings.this.contains(Edifício.ACADEMIA_1NÍVEL) 
						&& Buildings.this.contains(Edifício.ACADEMIA_3NÍVEIS)) {
				
					if (ed.equals(Edifício.ACADEMIA_1NÍVEL))
						label.setText(ed.toString() + " (1 nível)");
					else if (ed.equals(Edifício.ACADEMIA_3NÍVEIS))
						label.setText(ed.toString() + " (3 níveis)");
						
				}
				
				if (hasNames) {
					edPanel.add(label, panelC);
					panelC.gridx++;
				}
				
				EdifícioFormattedTextField txt = new EdifícioFormattedTextField(ed, getLevel(ed)) {
					public void go() {
						onChange.run();
					}
				};
				
				if (hasLevels) {
					edPanel.add(txt, panelC);
					panelC.gridx++;
				}
				map.put(ed, txt);
				
				panels.add(edPanel);
				panel.add(edPanel);
			}
		       
			return panel;
			
		}
		
		public void saveValues() {
			
			for (Edifício i : map.keySet()) 
				addBuilding(i, map.get(i).getValueInt());
			
		}
		
		public void resetComponents() {
			
			for (EdifícioFormattedTextField t : map.values())
				t.setText("");
			
		}
		
		public void setValues(Buildings buildings) {
			
			resetComponents();
			
			for (Building t : buildings.buildings)
				if (t.nivel > 0)
					map.get(t.edifício).setText(String.valueOf(t.nivel));	
				
			saveValues();
			
		}
		
		public Buildings getBuildings() {
			return Buildings.this;
		}
		
		public void setOpaquePanels(boolean isOpaque) {
			for (JPanel panel : panels)
				panel.setOpaque(false);
		}
		
	}
}
