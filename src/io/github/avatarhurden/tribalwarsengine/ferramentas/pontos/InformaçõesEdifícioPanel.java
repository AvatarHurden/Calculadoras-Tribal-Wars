package io.github.avatarhurden.tribalwarsengine.ferramentas.pontos;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock;
import io.github.avatarhurden.tribalwarsengine.objects.building.Construction;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class InformaçõesEdifícioPanel {

	// JPanel com as características (pontos e população)
	private JPanel dadosPanel;

	// JPanel com população restante (usado somente para fins de nomeação)
	private JPanel populaçãoRestantePanel;

	private JLabel população, pontos;

	private Insets labelInsets, separatorInsets;
	
	/**
	 * Cria um panel para a inserção de uma unidade, com textField para o
	 * quantidade e labels para as características
	 * 
	 * @param cor
	 *            que o panel terá
	 * @param unidade
	 *            a que o panel corresponde
	 */
	public InformaçõesEdifícioPanel(Color cor) {

		população = new JLabel();
		pontos = new JLabel();
		
		labelInsets = new Insets(5, 0, 5, 5);
		separatorInsets = new Insets(0, 0, 0, 0);
		
		dadosPanel = new JPanel();
		dadosPanel.setBackground(cor);

		setDadosPanel();

	}

	// Adiciona o panel com as principais características
	private void setDadosPanel() {

		GridBagLayout gbl = new GridBagLayout();

		gbl.columnWidths = new int[] { 75, 1, 75 };
		gbl.rowHeights = new int[] { 26 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		dadosPanel.setLayout(gbl);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridy = 0;
		c.gridx = 0;

		c.insets = labelInsets;
		dadosPanel.add(pontos, c);
		
		c.gridx++;
		c.insets = separatorInsets;
		dadosPanel.add(makeSeparator(), c);

		c.gridx++;
		c.insets = labelInsets;
		dadosPanel.add(população, c);

	}

	// Adiciona um JSeparator vertical para separar as características
	private JSeparator makeSeparator() {

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(Cores.SEPARAR_CLARO);

		return separator;
	}

	// Modifica os valores das características para adequar ao número de
	// unidades
	protected void changeValues(Object obj) {

		if (obj instanceof Construction)
			changeValuesBuilding((Construction) obj);
		else 
			changeValuesBuildings((BuildingBlock) obj);
		
	}
	
	private void changeValuesBuilding(Construction building) {
		if (building.getNível() == 0) {
			resetValues();
			return;
		}
		
		pontos.setText(getFormattedNumber(building.getPontos()));
		população.setText(getFormattedNumber(building.getPopulação()));	
	}
	
	private void changeValuesBuildings(BuildingBlock buildings) {
		if (buildings.getPopulaçãoUsada() == 0) {
			resetValues();
			return;
		}
		
		pontos.setText(getFormattedNumber(buildings.getPontos()));
		população.setText(getFormattedNumber(buildings.getPopulaçãoUsada()));	
	}

	// Zera os valores quando a "quantidade" é nula
	protected void resetValues() {

		pontos.setText("");
		população.setText("");

	}
	
	private String getFormattedNumber(int input) {
		String formated = NumberFormat.getNumberInstance(Locale.GERMANY).format(input);

		return formated;
	}

	protected void setBorder(Border border) {
		dadosPanel.setBorder(border);
	}
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPanel() {
		return dadosPanel;
	}

	protected JLabel getPontos() {
		return pontos;
	}

	protected JLabel getPopulação() {
		return população;
	}

	protected JPanel getPopulaçãoRestantePanel() {
		return populaçãoRestantePanel;
	}

}
