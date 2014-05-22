package assistente_saque;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import config.Lang;
import custom_components.CoordenadaPanel;
import custom_components.EdifícioFormattedComboBox;
import custom_components.TimeFormattedJLabel;
import database.Cores;
import database.Edifício;

/**
 * Panel para inserção e output dos dados relativo ao cálculo do intervalo de tempo
 * entre ataques. Basicamente, dados relacionados à aldeia de origem
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class PanelIntervalo extends JPanel{

	private CoordenadaPanel coordenadas;
	private JPanel edifífcioPanel, nívelPanel;
	private TimeFormattedJLabel respostaLabel;
	
	private Map<Edifício, EdifícioFormattedComboBox> edificios = 
			new HashMap<Edifício, EdifícioFormattedComboBox>();
	
	protected PanelIntervalo() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		setOpaque(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 0;
		c.gridx = 0;
		
		// Adds the village coordinates
		coordenadas = new CoordenadaPanel("Aldeia de Destino");
		
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 10, 0);
		add(coordenadas, c);
		
		// Adding "Unidades" panel
		
		edifífcioPanel= new JPanel();
		edifífcioPanel.add(new JLabel(Lang.Edificio.toString()));
	
		edifífcioPanel.setBackground(Cores.FUNDO_ESCURO);
		edifífcioPanel.setBorder(new MatteBorder(1, 1, 1, 0,Cores.SEPARAR_ESCURO));
				
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(edifífcioPanel, c);
				
		// Adding "Quantidades" panel
			
		nívelPanel = new JPanel();
		nívelPanel.add(new JLabel(Lang.Nivel.toString()));
			
		nívelPanel.setBackground(Cores.FUNDO_ESCURO);
		nívelPanel.setBorder(new MatteBorder(1, 0, 1, 1,Cores.SEPARAR_ESCURO));
		
		c.gridx++;
		add(nívelPanel, c);
			
		// Adding the buildings
		c.gridy++;
		addBuildingPanel(Edifício.BOSQUE, c);
		
		c.gridy++;
		addBuildingPanel(Edifício.POÇO_DE_ARGILA, c);
		
		c.gridy++;
		addBuildingPanel(Edifício.MINA_DE_FERRO, c);
		
		c.gridy++;
		addBuildingPanel(Edifício.ARMAZÉM, c);
		
		c.gridy++;
		addBuildingPanel(Edifício.ESCONDERIJO, c);
		
		// Adds the panel with the interval
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.insets = new Insets(25, 5, 5, 5);
		add(makePanelResposta(), c);
	}
	
	// Cria um painel para o edifício dado
	private void addBuildingPanel(Edifício ed, GridBagConstraints c) {
		
		// Adds the unit name
		JPanel buildingName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buildingName.add(new JLabel(ed.toString()));
		
		// Uses the height to determine the color
		buildingName.setBackground(Cores.getAlternar(c.gridy));
		if (ed.equals(Edifício.ESCONDERIJO))
			buildingName.setBorder(new MatteBorder(0, 1, 1, 0,Cores.SEPARAR_ESCURO));
		else
			buildingName.setBorder(new MatteBorder(0, 1, 0, 0,Cores.SEPARAR_ESCURO));

		c.gridx = 0;
		add(buildingName, c);
				
		// Adds the IntegerFormattedTextField
		JPanel buildingLevel = new JPanel();
				
		edificios.put(ed, new EdifícioFormattedComboBox(ed, 0) {
			public void go() {}
		});
		buildingLevel.add(edificios.get(ed));
				
		buildingLevel.setBackground(Cores.getAlternar(c.gridy));
		if (ed.equals(Edifício.ESCONDERIJO))
			buildingLevel.setBorder(
					new MatteBorder(0, 0, 1, 1,Cores.SEPARAR_ESCURO));
		else
			buildingLevel.setBorder(
				new MatteBorder(0, 0, 0, 1,Cores.SEPARAR_ESCURO));
		
				
		c.gridx++;
		add(buildingLevel, c);
		
	}
	
	private JPanel makePanelResposta() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setOpaque(false);
		
		// Add panel de último ataque
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("Enviar ataques a cada"));
			
		ataquePanel.setBackground(Cores.FUNDO_ESCURO);
		ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		panel.add(ataquePanel);
				
		// Add panel de horário
		
		respostaLabel = new TimeFormattedJLabel(false);
		
		JPanel horaPanel = new JPanel();
		horaPanel.add(respostaLabel);
						
		horaPanel.setBackground(Cores.ALTERNAR_ESCURO);
		horaPanel.setBorder(new MatteBorder(0, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		panel.add(horaPanel);
		
		return panel;
	}
	
	protected Map<Edifício, EdifícioFormattedComboBox> getEdifícios() {
		return edificios;
	}
	
	/**
	 * Retorna o panel com as coordenadas da aldeia de destino
	 * @return CoordenadaPanel
	 */
	protected CoordenadaPanel getCoordenadaDestino() {
		return coordenadas;
	}
	
	/**
	 * Sets the panel with a format time (value given in milliseconds)
	 * @param millis
	 */
	protected void setDisplayIntervalo(long millis){
		
		respostaLabel.setTime(millis);
		
	}
	
	protected void resetAll() {
		
		coordenadas.reset();
		
		for (EdifícioFormattedComboBox c : edificios.values())
			c.setText(" ");
		
		respostaLabel.setText("");
		
	}
	
}
