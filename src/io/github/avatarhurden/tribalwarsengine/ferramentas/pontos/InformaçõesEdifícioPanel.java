package io.github.avatarhurden.tribalwarsengine.ferramentas.pontos;

import io.github.avatarhurden.tribalwarsengine.objects.Buildings;
import io.github.avatarhurden.tribalwarsengine.objects.Buildings.Building;

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

import database.Cores;
import database.Edif�cio;

public class Informa��esEdif�cioPanel {

	// JPanel com as caracter�sticas (pontos e popula��o)
	private JPanel dadosPanel;

	// JPanel com popula��o restante (usado somente para fins de nomea��o)
	private JPanel popula��oRestantePanel;

	private JLabel popula��o, pontos;

	private Insets labelInsets, separatorInsets;
	
	/**
	 * Cria um panel para a inser��o de uma unidade, com textField para o
	 * quantidade e labels para as caracter�sticas
	 * 
	 * @param cor
	 *            que o panel ter�
	 * @param unidade
	 *            a que o panel corresponde
	 */
	public Informa��esEdif�cioPanel(Color cor) {

		popula��o = new JLabel();
		pontos = new JLabel();
		
		labelInsets = new Insets(5, 0, 5, 5);
		separatorInsets = new Insets(0, 0, 0, 0);
		
		dadosPanel = new JPanel();
		dadosPanel.setBackground(cor);

		setDadosPanel();

	}

	// Adiciona o panel com as principais caracter�sticas
	private void setDadosPanel() {

		GridBagLayout gbl = new GridBagLayout();

		gbl.columnWidths = new int[] { 75, 1, 75 };
		gbl.rowHeights = new int[] { 28 };
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
		dadosPanel.add(popula��o, c);

	}

	// Adiciona um JSeparator vertical para separar as caracter�sticas
	private JSeparator makeSeparator() {

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(Cores.SEPARAR_CLARO);

		return separator;
	}

	// Modifica os valores das caracter�sticas para adequar ao n�mero de
	// unidades
	protected void changeValues(Object obj) {

		if (obj instanceof Building)
			changeValuesBuilding((Building) obj);
		else 
			changeValuesBuildings((Buildings) obj);
		
	}
	
	private void changeValuesBuilding(Building building) {
		if (building.getN�vel() == 0) {
			resetValues();
			return;
		}
		
		pontos.setText(getFormattedNumber(building.getPontos()));
		
		if (!building.getEdif�cio().equals(Edif�cio.FAZENDA))
			popula��o.setText(getFormattedNumber(building.getPopula��o()));	
	}
	
	private void changeValuesBuildings(Buildings buildings) {
		if (buildings.getPopula��oUsada() == 0) {
			resetValues();
			return;
		}
		
		pontos.setText(getFormattedNumber(buildings.getPontos()));
		popula��o.setText(getFormattedNumber(buildings.getPopula��oUsada()));	
	}

	// Zera os valores quando a "quantidade" � nula
	protected void resetValues() {

		pontos.setText("");
		popula��o.setText("");

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

	protected JLabel getPopula��o() {
		return popula��o;
	}

	protected JPanel getPopula��oRestantePanel() {
		return popula��oRestantePanel;
	}

}
