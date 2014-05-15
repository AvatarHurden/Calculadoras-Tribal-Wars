package assistente_saque;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import custom_components.CoordenadaPanel;
import custom_components.EdifícioFormattedComboBox;
import custom_components.IntegerFormattedTextField;
import database.BigOperation;
import database.Edifício;
import database.Unidade;

public class Cálculo {
	
	// Dados recebidos
	
	// Produção dos recursos (recurso/milissegundos)
	private BigDecimal[] produção = new BigDecimal[3];
	// Leva em conta o nível do armazém e do esconderijo
	private BigDecimal armazenamento;
	// Soma de todo o saque
	private BigDecimal saqueTotal;
	// Velocidade mais lenta (milissegundos/campo)
	private BigDecimal velocidade;
	
	// Momento do último ataque (em milisegundos)
	private BigDecimal ultimoAtaque;
	// Distância entre as aldeias
	private BigDecimal distância;
	// Recursos restantes
	private BigDecimal[] restantes = new BigDecimal[3];
	
	// Unidades disponíveis para enviar
	private Map<Unidade, BigDecimal> tropasDisponíveis = new HashMap<Unidade, BigDecimal>();
	
	// Dados intermediários para cálculo

	// Se o saque total for maior do que o armazém
	private boolean armazémPequeno = false;
	
	// Dados finais
	
	// Tempo entre ataques (em milissegundos)
	private BigDecimal intervalo;
	// Horário para enviar próximo ataque (em milisegundos)
	private BigDecimal enviarAtaque;
	// Unidades recomendadas
	private Map<Unidade, BigDecimal> tropasRecomendadas = new HashMap<Unidade, BigDecimal>();

	protected Cálculo() {}
	
	protected void setIntervalo() {
		
		intervalo = getTimeToSend(null);
		
		if (armazémPequeno)
			setUnidadesRecomendadas();
		else
			tropasRecomendadas = null;
		
	
	}
	
	protected void setEnviarAtaque() {
		
		// de minutos/campo para milissegundos/campo
		velocidade = velocidade.multiply(new BigDecimal("60000"));
		
		for (int i = 0; i < 3; i++)
			restantes[i] = restantes[i].add(
					distância.multiply(velocidade).multiply(produção[i]));
						
		// Recebe o tempo que deveria esperar até enviar outro ataque
		enviarAtaque = getTimeToSend(restantes);
		// Coloca o horário a enviar o ataque
		enviarAtaque = enviarAtaque.add(ultimoAtaque);
	
	}
	
	protected void setUnidadesRecomendadas() {
		
		tropasRecomendadas = new HashMap<Unidade, BigDecimal>();
		
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
		
		BigDecimal saqueRecomendado = armazenamento.multiply(new BigDecimal("3"));
		
		for (Unidade i : preferência) {
			
			if (tropasDisponíveis.containsKey(i))
				if (tropasDisponíveis.get(i).multiply(i.saque()).compareTo(saqueRecomendado) > -1) {
					
					// Puts the maximum number of troops the warehouse holds
					tropasRecomendadas.put(i, saqueRecomendado.
							divide(i.saque(), 0, RoundingMode.CEILING));
					break;
					
				} else {
					tropasRecomendadas.put(i, tropasDisponíveis.get(i));
					// Prepares the remaining available loot for other troops
					saqueRecomendado = saqueRecomendado.
							subtract(tropasDisponíveis.get(i).multiply(i.saque()));
				}
				
		}
		
		// Keeps the units with no loot power
		// TODO decide if they should be kept or completely removed
		for (Unidade i : tropasDisponíveis.keySet()) {
			if (!preferência.contains(i))
				tropasRecomendadas.put(i, tropasDisponíveis.get(i));
			// If the 'preferência' broke, puts the remaining things with zero
			if (!tropasRecomendadas.containsKey(i))
				tropasRecomendadas.put(i, BigDecimal.ZERO);
		}
		
	}
	
	/**
	 * Given initial resources, calculates the time to wait unitl sending another attack.
	 * Uses the values of "saqueTotal", "armazenamento", "armazémPequeno"
	 * @param An array with 3 elements, representing leftover wood, clay and iron.
	 */
	private BigDecimal getTimeToSend(BigDecimal[] initial) {
		
		if (saqueTotal.compareTo(
				armazenamento.multiply(new BigDecimal("3"))) == 1) {
			armazémPequeno = true;
			saqueTotal = armazenamento.multiply(new BigDecimal("3"));
		} else
			armazémPequeno = false;
		
		// Handles null parameter
		if (initial != null) {
			for (int i = 0; i < initial.length; i++)
				if (initial[i] == null)
					initial[i] = BigDecimal.ZERO;
		} else
			initial = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
		
		// Array that will contain time to fill resources (wood, clay, iron)
		BigDecimal[] tempoEncher = new BigDecimal[3];
		
		// Setting the time it would take to fill every resource
		for (int i = 0; i < 3; i++) {
			
			if (produção[i].compareTo(BigDecimal.ZERO) == 1)
				tempoEncher[i] = armazenamento.subtract(initial[i]).
					divide(produção[i], 30, RoundingMode.HALF_EVEN);
			else
				// If there is no resource production, the time is infinite
				tempoEncher[i] = new BigDecimal(Integer.MAX_VALUE);
			
			// Doesn't allow negative times
			if (tempoEncher[i].signum() == -1)
				tempoEncher[i] = BigDecimal.ZERO;
			
		}
		// Setting the initial calculated time at 0
		BigDecimal time = BigDecimal.ZERO;
		// A soma das produções dos recursos (recurso/milissegundo)
		BigDecimal somaProdução;
		
		// Variable that controls whether the calculated time is larger than any tempoEncher
		// Starts at true to ensure one run
		boolean overflow = true;
		
		while (overflow) {
			
			overflow = false;
			
			// Recursos que são constantes para o cálculo
			// Armazena os que tiveram overflow, pois é certo de que cada passada aumenta o tempo necessário
			BigDecimal resources = BigDecimal.ZERO;
			
			// A produção de recursos por milissegundo, não contando aqueles que sofreram overflow
			somaProdução = BigDecimal.ZERO;
			
			for (int i = 0; i < 3; i++)
				if (tempoEncher[i].compareTo(time) == 1) {
					somaProdução = somaProdução.add(produção[i]);
				} else if (tempoEncher[i].compareTo(time) == 0) {
					// if the time is exact, the limit has been reached without overflow
					resources = resources.add(armazenamento);
				} else {
					// Only has overflow if the time passed the limit
					resources = resources.add(armazenamento);
					overflow = true;
				}
					
			
			// Saque total = recursos + time*produção
			time = saqueTotal.subtract(resources);
			if (somaProdução.compareTo(BigDecimal.ZERO) == 1)
				time = time.divide(somaProdução, 30, RoundingMode.HALF_EVEN);
		
		}
		
		return time;
		
	}
	
	/**
	 * Determina o armazenamento total da aldeia (armazém-esconderijo)
	 * e a produção por segundo dos recursos.
	 * @param Mapa relacionando edifícios aos seus níveis
	 */
	protected void setProduçãoEArmazenamento(
				Map<Edifício, EdifícioFormattedComboBox> edifícios) {
		
		armazenamento = BigDecimal.ZERO;
		
		for (Edifício ed : edifícios.keySet()) {
			
			
			switch (ed) {
			case ARMAZÉM:
				if (edifícios.get(ed).getSelectedIndex() > 0)
					armazenamento = armazenamento.add(
						new BigDecimal("1.22949").pow(
								edifícios.get(ed).getSelectedIndex()-1)
								.multiply(new BigDecimal("1000"))).setScale(0, RoundingMode.HALF_EVEN);
				break;
			case ESCONDERIJO:
				if (edifícios.get(ed).getSelectedIndex() > 0)
					armazenamento = armazenamento.subtract(
						new BigDecimal("1.3335").pow(
							edifícios.get(ed).getSelectedIndex()-1).
							multiply(new BigDecimal("100"))).setScale(0, RoundingMode.HALF_EVEN);
				break;
			// Caso não seja nem armazém nem esconderijo, é um dos
			// produtores
			case BOSQUE:
				if (edifícios.get(ed).getSelectedIndex() == 0)
					produção[0] = BigDecimal.ZERO;
				else
					produção[0] = new BigDecimal("1.16311").pow(
						edifícios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("30")).setScale(0, RoundingMode.HALF_EVEN);;
				break;
			case POÇO_DE_ARGILA:
				if (edifícios.get(ed).getSelectedIndex() == 0)
					produção[1] = BigDecimal.ZERO;
				else
					produção[1] = new BigDecimal("1.16311").pow(
						edifícios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("30")).setScale(0, RoundingMode.HALF_EVEN);;
				break;
			case MINA_DE_FERRO:
				if (edifícios.get(ed).getSelectedIndex() == 0)
					produção[2] = BigDecimal.ZERO;
				else
					produção[2] = new BigDecimal("1.16311").pow(
						edifícios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("30")).setScale(0, RoundingMode.HALF_EVEN);;
				break;
			default:
			}
		}
		
		// Changes production from per hour to per millissecond
		for (int i = 0; i < 3; i++)
			produção[i] = produção[i].divide(new BigDecimal("3600000"), 30, RoundingMode.HALF_DOWN);
			
		
	}
	
	/**
	 * Determina o saque total das tropas fornecidas e a velocidade do ataque
	 * @param Mapa relacionando as unidades com os textFields
	 */
	protected void setSaqueTotal(
				Map<Unidade, IntegerFormattedTextField> quantidades) {
		
		saqueTotal = BigDecimal.ZERO;
		velocidade = BigDecimal.ZERO;
		
		for (Unidade i : quantidades.keySet()) {
			// Adiciona o saque unitário*quantidade
			saqueTotal = saqueTotal.add(
					quantidades.get(i).getValue().
					multiply(i.saque()));
			
			// Verifica se a unidade atual é mais lenta do que a registrada
			if (i.velocidade().compareTo(velocidade) == 1)
				velocidade = i.velocidade();
			
			// Coloca as tropas no map de disponibilidade
			tropasDisponíveis.put(i, quantidades.get(i).getValue());
	
		}
		
	}
	
	/**
	 * Calcula a distância entre as duas aldeias
	 * @param Aldeia de origem
	 * @param Aldeia de destino
	 */
	protected void setDistância(CoordenadaPanel origem, CoordenadaPanel destino) {
		
		BigDecimal cateto1 = new BigDecimal(origem.getCoordenadaX()
				-destino.getCoordenadaX());
		
		BigDecimal cateto2 = new BigDecimal(origem.getCoordenadaY()
				-destino.getCoordenadaY());
		
		// sum of the squares
		distância = cateto1.pow(2).add(cateto2.pow(2));
		
		distância = BigOperation.sqrt(distância, 30);
		
	}
	
	/**
	 * Sets the remaining resources in the looted village
	 * @param An array with the 3 resources
	 */
	protected void setRestantes(IntegerFormattedTextField[] array) {
		
		for (int i = 0; i < array.length; i++)
			restantes[i] = array[i].getValue();
		
	}
	
	protected void setUltimoAtaque(long tempo) {
		ultimoAtaque = new BigDecimal(tempo);
	}
	
	protected long getIntervalo() {
		return intervalo.longValue();
	}
	
	protected long getHorario() throws SameDateException{
		
		if (distância.compareTo(BigDecimal.ZERO) == 0)
			throw new SameDateException("Aldeias com coordenadas iguais");
		else
			return enviarAtaque.longValue();
	}
	
	/**
	 * Gives the recommended units to send with the specified buildings. If the sent units are less than the
	 * recommended, returns null
	 * @return Map<Unidade, BigDecimal> of the troops
	 * 			If sent troops are less than recommended, returns null
	 */
	protected Map<Unidade, BigDecimal> getUnidadesRecomendadas() {
		
		for (Unidade i : tropasDisponíveis.keySet())
			if (!tropasRecomendadas.get(i).equals(tropasDisponíveis.get(i)))
				return tropasRecomendadas;
		
		// Apenas chega aqui se todas as tropas recomendadas forem iguais às enviadas
		return null;
		
	}
	
	protected class NoIntervalException extends Exception {
		public NoIntervalException() {}
	}
	
	protected class SameDateException extends Exception {
		protected String error;
		public SameDateException(String error) {
			this.error = error;
		}
	}
		
}
