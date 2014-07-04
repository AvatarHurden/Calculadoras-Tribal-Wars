package pontos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Lang;
import config.Mundo_Reader;
import custom_components.Edif�cioFormattedTextField;
import custom_components.Ferramenta;
import database.Cores;
import database.Edif�cio;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {

	List<Edif�cio> edif�ciosUtilizados = new ArrayList<Edif�cio>();
	List<PanelEdif�cio> panelEdif�cioList = new ArrayList<PanelEdif�cio>();

	Map<String, BigInteger> somaTotal = new HashMap<String, BigInteger>();

	PanelSoma total = new PanelSoma();

	/**
	 * Ferramenta que calcula a pontua��o e a popula��o utilizada com base nos
	 * n�veis dos edif�cios inseridos. Tamb�m calcula a popula��o restante
	 * atrav�s do n�vel fornecido da fazenda
	 */
	public GUI() {

		super(Lang.FerramentaPontos.toString());

		setEdif�cios();

		total.setPanelListAndColor(panelEdif�cioList, getNextColor());

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
				
				for (PanelEdif�cio i : panelEdif�cioList)
					i.getComboBox().setText(" ");
				
			}
		};
		
		gbc.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(action), gbc);
		
		Map<Edif�cio, Edif�cioFormattedTextField> map = new HashMap<Edif�cio, Edif�cioFormattedTextField>();
		for (PanelEdif�cio i : panelEdif�cioList)
			map.put(i.getEdif�cio(), i.getComboBox());
		
		gbc.gridx++;
		//gbc.insets = new Insets(5, 0, 5, 0);
		add(tools.addModelosAldeiasPanel(true, map, null), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(5, 5, 5, 5);
		addHeader(true, gbc);

		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		add(unitePanels("identificadores"), gbc);

		gbc.gridwidth = 1;
		gbc.gridx += 2;
		add(unitePanels("dadosPanel"), gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		addHeader(false, gbc);

		gbc.gridy++;
		gbc.gridx = 1;
		addPanelTotal(gbc);

	}

	/**
	 * Adiciona um cabe�alho com os nomes das informa��es de cada coluna
	 * 
	 * @param boolean com "edif�cio" e "n�vel", para ser us�vel embaixo
	 */
	private void addHeader(boolean topHeader, GridBagConstraints gbc) {

		PanelEdif�cio header = new PanelEdif�cio(!topHeader);

		if (topHeader) {
			gbc.gridwidth = 2;
			header.getIdentificadores().setBorder(
					new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
			add(header.getIdentificadores(), gbc);
			
		}
		
		gbc.gridwidth = 1;
		gbc.gridx += 2;
		header.getDadosPanel().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(header.getDadosPanel(), gbc);

		if (!topHeader) {
			gbc.gridx++;
			header.getPopula��oRestantePanel().setBorder(
					new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
			add(header.getPopula��oRestantePanel(), gbc);
		}

	}

	/**
	 * Junta os panels de todas as unidades num �nico panel
	 * 
	 * @param String
	 *            qual panel pegar
	 */
	private JPanel unitePanels(String s) {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		for (PanelEdif�cio i : panelEdif�cioList) {

			gbc.gridy++;
			if (s.toLowerCase().equals("identificadores"))
				panel.add(i.getIdentificadores(), gbc);
			else if (s.toLowerCase().equals("dadospanel"))
				panel.add(i.getDadosPanel(), gbc);

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
		total.getPopula��oRestantePanel().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(total.getPopula��oRestantePanel(), gbc);

	}

	/**
	 * Cria a lista de PanelEdif�cio, utilizando os edif�cios do mundo
	 */
	private void createPanelEdif�cio() {

		for (Edif�cio i : edif�ciosUtilizados)
			panelEdif�cioList.add(new PanelEdif�cio(getNextColor(), i, total));

	}

	/**
	 * Define quais edif�cios ser�o utilizadas, com as configura��es do mundo
	 */
	private void setEdif�cios() {

		edif�ciosUtilizados.add(Edif�cio.EDIF�CIO_PRINCIPAL);
		edif�ciosUtilizados.add(Edif�cio.QUARTEL);
		edif�ciosUtilizados.add(Edif�cio.EST�BULO);
		edif�ciosUtilizados.add(Edif�cio.OFICINA);

		if (Mundo_Reader.MundoSelecionado.hasIgreja()) {
			edif�ciosUtilizados.add(Edif�cio.IGREJA);
			edif�ciosUtilizados.add(Edif�cio.PRIMEIRA_IGREJA);
		}

		if (Mundo_Reader.MundoSelecionado.isAcademiaDeN�veis())
			edif�ciosUtilizados.add(Edif�cio.ACADEMIA_3N�VEIS);
		else
			edif�ciosUtilizados.add(Edif�cio.ACADEMIA_1N�VEL);

		edif�ciosUtilizados.add(Edif�cio.FERREIRO);
		edif�ciosUtilizados.add(Edif�cio.PRA�A_DE_REUNI�O);

		if (Mundo_Reader.MundoSelecionado.hasPaladino())
			edif�ciosUtilizados.add(Edif�cio.EST�TUA);

		edif�ciosUtilizados.add(Edif�cio.MERCADO);
		edif�ciosUtilizados.add(Edif�cio.BOSQUE);
		edif�ciosUtilizados.add(Edif�cio.PO�O_DE_ARGILA);
		edif�ciosUtilizados.add(Edif�cio.MINA_DE_FERRO);
		edif�ciosUtilizados.add(Edif�cio.FAZENDA);
		edif�ciosUtilizados.add(Edif�cio.ARMAZ�M);
		edif�ciosUtilizados.add(Edif�cio.ESCONDERIJO);
		edif�ciosUtilizados.add(Edif�cio.MURALHA);

		createPanelEdif�cio();
	}

}
