package io.github.avatarhurden.tribalwarsengine.panels;

import io.github.avatarhurden.tribalwarsengine.tools.property_classes.Property;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import database.Cores;
import database.Mundo;

@SuppressWarnings("serial")
public class WorldInfoPanel extends JPanel {

	// Utilizando uma ferramenta apenas para a sua função de "getNextColor"
	private Ferramenta ferramenta_cor;

	private Mundo mundo;

	/**
	 * Basicamente uma tabela com as informações de um mundo específico
	 */
	public WorldInfoPanel() {

		// Only creates the panel to be added later

	}

	/**
	 * Muda as informações da tabela A lista de propriedades inserida não é
	 * exatamente igual à que será mostrada, pois ocorrem adaptações para
	 * facilitar a visualização
	 * 
	 * @param prop
	 *            Propriedades básicas
	 */
	public void changeProperties(Mundo mundo) {

		removeAll();

		this.mundo = mundo;

		ferramenta_cor = new Ferramenta();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 290 };
		gridBagLayout.rowHeights = new int[] {};
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		setOpaque(false);

		setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		addProperties();

	}

	/**
	 * Adds the properties to the panel, in a predefined order
	 */
	private void addProperties() {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		for (Property p : mundo.variableList) {
			
			// Only adds if paladin is active OR the property to be added is not "Itens Aprimorados"
			// TODO maybe add a way to subordinar items a outros items.
			if (!p.getName().equals("Itens Aprimorados") || mundo.hasPaladino() == true) {
				gbc.gridy++;
				add(panelProperty(p), gbc);
			}
			
		}
	
	}
	
	private JPanel panelProperty (Property property) {
		
		JPanel panel = new JPanel();
		
		panel.setBackground(ferramenta_cor.getNextColor());

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 150, 20, 140 };
		gbl_panel.rowHeights = new int[] { 20 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(gbl_panel);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 5, 5, 5);

		JLabel lblName = new JLabel(property.getName());

		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panel.add(lblName, gbc_panel);

		JLabel lblValue = new JLabel(property.getValueName());

		gbc_panel.gridx = 2;
		gbc_panel.gridy = 0;
		panel.add(lblValue, gbc_panel);
		
		return panel;
		
	}

}