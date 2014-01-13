package oponentes_derrotados;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import database.Cores;
import database.TroopFormattedTextField;
import database.Unidade;

@SuppressWarnings("serial")
public class PanelUnidade{
	
	//Panel com o nome e quantidade da unidade
	private JPanel panelDados;
	
	//Panel com a lblOD
	private JPanel panelOD;
	
	private Unidade unidade;
	private JLabel nome;
	private TroopFormattedTextField quantidade;
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
					
		quantidade = new TroopFormattedTextField(9) {
			
			@Override
			public void go() {
				
			if (!quantidade.getText().equals(""))
				changeOD();
			else
				resetOD();
			
			gui.total.setTotal();
				
			}
		};
		quantidade.setColumns(6);
			
		GridBagConstraints gbc_quantidade = new GridBagConstraints();
		gbc_quantidade.insets = new Insets(5, 0, 5, 5);
		gbc_quantidade.gridx = 1;
		gbc_quantidade.gridy = 0;
		panelDados.add(quantidade, gbc_quantidade);
		
		
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
		
		BigDecimal quantia = quantidade.getValue();
		
		if (gui.buttonAtaque.isSelected())
			lblOD.setText(quantidade.numberFormat.format(
					gui.getODA(unidade).multiply(quantia)));
		
		else if (gui.buttonDefesa.isSelected())
			lblOD.setText(quantidade.numberFormat.format(
					gui.getODD(unidade).multiply(quantia)));
	}
	
	private void resetOD() {
		lblOD.setText("");
	}

	protected Unidade getUnidade() { return unidade; }
	
	protected String getQuantidade() { return quantidade.getText(); }
	
	protected String getOD() throws ParseException { 
		return quantidade.numberFormat.parse(lblOD.getText()).toString(); 
	}
	
	protected JPanel getPanelDados() { return panelDados; }

	protected JPanel getPanelOD() { return panelOD; }
}

