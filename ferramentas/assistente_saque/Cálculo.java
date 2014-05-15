package assistente_saque;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import custom_components.CoordenadaPanel;
import custom_components.Edif�cioFormattedComboBox;
import custom_components.IntegerFormattedTextField;
import database.BigOperation;
import database.Edif�cio;
import database.Unidade;

public class C�lculo {
	
	// Dados recebidos
	
	// Produ��o dos recursos (recurso/milissegundos)
	private BigDecimal[] produ��o = new BigDecimal[3];
	// Leva em conta o n�vel do armaz�m e do esconderijo
	private BigDecimal armazenamento;
	// Soma de todo o saque
	private BigDecimal saqueTotal;
	// Velocidade mais lenta (milissegundos/campo)
	private BigDecimal velocidade;
	
	// Momento do �ltimo ataque (em milisegundos)
	private BigDecimal ultimoAtaque;
	// Dist�ncia entre as aldeias
	private BigDecimal dist�ncia;
	// Recursos restantes
	private BigDecimal[] restantes = new BigDecimal[3];
	
	// Unidades dispon�veis para enviar
	private Map<Unidade, BigDecimal> tropasDispon�veis = new HashMap<Unidade, BigDecimal>();
	
	// Dados intermedi�rios para c�lculo

	// Se o saque total for maior do que o armaz�m
	private boolean armaz�mPequeno = false;
	
	// Dados finais
	
	// Tempo entre ataques (em milissegundos)
	private BigDecimal intervalo;
	// Hor�rio para enviar pr�ximo ataque (em milisegundos)
	private BigDecimal enviarAtaque;
	// Unidades recomendadas
	private Map<Unidade, BigDecimal> tropasRecomendadas = new HashMap<Unidade, BigDecimal>();

	protected C�lculo() {}
	
	protected void setIntervalo() {
		
		intervalo = getTimeToSend(null);
		
		if (armaz�mPequeno)
			setUnidadesRecomendadas();
		else
			tropasRecomendadas = null;
		
	
	}
	
	protected void setEnviarAtaque() {
		
		// de minutos/campo para milissegundos/campo
		velocidade = velocidade.multiply(new BigDecimal("60000"));
		
		for (int i = 0; i < 3; i++)
			restantes[i] = restantes[i].add(
					dist�ncia.multiply(velocidade).multiply(produ��o[i]));
						
		// Recebe o tempo que deveria esperar at� enviar outro ataque
		enviarAtaque = getTimeToSend(restantes);
		// Coloca o hor�rio a enviar o ataque
		enviarAtaque = enviarAtaque.add(ultimoAtaque);
	
	}
	
	protected void setUnidadesRecomendadas() {
		
		tropasRecomendadas = new HashMap<Unidade, BigDecimal>();
		
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
		
		BigDecimal saqueRecomendado = armazenamento.multiply(new BigDecimal("3"));
		
		for (Unidade i : prefer�ncia) {
			
			if (tropasDispon�veis.containsKey(i))
				if (tropasDispon�veis.get(i).multiply(i.saque()).compareTo(saqueRecomendado) > -1) {
					
					// Puts the maximum number of troops the warehouse holds
					tropasRecomendadas.put(i, saqueRecomendado.
							divide(i.saque(), 0, RoundingMode.CEILING));
					break;
					
				} else {
					tropasRecomendadas.put(i, tropasDispon�veis.get(i));
					// Prepares the remaining available loot for other troops
					saqueRecomendado = saqueRecomendado.
							subtract(tropasDispon�veis.get(i).multiply(i.saque()));
				}
				
		}
		
		// Keeps the units with no loot power
		// TODO decide if they should be kept or completely removed
		for (Unidade i : tropasDispon�veis.keySet()) {
			if (!prefer�ncia.contains(i))
				tropasRecomendadas.put(i, tropasDispon�veis.get(i));
			// If the 'prefer�ncia' broke, puts the remaining things with zero
			if (!tropasRecomendadas.containsKey(i))
				tropasRecomendadas.put(i, BigDecimal.ZERO);
		}
		
	}
	
	/**
	 * Given initial resources, calculates the time to wait unitl sending another attack.
	 * Uses the values of "saqueTotal", "armazenamento", "armaz�mPequeno"
	 * @param An array with 3 elements, representing leftover wood, clay and iron.
	 */
	private BigDecimal getTimeToSend(BigDecimal[] initial) {
		
		if (saqueTotal.compareTo(
				armazenamento.multiply(new BigDecimal("3"))) == 1) {
			armaz�mPequeno = true;
			saqueTotal = armazenamento.multiply(new BigDecimal("3"));
		} else
			armaz�mPequeno = false;
		
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
			
			if (produ��o[i].compareTo(BigDecimal.ZERO) == 1)
				tempoEncher[i] = armazenamento.subtract(initial[i]).
					divide(produ��o[i], 30, RoundingMode.HALF_EVEN);
			else
				// If there is no resource production, the time is infinite
				tempoEncher[i] = new BigDecimal(Integer.MAX_VALUE);
			
			// Doesn't allow negative times
			if (tempoEncher[i].signum() == -1)
				tempoEncher[i] = BigDecimal.ZERO;
			
		}
		// Setting the initial calculated time at 0
		BigDecimal time = BigDecimal.ZERO;
		// A soma das produ��es dos recursos (recurso/milissegundo)
		BigDecimal somaProdu��o;
		
		// Variable that controls whether the calculated time is larger than any tempoEncher
		// Starts at true to ensure one run
		boolean overflow = true;
		
		while (overflow) {
			
			overflow = false;
			
			// Recursos que s�o constantes para o c�lculo
			// Armazena os que tiveram overflow, pois � certo de que cada passada aumenta o tempo necess�rio
			BigDecimal resources = BigDecimal.ZERO;
			
			// A produ��o de recursos por milissegundo, n�o contando aqueles que sofreram overflow
			somaProdu��o = BigDecimal.ZERO;
			
			for (int i = 0; i < 3; i++)
				if (tempoEncher[i].compareTo(time) == 1) {
					somaProdu��o = somaProdu��o.add(produ��o[i]);
				} else if (tempoEncher[i].compareTo(time) == 0) {
					// if the time is exact, the limit has been reached without overflow
					resources = resources.add(armazenamento);
				} else {
					// Only has overflow if the time passed the limit
					resources = resources.add(armazenamento);
					overflow = true;
				}
					
			
			// Saque total = recursos + time*produ��o
			time = saqueTotal.subtract(resources);
			if (somaProdu��o.compareTo(BigDecimal.ZERO) == 1)
				time = time.divide(somaProdu��o, 30, RoundingMode.HALF_EVEN);
		
		}
		
		return time;
		
	}
	
	/**
	 * Determina o armazenamento total da aldeia (armaz�m-esconderijo)
	 * e a produ��o por segundo dos recursos.
	 * @param Mapa relacionando edif�cios aos seus n�veis
	 */
	protected void setProdu��oEArmazenamento(
				Map<Edif�cio, Edif�cioFormattedComboBox> edif�cios) {
		
		armazenamento = BigDecimal.ZERO;
		
		for (Edif�cio ed : edif�cios.keySet()) {
			
			
			switch (ed) {
			case ARMAZ�M:
				if (edif�cios.get(ed).getSelectedIndex() > 0)
					armazenamento = armazenamento.add(
						new BigDecimal("1.22949").pow(
								edif�cios.get(ed).getSelectedIndex()-1)
								.multiply(new BigDecimal("1000"))).setScale(0, RoundingMode.HALF_EVEN);
				break;
			case ESCONDERIJO:
				if (edif�cios.get(ed).getSelectedIndex() > 0)
					armazenamento = armazenamento.subtract(
						new BigDecimal("1.3335").pow(
							edif�cios.get(ed).getSelectedIndex()-1).
							multiply(new BigDecimal("100"))).setScale(0, RoundingMode.HALF_EVEN);
				break;
			// Caso n�o seja nem armaz�m nem esconderijo, � um dos
			// produtores
			case BOSQUE:
				if (edif�cios.get(ed).getSelectedIndex() == 0)
					produ��o[0] = BigDecimal.ZERO;
				else
					produ��o[0] = new BigDecimal("1.16311").pow(
						edif�cios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("30")).setScale(0, RoundingMode.HALF_EVEN);;
				break;
			case PO�O_DE_ARGILA:
				if (edif�cios.get(ed).getSelectedIndex() == 0)
					produ��o[1] = BigDecimal.ZERO;
				else
					produ��o[1] = new BigDecimal("1.16311").pow(
						edif�cios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("30")).setScale(0, RoundingMode.HALF_EVEN);;
				break;
			case MINA_DE_FERRO:
				if (edif�cios.get(ed).getSelectedIndex() == 0)
					produ��o[2] = BigDecimal.ZERO;
				else
					produ��o[2] = new BigDecimal("1.16311").pow(
						edif�cios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("30")).setScale(0, RoundingMode.HALF_EVEN);;
				break;
			default:
			}
		}
		
		// Changes production from per hour to per millissecond
		for (int i = 0; i < 3; i++)
			produ��o[i] = produ��o[i].divide(new BigDecimal("3600000"), 30, RoundingMode.HALF_DOWN);
			
		
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
			// Adiciona o saque unit�rio*quantidade
			saqueTotal = saqueTotal.add(
					quantidades.get(i).getValue().
					multiply(i.saque()));
			
			// Verifica se a unidade atual � mais lenta do que a registrada
			if (i.velocidade().compareTo(velocidade) == 1)
				velocidade = i.velocidade();
			
			// Coloca as tropas no map de disponibilidade
			tropasDispon�veis.put(i, quantidades.get(i).getValue());
	
		}
		
	}
	
	/**
	 * Calcula a dist�ncia entre as duas aldeias
	 * @param Aldeia de origem
	 * @param Aldeia de destino
	 */
	protected void setDist�ncia(CoordenadaPanel origem, CoordenadaPanel destino) {
		
		BigDecimal cateto1 = new BigDecimal(origem.getCoordenadaX()
				-destino.getCoordenadaX());
		
		BigDecimal cateto2 = new BigDecimal(origem.getCoordenadaY()
				-destino.getCoordenadaY());
		
		// sum of the squares
		dist�ncia = cateto1.pow(2).add(cateto2.pow(2));
		
		dist�ncia = BigOperation.sqrt(dist�ncia, 30);
		
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
		
		if (dist�ncia.compareTo(BigDecimal.ZERO) == 0)
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
		
		for (Unidade i : tropasDispon�veis.keySet())
			if (!tropasRecomendadas.get(i).equals(tropasDispon�veis.get(i)))
				return tropasRecomendadas;
		
		// Apenas chega aqui se todas as tropas recomendadas forem iguais �s enviadas
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
