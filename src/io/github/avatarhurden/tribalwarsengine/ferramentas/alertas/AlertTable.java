package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterEvent.Type;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.json.JSONArray;
import org.json.JSONObject;

import sun.swing.table.DefaultTableCellHeaderRenderer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe para representar, em uma JTable, objetos da classe Alert
 * 
 * @author Arthur
 */
public class AlertTable extends JTable{
	
	private List<Alert> alerts;
	private List<Alert> oldRows;
	
	protected AlertTable() {
		
		alerts = new ArrayList<Alert>();
		oldRows = new ArrayList<Alert>();
			
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
		// This is the default, used by String and Tipo
		setDefaultRenderer(Object.class, new CustomCellRenderer());
		setDefaultRenderer(Date.class, new RescheduleCellRenderer());
		setDefaultEditor(Date.class, new RescheduleCellEditor());
		setDefaultRenderer(Army.class, new TropaCellRenderer());
		setDefaultRenderer(Long.class, new TimeCellRenderer());
		getColumnModel().getColumn(7).setCellRenderer(new NotaCellRenderer());
		
		// Cria um renderer igual ao normal, mas com texto centralizado
		CustomCellRenderer aldeiaRenderer = new CustomCellRenderer();
		aldeiaRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		setDefaultRenderer(Aldeia.class, aldeiaRenderer);
		
		setSorter();
		
		addNoteClickListener();
		
		changeHeader();
		
		positionColumns();
	}
	
	public void setStartingPosition(JScrollPane scroll) {
		if ((boolean) AlertManager.getInstance().getConfig("show_past", false)) {
			scroll.getVerticalScrollBar().setValue(getStartingPosition());
			scroll.getVerticalScrollBar().setValue(getStartingPosition());
		}
	}
	
	private int getStartingPosition() {
		return AlertManager.getInstance().getPastAlertList().size() * 52;
	}
	
	/**
	 * Cria o sorter específico para a tabela. As mudanças em relação ao sorter padrão são:
	 * - Não permite sortar pela coluna de notas
	 * - Para comparar lista de tropas, testa qual possui menos unidades diferentes, sendo desempatado
	 * pela quantidade específica de cada unidade, em ordem.
	 */
	private void setSorter() {
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(getModel());
		
		// Doesn't allow sorting by notes
		sorter.setSortable(7, false);
		
		// Changes the sorter for units sent
		sorter.setComparator(4, new Comparator<Army>() {
			
			public int compare(Army a1, Army a2) {
				
				int amount1 = 0, amount2 = 0;
				
				for (Troop t : a1.getTropas())
					if (t.getQuantity() > 0) amount1++;
				
				for (Troop t : a2.getTropas())
					if (t.getQuantity() > 0) amount2++;
				
				// The smallest are the ones with least different units
				if (amount1 > amount2)  return 1;
				else if (amount2 > amount1) return -1;
				else if (amount1 == 0 && amount2 == 0) return 0;
				else {
					
					// For alerts with same different units, we compare the amount of every individual unit
					for (Troop t : a1.getTropas())
						// O segundo mapa tem a unidade, com menos quantidade
						if (a2.contains(t.getName()) && a2.getQuantidade(t.getUnit()) < t.getQuantity()) 
							return 1;
						// Tem, com mais quantidade
						else if (a2.contains(t.getName()) && a2.getQuantidade(t.getUnit()) > t.getQuantity()) 
							return -1;
					
				}
						
				return 0;
			}
		});
		
		sorter.addRowSorterListener(new RowSorterListener() {
			@Override
			public void sorterChanged(RowSorterEvent e) {
				if (e.getType() == Type.SORT_ORDER_CHANGED) {
					SortKey order = getRowSorter().getSortKeys().get(0);
					
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					AlertManager.getInstance().setConfig("sort_order", new JSONObject(gson.toJson(order)));
				}
			}
		});
		
		setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		
		// Getting the saved order
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JSONObject json = (JSONObject) AlertManager.getInstance().getConfig("sort_order", null);
			SortKey order = gson.fromJson(json.toString(), SortKey.class);
			
			sorter.toggleSortOrder(order.getColumn());
			if (order.getSortOrder() == SortOrder.DESCENDING)
				sorter.toggleSortOrder(order.getColumn());
		} catch (NullPointerException e) {
			sorter.toggleSortOrder(5);
		}
	}
	
	/**
	 * Creates a mouse listener that, when clicked over a note, opens a Dialog with the contents of
	 * the note to be edited.
	 */
	private void addNoteClickListener() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    if (e.getClickCount() == 2) {
			    	
			    	AlertTable table = (AlertTable) e.getSource();
			    	
			    	int row = table.getSelectedRow();
				    int column = table.getSelectedColumn();
			    	
				    if (table.convertColumnIndexToModel(column) == 7) {
					    Rectangle cell = table.getCellRect(row, column, false);
					    
					    Point point = new Point((int) cell.getCenterX() + table.getLocationOnScreen().x, 
					    		(int) cell.getCenterY() + table.getLocationOnScreen().y);
					    
					    Alert a = (Alert) table.getValueAt(row, -1);
					    
				    	new AlertNoteEditor(a, point);
				    }
		    		
			    }
			}
		});
		
	}
	
	/**
	 * Muda o Renderer do Header, pra ficar semelhante ao do resto do programa
	 */
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
	
	public void positionColumns() {
		int[] default_order = new int[] { 0, 7, 1, 8, 2, 3, 4, 5, 6 };
		int[] default_widths = new int[] { 279, 76, 95, 144, 193, 193, 185, 217, 206 };
		
		JSONArray order, widths;
		
		order = (JSONArray) AlertManager.getInstance().getConfig("column_order", new JSONArray(default_order));
		widths = (JSONArray) AlertManager.getInstance().getConfig("column_width", new JSONArray(default_widths));
		
		TableColumn column[] = new TableColumn[order.length()];
		
		// Reordering the columns
		for (int i = 0; i < column.length; i++)
			 column[i] = columnModel.getColumn(order.getInt(i));
		 
		while (columnModel.getColumnCount() > 0) 
			 columnModel.removeColumn(columnModel.getColumn(0));
		 
		for (int i = 0; i < column.length; i++)
			 columnModel.addColumn(column[i]);
		
		// Putting the correct widths
		for (int i = 0; i < widths.length(); i++)
			getColumnModel().getColumn(i).setPreferredWidth(widths.getInt(i));
					
		getColumnModel().addColumnModelListener(new TableColumnModelListener() {
			
			public void columnSelectionChanged(ListSelectionEvent e) {}
			
			public void columnRemoved(TableColumnModelEvent e) {}
			
			@Override
			public void columnMoved(TableColumnModelEvent e) {
				
				int[] order = new int[getColumnModel().getColumnCount()];
				
				for (int i = 0; i < order.length; i++)
					order[i] = getColumnModel().getColumn(i).getModelIndex();
				
				AlertManager.getInstance().setConfig("column_order", new JSONArray(order));
				
				
			}
			
			public void columnMarginChanged(ChangeEvent e) {
				
				int[] width = new int[getColumnModel().getColumnCount()];
				
				for (int i = 0; i < width.length; i++)
					width[i] = getColumnModel().getColumn(i).getPreferredWidth();
				
				AlertManager.getInstance().setConfig("column_width", new JSONArray(width));
			}
			
			@Override
			public void columnAdded(TableColumnModelEvent e) {}
		});
	}
	
	/**
	 * Updates the table when there has been a change
	 */
	public void changedAlert() {
		((AlertTableModel) getModel()).fireTableDataChanged();
		getRowSorter().allRowsChanged();
		repaint();
	}
	
	public void changedAlert(int row) {
		((AlertTableModel) getModel()).fireTableRowsUpdated(row, row);
		repaint();
	}
	
	public void selectAlert(Alert a) {
		if (alerts.contains(a)) 
			setRowSelectionInterval(alerts.indexOf(a), alerts.indexOf(a));
	}
	
	public void addAlert(Alert a) {
		if (!alerts.contains(a)) {
			alerts.add(a);
			changedAlert();
		}
	}
	
	public void editAlert(int row, Alert a) {
		alerts.remove(row);
		alerts.add(row, a);
		changedAlert(row);
	}
	
	public void removeAlert(int row) {
		int selected = getSelectedRow();
		clearSelection();
		getRowSorter().rowsDeleted(row, row);
		try {
			setRowSelectionInterval(selected - 1, selected - 1);
		} catch (Exception e) {
			clearSelection();
		}
		alerts.remove(row);
		changedAlert();
	}
	
	public void removePast(Alert a) {
		if (!(boolean) AlertManager.getInstance().getConfig("show_past", false))
			if (alerts.contains(a))
				removeAlert(alerts.indexOf(a));
	}
	
	public void managePast() {
		if (!(boolean) AlertManager.getInstance().getConfig("show_past", false))
			for (Alert a : AlertManager.getInstance().getPastAlertList())
				removePast(a);
		else
			for (Alert a : AlertManager.getInstance().getPastAlertList())
				addAlert(a);
	}
	
	public void rescheduledAlert(Alert a) {
		oldRows.remove(a);
		changedAlert();
	}
	
	/**
	 * Retorna o alerta que está na posição <code>row</code>
	 * @param row do alerta
	 * @return alerta na posição passada
	 */
	protected Alert getAlert(int row) {
		return alerts.get(row);
	}
	
	/**
	 * Renderer para a nota. Possui um desenho representativo, com um tooltip explicativo
	 */
	private class NotaCellRenderer extends CustomCellRenderer {
		public Component getTableCellRendererComponent (final JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			ImageIcon i = new ImageIcon(Imagens.getImage("edit.png"));
			
			Component cell = super.getTableCellRendererComponent(
					   table, obj, isSelected, hasFocus, row, column);
			
			JLabel label = new JLabel(i);
			label.setBackground(cell.getBackground());
			label.setOpaque(true);
			
			label.setToolTipText("Clique duas vezes para ver e editar a nota");
			
			return label;
		}
	}
	
	/**
	 * Renderer para o horário do alerta. Possui um simples DateFormat.
	 */
	private class DateCellRenderer extends CustomCellRenderer {
		public Component getTableCellRendererComponent (final JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			String date = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format((Date) obj);
			
			Component cell = super.getTableCellRendererComponent(
					   table, date, isSelected, hasFocus, row, column);
			
			JPanel panel = new JPanel();
			
			JLabel time = new JLabel(date.substring(0, 8));
			time.setFont(time.getFont().deriveFont((float) 14.0));
			
			JLabel datelbl = new JLabel(date.substring(9));
			datelbl.setForeground(Color.gray);
			
			panel.add(time);
			panel.add(datelbl);
			panel.setBackground(cell.getBackground());
			
			return panel;
		}
	}
	
	private class RescheduleCellRenderer extends CustomCellRenderer {
		
		public Component getTableCellRendererComponent (final JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Alert a = (Alert) getValueAt(row, -1);
			
			if (((Date) obj).after(new Date()) || a.getRepete() == 0)
				return new DateCellRenderer().getTableCellRendererComponent(
						table, obj, isSelected, hasFocus, row, column);
			
			Component cell = new RescheduleCellEditor()
				.getTableCellEditorComponent(table, obj, isSelected, row, column);
			
			cell.setBackground( new DateCellRenderer().getTableCellRendererComponent(
						table, obj, isSelected, hasFocus, row, column).getBackground());
			
			return cell;
		}
		
	}
	
	private class RescheduleCellEditor extends AbstractCellEditor implements TableCellEditor {

		private TWSimpleButton button;
		private TimeFormattedJLabel label;
		
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, 
					boolean isSelected, final int row, int column) {
				
				Alert a = (Alert) getValueAt(row, -1);
				
				if (a.getRepete() == null)
					return new CustomCellRenderer().getTableCellRendererComponent(
							   table, null, true, true, row, column);
				
				Component cell = new CustomCellRenderer().getTableCellRendererComponent(
						   table, value, true, true, row, column);
				
				label = new TimeFormattedJLabel(false);
				
				button = new TWSimpleButton("Remarcar");
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						Alert a = (Alert) getValueAt(row, -1);
						AlertManager.getInstance().rescheduleAlert(a);
						fireEditingStopped();
					}
				});
				
				JPanel panel = new JPanel(); 
				panel.add(button);
				panel.add(label);
				panel.setBackground(cell.getBackground());
				
				label.setDate(new Date(new Date().getTime() + a.getRepete()));
				
				if (!oldRows.contains(a))
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							changedAlert(row);
						}
					}, (Date) value, 1000);
				
				if (!oldRows.contains(a))
					oldRows.add(a);
				
				return panel;
		    }

		    @Override
		    public Object getCellEditorValue() {
		        return label.getDate();
		    }

		    @Override
		    public boolean isCellEditable(EventObject anEvent) {
		        return true;
		    }

		    @Override
		    public boolean shouldSelectCell(EventObject anEvent) {
		        return true;
		    }
	}
	
	/**
	 * Renderer para o tempo de repetição do alerta. Faz uma formatação do horário, se houver.
	 * Caso não haja, imprime cinco hífens
	 */
	private class TimeCellRenderer extends CustomCellRenderer {
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			long value = (long) obj;
			String time;
			
			if (value > 0) {
			
				TimeFormattedJLabel label = new TimeFormattedJLabel(false);
				label.setTime(value);
				time = label.getText();
				
			} else {
				time = "-----";
				setHorizontalAlignment(JLabel.CENTER);
			}
			
			Component cell = super.getTableCellRendererComponent(
					   table, time, isSelected, hasFocus, row, column);
			
			return cell;
		}
	}
	
	/**
	 * Renderer para a lista de tropas enviadas. Caso haja 3 ou menos tipos de tropas, imprime todas.
	 * Caso haja mais, imprime duas unidades, seguido de um sinal de que há mais. Possui um tooltip indicando
	 * todas as unidades presentes.
	 */
	private class TropaCellRenderer extends CustomCellRenderer {
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Army army = (Army) obj;
			
			String escrita = "<html>";
			String tooltip = "<html>";
			int lines = 0;
			
			if (army == null) {
				setHorizontalAlignment(JLabel.CENTER);
				setToolTipText(null);
				return super.getTableCellRendererComponent(
						   table, obj, isSelected, hasFocus, row, column);
			}
				
			for (Troop t : army.getTropas())
				if (t.getQuantity() > 0) {
					tooltip += t.getPrettyName() + ": " + t.getQuantity() +"<br>";
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
				setToolTipText(tooltip);
			
			return cell;
		}
	}
	
	/**
	 * Renderer padrão para todas as células. Define a cor de fundo da célula com base em
	 * sua fileira. Caso o objeto seja nulo, imprime 5 hífens.
	 */
	private class CustomCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component cell;
			
			if (obj == null)
				cell = super.getTableCellRendererComponent(
					   table, "-----", isSelected, hasFocus, row, column);
			else
				cell = super.getTableCellRendererComponent(
						   table, obj, isSelected, hasFocus, row, column);
			
			((JComponent) cell).setBorder(new EmptyBorder(0, 5, 0, 5));
			
			Color color;
			if (((Alert) table.getValueAt(row, -1)).isPast())
				color = Cores.getAlternar(row % 2).darker();
			else
				color = Cores.getAlternar(row % 2);
			
			if (isSelected) {
				if (((Alert) table.getValueAt(row, -1)).isPast())
					cell.setBackground(Cores.SEPARAR_CLARO.darker());
				else
					cell.setBackground(Cores.SEPARAR_CLARO);
			} else
				cell.setBackground(color);
			
			return cell;
		}
		
	}
	
	/**
	 * Modelo para a tabela.
	 */
	protected class AlertTableModel extends AbstractTableModel implements TableModel  {
		
		protected AlertTableModel() {}
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {}

		@Override
		public Class<?> getColumnClass(int column) {
			switch(column) {
				case 0: return String.class;
				case 1: return Alert.Tipo.class;
				case 2: return Alert.Aldeia.class;
				case 3: return Alert.Aldeia.class;
				case 4: return Army.class;
				case 5: return Date.class;
				case 6: return Long.class;
				case 7: return String.class;
				case 8 : return String.class;
				default: return null;
			}
		}

		@Override
		public int getColumnCount() {
			return 9;
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
				case 6: return "Intervalo";
				case 7: return "Notas";
				case 8: return "Mundo";
				default: return null;
			}	
		}

		@Override
		public int getRowCount() {
			return alerts.size();
		}

		/**
		 * Para valores entre 0 e 7 de coluna, retorna uma propriedade do alerta da linha correspondente.
		 * Para qualquer outro valor de coluna, retorna o alerta em si.
		 */
		public Object getValueAt(int row, int column) {
			switch(column) {
				case 0: return (alerts.get(row).getNome() != null) ? alerts.get(row).getNome() : "";
				case 1: return (alerts.get(row).getTipo() != null) ? alerts.get(row).getTipo() : Tipo.Geral;
				case 2: return alerts.get(row).getOrigem();
				case 3: return alerts.get(row).getDestino();
				case 4: return alerts.get(row).getArmy();
				case 5: return alerts.get(row).getHorário();
				case 6: return (alerts.get(row).getRepete() != null) ? alerts.get(row).getRepete() : 0;
				case 7: return (alerts.get(row).getNotas() != null) ? alerts.get(row).getNotas() : "";
				case 8: return alerts.get(row).getWorld().getPrettyName();
				default: return alerts.get(row);
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			if (column == 5 && oldRows.contains(getValueAt(row, -1)))
				return true;
			else
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
