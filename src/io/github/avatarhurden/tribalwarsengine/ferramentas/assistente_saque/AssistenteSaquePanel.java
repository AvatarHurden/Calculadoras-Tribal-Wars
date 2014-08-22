package io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.C�lculo.NoIntervalException;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.C�lculo.SameDateException;
import io.github.avatarhurden.tribalwarsengine.objects.Army;
import io.github.avatarhurden.tribalwarsengine.objects.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.objects.Army.Tropa;
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

import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class AssistenteSaquePanel extends Ferramenta{

	// Objeto de c�lculo
	private C�lculo c�lculo;
	
	// Panels de inser��o de dados
	private PanelIntervalo panelIntervalo;
	private PanelHor�rio panelHor�rio;
	
	// Panels para mostrar as respostas
	private JPanel panelRecomendado;
	
	private Map<Unidade, JLabel> mapRecomendado = new HashMap<Unidade, JLabel>();
	
	private Army army;
	private ArmyEditPanel armyEdit;
	private OnChange onChange;
	
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
		
		onChange = new OnChange() {
			public void run() {
				armyEdit.saveValues();
				editIntervalObject();
				editHor�rioObject();
			}
		};
		
		c�lculo = new C�lculo();
		
//		panelUnidades = new PanelUnidade() {
//			protected void doAction() {
//				editIntervalObject();
//				editHor�rioObject();
//			}
//		};
		panelIntervalo = new PanelIntervalo() {
			protected void doAction() {
				editIntervalObject();
				editHor�rioObject();
			}
		};
		panelHor�rio = new PanelHor�rio() {
			protected void doAction() {
				editIntervalObject();
				editHor�rioObject();
			}
		};
		
		army = new Army(Army.getAttackingUnits());
		armyEdit = army.new ArmyEditPanel(onChange, true, true, true, false, false);
				
		makePanelRecomendado();
		panelRecomendado.setVisible(false);
		
		// Add reset button
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		// Add troop model button
		c.anchor = GridBagConstraints.CENTER;
		c.gridx++;
		add(tools.addModelosTropasPanel(true, armyEdit));
		
		// Add modeloAldeia for interval
		c.gridx += 2; // empty space for recommendations
		add(tools.addModelosAldeiasPanel(true, panelIntervalo.getEdif�cios()), c);
		
		// Add modeloAldeia for time
		c.gridx += 2;
		add(tools.addModelosAldeiasPanel(false, null), c);
		
		// Add units panel
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.NORTH;
		add(armyEdit, c);
		
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
		
		// Separar as duas fun��es
		
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
		add(panelHor�rio, c);
		
	}
	
	private ActionListener getResetButtonAction() {
		
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				armyEdit.resetComponents();
				panelIntervalo.resetAll();
				panelHor�rio.resetAll();
				
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
				+ "<br>A recomenda��o � baseada nas tropas dispon�veis, utilizando as mais eficientes antes.</html>");
		
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
		
		for (Tropa i : army.getTropas()) {
			
			// Adds the JLabel
			JPanel unitQuantity = new JPanel();
			unitQuantity.setOpaque(false);
			
			unitQuantity.setPreferredSize(new Dimension(panelRecomendado.getPreferredSize().width, 30));
			
			mapRecomendado.put(i.getUnidade(), new JLabel(" "));
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
				
				armyEdit.setValues(c�lculo.getUnidadesRecomendadas());
				
				panelRecomendado.setVisible(false);
				
				repaint();
			}
		});
		
		return button;
	}
	
	protected void setRecomendadoPanel(Army army) {
		
		if (army != null) {
			panelRecomendado.setVisible(true);
			for (Tropa t : army.getTropas())
				mapRecomendado.get(t.getUnidade()).setText(String.valueOf(t.getQuantidade()));	
	
		} else {
			panelRecomendado.setVisible(false);
			for (Unidade i : mapRecomendado.keySet())
				mapRecomendado.get(i).setText(" ");
		}
		
	}

	private void editIntervalObject() {
		
		c�lculo.setProdu��oEArmazenamento(panelIntervalo.getEdif�cios());
		c�lculo.setArmy(army);
		
		c�lculo.setIntervalo();
		try {
			panelIntervalo.setDisplayIntervalo(c�lculo.getIntervalo());
		} catch (NoIntervalException exc) {
			panelIntervalo.setErrorMessage(exc.getMessage());
		}
		
		setRecomendadoPanel(c�lculo.getUnidadesRecomendadas());
		
	}
	
	private void editHor�rioObject() {
		
		c�lculo.setDist�ncia(panelHor�rio.getCoordenadaOrigem(), panelIntervalo.getCoordenadaDestino());
		c�lculo.setRestantes(panelHor�rio.getRecursosRestantes());
		c�lculo.setUltimoAtaque(panelHor�rio.getDataEnviada());
		
		c�lculo.setEnviarAtaque();
		try {
			panelHor�rio.setDisplayHorario(c�lculo.getHorario());
		} catch (SameDateException exc) {
			panelHor�rio.setErrorMessage(exc.getMessage());
		}
		
	}
}
