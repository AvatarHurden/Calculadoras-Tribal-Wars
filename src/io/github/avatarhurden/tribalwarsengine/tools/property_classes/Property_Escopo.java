package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import config.Mundo_Reader;
import database.Cores;
import database.Mundo;

/**
 * O escopo do objeto, podendo ser global ou apenas um (ou mais) mundos.
 * 
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class Property_Escopo implements Property {
	
	private List<Mundo> mundos = new ArrayList<Mundo>();
	private CheckBoxList selectionList;
	
	private JPanel global, escolha;
	boolean isGlobal;
	
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
	
	public JPanel makeEditDialogPanel(JPanel panel, final OnChange change) {
		
		selectionList = new CheckBoxList(mundos, change);
		
		int width = selectionList.getPreferredSize().width;
		((GridBagLayout)panel.getLayout()).columnWidths = new int[] {width/2+5, width/2+5};
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		
		// Add "escopo" label
		JLabel label = new JLabel("Mundos em que é visível");

		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		c.insets = new Insets(5, 5, 5, 0);
		panel.add(label, c);
		
		
		// Add "Global" button
		makeGlobalButton(change);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		panel.add(global, c);
		
		
		// Add "Escolha Mundo" Button
		makeEscolhaButton(change);
		
		c.gridx++;
		panel.add(escolha, c);
		
		
		// Add selectionList
	
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		panel.add(selectionList, c);
		
		// Initial formatting
		
		if (mundos.isEmpty()) {
			selectionList.setVisible(false);
			global.setBackground(Cores.FUNDO_ESCURO);
			global.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		} else {
			escolha.setBackground(Cores.FUNDO_ESCURO);
			escolha.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		}
			
		return panel;
	}
	
	private void makeGlobalButton(final OnChange change) {
		
		global = new JPanel();
		
		global.add(new JLabel("Todos"));
		
		global.setBackground(Cores.FUNDO_CLARO);
		global.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		
		global.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {}
			
			public void mousePressed(MouseEvent arg0) {}
			
			public void mouseExited(MouseEvent arg0) {}
			
			public void mouseEntered(MouseEvent arg0) {}
			
			public void mouseClicked(MouseEvent e) {
				
				((JPanel) e.getSource()).setBackground(Cores.FUNDO_ESCURO);
				((JPanel) e.getSource()).setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				
				escolha.setBackground(Cores.FUNDO_CLARO);
				escolha.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				
				selectionList.setVisible(false);
				
				isGlobal = true;
				
				change.run();
				
			}
		});
		
	}
	
	private void makeEscolhaButton(final OnChange change) {
		
		escolha = new JPanel();
		
		escolha.add(new JLabel("Escolher Mundos"));
		
		escolha.setBackground(Cores.FUNDO_CLARO);
		escolha.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		
		escolha.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {}
			
			public void mousePressed(MouseEvent arg0) {}
			
			public void mouseExited(MouseEvent arg0) {}
			
			public void mouseEntered(MouseEvent arg0) {}
			
			public void mouseClicked(MouseEvent e) {
				
				((JPanel) e.getSource()).setBackground(Cores.FUNDO_ESCURO);
				((JPanel) e.getSource()).setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				
				global.setBackground(Cores.FUNDO_CLARO);
				global.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				
				selectionList.setVisible(true);
				
				isGlobal = false;
				
				change.run();
				
			}
		});
		
	}

	/**
	 * Não se aplica ao escopo
	 */
	public String getName() {
		return null;
	}

	
	public String getValueName() {
		return null;
	}
	
	public void setValue() {
		
		if (isGlobal)
			mundos = new ArrayList<Mundo>();
		else
			mundos = selectionList.getSelecionados();
		
	}
	
	/**
	 * Retorna a lista de mundos no qual está disponível.
	 * Caso seja de alcance global, a lista será todos os mundos
	 */
	public List<Mundo> getListMundos() {
		
		return mundos;
		
	}
	
	/**
	 * Possui todos os mundos disponíveis, salvando quais estão selecionados ou não
	 * 
	 * @author Arthur
	 *
	 */
	private class CheckBoxList extends JPanel {
		
		private OnChange change;
		
		private List<Mundo> mundosSelecionados = new ArrayList<Mundo>();
		private List<JCheckBox> chkboxList = new ArrayList<JCheckBox>();
		private List<JPanel> panelList = new ArrayList<JPanel>();
		private List<Mundo> mundoList = new ArrayList<Mundo>();

		
		private CheckBoxList(List<Mundo> selected, OnChange onChange) {
			
			change = onChange;
			
			mundosSelecionados = selected;
			
			setOpaque(false);
			setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridy = 0;
			c.gridx = 0;
			
			c.gridwidth = 2;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(5, 5, 5, 5);
			add(makeToggleButton(), c);
			
			int maxY = Mundo_Reader.getMundoList().size()/2+1;
			
			c.insets = new Insets(0, 0, 5, 5);
			c.gridy++;
			c.gridwidth = 1;
			for (Mundo m : Mundo_Reader.getMundoList()) {
				
				if (c.gridy >= maxY) {
					c.gridy = 1;
					c.gridx++;
					c.insets = new Insets(0, 0, 5, 0);
				}
			
				add(makeMundoPanel(m, selected.contains(m)), c);
				c.gridy++;
			}
			
		}
		
		private JPanel makeMundoPanel(final Mundo m, boolean selected) {
			
			final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			panel.setBackground(Cores.FUNDO_CLARO);
			panel.setBorder(new LineBorder(selected ? Cores.SEPARAR_CLARO : Cores.FUNDO_CLARO));
			
			final JCheckBox checkBox = new JCheckBox();
			checkBox.setOpaque(false);
			checkBox.setSelected(selected);
			
			checkBox.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					changeSelected(m, panel, ((JCheckBox) e.getSource()).isSelected());
					change.run();
					
				}
			});
			
			panel.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent arg0) {}
				
				public void mousePressed(MouseEvent arg0) {}
				
				public void mouseExited(MouseEvent arg0) {}
				
				public void mouseEntered(MouseEvent arg0) {}
				
				public void mouseClicked(MouseEvent arg0) {
					
					checkBox.doClick();
					
				}
			});
			
			chkboxList.add(checkBox);
			panelList.add(panel);
			mundoList.add(m);
			
			panel.add(checkBox);
			panel.add(new JLabel(m.toString()));

			panel.setPreferredSize(new Dimension(100, panel.getPreferredSize().height));
			
			return panel;
			
		}
		
		private void changeSelected(Mundo m, JPanel panel, boolean active) {
			
			if (active) {
				panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
				mundosSelecionados.add(m);
			} else {
				panel.setBorder(new LineBorder(Cores.FUNDO_CLARO));
				mundosSelecionados.remove(m);
			}
			
		}
		
		private JButton	makeToggleButton() {
			
			JButton button = new TWSimpleButton("Inverter seleções");
			
			button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					for (JCheckBox c : chkboxList) {
						c.setSelected(!c.isSelected());
						changeSelected(mundoList.get(chkboxList.indexOf(c)),panelList.get(chkboxList.indexOf(c)), c.isSelected());
					}
					
					change.run();
				}
			});
			
			return button;
			
		}
		
		
		
		private List<Mundo> getSelecionados() {
			return mundosSelecionados;
		}
		
		
	}
	

}
