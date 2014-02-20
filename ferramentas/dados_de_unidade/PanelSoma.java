package dados_de_unidade;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import config.Mundo_Reader;
import database.Cores;

public class PanelSoma {

	private JPanel dadosPrincipais, dadosCusto;

	private JLabel dano, defGeral, defCavalo, defArqueiro, saque;
	private JLabel madeira, argila, ferro, população;

	private List<PanelUnidade> panelUnidadeList;

	private Map<String, BigInteger> somaTotal = new HashMap<String, BigInteger>();

	/**
	 * Painel com a soma das informações das unidades
	 */
	public PanelSoma() {

		setPrincipais();
		setCusto();

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

		dadosPrincipais.setBackground(cor);
		dadosCusto.setBackground(cor);

	}

	// Adiciona panel com as características principais
	private void setPrincipais() {

		dadosPrincipais = new JPanel();

		GridBagLayout gbl = new GridBagLayout();

		// Caso o mundo tenha arqueiros, coloca lugar para a defesa de arqueiro
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			gbl.columnWidths = new int[] { 75, 1, 75, 1, 75, 1, 75, 1, 75 };
		else
			gbl.columnWidths = new int[] { 75, 1, 75, 1, 75, 1, 75 };

		gbl.rowHeights = new int[] { 30 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		dadosPrincipais.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;

		dano = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosPrincipais.add(dano, constraints);

		addSeparator(constraints, dadosPrincipais);

		defGeral = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(defGeral, constraints);

		addSeparator(constraints, dadosPrincipais);

		defCavalo = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(defCavalo, constraints);

		addSeparator(constraints, dadosPrincipais);

		defArqueiro = new JLabel();

		if (Mundo_Reader.MundoSelecionado.hasArqueiro()) {

			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.gridx++;
			dadosPrincipais.add(defArqueiro, constraints);

			addSeparator(constraints, dadosPrincipais);

		}

		saque = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(saque, constraints);

	}

	// Adiciona panel com as características de custo
	private void setCusto() {

		dadosCusto = new JPanel();

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 75, 1, 75, 1, 75, 1, 75 };
		gbl.rowHeights = new int[] { 30 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		dadosCusto.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;

		madeira = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosCusto.add(madeira, constraints);

		addSeparator(constraints, dadosCusto);

		argila = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosCusto.add(argila, constraints);

		addSeparator(constraints, dadosCusto);

		ferro = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosCusto.add(ferro, constraints);

		addSeparator(constraints, dadosCusto);

		população = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosCusto.add(população, constraints);
	}

	// Adiciona um JSeparator vertical para separar as características
	private void addSeparator(GridBagConstraints c, JPanel panel) {

		JSeparator test = new JSeparator(SwingConstants.VERTICAL);
		test.setForeground(Cores.SEPARAR_CLARO);
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx++;
		panel.add(test, c);

	}

	// Precisa throw a exceção porque java é estranho
	private void soma() throws ParseException {

		// Somando danos
		BigInteger dano = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				dano = dano.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getDano().getText()).toString()));

		somaTotal.put("dano", dano);

		// Somando defesa geral
		BigInteger defGeral = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				defGeral = defGeral.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getDefGeral().getText()).toString()));

		somaTotal.put("defGeral", defGeral);

		// Somando defesa de cavalo
		BigInteger defCavalo = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				defCavalo = defCavalo.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getDefCavalo().getText()).toString()));

		somaTotal.put("defCavalo", defCavalo);

		// Somando defesa de arqueiro
		BigInteger defArqueiro = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				defArqueiro = defArqueiro.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getDefArqueiro().getText()).toString()));

		somaTotal.put("defArqueiro", defArqueiro);

		// Somando saque
		BigInteger saque = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				saque = saque.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getSaque().getText()).toString()));

		somaTotal.put("saque", saque);

		// Somando custo de madeira
		BigInteger madeira = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				madeira = madeira.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getMadeira().getText()).toString()));

		somaTotal.put("madeira", madeira);

		// Somando custo de argila
		BigInteger argila = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				argila = argila.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getArgila().getText()).toString()));

		somaTotal.put("argila", argila);

		// Somando custo de ferro
		BigInteger ferro = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				ferro = ferro.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getFerro().getText()).toString()));

		somaTotal.put("ferro", ferro);

		// Somando custo de população
		BigInteger população = new BigInteger("0");
		for (PanelUnidade i : panelUnidadeList)
			if (!i.getQuantidade().getText().equals(""))
				população = população.add(new BigInteger(NumberFormat
						.getNumberInstance(Locale.GERMANY)
						.parse(i.getPopulação().getText()).toString()));

		somaTotal.put("população", população);
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
		if (!somaTotal.get("defGeral").equals(BigInteger.ZERO)) {

			dano.setText(String.format("%,d", somaTotal.get("dano")));
			defGeral.setText(String.format("%,d", somaTotal.get("defGeral")));
			defCavalo.setText(String.format("%,d", somaTotal.get("defCavalo")));
			defArqueiro.setText(String.format("%,d",
					somaTotal.get("defArqueiro")));
			saque.setText(String.format("%,d", somaTotal.get("saque")));

			madeira.setText(String.format("%,d", somaTotal.get("madeira")));
			argila.setText(String.format("%,d", somaTotal.get("argila")));
			ferro.setText(String.format("%,d", somaTotal.get("ferro")));
			população.setText(String.format("%,d", somaTotal.get("população")));

		} else {

			dano.setText("");
			defGeral.setText("");
			defCavalo.setText("");
			defArqueiro.setText("");
			saque.setText("");

			madeira.setText("");
			argila.setText("");
			ferro.setText("");
			população.setText("");

		}

	}

	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPrincipais() {
		return dadosPrincipais;
	}

	/**
	 * @return JPanel com as labels dos dados de custo
	 */
	protected JPanel getDadosCusto() {
		return dadosCusto;
	}

}
