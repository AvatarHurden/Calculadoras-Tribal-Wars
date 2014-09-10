package io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.Cálculo.NoIntervalException;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.Cálculo.SameDateException;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class AssistenteSaquePanel extends Ferramenta{

	// Objeto de cálculo
	private Cálculo cálculo;
	
	// Panels de inserção de dados
	private PanelIntervalo panelIntervalo;
	private PanelHorário panelHorário;
	
	// Panels para mostrar as respostas
	private JPanel panelRecomendado;
	
	private Map<Unit, JLabel> mapRecomendado = new HashMap<Unit, JLabel>();
	
	private Army army;
	private ArmyEditPanel armyEdit;
	private OnChange onChange;
	
	public AssistenteSaquePanel () {
		
		super("Assistente de Saque");
		
		onChange = new OnChange() {
			public void run() {
				armyEdit.saveValues();
				panelIntervalo.getBuildingsEdit().saveValues();
				editIntervalObject();
				editHorárioObject();
			}
		};
		
		cálculo = new Cálculo();
		
		panelIntervalo = new PanelIntervalo(onChange);
		
		panelHorário = new PanelHorário(onChange);
		
		army = new Army(Army.getAttackingUnits());
		armyEdit = army.getEditPanelNoLevels(onChange, 30);
				
		makePanelRecomendado();
		panelRecomendado.setVisible(false);
		
	}
	
	protected void makeGUI() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		// Add reset button
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		// Add troop model button
		c.anchor = GridBagConstraints.CENTER;
		c.gridx++;
		add(tools.addModelosTropasPanel(true, armyEdit));
		
		// Add modeloAldeia for interval
		c.gridx += 2; // empty space for recommendations
		add(tools.addModelosAldeiasPanel(true, panelIntervalo.getBuildingsEdit()), c);
		
		// Add modeloAldeia for time
		c.gridx += 2;
		add(tools.addModelosAldeiasPanel(false, null), c);
		
		// Add units panel
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.NORTH;
		add(armyEdit, c);
		
		// Add recomended units panel
		JPanel container = new JPanel(new GridLayout(1,0));
		container.setPreferredSize(panelRecomendado.getPreferredSize());
		container.setOpaque(false);
		container.add(panelRecomendado);
		
		c.gridx += 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.insets = new Insets(25, 5, 5, 5);
		add(container, c);
		
		// Add the panel that receives input for interval calculations 
		c.gridy++;
		c.gridx++;
		c.gridheight = 1;
		c.insets = new Insets(5, 5, 5, 5);
		add(panelIntervalo, c);
		
		// Separar as duas funções
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setForeground(Cores.SEPARAR_ESCURO);
		
		c.gridx++;
		c.gridy = 1;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 30, 0, 30);
		add(separator, c);
			
		// Add the panel that receives input for exact time calculations 
		c.gridy = 1;
		c.gridx++;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		add(panelHorário, c);
		
	}
	
	private ActionListener getResetButtonAction() {
		
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				armyEdit.resetComponents();
				panelIntervalo.resetAll();
				panelHorário.resetAll();
				
			}
		};
		
	}
	
	private void makePanelRecomendado() {
		
		panelRecomendado = new JPanel(new GridBagLayout());
		panelRecomendado.setOpaque(false);
		panelRecomendado.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridy = 0;
		c.gridx = 0;
		
		// Adding "Recomendado" panel
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.add(new JLabel("<html>Recomendado<sup>?<sup></html>"));
		headerPanel.setToolTipText("<html>As unidades enviadas ultrapassam a capacidade de armazenamento da aldeia."
				+ "<br>A recomendação é baseada nas tropas disponíveis, utilizando as mais eficientes antes.</html>");
		
		c.insets = new Insets(0, 0, 3, 0);
		panelRecomendado.add(headerPanel, c);
		
		// Adding button panel
		
		// GridBagLayout to allow for the whole button to be displayed
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.add(makeUseRecomenadoButton());
		
		// To make this the right size
		buttonPanel.setPreferredSize(new Dimension(
				buttonPanel.getPreferredSize().width, 28));
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		panelRecomendado.add(buttonPanel, c);
		
		for (Unit i : army.getUnits()) {
			
			// Adds the JLabel
			JPanel unitQuantity = new JPanel();
			unitQuantity.setOpaque(false);
			
			unitQuantity.setPreferredSize(new Dimension(panelRecomendado.getPreferredSize().width, 30));
			
			mapRecomendado.put(i, new JLabel(" "));
			unitQuantity.add(mapRecomendado.get(i));
			
			// Changes the font color to gray
			mapRecomendado.get(i).setForeground(Color.gray);
			
			c.gridy++;
			panelRecomendado.add(unitQuantity, c);
			
		} 
	}
	
	private JButton makeUseRecomenadoButton() {
		
		JButton button = new TWSimpleButton("Usar");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				armyEdit.setValues(cálculo.getUnidadesRecomendadas());
				
				panelRecomendado.setVisible(false);
				
				repaint();
			}
		});
		
		return button;
	}
	
	protected void setRecomendadoPanel(Army recommendedArmy) {
		
		if (recommendedArmy != null) {
			panelRecomendado.setVisible(true);
			for (Troop t : recommendedArmy.getTropas())
				mapRecomendado.get(t.getUnit()).setText(String.valueOf(t.getQuantity()));	
	
		} else {
			panelRecomendado.setVisible(false);
			for (Unit i : mapRecomendado.keySet())
				mapRecomendado.get(i).setText(" ");
		}
		
	}

	private void editIntervalObject() {
		
		cálculo.setProduçãoEArmazenamento(panelIntervalo.getBuildings());
		cálculo.setArmy(army);
		cálculo.setIntervalo();
		try {
			panelIntervalo.setDisplayIntervalo(cálculo.getIntervalo());
		} catch (NoIntervalException exc) {
			panelIntervalo.setErrorMessage(exc.getMessage());
		}
		
		setRecomendadoPanel(cálculo.getUnidadesRecomendadas());
		
	}
	
	private void editHorárioObject() {
		
		cálculo.setDistância(panelHorário.getCoordenadaOrigem(), panelIntervalo.getCoordenadaDestino());
		cálculo.setRestantes(panelHorário.getRecursosRestantes());
		cálculo.setUltimoAtaque(panelHorário.getDataEnviada());
		
		cálculo.setEnviarAtaque();
		try {
			panelHorário.setDisplayHorario(cálculo.getHorario());
		} catch (SameDateException exc) {
			panelHorário.setErrorMessage(exc.getMessage());
		}
		
	}
}
