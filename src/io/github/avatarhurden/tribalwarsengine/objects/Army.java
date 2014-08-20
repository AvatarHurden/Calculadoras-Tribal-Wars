package io.github.avatarhurden.tribalwarsengine.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	public Army() {}
	
	/**
	 * Adiciona uma nova tropa ao exército. Se já existe uma tropa com a
	 * unidade especificada, ela será removida
	 * 
	 * @param unidade
	 * @param nivel
	 * @param quantidade
	 */
	public void addTropa(Unidade unidade, int nivel, int quantidade) {
		
		Iterator<Tropa> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().unidade.equals(unidade))
				iter.remove();
		
		tropas.add(new Tropa(unidade, quantidade, nivel));
		
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
		private int nivel;
		private int quantidade;
		
		private Tropa(Unidade unidade, int quantidade, int nivel) {
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel = nivel;
		}
		
		private Tropa(Unidade unidade, int quantidade) {
			this(unidade, quantidade, 1);
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
	
}
