package pontos;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import custom_components.EdifícioFormattedComboBox;

import database.Cores;
import database.Edifício;

public class PanelEdifício{
	
	// JPanel com o nome e quantidade
	private JPanel identificadores;
	
	// JPanel com as características (pontos e população)
	private JPanel dadosPanel;
	
	// JPanel com população restante (usado somente para fins de nomeação)
	private JPanel populaçãoRestantePanel;
	
	private JLabel nome;
	private EdifícioFormattedComboBox txtNível;
	private JLabel população, pontos, populaçãoRestante;

	private Edifício edifício;
	private Color cor;
	
	private PanelSoma soma;
	
	/**
	 * Cria um panel para a inserção de uma unidade, com textField para o quantidade e labels para as características
	 * 
	 * @param cor que o panel terá
	 * @param unidade a que o panel corresponde
	 */
	public PanelEdifício(Color cor, Edifício edifício, PanelSoma soma) {
		
		this.edifício = edifício;
		this.cor = cor;
		this.soma = soma;
		
		identificadores = new JPanel();
		identificadores.setBackground(cor);
		
		setInserção();
	
		dadosPanel = new JPanel();
		dadosPanel.setBackground(cor);
		
		setDadosPanel();

	}
	
	/**
	 * Cria um panel com apenas os nomes de cada campo, para ser usado como header
	 * 
	 * @param hasPopulaçãoRestante se o panel deve ter lugar para a label "populaçãoRestante"
	 */
	public PanelEdifício(boolean hasPopulaçãoRestante) {
		
		// Creating "identificadores"
		
		identificadores = new JPanel();
		identificadores.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl_inserção = new GridBagLayout();
		gbl_inserção.columnWidths = new int[] {125, 1, 47};
		gbl_inserção.rowHeights = new int[] {20};
		gbl_inserção.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_inserção.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		identificadores.setLayout(gbl_inserção);
		
		GridBagConstraints gbc_inserção = new GridBagConstraints();
		gbc_inserção.insets = new Insets(5, 5, 5, 5);
		
		nome = new JLabel("Edifício");
		gbc_inserção.gridx = 0;
		identificadores.add(nome, gbc_inserção);
		
		addSeparator(gbc_inserção, identificadores);
		
		JLabel quantidade = new JLabel("Nível");
		gbc_inserção.gridx++;
		identificadores.add(quantidade, gbc_inserção);
		
		
		// Creating "dadosPanel"
		
		dadosPanel = new JPanel();
		dadosPanel.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {75, 1, 75};
		gbl.rowHeights = new int[] {20};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		dadosPanel.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		
		pontos = new JLabel("Pontos");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosPanel.add(pontos, constraints);
		
		addSeparator(constraints, dadosPanel);
		
		população = new JLabel("População");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPanel.add(população, constraints);
		
		if (hasPopulaçãoRestante) {
			
			populaçãoRestantePanel = new JPanel();
			populaçãoRestantePanel.setBackground(Cores.FUNDO_ESCURO);
			
			GridBagLayout gbl_população = new GridBagLayout();
			gbl_população.columnWidths = new int[] {131};
			gbl_população.rowHeights = new int[] {20};
			gbl_população.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_população.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			populaçãoRestantePanel.setLayout(gbl_população);
			
			GridBagConstraints constraints_população = new GridBagConstraints();
			constraints_população.gridy = 0;
			
			populaçãoRestante = new JLabel("População Disponível");
			constraints_população.insets = new Insets(5, 5, 5, 5);
			populaçãoRestantePanel.add(populaçãoRestante, constraints_população);
			
		}
		
	}
	
	@SuppressWarnings("serial")
	private void setInserção() {
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 1, 47};
		gbl.rowHeights = new int[] {26};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0};
		identificadores.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		nome = new JLabel(edifício.nome());
		identificadores.add(nome, constraints);
		
		addSeparator(constraints, identificadores);
		
		txtNível = new EdifícioFormattedComboBox(edifício, 0, cor) {
			
			@Override
			public void go() {
				
				if (txtNível.getSelectedIndex() != 0)
					changeValues(txtNível.getSelectedIndex());
				else
					resetValues();
				
				soma.setTotal();
				
			}
		};
//		txtNível.setHorizontalAlignment(SwingConstants.CENTER);
//		txtNível.setColumns(3);
//		txtNível.setDocument(new PlainDocument() {
//			
//			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
//				    if (str == null)
//				      return;
//				    
//				    // permite no máximo 2 dígitos
//
//				    if ((getLength() + str.length()) <= 2 && Character.isDigit(str.charAt(0))) {
//				      super.insertString(offset, str, attr);
//				    }
//				  }
//			
//		});
//		
//		txtNível.addKeyListener(new KeyListener() {
//			
//			public void keyTyped(KeyEvent arg0) {}
//			
//			public void keyReleased(KeyEvent e) {
//				
//				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE ) {
//					
//					if (!txtNível.getText().equals("")) {
//						if (Integer.parseInt(txtNível.getText()) > edifício.nívelMáximo())
//							txtNível.setText(""+edifício.nívelMáximo());
//						changeValues(Integer.parseInt(txtNível.getText()));
//					} else
//						resetValues();
//					
//				}	
//					
//				soma.setTotal();
//				
//			}
//			
//			public void keyPressed(KeyEvent arg0) {}
//		});
		
//		changeValues(txtNível.getSelectedIndex());
		
//		soma.setTotal();
		
		constraints.insets = new Insets(4, 5, 4, 5);
		constraints.gridx++;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		identificadores.add(txtNível, constraints);
		
	}

	// Adiciona o panel com as principais características
	private void setDadosPanel() {
		
		GridBagLayout gbl = new GridBagLayout();
		
		gbl.columnWidths = new int[] {75, 1, 75};
		gbl.rowHeights = new int[] {28};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		dadosPanel.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		
		pontos = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosPanel.add(pontos, constraints);
		
		addSeparator(constraints, dadosPanel);
		
		população = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPanel.add(população, constraints);
		
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
	
	// Modifica os valores das características para adequar ao número de unidades
	private void changeValues(int nível) {
		
		pontos.setText(String.format("%,d",edifício.pontos(nível)));
		
		if (!edifício.equals(Edifício.FAZENDA))
			população.setText(String.format("%,d",edifício.população(nível)));
		else
			população.setText("0");
	}
	
	// Zera os valores quando a "quantidade" é nula
	private void resetValues() {
		
		pontos.setText("");
		população.setText("");
		
	}
	
	/**
	 * @return JPanel com nome e textField para inserção da quantidade
	 */
	protected JPanel getIdentificadores() { return identificadores; }

	protected Edifício getEdifício() { return edifício; }
	protected int getNível() {
		return txtNível.getSelectedIndex();
	}
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPanel() { return dadosPanel; }
	
	protected JLabel getPontos() { return pontos; }
	protected JLabel getPopulação() { return população; }
	
	protected JPanel getPopulaçãoRestantePanel() { return populaçãoRestantePanel; }
	
}
