package pontos;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import custom_components.Edif�cioFormattedComboBox;

import database.Cores;
import database.Edif�cio;

public class PanelEdif�cio{
	
	// JPanel com o nome e quantidade
	private JPanel identificadores;
	
	// JPanel com as caracter�sticas (pontos e popula��o)
	private JPanel dadosPanel;
	
	// JPanel com popula��o restante (usado somente para fins de nomea��o)
	private JPanel popula��oRestantePanel;
	
	private JLabel nome;
	private Edif�cioFormattedComboBox txtN�vel;
	private JLabel popula��o, pontos, popula��oRestante;

	private Edif�cio edif�cio;
	private Color cor;
	
	private PanelSoma soma;
	
	/**
	 * Cria um panel para a inser��o de uma unidade, com textField para o quantidade e labels para as caracter�sticas
	 * 
	 * @param cor que o panel ter�
	 * @param unidade a que o panel corresponde
	 */
	public PanelEdif�cio(Color cor, Edif�cio edif�cio, PanelSoma soma) {
		
		this.edif�cio = edif�cio;
		this.cor = cor;
		this.soma = soma;
		
		identificadores = new JPanel();
		identificadores.setBackground(cor);
		
		setInser��o();
	
		dadosPanel = new JPanel();
		dadosPanel.setBackground(cor);
		
		setDadosPanel();

	}
	
	/**
	 * Cria um panel com apenas os nomes de cada campo, para ser usado como header
	 * 
	 * @param hasPopula��oRestante se o panel deve ter lugar para a label "popula��oRestante"
	 */
	public PanelEdif�cio(boolean hasPopula��oRestante) {
		
		// Creating "identificadores"
		
		identificadores = new JPanel();
		identificadores.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl_inser��o = new GridBagLayout();
		gbl_inser��o.columnWidths = new int[] {125, 1, 47};
		gbl_inser��o.rowHeights = new int[] {20};
		gbl_inser��o.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_inser��o.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		identificadores.setLayout(gbl_inser��o);
		
		GridBagConstraints gbc_inser��o = new GridBagConstraints();
		gbc_inser��o.insets = new Insets(5, 5, 5, 5);
		
		nome = new JLabel("Edif�cio");
		gbc_inser��o.gridx = 0;
		identificadores.add(nome, gbc_inser��o);
		
		addSeparator(gbc_inser��o, identificadores);
		
		JLabel quantidade = new JLabel("N�vel");
		gbc_inser��o.gridx++;
		identificadores.add(quantidade, gbc_inser��o);
		
		
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
		
		popula��o = new JLabel("Popula��o");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPanel.add(popula��o, constraints);
		
		if (hasPopula��oRestante) {
			
			popula��oRestantePanel = new JPanel();
			popula��oRestantePanel.setBackground(Cores.FUNDO_ESCURO);
			
			GridBagLayout gbl_popula��o = new GridBagLayout();
			gbl_popula��o.columnWidths = new int[] {131};
			gbl_popula��o.rowHeights = new int[] {20};
			gbl_popula��o.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_popula��o.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			popula��oRestantePanel.setLayout(gbl_popula��o);
			
			GridBagConstraints constraints_popula��o = new GridBagConstraints();
			constraints_popula��o.gridy = 0;
			
			popula��oRestante = new JLabel("Popula��o Dispon�vel");
			constraints_popula��o.insets = new Insets(5, 5, 5, 5);
			popula��oRestantePanel.add(popula��oRestante, constraints_popula��o);
			
		}
		
	}
	
	@SuppressWarnings("serial")
	private void setInser��o() {
		
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
		
		nome = new JLabel(edif�cio.nome());
		identificadores.add(nome, constraints);
		
		addSeparator(constraints, identificadores);
		
		txtN�vel = new Edif�cioFormattedComboBox(edif�cio, 0, cor) {
			
			@Override
			public void go() {
				
				if (txtN�vel.getSelectedIndex() != 0)
					changeValues(txtN�vel.getSelectedIndex());
				else
					resetValues();
				
				soma.setTotal();
				
			}
		};
//		txtN�vel.setHorizontalAlignment(SwingConstants.CENTER);
//		txtN�vel.setColumns(3);
//		txtN�vel.setDocument(new PlainDocument() {
//			
//			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
//				    if (str == null)
//				      return;
//				    
//				    // permite no m�ximo 2 d�gitos
//
//				    if ((getLength() + str.length()) <= 2 && Character.isDigit(str.charAt(0))) {
//				      super.insertString(offset, str, attr);
//				    }
//				  }
//			
//		});
//		
//		txtN�vel.addKeyListener(new KeyListener() {
//			
//			public void keyTyped(KeyEvent arg0) {}
//			
//			public void keyReleased(KeyEvent e) {
//				
//				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE ) {
//					
//					if (!txtN�vel.getText().equals("")) {
//						if (Integer.parseInt(txtN�vel.getText()) > edif�cio.n�velM�ximo())
//							txtN�vel.setText(""+edif�cio.n�velM�ximo());
//						changeValues(Integer.parseInt(txtN�vel.getText()));
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
		
//		changeValues(txtN�vel.getSelectedIndex());
		
//		soma.setTotal();
		
		constraints.insets = new Insets(4, 5, 4, 5);
		constraints.gridx++;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		identificadores.add(txtN�vel, constraints);
		
	}

	// Adiciona o panel com as principais caracter�sticas
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
		
		popula��o = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPanel.add(popula��o, constraints);
		
	}

	// Adiciona um JSeparator vertical para separar as caracter�sticas
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
	
	// Modifica os valores das caracter�sticas para adequar ao n�mero de unidades
	private void changeValues(int n�vel) {
		
		pontos.setText(String.format("%,d",edif�cio.pontos(n�vel)));
		
		if (!edif�cio.equals(Edif�cio.FAZENDA))
			popula��o.setText(String.format("%,d",edif�cio.popula��o(n�vel)));
		else
			popula��o.setText("0");
	}
	
	// Zera os valores quando a "quantidade" � nula
	private void resetValues() {
		
		pontos.setText("");
		popula��o.setText("");
		
	}
	
	/**
	 * @return JPanel com nome e textField para inser��o da quantidade
	 */
	protected JPanel getIdentificadores() { return identificadores; }

	protected Edif�cio getEdif�cio() { return edif�cio; }
	protected int getN�vel() {
		return txtN�vel.getSelectedIndex();
	}
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPanel() { return dadosPanel; }
	
	protected JLabel getPontos() { return pontos; }
	protected JLabel getPopula��o() { return popula��o; }
	
	protected JPanel getPopula��oRestantePanel() { return popula��oRestantePanel; }
	
}
