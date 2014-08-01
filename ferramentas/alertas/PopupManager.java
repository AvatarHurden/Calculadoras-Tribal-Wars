package alertas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import alertas.Alert.Aldeia;
import alertas.Alert.Tipo;
import config.Config_Gerais;
import config.File_Manager;
import config.Mundo_Reader;
import database.Cores;
import database.Unidade;


public class PopupManager {
	
	List<Alert> alertas = new ArrayList<Alert>();
	
	int openDialogs;
	
	Timer timer = new Timer();
	
	protected PopupManager() {
		
	for (int i = 0; i < 2; i++) {
		
		Alert alerta = new Alert();
		
		alerta.setNome("Nome"+i);
		
		alerta.setNotas(i+"Ataque para limpeza. Enviar relatório para Pedro2");
		alerta.setTipo(Tipo.values()[i % 4]);
		alerta.setOrigem(new Aldeia("Origem"+i, i*111, i*55));
		alerta.setDestino(new Aldeia("Destino"+i, i*11, i*555));
		
		if (i > 0) {
		Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
		for (Unidade u : Unidade.values())
			map.put(u, (int) (Math.random()*100 + 1));
		alerta.setTropas(map);
		} else {
			Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
			map.put(Unidade.LANCEIRO, 34);
			map.put(Unidade.ESPADACHIM, 342);
			map.put(Unidade.ARCOCAVALO, 32);
			alerta.setTropas(map);
			
		}
		
		Date now = new Date();
		alerta.setHorário(new Date(now.getTime()+i*3000));
		
		alerta.setRepete((long) (Math.random()*100000000));
		
		alertas.add(alerta);
		
	}
		
		for (final Alert d : alertas)
			timer.schedule(new TimerTask() {
				public void run() {
					new PopupGUI(d).showOnScreen();
				}
			}, d.getHorário());
		
		
	}
	
	private class PopupGUI extends JDialog {
		
		private int POSITION;
		
		private PopupGUI() {
			
			setUndecorated(true);
			//setPreferredSize(new Dimension(300, 100));
			
			getContentPane().setBackground(new Color((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255)));			
			
			((JPanel)getContentPane()).setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			pack();
		}
		
		private PopupGUI(Alert alerta) {
			
			this();
			
			getContentPane().setBackground(Cores.FUNDO_CLARO);
			
			setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 1;
			c.anchor = GridBagConstraints.WEST;
			
			// Adding name and type label
			JLabel namelbl = new JLabel(alerta.getNome());
			namelbl.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
			
			c.gridwidth = 2;
			add(namelbl, c);
			
			JLabel typelbl = new JLabel(alerta.getTipo().toString());
			
			c.gridx += 2;
			c.gridwidth = 1;
			add(typelbl, c);
			
			c.gridx = 0;
			c.gridy++;
			c.gridwidth = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(makeSpoilerPanel(makeInfoPanel(alerta)), c);
			
			JLabel datelbl = new JLabel();
			datelbl.setFont(datelbl.getFont().deriveFont((float) 15));
			datelbl.setText(new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(alerta.getHorário()));
			
			c.gridwidth = 1;
			c.gridy++;
			add(datelbl, c);
			
			// Adding the header
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 0;
			add(makeHeader(getPreferredSize().width), c);
			
			pack();
		}
		
		private JPanel makeHeader(int width) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(true);
			panel.setBackground(Cores.FUNDO_ESCURO);
			panel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			panel.setLayout(gridBagLayout);
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(2, 2, 2, 5);
			c.gridx = 0;
			c.gridy = 0;
			
			Image image = Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/Icon.png"));
			
			ImageIcon icon = new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
			
			c.anchor = GridBagConstraints.WEST;
			panel.add(new JLabel("Tribal Wars Engine", icon, SwingConstants.LEFT), c);
			
			JLabel closelbl = new JLabel("X");
			closelbl.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseEntered(MouseEvent e) {
					((JLabel) e.getSource()).setForeground(Color.RED);
					((JLabel) e.getSource()).repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					((JLabel) e.getSource()).setForeground(Color.BLACK);
					((JLabel) e.getSource()).repaint();
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					subtractOpenDialogs();
					dispose();
				}
			});
			
			c.anchor = GridBagConstraints.EAST;
			c.gridx++;
			panel.add(closelbl, c);
			
			panel.setPreferredSize(new Dimension(width, panel.getPreferredSize().height));
			
			return panel;
		}
		
		// Makes a panel with a button that, when clicked, shows the panel passed as parameter
		private JPanel makeSpoilerPanel(final JComponent spoiler) {
			
			final JPanel panel = new JPanel();
			panel.setOpaque(true);
			panel.setBackground(Cores.ALTERNAR_CLARO);
			panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			panel.setLayout(gridBagLayout);
			
			final GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			
			Image image = Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/down_arrow.png"));
			ImageIcon icon = new ImageIcon(image.getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			
			JLabel label = new JLabel(icon, SwingConstants.RIGHT);
			label.setPreferredSize(new Dimension(getPreferredSize().width, 18));
			
			label.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseExited(MouseEvent e) {
					panel.setBackground(Cores.ALTERNAR_CLARO);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					panel.setBackground(Cores.ALTERNAR_ESCURO);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					panel.setBackground(Cores.ALTERNAR_CLARO);
					panel.remove((JComponent) e.getSource());
					panel.add(spoiler, c);
					pack();
					subtractOpenDialogs();
					showOnScreen();
				}
			});

			panel.add(label, c);
			
			//panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 20));
			
			return panel;
		}
		
		private JPanel makeInfoPanel(Alert alerta) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(true);
			panel.setBackground(Cores.FUNDO_CLARO);
			
			panel.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			//c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			
			// Inserindo as aldeias
			JLabel origemlbl = new JLabel("Origem");
			origemlbl.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
			
			panel.add(origemlbl, c);
			
			JLabel destinolbl = new JLabel("Destino");
			destinolbl.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
			
			c.gridx++;
			panel.add(destinolbl, c);
			
			c.gridx = 0;
			c.gridy++;
			panel.add(new JLabel(alerta.getOrigem().toString()), c);
			
			c.gridx++;
			panel.add(new JLabel(alerta.getDestino().toString(), SwingConstants.CENTER), c);
			
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy++;
			panel.add(makeTropasPanel(alerta.getTropas()), c);
			
			JTextArea notes = new JTextArea(alerta.getNotas());
			notes.setWrapStyleWord(true);
			notes.setLineWrap(true);
			notes.setEditable(false);
			notes.setOpaque(false);
			
			c.gridy++;
			panel.add(notes, c);
			
			return panel;
		}
		
		private JPanel makeTropasPanel(Map<Unidade, Integer> map) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			
			String escrita = "<html>";
			String tooltip = "<html>";
			int lines = 0;
			
			for (Unidade i : Unidade.values())
				if (map.containsKey(i) && map.get(i) > 0) {
					tooltip += i + ": " + map.get(i)+"<br>";
					lines++;
					if (lines <= 3)
						escrita = tooltip;
				}
			
			String[] parts = tooltip.split("<br>");
			
			if (lines > 3)
				escrita = parts[0] + "<br>" + parts[1] + "<br> <center><font size=3>+</font></center>";
			
			escrita += "</html>";
			tooltip += "</html>";
			
			panel.add(new JLabel(escrita));
			
			if (lines > 3)
				panel.setToolTipText(tooltip);
			else
				panel.setToolTipText(null);
			
			return panel;
			
		}
		
		protected void showOnScreen() {
			
			// Setting the position
			int width = Toolkit.getDefaultToolkit().getScreenSize().width;
			int height = Toolkit.getDefaultToolkit().getScreenSize().height;
			
			Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
			int taskBarSize = scnMax.bottom;
			
			int pos = 0;
			
			// Finds out which position is empty
			int empty = openDialogs;
			while (empty % 2 != 0 && empty > 0) {
				empty = (int) empty/2;
				pos++;
			}
			
			POSITION = pos;
			
			setLocation(width-getPreferredSize().width-5, 
					// Moves the location of the dialog up for every open dialog
					height - getPreferredSize().height*(POSITION+1) - taskBarSize);
			
			setVisible(true);
			setAlwaysOnTop(true);
			
			//new Timer().schedule(new TimerTask() {
			//	public void run() {
			//		subtractOpenDialogs();
			//		dispose();
			//	}
			//}, 5000);
			
			openDialogs += Math.pow(2, POSITION);
			
		}
		
		private void subtractOpenDialogs() {
			openDialogs -= Math.pow(2, POSITION);
		}
		
		
	}
	
	public static void main(String args[]) {
		
		Config_Gerais.read();
		
		File_Manager.read();

		File_Manager.defineMundos();
		
		Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundo(4));
		
		PopupManager tet = new PopupManager();
		
	}
	
}
