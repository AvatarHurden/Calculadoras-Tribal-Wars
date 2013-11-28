package simulador;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.Unidade;

public class ResultDisplay extends JPanel {

	// Enum com os tipos diferentes que o panel pode possuir
		// Eles diferem na presen�a ou n�o de mil�cia e nas informa��es adicionais
		// que podem receber
		private enum Tipo {
			atacante, defensor;
		}
		
		private Tipo tipo;
		
		private Map<Unidade, JTextField> mapQuantidades = new HashMap<Unidade, JTextField>();
	
		private JLabel muralha, edif�cio;
		
		public ResultDisplay(Tipo tipo) {
			
			this.tipo = tipo;
			
			
		}
}
