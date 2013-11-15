package recrutamento;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import database.Cores;
import database.Edifício;
import database.Ferramenta;
import database.MundoSelecionado;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {
	
	private Map<Edifício, PanelEdifício> mapaEdifício = new HashMap<Edifício, PanelEdifício>();
	
	/**
	 * Ferramenta para calcular o tempo necessário de produção de unidades
	 * Ela mostra: 
	 * - tempo de produção unitária de cada unidade
	 * - tempo de produção total para cada unidade
	 * - tempo de produção total para cada edifício
	 */
	public GUI() {
		
		super("Tempo de Recruta");
		
		setBackground(Cores.FUNDO_CLARO);
		setOpaque(true);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {200, 0};
		gridBagLayout.rowHeights = new int[] {0, 100, 100, 50, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		addHeader();
		
		PanelEdifício quartel = new PanelEdifício(Edifício.QUARTEL);
		mapaEdifício.put(Edifício.QUARTEL, quartel);
		
		GridBagConstraints gbc_quartel = new GridBagConstraints();
		gbc_quartel.anchor = GridBagConstraints.CENTER;
		gbc_quartel.fill = GridBagConstraints.BOTH;
		gbc_quartel.insets = new Insets(0, 5, 5, 5);
		gbc_quartel.gridx = 0;
		gbc_quartel.gridy = 1;
		add(quartel, gbc_quartel);
		

		PanelUnidade lanceiro = new PanelUnidade(getNextColor(), Unidade.LANCEIRO, quartel);
		quartel.addPanel(lanceiro);
		
		PanelUnidade espadachim = new PanelUnidade(getNextColor(), Unidade.ESPADACHIM, quartel);
		quartel.addPanel(espadachim);

		if (MundoSelecionado.hasArqueiro()) {
			PanelUnidade arqueiro = new PanelUnidade(getNextColor(), Unidade.ARQUEIRO, quartel);
			quartel.addPanel(arqueiro);
		}

		PanelUnidade bárbaro = new PanelUnidade(getNextColor(), Unidade.BÁRBARO, quartel);
		quartel.addPanel(bárbaro);
		
		quartel.addFinish(bárbaro);
		
		
		PanelEdifício estábulo = new PanelEdifício(Edifício.ESTÁBULO);
		mapaEdifício.put(Edifício.ESTÁBULO, estábulo);
		
		GridBagConstraints gbc_estábulo = new GridBagConstraints();
		gbc_estábulo.anchor = GridBagConstraints.CENTER;
		gbc_estábulo.fill = GridBagConstraints.BOTH;
		gbc_estábulo.insets = new Insets(0, 5, 5, 5);
		gbc_estábulo.gridx = 0;
		gbc_estábulo.gridy = 2;
		add(estábulo, gbc_estábulo);
	
		PanelUnidade explorador = new PanelUnidade(getNextColor(), Unidade.EXPLORADOR, estábulo);
		estábulo.addPanel(explorador);
		
		PanelUnidade cavalariaLeve = new PanelUnidade(getNextColor(), Unidade.CAVALOLEVE, estábulo);
		estábulo.addPanel(cavalariaLeve);

		if (MundoSelecionado.hasArqueiro()) {
			PanelUnidade arqueiroCavalo = new PanelUnidade(getNextColor(), Unidade.ARCOCAVALO, estábulo);
			estábulo.addPanel(arqueiroCavalo);
		}
		
		PanelUnidade cavalariaPesada = new PanelUnidade(getNextColor(), Unidade.CAVALOPESADO, estábulo);
		estábulo.addPanel(cavalariaPesada);
		
		estábulo.addFinish(cavalariaPesada);
		
		
		PanelEdifício oficina = new PanelEdifício(Edifício.OFICINA);
		mapaEdifício.put(Edifício.OFICINA, oficina);
		
		GridBagConstraints gbc_oficina = new GridBagConstraints();
		gbc_oficina.anchor = GridBagConstraints.CENTER;
		gbc_oficina.fill = GridBagConstraints.BOTH;
		gbc_oficina.insets = new Insets(0, 5, 5, 5);
		gbc_oficina.gridx = 0;
		gbc_oficina.gridy = 3;
		add(oficina, gbc_oficina);
	
		PanelUnidade aríete = new PanelUnidade(getNextColor(), Unidade.ARÍETE, oficina);
		oficina.addPanel(aríete);
		
		PanelUnidade catapulta = new PanelUnidade(getNextColor(), Unidade.CATAPULTA, oficina);
		oficina.addPanel(catapulta);
		
		oficina.addFinish(catapulta);	
		
		
		PanelEdifício academia;
		
		if (MundoSelecionado.isAcademiaDeNíveis()) {
			
			academia = new PanelEdifício(Edifício.ACADEMIA_3NÍVEIS);
			mapaEdifício.put(Edifício.ACADEMIA_3NÍVEIS, academia);
			
			GridBagConstraints gbc_academia = new GridBagConstraints();
			gbc_academia.anchor = GridBagConstraints.CENTER;
			gbc_academia.fill = GridBagConstraints.BOTH;
			gbc_academia.insets = new Insets(0, 5, 5, 5);
			gbc_academia.gridx = 0;
			gbc_academia.gridy = 4;
			add(academia, gbc_academia);
			
			PanelUnidade nobre = new PanelUnidade(getNextColor(), Unidade.NOBRE, academia);
			academia.addPanel(nobre);
			
			academia.addFinish(nobre);
			
		} else {
			
			PanelUnidade nobre = new PanelUnidade(getNextColor(), Unidade.NOBRE, null);
			nobre.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
			GridBagConstraints gbc_nobre = new GridBagConstraints();
			gbc_nobre.anchor = GridBagConstraints.CENTER;
			gbc_nobre.fill = GridBagConstraints.HORIZONTAL;
			gbc_nobre.insets = new Insets(0, 5, 5, 5);
			gbc_nobre.gridx = 0;
			gbc_nobre.gridy = 4;
			add(nobre, gbc_nobre);
		}
		
		
		if (MundoSelecionado.hasPaladino()) {
			PanelUnidade paladino = new PanelUnidade(getNextColor(), Unidade.PALADINO, null);
			paladino.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			GridBagConstraints gbc_paladino = new GridBagConstraints();
			gbc_paladino.anchor = GridBagConstraints.CENTER;
			gbc_paladino.fill = GridBagConstraints.BOTH;
			gbc_paladino.insets = new Insets(0, 5, 5, 5);
			gbc_paladino.gridx = 0;
			gbc_paladino.gridy = 5;
			add(paladino, gbc_paladino);
		}
		
	}
	
	/**
	 * Adiciona a barra com os nomes de cada coluna
	 */
	private void addHeader() {
		
		JPanel header = new JPanel();

		header.setBackground(Cores.FUNDO_ESCURO);
		header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, true));

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 100, 100, 125};
		gbl.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl.columnWeights = new double[]{0.0, 1.0, 0.0};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		header.setLayout(gbl);
		
		GridBagConstraints gbc_header = new GridBagConstraints();
		gbc_header.anchor = GridBagConstraints.CENTER;
		gbc_header.fill = GridBagConstraints.BOTH;
		gbc_header.insets = new Insets(5, 5, 5, 5);
		gbc_header.gridx = 0;
		gbc_header.gridy = 0;
		add(header, gbc_header);
		
		JLabel lblUnidades = new JLabel("Unidade");
		GridBagConstraints gbc_lblUnidades = new GridBagConstraints();
		gbc_lblUnidades.insets = new Insets(5, 5, 5, 5);
		gbc_lblUnidades.gridx = 0;
		gbc_lblUnidades.gridy = 0;
		header.add(lblUnidades, gbc_lblUnidades);
		
		JLabel lblQuantidade = new JLabel("Quantidade");
		GridBagConstraints gbc_lblQuantidade = new GridBagConstraints();
		gbc_lblQuantidade.insets = new Insets(5, 0, 5, 5);
		gbc_lblQuantidade.gridx = 1;
		gbc_lblQuantidade.gridy = 0;
		header.add(lblQuantidade, gbc_lblQuantidade);
		
		JLabel lblTempoPorUnidade = new JLabel("Tempo por unidade");
		GridBagConstraints gbc_lblTempoPorUnidade = new GridBagConstraints();
		gbc_lblTempoPorUnidade.insets = new Insets(5, 0, 5, 5);
		gbc_lblTempoPorUnidade.gridx = 2;
		gbc_lblTempoPorUnidade.gridy = 0;
		header.add(lblTempoPorUnidade, gbc_lblTempoPorUnidade);
		
		JLabel lblTempoTotal = new JLabel("Tempo total");
		GridBagConstraints gbc_lblTempoTotal = new GridBagConstraints();
		gbc_lblTempoTotal.insets = new Insets(5, 0, 5, 0);
		gbc_lblTempoTotal.gridx = 3;
		gbc_lblTempoTotal.gridy = 0;
		header.add(lblTempoTotal, gbc_lblTempoTotal);
		
	}
	
}
