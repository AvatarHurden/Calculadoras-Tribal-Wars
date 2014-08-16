package database;

import java.awt.*;

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
     * Enter a number to get one of the alternating colors. If the number is
     * even, returns the light version. If the number is odd, returns the dark
     * version.
     *
     * @param i
     */
    public static Color getAlternar(int i) {

        if (i % 2 == 0)
            return ALTERNAR_CLARO;
        else
            return ALTERNAR_ESCURO;
    }

    /**
     * Converte uma cor em hexadecimal para RGB
     * O local adequado para colocar esse metodo não seria aqui. Depois eu movo para outro lugar
     *
     * @param colorStr - Uma cor em hex, exemplo: #FFFFFF
     * @return color - Color object
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
