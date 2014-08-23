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

public class C�lculo {
	
	// Dados recebidos
	
	private Army army;
	
	// Produ��o dos recursos recurso/milissegundo
	private BigDecimal[] produ��o = new BigDecimal[3];
	// Leva em conta o n�vel do armaz�m e do esconderijo
	private int armazenamento;
	
	// Momento do �ltimo ataque (em milisegundos)
	private Date ultimoAtaque;
	// Dist�ncia entre as aldeias
	private double dist�ncia;
	// Recursos restantes
	private int[] restantes = new int[3];
	
	// Dados intermedi�rios para c�lculo

	// Se o saque total for maior do que o armaz�m
	private boolean armaz�mPequeno = false;
	
	// Dados finais
	
	// Tempo entre ataques (em milissegundos)
	private long intervalo;
	// Hor�rio para enviar pr�ximo ataque (em milisegundos)
	private Date enviarAtaque;
	// Unidades recomendadas
	private Army tropasRecomendadas;

	protected C�lculo() {}
	
	protected void setIntervalo() {
		
		intervalo = getTimeToSend(null);
		
		if (armaz�mPequeno)
			setUnidadesRecomendadas();
		else
			tropasRecomendadas = null;
		
	
	}
	
	protected void setEnviarAtaque() {
				
		// recursos = campos*(millisegundos/campo)*recursos/millisegundos
		
		for (int i = 0; i < 3; i++)
			restantes[i] += dist�ncia * army.getVelocidade() * produ��o[i].doubleValue();
		
		// Coloca o hor�rio a enviar o ataque
		enviarAtaque = new Date(ultimoAtaque.getTime() + getTimeToSend(restantes));
	
	}
	
	protected void setUnidadesRecomendadas() {
		
		tropasRecomendadas = new Army(Army.getAttackingUnits());
		
		// Cria a lista de ordem das unidades para preencher 
		List<Unidade> prefer�ncia = new ArrayList<Unidade>();
		prefer�ncia.add(Unidade.PALADINO);
		prefer�ncia.add(Unidade.CAVALOLEVE);
		prefer�ncia.add(Unidade.ARCOCAVALO);
		prefer�ncia.add(Unidade.CAVALOPESADO);
		prefer�ncia.add(Unidade.LANCEIRO);
		prefer�ncia.add(Unidade.ARQUEIRO);
		prefer�ncia.add(Unidade.B�RBARO);
		prefer�ncia.add(Unidade.ESPADACHIM);
		
		int saqueRecomendado = armazenamento * 3;
		
		for (Unidade i : prefer�ncia) {
			
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
			if (!prefer�ncia.contains(i))
				tropasRecomendadas.addTropa(i, army.getQuantidade(i), 1);
			// If the 'prefer�ncia' broke, puts the remaining things with zero
			if (!tropasRecomendadas.contains(i))
				tropasRecomendadas.addTropa(i, 0, 1);
		}
		
	}
	
	/**
	 * Given initial resources, calculates the time to wait unitl sending another attack.
	 * Uses the values of "saqueTotal", "armazenamento", "armaz�mPequeno"
	 * @param An array with 3 elements, representing leftover wood, clay and iron.
	 */
	private long getTimeToSend(int[] initial) {
		
		int saqueDisponivel = army.getSaque();
		
		if (saqueDisponivel > armazenamento * 3) {
			armaz�mPequeno = true;
			saqueDisponivel = armazenamento * 3;
		} else
			armaz�mPequeno = false;
		
		// Handles null parameter
		if (initial == null)
			initial = new int[] { 0, 0, 0 };
		
		// Array that will contain time to fill resources (wood, clay, iron)
		double[] tempoEncher = new double[3];
		
		// Setting the time it would take to fill every resource
		for (int i = 0; i < 3; i++) {
			if (produ��o[i].doubleValue() > 0)
				tempoEncher[i] = (armazenamento - initial[i]) / produ��o[i].doubleValue();
			else
				// If there is no resource production, the time is infinite
				tempoEncher[i] = Double.MAX_VALUE;
			
			// Doesn't allow negative times
			if (tempoEncher[i] < 0)
				tempoEncher[i] = 0;
			
		}
		
		// Setting the initial calculated time at 0
		double time = 0;
		// A soma das produ��es dos recursos (recurso/milissegundo)
		BigDecimal somaProdu��o;
		
		// Variable that controls whether the calculated time is larger than any tempoEncher
		// Starts at true to ensure one run
		boolean overflow = true;
		
		while (overflow) {
			
			overflow = false;
			
			// Recursos que s�o constantes para o c�lculo
			// Armazena os que tiveram overflow, pois � certo que cada passada aumenta o tempo necess�rio
			int resources = 0;
			
			// A quantidade de milissegundos para fazer 1 recurso, n�o contando aqueles que sofreram overflow
			somaProdu��o = BigDecimal.ZERO;
			
			for (int i = 0; i < 3; i++)
				if (tempoEncher[i] > time) {
					somaProdu��o = somaProdu��o.add(produ��o[i]);
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
			
			// Saque total = recursos + time*produ��o
			if (somaProdu��o.compareTo(BigDecimal.ZERO) == 1) {
				time = saqueDisponivel - resources- stored;
				time /= somaProdu��o.doubleValue();
			} else
				// Caso n�o haja produ��o, o tempo � zero
				time = 0;
		
		}

		return (long) Math.round(time);
		
	}
	
	/**
	 * Determina o armazenamento total da aldeia (armaz�m-esconderijo)
	 * e a produ��o por segundo dos recursos.
	 * @param Mapa relacionando edif�cios aos seus n�veis
	 */
	protected void setProdu��oEArmazenamento(Buildings buildings) {
		
		armazenamento = buildings.getArmazenamento(true);
		produ��o[0] = new BigDecimal(buildings.getWoodProduction());
		produ��o[1] = new BigDecimal(buildings.getClayProduction());
		produ��o[2] = new BigDecimal(buildings.getIronProduction());
		
		// Changes production from per hour to per millissecond
		for (int i = 0; i < 3; i++) {
			produ��o[i] = produ��o[i].divide(new BigDecimal("3600000"), 30, RoundingMode.HALF_EVEN);
			// Arruma para a velocidade do mundo
			produ��o[i] = produ��o[i].multiply(new BigDecimal(
					WorldManager.get().getSelectedWorld().getWorldSpeed()));
		}
			
		
	}
	
	protected void setArmy(Army army) {
		this.army = army;
	}
	
	/**
	 * Calcula a dist�ncia entre as duas aldeias
	 * @param Aldeia de origem
	 * @param Aldeia de destino
	 */
	protected void setDist�ncia(CoordenadaPanel origem, CoordenadaPanel destino) {
		
		double cateto1 = origem.getCoordenadaX() - destino.getCoordenadaX();
		
		double cateto2 = origem.getCoordenadaY() - destino.getCoordenadaY();
		
		// sum of the squares
		dist�ncia = cateto1*cateto1 + cateto2*cateto2;;
		
		dist�ncia = Math.sqrt(dist�ncia);
		
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
		
		if (dist�ncia == 0)
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
		
		// Caso padr�o
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
