package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TroopLevelComboBox;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import database.Cores;
import database.ItemPaladino;
import database.Unidade;
import database.Unidade.UnidadeTipo;

/**
 * Essa classe guarda informações relativas a um exército. Ela contém
 * as tropas disponíveis e as quantidades, e pode fornecer informações como ataque,
 * defesa, população etc
 * @author Arthur
 *
 */
public class Army {
	

	private List<Tropa> tropas = new ArrayList<Tropa>();
	
	private transient ItemPaladino item;
	
	public static boolean isArmyJson(JSONObject json) {
		return json.has("tropas");
		
	}
	
	public static ArrayList<Unidade> getAvailableUnits() {
		ArrayList<Unidade> list = new ArrayList<Unidade>();
		
    	for (Unidade u : Unidade.values())
    		list.add(u);
    	
    	if (!WorldManager.get().getSelectedWorld().isArcherWorld()) {
    		list.remove(Unidade.ARQUEIRO);
    		list.remove(Unidade.ARCOCAVALO);
    	}
    	if (!WorldManager.get().getSelectedWorld().isPaladinWorld())
    		list.remove(Unidade.PALADINO);
    	if (!WorldManager.get().getSelectedWorld().isMilitiaWorld())
    		list.remove(Unidade.MILÍCIA);
    	
    	return list;
	}
	
	public static ArrayList<Unidade> getAttackingUnits() {
		
		ArrayList<Unidade> list = getAvailableUnits();
    	
    	list.remove(Unidade.MILÍCIA);
    	
    	return list;
		
	}
	
	public Army() {
		this(Arrays.asList(Unidade.values()));
	}
	
	public Army(List<Unidade> units) {
		
		for (Unidade i : units)
			tropas.add(new Tropa(i, 0));
		
	}
	
	/**
	 * Adiciona uma nova tropa ao exército. Se já existe uma tropa com a
	 * unidade especificada, ela será removida
	 * 
	 * @param unidade
	 * @param quantidade
	 * @param nivel
	 */
	public void addTropa(Unidade unidade, int quantidade, int nivel) {
		
		Iterator<Tropa> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().unidade.equals(unidade))
				iter.remove();
		
		tropas.add(new Tropa(unidade, quantidade, nivel, nivel));
		
	}
	
	/**
	 * Adiciona uma nova tropa ao exército. Se já existe uma tropa com a
	 * unidade especificada, ela será removida
	 * 
	 * @param unidade
	 * @param quantidade
	 * @param nivel3 - Nível da Unidade em um mundo com pesquisa de 3 níveis
	 * @param nivel10 - Nível da Unidade em um mundo com pesquisa clássica
	 */
	public void addTropa(Unidade unidade, int quantidade, int nivel3, int nivel10) {
		addTropa(new Tropa(unidade, quantidade, nivel3, nivel10));
	}
	
	public void addTropa(Tropa tropa) {
		int position = tropas.size()-1;
		
		Iterator<Tropa> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().unidade.equals(tropa.unidade)) {
				position = tropas.indexOf(getTropa(tropa.unidade));
				iter.remove();
			}
		
		tropas.add(position, tropa);
	}
	
	public List<Tropa> getTropas() {
		return tropas;
	}
	
	public List<Unidade> getUnidades() {
		List<Unidade> list = new ArrayList<>();
		for (Tropa t : tropas)
			list.add(t.unidade);
		
		return list;
	}

	public int getQuantidade(Unidade i) {
		return getTropa(i).quantidade;
	}
	
	public Tropa getTropa(Unidade u) {
		for (Tropa t : tropas)
			if (t.unidade.equals(u))
				return t;
		
		return null;
	}
	
	public boolean contains(Unidade u) {
		for (Tropa t : tropas)
			if (t.unidade.equals(u))
				return true;
		
		return false;
	}
	
	public int getAtaque() {
		int ataque = 0;
		
		for (Tropa t : tropas)
			ataque += t.getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueGeral() {
		int ataque = 0;
		
		for (Tropa t : tropas)
			if (t.getTipo().equals(UnidadeTipo.Geral))
				ataque += t.getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueCavalaria() {
		int ataque = 0;
		
		for (Tropa t : tropas)
			if (t.getTipo().equals(UnidadeTipo.Cavalo))
				ataque += t.getAtaque(item);
			
		return ataque;
	}
	
	public int getAtaqueArqueiro() {
		int ataque = 0;
		
		for (Tropa t : tropas)
			if (t.getTipo().equals(UnidadeTipo.Arqueiro))
				ataque += t.getAtaque(item);
	
		return ataque;
	}
	
	public int getDefesaGeral() {
		int defesa = 0;
		
		for (Tropa t : tropas)
			defesa += t.getDefesaGeral(item);
	
		return defesa;
	}
	
	public int getDefesaCavalaria() {
		int defesa = 0;
		
		for (Tropa t : tropas)
			defesa += t.getDefesaCavalaria(item);
		
		return defesa;
	}
	
	public int getDefesaArqueiro() {
		int defesa = 0;
		
		for (Tropa t : tropas)
			defesa += t.getDefesaArqueiro(item);
	
		return defesa;
	}
	
	public int getSaque() {
		int saque = 0;
		
		for (Tropa t : tropas)
			saque += t.getSaque();
		
		return saque;
	}
	
	public int getPopulação() {
		int população = 0;
		
		for (Tropa t : tropas)
			população += t.getPopulação();
		
		return população;
	}
	
	public int getCustoMadeira() {
		int madeira = 0;
		
		for (Tropa t : tropas)
			madeira += t.getCustoMadeira();
	
		return madeira;
	}
	
	public int getCustoArgila() {
		int argila = 0;
		
		for (Tropa t : tropas)
			argila += t.getCustoArgila();
	
		return argila;
	}
	
	public int getCustoFerro() {
		int ferro = 0;
		
		for (Tropa t : tropas)
			ferro += t.getCustoFerro();
		
		return ferro;
	}
	
	public int getODAtaque() {
		int ODA = 0;
		
		for (Tropa t : tropas)
			ODA += t.getODAtaque();
		
		return ODA;
	}

	public int getODDefesa() {
		int ODD = 0;
		
		for (Tropa t : tropas)
			ODD += t.getODDefesa();
		
		return ODD;
	}
	
	/**
	 * Retorna a "velocidade" da unidade mais lenta do exército, em milissegundos/campo.
	 * São consideradas a velocidade e modificador de unidade do mundo.
	 */
	public double getVelocidade() {
		
		double slowest = 0;
		
		for (Tropa t : tropas)
			if (t.getVelocidade() > slowest)
				slowest = t.getVelocidade();
	
		// Transforma de minutos/campo para milissegundos/campo
		return slowest*60000;
	}
	
	public ArmyEditPanel getEditPanelFull(OnChange onChange) {
		return new ArmyEditPanel(onChange, true, true, true, true, true);
	}
	
	public ArmyEditPanel getEditPanelFullNoHeader(OnChange onChange) {
		return new ArmyEditPanel(onChange, false, true, true, true, true);
	}
	
	public ArmyEditPanel getEditPanelWorldLevels(OnChange onChange) {
		int levels = WorldManager.get().getSelectedWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, true, true, true, levels == 3, levels == 10);
	}
	
	public ArmyEditPanel getEditPanelWorldLevelsNoHeader(OnChange onChange) {
		int levels = WorldManager.get().getSelectedWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, false, true, true, levels == 3, levels == 10);
	}
	
	public ArmyEditPanel getEditPanelNoLevels(OnChange onChange) {
		return new ArmyEditPanel(onChange, true, true, true, false, false);
	}
	
	public ArmyEditPanel getEditPanelNoLevelsNoHeader(OnChange onChange) {
		return new ArmyEditPanel(onChange, false, true, true, false, false);
	}
	
	/**
	 * Classe que representa uma tropa específica, com a unidade, nível e quantidade
	 * @author Arthur
	 *
	 */
	public class Tropa {
		
		private Unidade unidade;
		private int quantidade;
		private int nivel3;
		private int nivel10;
		
		private transient int nivel;
		
		private Tropa(Unidade unidade, int quantidade, int nivel3, int nivel10) {
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel3 = nivel3;
			this.nivel10 = nivel10;
			
			if (WorldManager.get().getSelectedWorld().getResearchSystem().getResearch() == 10)
				nivel = nivel10;
			else if (WorldManager.get().getSelectedWorld().getResearchSystem().getResearch() == 3)
				nivel = nivel3;
			else
				nivel = 1;
		}
		
		private Tropa(Unidade unidade, int quantidade) {
			this(unidade, quantidade, 1, 1);
		}
		
		public int getAtaque() {
			return getAtaque(ItemPaladino.NULL);
		}
		
		public int getAtaque(ItemPaladino item) {
			return unidade.getAtaque(nivel, item) * quantidade;
		}
		
		public int getDefesaGeral() {
			return getDefesaGeral(ItemPaladino.NULL);
		}
	
		public int getDefesaGeral(ItemPaladino item) {
			return unidade.getDefGeral(nivel, item) * quantidade;
		}
		
		public int getDefesaCavalaria() {
			return getDefesaCavalaria(ItemPaladino.NULL);
		}
		
		public int getDefesaCavalaria(ItemPaladino item) {
			return unidade.getDefCav(nivel, item) * quantidade;
		}
		
		public int getDefesaArqueiro() {
			return getDefesaArqueiro(ItemPaladino.NULL);
		}
		
		public int getDefesaArqueiro(ItemPaladino item) {
			return unidade.getDefArq(nivel, item) * quantidade;
		}
		
		public int getCustoMadeira() {
			return unidade.getMadeira() * quantidade;
		}
		
		public int getCustoArgila() {
			return unidade.getArgila() * quantidade;
		}
		
		public int getCustoFerro() {
			return unidade.getFerro() * quantidade;
		}
		
		public int getPopulação() {
			return unidade.getPopulação() * quantidade;
		}
		
		public int getSaque() {
			return unidade.getSaque() * quantidade;
		}
		
		public int getTempoProdução() {
			return unidade.getTempoDeProdução() * quantidade * 1000;
		}
		
		public int getODAtaque() {
			return unidade.getODA() * quantidade;
		}
		
		public int getODDefesa() {
			return unidade.getODD() * quantidade;
		}
		
		public double getVelocidade() {
			if (quantidade == 0)
				return 0;
			else
				return unidade.getVelocidade()
						* WorldManager.get().getSelectedWorld().getWorldSpeed()
						* WorldManager.get().getSelectedWorld().getUnitModifier();
		}
		
		public UnidadeTipo getTipo() {
			return unidade.getType();
		}
		
		public Unidade getUnidade() {
			return unidade;
		}
		
		public int getQuantidade() {
			return quantidade;
		}

	}
	
	@SuppressWarnings("serial")
	public class ArmyEditPanel extends JPanel {
		
		private HashMap<Unidade, IntegerFormattedTextField> quantities;
		private HashMap<Unidade, TroopLevelComboBox> level3;
		private HashMap<Unidade, TroopLevelComboBox> level10;
		
		private GridBagLayout layout;
		
		private boolean hasHeader, hasNames, hasAmount, hasNivel3, hasNivel10;
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
		private ArmyEditPanel(OnChange onChange, boolean hasHeader, 
				boolean hasNames, boolean hasAmount,
				boolean hasNivel3, boolean hasNivel10) {
			
			quantities = new HashMap<Unidade, IntegerFormattedTextField>();
			level3 = new HashMap<Unidade, TroopLevelComboBox>();
			level10 = new HashMap<Unidade, TroopLevelComboBox>();
			
			this.hasHeader = hasHeader;
			this.hasNames = hasNames;
			this.hasAmount = hasAmount;
			this.hasNivel3 = hasNivel3;
			this.hasNivel10 = hasNivel10;
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
			if (hasAmount)
				widths.add(100);
			if (hasNivel3)
				widths.add(40);
			if (hasNivel10)
				widths.add(40);
			
			int[] widthsArray = new int[widths.size()];
			
			for (int i = 0; i < widthsArray.length; i++)
				widthsArray[i] = widths.get(i);
			
			layout = new GridBagLayout();
		    layout.columnWidths = widthsArray;
		    layout.rowHeights = new int[]{ 30 };
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
			
			if (hasNames)
				panel.add(new JLabel("Unidade"), c);
			
			if (hasAmount) {
				c.gridx++;
				panel.add(new JLabel("Quantidade"), c);
			}
			
			if (hasNivel3 && hasNivel10) {
				c.gridx++;
				panel.add(new JLabel("<html><center>Nível<br>(3)</center></html>"), c);
				c.gridx++;
				panel.add(new JLabel("<html><center>Nível<br>(10)</center></html>"), c);
			} else {
				if (hasNivel3) {
					c.gridx++;
					panel.add(new JLabel("Nível"), c);
				}
			
				if (hasNivel10) {
					c.gridx++;
					panel.add(new JLabel("Nível"), c);
				}
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
				
				Unidade u = tropas.get(i).unidade;
				
				JPanel unitPanel = new JPanel(layout);
				unitPanel.setBackground(Cores.getAlternar(i+1));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets(5, 5, 5, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(u.toString());
				label.setHorizontalAlignment(SwingConstants.LEADING);
				if (hasNames) {
					unitPanel.add(label, panelC);
					panelC.gridx++;
				}
				
				IntegerFormattedTextField txt = new IntegerFormattedTextField(getQuantidade(u)) {
					public void go() {
						onChange.run();
					}
				};
				
				quantities.put(u, txt);
	
				if (hasAmount) {
					unitPanel.add(txt, panelC);
					panelC.gridx++;
				}
				
				TroopLevelComboBox combo3 = new TroopLevelComboBox(3, unitPanel.getBackground());
				combo3.setSelectedItem(Army.this.getTropa(u).nivel3);
				level3.put(u, combo3);
					
				combo3.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (hasNivel3) {
					unitPanel.add(combo3, panelC);
					panelC.gridx++;
				}
				
				TroopLevelComboBox combo10 = new TroopLevelComboBox(10, unitPanel.getBackground());
				combo10.setSelectedItem(Army.this.getTropa(u).nivel10);
				level10.put(u, combo10);
					
				combo10.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (hasNivel10){
					unitPanel.add(combo10, panelC);
					panelC.gridx++;
				}
				
				panel.add(unitPanel);
			}
			
			return panel;
			
		}
		
		public void saveValues() {
			
			for (Unidade i : quantities.keySet())
				Army.this.addTropa(i, quantities.get(i).getValue().intValue(),
						(int) level3.get(i).getSelectedItem(),
						(int) level10.get(i).getSelectedItem() );
		}
		
		public void resetComponents() {
			
			for (IntegerFormattedTextField t : quantities.values())
				t.setText("");
			for (TroopLevelComboBox t : level3.values())
				t.setSelectedIndex(0);
			for (TroopLevelComboBox t : level10.values())
				t.setSelectedIndex(0);
			
		}
		
		public void setValues(Army army) {
			
			resetComponents();
			
			for (Tropa t : army.tropas)
				if (getUnidades().contains(t.unidade)) {
					if (t.quantidade > 0)
						quantities.get(t.unidade).setText(String.valueOf(t.quantidade));	
					level3.get(t.unidade).setSelectedItem(t.nivel3);
					level10.get(t.unidade).setSelectedItem(t.nivel10);
			}
			
			saveValues();
			
		}
		
		public Army getArmy() {
			return Army.this;
		}
		
	}
	
}
