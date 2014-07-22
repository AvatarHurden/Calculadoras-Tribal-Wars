package alertas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import alertas.Alert.Aldeia;
import alertas.Alert.Tipo;
import custom_components.Ferramenta;
import database.Cores;
import database.Unidade;

public class GUI extends Ferramenta {
	
	List<Alert> alertas = new ArrayList<Alert>();
	JTable table = new JTable(new test(alertas));
	
	public GUI() {
		
		super("Alertas");
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 400 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		String[] nomes = {"Nome", "Tipo", "Origem", "Destino", "Tropas", "Horário", "Repete", "Notas" };
		
		for (int i = 0; i < 3; i++) {
			
			Alert alerta = new Alert();
			
			alerta.setNome("Nome"+i);
			
			alerta.setNotas(i+" MEU NOME É ESSE QUE ESTOU MOSTRANDO PARA VOC~ES AQUI, AGORA, NESSE DIA PARA A AL ELREAIOD HAUSD ASD ASOD HASOD HASDO AHSDO HSADO YASID AISD");
			
			alerta.setTipo(Tipo.values()[i % 4]);
			alerta.setOrigem(new Aldeia(nomes[2]+i, i*111, i*55));
			alerta.setDestino(new Aldeia(nomes[3]+i, i*11, i*555));
			
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

			alerta.setHorário(new Date(now.getTime()+i*100000000));
			
			alerta.setRepete((long) (Math.random()*100000000));
			
			alertas.add(alerta);
			
		}
		
		table.setLayout(new BorderLayout(10, 10));
		
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		table.setDefaultRenderer(Date.class, new DateCellRenderer());
		table.setDefaultRenderer(HashMap.class, new TropaCellRenderer());
		table.setDefaultRenderer(Long.class, new TimeCellRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new NotaCellRenderer());
		//table.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		
		sorter.setComparator(4, new Comparator<HashMap<Unidade, Integer>>() {
			
			public int compare(HashMap<Unidade, Integer> o1, HashMap<Unidade, Integer> o2) {
				
				int amount1 = 0, amount2 = 0;
				
				for (Entry<Unidade, Integer> e : o1.entrySet())
					if (e.getValue() > 0) amount1++;
				
				for (Entry<Unidade, Integer> e : o2.entrySet())
					if (e.getValue() > 0) amount2++;
				
				if (amount1 > amount2)  return 1;
				else if (amount2 > amount1) return -1;
				else {
					
					for (Unidade u : Unidade.values())
						// O segundo mapa tem a unidade, com menos quantidade
						if (o1.containsKey(u) && o2.containsKey(u) && o2.get(u) < o1.get(u)) return 1;
						// Tem, com mais quantidade
						else if (o1.containsKey(u) && o2.containsKey(u) && o2.get(u) > o1.get(u)) return -1;
					
				}
						
				return 0;
			}
		});
		
		table.setRowSorter(sorter);
		
		// Creates the "button" listener
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    if (e.getClickCount() == 2) {
			      JTable target = (JTable)e.getSource();
			      int clickrow = target.getSelectedRow();
			      int clickcolumn = target.getSelectedColumn();
			      	if (clickcolumn == 7)
			      		JOptionPane.showMessageDialog(null, alertas.get(clickrow).getNotas());
			    }
			  }
		});
		
		table.setRowHeight(table.getRowHeight()+36);
		
		table.getTableHeader().setBackground(Cores.FUNDO_ESCURO);
		
		final TableCellRenderer old = table.getTableHeader().getDefaultRenderer();
		
		class HeaderCellRenderer extends DefaultTableCellRenderer {
			
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				
				Component t = old.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				
				((JComponent) t).setPreferredSize(new Dimension(
						((JComponent) t).getPreferredSize().width, 30));
				
				return t;
				
			}
			
		}
		
		table.getTableHeader().setDefaultRenderer(new HeaderCellRenderer());
		
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(false);
		
		table.setGridColor(Cores.SEPARAR_CLARO);
		
		table.getColumnModel().setColumnMargin(1);
		table.setRowMargin(0);
		
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(1000,500));
		table.setFillsViewportHeight(true);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		add(scrollPane, c);
		
		c.gridy++;
		add(makeButtonPanels(), c);
		
	}
	
	private JPanel makeButtonPanels() {
	
		JPanel panel = new JPanel();
		
		JButton addAlerta = new JButton("Criar Novo");
		addAlerta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Editor editor = new Editor();
				
				editor.setModal(true);
				editor.setVisible(true);
				
				Alert alerta = editor.getAlerta();
				
				((test) table.getModel()).addAlert(alerta);
				
				((test)table.getModel()).fireTableDataChanged();
				table.repaint();
				
			}
		});
		
		JButton editAlerta = new JButton("Editar");
		editAlerta.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Alert selected = (Alert) table.getModel().getValueAt(
						table.convertRowIndexToModel(table.getSelectedRow()), -1);
				
				Editor editor = new Editor(selected);
				
				editor.setModal(true);
				editor.setVisible(true);
				
				Alert alerta = editor.getAlerta();
				System.out.println(alerta.getNome());
				
				((test) table.getModel()).setValueAt(alerta, 
						table.convertRowIndexToModel(table.getSelectedRow()), 0);
				
				((test)table.getModel()).fireTableDataChanged();
				table.repaint();
				
			}
		});
		
		JButton deleteAlerta = new JButton("Deletar");
		
		panel.add(addAlerta);
		panel.add(editAlerta);
		
		return panel;
	}
	
	private class NotaCellRenderer extends CustomCellRenderer {
		
		public Component getTableCellRendererComponent (final JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, final int row, final int column) {
			
			ImageIcon i = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/edit.png")));
			
			Component cell = super.getTableCellRendererComponent(
					   table, obj, isSelected, hasFocus, row, column);
			
			JLabel label = new JLabel(i);
			label.setBackground(cell.getBackground());
			label.setOpaque(true);
			
			return label;
			
		}
		
	}
	
	private class DateCellRenderer extends CustomCellRenderer {
		
		public Component getTableCellRendererComponent (final JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, final int row, final int column) {
			
			String date = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format((Date) obj);
			
			Component cell = super.getTableCellRendererComponent(
					   table, date, isSelected, hasFocus, row, column);
			
			return cell;
			
		}
		
	}
	
	private class TimeCellRenderer extends CustomCellRenderer {
		
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			long value = (long) obj;
			String time;
			
			if (value > 0) {
			
				long d = TimeUnit.MILLISECONDS.toDays(value);
				long h = TimeUnit.MILLISECONDS.toHours(value-TimeUnit.DAYS.toMillis(d));
				long m = TimeUnit.MILLISECONDS.toMinutes(value-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h));
				long s = TimeUnit.MILLISECONDS.toSeconds(value-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h)-TimeUnit.MINUTES.toMillis(m));
			
				if (d > 0)
					time = (String.format("%dd %02d:%02d:%02d", d, h, m, s));
				else
					time = (String.format("%02d:%02d:%02d", h, m, s));
			
			} else {
				time = "-----";
				setHorizontalAlignment(JLabel.CENTER);
			}
			
			Component cell = super.getTableCellRendererComponent(
					   table, time, isSelected, hasFocus, row, column);
			
			return cell;
			
		}
		
	}
	
	private class TropaCellRenderer extends CustomCellRenderer {
		
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			HashMap<Unidade, Integer> map = (HashMap<Unidade, Integer>) obj;
			
			System.out.println(map);
			
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
			
			Component cell = super.getTableCellRendererComponent(
					   table, escrita, isSelected, hasFocus, row, column);
			
			if (lines > 3)
				((JComponent) cell).setToolTipText(tooltip);
			
			return cell;
			
		}
		
	}
	
	private class CustomCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component cell = super.getTableCellRendererComponent(
					   table, obj, isSelected, hasFocus, row, column);
			
			((JComponent) cell).setBorder(new EmptyBorder(0, 5, 0, 5));
			
			if (isSelected)
				cell.setBackground(Cores.SEPARAR_CLARO);
			else if (row % 2 == 0)
				cell.setBackground(Cores.ALTERNAR_CLARO);
			else
				cell.setBackground(Cores.ALTERNAR_ESCURO);
				
			return cell;
			
		}
		
	}
	
	private class test extends AbstractTableModel implements TableModel  {
		
		private List<Alert> alerts;
		
		protected test(Alert[] alerts) {
			this.alerts = new ArrayList<Alert>(Arrays.asList(alerts));
		}
		
		protected test(List<Alert> alerts) {
			this.alerts = alerts;
		}
		
		protected void addAlert(Alert alerta) {
			alerts.add(alerta);
		}
		
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {
			
		}

		@Override
		public Class<?> getColumnClass(int column) {
			
			switch(column) {
			case 0: return alerts.get(0).getNome().getClass();
			case 1: return alerts.get(0).getTipo().getClass();
			case 2: return alerts.get(0).getOrigem().getClass();
			case 3: return alerts.get(0).getDestino().getClass();
			case 4: return alerts.get(0).getTropas().getClass();
			case 5: return alerts.get(0).getHorário().getClass();
			case 6: return alerts.get(0).getRepete().getClass();
			case 7: return alerts.get(0).getNotas().getClass();
			default: return null;
		}
			
		}

		@Override
		public int getColumnCount() {
			return 8;
		}

		@Override
		public String getColumnName(int column) {
			
			switch(column) {
				case 0: return "Nome";
				case 1: return "Tipo";
				case 2: return "Origem";
				case 3: return "Destino";
				case 4: return "Tropas";
				case 5: return "Horário";
				case 6: return "Repete";
				case 7: return "Notas";
				default: return null;
			}
			
		}

		@Override
		public int getRowCount() {
			return alerts.size();
		}


		/**
		 * Para valores entre 0 e 6 de coluna, retorna uma propriedade do alerta da linha correspondente.
		 * <br>Para qualquer outro valor de coluna, retorna o alerta em si.
		 */
		public Object getValueAt(int row, int column) {
			
			switch(column) {
				case 0: return (alerts.get(row).getNome() != null) ? alerts.get(row).getNome() : "";
				case 1: return (alerts.get(row).getTipo() != null) ? alerts.get(row).getTipo() : Tipo.Geral;
				case 2: return alerts.get(row).getOrigem();
				case 3: return alerts.get(row).getDestino();
				case 4: return alerts.get(row).getTropas();
				case 5: return alerts.get(row).getHorário();
				case 6: return (alerts.get(row).getRepete() != null) ? alerts.get(row).getRepete() : 0;
				// Não permite ordenar os alertas através das notas
				case 7: return null;
				default: return alerts.get(row);
			}
			
		}

		@Override
		public boolean isCellEditable(int i, int j) {
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValueAt(Object obj, int row, int column) {
			
			alertas.remove(row);
			alertas.add(row, (Alert) obj);
			
		}
		
	}
	
}
