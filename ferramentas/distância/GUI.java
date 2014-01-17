package distância;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Mundo_Reader;
import custom_components.Ferramenta;
import database.BigOperation;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta{

	private List<PanelTempoUnidade> unidadesUtilizadas = new ArrayList<PanelTempoUnidade>();
	
	private PanelAldeia aldeiaOrigem, aldeiaDestino;
	
	private BigDecimal distância;
	
	/**
	 * Calcula o tempo de deslocamento entre duas aldeias.
	 * Mostra o tempo de cada unidade.
	 */
	public GUI() {
		
		super("Cálculo de Distância");
		
		setBackground(Cores.FUNDO_CLARO);
		setUnidades();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		aldeiaOrigem = new PanelAldeia("Aldeia de Origem", this);
		
		constraints.insets = new Insets(5, 5, 5, 5);
		add(aldeiaOrigem, constraints);
		
		aldeiaDestino = new PanelAldeia("Aldeia de Destino", this);
		
		constraints.gridx = 1;
		add(aldeiaDestino, constraints);
		
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 0, 5);
		addHeader(constraints);
		
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(unitePanels(), constraints);
		
	}
	
	/**
	 * Define as unidades que estão presentes no mundo
	 */
	private void setUnidades() {
		
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.LANCEIRO));
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.ESPADACHIM));
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.ARQUEIRO));

		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.BÁRBARO));
		
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.EXPLORADOR));
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.CAVALOLEVE));
		
		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
			unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.ARCOCAVALO));
		
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.CAVALOPESADO));
		
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.ARÍETE));
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.CATAPULTA));
		
		if (Mundo_Reader.MundoSelecionado.hasPaladino())
			unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.PALADINO));
		
		unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), Unidade.NOBRE));
		
	}
	
	protected void calculateDistanceAndTimes() {
		
		if (aldeiaOrigem.hasCompleteCoordinates() && aldeiaDestino.hasCompleteCoordinates()) {
			
			int diferençaX = aldeiaOrigem.getCoordenadaX()-aldeiaDestino.getCoordenadaX();
			int diferençaY = aldeiaOrigem.getCoordenadaY()-aldeiaDestino.getCoordenadaY();
			
			BigDecimal xSquared = new BigDecimal(String.valueOf(diferençaX)).pow(2);
			BigDecimal ySquared = new BigDecimal(String.valueOf(diferençaY)).pow(2);
			
			distância = BigOperation.sqrt(xSquared.add(ySquared),30);
			
			for (PanelTempoUnidade i : unidadesUtilizadas)
				i.setTempo(distância);
			
		} else 
			for (PanelTempoUnidade i : unidadesUtilizadas)
				i.setTempo(null);
		
	}

	private void addHeader(GridBagConstraints c) {
		
		c.gridy = 1;
		add(new PanelTempoUnidade(),c);

	}
	
	/**
	 * Junta os panels de todas as unidades num único panel
	 * 
	 * @param String qual panel pegar
	 */
	private JPanel unitePanels() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1,false));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		
		for (PanelTempoUnidade i : unidadesUtilizadas) {
			panel.add(i, gbc);
			gbc.gridy++;
		}
		
		return panel;
		
	}
	
}
