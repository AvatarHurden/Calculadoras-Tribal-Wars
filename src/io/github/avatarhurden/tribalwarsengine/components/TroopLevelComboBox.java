package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;

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

@SuppressWarnings("serial")
public class TroopLevelComboBox extends JComboBox<Integer>{
	
	Color cor;
	
	public TroopLevelComboBox(int maxLevel, Color cor) {
		
		this.cor = cor;
		setRenderer(new TroopComboBoxRenderer());
		
		for (int i = 1; i <= maxLevel; i++)
			addItem(i);
		
		UIManager.put("ComboBox.selectionBackground", Cores.FUNDO_ESCURO);
		UIManager.put("ComboBox.background", cor);
		
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
		
		setBackground(cor);
		setOpaque(true);

	}
	
	private class TroopComboBoxRenderer extends JLabel implements ListCellRenderer<Integer> {
		
		private TroopComboBoxRenderer() {
			setHorizontalAlignment(SwingConstants.CENTER);
			setOpaque(true);
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Integer value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
            setText(String.valueOf(value));
            setForeground(Color.DARK_GRAY);
			
			if (isSelected)
				setBackground(Cores.FUNDO_ESCURO);
			else
				setBackground(cor);
			
			return this;
		}
	
	}
	
}
