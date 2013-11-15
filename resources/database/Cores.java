package database;

import java.awt.Color;

public class Cores {

	/**
	 * Cor para plano de fundo, versão escura
	 */
	public static final Color FUNDO_ESCURO = new Color(196, 166, 106);
	
	/**
	 * Cor para plano de fundo nas ferramentas
	 */
	public static final Color FUNDO_CLARO = new Color(251, 235, 201);
	
	/**
	 * Cor para separar linhas, versão escura
	 */
	public static final Color SEPARAR_ESCURO = new Color(146, 116, 56);
	
	/**
	 * Cor para separar linhas, versão clara
	 */
	public static final Color SEPARAR_CLARO = new Color(196, 166, 106);
	
	/**
	 * Cor para alternar entre linhas, versão escura
	 */
	public static final Color ALTERNAR_ESCURO = new Color(239, 223, 187);
	
	/**
	 * Cor para alternar entre linhas, versão clara
	 */
	public static final Color ALTERNAR_CLARO = new Color(245, 237, 216);
	
	/**
	 * Enter a number to get one of the alternating colors.
	 * If the number is even, returns the light version.
	 * If the number is odd, returns the dark version.
	 * 
	 * @param i
	 */
	public static Color getAlternar(int i) {
		
		if (i % 2 == 0)
			return ALTERNAR_CLARO;
		else
			return ALTERNAR_ESCURO;
	}
	
}
