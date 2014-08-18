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

import config.Mundo_Reader;
import database.Cores;
import database.Mundo;

public class Scope extends ArrayList<Mundo>{

	private boolean isGlobal;
	
	public Scope() {
		super();
	}
	
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	public boolean getGlobal() {
		return isGlobal;
	}
	
	private Scope getThis() {
		return this;
	}
	
	/**
	 * Possui todos os mundos dispon�veis, salvando quais est�o selecionados ou n�o
	 * 
	 * @author Arthur
	 *
	 */
	public class ScopeSelectionPanel extends JPanel {
		
		private OnChange change;
		
		private List<Mundo> mundosSelecionados = new ArrayList<Mundo>();
		private List<JCheckBox> chkboxList = new ArrayList<JCheckBox>();
		private List<JPanel> panelList = new ArrayList<JPanel>();
		private List<Mundo> mundoList = new ArrayList<Mundo>();

		
		public ScopeSelectionPanel(OnChange onChange) {
			
			change = onChange;
			
			mundosSelecionados = getThis();
			
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
			
				add(makeMundoPanel(m, mundosSelecionados.contains(m)), c);
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
			
			JButton button = new TWSimpleButton("Inverter sele��es");
			
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
