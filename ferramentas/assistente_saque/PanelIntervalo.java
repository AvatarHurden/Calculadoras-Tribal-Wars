package assistente_saque;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import config.Lang;
import custom_components.CoordenadaPanel;
import custom_components.EdifícioFormattedComboBox;
import database.Cores;
import database.Edifício;

/**
 * Panel para inserção dos dados relativo ao cálculo do intervalo de tempo
 * entre ataques. Basicamente, dados relacionados à aldeia de origem
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class PanelIntervalo extends JPanel{

	private CoordenadaPanel coordenadas;
	private JPanel edifífcioPanel, nívelPanel;
	
	private Map<Edifício, EdifícioFormattedComboBox> edificios = 
			new HashMap<Edifício, EdifícioFormattedComboBox>();
	
	protected PanelIntervalo() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		// Border to close on the bottom
		setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
		setOpaque(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 0;
		c.gridx = 0;
		
		// Adds the village coordinates
		coordenadas = new CoordenadaPanel("Aldeia de Destino");
		
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 3, 0);
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
	}
	
	// Cria um painel para o edifício dado
	private void addBuildingPanel(Edifício ed, GridBagConstraints c) {
		
		// Adds the unit name
		JPanel buildingName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buildingName.add(new JLabel(ed.toString()));
		
		// Uses the height to determine the color
		buildingName.setBackground(Cores.getAlternar(c.gridy));
		buildingName.setBorder(new MatteBorder(0, 1, 0, 0,Cores.SEPARAR_ESCURO));
				
		c.gridx = 0;
		add(buildingName, c);
				
		// Adds the TroopFormattedTextField
		JPanel buildingLevel = new JPanel();
				
		edificios.put(ed, new EdifícioFormattedComboBox(ed, 0) {
			public void go() {}
		});
		buildingLevel.add(edificios.get(ed));
				
		buildingLevel.setBackground(Cores.getAlternar(c.gridy));
		buildingLevel.setBorder(
				new MatteBorder(0, 0, 0, 1,Cores.SEPARAR_ESCURO));
				
		c.gridx++;
		add(buildingLevel, c);
		
	}
	
	protected Map<Edifício, EdifícioFormattedComboBox> getEdifícios() {
		return edificios;
	}
	
}
