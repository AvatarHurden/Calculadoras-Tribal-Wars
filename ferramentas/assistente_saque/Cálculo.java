package assistente_saque;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import custom_components.CoordenadaPanel;
import custom_components.Edif�cioFormattedComboBox;
import custom_components.TroopFormattedTextField;
import database.BigOperation;
import database.Edif�cio;
import database.Unidade;

public class C�lculo {
	
	// Dados recebidos
	
	// N�o � necess�rio saber qual recurso � produzido, apenas a soma deles
	private BigDecimal produ��o;
	// Leva em conta o n�vel do armaz�m e do esconderijo
	private BigDecimal armazenamento;
	// Soma de todo o saque
	private BigDecimal saqueTotal;
	// Velocidade mais lenta
	private BigDecimal velocidade;
	
	// Momento do �ltimo ataque
	private BigDecimal ultimoAtaque;
	// Dist�ncia entre as aldeias
	private BigDecimal dist�ncia;
	// Recursos restantes
	private BigDecimal restantes;
	
	// Dados calculados
	
	// Tempo entre ataques (em segundos)
	private BigDecimal intervalo;
	// Hor�rio para enviar pr�ximo ataque
	private BigDecimal enviarAtaque;
	// Se o saque total for maior do que o armaz�m
	private boolean armaz�mPequeno;
	
	
	protected C�lculo() {}
	
	/**
	 * Calcula o intervalo entre ataques e define se o armaz�m for pequeno
	 */
	private void setIntervalo() {
		
		if (saqueTotal.compareTo(armazenamento) == 1) {
			armaz�mPequeno = true;
			saqueTotal = armazenamento;
		}
		
		intervalo = saqueTotal.divide(produ��o, 30, RoundingMode.HALF_EVEN);
		
	}
	
	private void setEnviarAtaque() {
		
		
		
	}
	
	/**
	 * Determina o armazenamento total da aldeia (armaz�m-esconderijo)
	 * e a produ��o por segundo dos recursos.
	 * @param Mapa relacionando edif�cios aos seus n�veis
	 */
	protected void setProdu��oEArmazenamento(
				Map<Edif�cio, Edif�cioFormattedComboBox> edif�cios) {
		
		produ��o = BigDecimal.ZERO;
		
		for (Edif�cio ed : edif�cios.keySet()) {
			
			switch (ed) {
			case ARMAZ�M:
				armazenamento = new BigDecimal("1.22949").pow(
						edif�cios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("1000")).setScale(0);
				break;
			case ESCONDERIJO:
				armazenamento = armazenamento.subtract(
						new BigDecimal("1.3335").pow(
							edif�cios.get(ed).getSelectedIndex()-1).
							multiply(new BigDecimal("100"))).setScale(0);
				break;
			// Caso n�o seja nem armaz�m nem esconderijo, � um dos
			// produtores
			default:
				produ��o = produ��o.add(null);
			}
		}
		
		// Changes production from per hour to per second
		produ��o = produ��o.divide(new BigDecimal("3600"), 
				30, RoundingMode.HALF_DOWN);
		
	}
	
	/**
	 * Determina o saque total das tropas fornecidas
	 * @param Mapa relacionando as unidades com os textFields
	 */
	protected void setSaqueTotal(
				Map<Unidade, TroopFormattedTextField> quantidades) {
		
		saqueTotal = BigDecimal.ZERO;
		velocidade = BigDecimal.ZERO;
		
		for (Unidade i : quantidades.keySet()) {
			// Adiciona o saque unit�rio*quantidade
			saqueTotal = saqueTotal.add(
					quantidades.get(i).getValue().
					multiply(i.saque()));
			
			// Verifica se a unidade atual � mais lenta do que a registrada
			if (i.velocidade().compareTo(velocidade) == -1)
				velocidade = i.velocidade();
			
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
	protected void setRestantes(TroopFormattedTextField[] array) {
		
		restantes = BigDecimal.ZERO;
		
		for (TroopFormattedTextField i : array)
			restantes = restantes.add(i.getValue());
		
	}
	
}
