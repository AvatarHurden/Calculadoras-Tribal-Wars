package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

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

import org.json.JSONObject;

import database.Cores;

public class Scope{

	private ArrayList<String> selectedWorlds;
	private boolean isGlobal;
	
	public Scope() {
		selectedWorlds = new ArrayList<String>();
		isGlobal = true;
	}
	
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	public boolean isGlobal() {
		return isGlobal;
	}
	
	public boolean contains(World world) {
		return selectedWorlds.contains(world.toString()) || isGlobal;
	}
	
	/**
	 * Possui todos os mundos disponíveis, salvando quais estão selecionados ou não
	 * 
	 * @author Arthur
	 *
	 */
	@SuppressWarnings("serial")
	public class ScopeSelectionPanel extends JPanel {
		
		private OnChange change;
		
		private List<JCheckBox> chkboxList = new ArrayList<JCheckBox>();
		private List<JPanel> panelList = new ArrayList<JPanel>();
		private List<World> mundoList;

		public ScopeSelectionPanel(OnChange onChange) {
			
			mundoList = new ArrayList<>(WorldManager.get().getList());
			change = onChange;
			
			setOpaque(false);
			setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridy = 0;
			c.gridx = 0;
			
			c.gridwidth = 2;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(5, 5, 5, 5);
			add(makeToggleButton(), c);
			
			int maxY = WorldManager.get().getList().size()/2+1;
			
			c.insets = new Insets(0, 0, 5, 5);
			c.gridy++;
			c.gridwidth = 1;
			for (World m : mundoList) {
				
				if (c.gridy >= maxY) {
					c.gridy = 1;
					c.gridx++;
					c.insets = new Insets(0, 0, 5, 0);
				}
				add(makeMundoPanel(m.toString(), selectedWorlds.contains(m.toString())), c);
				c.gridy++;
			}
		}
		
		private JPanel makeMundoPanel(final String m, boolean selected) {
			
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
			
			panel.add(checkBox);
			panel.add(new JLabel(m.toString()));

			panel.setPreferredSize(new Dimension(100, panel.getPreferredSize().height));
			
			return panel;
		}
		
		private void changeSelected(String m, JPanel panel, boolean active) {
			
			if (active) {
				panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
				selectedWorlds.add(m);
			} else {
				panel.setBorder(new LineBorder(Cores.FUNDO_CLARO));
				selectedWorlds.remove(m);
			}
		}
		
		private JButton	makeToggleButton() {
			
			JButton button = new TWSimpleButton("Inverter seleções");
			
			button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					for (JCheckBox c : chkboxList) {
						c.setSelected(!c.isSelected());
						changeSelected(mundoList.get(chkboxList.indexOf(c)).toString(),
								panelList.get(chkboxList.indexOf(c)), c.isSelected());
					}
					
					change.run();
				}
			});
			
			return button;
		}
		
	}
	
	public static boolean isScopeJson(JSONObject json) {
		return json.has("isGlobal");
	}
	
}
