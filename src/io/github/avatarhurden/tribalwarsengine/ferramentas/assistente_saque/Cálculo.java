package io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.Army;
import io.github.avatarhurden.tribalwarsengine.objects.Buildings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.Unidade;

public class Cálculo {
	
	// Dados recebidos
	
	private Army army;
	
	// Produção dos recursos recurso/milissegundo
	private BigDecimal[] produção = new BigDecimal[3];
	// Leva em conta o nível do armazém e do esconderijo
	private int armazenamento;
	
	// Momento do último ataque (em milisegundos)
	private Date ultimoAtaque;
	// Distância entre as aldeias
	private double distância;
	// Recursos restantes
	private int[] restantes = new int[3];
	
	// Dados intermediários para cálculo

	// Se o saque total for maior do que o armazém
	private boolean armazémPequeno = false;
	
	// Dados finais
	
	// Tempo entre ataques (em milissegundos)
	private long intervalo;
	// Horário para enviar próximo ataque (em milisegundos)
	private Date enviarAtaque;
	// Unidades recomendadas
	private Army tropasRecomendadas;

	protected Cálculo() {}
	
	protected void setIntervalo() {
		
		intervalo = getTimeToSend(null);
		
		if (armazémPequeno)
			setUnidadesRecomendadas();
		else
			tropasRecomendadas = null;
		
	
	}
	
	protected void setEnviarAtaque() {
				
		// recursos = campos*(millisegundos/campo)*recursos/millisegundos
		
		for (int i = 0; i < 3; i++)
			restantes[i] += distância * army.getVelocidade() * produção[i].doubleValue();
		
		// Coloca o horário a enviar o ataque
		enviarAtaque = new Date(ultimoAtaque.getTime() + getTimeToSend(restantes));
	
	}
	
	protected void setUnidadesRecomendadas() {
		
		tropasRecomendadas = new Army(Army.getAttackingUnits());
		
		// Cria a lista de ordem das unidades para preencher 
		List<Unidade> preferência = new ArrayList<Unidade>();
		preferência.add(Unidade.PALADINO);
		preferência.add(Unidade.CAVALOLEVE);
		preferência.add(Unidade.ARCOCAVALO);
		preferência.add(Unidade.CAVALOPESADO);
		preferência.add(Unidade.LANCEIRO);
		preferência.add(Unidade.ARQUEIRO);
		preferência.add(Unidade.BÁRBARO);
		preferência.add(Unidade.ESPADACHIM);
		
		int saqueRecomendado = armazenamento * 3;
		
		for (Unidade i : preferência) {
			
			if (army.contains(i))
				if (army.getQuantidade(i) * i.getSaque() >= saqueRecomendado) {
					
					// Puts the maximum number of troops the warehouse holds
					tropasRecomendadas.addTropa(i, 
							(int) Math.ceil(saqueRecomendado / (double) i.getSaque()), 1);
					break;
					
				} else {
					tropasRecomendadas.addTropa(i, army.getQuantidade(i), 1);
					// Prepares the remaining available loot for other troops
					saqueRecomendado -= army.getQuantidade(i) * i.getSaque();
				}
				
		}
		
		for (Unidade i : army.getUnidades()) {
			if (!preferência.contains(i))
				tropasRecomendadas.addTropa(i, army.getQuantidade(i), 1);
			// If the 'preferência' broke, puts the remaining things with zero
			if (!tropasRecomendadas.contains(i))
				tropasRecomendadas.addTropa(i, 0, 1);
		}
		
	}
	
	/**
	 * Given initial resources, calculates the time to wait unitl sending another attack.
	 * Uses the values of "saqueTotal", "armazenamento", "armazémPequeno"
	 * @param An array with 3 elements, representing leftover wood, clay and iron.
	 */
	private long getTimeToSend(int[] initial) {
		
		int saqueDisponivel = army.getSaque();
		
		if (saqueDisponivel > armazenamento * 3) {
			armazémPequeno = true;
			saqueDisponivel = armazenamento * 3;
		} else
			armazémPequeno = false;
		
		// Handles null parameter
		if (initial == null)
			initial = new int[] { 0, 0, 0 };
		
		// Array that will contain time to fill resources (wood, clay, iron)
		double[] tempoEncher = new double[3];
		
		// Setting the time it would take to fill every resource
		for (int i = 0; i < 3; i++) {
			if (produção[i].doubleValue() > 0)
				tempoEncher[i] = (armazenamento - initial[i]) / produção[i].doubleValue();
			else
				// If there is no resource production, the time is infinite
				tempoEncher[i] = Double.MAX_VALUE;
			
			// Doesn't allow negative times
			if (tempoEncher[i] < 0)
				tempoEncher[i] = 0;
			
		}
		
		// Setting the initial calculated time at 0
		double time = 0;
		// A soma das produções dos recursos (recurso/milissegundo)
		BigDecimal somaProdução;
		
		// Variable that controls whether the calculated time is larger than any tempoEncher
		// Starts at true to ensure one run
		boolean overflow = true;
		
		while (overflow) {
			
			overflow = false;
			
			// Recursos que são constantes para o cálculo
			// Armazena os que tiveram overflow, pois é certo que cada passada aumenta o tempo necessário
			int resources = 0;
			
			// A quantidade de milissegundos para fazer 1 recurso, não contando aqueles que sofreram overflow
			somaProdução = BigDecimal.ZERO;
			
			for (int i = 0; i < 3; i++)
				if (tempoEncher[i] > time) {
					somaProdução = somaProdução.add(produção[i]);
				} else if (tempoEncher[i] == time) {
					// if the time is exact, the limit has been reached without overflow
					resources += armazenamento;
				} else {
					// Only has overflow if the time passed the limit
					resources += armazenamento;
					overflow = true;
				}
			
			int stored = 0;
			
			// Adds all of the stored resources
			for (int i : initial)
				stored += i;
			
			// Saque total = recursos + time*produção
			if (somaProdução.compareTo(BigDecimal.ZERO) == 1) {
				time = saqueDisponivel - resources- stored;
				time /= somaProdução.doubleValue();
			} else
				// Caso não haja produção, o tempo é zero
				time = 0;
		
		}

		return (long) Math.round(time);
		
	}
	
	/**
	 * Determina o armazenamento total da aldeia (armazém-esconderijo)
	 * e a produção por segundo dos recursos.
	 * @param Mapa relacionando edifícios aos seus níveis
	 */
	protected void setProduçãoEArmazenamento(Buildings buildings) {
		
		armazenamento = buildings.getArmazenamento(true);
		produção[0] = new BigDecimal(buildings.getWoodProduction());
		produção[1] = new BigDecimal(buildings.getClayProduction());
		produção[2] = new BigDecimal(buildings.getIronProduction());
		
		// Changes production from per hour to per millissecond
		for (int i = 0; i < 3; i++) {
			produção[i] = produção[i].divide(new BigDecimal("3600000"), 30, RoundingMode.HALF_EVEN);
			// Arruma para a velocidade do mundo
			produção[i] = produção[i].multiply(new BigDecimal(
					WorldManager.get().getSelectedWorld().getWorldSpeed()));
		}
			
		
	}
	
	protected void setArmy(Army army) {
		this.army = army;
	}
	
	/**
	 * Calcula a distância entre as duas aldeias
	 * @param Aldeia de origem
	 * @param Aldeia de destino
	 */
	protected void setDistância(CoordenadaPanel origem, CoordenadaPanel destino) {
		
		double cateto1 = origem.getCoordenadaX() - destino.getCoordenadaX();
		
		double cateto2 = origem.getCoordenadaY() - destino.getCoordenadaY();
		
		// sum of the squares
		distância = cateto1*cateto1 + cateto2*cateto2;;
		
		distância = Math.sqrt(distância);
		
	}
	
	/**
	 * Sets the remaining resources in the looted village
	 * @param An array with the 3 resources
	 */
	protected void setRestantes(IntegerFormattedTextField[] array) {
		
		for (int i = 0; i < array.length; i++) 
			restantes[i] = array[i].getValue().intValue();
		
	}
	
	protected void setUltimoAtaque(Date tempo) {
		ultimoAtaque = tempo;
	}
	
	protected long getIntervalo() throws NoIntervalException{
	
		if (army.getSaque() == 0)
			throw new NoIntervalException("<html><div style=\"text-align: center;\">"
					+ "Sem Unidades<br>com Saque</html>");
			
		if (armazenamento == 0)
			throw new NoIntervalException("Sem Armazenamento");
		
		return intervalo;
		
	}
	
	protected Date getHorario() throws SameDateException{
		
		if (distância == 0)
			throw new SameDateException("Aldeias com coordenadas iguais");
		else
			return enviarAtaque;
	}
	
	/**
	 * Gives the recommended units to send with the specified buildings. If the sent units are less than the
	 * recommended, returns null
	 * @return Map<Unidade, BigDecimal> of the troops
	 * 			If sent troops are less than recommended, returns null
	 */
	protected Army getUnidadesRecomendadas() {
		
		// Caso padrão
		if (tropasRecomendadas == null)
			return null;
		
		if (tropasRecomendadas.getTropas().equals(army.getTropas()))
			return null;
		else
			return tropasRecomendadas;
		
	}
	
	@SuppressWarnings("serial")
	protected class NoIntervalException extends Exception {
		protected String error;
		public NoIntervalException(String error) {
			this.error = error;
		}
		
		public String getMessage() {
			return error;
		}
	}
	
	@SuppressWarnings("serial")
	protected class SameDateException extends Exception {
		protected String error;
		public SameDateException(String error) {
			this.error = error;
		}
		
		public String getMessage() {
			return error;
		}
	}
		
}
