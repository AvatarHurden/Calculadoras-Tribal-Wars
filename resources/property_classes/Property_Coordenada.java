package property_classes;

import javax.swing.JPanel;

import custom_components.CoordenadaPanel;

public class Property_Coordenada implements Property {
	
	private CoordenadaPanel coordenadaPanel;
	
	private int x, y;
	
	public Property_Coordenada(int x, int y) {
	
		this.x = x;
		this.y = y;
		
	}
	
	@SuppressWarnings("serial")
	public JPanel makeEditDialogPanel(JPanel panel, final OnChange change) {
		
		coordenadaPanel = new CoordenadaPanel("") {
			public void go() {
				change.run();
			}
		};
		
		return coordenadaPanel;
		
	}

	public String getName() {
		return null;
	}
	
	
	public String getValueName() {
		
		return x+"|"+y;
		
	}

	
	public void setValue() {
		
		x = coordenadaPanel.getCoordenadaX();
		y = coordenadaPanel.getCoordenadaY();
		
	}

}
