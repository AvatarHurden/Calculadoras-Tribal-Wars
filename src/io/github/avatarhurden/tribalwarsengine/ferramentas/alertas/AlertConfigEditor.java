package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

public class AlertConfigEditor extends JDialog{

	private List<PropertyPanel> properties;
	private JSONObject config;
	
	public AlertConfigEditor() {
		
		config = AlertManager.getInstance().getConfig();
		properties = new ArrayList<PropertyPanel>();
		
		getContentPane().setBackground(Cores.FUNDO_CLARO);
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		
		c.gridwidth = 3;
		add(makeShowPastPanel(), c);
		
		c.gridy++;
		add(makeShowWorldPanel(), c);
		
		c.gridy++;
		add(makeDeletePastPanel(), c);
		
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy++;
		add(makeSaveButton(), c);
		
		c.gridx++;
		add(makeDefaultButton(), c);
		
		c.gridx++;
		add(makeCancelButton(), c);
		
		for (PropertyPanel p : properties)
			p.setValue();

		setModal(true);
		pack();
		setLocation(getWindowLocation());
		setVisible(true);
		
		saveWindowLocation();
	}
	
	private Point getWindowLocation() {
		Point point = new Point();
		
		JSONObject location = config.optJSONObject("location");
		if (location == null)
			location = new JSONObject();
		
		point.x = location.optInt("x", 0);
		point.y = location.optInt("y", 0);
		
		return point;
	}
	
	private void saveWindowLocation() {
		JSONObject location = new JSONObject();
    	location.put("x", getLocation().x);
    	location.put("y", getLocation().y);
    	
    	config.put("location", location);
	}
	
	public JButton makeSaveButton() {
		JButton button = new TWButton("Salvar");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (PropertyPanel p : properties)
					p.save();
				dispose();
			}
		});
		
		return button;
	}
	
	public JButton makeDefaultButton() {
		JButton button = new TWSimpleButton("Restaurar Padrões");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (PropertyPanel p : properties)
					p.setDefault();
			}
		});
		
		return button;
	}
	
	public JButton makeCancelButton() {
	JButton button = new TWSimpleButton("Cancelar");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		return button;	
	}
	
	public PropertyPanel makeShowPastPanel() {
		
		final JCheckBox check = new JCheckBox();
		final JCheckBox pastLoot = new JCheckBox();
		check.setOpaque(false);
		pastLoot.setOpaque(false);
		
		check.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (check.isSelected())
					pastLoot.setSelected(true);
			}
		});
		
		PropertyPanel panel = new PropertyPanel() {
			protected void saveSelf() {
				config.put("show_past", check.isSelected());
				config.put("show_past_loot", check.isSelected());
			}
			
			protected void setValueSelf() {
				check.setSelected(config.optBoolean("show_past", false));
				check.setSelected(config.optBoolean("show_past_loot", true));
			}
			
			protected void setDefaultSelf() {
				check.setSelected(false);
				pastLoot.setSelected(true);
			}

			protected void setGUI() {
				
			}
		};
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		panel.add(check, c);
		c.gridx++;
		panel.add(new JLabel("Manter alertas antigos na tabela"), c);
		
		c.gridy++;
		c.gridx = 0;
		panel.add(pastLoot, c);
		c.gridx++;
		panel.add(new JLabel("Manter alertas antigos de saque na tabela"), c);
		
		properties.add(panel);
		return panel;
	}
	
	public PropertyPanel makeShowWorldPanel() {
		
		PropertyPanel panel = new PropertyPanel() {
			JCheckBox check;
			
			@Override
			protected void setValueSelf() {
				check.setSelected(config.optBoolean("show_only_selected", false));
			}
			
			@Override
			protected void setGUI() {
				check = new JCheckBox();
				check.setOpaque(false);
				
				JPanel selectPanel = new JPanel();
				selectPanel.add(check);
				selectPanel.add(new JLabel("Mostrar apenas alertas do mundo selecionado"));
				
				add(selectPanel);
			}
			
			@Override
			protected void setDefaultSelf() {
				check.setSelected(false);
			}
			
			@Override
			protected void saveSelf() {
				config.put("show_only_selected", check.isSelected());
			}
		};
		
		properties.add(panel);
		return panel;
	}
	
	public PropertyPanel makeDeletePastPanel() {

		final PropertyPanel timePanel = new PropertyPanel() {
			JSpinner spinner;
			
			protected void setValueSelf() {
				spinner.setValue(config.optInt("deletion_time", 24));
			}
			
			protected void setDefaultSelf() {
				spinner.setValue(24);
				enableComponents(this, false);
			}
			
			protected void saveSelf() {
				config.put("deletion_time", spinner.getValue());
			}

			protected void setGUI() {
				spinner = new JSpinner(new SpinnerNumberModel(24, 0, 999, 1));
				
				JPanel childPanel = new JPanel();
				
				childPanel.add(new JLabel("Deletar alertas antigos depois de "));
				childPanel.add(spinner);
				childPanel.add(new JLabel(" horas"));
				
				add(childPanel);
			}
		};
		
		PropertyPanel panel = new PropertyPanel() {
			JCheckBox check;
			
			protected void saveSelf() {
				config.put("delete_past", check.isSelected());
			}
			
			protected void setValueSelf() {
				check.setSelected(config.optBoolean("delete_past", true));
				enableComponents(timePanel, check.isSelected());
			}
			
			protected void setDefaultSelf() {
				check.setSelected(true);
			}

			protected void setGUI() {
				check = new JCheckBox();
				check.setOpaque(false);
				check.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						enableComponents(timePanel, check.isSelected());
					}
				});	
				
				JPanel selectPanel = new JPanel();
				selectPanel.add(check);
				selectPanel.add(new JLabel("Deletar alertas antigos"));
				
				add(selectPanel);
				addChild(timePanel);
			}
		};
		
		properties.add(panel);
		return panel;
	}	
	
	private abstract class PropertyPanel extends JPanel {
		
		List<PropertyPanel> children;
		GridBagConstraints c;
		
		public PropertyPanel() {
			
			children = new ArrayList<PropertyPanel>();
			
			setBackground(Cores.FUNDO_CLARO);
			setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			setLayout(new GridBagLayout());
			
			c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			
			setGUI();
		}
		
		protected void save() {
			saveSelf();
			for (PropertyPanel p : children)
				p.save();
		}
		
		protected abstract void saveSelf();
		
		protected void setValue() {
			setValueSelf();
			for (PropertyPanel p : children)
				p.setValue();
		}
		
		protected abstract void setValueSelf();
		
		protected void setDefault() {
			setDefaultSelf();
			for (PropertyPanel p : children)
				p.setDefault();
		}
		
		protected abstract void setDefaultSelf();
		
		protected abstract void setGUI();
		
		protected void add(JPanel panel) {
			panel.setOpaque(false);

			c.insets = new Insets(0, 0, 0, 0);
			add(panel, c);
			c.gridy++;
		}
		
		protected void addChild(PropertyPanel child) {	
			children.add(child);
			
			child.setBorder(new EmptyBorder(0, 0, 0, 0));
			c.insets = new Insets(0, 35, 5, 0);
			add(child, c);
			c.gridy++;
		}
	}
	
	protected void enableComponents(Container container, boolean isEnabled) {
		 Component[] components = container.getComponents();
	        for (Component component : components) {
	            component.setEnabled(isEnabled);
	            if (component instanceof Container)
	                enableComponents((Container)component, isEnabled);
	        }
	}
}
