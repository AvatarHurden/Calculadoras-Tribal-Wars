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
	public PanelUnidade(Color cor, Unidade unidade, PanelEdifício edifício) {

		this.cor = cor;

		setBackground(cor);

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 125, 100, 0, 100, 125 };
		gbl.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 0.0 };
		gbl.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		setLayout(gbl);

		nome = new JLabel(unidade.nome());
		GridBagConstraints gbc_nome = new GridBagConstraints();
		gbc_nome.anchor = GridBagConstraints.WEST;
		gbc_nome.insets = new Insets(5, 5, 5, 5);
		gbc_nome.gridx = 0;
		gbc_nome.gridy = 0;
		add(nome, gbc_nome);

		quantidade = new TroopFormattedTextField(9) {

			@Override
			public void go() {

				if (quantidade.getText().equals(""))
					resetTimes();
				else
					changeTimes();

			}
		};

		GridBagConstraints gbc_quantidade = new GridBagConstraints();
		gbc_quantidade.insets = new Insets(5, 0, 5, 5);
		gbc_quantidade.gridx = 1;
		gbc_quantidade.gridy = 0;
		add(quantidade, gbc_quantidade);

		tempoUnitário = new JLabel();
		GridBagConstraints gbc_tempoUnitário = new GridBagConstraints();
		gbc_tempoUnitário.insets = new Insets(5, 0, 5, 5);
		gbc_tempoUnitário.gridx = 3;
		gbc_tempoUnitário.gridy = 0;
		add(getTempoUnitário(), gbc_tempoUnitário);

		tempoTotal = new JLabel();
		GridBagConstraints gbc_tempoTotal = new GridBagConstraints();
		gbc_tempoTotal.insets = new Insets(0, 0, 5, 0);
		gbc_tempoTotal.gridx = 4;
		gbc_tempoTotal.gridy = 0;
		add(tempoTotal, gbc_tempoTotal);

		this.edifício = edifício;
		this.unidade = unidade;

		// Caso não tenha edifício (paladino ou nobre)
		if (edifício == null)
			if (unidade.equals(Unidade.PALADINO))
				setTempoUnitário(unidade.tempoDeProdução().multiply(
						Mundo_Reader.MundoSelecionado
								.getPorcentagemDeProdução(0)));
			else
				setTempoUnitário(unidade.tempoDeProdução().multiply(
						Mundo_Reader.MundoSelecionado
								.getPorcentagemDeProdução(1)));

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
	public void setTempoUnitário(BigDecimal tempo) {

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

	public JLabel getTempoUnitário() {
		return tempoUnitário;
	}

	public Unidade getUnidade() {
		return unidade;
	}

}
