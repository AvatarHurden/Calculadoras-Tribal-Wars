package io.github.avatarhurden.tribalwarsengine.ferramentas.oponentes_derrotados;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import config.Lang;
import config.Mundo_Reader;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class OponentesDerrotadosPanel extends Ferramenta {

	private List<PanelUnidade> panelUnidadeList = new ArrayList<PanelUnidade>();

	private Map<Unidade, IntegerFormattedTextField> mapQuantidades = new HashMap<Unidade, IntegerFormattedTextField>();
	
	private final Map<Unidade, Integer> pontos_ODA = new HashMap<Unidade, Integer>();
	private final Map<Unidade, Integer> pontos_ODD = new HashMap<Unidade, Integer>();

	PanelSoma total = new PanelSoma();

	JPanel panelButtons;
	JRadioButton buttonDefesa;
	JRadioButton buttonAtaque;
	
	public OponentesDerrotadosPanel() {

		super(Lang.FerramentaOD.toString());

		setUnidades();
		setMaps();

		total.setPanelListAndColor(panelUnidadeList, getNextColor());

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);

		ActionListener reset = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IntegerFormattedTextField t : mapQuantidades.values())
					t.setText("");
			}
		};
		
		gbc.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(reset), gbc);
		
		gbc.anchor = GridBagConstraints.EAST;
		add(tools.addModelosTropasPanel(true, mapQuantidades), gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy++;
		gbc.gridwidth = 1;
		addHeader(true, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		add(unitePanels("dados"), gbc);

		gbc.gridx++;
		add(unitePanels("od"), gbc);

		createPanelButtons();
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridheight = 2;
		add(panelButtons, gbc);
		
		gbc.gridx = 0;
		gbc.gridheight = 1;
		addHeader(false, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		addPanelTotal(gbc);
		

	}
	
	// Cria um painel com os botões para selecionar se o OD mostrado é de ataque
	// ou defesa
	private void createPanelButtons() {

		buttonAtaque = new JRadioButton(Lang.ODAtaque.toString());
		buttonAtaque.setOpaque(false);

		buttonAtaque.addItemListener(new buttonChangeListener());

		buttonDefesa = new JRadioButton(Lang.ODDefesa.toString());
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
	 * 
	 * @param boolean com "nome" e "quantidade", para ser usável embaixo
	 */
	private void addHeader(boolean withIdentifiers, GridBagConstraints gbc) {

		PanelUnidade header = new PanelUnidade(!withIdentifiers);

		if (withIdentifiers) {
			header.getPanelDados().setBorder(
					new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
			add(header.getPanelDados(), gbc);
		}

		gbc.gridx++;
		header.getPanelOD().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(header.getPanelOD(), gbc);

	}

	/**
	 * Junta os panels de todas as unidades num único panel Para unir o panel
	 * com o nome e quantidade da unidade, usar parameter "dados" Para unir o
	 * panel com o OD da unidade, usar parameter "od"
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
		total.getPanelOD().setBorder(
				new LineBorder(Cores.SEPARAR_ESCURO, 1, false));
		add(total.getPanelOD(), gbc);

	}

	/**
	 * Define quais unidades serão utilizadas, com as configurações do mundo
	 */
	private void setUnidades() {
		
		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades())
			if (i != null) {
				panelUnidadeList.add(new PanelUnidade(getNextColor(), i, this));
				mapQuantidades.put(i, panelUnidadeList.get(panelUnidadeList.size() - 1)
						.getTextField());
			}
		
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
