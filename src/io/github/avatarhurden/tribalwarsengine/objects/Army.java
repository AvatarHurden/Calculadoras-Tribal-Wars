package io.github.avatarhurden.tribalwarsengine.objects;

import java.util.ArrayList;
import java.util.List;

import database.Bandeira;
import database.Bandeira.CategoriaBandeira;
import database.Edifício;
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
	private int muralha;
	private ItemPaladino item;
	private Bandeira bandeira;
	private double sorte;
	private int moral;
	private boolean religioso;
	private boolean bonusNoturno;
	
	
	public Army() {
		
		// Valores padrões para as coisas
		muralha = 0;
		item = ItemPaladino.NULL;
		bandeira = new Bandeira(CategoriaBandeira.NULL, 0);
		sorte = 0;
		moral = 100;
		religioso = true;
		bonusNoturno = false;
		
	}
	
	
	public void addTropa(Unidade unidade, int nivel, int quantidade) {
		
		tropas.add(new Tropa(unidade, quantidade, nivel));
		
	}

	public void setMuralha(int muralha) {
		this.muralha = muralha;
	}

	public void setItem(ItemPaladino item) {
		this.item = item;
	}

	public void setBandeira(Bandeira bandeira) {
		this.bandeira = bandeira;
	}
	
	/**
	 * @param sorte - Um valor entre -25 e 25
	 */
	public void setSorte(double sorte) {
		this.sorte = sorte;
	}

	public void setMoral(int moral) {
		this.moral = moral;
	}

	public void setReligioso(boolean religioso) {
		this.religioso = religioso;
	}
	
	public void setBonusNoturno(boolean bonusNoturno) {
		this.bonusNoturno = bonusNoturno;
	}

	public int getAtaque(Unidade... units) {
		int ataque = 0;
		
		if (units == null)
			for (Tropa t : tropas)
				ataque += t.getAtaque();
		else
			for (Unidade u : units)
				ataque += getTropa(u).getAtaque();
		
		ataque *= 1 + sorte/100;
		ataque *= moral/100;
		
		return ataque;
	}
	
	public int getAtaqueGeral(Unidade... units) {
		int ataque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Geral))
					ataque += t.getAtaque();
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Geral))
					ataque += getTropa(u).getAtaque();
		
		ataque *= 1 + sorte/100;
		ataque *= moral/100;
		
		return ataque;
	}
	
	public int getAtaqueCavalaria(Unidade... units) {
		int ataque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Cavalo))
					ataque += t.getAtaque();
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Cavalo))
					ataque += getTropa(u).getAtaque();
		
		ataque *= 1 + sorte/100;
		ataque *= moral/100;
		
		return ataque;
	}
	
	public int getAtaqueArqueiro(Unidade... units) {
		int ataque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Arqueiro))
					ataque += t.getAtaque();
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Arqueiro))
					ataque += getTropa(u).getAtaque();
		
		ataque *= 1 + sorte/100;
		ataque *= moral/100;
		
		return ataque;
	}
	
	public int getDefesaGeral(Unidade... units) {
		int defesa = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				defesa += t.getDefesaGeral();
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaGeral();
		
		if (religioso == false)
			defesa /= 2;
		if (bonusNoturno == true)
			defesa *= 2;
		if (bandeira.getTipo().equals(CategoriaBandeira.DEFESA))
			defesa *= bandeira.getValue();
		
		defesa *= Edifício.MURALHA.bônusPercentual(muralha).intValue();
		
		defesa += Edifício.MURALHA.bônusFlat(muralha).intValue();
		
		return defesa;
	}
	
	public int getDefesaCavalaria(Unidade... units) {
		int defesa = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				defesa += t.getDefesaCavalaria();
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaCavalaria();
		
		if (religioso == false)
			defesa /= 2;
		if (bonusNoturno == true)
			defesa *= 2;
		if (bandeira.getTipo().equals(CategoriaBandeira.DEFESA))
			defesa *= bandeira.getValue();
		
		defesa *= Edifício.MURALHA.bônusPercentual(muralha).intValue();
		
		defesa += Edifício.MURALHA.bônusFlat(muralha).intValue();
		
		return defesa;
	}
	
	public int getDefesaArqueiro(Unidade... units) {
		int defesa = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				defesa += t.getDefesaArqueiro();
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaArqueiro();
		
		if (religioso == false)
			defesa /= 2;
		if (bonusNoturno == true)
			defesa *= 2;
		if (bandeira.getTipo().equals(CategoriaBandeira.DEFESA))
			defesa *= bandeira.getValue();
		
		defesa *= Edifício.MURALHA.bônusPercentual(muralha).intValue();
		
		defesa += Edifício.MURALHA.bônusFlat(muralha).intValue();
		
		return defesa;
	}
	
	public int getSaque(Unidade... units) {
		int saque = 0;
		
		if (units == null) {
			for (Tropa t : tropas)
				saque += t.getSaque();
		} else
			for (Unidade u : units)
				saque += getTropa(u).getSaque();
		
		if (bandeira.getTipo().equals(CategoriaBandeira.SAQUE))
			saque *= bandeira.getValue();
		
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
	
	public int getQuantidade(Unidade i) {
		
		return getTropa(i).quantidade;
		
	}
	
	private Tropa getTropa(Unidade u) {
		for (Tropa t : tropas)
			if (t.unidade.equals(u))
				return t;
		
		return null;
	}
	
	private class Tropa {
		
		private Unidade unidade;
		private int nivel;
		private int quantidade;
		
		private Tropa(Unidade unidade, int quantidade) {
			
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel = 1;
			
		}
		
		private Tropa(Unidade unidade, int quantidade, int nivel) {
			
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel = nivel;
			
		}
		
		private int getAtaque() {
			return unidade.ataque(nivel, item).intValue() * quantidade;
		}
		
		private int getDefesaGeral() {
			return unidade.defGeral(nivel, item).intValue() * quantidade;
		}
		
		private int getDefesaCavalaria() {
			return unidade.defCav(nivel, item).intValue() * quantidade;
		}
		
		private int getDefesaArqueiro() {
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
		
		/**
		 * @return tempo de produção de todas as unidades, em milissegundos 
		 */
		private int getTempoProdução() {
			return unidade.tempoDeProdução().intValue() * quantidade * 1000;
		}
		
		private UnidadeTipo getTipo() {
			return unidade.type();
		}
		
	}

}
