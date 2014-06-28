package recrutamento;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Lang;
import config.Mundo_Reader;
import custom_components.Ferramenta;
import custom_components.IntegerFormattedTextField;
import database.Cores;
import database.Edifício;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {
	
	private List<PanelEdifício> edifícioList = new ArrayList<PanelEdifício>();
	
	private Map<Unidade, PanelUnidade> panelUnidadeMap = new HashMap<Unidade, PanelUnidade>();
	
	private Map<Unidade, IntegerFormattedTextField> mapQuantidades = new HashMap<Unidade, IntegerFormattedTextField>();
	
	/**
	 * Ferramenta para calcular o tempo necessário de produção de unidades. 
	 * <br>Ela mostra: 
	 * <br>- tempo de produção unitária de cada unidade 
	 * <br>- tempo de produção total para cada unidade 
	 * <br>- tempo de produção total para cada edifício
	 */
	public GUI() {

		super(Lang.FerramentaRecruta.toString());

		setBackground(Cores.FUNDO_CLARO);
		setOpaque(true);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 200, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 100, 100, 50, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		createPanelUnidadeList();
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		add(createToolPanel(), c);
		
		c.gridy++;
		add(createHeader(), c);
		
		c.gridy++;
		c.insets = new Insets(0, 5, 5, 5);
		addPanelsToGUI(c);
	
	}

	private void createPanelUnidadeList() {
		
		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades())
			if (i != null && !i.equals(Unidade.MILÍCIA)) {
				panelUnidadeMap.put(i, new PanelUnidade(getNextColor(), i));
				mapQuantidades.put(i, panelUnidadeMap.get(i).getTextField());
			}
		
		
	}
	
	private void addPanelsToGUI(GridBagConstraints c) {
		
		PanelEdifício quartel = new PanelEdifício(Edifício.QUARTEL);
		edifícioList.add(quartel);
		
		quartel.addPanel(panelUnidadeMap.get(Unidade.LANCEIRO));
		quartel.addPanel(panelUnidadeMap.get(Unidade.ESPADACHIM));
		quartel.addPanel(panelUnidadeMap.get(Unidade.BÁRBARO));
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			quartel.addPanel(panelUnidadeMap.get(Unidade.ARQUEIRO));
		quartel.finish();
		
		add(quartel, c);
		
		PanelEdifício estábulo = new PanelEdifício(Edifício.ESTÁBULO);
		edifícioList.add(estábulo);
		
		estábulo.addPanel(panelUnidadeMap.get(Unidade.EXPLORADOR));
		estábulo.addPanel(panelUnidadeMap.get(Unidade.CAVALOLEVE));
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			estábulo.addPanel(panelUnidadeMap.get(Unidade.ARCOCAVALO));
		estábulo.addPanel(panelUnidadeMap.get(Unidade.CAVALOPESADO));
		estábulo.finish();
		
		c.gridy++;
		add(estábulo, c);
		
		PanelEdifício oficina = new PanelEdifício(Edifício.OFICINA);
		edifícioList.add(oficina);
		
		oficina.addPanel(panelUnidadeMap.get(Unidade.ARÍETE));
		oficina.addPanel(panelUnidadeMap.get(Unidade.CATAPULTA));
		oficina.finish();
		
		c.gridy++;
		add(oficina, c);
		
		if (Mundo_Reader.MundoSelecionado.isAcademiaDeNíveis()) {
			
			PanelEdifício academia = new PanelEdifício(Edifício.ACADEMIA_3NÍVEIS);
			edifícioList.add(academia);
			
			academia.addPanel(panelUnidadeMap.get(Unidade.NOBRE));
			academia.finish();
			
			c.gridy++;
			add(academia, c);
			
		} else {
			
			panelUnidadeMap.get(Unidade.NOBRE).setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			c.gridy++;
			add(panelUnidadeMap.get(Unidade.NOBRE), c);
			
		}
		
		if (Mundo_Reader.MundoSelecionado.hasPaladino()) {
			
			panelUnidadeMap.get(Unidade.PALADINO).setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			c.gridy++;
			add(panelUnidadeMap.get(Unidade.PALADINO), c);
			
		}
			
		
	}
	
	private JPanel createToolPanel() {
		
		JPanel toolPanel = new JPanel();
		toolPanel.setOpaque(false);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 125, 100, 100, 125 };
		gbl.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl.rowWeights = new double[] { Double.MIN_VALUE };
		toolPanel.setLayout(gbl);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 0;
		c.gridx = 0;
		
		ActionListener reset = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				for (IntegerFormattedTextField i : mapQuantidades.values())
					i.setText("");
				
				for (PanelEdifício i : edifícioList)
					i.getComboBox().setText("1");
				
				mapQuantidades.get(Unidade.LANCEIRO).requestFocus();
				
			}
		};
		
		c.anchor = GridBagConstraints.WEST;
		toolPanel.add(tools.addResetPanel(reset), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.CENTER;
		toolPanel.add(tools.addModelosTropasPanel(true, mapQuantidades), c);

		return toolPanel;
		
	}
	
	/**
	 * Adiciona a barra com os nomes de cada coluna
	 */
	private JPanel createHeader() {

		JPanel header = new JPanel();

		header.setBackground(Cores.FUNDO_ESCURO);
		header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 125, 100, 100, 125 };
		gbl.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		header.setLayout(gbl);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		
		JLabel lblUnidades = new JLabel(Lang.Unidade.toString());
		header.add(lblUnidades, c);

		JLabel lblQuantidade = new JLabel(Lang.Quantidade.toString());
	
		c.insets = new Insets(5, 0, 5, 5);
		c.gridx++;
		header.add(lblQuantidade, c);

		JLabel lblTempoPorUnidade = new JLabel(Lang.TempoUnidade.toString());
	
		c.gridx++;	
		header.add(lblTempoPorUnidade, c);

		JLabel lblTempoTotal = new JLabel(Lang.TempoTotal.toString());
		
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx++;
		header.add(lblTempoTotal, c);

		return header;
		
	}

}
