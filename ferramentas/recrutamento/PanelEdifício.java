package recrutamento;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Lang;
import custom_components.Edif�cioFormattedTextField;
import custom_components.TimeFormattedJLabel;
import database.Cores;
import database.Edif�cio;

@SuppressWarnings("serial")
public class PanelEdif�cio extends JPanel {

	GridBagConstraints gbc = new GridBagConstraints();
	private Edif�cio edif�cio;
	private ArrayList<PanelUnidade> unidades;

	private Edif�cioFormattedTextField txtN�vel;
	private TimeFormattedJLabel tempoTotal;

	/**
	 * @param possuir
	 *            cabe�alho com os labels "unidade", "quantidade", etc
	 * @param nome
	 *            do edif�cio
	 * @param n�vel
	 *            m�ximo que o edif�cio possui
	 */
	public PanelEdif�cio(Edif�cio edif�cio) {

		this.edif�cio = edif�cio;
		unidades = new ArrayList<PanelUnidade>();

		setBackground(Cores.FUNDO_ESCURO);

		setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 125, 100, 100, 125 };
		gbl.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gbl);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 0;

	}

	/**
	 * @param PanelUnidade
	 *            para ser adicionado
	 */
	public void addPanel(PanelUnidade panel) {

		unidades.add(panel);
		// panel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		panel.setEdif�cio(this);
		
		
		gbc.gridy++;
		add(panel, gbc);

	}

	/**
	 * Finaliza o Panel, colocando o seletor de n�vel e JLabel com tempo total
	 * de produ��o
	 */
	public void finish() {

		// Adiciona a border para separar as unidades do resto
		unidades.get(unidades.size() - 1).setBorder(
				new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));

		GridBagConstraints gbc_finish = new GridBagConstraints();
		gbc_finish.gridx = 0;
		gbc_finish.gridy = gbc.gridy;

		// Checa se edif�cio � masculino ou feminino (de forma n�o geral mas
		// aplic�vel nesse caso)
		JLabel lblN�vel;
		if (edif�cio.nome().endsWith("a"))
			lblN�vel = new JLabel(Lang.NivelDaEdificio.toString() + edif�cio.nome());
		else
			lblN�vel = new JLabel(Lang.NivelDoEdificio.toString() + edif�cio.nome());

		gbc_finish.fill = GridBagConstraints.NONE;
		gbc_finish.anchor = GridBagConstraints.CENTER;
		gbc_finish.insets = new Insets(3, 5, 0, 0);
		gbc_finish.gridwidth = 1;
		gbc_finish.gridy++;
		add(lblN�vel, gbc_finish);

		txtN�vel = new Edif�cioFormattedTextField(edif�cio, 1) {

			@Override
			public void go() {

				// Passa por todas as unidades do edif�cio, definindo o tempo
				// individual
				for (PanelUnidade panel : unidades) {
	
					panel.setTempoUnit�rio(txtN�vel.getValueInt());
					panel.changeTimes();

				}

			}

		};

		// Primeira passada para definir os tempos logo que o panel � formado
//		for (PanelUnidade panel : unidades) {
//			panel.setTempoUnit�rio(txtN�vel.getSelectedIndex());
//		}

		gbc_finish.anchor = GridBagConstraints.WEST;
		gbc_finish.fill = GridBagConstraints.VERTICAL;
		gbc_finish.insets = new Insets(7, 0, 5, 0);
		gbc_finish.gridx = 1;
		add(txtN�vel, gbc_finish);

		JLabel lblTempoTotal = new JLabel(Lang.TempoTotal.toString()+":");
		gbc_finish.anchor = GridBagConstraints.CENTER;
		gbc_finish.gridx = 2;
		add(lblTempoTotal, gbc_finish);

		tempoTotal = new TimeFormattedJLabel(false);
		gbc_finish.insets = new Insets(7, 0, 5, 5);
		gbc_finish.fill = GridBagConstraints.NONE;
		gbc_finish.gridx = 3;
		add(tempoTotal, gbc_finish);

	}

	/**
	 * Soma os tempos totais de cada unidade, colocando o resultado no Label
	 * tempoTotal
	 */
	public void setTempoTotal() {

		BigDecimal tempoTotal = BigDecimal.ZERO;

		for (PanelUnidade panel : unidades) {

			tempoTotal = tempoTotal.add(panel.getTempoTotal());

		}
		if (!tempoTotal.equals(BigDecimal.ZERO))
			this.tempoTotal.setTime(tempoTotal.longValue());
		else
			this.tempoTotal.setText("");

	}
	
	public Edif�cioFormattedTextField getComboBox(){
		return txtN�vel;
	}
}
