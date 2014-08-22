package io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.Cálculo.NoIntervalException;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.Cálculo.SameDateException;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.Army;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.EditPanelCreator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class AssistenteSaquePanel extends Ferramenta{

	// Objeto de cálculo
	private Cálculo cálculo;
	
	// Panels de inserção de dados
	private PanelUnidade panelUnidades;
	private PanelIntervalo panelIntervalo;
	private PanelHorário panelHorário;
	
	// Panels para mostrar as respostas
	private JPanel panelRecomendado;
	
	private Map<Unidade, JLabel> mapRecomendado = new HashMap<Unidade, JLabel>();
	
	private Army army;
	
	public AssistenteSaquePanel () {
		
		super("Assistente de Saque");
		
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
		
		cálculo = new Cálculo();
		
		panelUnidades = new PanelUnidade() {
			protected void doAction() {
				editIntervalObject();
				editHorárioObject();
			}
		};
		panelIntervalo = new PanelIntervalo() {
			protected void doAction() {
				editIntervalObject();
				editHorárioObject();
			}
		};
		panelHorário = new PanelHorário() {
			protected void doAction() {
				editIntervalObject();
				editHorárioObject();
			}
		};
		
		army = new Army();
		
		makePanelRecomendado();
		panelRecomendado.setVisible(false);
		
		// Add reset button
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		// Add troop model button
		c.anchor = GridBagConstraints.CENTER;
		c.gridx++;
		add(tools.addModelosTropasPanel(true, panelUnidades.getTextFields()));
		
		// Add modeloAldeia for interval
		c.gridx += 2; // empty space for recommendations
		add(tools.addModelosAldeiasPanel(true, panelIntervalo.getEdifícios()), c);
		
		// Add modeloAldeia for time
		c.gridx += 2;
		add(tools.addModelosAldeiasPanel(false, null), c);
		
		// Add units panel
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.NORTH;
		add(army.new ArmyEditPanel(null, true, true, true, false, false),c);
		
		// Add recomended units panel
		JPanel container = new JPanel(new GridLayout(1,0));
		container.setPreferredSize(panelRecomendado.getPreferredSize());
		container.setOpaque(false);
		container.add(panelRecomendado);
		
		c.gridx += 2;
		c.gridwidth = 1;
		add(container, c);
		
		// Add the panel that receives input for interval calculations 
		c.gridy++;
		c.gridx++;
		add(panelIntervalo, c);
		
//		// Adds the panel that displays the interval in which to attack
//		c.gridy++;
//		add(respostaIntervalo, c);
		
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
		
		// Adds the panel that displays the time to send the attack 
		c.gridy++;
		
		JButton button = new JButton("Go");
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				cálculo.setProduçãoEArmazenamento(panelIntervalo.getEdifícios());
				cálculo.setSaqueTotal(panelUnidades.getTextFields());
				cálculo.setDistância(panelHorário.getCoordenadaOrigem(), panelIntervalo.getCoordenadaDestino());
				cálculo.setRestantes(panelHorário.getRecursosRestantes());
				cálculo.setUltimoAtaque(panelHorário.getDataEnviada());
				
				cálculo.setIntervalo();
				cálculo.setEnviarAtaque();
				try {
					panelIntervalo.setDisplayIntervalo(cálculo.getIntervalo());
				} catch (NoIntervalException exc) {
					panelIntervalo.setErrorMessage(exc.getMessage());
				}
				try {
					panelHorário.setDisplayHorario(cálculo.getHorario());
				} catch (SameDateException exc) {
					panelHorário.setErrorMessage(exc.getMessage());
				}
				setRecomendadoPanel(cálculo.getUnidadesRecomendadas());
			}
		});
		
	//	add(button, c);
	}
	
	private ActionListener getResetButtonAction() {
		
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				panelUnidades.resetTextFields();
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
		
		for (Unidade i : WorldManager.get().getAvailableUnits()) {
			
			if (i != null && !i.equals(Unidade.MILÍCIA)) {
				
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
			
		} // ends for loop		
	}
	
	private JButton makeUseRecomenadoButton() {
		
		JButton button = new TWSimpleButton("Usar");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Map<Unidade, BigDecimal> recommended = cálculo.getUnidadesRecomendadas();
				
				for (Entry<Unidade, IntegerFormattedTextField> entry : 
							panelUnidades.getTextFields().entrySet()) {
					
					// Muda caso o novo valor seja diferente de 0
					if (!recommended.get(entry.getKey()).equals(BigDecimal.ZERO))
						entry.getValue().setText(recommended.get(entry.getKey()).toString());
					// Caso o novo seja zero, mas o anterior seja diferente, limpa o textField
					else if (!recommended.get(entry.getKey()).equals(entry.getValue().getValue()))
						entry.getValue().setText("");
						
					
				}
				
				panelRecomendado.setVisible(false);
				
				repaint();
			}
		});
		
		return button;
	}
	
	protected void setRecomendadoPanel(Map<Unidade, BigDecimal> map) {
		
		if (map != null) {
			panelRecomendado.setVisible(true);
			for (Unidade i : map.keySet())
				mapRecomendado.get(i).setText(map.get(i).toString());	
	
		} else {
			panelRecomendado.setVisible(false);
			for (Unidade i : mapRecomendado.keySet())
				mapRecomendado.get(i).setText(" ");
		}
		
	}

	private void editIntervalObject() {
		
		cálculo.setProduçãoEArmazenamento(panelIntervalo.getEdifícios());
		cálculo.setSaqueTotal(panelUnidades.getTextFields());
		
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
