package recrutamento;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import config.Mundo_Reader;
import custom_components.TroopFormattedTextField;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class PanelUnidade extends JPanel {

	private PanelEdifício edifício;
	private Unidade unidade;
	private JLabel nome;
	private TroopFormattedTextField quantidade;
	private JLabel tempoUnitário;
	private JLabel tempoTotal;

	private Color cor;

	/**
	 * @param Cor
	 *            que será pintado
	 * @param Unidade
	 *            que representa
	 * @param Edifício
	 *            em que é construída
	 */
	public PanelUnidade(Color cor, Unidade unidade) {
		
		this.unidade = unidade;
		edifício = null;
		this.cor = cor;

		setBackground(cor);

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 125, 100, 0, 100, 125 };
		gbl.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 0.0 };
		gbl.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		setLayout(gbl);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		nome = new JLabel(unidade.nome());
		add(nome, c);

		quantidade = new TroopFormattedTextField(9) {

			@Override
			public void go() {

				if (quantidade.getText().equals(""))
					resetTimes();
				else
					changeTimes();

			}
		};

		c.insets = new Insets(5, 0, 5, 5);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		add(quantidade, c);

		tempoUnitário = new JLabel();
		c.insets = new Insets(5, 0, 5, 5);
		c.gridx = 3;
		add(getTempoUnitário(), c);

		tempoTotal = new JLabel();
		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 4;
		add(tempoTotal, c);

		setTempoUnitário(1);
		

	}

	/**
	 * Multiplica o tempo unitário pela quantidade, modificando o label
	 * tempoTotal.
	 * 
	 * Também altera o tempo total do edifício a que a unidade pertence, caso
	 * este não seja nulo
	 */
	public void changeTimes() {

		BigDecimal tempo = Cálculos.getSeconds(getTempoUnitário().getText());

		// Remove os pontos de milhar para cálculo

		BigDecimal quantia;
		if (!quantidade.getText().equals(""))
			quantia = quantidade.getValue();
		else
			quantia = BigDecimal.ZERO;

		tempo = tempo.multiply(quantia);
		tempoTotal.setText(Cálculos.format(tempo));

		if (edifício != null)
			edifício.setTempoTotal();
	}

	private void resetTimes() {

		tempoTotal.setText("");

		if (edifício != null)
			edifício.setTempoTotal();

	}

	/**
	 * Muda o tempo que aparece na label "tempoUnitário"
	 * 
	 * @param BigDecimal
	 *            tempo, em segundos
	 */
	public void setTempoUnitário(int nível) {

		if (edifício == null)
			if (unidade.equals(Unidade.PALADINO))
				nível = 0;
			else
				nível = 1;
		
		BigDecimal tempo = unidade.tempoDeProdução().multiply(Mundo_Reader.MundoSelecionado
								.getPorcentagemDeProdução(nível));
		
		tempoUnitário.setText(Cálculos.format(tempo));

	}

	/**
	 * Retorna o tempo total de produção da unidade
	 * 
	 * @return BigDecimal tempo, em segundos
	 */
	public BigDecimal getTempoTotal() {

		StringBuilder builder = new StringBuilder(tempoTotal.getText());

		while (builder.indexOf(".") != -1)
			builder.deleteCharAt(builder.indexOf("."));

		try {
			return Cálculos.getSeconds(builder.toString());
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	// Adiciona um JSeparator vertical para separar as características
	@SuppressWarnings("unused")
	private void addSeparator(GridBagConstraints c, JPanel panel) {

		JSeparator test = new JSeparator(SwingConstants.VERTICAL);
		if (cor == null)
			test.setForeground(Cores.SEPARAR_ESCURO);
		else
			test.setForeground(Cores.SEPARAR_CLARO);
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx++;
		panel.add(test, c);

	}

	public void setEdifício(PanelEdifício d) {
		edifício = d;
	}
	
	public TroopFormattedTextField getTextField() {
		return quantidade;
	}
	
	public JLabel getTempoUnitário() {
		return tempoUnitário;
	}

	public Unidade getUnidade() {
		return unidade;
	}

}
