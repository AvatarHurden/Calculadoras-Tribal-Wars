package io.github.avatarhurden.tribalwarsengine.ferramentas.dados_de_unidade;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class InformaçãoTropaPanel {

	// JPanel com as características principais
	private JPanel dadosPanel;

	// JPanel com os dados de custo
	private JPanel custoPanel;

	private JLabel danoLabel, defGeralLabel, defCavaloLabel, defArqueiroLabel, saqueLabel;
	private JLabel madeiraLabel, argilaLabel, ferroLabel, populaçãoLabel;
	
	private List<JLabel> labelsDados;
	private List<JLabel> labelsCusto;
	
	private Color cor;

	private Insets labelInsets, separatorInsets;

	public InformaçãoTropaPanel(Color cor) {
		this.cor = cor;
		
		setComponents();
	}
	
	private void setComponents() {
		setLabelLists();
		
		labelInsets = new Insets(5, 0, 5, 5);
		separatorInsets = new Insets(0, 0, 0, 0);
		
		dadosPanel = new JPanel();
		dadosPanel.setBackground(cor);

		setPrincipais();

		custoPanel = new JPanel();
		custoPanel.setBackground(cor);

		setCusto();
	}
	
	private void setLabelLists() {
		
		danoLabel = new JLabel();
		defGeralLabel = new JLabel();
		defCavaloLabel = new JLabel();
		defArqueiroLabel = new JLabel();
		saqueLabel = new JLabel();
		
		madeiraLabel = new JLabel();
		argilaLabel = new JLabel();
		ferroLabel = new JLabel();
		populaçãoLabel = new JLabel();
		
		labelsDados = new ArrayList<JLabel>();
		
		labelsDados.add(danoLabel);
		labelsDados.add(defGeralLabel);
		labelsDados.add(defCavaloLabel);
		labelsDados.add(defArqueiroLabel);
		labelsDados.add(saqueLabel);
		
		labelsCusto = new ArrayList<JLabel>();
		
		labelsCusto.add(madeiraLabel);
		labelsCusto.add(argilaLabel);
		labelsCusto.add(ferroLabel);
		labelsCusto.add(populaçãoLabel);
		
	}
	
	/**
	 * Adiciona o JPanel com as características básicas das unidades
	 */
	private void setPrincipais() {

		GridBagLayout gbl = new GridBagLayout();

		gbl.columnWidths = new int[] { 70, 1, 70, 1, 70, 1, 70, 1, 70 };
		gbl.rowHeights = new int[] { 30 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		dadosPanel.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.fill = GridBagConstraints.VERTICAL;
		
		for (JLabel label : labelsDados) {
			
			constraints.insets = labelInsets;
			dadosPanel.add(label, constraints);
			
			if (labelsDados.indexOf(label) < labelsDados.size() - 1) {
				constraints.gridx++;
				constraints.insets = separatorInsets;
				dadosPanel.add(makeSeparator(), constraints);
			}
			
			constraints.gridx++;
		}
	}

	/**
	 * Adiciona o JPanel com as caracterísitcas de custo das unidades
	 */
	private void setCusto() {

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 70, 1, 70, 1, 70, 1, 70 };
		gbl.rowHeights = new int[] { 30 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		custoPanel.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.fill = GridBagConstraints.VERTICAL;
		
		for (JLabel label : labelsCusto) {
			
			constraints.insets = labelInsets;
			custoPanel.add(label, constraints);
			
			if (labelsCusto.indexOf(label) < labelsCusto.size() - 1) {
				constraints.gridx++;
				constraints.insets = separatorInsets;
				custoPanel.add(makeSeparator(), constraints);
			}
			
			constraints.gridx++;
		}
	}

	// Adiciona um JSeparator vertical para separar as características
	private JSeparator makeSeparator() {

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(Cores.SEPARAR_CLARO);
		
		return separator;
	}


	// Modifica os valores das características para adequar ao número de
	// unidades
	// (valor unitário * quantidade)
	protected void changeValues(Object obj) {
		
		if (obj instanceof Troop)
			changeValuesTropa((Troop) obj);
		else 
			changeValuesArmy((Army) obj);
		
	}
	
	private void changeValuesArmy(Army army) {
		
		// Caso o exército seja vazio, zera os labels
		if (army.getPopulação() == 0) {
			resetValues();
			return;
		}
		
		danoLabel.setText(getFormattedNumber(army.getAtaque()));
		defGeralLabel.setText(getFormattedNumber((int) army.getDefesaGeral()));
		defCavaloLabel.setText(getFormattedNumber((int) army.getDefesaCavalaria()));
		defArqueiroLabel.setText(getFormattedNumber((int) army.getDefesaArqueiro()));
		
		saqueLabel.setText(getFormattedNumber(army.getSaque()));

		madeiraLabel.setText(getFormattedNumber(army.getCustoMadeira()));
		argilaLabel.setText(getFormattedNumber(army.getCustoArgila()));
		ferroLabel.setText(getFormattedNumber(army.getCustoFerro()));
		populaçãoLabel.setText(getFormattedNumber(army.getPopulação()));

	}
	
	private void changeValuesTropa(Troop tropa) {
		
		if (tropa.getQuantity() == 0) {
			resetValues();
			return;
		}
		
		danoLabel.setText(getFormattedNumber(tropa.getAttack()));
		defGeralLabel.setText(getFormattedNumber(tropa.getDefense()));
		defCavaloLabel.setText(getFormattedNumber(tropa.getDefenseCavalry()));
		defArqueiroLabel.setText(getFormattedNumber(tropa.getDefenseArcher()));

		saqueLabel.setText(getFormattedNumber(tropa.getHaul()));

		madeiraLabel.setText(getFormattedNumber(tropa.getCostWood()));
		argilaLabel.setText(getFormattedNumber(tropa.getCostClay()));
		ferroLabel.setText(getFormattedNumber(tropa.getCostIron()));
		populaçãoLabel.setText(getFormattedNumber(tropa.getPopulation()));

	}
	
	// Zera os valores quando a "quantidade" é nula
	protected void resetValues() {

		danoLabel.setText("");
		defGeralLabel.setText("");
		defCavaloLabel.setText("");
		defArqueiroLabel.setText("");
		saqueLabel.setText("");

		madeiraLabel.setText("");
		argilaLabel.setText("");
		ferroLabel.setText("");
		populaçãoLabel.setText("");

	}

	private String getFormattedNumber(int input) {
		return NumberFormat.getNumberInstance(Locale.GERMANY).format(input);
	}

	protected void setBorder(Border border) {
		dadosPanel.setBorder(border);
		custoPanel.setBorder(border);
	}
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPanel() {
		return dadosPanel;
	}

	/**
	 * @return JPanel com as labels dos dados de custo
	 */
	protected JPanel getCustoPanel() {
		return custoPanel;
	}
}
