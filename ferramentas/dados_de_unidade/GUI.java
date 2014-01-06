package dados_de_unidade;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import database.Cores;
import database.Ferramenta;
import database.Unidade;
import config.Mundo_Reader;;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {

	List<Unidade> unidadesUtilizadas = new ArrayList<Unidade>();
	List<PanelUnidade> panelUnidadeList = new ArrayList<PanelUnidade>();
	
	Map<String, BigInteger> somaTotal = new HashMap<String, BigInteger>();
	
	PanelSoma total = new PanelSoma();
	
	/**
	 * Ferramenta com informa��es de unidades
	 * Possui:
	 * - Ataque
	 * - Defesas
	 * - Saque
	 * - Custo de recursos para produ��o
	 * - Uso de popula��o
	 * 
	 * Em caso de mundo com n�veis, � poss�vel escolher o n�vel das unidades (n�o � limitado a 15)
	 */
	public GUI() {
		
		super("C�lculo de Unidades");
		
		setBackground(Cores.FUNDO_CLARO);
		setUnidades();
		
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
		
		addHeader(true, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 0;
		add(unitePanels("identificadores"), gbc);
		
		gbc.gridx++;
		add(unitePanels("dadosPrincipais"), gbc);
		
		gbc.gridx++;
		add(unitePanels("dadosCusto"), gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		addHeader(false, gbc);
		
		gbc.gridy = 3;
		gbc.gridx = 0;
		addPanelTotal(gbc);
		
	}
	
	/**
	 * Adiciona um cabe�alho com os nomes das informa��es de cada coluna
	 * @param boolean com "nome" e "quantidade", para ser us�vel embaixo 
	 */
	private void addHeader(boolean withIdentifiers, GridBagConstraints gbc) {
		 
		PanelUnidade header = new PanelUnidade();
		
		if (withIdentifiers) {
			header.getIdentificadores().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,true));
			add(header.getIdentificadores(), gbc);
		}
		
		gbc.gridx++;
		header.getDadosPrincipais().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,true));
		add(header.getDadosPrincipais(), gbc);
		
		gbc.gridx++;
		header.getDadosCusto().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,true));
		add(header.getDadosCusto(), gbc);
		
	}
	
	/**
	 * Junta os panels de todas as unidades num �nico panel
	 * 
	 * @param String qual panel pegar
	 */
	private JPanel unitePanels(String s) {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, true));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		
		for (PanelUnidade i : panelUnidadeList) {
			
			gbc.gridy++;
			if (s.toLowerCase().equals("identificadores"))
				panel.add(i.getIdentificadores(), gbc);
			else if (s.toLowerCase().equals("dadosprincipais"))
				panel.add(i.getDadosPrincipais(), gbc);
			else if (s.toLowerCase().equals("dadoscusto"))
				panel.add(i.getDadosCusto(), gbc);
			
		}
		
		return panel;
		
	}
	
	/**
	 * Adiciona o PanelTotal no gui
	 */
	private void addPanelTotal(GridBagConstraints gbc) {		
		
		gbc.gridx++;
		total.getDadosPrincipais().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,true));
		add(total.getDadosPrincipais(), gbc);
		
		gbc.gridx++;
		total.getDadosCusto().setBorder(new LineBorder(Cores.SEPARAR_ESCURO,1,true));
		add(total.getDadosCusto(), gbc);
		
	}
	
	/**
	 * Cria a lista de PanelUnidade, utilizando as unidades do mundo
	 */
	private void createPanelUnidade() {
		
		for (Unidade i : unidadesUtilizadas)
			panelUnidadeList.add(new PanelUnidade(getNextColor(),i, total));
		
	}
	
	/**
	 * Define quais unidades ser�o utilizadas, com as configura��es do mundo
	 */
 	private void setUnidades() {
	
		unidadesUtilizadas.add(Unidade.LANCEIRO);
		unidadesUtilizadas.add(Unidade.ESPADACHIM);
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro()) 
			unidadesUtilizadas.add(Unidade.ARQUEIRO);
		
		unidadesUtilizadas.add(Unidade.B�RBARO);
		
		unidadesUtilizadas.add(Unidade.EXPLORADOR);
		unidadesUtilizadas.add(Unidade.CAVALOLEVE);
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro()) 
			unidadesUtilizadas.add(Unidade.ARCOCAVALO);
		
		unidadesUtilizadas.add(Unidade.CAVALOPESADO);
		
		unidadesUtilizadas.add(Unidade.AR�ETE);
		unidadesUtilizadas.add(Unidade.CATAPULTA);
		unidadesUtilizadas.add(Unidade.NOBRE);
		
		if (Mundo_Reader.MundoSelecionado.hasMil�cia())
			unidadesUtilizadas.add(Unidade.MIL�CIA);
		
		if (Mundo_Reader.MundoSelecionado.hasPaladino())
			unidadesUtilizadas.add(Unidade.PALADINO);
		
		createPanelUnidade();
	}

	
}
