package simulador;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import database.Bandeira;
import database.ItemPaladino;
import database.Unidade;

public class GUI extends JPanel{
	
	InputInfo input = new InputInfo();
	
	public GUI() {
		
		
	}
	
	public class InputInfo {
		
		private Map<Unidade, BigDecimal> tropasAtacantes = new HashMap<Unidade, BigDecimal>();
		private Map<Unidade, BigDecimal> tropasDefensoras = new HashMap<Unidade, BigDecimal>();
	
		private Map<Unidade, Integer> n�velTropasAtaque = new HashMap<Unidade, Integer>();
		private Map<Unidade, Integer> n�velTropasDefesa = new HashMap<Unidade, Integer>();
		
		private int muralha;
	
		private int edif�cio;
		
		private int moral;
		
		private ItemPaladino itemAtacante, itemDefensor;

		private Bandeira bandeiraAtacante, bandeiraDefensor;
		
		private boolean religi�oAtacante, religi�oDefensor;
		
		private int sorte;
		
		private boolean noite;
		
		public InputInfo() {}

		/**
		 * @return Map com tropas atacantes
		 */
		protected Map<Unidade, BigDecimal> getTropasAtacantes() {
			return tropasAtacantes;
		}

		protected void setTropasAtacantes(Map<Unidade, BigDecimal> tropasAtacantes) {
			this.tropasAtacantes = tropasAtacantes;
		}

		/**
		 * @return Map com tropas defensoras
		 */
		protected Map<Unidade, BigDecimal> getTropasDefensoras() {
			return tropasDefensoras;
		}

		protected void setTropasDefensoras(Map<Unidade, BigDecimal> tropasDefensoras) {
			this.tropasDefensoras = tropasDefensoras;
		}

		/**
		 * @return Map com n�vel das tropas atacantes
		 */
		protected Map<Unidade, Integer> getN�velTropasAtaque() {
			return n�velTropasAtaque;
		}

		protected void setN�velTropasAtaque(Map<Unidade, Integer> n�velTropasAtaque) {
			this.n�velTropasAtaque = n�velTropasAtaque;
		}

		/**
		 * @return Map com n�vel das tropas defensoras
		 */
		protected Map<Unidade, Integer> getN�velTropasDefesa() {
			return n�velTropasDefesa;
		}

		protected void setN�velTropasDefesa(Map<Unidade, Integer> n�velTropasDefesa) {
			this.n�velTropasDefesa = n�velTropasDefesa;
		}

		/**
		 * @return n�vel da muralha
		 */
		protected int getMuralha() {
			return muralha;
		}

		/**
		 * @param n�vel da muralha (se for maior do que o m�ximo ou menor do que o m�nimo, ele arruma)
		 */
		protected void setMuralha(int muralha) {
			this.muralha = muralha;
		}

		/**
		 * @return n�vel do edif�cio
		 */
		protected int getEdif�cio() {
			return edif�cio;
		}

		protected void setEdif�cio(int edif�cio) {
			this.edif�cio = edif�cio;
		}

		/**
		 * @return moral, em porcentagem (e.g 80)
		 */
		protected int getMoral() {
			return moral;
		}

		/**
		 * @param moral, em porcentagem (e.g 80)
		 */
		protected void setMoral(int moral) {
			this.moral = moral;
		}

		/**
		 * @return item do paladino atacante
		 */
		protected ItemPaladino getItemAtacante() {
			return itemAtacante;
		}

		/**
		 * @param item do paladino atacante
		 */
		protected void setItemAtacante(ItemPaladino itemAtacante) {
			this.itemAtacante = itemAtacante;
		}

		/**
		 * @return item do paladino defensor
		 */
		protected ItemPaladino getItemDefensor() {
			return itemDefensor;
		}

		/**
		 * @param item do paladino defensor
		 */
		protected void setItemDefensor(ItemPaladino itemDefensor) {
			this.itemDefensor = itemDefensor;
		}

		/**
		 * @return bandeira atacante
		 */
		protected Bandeira getBandeiraAtacante() {
			return bandeiraAtacante;
		}

		/**
		 * @param bandeira atacante
		 */
		protected void setBandeiraAtacante(Bandeira bandeiraAtacante) {
			this.bandeiraAtacante = bandeiraAtacante;
		}

		/**
		 * @return bandeira defensor
		 */
		protected Bandeira getBandeiraDefensor() {
			return bandeiraDefensor;
		}

		/**
		 * @param bandeira defensor
		 */
		protected void setBandeiraDefensor(Bandeira bandeiraDefensor) {
			this.bandeiraDefensor = bandeiraDefensor;
		}

		/**
		 * @return religi�o do atacante
		 */
		protected boolean isReligi�oAtacante() {
			return religi�oAtacante;
		}

		/**
		 * @param religi�o do atacante
		 */
		protected void setReligi�oAtacante(boolean religi�oAtacante) {
			this.religi�oAtacante = religi�oAtacante;
		}

		/**
		 * @return religi�o do defensor
		 */
		protected boolean isReligi�oDefensor() {
			return religi�oDefensor;
		}

		/**
		 * @param religi�o do defensor
		 */
		protected void setReligi�oDefensor(boolean religi�oDefensor) {
			this.religi�oDefensor = religi�oDefensor;
		}

		/**
		 * @return sorte do atacante, em porcetagem (e.g 20)
		 */
		protected int getSorte() {
			return sorte;
		}

		/**
		 * @param sorte do atacante, em porcetagem (e.g 20)
		 */
		protected void setSorte(int sorte) {
			this.sorte = sorte;
		}

		/**
		 * @return se possui b�nus noturno
		 */
		protected boolean isNoite() {
			return noite;
		}

		/**
		 * @param se possui b�nus noturno
		 */
		protected void setNoite(boolean noite) {
			this.noite = noite;
		};
		
		
	}
	
	protected class OutputInfo {
		
	}

}
