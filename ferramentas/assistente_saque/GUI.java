package assistente_saque;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Mundo_Reader;
import custom_components.Ferramenta;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta{

	// Objeto de cálculo
	private Cálculo cálculo;
	
	// Panels de inserção de dados
	private PanelUnidade panelUnidades;
	private PanelIntervalo panelIntervalo;
	private PanelHorário panelHorário;
	
	// Panels para mostrar as respostas
	private JPanel panelRecomendado;
	private JPanel respostaIntervalo;
	private JPanel respostaHorário;
	
	private Map<Unidade, JLabel> mapRecomendado = new HashMap<Unidade, JLabel>();
	private JLabel labelDisplayIntervalo;
	
	
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
		
		cálculo = new Cálculo();
		
		panelUnidades = new PanelUnidade();
		panelIntervalo = new PanelIntervalo();
		panelHorário = new PanelHorário();
		
		makePanelRecomendado();
		makeRespostaIntervalo();
		panelRecomendado.setVisible(false);
		
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
		JPanel container = new JPanel();
		container.setPreferredSize(panelRecomendado.getPreferredSize());
		container.setOpaque(false);
		container.add(panelRecomendado);
		
		c.gridx += 2;
		c.gridwidth = 1;
		add(container, c);
		
		// Where the "AldeiaModelos" panel will be added
		c.gridy = 0;
		c.gridx++;
		c.gridheight = 1;
		add(tools.addModelosPanel(true, null), c);
		
		// Add the panel that receives input for interval calculations 
		c.gridy++;
		add(panelIntervalo, c);
		
		// Adds the panel that displays the interval in which to attack
		c.gridy++;
		add(respostaIntervalo, c);
		
		// Separar as duas funções
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setForeground(Cores.SEPARAR_ESCURO);
		
		c.gridx++;
		c.gridy = 1;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 30, 0, 30);
		add(separator, c);
		
		// Where the "AldeiaModelos" panel will be added
		c.gridy = 0;
		c.gridx++;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5, 5, 5, 5);
		add(tools.addModelosPanel(true, null), c);
		
		// Add the panel that receives input for exact time calculations 
		c.gridy = 1;
		c.anchor = GridBagConstraints.NORTH;
		add(panelHorário, c);
		
		// Adds the panel that displays the time to send the attack 
		c.gridy++;
		
		JButton button = new JButton("Go");
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				cálculo.setProduçãoEArmazenamento(panelIntervalo.getEdifícios());
				cálculo.setSaqueTotal(panelUnidades.getTextFields());
				
				cálculo.setIntervalo();
				setDisplayIntervalo(cálculo.getIntervalo());
			}
		});
		
		add(button, c);
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
		
		// GridBagLayout to allow for the whole button to be displayed
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
	
	private void makeRespostaIntervalo() {
		
		respostaIntervalo = new JPanel(new GridLayout(0,1));
		respostaIntervalo.setOpaque(false);
		
		// Add panel de último ataque
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("Enviar ataques a cada"));
			
		ataquePanel.setBackground(Cores.FUNDO_ESCURO);
		ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		respostaIntervalo.add(ataquePanel);
				
		// Add panel de horário
		
		labelDisplayIntervalo = new JLabel();
		
		JPanel horaPanel = new JPanel();
		horaPanel.add(labelDisplayIntervalo);
						
		horaPanel.setBackground(Cores.ALTERNAR_ESCURO);
		horaPanel.setBorder(new MatteBorder(0, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		respostaIntervalo.add(horaPanel);
		
	}
	
	protected void setDisplayIntervalo(long millis){
		
		long d = TimeUnit.MILLISECONDS.toDays(millis);
		long h = TimeUnit.MILLISECONDS.toHours(millis-TimeUnit.DAYS.toMillis(d));
		long m = TimeUnit.MILLISECONDS.toMinutes(millis-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h));
		long s = TimeUnit.MILLISECONDS.toSeconds(millis-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h)-TimeUnit.MINUTES.toMillis(m));
			
		if (d > 0)
			labelDisplayIntervalo.setText(String.format("%dd %02d:%02d:%02d", d, h, m, s));
		else
			labelDisplayIntervalo.setText(String.format("%02d:%02d:%02d", h, m, s));

	};
	
}
