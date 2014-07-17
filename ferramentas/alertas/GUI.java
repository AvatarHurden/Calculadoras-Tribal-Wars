package alertas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import alertas.Alert.Aldeia;
import alertas.Alert.Tipo;
import custom_components.Ferramenta;
import database.Cores;
import database.Unidade;

public class GUI extends Ferramenta {
	
	public GUI() {
		
		super("Alertas");
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 400 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		String[] nomes = {"Nome", "Tipo", "Origem", "Destino", "Tropas", "Horário", "Repete" };
		
		Alert[] data = new Alert[10];
		
		for (int i = 0; i < 10; i++) {
			
			Alert alerta = new Alert();
			alerta.setNome("Nome"+i);
			alerta.setTipo(Tipo.values()[i % 3]);
			alerta.setOrigem(new Aldeia(nomes[2]+i, i*111, i*55));
			alerta.setDestino(new Aldeia(nomes[3]+i, i*11, i*555));
			
			Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
			for (Unidade u : Unidade.values())
				map.put(u, (int) (Math.random()*100 + 1));
			alerta.setTropas(map);
			
			Date now = new Date();
			System.out.println(now);
			
			alerta.setHorário(new Date(now.getTime()+i*1000));
			
			alerta.setRepete(new Date(now.getTime()));
			
			data[i] = alerta;
			
		}
		
		JTable table = new JTable(new test(data));
		
		table.setAutoCreateRowSorter(true);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		
		//table.getColumn();
		
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800,500));
		table.setFillsViewportHeight(true);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		add(scrollPane, c);
		
	}
	private class CustomCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent (JTable table, 
				Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component cell = super.getTableCellRendererComponent(
					   table, obj, isSelected, hasFocus, row, column);
			
			Border border = (Border) ((JComponent) cell).getBorder();
			
			((JComponent) cell).setBorder(new EmptyBorder(border.getBorderInsets(cell)));
			
			if (isSelected)
				cell.setBackground(Cores.FUNDO_ESCURO);
			else if (row % 2 == 0)
				cell.setBackground(Cores.ALTERNAR_CLARO);
			else
				cell.setBackground(Cores.ALTERNAR_ESCURO);
			
			return cell;
			
		}
		
	}
	
	private class test extends AbstractTableModel implements TableModel  {
		
		private Alert[] alerts;
		
		protected test(Alert[] alerts) {
			this.alerts = alerts;
		}
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Class<?> getColumnClass(int column) {
			
			switch(column) {
			case 0: return alerts[0].getNome().getClass();
			case 1: return alerts[0].getTipo().getClass();
			case 2: return alerts[0].getOrigem().getClass();
			case 3: return alerts[0].getDestino().getClass();
			case 4: return alerts[0].getTropas().getClass();
			case 5: return alerts[0].getHorário().getClass();
			case 6: return alerts[0].getRepete().getClass();
			default: return null;
		}
			
		}

		@Override
		public int getColumnCount() {
			return 7;
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
				default: return null;
			}
			
		}

		@Override
		public int getRowCount() {
			return alerts.length;
		}

		@Override
		public Object getValueAt(int row, int column) {
			
			switch(column) {
				case 0: return alerts[row].getNome();
				case 1: return alerts[row].getTipo();
				case 2: return alerts[row].getOrigem();
				case 3: return alerts[row].getDestino();
				case 4: return alerts[row].getTropas();
				case 5: return alerts[row].getHorário();
				case 6: return alerts[row].getRepete();
				default: return null;
			}
			
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
