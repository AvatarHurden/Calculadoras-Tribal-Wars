package oponentes_derrotados;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import config.Mundo_Reader;
import database.Cores;
import database.Ferramenta;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {
	
	private List<PanelUnidade> panelUnidadeList = new ArrayList<PanelUnidade>();
	
	private final Map<Unidade, Integer> pontos_ODA = new HashMap<Unidade, Integer>();
	private final Map<Unidade, Integer> pontos_ODD = new HashMap<Unidade, Integer>();
	
	PanelSoma total = new PanelSoma();
	
	JPanel panelButtons;
	JRadioButton buttonDefesa;
	JRadioButton buttonAtaque;
	
	public GUI() {
		
		super("Cálculo de OD");
		
		setBackground(Cores.FUNDO_CLARO);
		
		setUnidades();
		setMaps();
		
		total.setPanelListAndColor(panelUnidadeList, getNextColor());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0 ,0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		createPanelButtons();
		gbc.gridwidth = 2;
		add(panelButtons, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		addHeader(true, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		add(unitePanels("dados"), gbc);
		
		gbc.gridx++;
		add(unitePanels("od"), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		addHeader(false, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		addPanelTotal(gbc);
		
	}
	
	// Cria um painel com os botões para selecionar se o OD mostrado é de ataque ou defesa
	private void createPanelButtons() {
		
		buttonAtaque = new JRadioButton("OD Ataque");
		buttonAtaque.setOpaque(false);
		
		buttonAtaque.addItemListener(new buttonChangeListener());
		
		buttonDefesa = new JRadioButton("OD Defesa");
		buttonDefesa.setOpaque(false);
		
		buttonDefesa.addItemListener(new buttonChangeListener());
		
		ButtonGroup group = new ButtonGroup();
		group.add(buttonAtaque);
		group.add(buttonDefesa);
		
		buttonAtaque.setSelected(true);
		
		panelButtons = new JPanel(new GridBagLayout());
		panelButtons.add(buttonAtaque);
		panelButtons.add(buttonDefesa);
		
		panelButtons.setOpaque(false);
			
	}

	// Classe para os botões de ataque e defesa
	class buttonChangeListener implements ItemListener {
		public void itemStateChanged(ItemEvent arg0) {
		
			for (PanelUnidade i : panelUnidadeList)
				if (!i.getQuantidade().equals(""))
					i.changeOD();
			
			total.setTotal();
		}
	}
	
	/**
	 * Adiciona um cabeçalho com os nomes das informações de cada coluna
	 * @param boolean com "nome" e "quantidade", para ser usável embaixo 
	 */
	private void addHeader(boolean withIdentifiers, GridBagConstraints gbc) {
		 
		PanelUnidade header = new PanelUnidade(!withIdentifiers);
		
		if (withIdentifiers) {
			header.getPanelDados().setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1,false));
			add(header.getPanelDados(), gbc);
		}
		
		gbc.gridx++;
		header.getPanelOD().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,false));
		add(header.getPanelOD(), gbc);
		
	}
	
	/**
	 * Junta os panels de todas as unidades num único panel
	 * Para unir o panel com o nome e quantidade da unidade, usar parameter "dados"
	 * Para unir o panel com o OD da unidade, usar parameter "od"
	 * 
	 * @param String qual panel pegar
	 */
	private JPanel unitePanels(String s) {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1,false));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		
		for (PanelUnidade i : panelUnidadeList) {
			
			gbc.gridy++;
			if (s.toLowerCase().equals("dados"))
				panel.add(i.getPanelDados(), gbc);
			else if (s.toLowerCase().equals("od"))
				panel.add(i.getPanelOD(), gbc);
			
		}
		
		return panel;
		
	}
	
	/**
	 * Adiciona o PanelTotal no gui
	 */
	private void addPanelTotal(GridBagConstraints gbc) {		
		
		gbc.gridx++;
		total.getPanelOD().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,false));
		add(total.getPanelOD(), gbc);
		
	}
	
	/**
	 * Define quais unidades serão utilizadas, com as configurações do mundo
	 */
 	private void setUnidades() {
	
 		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.LANCEIRO, this));
 		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.ESPADACHIM, this));
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro()) 
			panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.ARQUEIRO, this));
		
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.BÁRBARO, this));
		
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.EXPLORADOR, this));
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.CAVALOLEVE, this));
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.ARCOCAVALO, this));
		
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.CAVALOPESADO, this));
		
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.ARÍETE, this));
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.CATAPULTA, this));
		panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.NOBRE, this));
		
		if (Mundo_Reader.MundoSelecionado.hasMilícia())
			panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.MILÍCIA, this));
		
		if (Mundo_Reader.MundoSelecionado.hasPaladino())
			panelUnidadeList.add(new PanelUnidade(getNextColor(),Unidade.PALADINO, this));
		
	}
	
 	// Cria os mapas com os valores de OD
 	private void setMaps() {
 		
 		pontos_ODA.put(Unidade.LANCEIRO, 4);
 		pontos_ODA.put(Unidade.ESPADACHIM, 5);
 		pontos_ODA.put(Unidade.BÁRBARO, 1);
 		pontos_ODA.put(Unidade.ARQUEIRO, 5);
 		pontos_ODA.put(Unidade.EXPLORADOR, 1);
 		pontos_ODA.put(Unidade.CAVALOLEVE, 5);
 		pontos_ODA.put(Unidade.ARCOCAVALO, 6);
 		pontos_ODA.put(Unidade.CAVALOPESADO, 23);
 		pontos_ODA.put(Unidade.ARÍETE, 4);
 		pontos_ODA.put(Unidade.CATAPULTA, 12);
 		pontos_ODA.put(Unidade.PALADINO, 40);
 		pontos_ODA.put(Unidade.MILÍCIA, 4);
 		pontos_ODA.put(Unidade.NOBRE, 200);
 		
 		pontos_ODD.put(Unidade.LANCEIRO, 1);
 		pontos_ODD.put(Unidade.ESPADACHIM, 2);
 		pontos_ODD.put(Unidade.BÁRBARO, 4);
 		pontos_ODD.put(Unidade.ARQUEIRO, 2);
 		pontos_ODD.put(Unidade.EXPLORADOR, 2);
 		pontos_ODD.put(Unidade.CAVALOLEVE, 13);
 		pontos_ODD.put(Unidade.ARCOCAVALO, 12);
 		pontos_ODD.put(Unidade.CAVALOPESADO, 15);
 		pontos_ODD.put(Unidade.ARÍETE, 8);
 		pontos_ODD.put(Unidade.CATAPULTA, 10);
 		pontos_ODD.put(Unidade.PALADINO, 20);
 		pontos_ODD.put(Unidade.MILÍCIA, 0);
 		pontos_ODD.put(Unidade.NOBRE, 200);
 			
 	}
 	
 	public BigDecimal getODA(Unidade unidade) { 
 		return new BigDecimal(pontos_ODA.get(unidade)); 
 	}
 	
 	public BigDecimal getODD(Unidade unidade) { 
 		return new BigDecimal(pontos_ODD.get(unidade)); 
 	}
	
}
