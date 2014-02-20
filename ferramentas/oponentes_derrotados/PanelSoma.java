package oponentes_derrotados;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelSoma {

	private JPanel panelOD;

	private JLabel lblOD;

	private List<PanelUnidade> panelUnidadeList;

	private BigInteger ODTotal;

	public PanelSoma() {

		setPrincipais();

	}

	/**
	 * Define a lista de painéis que serão somados e a cor dos painéis
	 * 
	 * @param list
	 *            dos painéis
	 * @param cor
	 */
	protected void setPanelListAndColor(List<PanelUnidade> list, Color cor) {

		panelUnidadeList = list;

		panelOD.setBackground(cor);

	}

	// Adiciona panel com as características principais
	private void setPrincipais() {

		panelOD = new JPanel();

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 100 };
		gbl.rowHeights = new int[] { 30 };
		gbl.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 0.0 };
		gbl.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		panelOD.setLayout(gbl);

		lblOD = new JLabel();
		GridBagConstraints gbc_lblOD = new GridBagConstraints();
		gbc_lblOD.insets = new Insets(5, 0, 5, 5);
		gbc_lblOD.gridx = 0;
		gbc_lblOD.gridy = 0;
		panelOD.add(lblOD, gbc_lblOD);

	}

	// Precisa throw a exceção porque java é estranho
	private void soma() throws ParseException {

		ODTotal = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getOD().equals(""))
				ODTotal = ODTotal.add(new BigInteger(i.getOD()));

	}

	// Define o texto das labels
	protected void setTotal() {

		try {
			soma();
		} catch (ParseException e) {
		}

		// Checa se os valores são diferentes de zero (como toda unidade tem
		// defesa geral, usei essa
		// característica como teste
		if (!ODTotal.equals(BigInteger.ZERO))
			lblOD.setText(String.format("%,d", ODTotal));
		else
			lblOD.setText("");

	}

	protected JPanel getPanelOD() {
		return panelOD;
	}

}
