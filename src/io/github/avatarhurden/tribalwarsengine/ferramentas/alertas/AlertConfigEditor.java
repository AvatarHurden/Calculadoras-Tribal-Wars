package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		
		add(makeTimeToDeletePanel(), c);
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridy++;
		add(makeSaveButton(), c);
		
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
    	
    	System.out.println(location.toString());
    	config.put("location", location);
	}
	
	public JButton makeSaveButton() {
		
		JButton button = new JButton("Salvar");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (PropertyPanel p : properties)
					p.save();
			}
		});
		
		return button;
	}
	
	public PropertyPanel makeTimeToDeletePanel() {
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
		
		PropertyPanel panel = new PropertyPanel() {

			protected void save() {
				config.put("deletion_time", spinner.getValue());
			}

			protected void setValue() {
				try {
					spinner.setValue(config.get("deletion_time"));
				} catch (Exception e) {
					setDefault();
				}
			}
			
			protected void setDefault() {
				spinner.setValue(1);
			}
		};
		
		panel.add(new JLabel("Deletar avisos antigos depois de "));
		panel.add(spinner);
		panel.add(new JLabel("horas"));
		
		properties.add(panel);
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
}
