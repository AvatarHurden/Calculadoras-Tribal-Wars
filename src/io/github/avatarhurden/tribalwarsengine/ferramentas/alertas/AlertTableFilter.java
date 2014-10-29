package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertTable.AlertTableModel;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class AlertTableFilter{
	
	private AlertTableFilterEditor editor;
	private TableRowSorter<AlertTableModel> sorter;
	
	public AlertTableFilter(Ferramenta parent) {
		
		OnChange onChange = new OnChange() {
			@Override
			public void run() {
//				army.saveValues();
				setFilter();
			}
		};
		
		editor = new AlertTableFilterEditor(parent, onChange);
		
	}
	
	public JPanel getEditor() {
		return editor;
	}
	
	@SuppressWarnings("unchecked")
	public void setSorter(RowSorter<? extends TableModel> rowSorter) {
		this.sorter = (TableRowSorter<AlertTableModel>) rowSorter;
	}
	
	private boolean isSelectedType(Tipo tipo) {
		if (!editor.getFilterType(Tipo.Geral) && !editor.getFilterType(Tipo.Ataque) &&
				!editor.getFilterType(Tipo.Apoio) && !editor.getFilterType(Tipo.Saque))
			return true;
		
		return editor.getFilterType(tipo);
	}
	
	private boolean hasArmy(Army a) {
		boolean res = true;
		
		for (Troop t : editor.getFilterArmy().getTropas())
			if (t.getQuantity() > 0) {
				if (a == null) {
					res = false;
					break;
				} else
					res = a.getQuantidade(t.getName()) > 0;
			}
				
		return res;
	}
	
	private boolean isSelectedWorld(String name) {
		if (editor.getFilterCurrentWorld())
			return name.equals(WorldManager.getSelectedWorld().getPrettyName());
		
		return true;
	}
	
	public void setFilter() {
		
		RowFilter<TableModel, Integer> filter = new RowFilter<TableModel, Integer>() {

			@Override
			public boolean include(
					javax.swing.RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
				boolean toAdd = true;
				
				toAdd &= RowFilter.regexFilter(editor.getFilterName(), 0).include(entry);
				toAdd &= isSelectedType((Tipo) entry.getValue(1));
//				toAdd &= hasArmy((Army) entry.getValue(4));
				toAdd &= isSelectedWorld((String) entry.getValue(8));
				
				return toAdd;
			}
		};
		
		sorter.setRowFilter(filter);
		
	}
	
}