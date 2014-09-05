package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

import database.Cores;


/**
 * Classe para lidar com a criação e manutenção dos popups para objetos da classe Alert
 * 
 * @author Arthur
 */
public class PopupManager {
	
	List<PopupGUI> openPopups;
	
	private Insets scnMax;
	private int bottomBorder;
	private int rightBorder;
	
	private int screenWidth;
	private int screenHeight;
	
	/**
	 * Cria um objeto do PopupManager. Para adicionar popups, utilize showNewPopup.
	 */
	protected PopupManager() {
				
		openPopups = new ArrayList<PopupGUI>();
		
		scnMax = Toolkit.getDefaultToolkit().getScreenInsets(new PopupGUI(null).getGraphicsConfiguration());
		
		bottomBorder = scnMax.bottom;
		rightBorder = scnMax.right;
		
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width - rightBorder;
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height - bottomBorder;
		
	}
	
	protected void showNewPopup(Alert alerta) {
		
		final PopupGUI popup = new PopupGUI(alerta);
		
		openPopups.add(popup);
		
		popup.showOnScreen();

	}
	
	private void positionPopup(PopupGUI popup) {
		
		if (!openPopups.contains(popup))
			return;
		
		int height = screenHeight;
		
		// Coloca o ponto máximo do popup no primeiro lugar aberto
		for (PopupGUI p : openPopups.subList(0, openPopups.indexOf(popup)))
			height -= p.getHeight();
		
		popup.setLocation(screenWidth - popup.getWidth(), 
				height - popup.getHeight());
			
	}
	
	private void positionAllPopups(int startingPoint) {
		
		for (PopupGUI p : openPopups.subList(startingPoint, openPopups.size()))
			positionPopup(p);
		
	}
	
	@SuppressWarnings("serial")
	private class PopupGUI extends JDialog {
		
		private Thread closeThread;
		
		/**
		 * Cria o JDialog do popup
		 * @param alerta
		 */
		private PopupGUI(Alert alerta) {
			
			if (alerta == null)
				return;
			
			setCloseTime();
			
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
			
			setSize(new Dimension(200,114));
			
			addFocusListener();
			
			setCloseTime();
			
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
					AlertasPanel.class.getResource("/images/Icon.png"));
			
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
					close();
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
					AlertasPanel.class.getResource("/images/down_arrow.png"));
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
					
					pack();
					
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
			
			tropas = makeTropasPanel(alerta.getArmy());
			
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

			panel.setPreferredSize(new Dimension(66, panel.getPreferredSize().height));
			
			return panel;
			
		}
		
		/**
		 * Cria um JPanel com as tropas enviadas no alerta
		 * 
		 * @param army das unidades enviadas
		 * @return JPanel
		 */
		private JPanel makeTropasPanel(Army army) {
			
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Tropas Enviadas"));
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[] { 1.0 };
			panel.setLayout(gridBagLayout);
			
			String s = "<html>";
			String maxLine = "";
			
			for (Troop t : army.getTropas())
				if (t.getQuantity() > 0 ) {
					String temp = t.getUnit().getPrettyName() + ": " + t.getQuantity();
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
			if (panel.getPreferredSize().width < 115)
				panel.setPreferredSize(new Dimension(115, panel.getPreferredSize().height));
			
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
			
			positionAllPopups(openPopups.indexOf(this));
			
			setVisible(true);
			setAlwaysOnTop(true);
			
		}
		
		private void setCloseTime() {
			
			if (closeThread != null)
				closeThread.interrupt();
			
			closeThread = new Thread(new Runnable() {
				public void run() {
					try {
					Thread.sleep(5000);
					close();
					} catch (Exception e) {}
				}
			});
			closeThread.start();;
			
		}
			
		private void close() {
			openPopups.remove(this);
			dispose();
			positionAllPopups(0);
		}
		
		private void addFocusListener() {
			
			addWindowFocusListener(new WindowFocusListener() {
				
				@Override
				public void windowLostFocus(WindowEvent arg0) {
					System.out.println("lost");
					setCloseTime();
				}
				
				@Override
				public void windowGainedFocus(WindowEvent arg0) {}
			});
			
			addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					if (closeThread != null) {
						closeThread.interrupt();
					}
				}
			});
			
		}
		
	}
	
}
