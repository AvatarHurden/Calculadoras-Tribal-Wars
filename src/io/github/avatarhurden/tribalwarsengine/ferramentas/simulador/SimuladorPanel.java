package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.enums.ItemPaladino;
import io.github.avatarhurden.tribalwarsengine.managers.ServerManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import config.Lang;
import database.Bandeira;

@SuppressWarnings("serial")
public class SimuladorPanel extends Ferramenta {

	InputInfo input;
	OutputInfo output;

	Cálculo cálculo;

	StatInsertion statAtacante;
	StatInsertion statDefensor;

	ResultTroopDisplay display;

	public SimuladorPanel() {

		super(Lang.FerramentaSimulador.toString());
		
		input = new InputInfo();
		
		output = new OutputInfo();
		
		cálculo = new Cálculo(input, output);
		
		statAtacante = new StatInsertion(StatInsertion.Tipo.Atacante, input, tools);
		
		statDefensor = new StatInsertion(StatInsertion.Tipo.Defensor, input, tools);
		
		display = new ResultTroopDisplay(output);
		
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				statAtacante.resetValues();
				statDefensor.resetValues();
				
				statAtacante.setInputInfo();

				statDefensor.setInputInfo();

				display.resetValues();
			}
		};
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridheight = 2;
		add(tools.addResetPanel(action), c);
		
		c.anchor = GridBagConstraints.NORTH;
		c.gridheight = 1;
		c.gridy += 2;
		
		if (ServerManager.getSelectedServer().getWorld().getResearchSystemLevels() > 1) {
			addEmptySpace(c);
			
			c.gridy++;
		} else
			c.gridheight = 2;

		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridheight = 4;
		add(statAtacante, c);

		c.gridx++;
		add(statDefensor, c);

		c.gridy = 0;
		c.gridx++;	
		
		if (ServerManager.getSelectedServer().getWorld().getResearchSystemLevels() == 1) {
			
			c.insets = new Insets(0, 5, 4, 5);
			c.gridheight = 1;
			addEmptySpace(c);
			
			c.insets = new Insets(0, 5, 5, 5);
			c.gridheight = 3;
			c.gridy++;
			
		} else {
			c.insets = new Insets(0, 5, 5, 5);
			c.gridheight = 2;
			c.gridy += 2;
		}
		
		add(display, c);

		JButton button = new JButton(Lang.BtnCalcular.toString());
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				statAtacante.setInputInfo();

				statDefensor.setInputInfo();

				cálculo.calculate();

				display.setValues();

			}
		});
		
		c.anchor = GridBagConstraints.SOUTH;
		add(button, c);

	}

	private void addEmptySpace(GridBagConstraints c) {

		JLabel space = new JLabel("");
		
		// If mundo has levels, adds extra space so that the units align with the
		// input panels
		space.setPreferredSize(new Dimension(
				space.getPreferredSize().width, 24));
		
		add(space, c);

	}

	protected class InputInfo {

		private Army attacker;
		private Army defender;
		
		private int muralha;

		private int edifício;

		private int moral;

		private ItemPaladino itemAtacante, itemDefensor;

		private Bandeira bandeiraAtacante, bandeiraDefensor;

		private boolean religiãoAtacante, religiãoDefensor;

		private int sorte;

		private boolean noite;

		public InputInfo() {
		}

		/**
		 * @return Map com tropas atacantes
		 */
		protected Army getArmyAttacker() {
			return attacker;
		}

		protected void setArmyAttacker(Army attacker) {
			this.attacker = attacker;
		}

		/**
		 * @return Map com tropas defensoras
		 */
		protected Army getArmyDefender() {
			return defender;
		}

		protected void setArmyDefender(Army defender) {
			this.defender = defender;
		}

		/**
		 * @return nível da muralha
		 */
		protected int getMuralha() {
			return muralha;
		}

		/**
		 * @param nível
		 *            da muralha (se for maior do que o máximo ou menor do que o
		 *            mínimo, ele arruma)
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
		 * @param moral
		 *            , em porcentagem (e.g 80)
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
		 * @param item
		 *            do paladino atacante
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
		 * @param item
		 *            do paladino defensor
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
		 * @param bandeira
		 *            atacante
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
		 * @param bandeira
		 *            defensor
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
		 * @param religião
		 *            do atacante
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
		 * @param religião
		 *            do defensor
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
		 * @param sorte
		 *            do atacante, em porcetagem (e.g 20)
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
		 * @param se
		 *            possui bônus noturno
		 */
		protected void setNoite(boolean noite) {
			this.noite = noite;
		};

	}

	protected class OutputInfo {

		private Army lostAttacker;
		private Army lostDefender;
		
		private int muralha;

		private int edifício;
		

		public Army getLostAttacker() {
			return lostAttacker;
		}

		public void setLostAttacker(Army lostAttacker) {
			this.lostAttacker = lostAttacker;
		}

		public Army getLostDefender() {
			return lostDefender;
		}

		public void setLostDefender(Army lostDefender) {
			this.lostDefender = lostDefender;
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
