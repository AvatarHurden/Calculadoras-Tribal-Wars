package io.github.avatarhurden.tribalwarsengine.objects.building;

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

/**
 * Essa classe mantém tudo relacionado a um conjunto de edifícios
 * 
 * @author Arthur
 *
 */
public class BuildingBlock {
	
	private ArrayList<Construction> buildings = new ArrayList<Construction>();;
	
	public static boolean isBuildingJson(JSONObject json) {
		return json.has("buildings");
	}
	
	public static List<Building> getAvailableBuildings() {
    	return WorldManager.getSelectedWorld().getBuildings();
	}
	
	public BuildingBlock() {
		this(getAvailableBuildings());
	}

	public BuildingBlock(Building... ed) {
		this(Arrays.asList(ed));
	}
	
	public BuildingBlock(List<Building> list) {
		
		for (Building e : list)
			buildings.add(new Construction(e, e.getMinLevel()));
		
	}
	
	public BuildingBlock(String... names) {
		
		List<String> namesList = new ArrayList<String>(Arrays.asList(names));
		
		for (Building u : getAvailableBuildings())
			if (namesList.contains(u.getName()))
				buildings.add(new Construction(u, u.getMinLevel()));
		
	}
	
	public void addConstruction(Building ed, int nível) {
		addConstruction(new Construction(ed, nível));
	}
	
	public void addConstruction(Construction b) {
		int position = buildings.size()-1;
		
		Iterator<Construction> iter = buildings.iterator();
		while (iter.hasNext())
			if (iter.next().getEdifício().equals(b.getEdifício())) {
				position = buildings.indexOf(getConstruction(b.getEdifício()));
				iter.remove();
			}
		
		buildings.add(position, b);
		
	}
	
	public boolean contains(Building building) {
		for (Construction t : buildings)
			if (t.equals(building))
				return true;
		
		return false;
	}
	
	public boolean contains(String name) {
		for (Construction t : buildings)
			if (t.getName().equals(name))
				return true;
		
		return false;
	}
	
	public Building getBuilding(String name) {
		for (Construction b : buildings)
			if (b.getName().equals(name))
				return b.getEdifício();
		
		return null;
	}
	
	public Construction getConstruction(Building ed) {
		for (Construction b : buildings)
			if (b.getEdifício().equals(ed))
				return b;
		
		return null;
	}
	
	public Construction getConstruction(String name) {
		for (Construction b : buildings)
			if (b.getName().equals(name))
				return b;
		
		return null;
	}
	
	public int getLevel(String name) {
		
		for (Construction b : buildings)
			if (b.getName().equals(name))
				return b.getNível();
		
		return -1;
	}
	
	public List<Construction> getBuildings() {
		return buildings;
	}
	
	public List<Building> getEdifícios() {
		List<Building> list = new ArrayList<>();
		for (Construction t : buildings)
			list.add(t.getEdifício());
		
		return list;
	}
	
	public int getPontos() {
		int points = 0;
		for (Construction b : buildings)
			points += b.getPontos();
		return points;
	}
	
	public int getPopulaçãoDisponível() {
		if (contains("farm"))
			return (int) Math.round(240 * Math.pow(1.17, getLevel("farm")-1));
		else
			return 0;
	}
	
	public int getPopulaçãoUsada() {
		int pop = 0;
		for (Construction b : buildings)
			if (!b.getName().equals("farm"))
				pop += b.getPopulação();
		return pop;
	}
	
	public int getPopulaçãoRestante() {
		return getPopulaçãoDisponível() - getPopulaçãoUsada();
	}
	
	public int getProduçãoMadeira() {
		return getProduction("wood");
	}
	
	public int getProduçãoArgila() {
		return getProduction("stone");
	}

	public int getProduçãoFerro() {
		return getProduction("iron");
	}
	
	private int getProduction(String name) {
		if (contains(name)) {
			if (getLevel(name) == 0)
				return 5;
			else
				return (int) Math.round(
						WorldManager.getSelectedWorld().getWorld().getBaseProduction() 
						* Math.pow(1.163118, getLevel(name)-1));
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
		
		if (contains("storage")) {
			if (getLevel("storage") == 0)
				armazenamento = 0;
			else
				armazenamento = (int) Math.round(
					1000 * Math.pow(1.2294934, getLevel("storage")-1));
		} else
			return 0;
		
		if (usaEsconderijo && contains("hide") && getLevel("hide") > 0)
			armazenamento -= (int) Math.round(
					150 * Math.pow(1.3333333, getLevel("hide")-1));
		
		return armazenamento;
	}
	
	public double getFatorProduçãoUnidade(Building ed) {
		
		if (!contains(ed.getName()))
			return 1;
		else
			return 2.0/3.0*Math.pow(1.06, -getLevel(ed.getName())); 
		
	}
	
	public double getWallBonusFlat() {
		
		if (!contains("wall"))
			return 20;
		else {
			return 20 + 50 * getLevel("wall");
		}
	}
	
	public double getWallBonusPercent() {
		
		if (!contains("wall"))
			return 1;
		else {
			return Math.pow(1.037, getLevel("wall"));
		}
		
	}
	
	public BuildingsEditPanel getEditPanelFull(OnChange onChange) {
		return new BuildingsEditPanel(onChange, true, true, true);
	}
	
	public BuildingsEditPanel getEditPanelFullNoHeader(OnChange onChange) {
		return new BuildingsEditPanel(onChange, false, true, true);
	}
	
	public class BuildingsEditPanel extends JPanel {
		
		private List<JPanel> panels;
		private HashMap<Building, EdifícioFormattedTextField> map;
		private GridBagLayout layout;
		
		private boolean hasHeader, hasNames, hasLevels;
		private OnChange onChange;
		
		public BuildingsEditPanel(OnChange onChange, boolean hasHeader, 
				boolean hasNames, boolean hasLevels) {
			
			map = new HashMap<Building, EdifícioFormattedTextField>();
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

				Building ed = buildings.get(i).getEdifício();
				
				JPanel edPanel = new JPanel(layout);
				edPanel.setBackground(Cores.getAlternar(i+1));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets(3, 5, 3, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(ed.toString());
			
				if (hasNames) {
					edPanel.add(label, panelC);
					panelC.gridx++;
				}
				
				EdifícioFormattedTextField txt = new EdifícioFormattedTextField(ed, getLevel(ed.getName())) {
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
			
			for (Building i : map.keySet()) 
				addConstruction(i, map.get(i).getValueInt());
			
		}
		
		public void resetComponents() {
			
			for (EdifícioFormattedTextField t : map.values())
				t.setText("");
			
		}
		
		public void setValues(BuildingBlock buildings) {
			
			resetComponents();
			
			for (Construction t : buildings.buildings)
				if (t.getNível() > 0)
					map.get(t.getEdifício()).setText(String.valueOf(t.getNível()));	
				
			saveValues();
			
		}
		
		public BuildingBlock getBuildings() {
			return BuildingBlock.this;
		}
		
		public void setOpaquePanels(boolean isOpaque) {
			for (JPanel panel : panels)
				panel.setOpaque(false);
		}
		
	}
}
