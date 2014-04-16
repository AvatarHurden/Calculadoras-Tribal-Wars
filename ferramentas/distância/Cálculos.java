package distância;

import java.math.BigDecimal;

public class Cálculos {

	/**
	 * Formata o número, em segundos, para representar de forma legível, em
	 * dias, horas, minutos e segundos.
	 * 
	 * @param número
	 *            de segundos
	 * 
	 * @return String com a configuração 0d 0h0m0s
	 */
	public static String format(BigDecimal number) {
		
		BigDecimal dias = number.divideToIntegralValue(new BigDecimal(86400));
		
		BigDecimal horas = number.divideToIntegralValue(new BigDecimal(3600))
				.subtract(dias.multiply(new BigDecimal(24)));
		
		BigDecimal minutos = number.divideToIntegralValue(new BigDecimal(60))
				.subtract(horas.multiply(new BigDecimal(60)))
				.subtract(dias.multiply(new BigDecimal(1440)));
		
		BigDecimal segundos = number
				.subtract(minutos.multiply(new BigDecimal(60)))
				.subtract(horas.multiply(new BigDecimal(3600)))
				.subtract(dias.multiply(new BigDecimal(86400)));

		String s = "";

		// Checar se deve adicionar termos para dia, hora, minuto e segundo
		if (dias.compareTo(BigDecimal.ZERO) == 1)
			s += String.format("%,.0fd ", dias);

		if (horas.compareTo(BigDecimal.ZERO) == 1)
			s += String.format("%.0fh", horas);

		if (minutos.compareTo(BigDecimal.ZERO) == 1)
			s += String.format("%.0fm", minutos);

		if (segundos.compareTo(BigDecimal.ZERO) == 1)
			s += String.format("%.0fs", segundos);

		return s;

	}

	/**
	 * Converte o número formatado utilizando o método "format" para segundos
	 * 
	 * @param string
	 *            formatada
	 * @return tempo em segundos, BigDecimal
	 */
	public static BigDecimal getSeconds(String s) {

		StringBuilder builder = new StringBuilder(s);

		BigDecimal dias, horas, minutos, segundos;

		BigDecimal tempo;

		// Adicionar dias
		if (builder.indexOf("d") != -1) {
			dias = new BigDecimal(builder.substring(0, builder.indexOf("d")));
			builder.delete(0, builder.indexOf("d") + 2);
		} else
			dias = BigDecimal.ZERO;

		// Adicionar horas
		if (builder.indexOf("h") != -1) {
			horas = new BigDecimal(builder.substring(0, builder.indexOf("h")));
			builder.delete(0, builder.indexOf("h") + 1);
		} else
			horas = BigDecimal.ZERO;

		// Adicionar minutos
		if (builder.indexOf("m") != -1) {
			minutos = new BigDecimal(builder.substring(0, builder.indexOf("m")));
			builder.delete(0, builder.indexOf("m") + 1);
		} else
			minutos = BigDecimal.ZERO;

		// Adicionar segundos
		if (builder.indexOf("s") != -1) {
			segundos = new BigDecimal(
					builder.substring(0, builder.indexOf("s")));
			builder.delete(0, builder.indexOf("s") + 1);
		} else
			segundos = BigDecimal.ZERO;

		tempo = dias.multiply(new BigDecimal(24));
		tempo = tempo.add(horas);
		tempo = tempo.multiply(new BigDecimal(60));
		tempo = tempo.add(minutos);
		tempo = tempo.multiply(new BigDecimal(60));
		tempo = tempo.add(segundos);

		return tempo;

	}

}
