package io.github.avatarhurden.tribalwarsengine.components;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class TroopLevelComboBox extends JComboBox<Integer>{
	
	public TroopLevelComboBox(int maxLevel) {
		
		for (int i = 1; i <= maxLevel; i++)
			addItem(i);
		
		// Cria um renderer para set usado no combox, centralizando o texto
		ListCellRenderer<Object> renderer = new DefaultListCellRenderer();
		((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel) renderer).setOpaque(true);

		setRenderer(renderer);
		setOpaque(false);

		// Zera a largura do botão
		setUI(new BasicComboBoxUI() {
			@SuppressWarnings("serial")
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
		
	}
	
}
