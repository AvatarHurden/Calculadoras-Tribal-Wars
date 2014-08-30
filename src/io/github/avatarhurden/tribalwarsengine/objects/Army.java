package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TroopLevelComboBox;
import io.github.avatarhurden.tribalwarsengine.managers.ServerManager;
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
	
	private transient ItemPaladino item = ItemPaladino.NULL;
	
	public static boolean isArmyJson(JSONObject json) {
		return json.has("tropas");
		
	}
	
	public static ArrayList<Unidade> getAvailableUnits() {
		
    	return new ArrayList<Unidade>(Arrays.asList(Unidade.values()));
	}
	
	public static ArrayList<Unidade> getAttackingUnits() {
		
		ArrayList<Unidade> list = getAvailableUnits();
    	
    	list.remove(Unidade.MILÍCIA);
    	
    	return list;
		
	}
	
	public Army() {
		this(Arrays.asList(Unidade.values()));
	}
	
	public Army(Unidade... units) {
		this(Arrays.asList(units));
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
		
		tropas.add(new Tropa(unidade, quantidade, nivel));
		
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
	
	public long getTempoProdução() {
		long tempo = 0;
		
		for (Tropa t : tropas)
			tempo += t.getTempoProdução();
		
		return tempo;
	}
	
	public ArmyEditPanel getEditPanelFull(OnChange onChange) {
		int levels = ServerManager.getSelectedServer().getWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, true, true, false, true, levels > 1);
	}
	
	public ArmyEditPanel getEditPanelFullNoHeader(OnChange onChange) {
		int levels = ServerManager.getSelectedServer().getWorld().getResearchSystem().getResearch();
		return new ArmyEditPanel(onChange, false, true, false, true, levels > 1);
	}
	
	public ArmyEditPanel getEditPanelNoLevels(OnChange onChange) {
		return new ArmyEditPanel(onChange, true, true, false, true, false);
	}
	
	public ArmyEditPanel getEditPanelNoLevelsNoHeader(OnChange onChange) {
		return new ArmyEditPanel(onChange, false, true, false, true, false);
	}
	
	public ArmyEditPanel getEditPanelSelectetion(OnChange onChange) {
		return new ArmyEditPanel(onChange, true, true, true, false, false);
	}
	
	public ArmyEditPanel getEditPanelSelectionNoHeader(OnChange onChange) {
		return new ArmyEditPanel(onChange, false, true, true, false, false);
	}
	
	public ArmyEditPanel getEditPanelNoInputs() {
		return new ArmyEditPanel(null, true, true, false, false, false);
	}
	
	public ArmyEditPanel getEditPanelNoInputsNoHeader() {
		return new ArmyEditPanel(null, false, true, false, false, false);
	}
	
	/**
	 * Classe que representa uma tropa específica, com a unidade, nível e quantidade
	 * @author Arthur
	 *
	 */
	public class Tropa {
		
		private Unidade unidade;
		private int quantidade;
		private int nivel;
		
		private Tropa(Unidade unidade, int quantidade, int nivel) {
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel = nivel;
		}
		
		private Tropa(Unidade unidade, int quantidade) {
			this(unidade, quantidade, 1);
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
			return unidade.getTempoProdução() * quantidade;
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
				return unidade.getVelocidade();
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
		
		private HashMap<Unidade, JCheckBox> selected;
		private HashMap<Unidade, IntegerFormattedTextField> quantities;
		private HashMap<Unidade, TroopLevelComboBox> level;
		
		private GridBagLayout layout;
		
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
		private ArmyEditPanel(OnChange onChange, boolean hasHeader, 
				boolean hasNames, boolean hasSelected, boolean hasAmount,
				boolean hasNivel) {
			
			selected = new HashMap<Unidade, JCheckBox>();
			quantities = new HashMap<Unidade, IntegerFormattedTextField>();
			level = new HashMap<Unidade, TroopLevelComboBox>();
			
			this.hasHeader = hasHeader;
			this.hasNames = hasNames;
			this.hasSelected = hasSelected;
			this.hasAmount = hasAmount;
			this.hasNivel = hasNivel;
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
			
			if (hasSelected) {
				c.gridx++;
			}
			
			if (hasNivel) {
				c.gridx++;
				panel.add(new JLabel("Nível"), c);
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
				
				int niveis = ServerManager.getSelectedServer().getWorld().getResearchSystem().getResearch();
				
				TroopLevelComboBox combo = new TroopLevelComboBox(niveis, unitPanel.getBackground());
				combo.setSelectedItem(Army.this.getTropa(u).nivel);
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
			
			for (Unidade i : quantities.keySet())
				if (hasSelected)
					Army.this.addTropa(i, selected.get(i).isSelected() ? 1 : 0, 
							(int) level.get(i).getSelectedItem());
				else
					Army.this.addTropa(i, quantities.get(i).getValue().intValue(),
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
			
			for (Tropa t : army.tropas)
				if (getUnidades().contains(t.unidade)) {
					if (t.quantidade > 0)
						quantities.get(t.unidade).setText(String.valueOf(t.quantidade));	
					level.get(t.unidade).setSelectedItem(t.nivel);
			}
			
			saveValues();
			
		}
		
		public Army getArmy() {
			return Army.this;
		}
		
	}
	
}
