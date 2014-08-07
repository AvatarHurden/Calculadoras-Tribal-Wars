package dados_de_unidade;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;

import config.Lang;
import config.Mundo_Reader;
import custom_components.Ferramenta;
import custom_components.IntegerFormattedTextField;
import database.Cores;
import database.Unidade;
import frames.MainWindow;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {

	// List<Unidade> unidadesUtilizadas = new ArrayList<Unidade>();
	// List<PanelUnidade> panelUnidadeList = new ArrayList<PanelUnidade>();

	Map<Unidade, IntegerFormattedTextField> mapQuantidades = new HashMap<Unidade, IntegerFormattedTextField>();

	List<PanelUnidade> panelUnidadeList = new ArrayList<PanelUnidade>();

	Map<String, BigInteger> somaTotal = new HashMap<String, BigInteger>();

	PanelSoma total = new PanelSoma();

	/**
	 * Ferramenta com informações de unidades. Possui: 
	 * <br>- Ataque 
	 * <br>- Defesas 
	 * <br>- Saque
	 * <br>- Custo de recursos para produção 
	 * <br>- Uso de população
	 * 
	 * Em caso de mundo com níveis, é possível escolher o nível das unidades
	 * (não é limitado a 15 níveis)
     *
     * Não consigo entender a gambiarra ( Os recursos tecnicos avançados ) que você usou nesse jPanel... Se puder ajeitar eu agradeceria :)
	 */
	public GUI() {
		super(Lang.FerramentaUnidade.toString());
		setUnidades();

		total.setPanelListAndColor(panelUnidadeList, getNextColor());

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);

		ActionListener action = new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				for (IntegerFormattedTextField i : mapQuantidades.values())
					i.setText("");
				
				for (PanelUnidade p : panelUnidadeList)
					p.getNível().setSelectedIndex(0);
				
			}
		};
		
		gbc.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(action), gbc);
		
		gbc.anchor = GridBagConstraints.EAST;
		add(tools.addModelosTropasPanel(true, mapQuantidades), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		addHeader(true, gbc);

		gbc.gridy++;
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
	 * Adiciona um cabeçalho com os nomes das informações de cada coluna
	 * 
	 * @param withIdentifiers com "nome" e "quantidade", para ser usável embaixo
	 */
	private void addHeader(boolean withIdentifiers, GridBagConstraints gbc) {

		PanelUnidade header = new PanelUnidade();

		if (withIdentifiers) {
			header.getIdentificadores().setBorder(
					new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
			add(header.getIdentificadores(), gbc);
		}

		gbc.gridx++;
		header.getDadosPrincipais().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(header.getDadosPrincipais(), gbc);

		gbc.gridx++;
		header.getDadosCusto().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(header.getDadosCusto(), gbc);

	}

	/**
	 * Junta os panels de todas as unidades num único panel
	 * 
	 * @param string  qual panel pegar
	 */
	private JPanel unitePanels(String string) {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		for (PanelUnidade i : panelUnidadeList) {

			gbc.gridy++;
			if ( string.toLowerCase().equals("identificadores"))
				panel.add(i.getIdentificadores(), gbc);
			else if ( string.toLowerCase().equals("dadosprincipais"))
				panel.add(i.getDadosPrincipais(), gbc);
			else if ( string.toLowerCase().equals("dadoscusto"))
				panel.add(i.getDadosCusto(), gbc);
		}

		return panel;

	}

	/**
	 * Adiciona o PanelTotal no gui
	 */
	private void addPanelTotal(GridBagConstraints gbc) {

		gbc.gridx++;
		total.getDadosPrincipais().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(total.getDadosPrincipais(), gbc);

		gbc.gridx++;
		total.getDadosCusto().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(total.getDadosCusto(), gbc);

	}

	/**
	 * Define quais unidades serão utilizadas, com as configurações do mundo
	 */
	private void setUnidades() {

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {

			if (i != null) {
				panelUnidadeList
						.add(new PanelUnidade(getNextColor(), i, total));
				mapQuantidades.put(i,
						panelUnidadeList.get(panelUnidadeList.size() - 1)
								.getQuantidade());
			}

		}

	}

}
