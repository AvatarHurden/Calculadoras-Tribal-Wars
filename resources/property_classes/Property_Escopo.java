package property_classes;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import config.Mundo_Reader;
import database.Cores;
import database.Mundo;

/**
 * O escopo do objeto, podendo ser global ou apenas um (ou mais) mundos.
 * 
 * @author Arthur
 *
 */
public class Property_Escopo implements Property {
	
	private List<Mundo> mundos = new ArrayList<Mundo>();
	private JTextField txtField;
	private JPanel worldListPanel;
	
	public Property_Escopo(List<Mundo> mundos) {
		
		// Remove todos os mundos que não foram encontrados
		for (Mundo m : mundos)
			if (m != null)
				this.mundos.add(m);
				
	}
	
	public Property_Escopo(Mundo... mundos) {
		
		for (Mundo m : mundos)
			if (m != null)
				this.mundos.add(m);
		
	}
	
	public JPanel makeEditDialogPanel(JPanel panel, OnChange change) {
		
		worldListPanel = new JPanel();
		worldListPanel.setOpaque(false);
		
		JList<Object> selectionList = new JList<Object>();
		selectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selectionList.setListData(Mundo_Reader.getMundoList().toArray());
		
		for (Mundo m : Mundo_Reader.getMundoList().toArray(new Mundo[0]))
			System.out.println(m.toString());
		
		selectionList.setCellRenderer(new CellRenderer());
		selectionList.setOpaque(false);
		
		worldListPanel.add(selectionList);
		
		txtField = new JTextField(mundos.toString());
		
		panel.add(txtField);
		panel.add(worldListPanel);
		
		return panel;
	}

	/**
	 * Não se aplica ao escopo
	 */
	public String getName() {
		return null;
	}

	
	public String getValueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue() {
		
		String[] worlds = txtField.getText().split(" \",\"");
		
		mundos = new ArrayList<Mundo>();
		
		for (String s : worlds)
			if (Mundo_Reader.getMundo(s) != null)			
				mundos.add(Mundo_Reader.getMundo(s));
		
	}
	
	/**
	 * Retorna a lista de mundos no qual está disponível.
	 * Caso seja de alcance global, a lista será todos os mundos
	 */
	public List<Mundo> getListMundos() {

		return mundos;
		
	}
	
	private class CellRenderer extends JLabel implements ListCellRenderer<Object> {
		
		private boolean selected = false;
		
		private CellRenderer() {
			
			setOpaque(true);
			setPreferredSize(new Dimension(100, 30));
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
				
		}
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			
			setText(value.toString());
			add(new JCheckBox());
			
			if (isSelected)
				selected = !selected;
			
			if (selected)
				setBackground(Cores.FUNDO_ESCURO);
			else
				setBackground(Cores.FUNDO_CLARO);
			
			
			return this;
		}	
	}
	
	private class CheckBoxList extends JList<Mundo> {
		
		private List<Mundo> selected = new ArrayList<Mundo>(); 
		
	}
	
	

}
