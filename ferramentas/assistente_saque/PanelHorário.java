package assistente_saque;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import custom_components.CoordenadaPanel;
import custom_components.EdifícioFormattedComboBox;
import custom_components.TroopFormattedTextField;
import database.Cores;
import database.Edifício;

/**
 * Panel para inserção dos dados relativos ao cálculo do horário específico
 * de enviada do próximo ataque.
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class PanelHorário extends JPanel{

	private CoordenadaPanel coordenadas;
	
	// TODO make a custom component for times, or find a good existing one
	private JTextField hora; 
	
	// TODO change the name of the textField to reflect what is does
	private TroopFormattedTextField[] recursosRestantes = 
			new TroopFormattedTextField[3];
	
	protected PanelHorário() {
		
		setOpaque(false);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 0;
		c.gridx = 0;
		
		// Add panel de aldeia
		coordenadas = new CoordenadaPanel("Aldeia de Origem");
		c.gridwidth = 3;
		c.insets = new Insets(0, 0, 3, 0);
		add(coordenadas, c);
		
		// Add panel de último ataque
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("<html>Horário de chegada do último ataque</html>"));
	
		ataquePanel.setBackground(Cores.FUNDO_ESCURO);
		ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(ataquePanel, c);
		
		// Add panel de horário
		
		hora = new JTextField(12);
		
		JPanel horaPanel = new JPanel();
		horaPanel.add(hora);
				
		horaPanel.setBackground(Cores.ALTERNAR_ESCURO);
		horaPanel.setBorder(new MatteBorder(0, 1, 1, 1,Cores.SEPARAR_ESCURO));
		
		c.gridy++;
		add(horaPanel, c);
		
		// Add label "Recursos Restantes"
		JPanel recursosPanel = new JPanel();
		recursosPanel.add(new JLabel("Recursos Restantes"));
		
		recursosPanel.setBackground(Cores.FUNDO_ESCURO);
		recursosPanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
		
		c.gridy++;
		c.insets = new Insets(10, 0, 3, 0);
		add(recursosPanel, c);
		
		// Adding the 3 resources
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 1;
		c.gridy++;
		addRecursoPanel(Edifício.POÇO_DE_ARGILA, c);
		
		c.gridx++;
		addRecursoPanel(Edifício.BOSQUE, c);
		
		c.gridx++;
		addRecursoPanel(Edifício.MINA_DE_FERRO, c);
	}
	
	/**
	 * Adds a JPanel with the resource name and a JTextField for the
	 * resource amount
	 * 
	 * @param produtor - O edifício que produz o recurso em questão
	 */
	// Se descobrir um jeito melhor de determinar qual recurso, usar
	private void addRecursoPanel(Edifício produtor, GridBagConstraints c) {
		
		JPanel recursoNome = new JPanel();
		
		recursoNome.setBackground(Cores.FUNDO_ESCURO);
		recursoNome.setBorder(new MatteBorder
					(1, 1, 1, 0, Cores.SEPARAR_ESCURO));
		
		switch(produtor) {
		case POÇO_DE_ARGILA:
			recursoNome.add(new JLabel("Argila"));
			break;
		case BOSQUE:
			recursoNome.add(new JLabel("Madeira"));
			break;
		case MINA_DE_FERRO:
			recursoNome.add(new JLabel("Ferro"));
			// Since iron is the last one, a full border is required
			recursoNome.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			break;
		default:
			break;
		}
		
		add(recursoNome, c);
		
		JPanel recursoQuantidade = new JPanel();
		
		recursoQuantidade.setBorder(
				new MatteBorder(0, 1, 1, 0,Cores.SEPARAR_ESCURO));
		
		switch(produtor) {
		case POÇO_DE_ARGILA:
			recursosRestantes[0] = new TroopFormattedTextField(7) {
				public void go() {}
			};
			recursoQuantidade.add(recursosRestantes[0]);
			break;
		case BOSQUE:
			recursosRestantes[1] = new TroopFormattedTextField(7) {
				public void go() {}
			};
			recursoQuantidade.add(recursosRestantes[1]);
			break;
		case MINA_DE_FERRO:
			recursosRestantes[1] = new TroopFormattedTextField(7) {
				public void go() {}
			};
			recursoQuantidade.add(recursosRestantes[1]);
			// Since iron is the last one, a full border is required
			recursoQuantidade.setBorder(new MatteBorder(
					0, 1, 1, 1, Cores.SEPARAR_ESCURO));
			break;
		default:
			break;
		}
		
		recursoQuantidade.setBackground(Cores.ALTERNAR_ESCURO);

		c.gridy++;
		add(recursoQuantidade, c);
		
		// Puts it up for next use
		c.gridy--;
		
	}
	
}
