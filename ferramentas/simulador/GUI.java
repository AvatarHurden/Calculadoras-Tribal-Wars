package simulador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import database.Bandeira;
import database.ItemPaladino;
import database.Unidade;

public class GUI extends JPanel{
	
	InputInfo input = new InputInfo();
	
	OutputInfo output = new OutputInfo();
	
	public GUI() {
		
		Map<Unidade,BigDecimal> map = new HashMap<Unidade,BigDecimal>();
		
		for (Unidade i : Unidade.values())
			map.put(i, new BigDecimal(Math.random()*1000).setScale(0,RoundingMode.HALF_EVEN));
		
		output.setTropasAtacantesPerdidas(map);
		
		output.setTropasDefensorasPerdidas(map);
		
		output.setEdifício(24);
		
		output.setMuralha(15);
		
	}
	
	protected class InputInfo {
		
		private Map<Unidade, BigDecimal> tropasAtacantes = new HashMap<Unidade, BigDecimal>();
		private Map<Unidade, BigDecimal> tropasDefensoras = new HashMap<Unidade, BigDecimal>();
	
		private Map<Unidade, Integer> nívelTropasAtaque = new HashMap<Unidade, Integer>();
		private Map<Unidade, Integer> nívelTropasDefesa = new HashMap<Unidade, Integer>();
		
		private int muralha;
		
		private int edifício;
		
		private int moral;
		
		private ItemPaladino itemAtacante, itemDefensor;

		private Bandeira bandeiraAtacante, bandeiraDefensor;
		
		private boolean religiãoAtacante, religiãoDefensor;
		
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
		 * @return Map com nível das tropas atacantes
		 */
		protected Map<Unidade, Integer> getNívelTropasAtaque() {
			return nívelTropasAtaque;
		}

		protected void setNívelTropasAtaque(Map<Unidade, Integer> nívelTropasAtaque) {
			this.nívelTropasAtaque = nívelTropasAtaque;
		}

		/**
		 * @return Map com nível das tropas defensoras
		 */
		protected Map<Unidade, Integer> getNívelTropasDefesa() {
			return nívelTropasDefesa;
		}

		protected void setNívelTropasDefesa(Map<Unidade, Integer> nívelTropasDefesa) {
			this.nívelTropasDefesa = nívelTropasDefesa;
		}

		/**
		 * @return nível da muralha
		 */
		protected int getMuralha() {
			return muralha;
		}

		/**
		 * @param nível da muralha (se for maior do que o máximo ou menor do que o mínimo, ele arruma)
		 */
		protected void setMuralha(int muralha) {
			
			if (muralha > 20) {
				muralha = 20;
			}
			
			this.muralha = muralha;
		}

		/**
		 * @return nível do edifício
		 */
		protected int getEdifício() {
			return edifício;
		}

		protected void setEdifício(int edifício) {
			this.edifício = edifício;
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
			
			if (moral > 100) {
				moral = 100;
			}
			
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
		 * @return religião do atacante
		 */
		protected boolean getReligiãoAtacante() {
			return religiãoAtacante;
		}

		/**
		 * @param religião do atacante
		 */
		protected void setReligiãoAtacante(boolean religiãoAtacante) {
			this.religiãoAtacante = religiãoAtacante;
		}

		/**
		 * @return religião do defensor
		 */
		protected boolean getReligiãoDefensor() {
			return religiãoDefensor;
		}

		/**
		 * @param religião do defensor
		 */
		protected void setReligiãoDefensor(boolean religiãoDefensor) {
			this.religiãoDefensor = religiãoDefensor;
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
			
			if (Math.abs(sorte) > 25) {
				if (Integer.signum(sorte) == -1)
					sorte = -25;
				else
					sorte = 25;
			}
			
			this.sorte = sorte;
		}

		/**
		 * @return se possui bônus noturno
		 */
		protected boolean getNoite() {
			return noite;
		}

		/**
		 * @param se possui bônus noturno
		 */
		protected void setNoite(boolean noite) {
			this.noite = noite;
		};
		
		
	}
	
	protected class OutputInfo {
		
		private Map<Unidade, BigDecimal> tropasAtacantesPerdidas = new HashMap<Unidade, BigDecimal>();
		private Map<Unidade, BigDecimal> tropasDefensorasPerdidas = new HashMap<Unidade, BigDecimal>();
	
		private int muralha;
		
		private int edifício;

		/**
		 * @return Map com tropas atacantes perdidas
		 */
		protected Map<Unidade, BigDecimal> getTropasAtacantesPerdidas() {
			return tropasAtacantesPerdidas;
		}

		protected void setTropasAtacantesPerdidas(
				Map<Unidade, BigDecimal> tropasAtacantesPerdidas) {
			this.tropasAtacantesPerdidas = tropasAtacantesPerdidas;
		}

		/**
		 * @return Map com tropas defensoras perdidas
		 */
		protected Map<Unidade, BigDecimal> getTropasDefensorasPerdidas() {
			return tropasDefensorasPerdidas;
		}

		protected void setTropasDefensorasPerdidas(
				Map<Unidade, BigDecimal> tropasDefensorasPerdidas) {
			this.tropasDefensorasPerdidas = tropasDefensorasPerdidas;
		}

		/**
		 * @return nível final da muralha
		 */
		protected int getMuralha() {
			return muralha;
		}

		protected void setMuralha(int muralha) {
			this.muralha = muralha;
		}

		/**
		 * @return nível final do edifício
		 */
		protected int getEdifício() {
			return edifício;
		}

		protected void setEdifício(int edifício) {
			this.edifício = edifício;
		}
		
		
		
	}

}
