package property_classes;

import javax.swing.JPanel;

import custom_components.CoordenadaPanel;

public class Property_Coordenada implements Property {

	private String name;
	
	private CoordenadaPanel coordenadaPanel;
	
	private int x, y;
	
	public Property_Coordenada(String nome, int x, int y) {
		
		name = nome;
		this.x = x;
		this.y = y;
		
	}
	
	@SuppressWarnings("serial")
	public JPanel makeEditDialogPanel(JPanel panel, final OnChange change) {
		
		coordenadaPanel = new CoordenadaPanel(name) {
			public void go() {
				change.run();
			}
		};
		
		return coordenadaPanel;
		
	}

	public String getName() {
		return name;
	}
	
	
	public String getValueName() {
		
		return x+"|"+y;
		
	}

	
	public void setValue() {
		
		x = coordenadaPanel.getCoordenadaX();
		y = coordenadaPanel.getCoordenadaY();
		
	}

}
