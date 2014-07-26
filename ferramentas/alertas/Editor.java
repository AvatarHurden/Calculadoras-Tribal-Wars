package alertas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;

import alertas.Alert.Aldeia;
import alertas.Alert.Tipo;
import config.Mundo_Reader;
import custom_components.CoordenadaPanel;
import custom_components.IntegerFormattedTextField;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class Editor extends JDialog{
	
	// Necess�rio para poder cancelar modifica��es
	Alert alerta;
	
	// Nome do alerta
	JTextField nome;
	
	// Data do alerta
	JSpinner spinnerDate, spinnerHour;
	
	// Qual o tipo do alerta
	JPanel[] tipos;
	
	// Aldeia de origem
	CoordenadaPanel origemCoord;
	JTextField origemNome;
	
	// Aldeia de destino
	CoordenadaPanel destinoCoord;
	JTextField destinoNome;
	
	// Tropas enviadas
	Map<Unidade, IntegerFormattedTextField> tropas;
	
	// Notas relacionadas ao alerta
	JTextArea notas;
	
	// Per�odos de avisos antes do evento
	JButton addAviso;
	LinkedHashMap<IntegerFormattedTextField, JComboBox<String>> avisos;
	
	// Componentes desativados quando o aviso � do tipo geral
	List<Component> villageComponents = new ArrayList<Component>();
	
	protected Editor() {
		
		setResizable(false);
		
		setLayout(new GridBagLayout());
		getContentPane().setBackground(Cores.FUNDO_CLARO);
		
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(Cores.FUNDO_CLARO);
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		
		// Adding name panel
		panel.add(makeNamePanel(), c);
		
		c.gridy++;
		panel.add(makeTipoPanel(), c);
		
		c.gridy++;
		panel.add(makeDataPanel(), c);
		
		c.gridwidth = 1;
		c.gridy++;
		panel.add(makeOrigemPanel(), c);
		
		c.gridx++;
		panel.add(makeDestinoPanel(), c);

		c.gridwidth = 2;
		c.gridy++;
		c.gridx--;
		panel.add(makeTropaPanel(), c);
		
		c.gridy++;
		panel.add(makeNotasPanel(), c);
		
		c.gridy++;
		panel.add(makeAvisosPanel(), c);
		
		JScrollPane scroll = new JScrollPane(panel);
		scroll.setOpaque(false);
		scroll.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		scroll.setPreferredSize(new Dimension(scroll.getPreferredSize().width+30, 500));
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 0;
		c.gridx = 0;
		add(scroll, c);
		
		c.gridy++;
		add(makeButtons(), c);

		// Como o tipo selecionado � geral, desativa as coisas adequadas.
		for (Component t : villageComponents)
			t.setEnabled(false);
		
		pack();
	}
	
	@SuppressWarnings("unchecked")
	protected Editor(Alert alerta) {
		
		this();
		
		this.alerta = alerta;
		
		if (alerta.getNome() != null) nome.setText(alerta.getNome());
		
		if (alerta.getHor�rio() != null) {
			spinnerDate.setValue(alerta.getHor�rio());
			spinnerHour.setValue(alerta.getHor�rio());
		}
		
		if (alerta.getTipo() != null) 
			switch (alerta.getTipo()) {
			case Geral : tipos[0].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[0], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			case Ataque: tipos[1].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[1], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			case Apoio: tipos[2].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[2], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			case Saque: tipos[3].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[3], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			}
		
		if (alerta.getOrigem() != null) {
			origemCoord.setCoordenadas(alerta.getOrigem().x, alerta.getOrigem().y);
			origemNome.setText(alerta.getOrigem().nome);
		}
		
		if (alerta.getDestino() != null) {
			destinoCoord.setCoordenadas(alerta.getDestino().x, alerta.getDestino().y);
			destinoNome.setText(alerta.getDestino().nome);
		}
		
		if (alerta.getTropas() != null)
			for (Entry<Unidade, IntegerFormattedTextField> e : tropas.entrySet())
				if (alerta.getTropas().containsKey(e.getKey()))
					e.getValue().setText(String.valueOf(alerta.getTropas().get(e.getKey())));
		
		if (alerta.getNotas() != null)
			notas.setText(alerta.getNotas());
		
		if (alerta.getAvisos() != null)
			for (Date d : alerta.getAvisos()) {
				long tempo = alerta.getHor�rio().getTime()-d.getTime();
				
				addAviso.doClick();
				
				// Aviso � x horas antes
				if (tempo % (60*60*1000) == 0) {
					((JTextField) avisos.keySet().toArray()[avisos.size()-1]).setText(""+tempo/(60*60*1000));
					((JComboBox<String>) avisos.values().toArray()[avisos.size()-1]).setSelectedIndex(0);
				} else if (tempo % (60*1000) == 0) {
					((JTextField) avisos.keySet().toArray()[avisos.size()-1]).setText(""+tempo/(60*1000));
					((JComboBox<String>) avisos.values().toArray()[avisos.size()-1]).setSelectedIndex(1);
				} else if (tempo % 1000 == 0) {
					((JTextField) avisos.keySet().toArray()[avisos.size()-1]).setText(""+tempo/(1000));
					((JComboBox<String>) avisos.values().toArray()[avisos.size()-1]).setSelectedIndex(2);
				}
				
			}
		
	}
	
	protected void setAlerta() {
		
		Alert alerta = new Alert();
		
		alerta.setNome(nome.getText());
		
		long time;
		// Gets the dateSpinner part of the time
		time = TimeUnit.MILLISECONDS.toDays(((Date) spinnerDate.getModel().getValue()).getTime());
		time = TimeUnit.DAYS.toMillis(time);
		// Gets the hours part of the time                  days*minutes*millis
		time += ((Date) spinnerHour.getModel().getValue()).getTime()%(24*3600*1000);

		alerta.setHor�rio(new Date(time));
		
		for (int i = 0; i < 4; i++)
			if (tipos[i].getBackground().equals(Cores.FUNDO_ESCURO))
				alerta.setTipo(Tipo.values()[i]);
		
		alerta.setOrigem(new Aldeia(origemNome.getText(), origemCoord.getCoordenadaX(), origemCoord.getCoordenadaY()));
		
		alerta.setDestino(new Aldeia(destinoNome.getText(), destinoCoord.getCoordenadaX(), destinoCoord.getCoordenadaY()));
		
		Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
		
		for (Entry<Unidade, IntegerFormattedTextField> e : tropas.entrySet())
			if (!e.getValue().getValue().equals(BigDecimal.ZERO))
				map.put(e.getKey(), e.getValue().getValue().intValue());
		
		alerta.setTropas(map);
		
		alerta.setNotas(notas.getText());
		
		List<Date> lista = new ArrayList<Date>();
		
		for (Entry<IntegerFormattedTextField, JComboBox<String>> e : avisos.entrySet()) {
			
			long entry = e.getKey().getValue().longValue();
			
			entry *= 1000*(60^Math.abs(2-e.getValue().getSelectedIndex()));
			
			lista.add(new Date(alerta.getHor�rio().getTime() - entry));
		}
			
		alerta.setAvisos(lista);
		
		if (this.alerta == null || this.alerta.getRepete() == null)
			alerta.setRepete(0);
		else
			alerta.setRepete(this.alerta.getRepete());
		
		this.alerta = alerta;
	}
	
	private JPanel makeNamePanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel nameLabel = new JLabel("Nome");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		panel.add(nameLabel, c);
		
		nome = new JTextField(16);
		c.gridy++;
		panel.add(nome, c);
		
		return panel;
	}
	
	private JPanel makeTipoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		
		JLabel nameLabel = new JLabel("Tipos");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		c.insets = new Insets(5, 10, 5, 5);
		c.gridwidth = 4;
		panel.add(nameLabel, c);
		
		JPanel geral = new JPanel();
		JPanel ataque = new JPanel();
		JPanel apoio = new JPanel();
		JPanel saque = new JPanel();
		
		geral.add(new JLabel("Geral"));
		ataque.add(new JLabel("Ataque"));
		apoio.add(new JLabel("Apoio"));
		saque.add(new JLabel("Saque"));
		
		tipos = new JPanel[] {geral, ataque, apoio, saque};
		
		for (JPanel p : tipos) {
			p.setBackground(Cores.FUNDO_CLARO);
			p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		}
		
		tipos[0].setBackground(Cores.FUNDO_ESCURO);
		tipos[0].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		
		MouseAdapter listener = new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				
				for (JPanel p : tipos) {
					p.setBackground(Cores.FUNDO_CLARO);
					p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				}
					
				((JPanel) e.getSource()).setBackground(Cores.FUNDO_ESCURO);
				((JPanel) e.getSource()).setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				
				if (((JPanel) e.getSource()).equals(tipos[0]))
					for (Component c : villageComponents)
						c.setEnabled(false);
				else
					for (Component c : villageComponents)
						c.setEnabled(true);
				
			}
		};
		
		c.gridx--;
		c.gridy++;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 5, 0);
		
		for (int i = 0; i < tipos.length; i++) {
			tipos[i].addMouseListener(listener);
			
			if (i == 1 || i == 2) c.insets = new Insets(5, 0, 5, 0);
			else if (i == tipos.length-1) c.insets = new Insets(5, 0, 5, 5);
			c.gridx++;
			panel.add(tipos[i], c);
		}
		
		return panel;
	}
	
	private JPanel makeDataPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel nameLabel = new JLabel("Data");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		c.gridwidth = 2;
		panel.add(nameLabel, c);
		
		spinnerDate = new JSpinner(new SpinnerDateModel());
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "dd/MM/yyy"));
		
		spinnerHour = new JSpinner(new SpinnerDateModel());
		spinnerHour.setEditor(new JSpinner.DateEditor(spinnerHour, "HH:mm:ss"));
		
		c.gridwidth = 1;
		c.gridy++;
		panel.add(spinnerHour, c);
		
		c.gridx++;
		panel.add(spinnerDate, c);
		
		return panel;
	}
	
	private JPanel makeRepetePanel() {
		
		JPanel panel = new JPanel();
		
		
		
		return panel;
	}
	
	private JPanel makeOrigemPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 0, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel nameLabel = new JLabel("Origem");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		c.gridwidth = 2;
		panel.add(nameLabel, c);
		
		origemCoord = new CoordenadaPanel(null) {
			public void go() {}
		};
		origemCoord.setBorder(new EmptyBorder(0, 0, 0, 0));
		origemCoord.setOpaque(false);
		
		c.insets = new Insets(5, 0, 0, 5);
		c.gridy++;
		c.gridwidth = 1;
		panel.add(origemCoord, c);
		
		origemNome = new JTextField(5);
		
		c.gridx++;
		panel.add(origemNome, c);
		
		villageComponents.addAll(Arrays.asList(panel.getComponents()));
		
		return panel;
	}
	
	private JPanel makeDestinoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 0, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel nameLabel = new JLabel("Destino");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		c.gridwidth = 2;
		panel.add(nameLabel, c);
		
		destinoCoord = new CoordenadaPanel(null) {
			public void go() {}
		};
		destinoCoord.setBorder(new EmptyBorder(0, 0, 0, 0));
		destinoCoord.setOpaque(false);
		
		c.insets = new Insets(5, 0, 0, 5);
		c.gridy++;
		c.gridwidth = 1;
		panel.add(destinoCoord, c);
		
		destinoNome = new JTextField(5);
		
		c.gridx++;
		panel.add(destinoNome, c);
		
		villageComponents.addAll(Arrays.asList(panel.getComponents()));
		
		return panel;
	}
	
	private JPanel makeTropaPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel nome = new JLabel("Tropas");
		nome.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		c.gridwidth = 2;
		panel.add(nome, c);
		
		tropas = new HashMap<Unidade, IntegerFormattedTextField>();
		
		c.gridwidth = 1;
		
		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {
			if (i != null && !i.equals(Unidade.MIL�CIA)) {
				
				c.gridx = 0;
				c.gridy++;
				panel.add(new JLabel(i.nome()), c);

				IntegerFormattedTextField txt = new IntegerFormattedTextField(9, Integer.MAX_VALUE) {
					public void go() {}
				};
			
				c.gridx = 1;
				panel.add(txt, c);
			
				tropas.put(i, txt);

			}
		}
		
		villageComponents.addAll(Arrays.asList(panel.getComponents()));
		
		return panel;
	}
	
	private JPanel makeNotasPanel() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		
		JLabel nameLabel = new JLabel("Notas");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		panel.add(nameLabel, c);
		
		notas = new JTextArea(5, 20);
		notas.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		notas.setLineWrap(true);
		notas.setWrapStyleWord(true);
		
		c.gridy++;
		panel.add(notas, c);
		
		return panel;
	}
	
	private JPanel makeAvisosPanel() {
		
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		
		JLabel nameLabel = new JLabel("Avisos");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		panel.add(nameLabel, c);
		
		avisos = new LinkedHashMap<IntegerFormattedTextField, JComboBox<String>>();
		
		addAviso = new JButton("+");
		addAviso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				IntegerFormattedTextField amount = new IntegerFormattedTextField(4, Integer.MAX_VALUE){
					public void go() {}
				};
				
				JComboBox<String> unit = new JComboBox<String>(new String[] { "horas", "minutos", "segundos" });
				
				avisos.put(amount, unit);	
				
				panel.remove((Component) e.getSource());
				
				JPanel avisoPanel = new JPanel();
				avisoPanel.setOpaque(false);
				avisoPanel.add(amount);
				avisoPanel.add(unit);
				avisoPanel.add(new JLabel("antes"));
				
				panel.add(avisoPanel, c);
				
				c.gridy++;
				panel.add((Component) e.getSource(), c);
				
				panel.revalidate();
				pack();
				
				
			}
		});
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		panel.add(addAviso, c);
		
		return panel;
	}
	
	private JPanel makeButtons() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		JButton salvar = new JButton("Salvar");
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				setAlerta();
				dispose();
				
			}
		});
		
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		panel.add(salvar);
		panel.add(cancelar);
		
		return panel;
	}
	
	protected Alert getAlerta() {
		return alerta;
	}
	
}
