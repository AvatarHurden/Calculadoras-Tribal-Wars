package simulador;

import javax.swing.JPanel;

public class StatInsertion extends JPanel{

	// Enum com os tipos diferentes que o panel pode possuir
	// Eles diferem na presen�a ou n�o de mil�cia e nas informa��es adicionais
	// que podem receber
	private enum Tipo {
		atacante, defensor;
	}
	
	Tipo tipo;
	
	/**
	 * @param tipo Se o panel � de atacante ou defensor
	 */
	public StatInsertion(Tipo tipo) {
		
		this.tipo = tipo;
		
		System.out.println("hello");
		
	}
	
}
