package assistente_saque;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Mundo_Reader;
import custom_components.Ferramenta;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta{

	// Panels de inserção de dados
	private PanelUnidade panelUnidades;
	private PanelIntervalo panelIntervalo;
	private PanelHorário panelHorário;
	
	// Panels para mostrar as respostas
	private JPanel panelRecomendado;
	private JPanel respostaIntervalo;
	private JPanel respostaHorário;
	
	private Map<Unidade, JLabel> mapRecomendado = new HashMap<Unidade, JLabel>();
	
	public GUI () {
		
		super("Assistente de Saque");
		
		// TODO make this default to ferramentas
		setBackground(Cores.FUNDO_CLARO);
		
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
		
		panelUnidades = new PanelUnidade();
		panelIntervalo = new PanelIntervalo();
		panelHorário = new PanelHorário();
		
		makePanelRecomendado();
		
		// Add reset button
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		// Add troop model button
		c.anchor = GridBagConstraints.CENTER;
		c.gridx++;
		add(tools.addModelosPanel(true, panelUnidades.getTextFields()));
		
		// Add units panel
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 2;
		add(panelUnidades,c);
		
		// Add recomended units panel
		c.gridx += 2;
		c.gridwidth = 1;
		add(panelRecomendado, c);
		
		// Where the "AldeiaModelos" panel will be added
		c.gridy = 0;
		c.gridx++;
		c.gridheight = 1;
		c.gridwidth = 2;
		add(tools.addModelosPanel(true, null), c);
		
		// Add the panel that receives input for interval calculations 
		c.gridy++;
//		c.gridx--;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(panelIntervalo, c);
		
		// Adds the panel that displays the interval in which to attack
		c.gridy++;
//		add(respostaIntervalo, c);
		
		// Where the "AldeiaModelos" panel will be added
		c.gridy = 0;
		c.gridx += 2;
		c.gridwidth = 3;
		add(tools.addModelosPanel(true, null), c);
		
		// Add the panel that receives input for exact time calculations 
		c.gridy = 1;
//		c.gridx--;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.NORTH;
		add(panelHorário, c);
		
		// Adds the panel that displays the time to send the attack 
		c.gridy++;
//		add(respostaHorário, c);
	}
	
	private ActionListener getResetButtonAction() {
		
		return null;
		
	}
	
	private void makePanelRecomendado() {
		
		// TODO add a way for users to get help on what this is
		
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
		headerPanel.add(new JLabel("Recomendado"));
		
		c.insets = new Insets(0, 0, 3, 0);
		panelRecomendado.add(headerPanel, c);
		
		// Adding button panel
		
		// GridBagLayout to allow for the whol button to be displayed
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.add(new JButton("Usar"));
		
		// To make this the right size
		buttonPanel.setPreferredSize(new Dimension(
				buttonPanel.getPreferredSize().width, 28));
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		panelRecomendado.add(buttonPanel, c);
		
		/** Variável para controlar a cor do unitPanel*/
//		int cor = -1;
		
		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {
			
			if (i != null && !i.equals(Unidade.MILÍCIA)) {
				
				// Adds the JLabel
				JPanel unitQuantity = new JPanel();
				unitQuantity.setOpaque(false);
				
				unitQuantity.setPreferredSize(new Dimension(0, 30));
				
				mapRecomendado.put(i, new JLabel(" "));
				unitQuantity.add(mapRecomendado.get(i));
				
				// Changes the font color to gray
				mapRecomendado.get(i).setForeground(Color.gray);
				
				c.gridy++;
				panelRecomendado.add(unitQuantity, c);
				
//				cor++;
			}
			
		} // ends for loop		
	}
	
}
