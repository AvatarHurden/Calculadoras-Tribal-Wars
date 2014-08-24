package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.objects.Army;

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
import java.util.Stack;
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
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
/**
 * Classe para criar ou editar objetos da classe Alert
 * 
 * @author Arthur
 */
public class AlertEditor extends JDialog{
	
	// Necessário para poder cancelar modificações
	private Alert alerta;
	
	// Nome do alerta
	private JTextField nome;
	
	// Data do alerta
	private JSpinner spinnerDate, spinnerHour;
	
	// Qual o tipo do alerta
	private JPanel[] tipos;
	
	// Aldeia de origem
	private CoordenadaPanel origemCoord;
	private JTextField origemNome;
	
	// Aldeia de destino
	private CoordenadaPanel destinoCoord;
	private JTextField destinoNome;
	
	// Tropas enviadas
	private Map<Unidade, IntegerFormattedTextField> tropas;
	
	// Notas relacionadas ao alerta
	private JTextArea notas;
	
	// Períodos de avisos antes do evento
	private JButton addAviso;
	private LinkedHashMap<IntegerFormattedTextField, JComboBox<String>> avisos;
	
	// Componentes desativados quando o aviso é do tipo geral
	private List<Component> villageComponents = new ArrayList<Component>();
	
	// Scrollpane
	private JScrollPane scroll;
	
	/**
	 * Cria um editor em branco.
	 */
	protected AlertEditor() {
		
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
		panel.add(makeNotasPanel(null), c);
		
		c.gridy++;
		panel.add(makeAvisosPanel(), c);
		
		scroll = new JScrollPane(panel);
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

		// Como o tipo selecionado é geral, desativa as coisas adequadas.
		for (Component t : villageComponents)
			t.setEnabled(false);

		pack();
		setLocationRelativeTo(null);
		
	}
	
	/**
	 * Cria um editor com dados preenchidos, baseado no alerta passado como parâmetro
	 * @param alerta com o qual preencher os dados
	 */
	@SuppressWarnings("unchecked")
	public AlertEditor(Alert alerta) {
		
		this();
		
		this.alerta = alerta;
		
		String stringNome = alerta.getNome();
		if (stringNome != null) 
			nome.setText(stringNome);
		
		Date hora = alerta.getHorário();
		if (hora != null) {
			spinnerDate.setValue(hora);
			spinnerHour.setValue(hora);
		}
		
		Tipo tipo = alerta.getTipo();
		if (tipo != null) 
			switch (tipo) {
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
		
		Aldeia origem = alerta.getOrigem();
		if (origem != null) {
			origemCoord.setCoordenadas(origem.x, origem.y);
			origemNome.setText(origem.nome);
		}
		
		Aldeia destino = alerta.getDestino();
		if (destino != null) {
			destinoCoord.setCoordenadas(destino.x, destino.y);
			destinoNome.setText(destino.nome);
		}
		
		Map<Unidade, Integer> mapTropas = alerta.getTropas();
		if (mapTropas != null)
			for (Entry<Unidade, IntegerFormattedTextField> e : tropas.entrySet())
				if (mapTropas.containsKey(e.getKey()))
					e.getValue().setText(String.valueOf(mapTropas.get(e.getKey())));
		
		String stringNotas = alerta.getNotas();
		if (stringNotas != null) 
			notas.setText(stringNotas);
		
		Stack<Date> stackAvisos = alerta.getAvisos();
		if (stackAvisos != null)
			for (Date d : stackAvisos) {
				long tempo = hora.getTime()-d.getTime();
				
				addAviso.doClick();
				
				// Aviso é x horas antes
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
		
		scroll.getVerticalScrollBar().setValue(0);
	}
	
	/**
	 * Define o alerta com base no que está preenchido no editor.
	 */
	protected void setAlerta() {
		
		if (alerta == null)
			alerta = new Alert();
		
		alerta.setNome(nome.getText());
		
		long time;
		// Gets the dateSpinner part of the time
		time = TimeUnit.MILLISECONDS.toDays(((Date) spinnerDate.getModel().getValue()).getTime());
		time = TimeUnit.DAYS.toMillis(time);
		// Gets the hours part of the time                  days*minutes*millis
		time += ((Date) spinnerHour.getModel().getValue()).getTime()%(24*3600*1000);

		alerta.setHorário(new Date(time));
		
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
			
			entry *= 1000*(Math.pow(60,Math.abs(2-e.getValue().getSelectedIndex())));
			
			lista.add(new Date(alerta.getHorário().getTime() - entry));
		}
			
		alerta.setAvisos(lista);
		
		if (this.alerta == null || this.alerta.getRepete() == null)
			alerta.setRepete(0);
		else
			alerta.setRepete(this.alerta.getRepete());
	}
	
	/**
	 * Cria um JPanel para inserir o nome do alerta
	 */
	private JPanel makeNamePanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Nome"));
		
		nome = new JTextField(16);
		panel.add(nome);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir o tipo do alerta
	 */
	private JPanel makeTipoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Tipo"));
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		
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
		
		for (int i = 0; i < tipos.length; i++) {
			tipos[i].addMouseListener(listener);
			
			if (i == 1 || i == 2) c.insets = new Insets(5, 0, 5, 0);
			else if (i == tipos.length-1) c.insets = new Insets(5, 0, 5, 5);
			c.gridx++;
			panel.add(tipos[i], c);
		}
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir o horário do alerta
	 */
	private JPanel makeDataPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Data"));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		spinnerDate = new JSpinner(new SpinnerDateModel());
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "dd/MM/yyy"));
		
		spinnerHour = new JSpinner(new SpinnerDateModel());
		spinnerHour.setEditor(new JSpinner.DateEditor(spinnerHour, "HH:mm:ss"));
		
		panel.add(spinnerHour, c);
		
		c.gridx++;
		panel.add(spinnerDate, c);
		
		return panel;
	}
	
	private JPanel makeRepetePanel() {
		
		JPanel panel = new JPanel();
		
		
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir a origem do alerta. Fica desativado caso o alerta seja de tipo Geral
	 */
	private JPanel makeOrigemPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Origem"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 0, 0, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
	
		origemCoord = new CoordenadaPanel(null) {
			public void go() {}
		};
		origemCoord.setBorder(new EmptyBorder(0, 0, 0, 0));
		origemCoord.setOpaque(false);
		
		panel.add(origemCoord, c);
		
		origemNome = new JTextField(5);
		
		c.gridx++;
		panel.add(origemNome, c);
		
		villageComponents.addAll(Arrays.asList(panel.getComponents()));
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir o destino do alerta. Fica desativado caso o alerta seja de tipo Geral
	 */
	private JPanel makeDestinoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Destino"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 0, 0, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		destinoCoord = new CoordenadaPanel(null) {
			public void go() {}
		};
		destinoCoord.setBorder(new EmptyBorder(0, 0, 0, 0));
		destinoCoord.setOpaque(false);
		
		panel.add(destinoCoord, c);
		
		destinoNome = new JTextField(5);
		
		c.gridx++;
		panel.add(destinoNome, c);
		
		villageComponents.addAll(Arrays.asList(panel.getComponents()));
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir as tropas enviadas do alerta. Fica desativado caso o alerta seja de tipo Geral
	 */
	private JPanel makeTropaPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Tropas"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = -1;
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		tropas = new HashMap<Unidade, IntegerFormattedTextField>();
		
		for (Unidade i : Army.getAttackingUnits()) {
			if (i != null && !i.equals(Unidade.MILÍCIA)) {
				
				c.gridx = 0;
				c.gridy++;
				panel.add(new JLabel(i.getNome()), c);

				IntegerFormattedTextField txt = new IntegerFormattedTextField() {
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
	
	/**
	 * Cria um JPanel para definir as notas do alerta
	 */
	private JPanel makeNotasPanel(String nota) {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Notas"));
		
		notas = new JTextArea(nota, 5, 20);
		// Não permite que o textArea modifique a posição do scrollpane
		((DefaultCaret)notas.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		notas.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		notas.setLineWrap(true);
		notas.setWrapStyleWord(true);
		
		panel.add(notas);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir os avisos prévios do alerta. Cada aviso é composto de um número e uma
	 * unidade (horas, minutos segundos). É possível adicionar novos avisos clicando no botão '+'.
	 */
	private JPanel makeAvisosPanel() {
		
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Avisos"));
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		
		avisos = new LinkedHashMap<IntegerFormattedTextField, JComboBox<String>>();
		
		addAviso = new TWSimpleButton("+");
		addAviso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				IntegerFormattedTextField amount = new IntegerFormattedTextField(0, 4){
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
				
				revalidate();
				pack();
				
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
				
			}
		});
		
		panel.add(addAviso, c);
		
		return panel;
	}
	
	/**
	 * Cria os botões que ficam na parte inferior do editor (salvar e cancelar).
	 */
	private JPanel makeButtons() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		JButton salvar = new TWSimpleButton("Salvar");
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				setAlerta();
				dispose();
				
			}
		});
		
		JButton cancelar = new TWSimpleButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alerta = null;
				dispose();
			}
		});
		
		panel.add(salvar);
		panel.add(cancelar);
		
		return panel;
	}
	
	/**
	 * Retorna o alerta definido pelo editor
	 * @return alerta
	 */
	public Alert getAlerta() {
		return alerta;
	}
	
}
