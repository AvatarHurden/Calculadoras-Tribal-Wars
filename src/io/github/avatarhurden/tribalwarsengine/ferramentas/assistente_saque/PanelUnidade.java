package io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Lang;
import database.Cores;
import database.Unidade;

/**
 * Panel para inserção das unidades que serão enviadas no ataque.
 * <br>Também é usado para mostrar as unidades máximas que devem ser enviadas,
 * quando aplicável.
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public abstract class PanelUnidade extends JPanel{
	
	// Map with the textFields associated with units
	private Map<Unidade,IntegerFormattedTextField> textFields = new HashMap<Unidade,IntegerFormattedTextField>();
	
	// Header panels
	private JPanel headerPanel, unidadePanel, quantidadePanel;
	
	protected PanelUnidade() {
		
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
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		c.gridx = 0;
		
		// Adding "Tropas Enviadas" header
		
		headerPanel = new JPanel();
		headerPanel.add(new JLabel("Unidades Enviadas"));
		
		headerPanel.setBackground(Cores.FUNDO_ESCURO);
		headerPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 3, 0);
		add(headerPanel, c);
		
		// Adding "Unidades" panel
		
		unidadePanel = new JPanel();
		unidadePanel.add(new JLabel(Lang.Unidade.toString()));
		
		unidadePanel.setBackground(Cores.FUNDO_ESCURO);
		unidadePanel.setBorder(new MatteBorder(1, 1, 1, 0,Cores.SEPARAR_ESCURO));
			
		c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(unidadePanel, c);
		
		// Adding "Quantidades" panel
		
		quantidadePanel = new JPanel();
		quantidadePanel.add(new JLabel(Lang.Quantidade.toString()));
		
		quantidadePanel.setBackground(Cores.FUNDO_ESCURO);
		quantidadePanel.setBorder(new MatteBorder(1, 0, 1, 1,Cores.SEPARAR_ESCURO));
	
		c.gridx++;
		add(quantidadePanel, c);
	
		/** Variável para controlar a cor do unitPanel*/
		int cor = -1;
		
		for (Unidade i : WorldManager.get().getAvailableUnits()) {
			
			if (i != null && !i.equals(Unidade.MILÍCIA)) {
				
				// Adds the unit name
				JPanel unitName = new JPanel(new FlowLayout(FlowLayout.LEFT));
				unitName.add(new JLabel(i.toString()));
				
				unitName.setBackground(Cores.getAlternar(cor));
				unitName.setBorder(new MatteBorder(0, 1, 0, 0,Cores.SEPARAR_ESCURO));
				
				c.gridy++;
				c.gridx = 0;
				add(unitName, c);
				
				// Adds the IntegerFormattedTextField
				JPanel unitQuantity = new JPanel();
				
				textFields.put(i, new IntegerFormattedTextField() {
					public void go() {
						doAction();
					}
				});
				unitQuantity.add(textFields.get(i));
				
				unitQuantity.setBackground(Cores.getAlternar(cor));
				unitQuantity.setBorder(
						new MatteBorder(0, 0, 0, 1,Cores.SEPARAR_ESCURO));
				
				c.gridx++;
				add(unitQuantity, c);
				
				cor++;
			}
			
		} // ends for loop
		
	}
	
	protected Map<Unidade, IntegerFormattedTextField> getTextFields() {
		return textFields;
	}
	
	protected void resetTextFields() {
		
		for (IntegerFormattedTextField t : textFields.values())
			t.setText("");
		
	}
	
	/**
	 * Method that is called whenever any component is edited
	 */
	protected abstract void doAction();
	
}
