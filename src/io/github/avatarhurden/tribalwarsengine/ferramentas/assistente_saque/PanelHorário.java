package io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import config.Lang;

/**
 * Panel para inserção dos dados relativos ao cálculo do horário específico
 * de enviada do próximo ataque.
 * 
 * @author Arthur
 *
 */
public class PanelHorário extends JPanel{

	private CoordenadaPanel coordenadas;
	
	private JSpinner dateSpinner, hourSpinner;
	
	private IntegerFormattedTextField[] recursosRestantes = new IntegerFormattedTextField[3];
	
	private TimeFormattedJLabel respostaLabel;
	private JLabel errorMessage;
	
	private OnChange onChange;
	
	protected PanelHorário(OnChange onChange) {
		this.onChange = onChange;
		
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
		coordenadas = new CoordenadaPanel(Lang.AldeiaOrigem.toString(), onChange);
		c.gridwidth = 3;
		c.insets = new Insets(0, 0, 3, 0);
		add(coordenadas, c);
		
		// Add label de último ataque
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("<html>Horário de chegada do último ataque</html>"));
	
		ataquePanel.setBackground(Cores.FUNDO_ESCURO);
		ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 0, 0, 0);
		c.gridy++;
		add(ataquePanel, c);
		
		// Add panel de horário
		dateSpinner = makeSpinner("dd/MM/yyyy");
		hourSpinner = makeSpinner("HH:mm:ss");
		
		JPanel horaPanel = new JPanel();
		horaPanel.add(dateSpinner);
		horaPanel.add(hourSpinner);
				
		horaPanel.setBackground(Cores.ALTERNAR_ESCURO);
		horaPanel.setBorder(new MatteBorder(0, 1, 1, 1,Cores.SEPARAR_ESCURO));
		
		c.gridy++;
		c.insets = new Insets(0, 0, 0, 0);
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
		addRecursoPanel("stone", c);
		
		c.gridx++;
		addRecursoPanel("wood", c);
		
		c.gridx++;
		addRecursoPanel("iron", c);
		
		// Adding respostaPanel
		c.gridy += 2;
		c.gridx = 0;
		c.gridwidth = 3;
		c.insets = new Insets(25, 0, 0, 0);
		add(makeRespostaPanel(), c);
	
		// Adding error message
		
		errorMessage = new JLabel(" ");
		errorMessage.setForeground(Color.RED);
		errorMessage.setAlignmentX(CENTER_ALIGNMENT);
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 0, 0, 0);
		add(errorMessage, c);
		
	}
	
	/**
	 * Adds a JPanel with the resource name and a JTextField for the
	 * resource amount
	 * 
	 * @param produtor - O edifício que produz o recurso em questão
	 */
	// Se descobrir um jeito melhor de determinar qual recurso, usar
	private void addRecursoPanel(String produtor, GridBagConstraints c) {
		
		JPanel recursoNome = new JPanel();
		
		recursoNome.setBackground(Cores.FUNDO_ESCURO);
		recursoNome.setBorder(new MatteBorder
					(1, 1, 1, 0, Cores.SEPARAR_ESCURO));
		
		switch(produtor) {
		case "stone":
			recursoNome.add(new JLabel("Argila"));
			break;
		case "wood":
			recursoNome.add(new JLabel("Madeira"));
			break;
		case "iron":
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
		case "stone":
			recursosRestantes[0] = new IntegerFormattedTextField(onChange);
			recursoQuantidade.add(recursosRestantes[0]);
			break;
		case "wood":
			recursosRestantes[1] = new IntegerFormattedTextField(onChange);
			recursoQuantidade.add(recursosRestantes[1]);
			break;
		case "iron":
			recursosRestantes[2] = new IntegerFormattedTextField(onChange);
			recursoQuantidade.add(recursosRestantes[2]);
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
	
	private JSpinner makeSpinner(String format) {
		
		JSpinner spinner = new JSpinner(new SpinnerDateModel());
		spinner.setEditor(new JSpinner.DateEditor(spinner, format));
		((JSpinner.DateEditor) spinner.getEditor()).getTextField().getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent arg0) {
				onChange.run();
			}

			public void insertUpdate(DocumentEvent arg0) {
				onChange.run();
			}

			public void changedUpdate(DocumentEvent arg0) {}

		});
		
		return spinner;
	}
	
	private JPanel makeRespostaPanel() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setOpaque(false);
		
		// Add panel de último ataque
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("Enviar próximo ataque em"));
			
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

	protected void setDisplayHorario(Date tempo) {
		respostaLabel.setDate(tempo);
		
		errorMessage.setText(" ");
	}
	
	protected void setErrorMessage(String error) {
		respostaLabel.setText("");
		
		errorMessage.setText(error);
	}
	
	/**
	 * Returns the time of the last attack, in milliseconds
	 * @return long
	 */
	protected Date getDataEnviada() {
		
		long time;
		
		// Gets the dateSpinner part of the time
		time = TimeUnit.MILLISECONDS.toDays(((Date) dateSpinner.getModel().getValue()).getTime());
		time = TimeUnit.DAYS.toMillis(time);
		
		// Gets the hours part of the time                  days*minutes*millis
		time += ((Date) hourSpinner.getModel().getValue()).getTime()%(24*3600*1000);
		
		return new Date(time);
	}
	
	/**
	 * @return IntegerFormattedTextField[3] with the remaining resources
	 */
	protected IntegerFormattedTextField[] getRecursosRestantes() {
		return recursosRestantes;
	}
	
	/**
	 * Retorna o panel com as coordenadas da aldeia de origem
	 * @return CoordenadaPanel
	 */
	protected CoordenadaPanel getCoordenadaOrigem() {
		return coordenadas;
	}
	
	protected TimeFormattedJLabel getDateLabel() {
		return respostaLabel;
	}
	
	protected void resetAll() {
		
		coordenadas.reset();
		
		for (IntegerFormattedTextField t : recursosRestantes)
			t.setText(" ");
		
		respostaLabel.setText(" ");
		errorMessage.setText(" ");
	}
}
