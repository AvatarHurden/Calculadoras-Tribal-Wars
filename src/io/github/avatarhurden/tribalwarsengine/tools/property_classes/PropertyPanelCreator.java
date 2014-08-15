package io.github.avatarhurden.tribalwarsengine.tools.property_classes;

import io.github.avatarhurden.tribalwarsengine.enums.SearchSystem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import database.Cores;

public class PropertyPanelCreator extends JPanel{
	
	JSONObject object;
	LinkedHashMap<String, String> keys;
	
	public PropertyPanelCreator(JSONObject object, LinkedHashMap<String, String> keys) {
		this.object = object;
		this.keys = keys;
		
		setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 5, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;

		
		for (String key : keys.keySet()) {
			
			if (!object.has(key))
				continue;
			
			JPanel panel = null;
			
			Object o = object.get(key);
			if (o instanceof Boolean)
				panel = makeBooleanPanel(keys.get(key), (Boolean) o);
			else if (o instanceof Double)
				panel = makeNumberPanel(keys.get(key), (Double) o);
			else if (o instanceof String)
				panel = makeStringPanel(keys.get(key), (String) o);
			else if (o instanceof SearchSystem)
				panel = makeResearchPanel(keys.get(key), (SearchSystem) o);
			else if (o instanceof Escopo)
				panel = makeEscopoPanel((Escopo) o);
			else if (o instanceof EdifíciosMap) 
				panel = makeEdifíciosPanel((EdifíciosMap) o);
			else if (o instanceof TropasMap)
				panel = makeTropasPanel((TropasMap) o);
			
			add(panel, c);
			c.gridy++;
		}
			
		
	}
	
	private JPanel makeBooleanPanel(String name, boolean isTrue) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
		
	}
	
	private JPanel makeNumberPanel(String name, double number) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
	}
	
	private JPanel makeStringPanel(String name, String string) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
	}

	private JPanel makeResearchPanel(String name, SearchSystem string) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
	}
	
	private JPanel makeTropasPanel(TropasMap map) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
	}
	
	private JPanel makeEdifíciosPanel(EdifíciosMap map) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
	}
	
	private JPanel makeEscopoPanel(Escopo escopo) {
		
		JPanel panel = makeDefaultPanel();
		
		return panel;
	}
	
	private JPanel makeDefaultPanel() {
		
		JPanel panel = new JPanel();

        panel.setBackground(Cores.FUNDO_CLARO);

        panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{,};
        layout.rowHeights = new int[]{20};
        layout.columnWeights = new double[]{1, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        panel.setLayout(layout);

        return panel;

	}
	
}
