package dados_de_unidade;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import config.Mundo_Reader;
import database.Cores;
import database.Unidade;

public class PanelUnidade{
	
	// JPanel com o nome e quantidade
	private JPanel identificadores;
	
	// JPanel com as características principais
	private JPanel dadosPrincipais;
	
	// JPanel com os dados de custo
	private JPanel dadosCusto;
	
	private JLabel nome;
	private JTextField quantidade;
	private JComboBox<Integer> nível;
	private JLabel dano, defGeral, defCavalo, defArqueiro, saque;
	private JLabel madeira, argila, ferro, população;

	private Unidade unidade;
	private Color cor;
	
	private PanelSoma soma;
	
	/**
	 * Cria um panel para a inserção de uma unidade, com textField para o quantidade e labels para as características
	 * 
	 * @param cor que o panel terá
	 * @param unidade a que o panel corresponde
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
	 * Cria um panel com apenas os nomes de cada campo, para ser usado como header
	 */
	public PanelUnidade() {
		
		// Creating "identificadores"
		
		identificadores = new JPanel();
		identificadores.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl_inserção = new GridBagLayout();
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis())
			gbl_inserção.columnWidths = new int[] {125, 1, 40, 1, 80};
		else
			gbl_inserção.columnWidths = new int[] {125, 80};
		gbl_inserção.rowHeights = new int[] {20};
		gbl_inserção.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_inserção.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		identificadores.setLayout(gbl_inserção);
		
		GridBagConstraints gbc_inserção = new GridBagConstraints();
		gbc_inserção.insets = new Insets(5, 5, 5, 5);
		
		JLabel nome = new JLabel("Unidade");
		gbc_inserção.gridx = 0;
		identificadores.add(nome, gbc_inserção);
		
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis()) {
			addSeparator(gbc_inserção, identificadores);
		
			JLabel nível = new JLabel("Nível");
			gbc_inserção.gridx++;
			identificadores.add(nível, gbc_inserção);
		
			addSeparator(gbc_inserção, identificadores);
		}
		
		JLabel quantidade = new JLabel("Quantidade");
		gbc_inserção.gridx++;
		identificadores.add(quantidade, gbc_inserção);
		
		
		// Creating "dadosPrincipais"
		
		dadosPrincipais = new JPanel();
		dadosPrincipais.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl = new GridBagLayout();
		
		// Caso o mundo tenha arqueiros, coloca lugar para a defesa de arqueiro
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			gbl.columnWidths = new int[] {75, 1, 75, 1, 75, 1, 75, 1, 75};
		else
			gbl.columnWidths = new int[] {75, 1, 75, 1, 75, 1, 75};
		
		
		gbl.rowHeights = new int[] {20};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		dadosPrincipais.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		
		dano = new JLabel("Ataque");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosPrincipais.add(dano, constraints);
		
		addSeparator(constraints, dadosPrincipais);
		
		defGeral = new JLabel("Def. Geral");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(defGeral, constraints);
		
		addSeparator(constraints, dadosPrincipais);
		
		defCavalo = new JLabel("Def. Cav.");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(defCavalo, constraints);
		
		addSeparator(constraints, dadosPrincipais);
		
		defArqueiro = new JLabel("Def. Arq.");
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro()) {
		
			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.gridx++;
			dadosPrincipais.add(defArqueiro, constraints);
		
			addSeparator(constraints, dadosPrincipais);
		
		}
		
		saque = new JLabel("Saque");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPrincipais.add(saque, constraints);
		
		// Creating "dadosCusto"
		
		dadosCusto = new JPanel();
		dadosCusto.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl_custo = new GridBagLayout();
		gbl_custo.columnWidths = new int[] {75, 1, 75, 1, 75, 1, 75};
		gbl_custo.rowHeights = new int[] {20};
		gbl_custo.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_custo.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		dadosCusto.setLayout(gbl_custo);
		
		GridBagConstraints constraints_custo = new GridBagConstraints();
		constraints_custo.gridy = 0;
		
		madeira = new JLabel("Madeira");
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx = 0;
		dadosCusto.add(madeira, constraints_custo);
		
		addSeparator(constraints_custo, dadosCusto);
		
		argila = new JLabel("Argila");
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx++;
		dadosCusto.add(argila, constraints_custo);
		
		addSeparator(constraints_custo, dadosCusto);
		
		ferro = new JLabel("Ferro");
		constraints_custo.insets = new Insets(5, 0, 5, 5);
		constraints_custo.gridx++;
		dadosCusto.add(ferro, constraints_custo);
		
		addSeparator(constraints_custo, dadosCusto);
		
		população = new JLabel("População");
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
			gbl.columnWidths = new int[] {125, 1, 40, 1, 80};
		else
			gbl.columnWidths = new int[] {125, 80};
		gbl.rowHeights = new int[] {20};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
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
			
			if (unidade.equals(Unidade.PALADINO) || unidade.equals(Unidade.NOBRE) 
					|| unidade.equals(Unidade.MILÍCIA))
				createComboBox(false, constraints, true);
			else
				createComboBox(true, constraints, true);
		
			addSeparator(constraints, identificadores);
		} else
			createComboBox(true, constraints, false);
		
		quantidade = new JTextField();
		quantidade.setHorizontalAlignment(SwingConstants.LEFT);
		quantidade.setDocument(new PlainDocument() {
			
			@Override
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    //Permite a entrada somente de números e no máximo 9 dígitos
				    if ((getLength() + str.length()) <= 9 && Character.isDigit(str.charAt(0))) {
				      super.insertString(offset, str, attr);
				    }
				    
				    if (getLength() > 3 && !str.contains(".")) 
				    quantidade.setText(getFormattedNumber(
				    		getUnformattedNumber(getText(0, getLength()))));
				    
			}
			
			@Override
			public void removeUpdate(AbstractDocument.DefaultDocumentEvent chng) {
				
				quantidade.setFocusable(false);	
				quantidade.setFocusable(true);
				quantidade.requestFocus();

				
			}
			
		});
		
		quantidade.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				go();
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				go();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				go();
				
			}
			
			private void go() {
				
				if (quantidade.getText().equals(""))
					resetValues();
				else
					changeValues();
				
				soma.setTotal();
				
			}
			
		});
		
		quantidade.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				
				if (!isFormatted(quantidade.getText()))
					quantidade.setText(getFormattedNumber(getUnformattedNumber(
							quantidade.getText())));
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
		
//		quantidade.addKeyListener(new KeyListener() {
//			
//			public void keyTyped(KeyEvent arg0) {}
//			
//			public void keyReleased(KeyEvent e) {
//				
//				// Apenas fazer modificações caso a key seja um número ou o backspace
//				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE )
//				try {
//					String formated = NumberFormat.getNumberInstance(Locale.GERMANY)
//							.parse(quantidade.getText()).toString();
//					
//					//Caso a quantidade seja vazia, não tenta mudar os valores, mas sim zerá-los
//					if (formated.equals(""))
//						resetValues();
//					else
//						changeValues();
//					
////					quantidade.setText(NumberFormat.getNumberInstance(Locale.GERMANY)
////							.format(Integer.parseInt(formated)));
//					
//					soma.setTotal();
//					
//				} catch (ParseException exc) {}
//				
//				soma.setTotal();
//				
//			}
//			
//			public void keyPressed(KeyEvent arg0) {}
//		});
		
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		identificadores.add(quantidade, constraints);
		quantidade.setColumns(6);
		
	}

	/**
	 * Adiciona o JPanel com as características básicas das unidades
	 */
	private void setPrincipais() {
		
		GridBagLayout gbl = new GridBagLayout();
		
		// Caso o mundo tenha arqueiros, coloca lugar para a defesa de arqueiro
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			gbl.columnWidths = new int[] {75, 1, 75, 1, 75, 1, 75, 1, 75};
		else
			gbl.columnWidths = new int[] {75, 1, 75, 1, 75, 1, 75};
		
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
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
		gbl.columnWidths = new int[] {75, 1, 75, 1, 75, 1, 75};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
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
	 * @param hasLevels se a unidade possui nível ou não (paladino, nobre e milícia)
	 * @param c constraint para ser inserido
	 * @param addtoPanel se o comboBox deve ser adicionado ao panel (false se o mundo não possui
	 * nível, sendo criado para ficar sempre no nível 1)
	 */
	private void createComboBox(boolean hasLevels, GridBagConstraints c, boolean addtoPanel) {
		
		// Coloca a cor padrão para os comboBox
		UIManager.put("ComboBox.selectionBackground", Cores.FUNDO_ESCURO); 
		UIManager.put("ComboBox.background", cor); 
		
		nível = new JComboBox<Integer>(new Integer[]{1,2,3});
		
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
		((JLabel)renderer).setHorizontalAlignment( SwingConstants.CENTER );
		((JLabel)renderer).setOpaque(true);
		
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
	
	// Modifica os valores das características para adequar ao número de unidades
	// (valor unitário * quantidade)
	private void changeValues() {
		
		String formated = "";
		try {
			formated = NumberFormat.getNumberInstance(Locale.GERMANY)
					.parse(quantidade.getText()).toString();
		} catch (ParseException e) {}
		
		BigDecimal quantia = new BigDecimal(formated);
		
		dano.setText(getFormattedNumber(quantia.multiply(unidade.ataque((
				nível.getSelectedIndex()+1))).setScale(0,RoundingMode.HALF_UP).toString()));
		defGeral.setText(getFormattedNumber(quantia.multiply(unidade.defGeral((
				nível.getSelectedIndex()+1))).setScale(0,RoundingMode.HALF_UP).toString()));
		defCavalo.setText(getFormattedNumber(quantia.multiply(unidade.defCav((
				nível.getSelectedIndex()+1))).setScale(0,RoundingMode.HALF_UP).toString()));
		defArqueiro.setText(getFormattedNumber(quantia.multiply(unidade.defArq((
				nível.getSelectedIndex()+1))).setScale(0,RoundingMode.HALF_UP).toString()));
		
		saque.setText(getFormattedNumber(quantia.multiply(unidade.saque()).toString()));
		
		madeira.setText(getFormattedNumber(quantia.multiply(unidade.madeira()).toString()));
		argila.setText(getFormattedNumber(quantia.multiply(unidade.argila()).toString()));
		ferro.setText(getFormattedNumber(quantia.multiply(unidade.ferro()).toString()));
		população.setText(getFormattedNumber(quantia.multiply(unidade.população()).toString()));
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
	
	// Returns the unformatted value of a formatted string
	private String getUnformattedNumber(String input) {
		
		String unformated = "";
		try {
			unformated = NumberFormat.getNumberInstance(Locale.GERMANY)
					.parse(input).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return unformated;
		
	}
	
	// Returns the formatted value of an unformatted string
	private String getFormattedNumber(String input) {
		
		String formated = NumberFormat.getNumberInstance(Locale.GERMANY)
				.format(Integer.parseInt(input));
		
		return formated;
		
	}
	
	private boolean isFormatted(String input) {
		
		List<String> pieces = Arrays.asList(input.split("\\."));
		
		for (String s : pieces) {
			
			if (pieces.indexOf(s) != 0 && s.length() != 3){
				return false;
			}
			
		}
		
		return true;
		
	}
	
	/**
	 * @return JPanel com nome e textField para inserção da quantidade
	 */
	protected JPanel getIdentificadores() { return identificadores; }
	
	protected JTextField getQuantidade() { return quantidade; }
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPrincipais() { return dadosPrincipais; }
	
	protected JLabel getDano() { return dano; }
	protected JLabel getDefGeral() { return defGeral; }
	protected JLabel getDefCavalo() { return defCavalo; }
	protected JLabel getDefArqueiro() { return defArqueiro; }
	protected JLabel getSaque() { return saque;}
	
	/**
	 * @return JPanel com as labels dos dados de custo
	 */
	protected JPanel getDadosCusto() { return dadosCusto; }
	
	protected JLabel getMadeira() { return madeira; }
	protected JLabel getArgila() { return argila; }
	protected JLabel getFerro() { return ferro; }
	protected JLabel getPopulação() { return população; }
	
	
}
