package recrutamento;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import config.Mundo_Reader;
import database.Cores;
import database.Edif�cio;

@SuppressWarnings("serial")
public class PanelEdif�cio extends JPanel {
	
	GridBagConstraints gbc = new GridBagConstraints();
	private Edif�cio edif�cio;
	private ArrayList<PanelUnidade> unidades;
	
	private JTextField txtN�vel;
	private JLabel tempoTotal;
	
	/**
	 * @param possuir cabe�alho com os labels "unidade", "quantidade", etc
	 * @param nome do edif�cio
	 * @param n�vel m�ximo que o edif�cio possui
	 */
	public PanelEdif�cio(Edif�cio edif�cio)  {
		
		this.edif�cio = edif�cio;
		unidades = new ArrayList<PanelUnidade>();
		
		setBackground(Cores.FUNDO_ESCURO);
		
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1,false));
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 100, 100, 125};
		gbl.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl.columnWeights = new double[]{0.0, 1.0, 0.0};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
	}
	
	/**
	 * @param PanelUnidade para ser adicionado
	 */
	public void addPanel(PanelUnidade panel) {
		
		unidades.add(panel);
//		panel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		gbc.gridy++;
		add(panel, gbc);
		
	}
	
	/**
	 * Finaliza o Panel, colocando o seletor de n�vel e JLabel com tempo total de produ��o
	 */
	public void addFinish(PanelUnidade lastPanel) {
		
		// Adiciona a border para separar as unidades do resto
		unidades.get(unidades.size()-1).setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
		
		GridBagConstraints gbc_finish = new GridBagConstraints();
		gbc_finish.gridx = 0;
		gbc_finish.gridy = gbc.gridy;
		
		// Checa se edif�cio � masculino ou feminino (de forma n�o geral mas aplic�vel nesse caso)
		JLabel lblN�vel;
		if(edif�cio.nome().endsWith("a"))
			lblN�vel = new JLabel("N�vel da "+edif�cio.nome());
		else
			lblN�vel = new JLabel("N�vel do "+edif�cio.nome());
		
		gbc_finish.fill = GridBagConstraints.NONE;
		gbc_finish.anchor = GridBagConstraints.CENTER;
		gbc_finish.insets = new Insets(3, 5, 0, 0);
		gbc_finish.gridwidth = 1;
		gbc_finish.gridy++;
		add(lblN�vel, gbc_finish);

		txtN�vel = new JTextField();
		txtN�vel.setHorizontalAlignment(SwingConstants.CENTER);
		txtN�vel.setColumns(3);
		txtN�vel.setDocument(new PlainDocument() {
			
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    // permite no m�ximo 2 d�gitos
				    
				    if ((getLength() + str.length()) <= 2 && Character.isDigit(str.charAt(0))) {
				      super.insertString(offset, str, attr);
				    }
				  }
			
		});
		
		txtN�vel.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {}
			
			public void keyReleased(KeyEvent e) {
				
				// Apenas fazer modifica��es caso a key seja um n�mero ou o backspace
				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE ) {
					
					if (!txtN�vel.getText().equals("")) {
						
						// N�o permite o n�vel inserido ser maior do que o n�vel m�ximo do edif�cio
						if (Integer.parseInt(txtN�vel.getText()) > edif�cio.n�velM�ximo())
							txtN�vel.setText(""+edif�cio.n�velM�ximo());
						
						// Passa por todas as unidades do edif�cio, definindo o tempo individual
						for (PanelUnidade panel : unidades) {

							BigDecimal tempo = panel.getUnidade().tempoDeProdu��o().
								multiply(Mundo_Reader.MundoSelecionado.
										getPorcentagemDeProdu��o(Integer.parseInt(txtN�vel.getText())));
						
							panel.getTempoUnit�rio().setText(C�lculos.format(tempo));
							panel.changeTimes();
						
						}
					}
				}
				
			}
			
			public void keyPressed(KeyEvent arg0) {}
		});
		
		txtN�vel.setText("1");
		
		// Primeira passada para definir os tempos logo que o panel � formado
		for (PanelUnidade panel : unidades) {

			BigDecimal tempo = panel.getUnidade().tempoDeProdu��o().
					multiply(Mundo_Reader.MundoSelecionado.
							getPorcentagemDeProdu��o(Integer.parseInt(txtN�vel.getText())));
			
			panel.getTempoUnit�rio().setText(C�lculos.format(tempo));
		}
		

		gbc_finish.anchor = GridBagConstraints.WEST;
		gbc_finish.fill = GridBagConstraints.VERTICAL;
		gbc_finish.insets = new Insets(7, 0, 5, 0);
		gbc_finish.gridx = 1;
		add(txtN�vel, gbc_finish);
		
		JLabel lblTempoTotal = new JLabel("Tempo Total:");
		gbc_finish.anchor = GridBagConstraints.CENTER;
		gbc_finish.gridx = 2;
		add(lblTempoTotal, gbc_finish);
		
		tempoTotal = new JLabel();
		gbc_finish.insets = new Insets(7, 0, 5, 5);
		gbc_finish.fill = GridBagConstraints.NONE;
		gbc_finish.gridx = 3;
		add(tempoTotal, gbc_finish);
		
	}
	
	/**
	 * Soma os tempos totais de cada unidade, colocando o resultado no Label tempoTotal
	 */
	public void setTempoTotal() {
		
		BigDecimal tempoTotal = BigDecimal.ZERO;
		
		for (PanelUnidade panel : unidades) {
			
			tempoTotal = tempoTotal.add(panel.getTempoTotal());
			
		}
		if (!tempoTotal.equals(BigDecimal.ZERO))
			this.tempoTotal.setText(C�lculos.format(tempoTotal));
		else
			this.tempoTotal.setText("");
		
	}
}
