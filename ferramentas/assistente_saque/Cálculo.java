package assistente_saque;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import custom_components.CoordenadaPanel;
import custom_components.EdifícioFormattedComboBox;
import custom_components.TroopFormattedTextField;
import database.BigOperation;
import database.Edifício;
import database.Unidade;

public class Cálculo {
	
	// Dados recebidos
	
	// Não é necessário saber qual recurso é produzido, apenas a soma deles
	private BigDecimal produção;
	// Leva em conta o nível do armazém e do esconderijo
	private BigDecimal armazenamento;
	// Soma de todo o saque
	private BigDecimal saqueTotal;
	// Velocidade mais lenta
	private BigDecimal velocidade;
	
	// Momento do último ataque
	private BigDecimal ultimoAtaque;
	// Distância entre as aldeias
	private BigDecimal distância;
	// Recursos restantes
	private BigDecimal restantes;
	
	// Dados calculados
	
	// Tempo entre ataques (em segundos)
	private BigDecimal intervalo;
	// Horário para enviar próximo ataque
	private BigDecimal enviarAtaque;
	// Se o saque total for maior do que o armazém
	private boolean armazémPequeno;
	
	
	protected Cálculo() {}
	
	/**
	 * Calcula o intervalo entre ataques e define se o armazém for pequeno
	 */
	private void setIntervalo() {
		
		if (saqueTotal.compareTo(armazenamento) == 1) {
			armazémPequeno = true;
			saqueTotal = armazenamento;
		}
		
		intervalo = saqueTotal.divide(produção, 30, RoundingMode.HALF_EVEN);
		
	}
	
	private void setEnviarAtaque() {
		
		
		
	}
	
	/**
	 * Determina o armazenamento total da aldeia (armazém-esconderijo)
	 * e a produção por segundo dos recursos.
	 * @param Mapa relacionando edifícios aos seus níveis
	 */
	protected void setProduçãoEArmazenamento(
				Map<Edifício, EdifícioFormattedComboBox> edifícios) {
		
		produção = BigDecimal.ZERO;
		
		for (Edifício ed : edifícios.keySet()) {
			
			switch (ed) {
			case ARMAZÉM:
				armazenamento = new BigDecimal("1.22949").pow(
						edifícios.get(ed).getSelectedIndex()-1)
						.multiply(new BigDecimal("1000")).setScale(0);
				break;
			case ESCONDERIJO:
				armazenamento = armazenamento.subtract(
						new BigDecimal("1.3335").pow(
							edifícios.get(ed).getSelectedIndex()-1).
							multiply(new BigDecimal("100"))).setScale(0);
				break;
			// Caso não seja nem armazém nem esconderijo, é um dos
			// produtores
			default:
				produção = produção.add(null);
			}
		}
		
		// Changes production from per hour to per second
		produção = produção.divide(new BigDecimal("3600"), 
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
			// Adiciona o saque unitário*quantidade
			saqueTotal = saqueTotal.add(
					quantidades.get(i).getValue().
					multiply(i.saque()));
			
			// Verifica se a unidade atual é mais lenta do que a registrada
			if (i.velocidade().compareTo(velocidade) == -1)
				velocidade = i.velocidade();
			
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
	protected void setRestantes(TroopFormattedTextField[] array) {
		
		restantes = BigDecimal.ZERO;
		
		for (TroopFormattedTextField i : array)
			restantes = restantes.add(i.getValue());
		
	}
	
}
