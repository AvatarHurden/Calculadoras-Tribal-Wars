package io.github.avatarhurden.tribalwarsengine.ferramentas.recrutamento;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;

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
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class PanelUnidade extends JPanel {

	private PanelEdif�cio edif�cio;
	private Unidade unidade;
	private JLabel nome;
	private IntegerFormattedTextField quantidade;
	private TimeFormattedJLabel tempoUnit�rio;
	private TimeFormattedJLabel tempoTotal;

	private Color cor;

	/**
	 * @param Cor
	 *            que ser� pintado
	 * @param Unidade
	 *            que representa
	 * @param Edif�cio
	 *            em que � constru�da
	 */
	public PanelUnidade(Color cor, Unidade unidade) {
		
		this.unidade = unidade;
		edif�cio = null;
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

		quantidade = new IntegerFormattedTextField(9, Integer.MAX_VALUE) {

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

		tempoUnit�rio = new TimeFormattedJLabel(false);
		c.insets = new Insets(5, 0, 5, 5);
		c.gridx = 3;
		add(getTempoUnit�rio(), c);

		tempoTotal = new TimeFormattedJLabel(false);
		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 4;
		add(tempoTotal, c);

		setTempoUnit�rio(1);
		

	}

	/**
	 * Multiplica o tempo unit�rio pela quantidade, modificando o label
	 * tempoTotal.
	 * 
	 * Tamb�m altera o tempo total do edif�cio a que a unidade pertence, caso
	 * este n�o seja nulo
	 */
	public void changeTimes() {

		BigDecimal tempo = new BigDecimal(tempoUnit�rio.getTime());

		// Remove os pontos de milhar para c�lculo

		BigDecimal quantia;
		if (!quantidade.getText().equals(""))
			quantia = quantidade.getValue();
		else
			quantia = BigDecimal.ZERO;

		tempo = tempo.multiply(quantia);
		tempoTotal.setTime(tempo.longValue());

		if (edif�cio != null)
			edif�cio.setTempoTotal();
	}

	private void resetTimes() {

		tempoTotal.setText("");

		if (edif�cio != null)
			edif�cio.setTempoTotal();

	}

	/**
	 * Muda o tempo que aparece na label "tempoUnit�rio"
	 * 
	 * @param BigDecimal
	 *            tempo, em segundos
	 */
	public void setTempoUnit�rio(int n�vel) {

		if (edif�cio == null)
			if (unidade.equals(Unidade.PALADINO))
				n�vel = 0;
			else
				n�vel = 1;
		
		BigDecimal tempo = unidade.tempoDeProdu��o().multiply(Mundo_Reader.MundoSelecionado
								.getPorcentagemDeProdu��o(n�vel)).multiply(new BigDecimal("1000"));
		
		tempoUnit�rio.setTime(tempo.longValue());

	}

	/**
	 * Retorna o tempo total de produ��o da unidade
	 * 
	 * @return BigDecimal tempo, em segundos
	 */
	public BigDecimal getTempoTotal() {

		try {
			return new BigDecimal(tempoTotal.getTime());
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	// Adiciona um JSeparator vertical para separar as caracter�sticas
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

	public void setEdif�cio(PanelEdif�cio d) {
		edif�cio = d;
	}
	
	public IntegerFormattedTextField getTextField() {
		return quantidade;
	}
	
	public JLabel getTempoUnit�rio() {
		return tempoUnit�rio;
	}

	public Unidade getUnidade() {
		return unidade;
	}

}