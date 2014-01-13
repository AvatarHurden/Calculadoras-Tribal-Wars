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
import database.Cores;
import database.TroopFormattedTextField;
import database.Unidade;

@SuppressWarnings("serial")
public class PanelUnidade extends JPanel {
	
	private PanelEdif�cio edif�cio;
	private Unidade unidade;
	private JLabel nome;
	private TroopFormattedTextField quantidade;
	private JLabel tempoUnit�rio;
	private JLabel tempoTotal;
	
	private Color cor;
	
	/**
	 * @param Cor que ser� pintado 
	 * @param Unidade que representa
	 * @param Edif�cio em que � constru�da
	 */
	public PanelUnidade(Color cor, Unidade unidade, PanelEdif�cio edif�cio) {
		
		this.cor = cor;
		
		setBackground(cor);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 100, 0, 100, 125};
		gbl.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0};
		gbl.rowWeights = new double[]{0.0, 0.0, 1.0};
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
//		quantidade.setHorizontalAlignment(SwingConstants.LEFT);
//		quantidade.setDocument(new PlainDocument() {
//			
//			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
//				    if (str == null)
//				      return;
//				    
//				    // permite no m�ximo 9 d�gitos
//
//				    if ((getLength() + str.length()) <= 9 && Character.isDigit(str.charAt(0))) {
//				      super.insertString(offset, str, attr);
//				    }
//				  }
//			
//		});
//		
//		quantidade.addKeyListener(new KeyListener() {
//			
//			public void keyTyped(KeyEvent arg0) {}
//			
//			public void keyReleased(KeyEvent e) {
//				
//				// Apenas fazer modifica��es caso a key seja um n�mero ou o backspace
//				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE )
//				try {
//					String formated = NumberFormat.getNumberInstance(Locale.GERMANY)
//							.parse(quantidade.getText()).toString();
//					
//					quantidade.setText(NumberFormat.getNumberInstance(Locale.GERMANY)
//							.format(Integer.parseInt(formated)));
//					
//				} catch (ParseException exc) {}
//				
//				changeTimes();
//			
//			}
//			
//			public void keyPressed(KeyEvent arg0) {}
//		});
			
		GridBagConstraints gbc_quantidade = new GridBagConstraints();
		gbc_quantidade.insets = new Insets(5, 0, 5, 5);
		gbc_quantidade.gridx = 1;
		gbc_quantidade.gridy = 0;
		add(quantidade, gbc_quantidade);
		quantidade.setColumns(6);
		
		tempoUnit�rio = new JLabel();
		GridBagConstraints gbc_tempoUnit�rio = new GridBagConstraints();
		gbc_tempoUnit�rio.insets = new Insets(5, 0, 5, 5);
		gbc_tempoUnit�rio.gridx = 3;
		gbc_tempoUnit�rio.gridy = 0;
		add(getTempoUnit�rio(), gbc_tempoUnit�rio);
		
		tempoTotal = new JLabel();
		GridBagConstraints gbc_tempoTotal = new GridBagConstraints();
		gbc_tempoTotal.insets = new Insets(0, 0, 5, 0);
		gbc_tempoTotal.gridx = 4;
		gbc_tempoTotal.gridy = 0;
		add(tempoTotal, gbc_tempoTotal);
		
		this.edif�cio = edif�cio;			
		this.unidade = unidade;
		
		// Caso n�o tenha edif�cio (paladino ou nobre)
		if (edif�cio == null)
			if (unidade.equals(Unidade.PALADINO))
				setTempoUnit�rio(unidade.tempoDeProdu��o().multiply(Mundo_Reader.MundoSelecionado.getPorcentagemDeProdu��o(0)));
			else
				setTempoUnit�rio(unidade.tempoDeProdu��o().multiply(Mundo_Reader.MundoSelecionado.getPorcentagemDeProdu��o(1)));
		
	}

	/**
	 * Multiplica o tempo unit�rio pela quantidade, modificando o label tempoTotal.
	 * 
	 * Tamb�m altera o tempo total do edif�cio a que a unidade pertence, caso este n�o seja nulo
	 */
	public void changeTimes(){
		
		BigDecimal tempo = C�lculos.getSeconds(getTempoUnit�rio().getText());
		
		//Remove os pontos de milhar para c�lculo
		
		BigDecimal quantia = quantidade.getValue();
		
		tempo = tempo.multiply(quantia);
		tempoTotal.setText(C�lculos.format(tempo));
		
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
	 * @param BigDecimal tempo, em segundos
	 */
	public void setTempoUnit�rio(BigDecimal tempo) {
		
		tempoUnit�rio.setText(C�lculos.format(tempo));
		
	}
	
	/**
	 * Retorna o tempo total de produ��o da unidade
	 * 
	 * @return BigDecimal tempo, em segundos
	 */
	public BigDecimal getTempoTotal() {
		
		StringBuilder builder = new StringBuilder(tempoTotal.getText());
					
		while (builder.indexOf(".") != -1)
			builder.deleteCharAt(builder.indexOf("."));
		
		try {
			return C�lculos.getSeconds(builder.toString());
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
	
	public JLabel getTempoUnit�rio() {
		return tempoUnit�rio;
	}

	public Unidade getUnidade() {
		return unidade;
	}

}
