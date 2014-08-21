package io.github.avatarhurden.tribalwarsengine.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;

import database.Cores;

@SuppressWarnings("serial")
public class TroopLevelComboBox extends JComboBox<Integer>{
	
	public TroopLevelComboBox(int maxLevel) {
		
		setRenderer(new TroopComboBoxRenderer());
		
		for (int i = 1; i <= maxLevel; i++)
			addItem(i);
		
		UIManager.put("ComboBox.selectionBackground", Cores.FUNDO_ESCURO);
		UIManager.put("ComboBox.background", Cores.FUNDO_CLARO);
		
		// Zera a largura do botão
		setUI(new BasicComboBoxUI() {
			@Override
			protected JButton createArrowButton() {
				return new JButton() {
					@Override
					public int getWidth() {
						return 0;
					}
				};
			}
		});
		
		setOpaque(true);

	}
	
	private class TroopComboBoxRenderer extends JLabel implements ListCellRenderer<Integer> {
		
		private TroopComboBoxRenderer() {}
		
		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Integer value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
            setText(String.valueOf(value));
            setForeground(Color.DARK_GRAY);
			
			if (isSelected)
				setBackground(Cores.FUNDO_ESCURO);
			else
				setBackground(Cores.FUNDO_CLARO);
			
			setHorizontalAlignment(SwingConstants.CENTER);
			setOpaque(true);
			
			return this;
		}
	}
	
}
