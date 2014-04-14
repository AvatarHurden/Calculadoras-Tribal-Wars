package simulador;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Mundo_Reader;
import custom_components.Ferramenta;
import database.Bandeira;
import database.Cores;
import database.ItemPaladino;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {

	InputInfo input;
	OutputInfo output;

	C�lculo c�lculo;

	StatInsertion statAtacante;
	StatInsertion statDefensor;

	ResultTroopDisplay display;

	public GUI() {

		super("Simulador - Beta");

		setBackground(Cores.FUNDO_CLARO);

		
		input = new InputInfo();
		
		output = new OutputInfo();
		
		c�lculo = new C�lculo(input, output);
		
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
		add(tools.addResetPanel(action), c);
		
		c.anchor = GridBagConstraints.NORTH;
		
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeN�veis())
			addEmptySpace(c);

		c.gridy++;
		c.insets = new Insets(0, 5, 5, 5);
		add(addUnitNames(), c);

		c.gridx++;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridheight = 2;
		add(statAtacante, c);

		c.gridx++;
		add(statDefensor, c);

		c.gridy = 0;
		c.gridx++;
		c.gridheight = 1;
		c.insets = new Insets(0, 5, 5, 5);
		
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeN�veis())
			addEmptySpace(c);

		c.gridy++;
		add(display, c);

		JButton button = new JButton("Go");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				statAtacante.setInputInfo();

				statDefensor.setInputInfo();

				c�lculo.calculate();

				display.setValues();

			}
		});

		c.gridy++;
		add(button, c);

	}

	public JPanel addUnitNames() {

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;

		// Adding the headers

		JLabel lblNome = new JLabel("Unidade");
		lblNome.setPreferredSize(new Dimension(
				lblNome.getPreferredSize().width + 10, 26));
		lblNome.setBackground(Cores.FUNDO_ESCURO);
		lblNome.setOpaque(true);
		lblNome.setHorizontalAlignment(SwingConstants.CENTER);

		c.gridy++;
		panel.add(lblNome, c);

		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;

		int loop = 0;

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {

			if (i != null) {

				JPanel tropaPanel = new JPanel();
				tropaPanel.setLayout(new GridBagLayout());
				tropaPanel.setBackground(Cores.getAlternar(loop));

				// Separa��o entre a parte de nomenclatura e as unidades
				if (i.equals(Unidade.LANCEIRO))
					tropaPanel.setBorder(new
							MatteBorder(1,0,0,0,Cores.SEPARAR_ESCURO));

				GridBagConstraints tropaC = new GridBagConstraints();
				tropaC.insets = new Insets(5, 5, 5, 5);
				tropaC.gridx = 0;
				tropaC.gridy = 0;

				// Creating the TextField for the quantity of troops
				JLabel lbl = new JLabel(i.nome());

				tropaPanel.add(lbl, tropaC);

				loop++;
				c.gridy++;
				panel.add(tropaPanel, c);

			}
		}

		return panel;

	}

	private void addEmptySpace(GridBagConstraints c) {

		JLabel space = new JLabel("");
		
		// If mundo has levels, adds extra space so that the units align with the
		// input panels
		space.setPreferredSize(new Dimension(
				space.getPreferredSize().width, 62));
		
		c.gridy = 0;
		add(space, c);

	}

	protected class InputInfo {

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

		public InputInfo() {
		}

		/**
		 * @return Map com tropas atacantes
		 */
		protected Map<Unidade, BigDecimal> getTropasAtacantes() {
			return tropasAtacantes;
		}

		protected void setTropasAtacantes(
				Map<Unidade, BigDecimal> tropasAtacantes) {
			this.tropasAtacantes = tropasAtacantes;
		}

		/**
		 * @return Map com tropas defensoras
		 */
		protected Map<Unidade, BigDecimal> getTropasDefensoras() {
			return tropasDefensoras;
		}

		protected void setTropasDefensoras(
				Map<Unidade, BigDecimal> tropasDefensoras) {
			this.tropasDefensoras = tropasDefensoras;
		}

		/**
		 * @return Map com n�vel das tropas atacantes
		 */
		protected Map<Unidade, Integer> getN�velTropasAtaque() {
			return n�velTropasAtaque;
		}

		protected void setN�velTropasAtaque(
				Map<Unidade, Integer> n�velTropasAtaque) {
			this.n�velTropasAtaque = n�velTropasAtaque;
		}

		/**
		 * @return Map com n�vel das tropas defensoras
		 */
		protected Map<Unidade, Integer> getN�velTropasDefesa() {
			return n�velTropasDefesa;
		}

		protected void setN�velTropasDefesa(
				Map<Unidade, Integer> n�velTropasDefesa) {
			this.n�velTropasDefesa = n�velTropasDefesa;
		}

		/**
		 * @return n�vel da muralha
		 */
		protected int getMuralha() {
			return muralha;
		}

		/**
		 * @param n�vel
		 *            da muralha (se for maior do que o m�ximo ou menor do que o
		 *            m�nimo, ele arruma)
		 */
		protected void setMuralha(int muralha) {

			if (muralha > 20) {
				muralha = 20;
			}

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
		 * @return religi�o do atacante
		 */
		protected boolean getReligi�oAtacante() {
			return religi�oAtacante;
		}

		/**
		 * @param religi�o
		 *            do atacante
		 */
		protected void setReligi�oAtacante(boolean religi�oAtacante) {
			this.religi�oAtacante = religi�oAtacante;
		}

		/**
		 * @return religi�o do defensor
		 */
		protected boolean getReligi�oDefensor() {
			return religi�oDefensor;
		}

		/**
		 * @param religi�o
		 *            do defensor
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
		 * @return se possui b�nus noturno
		 */
		protected boolean getNoite() {
			return noite;
		}

		/**
		 * @param se
		 *            possui b�nus noturno
		 */
		protected void setNoite(boolean noite) {
			this.noite = noite;
		};

	}

	protected class OutputInfo {

		private Map<Unidade, BigDecimal> tropasAtacantesPerdidas = new HashMap<Unidade, BigDecimal>();
		private Map<Unidade, BigDecimal> tropasDefensorasPerdidas = new HashMap<Unidade, BigDecimal>();

		private int muralha;

		private int edif�cio;

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
		 * @return n�vel final da muralha
		 */
		protected int getMuralha() {
			return muralha;
		}

		protected void setMuralha(int muralha) {
			this.muralha = muralha;
		}

		/**
		 * @return n�vel final do edif�cio
		 */
		protected int getEdif�cio() {
			return edif�cio;
		}

		protected void setEdif�cio(int edif�cio) {
			this.edif�cio = edif�cio;
		}

	}

}
