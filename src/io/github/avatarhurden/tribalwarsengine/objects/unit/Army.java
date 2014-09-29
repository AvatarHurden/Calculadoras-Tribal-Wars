package io.github.avatarhurden.tribalwarsengine.objects.unit;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TroopLevelComboBox;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.ItemPaladino;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit.UnitType;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

/**
 * Essa classe guarda informações relativas a um exército. Ela contém
 * as tropas disponíveis e as quantidades, e pode fornecer informações como ataque,
 * defesa, população etc
 * @author Arthur
 *
 */
public class Army {
	
	private ArrayList<Troop> tropas = new ArrayList<Troop>();
	
	private transient ItemPaladino item = ItemPaladino.NULL;
	private transient boolean religious = true;
	
	private transient double moral = 1;
	private transient double luck = 0;
	
	private transient int wall = -1;
	private transient boolean night = false;
	
	public static boolean isArmyJson(JSONObject json) {
		return json.has("tropas");
	}
	
	public static List<Unit> getAvailableUnits() {
    	return WorldManager.getSelectedWorld().getUnits();
	}
	
	public static List<Unit> getAttackingUnits() {
		ArrayList<Unit> list = new ArrayList<Unit>(getAvailableUnits());
    	
		Iterator<Unit> iter = list.iterator();
		while (iter.hasNext())
			if (iter.next().getName().equals("militia"))
				iter.remove();
    	
    	return list;
		
	}
	
	public static boolean containsUnit(String name) {
		for (Unit u : getAvailableUnits())
			if (u.getName().equals(name))
				return true;
		
		return false;
	}
	
	public Army() {
		this(getAvailableUnits());
	}
	
	public Army(Unit... units) {
		this(Arrays.asList(units));
	}
	
	public Army(List<Unit> units) {
		
		for (Unit i : units)
			tropas.add(new Troop(i, 0));
		
	}
	
	public Army(String... names) {
		
		List<String> namesList = new ArrayList<String>(Arrays.asList(names));
		
		for (Unit u : getAvailableUnits())
			if (namesList.contains(u.getName()))
				tropas.add(new Troop(u, 0));
		
	}
	
	/**
	 * Adiciona uma nova tropa ao exército. Se já existe uma tropa com a
	 * unidade especificada, ela será removida
	 * 
	 * @param unidade
	 * @param quantidade
	 * @param nivel
	 */
	public void addTropa(Unit unidade, int quantidade, int nivel) {
		addTropa(new Troop(unidade, quantidade, nivel));
	}
	
	public void addTropa(Troop tropa) {
		int position = tropas.size()-1;
		
		Iterator<Troop> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().getUnit().equals(tropa.getUnit())) {
				position = tropas.indexOf(getTropa(tropa.getUnit()));
				iter.remove();
			}
		
		tropas.add(position, tropa);
	}
	
	public void setItem(ItemPaladino item) {
		this.item = item;
	}
	
	public void setReligious(boolean religious) {
		this.religious = religious;
	}

	public void setMoral(double moral) {
		this.moral = moral;
	}

	public void setLuck(double luck) {
		this.luck = luck;
	}

	public void setWall(int wall) {
		this.wall = wall;
	}

	public void setNight(boolean night) {
		this.night = night;
	}

	public ArrayList<Troop> getTropas() {
		return tropas;
	}
	
	public List<Unit> getUnits() {
		List<Unit> list = new ArrayList<Unit>();
		for (Troop t : tropas)
			list.add(t.getUnit());
		
		return list;
	}
	
	public int getQuantidade(String name) {
		try {
			return getTropa(name).getQuantity();
		} catch (Exception e) {
			return 0;
		}
	}

	public int getQuantidade(Unit unidade) {
		return getTropa(unidade).getQuantity();
	}
	
	public Troop getTropa(Unit unidade) {
		for (Troop t : tropas)
			if (t.getUnit().equals(unidade)) 
				return t;
		
		return null;
	}
	
	public Troop getTropa(String name) {
		for (Troop t : tropas)
			if (t.getName().equals(name)) 
				return t;
		
		return null;
	}
	
	public Unit getUnit(String name) {
		for (Troop t : tropas)
			if (t.getName().equals(name)) 
				return t.getUnit();
		
		return null;
	}
	
	public boolean contains(String name) {
		for (Troop t : tropas)
			if (t.getName().equals(name))
				return true;
		
		return false;
	}
	
	public int getAtaque() {
		int ataque = 0;
		
		for (Troop t : tropas)
			ataque += t.getAttack(item);
		
		return (int) applyModifiers(ataque);
	}
	
	public int getAtaqueGeral() {
		int ataque = 0;
		
		for (Troop t : tropas)
			if (t.getType().equals(UnitType.General))
				ataque += t.getAttack(item);
		
		return (int) applyModifiers(ataque);
	}
	
	public int getAtaqueCavalaria() {
		int ataque = 0;
		
		for (Troop t : tropas)
			if (t.getType().equals(UnitType.Cavalry))
				ataque += t.getAttack(item);
			
		return (int) applyModifiers(ataque);
	}
	
	public int getAtaqueArqueiro() {
		int ataque = 0;
		
		for (Troop t : tropas)
			if (t.getType().equals(UnitType.Archer))
				ataque += t.getAttack(item);
	
		return (int) applyModifiers(ataque);
	}
	
	public double getDefesaGeral() {
		int defesa = 0;
		
		for (Troop t : tropas)
			defesa += t.getDefense(item);
	
		return applyModifiers(defesa);
	}
	
	public double getDefesaCavalaria() {
		int defesa = 0;
		
		for (Troop t : tropas)
			defesa += t.getDefenseCavalry(item);
		
		return applyModifiers(defesa);
	}
	
	public double getDefesaArqueiro() {
		int defesa = 0;
		
		for (Troop t : tropas)
			defesa += t.getDefenseArcher(item);
	
		return applyModifiers(defesa);
	}
	
	public int getSaque() {
		int saque = 0;
		
		for (Troop t : tropas)
			saque += t.getHaul();
		
		return saque;
	}
	
	public int getPopulação() {
		int população = 0;
		
		for (Troop t : tropas)
			população += t.getPopulation();
		
		return população;
	}
	
	public int getCustoMadeira() {
		int madeira = 0;
		
		for (Troop t : tropas)
			madeira += t.getCostWood();
	
		return madeira;
	}
	
	public int getCustoArgila() {
		int argila = 0;
		
		for (Troop t : tropas)
			argila += t.getCostClay();
	
		return argila;
	}
	
	public int getCustoFerro() {
		int ferro = 0;
		
		for (Troop t : tropas)
			ferro += t.getCostIron();
		
		return ferro;
	}
	
	public int getODAtaque() {
		int ODA = 0;
		
		for (Troop t : tropas)
			ODA += t.getODAttacker();
		
		return ODA;
	}

	public int getODDefesa() {
		int ODD = 0;
		
		for (Troop t : tropas)
			ODD += t.getODDefender();
		
		return ODD;
	}
	
	/**
	 * Retorna a "velocidade" da unidade mais lenta do exército, em milissegundos/campo.
	 * São consideradas a velocidade e modificador de unidade do mundo.
	 */
	public double getVelocidade() {
		
		double slowest = 0;
		
		for (Troop t : tropas)
			if (t.getSpeed() > slowest)
				slowest = t.getSpeed();
	
		// Transforma de minutos/campo para milissegundos/campo
		return slowest*60000;
	}
	
	public long getTempoProdução() {
		long tempo = 0;
		
		for (Troop t : tropas)
			tempo += t.getProductionTime();
		
		return tempo;
	}
	
	public double applyModifiers(double value) {
		
		value *= moral;
		value *= (1 + luck);
		
		if (!religious)
			value /= 2;
		
		if (night)
			value *= 2;
		
		if (wall > -1) {
			BuildingBlock wallBlock = new BuildingBlock("wall");
			wallBlock.addConstruction(wallBlock.getBuilding("wall"), wall);
			
			value *= wallBlock.getWallBonusPercent();
			value += wallBlock.getWallBonusFlat();
		}
		
		return value;
	}
	
	public ArmyEditPanel getEditPanelFull(OnChange onChange, int height) {
		int levels = WorldManager.getSelectedWorld().getWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, height, true, true, false, true, levels > 1);
	}
	
	public ArmyEditPanel getEditPanelFullNoHeader(OnChange onChange, int height) {
		int levels = WorldManager.getSelectedWorld().getWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, height, false, true, false, true, levels > 1);
	}
	
	public ArmyEditPanel getEditPanelNoLevels(OnChange onChange, int height) {
		return new ArmyEditPanel(onChange, height, true, true, false, true, false);
	}
	
	public ArmyEditPanel getEditPanelNoLevelsNoHeader(OnChange onChange, int height) {
		return new ArmyEditPanel(onChange, height, false, true, false, true, false);
	}
	
	public ArmyEditPanel getEditPanelNoNames(OnChange onChange, int height) {
		int levels = WorldManager.getSelectedWorld().getWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, height, true, false, false, true, levels > 1);
	}
	
	public ArmyEditPanel getEditPanelNoNamesNoHeader(OnChange onChange, int height) {
		int levels = WorldManager.getSelectedWorld().getWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, height, false, false, false, true, levels > 1);
	}
	
	public ArmyEditPanel getEditPanelSelection(OnChange onChange, int height) {
		return new ArmyEditPanel(onChange, height, true, true, true, false, false);
	}
	
	public ArmyEditPanel getEditPanelSelectionNoHeader(OnChange onChange, int height) {
		return new ArmyEditPanel(onChange, height, false, true, true, false, false);
	}
	
	public ArmyEditPanel getEditPanelNoInputs(int height) {
		return new ArmyEditPanel(null, height, true, true, false, false, false);
	}
	
	public ArmyEditPanel getEditPanelNoInputsNoHeader(int height) {
		return new ArmyEditPanel(null, height, false, true, false, false, false);
	}
	
	public class ArmyEditPanel extends JPanel {
		
		private HashMap<Unit, JCheckBox> selected;
		private HashMap<Unit, IntegerFormattedTextField> quantities;
		private HashMap<Unit, TroopLevelComboBox> level;
		
		private GridBagLayout layout;
		private int height;
		
		private boolean hasHeader, hasNames, hasSelected, hasAmount, hasNivel;
		private OnChange onChange;
		
		/**
		 * Cria um JPanel para edição de um Army. É possível escolher quais 
		 * componentes ele possui. <p>Caso <code>hasHeader</code> seja <code>false</code>,
		 * o resto do JPanel não possui uma borda. Caso seja <code>true</code>,
		 * o header fica localizado a 5px acima das informações, com uma borda em
		 * volta de cada um. <p>Para que todas as unidades possam ser modificadas
		 * pelo JPanel, deixar <code>unidades</code> em branco.
		 * 
		 * @param onChange
		 * @param hasHeader
		 * @param hasNames
		 * @param hasAmount
		 * @param hasNivel3
		 * @param hasNivel10
		 * @param unidades
		 */
		private ArmyEditPanel(OnChange onChange, int height, boolean hasHeader, 
				boolean hasNames, boolean hasSelected, boolean hasAmount,
				boolean hasNivel) {
			
			selected = new HashMap<Unit, JCheckBox>();
			quantities = new HashMap<Unit, IntegerFormattedTextField>();
			level = new HashMap<Unit, TroopLevelComboBox>();
			
			this.height = height;
			this.hasHeader = hasHeader;
			this.hasNames = hasNames;
			this.hasSelected = hasSelected;
			this.hasAmount = hasAmount;
			this.hasNivel = hasNivel;
			
			if (onChange == null)
				this.onChange = new OnChange() { public void run() {}};
			else
				this.onChange = onChange;
				
			setLayout();
			
			setOpaque(false);
			
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
			
			ArrayList<Integer> widths = new ArrayList<Integer>();
			if (hasNames)
				widths.add(125);
			if (hasSelected)
				widths.add(30);
			if (hasAmount)
				widths.add(100);
			if (hasNivel)
				widths.add(40);
			
			int[] widthsArray = new int[widths.size()];
			
			for (int i = 0; i < widthsArray.length; i++)
				widthsArray[i] = widths.get(i);
			
			layout = new GridBagLayout();
		    layout.columnWidths = widthsArray;
		    layout.rowHeights = new int[]{ height };
		    layout.columnWeights = new double[]{1, Double.MIN_VALUE};
		    layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			
		}
		
		private JPanel headerPanel() {
			
			JPanel panel = new JPanel();
			panel.setBackground(Cores.FUNDO_ESCURO);
			panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			panel.setLayout(layout);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.CENTER;
			
			if (hasNames) {
				panel.add(new JLabel("Unidade"), c);
				c.gridx++;
			}
			if (hasAmount) {
				panel.add(new JLabel("Quantidade"), c);
				c.gridx++;
			}
			
			if (hasSelected) {
				c.gridx++;
			}
			
			if (hasNivel) {
				panel.add(new JLabel("Nível"), c);
				c.gridx++;
			}
		
			return panel;
		}
		
		private JPanel mainPanel() {
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setOpaque(false);
			if (hasHeader)
				panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			for (int i = 0; i < tropas.size(); i++) {
				
				Unit u = tropas.get(i).getUnit();
				
				JPanel unitPanel = new JPanel(layout);
				unitPanel.setBackground(Cores.getAlternar(i+1));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets((height - 20) / 2, 5, (height - 20) / 2, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(u.toString());
				label.setHorizontalAlignment(SwingConstants.LEADING);
				if (hasNames) {
					unitPanel.add(label, panelC);
					panelC.gridx++;
				}
				
				JCheckBox chck = new JCheckBox();
				chck.setOpaque(false);
				selected.put(u, chck);
				
				chck.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						onChange.run();
					}
				});
				
				if (hasSelected) {
					unitPanel.add(chck, panelC);
					panelC.gridx++;
				}
				
				IntegerFormattedTextField txt = new IntegerFormattedTextField(getQuantidade(u), onChange);
				quantities.put(u, txt);
	
				if (hasAmount) {
					unitPanel.add(txt, panelC);
					panelC.gridx++;
				}
				
				int niveis = WorldManager.getSelectedWorld().getWorld().getResearchSystem().getResearch();
				
				TroopLevelComboBox combo = new TroopLevelComboBox(niveis, unitPanel.getBackground());
				combo.setSelectedItem(Army.this.getTropa(u).getLevel());
				level.put(u, combo);
					
				combo.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (hasNivel) {
					unitPanel.add(combo, panelC);
					panelC.gridx++;
				}
				
				panel.add(unitPanel);
			}
			
			return panel;
			
		}
		
		public void saveValues() {
			
			for (Unit i : quantities.keySet())
				if (hasSelected)
					Army.this.addTropa(i, selected.get(i).isSelected() ? 1 : 0, 
							(int) level.get(i).getSelectedItem());
				else
					Army.this.addTropa(i, quantities.get(i).getValue(),
						(int) level.get(i).getSelectedItem());
		}
		
		public void resetComponents() {
			
			for (IntegerFormattedTextField t : quantities.values())
				t.setText("");
			for (JCheckBox c : selected.values())
				c.setSelected(false);
			for (TroopLevelComboBox t : level.values())
				t.setSelectedIndex(0);
			
		}
		
		public void setValues(Army army) {
			
			resetComponents();
			
			for (Troop t : army.getTropas())
				if (getTropa(t.getUnit()) != null) {
					if (t.getQuantity() > 0)
						quantities.get(t.getUnit()).setText(String.valueOf(t.getQuantity()));	
					level.get(t.getUnit()).setSelectedItem(t.getLevel());
			}
			
			saveValues();
			
		}
		
		public Army getArmy() {
			return Army.this;
		}
		
	}
	
}
