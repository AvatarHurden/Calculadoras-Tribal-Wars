package dados_de_unidade;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;

import config.Lang;
import config.Mundo_Reader;
import custom_components.IntegerFormattedTextField;
import database.Cores;
import database.Unidade;

public class PanelUnidade {

	// JPanel com o nome e quantidade
	private JPanel identificadores;

	// JPanel com as características principais
	private JPanel dadosPrincipais;

	// JPanel com os dados de custo
	private JPanel dadosCusto;

	private JLabel nome;
	private IntegerFormattedTextField quantidade;
	private JComboBox<Integer> nível;
	private JLabel dano, defGeral, defCavalo, defArqueiro, saque;
	private JLabel madeira, argila, ferro, população;

	private Unidade unidade;
	private Color cor;

	private PanelSoma soma;

	/**
	 * Cria um panel para a inserção de uma unidade, com textField para o
	 * quantidade e labels para as características
	 * 
	 * @param cor
	 *            que o panel terá
	 * @param unidade
	 *            a que o panel corresponde
	 */
	public PanelUnidade(Color cor, Unidade unidade, PanelSoma soma) {

		this.unidade = unidade;
		this.cor = cor;
		this.soma = soma;

		identificadores = new JPanel();
		identificadores.setBackground(cor);

		setInserção();

		dadosPrincipais = new JPanel();
		dadosPrincipais.setBackground(cor);

		setPrincipais();

		dadosCusto = new JPanel();
		dadosCusto.setBackground(cor);

		setCusto();
	}

	/**
	 * Cria um panel com apenas os nomes de cada campo, para ser usado como
	 * header
	 */
	public PanelUnidade() {

		// Creating "identificadores"

		identificadores = new JPanel();
		identificadores.setBackground(Cores.FUNDO_ESCURO);

		GridBagLayout gbl_inserção = new GridBagLayout();
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis())
			gbl_inserção.columnWidths = new int[] { 125, 1, 40, 1, 100 };
		else
			gbl_inserção.columnWidths = new int[] { 125, 100 };
		gbl_inserção.rowHeights = new int[] { 20 };
		gbl_inserção.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_inserção.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		identificadores.setLayout(gbl_inserção);

		GridBagConstraints gbc_inserção = new GridBagConstraints();
		gbc_inserção.insets = new Insets(5, 5, 5, 5);

		JLabel nome = new JLabel(Lang.Unidade.toString());
		gbc_inserção.gridx = 0;
		identificadores.add(nome, gbc_inserção);

		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis()) {
			addSeparator(gbc_inserção, identificadores);

			JLabel nível = new JLabel(Lang.Nivel.toString());
			gbc_inserção.gridx++;
			identificadores.add(nível, gbc_inserção);

			addSeparator(gbc_inserção, identificadores);
		}

		JLabel quantidade = new JLabel(Lang.Quantidade.toString());
		gbc_inserção.gridx++;
		identificadores.add(quantidade, gbc_inserção);

		// Creating "dadosPrincipais"

		dadosPrincipais = new JPanel();
		dadosPrincipais.setBackground(Cores.FUNDO_ESCURO);

		GridBagLayout gbl = new GridBagLayout();

		// Caso o mundo tenha arqueiros, coloca lugar para a defesa de arqueiro
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			gbl.columnWidths = new int[] { 75, 1, 75, 1, 75, 1, 75, 1, 75 };
		else
			gbl.columnWidths = new int[] { 75, 1, 75, 1, 75, 1, 75 };

		gbl.rowHeights = new int[] { 20 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		dadosPrincipais.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;

		dano = new JLabel(Lang.Ataque.toString());
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosPrincipais.add(dano, constraints);

		addSeparator(constraints, dadosPrincipais);

		defGeral = new JLabel(Lang.DefGeral.toString());
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(defGeral, constraints);

		addSeparator(constraints, dadosPrincipais);

		defCavalo = new JLabel(Lang.DefCavalo.toString());
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(defCavalo, constraints);

		addSeparator(constraints, dadosPrincipais);

		defArqueiro = new JLabel(Lang.DefArqueiro.toString());

		if (Mundo_Reader.MundoSelecionado.hasArqueiro()) {

			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.gridx++;
			dadosPrincipais.add(defArqueiro, constraints);

			addSeparator(constraints, dadosPrincipais);

		}

		saque = new JLabel(Lang.Saque.toString());
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(saque, constraints);

		// Creating "dadosCusto"

		dadosCusto = new JPanel();
		dadosCusto.setBackground(Cores.FUNDO_ESCURO);

		GridBagLayout gbl_custo = new GridBagLayout();
		gbl_custo.columnWidths = new int[] { 75, 1, 75, 1, 75, 1, 75 };
		gbl_custo.rowHeights = new int[] { 20 };
		gbl_custo.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_custo.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		dadosCusto.setLayout(gbl_custo);

		GridBagConstraints constraints_custo = new GridBagConstraints();
		constraints_custo.gridy = 0;

		madeira = new JLabel(Lang.Madeira.toString());
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx = 0;
		dadosCusto.add(madeira, constraints_custo);

		addSeparator(constraints_custo, dadosCusto);

		argila = new JLabel(Lang.Argila.toString());
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx++;
		dadosCusto.add(argila, constraints_custo);

		addSeparator(constraints_custo, dadosCusto);

		ferro = new JLabel(Lang.Ferro.toString());
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx++;
		dadosCusto.add(ferro, constraints_custo);

		addSeparator(constraints_custo, dadosCusto);

		população = new JLabel(Lang.Populacao.toString());
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx++;
		dadosCusto.add(população, constraints_custo);

	}

	@SuppressWarnings("serial")
	/**
	 * Adiciona o JPanel com o nome, nível e quantidade das unidades
	 */
	private void setInserção() {

		GridBagLayout gbl = new GridBagLayout();
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis())
			gbl.columnWidths = new int[] { 125, 1, 40, 1, 100 };
		else
			gbl.columnWidths = new int[] { 125, 100 };
		gbl.rowHeights = new int[] { 20 };
		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		identificadores.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;

		nome = new JLabel(unidade.nome());
		identificadores.add(nome, constraints);

		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis()) {
			addSeparator(constraints, identificadores);

			if (unidade.equals(Unidade.PALADINO)
					|| unidade.equals(Unidade.NOBRE)
					|| unidade.equals(Unidade.MILÍCIA))
				createComboBox(false, constraints, true);
			else
				createComboBox(true, constraints, true);

			addSeparator(constraints, identificadores);
		} else
			createComboBox(true, constraints, false);

		quantidade = new IntegerFormattedTextField(9) {

			@Override
			public void go() {

				if (quantidade.getText().equals(""))
					resetValues();
				else
					changeValues();

				soma.setTotal();

			}
		};

		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx++;
		identificadores.add(quantidade, constraints);

	}

	/**
	 * Adiciona o JPanel com as características básicas das unidades
	 */
	private void setPrincipais() {

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

	/**
	 * Adiciona o JPanel com as caracterísitcas de custo das unidades
	 */
	private void setCusto() {

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
		if (cor == null)
			test.setForeground(Cores.SEPARAR_ESCURO);
		else
			test.setForeground(Cores.SEPARAR_CLARO);
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx++;
		panel.add(test, c);

	}

	/**
	 * Cria o comboBox para seleção de nível.
	 * 
	 * @param hasLevels
	 *            se a unidade possui nível ou não (paladino, nobre e milícia)
	 * @param c
	 *            constraint para ser inserido
	 * @param addtoPanel
	 *            se o comboBox deve ser adicionado ao panel (false se o mundo
	 *            não possui nível, sendo criado para ficar sempre no nível 1)
	 */
	private void createComboBox(boolean hasLevels, GridBagConstraints c,
			boolean addtoPanel) {

		// Coloca a cor padrão para os comboBox
		UIManager.put("ComboBox.selectionBackground", Cores.FUNDO_ESCURO);
		UIManager.put("ComboBox.background", cor);

		nível = new JComboBox<Integer>(new Integer[] { 1, 2, 3 });

		nível.setOpaque(false);
		nível.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!quantidade.getText().equals("")) {
					changeValues();

					soma.setTotal();
				}

			}
		});

		// Cria um renderer para set usado no combox, centralizando o texto
		ListCellRenderer<Object> renderer = new DefaultListCellRenderer();
		((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel) renderer).setOpaque(true);

		nível.setRenderer(renderer);

		// Zera a largura do botão
		nível.setUI(new BasicComboBoxUI() {
			@SuppressWarnings("serial")
			@Override
			protected JButton createArrowButton() {
				return new JButton() {
					@Override
					public int getWidth() {
						return 0;
					}
				};
			}
		});

		if (addtoPanel) {
			c.anchor = GridBagConstraints.CENTER;
			c.gridx++;
			if (hasLevels)
				identificadores.add(nível, c);
			else
				identificadores.add(new JLabel(), c);

		}

	}

	// Modifica os valores das características para adequar ao número de
	// unidades
	// (valor unitário * quantidade)
	private void changeValues() {

		BigDecimal quantia = quantidade.getValue();

		int lvl = nível.getSelectedIndex() + 1;

		dano.setText(getFormattedNumber(quantia.multiply(unidade.ataque(lvl))
				.toString()));
		defGeral.setText(getFormattedNumber(quantia.multiply(
				unidade.defGeral(lvl)).toString()));
		defCavalo.setText(getFormattedNumber(quantia.multiply(
				unidade.defCav(lvl)).toString()));
		defArqueiro.setText(getFormattedNumber(quantia.multiply(
				unidade.defArq(lvl)).toString()));

		saque.setText(getFormattedNumber(quantia.multiply(unidade.saque())
				.toString()));

		madeira.setText(getFormattedNumber(quantia.multiply(unidade.madeira())
				.toString()));
		argila.setText(getFormattedNumber(quantia.multiply(unidade.argila())
				.toString()));
		ferro.setText(getFormattedNumber(quantia.multiply(unidade.ferro())
				.toString()));
		população.setText(getFormattedNumber(quantia.multiply(
				unidade.população()).toString()));
	}

	// Zera os valores quando a "quantidade" é nula
	private void resetValues() {

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

	// Returns the formatted value of an unformatted string
	private String getFormattedNumber(String input) {

		String formated = quantidade.numberFormat.format(Integer
				.parseInt(input));

		return formated;

	}

	/**
	 * @return JPanel com nome e textField para inserção da quantidade
	 */
	protected JPanel getIdentificadores() {
		return identificadores;
	}

	protected IntegerFormattedTextField getQuantidade() {
		return quantidade;
	}
	
	protected JComboBox<Integer> getNível() {
		return nível;
	}

	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPrincipais() {
		return dadosPrincipais;
	}

	protected JLabel getDano() {
		return dano;
	}

	protected JLabel getDefGeral() {
		return defGeral;
	}

	protected JLabel getDefCavalo() {
		return defCavalo;
	}

	protected JLabel getDefArqueiro() {
		return defArqueiro;
	}

	protected JLabel getSaque() {
		return saque;
	}

	/**
	 * @return JPanel com as labels dos dados de custo
	 */
	protected JPanel getDadosCusto() {
		return dadosCusto;
	}

	protected JLabel getMadeira() {
		return madeira;
	}

	protected JLabel getArgila() {
		return argila;
	}

	protected JLabel getFerro() {
		return ferro;
	}

	protected JLabel getPopulação() {
		return população;
	}

}
