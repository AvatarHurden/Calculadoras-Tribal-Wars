package simulador;

import javax.swing.JPanel;

public class StatInsertion extends JPanel{

	// Enum com os tipos diferentes que o panel pode possuir
	// Eles diferem na presença ou não de milícia e nas informações adicionais
	// que podem receber
	private enum Tipo {
		atacante, defensor;
	}
	
	Tipo tipo;
	
	/**
	 * @param tipo Se o panel é de atacante ou defensor
	 */
	public StatInsertion(Tipo tipo) {
		
		this.tipo = tipo;
		
		System.out.println("hello");
		
	}
	
}
