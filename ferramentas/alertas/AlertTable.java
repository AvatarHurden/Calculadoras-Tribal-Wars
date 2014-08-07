package alertas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import sun.swing.table.DefaultTableCellHeaderRenderer;
import alertas.Alert.Tipo;
import config.Config_Gerais;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class AlertTable extends JTable{
	
	private List<Alert> alerts = new ArrayList<Alert>();
	
	protected AlertTable(List<Alert> alertas) {
		
		this();
		
		this.alerts = alertas;
		
	}	
	protected AlertTable() {
		
		setModel(new AlertTableModel());
		
		setBackground(Cores.FUNDO_CLARO);
		setRowHeight(getRowHeight()+36);
		
		// Changing the grid properties
		setShowVerticalLines(true);
		setShowHorizontalLines(false);
		
		setGridColor(Cores.SEPARAR_ESCURO);
		
		getColumnModel().setColumnMargin(1);
		setRowMargin(0);
		
		// Selection properties
		setRowSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		/* Setting the renderers */
		// This is the default, used by String, Aldeia and Tipo
		setDefaultRenderer(Object.class, new CustomCellRenderer());
		setDefaultRenderer(Date.class, new DateCellRenderer());
		setDefaultRenderer(HashMap.class, new TropaCellRenderer());
		setDefaultRenderer(Long.class, new TimeCellRenderer());
		getColumnModel().getColumn(7).setCellRenderer(new NotaCellRenderer());
		
		setSorter();
		
		addNoteClickListener();
		
		changeHeader();
		
		// Change the positions and widths of the columns
		try {
			
			TableColumn column[] = new TableColumn[Config_Gerais.getColumnOrder().length];
			
			// Reordering the columns
			for (int i = 0; i < column.length; i++)
				 column[i] = columnModel.getColumn(Config_Gerais.getColumnOrder()[i]);
			 
			while (columnModel.getColumnCount() > 0) 
				 columnModel.removeColumn(columnModel.getColumn(0));
			 
			for (int i = 0; i < column.length; i++)
				 columnModel.addColumn(column[i]);
			
			// Putting the correct widths
			for (int i = 0; i < Config_Gerais.getColumnWidths().length; i++)
				getColumnModel().getColumn(i).setPreferredWidth(Config_Gerais.getColumnWidths()[i]);
			
		} catch (Exception e) {
			getColumnModel().getColumn(0).setPreferredWidth(314);
			getColumnModel().getColumn(1).setPreferredWidth(86);
			getColumnModel().getColumn(2).setPreferredWidth(205);
			getColumnModel().getColumn(3).setPreferredWidth(215);
			getColumnModel().getColumn(4).setPreferredWidth(207);
			getColumnModel().getColumn(5).setPreferredWidth(219);
			getColumnModel().getColumn(6).setPreferredWidth(134);
			getColumnModel().getColumn(7).setPreferredWidth(108);
		}
		
		getColumnModel().addColumnModelListener(new TableColumnModelListener() {
			
			public void columnSelectionChanged(ListSelectionEvent e) {}
			
			public void columnRemoved(TableColumnModelEvent e) {}
			
			@Override
			public void columnMoved(TableColumnModelEvent e) {
				
				int[] order = new int[getColumnModel().getColumnCount()];
				
				for (int i = 0; i < order.length; i++)
					order[i] = getColumnModel().getColumn(i).getModelIndex();
				
				Config_Gerais.setColumnOrder(order);
			}
			
			public void columnMarginChanged(ChangeEvent e) {
				
				int[] width = new int[getColumnModel().getColumnCount()];
				
				for (int i = 0; i < width.length; i++)
					width[i] = getColumnModel().getColumn(i).getPreferredWidth();
				
				Config_Gerais.setColumnWidths(width);
				
			}
			
			@Override
			public void columnAdded(TableColumnModelEvent e) {}
		});
		
	}
	
	private void setSorter() {
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(getModel());
		
		// Doesn't allow sorting by notes
		sorter.setSortable(7, false);
		
		// Changes the sorter for units sent
		sorter.setComparator(4, new Comparator<HashMap<Unidade, Integer>>() {
			
			public int compare(HashMap<Unidade, Integer> o1, HashMap<Unidade, Integer> o2) {
				
				int amount1 = 0, amount2 = 0;
				
				for (Entry<Unidade, Integer> e : o1.entrySet())
					if (e.getValue() > 0) amount1++;
				
				for (Entry<Unidade, Integer> e : o2.entrySet())
					if (e.getValue() > 0) amount2++;
				
				// The smallest are the ones with least different units
				if (amount1 > amount2)  return 1;
				else if (amount2 > amount1) return -1;
				else {
					
					// For alerts with same different units, we compare the amount of every individual unit
					for (Unidade u : Unidade.values())
						// O segundo mapa tem a unidade, com menos quantidade
						if (o1.containsKey(u) && o2.containsKey(u) && o2.get(u) < o1.get(u)) return 1;
						// Tem, com mais quantidade
						else if (o1.containsKey(u) && o2.containsKey(u) && o2.get(u) > o1.get(u)) return -1;
					
				}
						
				return 0;
			}
		});
		
		setRowSorter(sorter);
		
	}
	
	// Creates a mouse listener that, when clicked over a note, opens a Dialog with the contents of
	// the note to be edited
	private void addNoteClickListener() {
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    if (e.getClickCount() == 2) {
			
			      final JTable target = (JTable)e.getSource();
			      
			      final int clickrow = target.convertRowIndexToModel(target.getSelectedRow());
			      int clickcolumn = target.getSelectedColumn();
			      
			      Rectangle cell = target.getCellRect(clickrow, target.getSelectedColumn(), false);
			      
			      // Only does this if the selected column is tne notes column
			      if (target.convertColumnIndexToModel(clickcolumn) == 7) {
			      	
			      		final JDialog dialog = new JDialog();
			      		dialog.setUndecorated(true);
			      		dialog.setFocusable(true);
			      		
			      		GridBagConstraints c = new GridBagConstraints();
			      		c.insets = new Insets(5, 5, 5, 5);
			      		c.gridx = 0;
			      		c.gridy = 0;
			      		
			      		JPanel panel = new JPanel();
			    		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			    		panel.setOpaque(true);
			    		panel.setBackground(Cores.FUNDO_CLARO);
			    		
			    		GridBagLayout gridBagLayout = new GridBagLayout();
			    		gridBagLayout.columnWidths = new int[] { 118, 118 };
			    		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
			    		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			    		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
			    		panel.setLayout(gridBagLayout);
			    		
			    		JLabel nameLabel = new JLabel("Notas");
			    		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
			    		
			    		c.anchor = GridBagConstraints.WEST;
			    		c.gridwidth = 2;
			    		panel.add(nameLabel, c);
			    		
			    		final JTextArea notas = new JTextArea(alerts.get(clickrow).getNotas(), 5, 20);
			    		notas.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
			    		notas.setLineWrap(true);
			    		notas.setWrapStyleWord(true);
			    		
			    		c.gridy++;
			    		panel.add(notas, c);
			      		
			    		JButton salvar = new JButton("Salvar");
			    		salvar.addActionListener(new ActionListener() {
			    			public void actionPerformed(ActionEvent e) {
			    				target.setValueAt(notas.getText(), clickrow, 7);
			    				dialog.dispose();
			    			}
			    		});
			    		
			    		JButton cancelar = new JButton("Cancelar");
			    		cancelar.addActionListener(new ActionListener() {
			    			public void actionPerformed(ActionEvent e) {
			    				dialog.dispose();
			    			}
			    		});
			    		
			    		c.gridwidth = 1;
			    		c.gridy++;
			    		c.anchor = GridBagConstraints.EAST;
			    		panel.add(salvar, c);
			    		
			    		c.gridx++;
			    		c.anchor = GridBagConstraints.WEST;
			    		panel.add(cancelar, c);
			    
			    		JScrollPane scroll = new JScrollPane(panel);
			    		scroll.setPreferredSize(new Dimension(scroll.getPreferredSize().width+16, scroll.getPreferredSize().height));
			    		scroll.setOpaque(false);
			    		
			    		dialog.add(scroll);
			    		
			    		dialog.addWindowFocusListener(new WindowFocusListener() {
							
							public void windowLostFocus(WindowEvent e) {
								dialog.dispose();
								}
							
							public void windowGainedFocus(WindowEvent e) {}
						});
			    		
			    		dialog.pack();
			    		
			    		dialog.setLocation((int)cell.getCenterX() + target.getLocationOnScreen().x - dialog.getPreferredSize().width/2, 
			    				(int)cell.getCenterY() + target.getLocationOnScreen().y - dialog.getPreferredSize().height/2);
			    		
			    		dialog.setVisible(true);
			    		
			      	}   	
			    }
			  }
		});
		
	}
	
	private void changeHeader() {
		
		getTableHeader().setBackground(Cores.FUNDO_ESCURO);
		
		// Simply changes the height of the header
		class HeaderCellRenderer extends DefaultTableCellHeaderRenderer {
			
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				
				Component t = super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				
				((JComponent) t).setPreferredSize(new Dimension(
						((JComponent) t).getPreferredSize().width, 30));
				
				return t;
				
			}
			
		}
	
		getTableHeader().setDefaultRenderer(new HeaderCellRenderer());
		
	}
	
	protected void addAlert(Alert alerta) {
		
		alerts.add(alerta);
		((AlertTableModel) getModel()).fireTableDataChanged();
		repaint();
		
	}
	
	protected void changeAlert(Alert alerta, int row) {
		
		alerts.remove(row);
		alerts.add(row, alerta);

		((AlertTableModel) getModel()).fireTableDataChanged();
		repaint();
		
	}
	
	protected void removeAlert(int row) {
		
		alerts.remove(row);
		((AlertTableModel) getModel()).fireTableDataChanged();
		repaint();
		
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
			
			label.setToolTipText("Clique duas vezes para ver e editar a nota");
			
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
				
				setHorizontalAlignment(JLabel.LEADING);
				
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
			
			@SuppressWarnings("unchecked")
			HashMap<Unidade, Integer> map = (HashMap<Unidade, Integer>) obj;
			
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
			else
				((JComponent) cell).setToolTipText(null);
			
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
	
	protected class AlertTableModel extends AbstractTableModel implements TableModel  {
		
		protected AlertTableModel() {}
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {}

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
				case 7: return (alerts.get(row).getNotas() != null) ? alerts.get(row).getNotas() : "";
				default: return alerts.get(row);
			}
			
		}

		@Override
		public boolean isCellEditable(int i, int j) {
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {}

		@Override
		public void setValueAt(Object obj, int row, int column) {
			
			// The only one to be changed this way is the notes
			if (column == 7)
				alerts.get(row).setNotas((String) obj);
			
		}
		
	}
	
	
}
