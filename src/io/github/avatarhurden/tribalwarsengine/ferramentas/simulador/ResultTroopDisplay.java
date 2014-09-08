package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.OutputInfo;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Lang;

@SuppressWarnings("serial")
public class ResultTroopDisplay extends JPanel {

	private OutputInfo output;

	private Map<Unit, JLabel> tropasAtacantesPerdidas = new HashMap<Unit, JLabel>();

	private Map<Unit, JLabel> tropasDefensorasPerdidas = new HashMap<Unit, JLabel>();

	private JLabel muralha, edifício;

	public ResultTroopDisplay(OutputInfo output) {

		this.output = output;

		setOpaque(false);

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {};
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		// Panel that says that the shown units are lost units
		JPanel identificationPanel = new JPanel();
		identificationPanel.setBackground(Cores.FUNDO_ESCURO);
		identificationPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		identificationPanel.add(new JLabel(Lang.UnidadesPerdidas.toString()));
		
		identificationPanel.setPreferredSize(new Dimension(
				identificationPanel.getPreferredSize().width-2, identificationPanel.getPreferredSize().height-3));
		
		c.insets = new Insets(0, 5, 1, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		add(identificationPanel, c);
		
		c.gridwidth = 1;
		c.gridy++;
		c.insets = new Insets(2, 5, 5, 5);
		add(addUnitNames(), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(5, 5, 5, 0);
		add(addAttackUnitPanel(), c);

		c.gridx++;
		add(addDefenseUnitPanel(), c);

		// Panel to store the wall and building panels, so they center nicely
		JPanel bottomPanels = new JPanel();
		bottomPanels.setOpaque(false);

		bottomPanels.add(addWall());
		bottomPanels.add(addBuilding());

		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 3;
		add(bottomPanels, c);

	}

	public JPanel addUnitNames() {
		return new Army().getEditPanelNoInputs(26);
	}
	
	private JPanel addHeaderPanel(String name) {
		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		panel.add(new JLabel(name));
		
		panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 28));
		
		return panel;
	}

	public JPanel addAttackUnitPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);

		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;

		panel.add(addHeaderPanel("Atacante"), c);
		
		c.gridy++;
		panel.add(makeUnitPanel(Army.getAttackingUnits(), tropasAtacantesPerdidas), c);
		
		return panel;
	}

	public JPanel addDefenseUnitPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);

		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		panel.add(addHeaderPanel("Defensor"), c);
		
		c.gridy++;
		panel.add(makeUnitPanel(Army.getAvailableUnits(), tropasDefensorasPerdidas), c);
		
		return panel;
	}
	
	private JPanel makeUnitPanel(List<Unit> list, Map<Unit, JLabel> map) {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		
		int loop = 1;
		for (Unit i : list) {

			JPanel tropaPanel = new JPanel();
			tropaPanel.setPreferredSize(new Dimension(60, 26));
			tropaPanel.setBackground(Cores.getAlternar(loop));

			// Creating the TextField for the quantity of troops
			JLabel lbl = new JLabel(" ");
			// Adding the text to a map with the units
			map.put(i, lbl);

			tropaPanel.add(lbl);

			loop++;
			panel.add(tropaPanel);

		}
		
		return panel;
	}

	public JPanel addWall() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		// Define a cor do panel com base no número de tropas do mundo
		panel.setBackground(Cores.getAlternar(1));
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel(Lang.Muralha.toString()), c);

		muralha = new JLabel(" ");

		c.gridx++;
		panel.add(muralha, c);

		return panel;

	}

	public JPanel addBuilding() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		// Define a cor do panel com base no número de tropas do mundo
		panel.setBackground(Cores.getAlternar(1));
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel(Lang.Edificio.toString()), c);

		edifício = new JLabel(" ");

		c.gridx++;
		panel.add(edifício, c);

		return panel;

	}

	public void setValues() {

		NumberFormat numberFormat = NumberFormat
				.getNumberInstance(Locale.GERMANY);
		
		for (Entry<Unit, JLabel> i : tropasAtacantesPerdidas.entrySet())
			i.getValue().setText(numberFormat.format(
					output.getLostAttacker().getQuantidade(i.getKey())));

		for (Entry<Unit, JLabel> i : tropasDefensorasPerdidas.entrySet())
			i.getValue().setText(numberFormat.format(
					output.getLostDefender().getQuantidade(i.getKey())));

		muralha.setText(String.valueOf(output.getMuralha()));

		edifício.setText(String.valueOf(output.getEdifício()));

	}
	
	public void resetValues() {
		
		for (Entry<Unit, JLabel> i : tropasAtacantesPerdidas.entrySet())
			i.getValue().setText(" ");

		for (Entry<Unit, JLabel> i : tropasDefensorasPerdidas.entrySet())
			i.getValue().setText(" ");

		muralha.setText(" ");

		edifício.setText(" ");
		
	}
	
}
