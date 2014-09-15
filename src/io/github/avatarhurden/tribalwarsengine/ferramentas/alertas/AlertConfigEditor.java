package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

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
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

public class AlertConfigEditor extends JDialog{

	private Map<String, PropertyPanel> properties;
	private JSONObject config;
	
	public AlertConfigEditor() {
		
		config = AlertManager.getInstance().getConfig();
		properties = new HashMap<String, PropertyPanel>();
		
		getContentPane().setBackground(Cores.FUNDO_CLARO);
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		
		c.gridwidth = 3;
		add(makeDeletePastPanel(), c);
		
		c.gridy++;
//		add(makeTimeToDeletePanel(), c);
		
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy++;
		add(makeSaveButton(), c);
		
		c.gridx++;
		add(makeDefaultButton(), c);
		
		c.gridx++;
		add(makeCancelButton(), c);
		
		for (PropertyPanel p : properties.values())
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
		JButton button = new JButton("Salvar");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (PropertyPanel p : properties.values())
					p.save();
			}
		});
		
		return button;
	}
	
	public JButton makeDefaultButton() {
		JButton button = new JButton("Restaurar Padrões");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (PropertyPanel p : properties.values())
					p.setDefault();
			}
		});
		
		return button;
	}
	
	public JButton makeCancelButton() {
	JButton button = new JButton("Cancelar");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		return button;	
	}
	
	public PropertyPanel makeDeletePastPanel() {

		final JPanel timePanel = new JPanel();
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(24, 0, 999, 1));
		
		final JCheckBox check = new JCheckBox();
		check.setOpaque(false);
		
		check.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				enableComponents(timePanel, check.isSelected());
			}
		});	
		
		PropertyPanel panel = new PropertyPanel() {
			
			protected void save() {
				config.put("delete_past", check.isSelected());
				config.put("deletion_time", spinner.getValue());
			}
			
			protected void setValue() {
				check.setSelected(config.optBoolean("delete_past", true));
				spinner.setValue(config.optInt("deletion_time", 24));
				enableComponents(timePanel, check.isSelected());
			}
			
			protected void setDefault() {
				check.setSelected(true);
				spinner.setValue(24);
			}
			
		};
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		
		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.X_AXIS));
		selectPanel.setOpaque(false);
		
		selectPanel.add(check);
		selectPanel.add(new JLabel("Deletar alertas antigos"));
		
		panel.add(selectPanel, c);
		
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
		timePanel.setOpaque(false);
		
		timePanel.add(Box.createHorizontalStrut(30));
		timePanel.add(new JLabel("Deletar alertas antigos depois de "));
		
		timePanel.add(spinner);
		timePanel.add(new JLabel("horas"));
		
		c.gridy++;
		panel.add(timePanel, c);
		
		properties.put("delete_past", panel);
		return panel;
	}	
	
	private abstract class PropertyPanel extends JPanel {
		
		public PropertyPanel() {
			setBackground(Cores.FUNDO_CLARO);
			setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		}
		
		protected abstract void save();
		
		protected abstract void setValue();
	
		protected abstract void setDefault();
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
