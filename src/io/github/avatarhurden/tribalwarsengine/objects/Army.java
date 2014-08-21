package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TroopLevelComboBox;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
	
	public Army() {
		
		for (Unidade i : Unidade.values())
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
		
		Iterator<Tropa> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().unidade.equals(unidade))
				iter.remove();
		
		tropas.add(new Tropa(unidade, quantidade, nivel3, nivel10));
		
	}

	public int getQuantidade(Unidade i) {
		return getTropa(i).quantidade;
	}
	
	private Tropa getTropa(Unidade u) {
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
	
	public int getAtaque(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units == null)
			for (Tropa t : tropas)
				ataque += t.getAtaque(item);
		else
			for (Unidade u : units)
				ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaque(Unidade... units) {
		return getAtaque(ItemPaladino.NULL, units);
	}
	
	public int getAtaqueGeral(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Geral))
					ataque += t.getAtaque(item);
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Geral))
					ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueGeral(Unidade... units) {
		return getAtaqueGeral(ItemPaladino.NULL, units);
	}
	
	public int getAtaqueCavalaria(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Cavalo))
					ataque += t.getAtaque(item);
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Cavalo))
					ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueCavalaria(Unidade... units) {
		return getAtaqueCavalaria(ItemPaladino.NULL, units);
	}
	
	public int getAtaqueArqueiro(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Arqueiro))
					ataque += t.getAtaque(item);
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Arqueiro))
					ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueArqueiro(Unidade... units) {
		return getAtaqueArqueiro(ItemPaladino.NULL, units);
	}
	
	public int getDefesaGeral(ItemPaladino item, Unidade... units) {
		int defesa = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				defesa += t.getDefesaGeral(item);
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaGeral(item);
		
		return defesa;
	}
	
	public int getDefesaGeral(Unidade... units) {
		return getDefesaGeral(ItemPaladino.NULL, units);
	}
	
	public int getDefesaCavalaria(ItemPaladino item, Unidade... units) {
		int defesa = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				defesa += t.getDefesaCavalaria(item);
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaCavalaria(item);
		
		return defesa;
	}
	
	public int getDefesaCavalaria(Unidade... units) {
		return getDefesaCavalaria(ItemPaladino.NULL, units);
	}
	
	public int getDefesaArqueiro(ItemPaladino item, Unidade... units) {
		int defesa = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				defesa += t.getDefesaArqueiro(item);
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaArqueiro(item);
		
		return defesa;
	}
	
	public int getDefesaArqueiro(Unidade... units) {
		return getDefesaArqueiro(ItemPaladino.NULL, units);
	}
	
	public int getSaque(Unidade... units) {
		int saque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				saque += t.getSaque();
		} else
			for (Unidade u : units)
				saque += getTropa(u).getSaque();
		
		return saque;
	}
	
	public int getPopulação(Unidade... units) {
		int população = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				população += t.getPopulação();
		} else
			for (Unidade u : units)
				população += getTropa(u).getPopulação();
		
		return população;
	}
	
	public int getCustoMadeira(Unidade... units) {
		int madeira = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				madeira += t.getCustoMadeira();
		} else
			for (Unidade u : units)
				madeira += getTropa(u).getCustoMadeira();
		
		return madeira;
	}
	
	public int getCustoArgila(Unidade... units) {
		int argila = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				argila += t.getCustoArgila();
		} else
			for (Unidade u : units)
				argila += getTropa(u).getCustoArgila();
		
		return argila;
	}
	
	public int getCustoFerro(Unidade... units) {
		int ferro = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				ferro += t.getCustoFerro();
		} else
			for (Unidade u : units)
				ferro += getTropa(u).getCustoFerro();
		
		return ferro;
	}
	
	/**
	 * Classe que representa uma tropa específica, com a unidade, nível e quantidade
	 * @author Arthur
	 *
	 */
	private class Tropa {
		
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
		
		private int getAtaque(ItemPaladino item) {
			return unidade.ataque(nivel, item).intValue() * quantidade;
		}
		
		private int getDefesaGeral(ItemPaladino item) {
			return unidade.defGeral(nivel, item).intValue() * quantidade;
		}
		
		private int getDefesaCavalaria(ItemPaladino item) {
			return unidade.defCav(nivel, item).intValue() * quantidade;
		}
		
		private int getDefesaArqueiro(ItemPaladino item) {
			return unidade.defArq(nivel, item).intValue() * quantidade;
		}
		
		private int getCustoMadeira() {
			return unidade.madeira().intValue() * quantidade;
		}
		
		private int getCustoArgila() {
			return unidade.argila().intValue() * quantidade;
		}
		
		private int getCustoFerro() {
			return unidade.ferro().intValue() * quantidade;
		}
		
		private int getPopulação() {
			return unidade.população().intValue() * quantidade;
		}
		
		private int getSaque() {
			return unidade.saque().intValue() * quantidade;
		}
		
		private int getTempoProdução() {
			return unidade.tempoDeProdução().intValue() * quantidade * 1000;
		}
		
		private UnidadeTipo getTipo() {
			return unidade.type();
		}

	}
	
	public static boolean isArmyJson(JSONObject json) {
		return json.has("tropas");
		
	}
	
	@SuppressWarnings("serial")
	public class ArmyEditPanel extends JPanel {
		
		private HashMap<Unidade, IntegerFormattedTextField> quantities;
		private HashMap<Unidade, TroopLevelComboBox> level3;
		private HashMap<Unidade, TroopLevelComboBox> level10;
		
		public ArmyEditPanel(OnChange onChange, boolean nivel3, 
				boolean nivel10, Unidade... unidades) {
			
			quantities = new HashMap<Unidade, IntegerFormattedTextField>();
			level3 = new HashMap<Unidade, TroopLevelComboBox>();
			level10 = new HashMap<Unidade, TroopLevelComboBox>();
			
			setOpaque(false);
			
	        GridBagLayout layout = new GridBagLayout();
	        layout.columnWidths = new int[]{,};
	        layout.rowHeights = new int[]{20};
	        layout.columnWeights = new double[]{1, Double.MIN_VALUE};
	        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
	        setLayout(layout);
	        
	        GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0, 0, 0, 0);
	        c.gridx = 0;
			c.gridy = -1;
	        c.gridwidth = 1;
	        c.anchor = GridBagConstraints.EAST;
	        c.fill = GridBagConstraints.BOTH;
			
	        Unidade[] values = unidades;
			if (values.length == 0)
				values = Unidade.values();
			
			for (Unidade i : values) {
				
				JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				panel.setBackground(Cores.getAlternar(c.gridy));
				
				JLabel label = new JLabel(i.toString());
				label.setHorizontalAlignment(SwingConstants.LEADING);
				panel.add(label);

				IntegerFormattedTextField txt = new IntegerFormattedTextField(getQuantidade(i)) {
					public void go() {
						onChange.run();
					}
				};
				
				quantities.put(i, txt);

				panel.add(txt);
				
				TroopLevelComboBox combo3 = new TroopLevelComboBox(3, panel.getBackground());
				combo3.setSelectedItem(Army.this.getTropa(i).nivel3);
				level3.put(i, combo3);
					
				combo3.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (nivel3)
					panel.add(combo3);
				
				
				TroopLevelComboBox combo10 = new TroopLevelComboBox(10, panel.getBackground());
				combo10.setSelectedItem(Army.this.getTropa(i).nivel10);
				level10.put(i, combo10);
					
				combo10.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (nivel10)
					panel.add(combo10);
				
				c.gridy++;
				add(panel, c);
			}
	        
		}
		
		public void setValues() {
			
			for (Unidade i : quantities.keySet())
				Army.this.addTropa(i, quantities.get(i).getValue().intValue(),
						(int) level3.get(i).getSelectedItem(),
						(int) level10.get(i).getSelectedItem() );
		}
		
	}
	
}
