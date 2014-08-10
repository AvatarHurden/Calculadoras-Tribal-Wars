package alertas;

import java.awt.Color;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import alertas.Alert.Tipo;
import database.Cores;
import database.Unidade;


/**
 * Classe para lidar com a criação e manutenção dos popups para objetos da classe Alert
 * 
 * @author Arthur
 */
public class PopupManager {
	
	List<Alert> alertas = new ArrayList<Alert>();
	
	private TreeSet<AlertStack> dates;
	private Map<Alert, TimerTask> tasksRodando = new HashMap<Alert, TimerTask>();
	private int openDialogs;
	
	private Date nextPopupDate;
	
	private Timer timer = new Timer();
	
	/**
	 * Cria um objeto do PopupManager. Para adicionar ou remover futuros popups, utilize addAlert e removeAlert.
	 */
	@SuppressWarnings("serial")
	protected PopupManager() {
			
		dates = new TreeSet<AlertStack>(getComparator()) {
			
			public boolean remove(Object o){
				
				if (o instanceof Alert)
					for (AlertStack s : this)
						if (s.alert.equals(o))
							return super.remove(s);
				
				return super.remove(o);
			}
			
		};		
		
	}
	
	/**
	 * Cria um comparador entre aldeias. No comparador, o menor valor é aquele que possui um horário mais
	 * cedo.
	 */
	private Comparator<AlertStack> getComparator() {
		
		return new Comparator<AlertStack>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(AlertStack a1, AlertStack a2) {
				
				Stack<Date> avisos1 = (Stack<Date>) a1.dates.clone();
				Stack<Date> avisos2 = (Stack<Date>) a2.dates.clone();
				
				int compare;
				while (!avisos1.isEmpty() && !avisos2.isEmpty()) {
					compare = avisos1.pop().compareTo(avisos2.pop());
					if (compare != 0)
						return compare;
				}
				
				if (avisos1.isEmpty() && avisos2.isEmpty())
					return 0;
				else if (avisos1.isEmpty())
					return -1;
				else 
					return 1;
			}
		};

	}
	
	/**
	 * Adiciona um alerta na lista, criando popups para os avisos e o horário
	 * @param alerta
	 */
	protected void addAlert(final Alert alerta) {
		
		alertas.add(alerta);
		
		Stack<Date> avisos = alerta.getAvisos();
		// Para o popup, os avisos não se diferenciam do alerta real.
		avisos.add(0, alerta.getHorário());
		
		// Remove todos os avisos que já deveriam ter sido mostrados
		for (Date d : avisos)
			if (d.compareTo(new Date()) < 1)
				avisos.remove(d);
		
		AlertStack stack = new AlertStack(alerta, avisos);

		dates.add(stack);
		
		if (tasksRodando.isEmpty() || nextPopupDate == null || nextPopupDate.compareTo(avisos.peek()) > 0)
			schedule(stack);
		
	}
	
	/**
	 * Remove todos os popups relacionados ao alerta dado
	 * @param alerta
	 */
	protected void removeAlert(Alert alerta) {
		
		dates.remove(alerta);
		
		for (Entry<Alert, TimerTask> e : tasksRodando.entrySet())
			if (e.getKey().equals(alerta))
				e.getValue().cancel();
		
		timer.purge();
	}
	
	protected void changeAlert(Alert alerta) {
		
		removeAlert(alerta);
		addAlert(alerta);
		
	}
	
	/**
	 * Cria um TimerTask para mostrar o próximo popup. Esse TimerTask, quando executado, chama novamente
	 * schedule, marcando o próximo popup. Isso é feito para minimizar o load no JVM.
	 * @param a AlertStack para ser marcado
	 */
	private void schedule(final AlertStack a) {
		
		Date date = a.getDate();
		
		if (date == null) {
			dates.remove(a);
			
			if (!dates.isEmpty())
				schedule(dates.first());
			
		} else if (a != null) {
			
			final AlertStack next = dates.first();
			
			tasksRodando.put(a.alert, new TimerTask() {
				
				public void run() {
					new PopupGUI(a.alert).showOnScreen();
					tasksRodando.remove(a.alert);
					timer.purge();
					if (next != null)
						schedule(next);
				}
			});
			
			timer.schedule(tasksRodando.get(a.alert), date);
			nextPopupDate = date; 
		}
	}
	
	/**
	 * Classe que liga um alerta a um stack de dates. Cada vez que um popup para o alerta é marcado,
	 * remove-se o date do stack.
	 */
	private class AlertStack {
		
		private Alert alert;
		private Stack<Date> dates;
		
		private AlertStack(Alert alert, Stack<Date> dates){
			this.alert = alert;
			this.dates = dates;
		}
		
		/**
		 * Retorna a próxima data a ser feita. Remove-a da lista. Caso não hajam mais datas, retorna null.
		 */
		private Date getDate() {
			if (dates.isEmpty())
				return null;
			else
				return dates.pop();
		}
		
	}
	
	@SuppressWarnings("serial")
	private class PopupGUI extends JDialog {
		
		private int POSITION;
		
		/**
		 * Cria o JDialog do popup
		 * @param alerta
		 */
		private PopupGUI(Alert alerta) {
			
			setUndecorated(true);
			((JPanel)getContentPane()).setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			getContentPane().setBackground(Cores.FUNDO_CLARO);
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
			setLayout(gridBagLayout);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			
			// Adding the header
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.gridwidth = 3;
			add(makeHeader(), c);
			
			// Adding name and type label
			JLabel namelbl = new JLabel(alerta.getNome());
			namelbl.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
			
			c.insets = new Insets(5, 5, 5, 5);
			c.gridwidth = 2;
			c.gridy++;
			add(namelbl, c);
			
			JLabel typelbl = new JLabel(alerta.getTipo().toString());
			
			c.gridx += 2;
			c.gridwidth = 1;
			add(typelbl, c);
			
			c.gridx = 0;
			c.gridy++;
			c.gridwidth = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(makeSpoilerPanel(makeInfoPanel(alerta, getPreferredSize().width)), c);
			
			JLabel datelbl = new JLabel();
			datelbl.setFont(datelbl.getFont().deriveFont((float) 15));
			datelbl.setText(new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(alerta.getHorário()));
			
			c.gridwidth = 1;
			c.gridy++;
			add(datelbl, c);
			
			pack();
		}
		
		/**
		 * Faz o header para o Popup, com o símbolo e nome do programa
		 */
		private JPanel makeHeader() {
			
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
			
			return panel;
		}
		
		/**
		 * Makes a panel with a button that, when clicked, shows the panel passed as parameter
		 * 
		 * @param spoiler Hidden Component
		 */
		private JPanel makeSpoilerPanel(final JComponent spoiler) {
			
			final JPanel panel = new JPanel();
			panel.setOpaque(true);
			panel.setBackground(Cores.ALTERNAR_CLARO);
			panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			panel.setLayout(gridBagLayout);
			
			final GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			
			Image image = Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/down_arrow.png"));
			ImageIcon icon = new ImageIcon(image.getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			
			final JLabel label = new JLabel(icon, SwingConstants.RIGHT);
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
					
					subtractOpenDialogs();
					showOnScreen();
					
				}
			});

			panel.add(label, c);
			
			return panel;
		}
		
		/**
		 * Cria um JPanel com as informações adicionais (aldeias, tropas e notas) do alerta
		 * 
		 * @param alerta
		 * @param width do JPanel
		 * @return JPanel
		 */
		private JPanel makeInfoPanel(Alert alerta, int width) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(true);
			panel.setBackground(Cores.FUNDO_CLARO);
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0 };
			gridBagLayout.rowWeights = new double[] { 0.0, 1.0 };
			panel.setLayout(gridBagLayout);
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.NORTH;
			c.insets = new Insets(2, 2, 3, 3);
			c.gridx = 0;
			c.gridy = 0;
			
			// Paineis que podem ser adicionados.
			JPanel origem, destino, tropas, notas;
			// Width de origem + destino, para definir tamanho das notas
			int notesWidth;
			
			if (!alerta.getTipo().equals(Tipo.Geral)) {
			
			origem = makeAldeiaPanel("Origem", alerta.getOrigem().toString());
			
			c.gridwidth = 1;
			panel.add(origem, c);
			
			destino = makeAldeiaPanel("Destino", alerta.getDestino().toString());
			
			c.gridx++;
			panel.add(destino, c);
			
			tropas = makeTropasPanel(alerta.getTropas());
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridheight = 2;
			c.gridx++;
			panel.add(tropas, c);
			
			notesWidth = origem.getPreferredSize().width + destino.getPreferredSize().width + 5;
			
			gridBagLayout.columnWidths = new int[] { origem.getPreferredSize().width, 
					destino.getPreferredSize().width, tropas.getPreferredSize().width };
			
			} else
				notesWidth = width;
			
			notas = makeNotasPanel(alerta.getNotas(), notesWidth);
			
			c.fill = GridBagConstraints.BOTH;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.gridx = 0;
			c.gridy++;
			panel.add(notas, c);
			
			// Arruma a largura para adequar aos componentes
			
			return panel;
		}
		
		/**
		 * Cria um painel para aldeia
		 * 
		 * @param titulo "Origem" ou "Destino"
		 * @param aldeia nome da aldeia (com coordenadas)
		 * @return JPanel
		 */
		private JPanel makeAldeiaPanel(String titulo, String aldeia) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), titulo));
			
			panel.add(new JLabel(aldeia));
			
			return panel;
			
		}
		
		/**
		 * Cria um JPanel com as tropas enviadas no alerta
		 * 
		 * @param map das unidades enviadas
		 * @return JPanel
		 */
		private JPanel makeTropasPanel(Map<Unidade, Integer> map) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Tropas Enviadas"));
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0 };
			panel.setLayout(gridBagLayout);
			
			String s = "<html>";
			String maxLine = "";
			
			for (Unidade i : Unidade.values())
				if (map.containsKey(i) && map.get(i) > 0 ) {
					String temp = i + ": " + map.get(i);
					s += temp + "<br>";
					if (temp.length() > maxLine.length())
						maxLine = temp;
				}
			
			s += "</html>";
			
			
			JTextPane label = new JTextPane();
			label.setContentType("text/html");  
			label.setText(s);
			label.setEditable(false);
			label.setOpaque(false);
			
			label.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, true);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0,0,0,0);
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.NORTH;
			panel.add(label, c);
			
			panel.setPreferredSize(new Dimension(
					label.getFontMetrics(label.getFont()).stringWidth(maxLine) + 25, panel.getPreferredSize().height));			
			
			return panel;
			
		}
		
		/**
		 * Cria um JPanel não editável com as notas do alerta
		 * 
		 * @param nota texto da nota
		 * @param width do JPanel
		 * @return JPanel
		 */
		private JPanel makeNotasPanel(String nota, int width) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Notas"));

			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			
			JTextPane notes = new JTextPane();
			notes.setText(nota);
			
			notes.setEditable(false);
			notes.setOpaque(false);
			
			JScrollPane scroll = new JScrollPane(notes);
			scroll.setViewportView(notes);
			scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
			scroll.getViewport().setOpaque(false);
			scroll.setOpaque(false);
			scroll.setPreferredSize(new Dimension(scroll.getPreferredSize().width, 100));
			
			panel.add(scroll, c);
			
			panel.setPreferredSize(new Dimension(width, panel.getPreferredSize().height));
			
			return panel;
			
		}
		
		/**
		 * Coloca o Popup no local adequado na tela, mostrando-o pelo tempo de 5 segundos (a menos que seja clicado)
		 */
		protected void showOnScreen() {
			
			pack();
			setSize(getPreferredSize());
			validate();
			pack();
			
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
			
			new Timer().schedule(new TimerTask() {
				public void run() {
					subtractOpenDialogs();
					dispose();
				}
			}, 5000);
			
			openDialogs += Math.pow(2, POSITION);
			
		}
		
		private void subtractOpenDialogs() {
			openDialogs -= Math.pow(2, POSITION);
		}
		
	}
	
}
