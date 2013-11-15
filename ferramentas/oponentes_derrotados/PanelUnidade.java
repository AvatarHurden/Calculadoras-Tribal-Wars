package oponentes_derrotados;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class PanelUnidade{
	
	//Panel com o nome e quantidade da unidade
	private JPanel panelDados;
	
	//Panel com a lblOD
	private JPanel panelOD;
	
	private Unidade unidade;
	private JLabel nome;
	private JTextField quantidade;
	private JLabel lblOD;
	
	private Color cor;
	private GUI gui;
	
	/**
	 * @param Cor que será pintado 
	 * @param Unidade que representa
	 */
	public PanelUnidade(Color cor, Unidade unidade, GUI gui) {
		
		this.cor = cor;
		this.unidade = unidade;
		this.gui = gui;
		
		createPanelDados();
		
		createPanelOD();
		
	}
	/**
	 * Builder para criar o header, com o a descrição de cada parte do panel
	 * 
	 * @param total se a descrição de "OD" deve ser "OD Total"
	 */
	public PanelUnidade(boolean total) {
		
		// Panel Dados
		
		panelDados = new JPanel();
		panelDados.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 100};
		gbl.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0};
		gbl.rowWeights = new double[]{0.0, 0.0, 1.0};
		panelDados.setLayout(gbl);
		
		nome = new JLabel("Unidade");
		GridBagConstraints gbc_nome = new GridBagConstraints();
		gbc_nome.anchor = GridBagConstraints.CENTER;
		gbc_nome.insets = new Insets(5, 5, 5, 5);
		gbc_nome.gridx = 0;
		gbc_nome.gridy = 0;
		panelDados.add(nome, gbc_nome);
					
		JLabel quantidade = new JLabel("Quantidade");
		GridBagConstraints gbc_quantidade = new GridBagConstraints();
		gbc_quantidade.insets = new Insets(5, 0, 5, 5);
		gbc_quantidade.gridx = 1;
		gbc_quantidade.gridy = 0;
		panelDados.add(quantidade, gbc_quantidade);
		
		
		// Panel OD
		
		panelOD = new JPanel();
		panelOD.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout gbl_OD = new GridBagLayout();
		gbl_OD.columnWidths = new int[] {100};
		gbl_OD.rowHeights = new int[] {30};
		gbl_OD.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0};
		gbl_OD.rowWeights = new double[]{0.0, 0.0, 1.0};
		panelOD.setLayout(gbl_OD);

		lblOD = new JLabel("OD");
		
		if (total) lblOD.setText("OD Total");
		
		GridBagConstraints gbc_lblOD = new GridBagConstraints();
		gbc_lblOD.insets = new Insets(5, 0, 5, 5);
		gbc_lblOD.gridx = 0;
		gbc_lblOD.gridy = 0;
		panelOD.add(lblOD, gbc_lblOD);
		
	}

	// Cria o panelDados
	public void createPanelDados() {
		
		panelDados = new JPanel();
		panelDados.setBackground(cor);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 100};
		gbl.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0};
		gbl.rowWeights = new double[]{0.0, 0.0, 1.0};
		panelDados.setLayout(gbl);
		
		nome = new JLabel(unidade.nome());
		GridBagConstraints gbc_nome = new GridBagConstraints();
		gbc_nome.anchor = GridBagConstraints.WEST;
		gbc_nome.insets = new Insets(5, 5, 5, 5);
		gbc_nome.gridx = 0;
		gbc_nome.gridy = 0;
		panelDados.add(nome, gbc_nome);
					
		quantidade = new JTextField();
		quantidade.setHorizontalAlignment(SwingConstants.LEFT);
		quantidade.setDocument(new PlainDocument() {
	
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    // permite no máximo 9 dígitos

				    if ((getLength() + str.length()) <= 9 && Character.isDigit(str.charAt(0))) {
				      super.insertString(offset, str, attr);  
				    }
				  }
			
		});
	
		quantidade.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {}
			
			public void keyReleased(KeyEvent e) {
				
				// Apenas fazer modificações caso a key seja um número ou o backspace
				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE )
				try {
					// Germany has the "." as the thousands separator, so I used it
					String formated = NumberFormat.getNumberInstance(Locale.GERMANY)
							.parse(quantidade.getText()).toString();
					
					quantidade.setText(NumberFormat.getNumberInstance(Locale.GERMANY)
							.format(Integer.parseInt(formated)));
					
				} catch (ParseException exc) {}
				
				if (!quantidade.getText().equals(""))
					changeOD();
				else
					resetOD();
				
				gui.total.setTotal();
				
			}
			
			public void keyPressed(KeyEvent arg0) {}
		});
			
		GridBagConstraints gbc_quantidade = new GridBagConstraints();
		gbc_quantidade.insets = new Insets(5, 0, 5, 5);
		gbc_quantidade.gridx = 1;
		gbc_quantidade.gridy = 0;
		panelDados.add(quantidade, gbc_quantidade);
		quantidade.setColumns(6);
		
	}
	
	// Cria o panelOD
	public void createPanelOD() {
		
		panelOD = new JPanel();
		panelOD.setBackground(cor);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {100};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0};
		gbl.rowWeights = new double[]{0.0, 0.0, 1.0};
		panelOD.setLayout(gbl);

		lblOD = new JLabel();
		GridBagConstraints gbc_lblOD = new GridBagConstraints();
		gbc_lblOD.insets = new Insets(5, 0, 5, 5);
		gbc_lblOD.gridx = 0;
		gbc_lblOD.gridy = 0;
		panelOD.add(lblOD, gbc_lblOD);
		
	}
	
	/**
	 * Multiplica a quantidade pelo OD individual, dependendo de qual RadioButton está escolhido
	 * no GUI
	 */
	protected void changeOD(){
		
		String formated = "";
		try {
			formated = NumberFormat.getNumberInstance(Locale.GERMANY)
					.parse(quantidade.getText()).toString();
		} catch (ParseException e) {}
		
		if (gui.buttonAtaque.isSelected())
			lblOD.setText(NumberFormat.getNumberInstance(Locale.GERMANY)
					.format(gui.getODA(unidade)*Integer.parseInt(formated)));
		
		else if (gui.buttonDefesa.isSelected())
			lblOD.setText( NumberFormat.getNumberInstance(Locale.GERMANY)
					.format(gui.getODD(unidade)*Integer.parseInt(formated)));
	}
	
	private void resetOD() {
		lblOD.setText("");
	}

	protected Unidade getUnidade() { return unidade; }
	
	protected String getQuantidade() { return quantidade.getText(); }
	
	protected String getOD() throws ParseException { 
		return NumberFormat.getNumberInstance(Locale.GERMANY)
			.parse(lblOD.getText()).toString(); 
	}
	
	protected JPanel getPanelDados() { return panelDados; }

	protected JPanel getPanelOD() { return panelOD; }
}

