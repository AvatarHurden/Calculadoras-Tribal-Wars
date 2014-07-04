package distância;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import config.Lang;
import custom_components.CoordenadaPanel;
import custom_components.Ferramenta;
import custom_components.TimeFormattedJLabel;
import database.BigOperation;
import database.Cores;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {

	private CoordenadaPanel aldeiaOrigem, aldeiaDestino;
	
	private PanelUnidade panelUnidade;
	
	private PanelPlanejador panelPlanejador;
	
	private long time;
	
	private TimeFormattedJLabel interval;

	/**
	 * Calcula o tempo de deslocamento entre duas aldeias. Mostra o tempo de
	 * cada unidade.
	 */
	public GUI() {

		super(Lang.FerramentaDistancia.toString());

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridy = 0;
		constraints.gridx = 0;
		
		aldeiaDestino = new CoordenadaPanel(Lang.AldeiaDestino.toString()) {
			public void go() {
				calculateDistanceAndTimes();
			}
		};
		
		aldeiaOrigem = new CoordenadaPanel(Lang.AldeiaOrigem.toString()) {
			public void go() {
				calculateDistanceAndTimes();
			}
		};
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				aldeiaOrigem.reset();
				aldeiaDestino.reset();
				
			}
		};

		constraints.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(action), constraints);
		
		constraints.gridx++;
		add(tools.addModelosAldeiasPanel(true, null, aldeiaOrigem));
		
		constraints.gridx++;
		add(tools.addModelosAldeiasPanel(false, null, aldeiaDestino));
		
		constraints.gridy++;
		constraints.gridx = 0;
		constraints.gridheight = 3;
		constraints.insets = new Insets(5, 5, 5, 25);
		add(panelUnidade = new PanelUnidade(this), constraints);
		
		// Adiciona aldeia de origem
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx++;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(aldeiaOrigem, constraints);
		
		// Adiciona aldeia de destino
		constraints.gridx++;
		add(aldeiaDestino, constraints);
		
		constraints.gridx--;
		constraints.gridy++;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		//Foi a única maneira que achei pra fazer funcionar
		constraints.insets = new Insets(40, 5, 5, 5);
		add(createTimePanel(), constraints);
		
		constraints.gridy++;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(panelPlanejador = new PanelPlanejador(this), constraints);
		
	}

	protected void calculateDistanceAndTimes() {

			int diferençaX = aldeiaOrigem.getCoordenadaX()
					- aldeiaDestino.getCoordenadaX();
			int diferençaY = aldeiaOrigem.getCoordenadaY()
					- aldeiaDestino.getCoordenadaY();

			BigDecimal xSquared = new BigDecimal(String.valueOf(diferençaX))
					.pow(2);
			BigDecimal ySquared = new BigDecimal(String.valueOf(diferençaY))
					.pow(2);

			BigDecimal distância = BigOperation.sqrt(xSquared.add(ySquared), 30);
			
			BigDecimal tempo = distância.multiply(panelUnidade.getSlowestSelected().velocidade());
			tempo = tempo.multiply(new BigDecimal("60000"));
			
			time = tempo.longValue();
			
			interval.setTime(time);
			
			panelPlanejador.changeDate(time);
			
	}
	
	private JPanel createTimePanel() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setOpaque(false);
		
		// Add panel de último ataque
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("Tempo de Deslocamento"));
			
		ataquePanel.setBackground(Cores.FUNDO_ESCURO);
		ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		panel.add(ataquePanel);
				
		// Add panel de horário
		
		interval = new TimeFormattedJLabel(true);
		
		JPanel horaPanel = new JPanel();
		horaPanel.add(interval);
						
		horaPanel.setBackground(Cores.ALTERNAR_ESCURO);
		horaPanel.setBorder(new MatteBorder(0, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		panel.add(horaPanel);
		
		return panel;

		
	}
	
	protected long getTime() {
		return time;
	}

}
