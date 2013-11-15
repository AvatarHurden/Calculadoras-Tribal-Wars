package simulador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import database.BigOperation;
import database.Edifício;
import database.Unidade;
import database.Unidade.Type;

/**
 * Classe que calcula a quantidade de tropas perdidas pelo atacante e defensor
 * 
 * Requer o nível de muralha, moral, sorte, tropas atacantes e tropas defensoras.
 * 
 * Variáveis disponíveis são:
 * - Porcentagem de tropas defensivas perdidas (percDefesa)
 * - Porcentagem de tropas ofensivas geral perdidas (percGeral)
 * - Porcentagem de tropas ofensivas cavalaria perdidas (percCavalaria)
 * - Porcentagem de troaps ofensivas arqueiro perdidas (percArqueiro)
 * - HashMap com tropas perdidas (ataque e defesa)
 * - HashMap com tropas restantes (ataque e defesa)
 * 
 * @author Arthur
 *
 */
public class Cálculo {

	// Values taken directly from GUI
	
		private GUI gui;
	
		private Map<Unidade, BigDecimal> tropasAtacantes = new HashMap<Unidade, BigDecimal>();
		private Map<Unidade, BigDecimal> tropasDefensoras = new HashMap<Unidade, BigDecimal>();
	
		private Map<Unidade, Integer> nívelTropasAtaque = new HashMap<Unidade, Integer>();
		private Map<Unidade, Integer> nívelTropasDefesa = new HashMap<Unidade, Integer>();
		
		private int muralhaInicial;
	
		private int edifícioInicial;
		
		private int moral;
		
		private boolean religiãoAtacante, religiãoDefensor;
		
		private int sorte;
		
	// Calculated Values that are maintaned during all combat
		
		private BigDecimal ataqueTotal;
		
		private Map<Unidade.Type, BigDecimal> ataqueTipos = new HashMap<Unidade.Type, BigDecimal>();
		private Map<Unidade.Type, BigDecimal> defesaTipos = new HashMap<Unidade.Type, BigDecimal>();
		
		private Map<Unidade, BigDecimal> tropasPerdidasAtaque = new HashMap<Unidade, BigDecimal>();
		private Map<Unidade, BigDecimal> tropasPerdidasDefesa = new HashMap<Unidade, BigDecimal>();
		
		// nível da muralha para adição de bônus (afetado por rams)
		
		private int muralhaBônusFlat;
		private int muralhaBônusPercentual;
	
		// níveis dos edifícios depois de serem destruídos
		
		int maxLevelLowered;
		
		private int muralhaFinal;
		private int edifícioFinal;
		
	public Cálculo(GUI gui) {
		
		this.gui = gui;
		
		setVariables(gui);
		
		damageWall();
		
		scoutBattle();
		
//		if (MundoSelecionado.hasArqueiro())
			BattleArcherWorld();
//		else
//			BattleNonArcherWorld();
			
		destroyBuildingAndWall();	
			
		printLostUnits();	
	}
	
	private void setVariables(GUI gui) {
		
		tropasAtacantes = gui.getListaAtacante();
		tropasDefensoras = gui.getListaDefensor();
		
		for (Unidade i : tropasAtacantes.keySet())
			nívelTropasAtaque.put(i, 1);
		
		for (Unidade i : tropasDefensoras.keySet())
			nívelTropasDefesa.put(i, 1);
		
		muralhaInicial = gui.getNívelMuralha();
		
		moral = gui.getMoral();
		
		sorte = gui.getSorte();
		
		religiãoAtacante = gui.getReligiãoAtacante();
		
		religiãoDefensor = gui.getReligiãoDefensores();
		
		for (Unidade i : tropasAtacantes.keySet())
			tropasPerdidasAtaque.put(i, BigDecimal.ZERO);
		
		for (Unidade i : tropasDefensoras.keySet())
			tropasPerdidasDefesa.put(i, BigDecimal.ZERO);
		
	}
	
	private void damageWall() {
		
		// max = #rams*attackOfRams / (8* 1.09^wallLevel)
		
		BigDecimal levelsLowered = tropasAtacantes.get(Unidade.ARÍETE).multiply(new BigDecimal(Unidade.ARÍETE.ataque(nívelTropasAtaque.get(Unidade.ARÍETE))));
		
		levelsLowered = levelsLowered.divide(new BigDecimal("8").multiply(
				BigOperation.pow(new BigDecimal("1.09"), new BigDecimal(muralhaInicial))), 30, RoundingMode.HALF_EVEN);
		
		maxLevelLowered = levelsLowered.setScale(0, RoundingMode.HALF_DOWN).intValue();
		
		muralhaBônusFlat = muralhaInicial - maxLevelLowered;
		muralhaBônusPercentual = muralhaBônusFlat;
		
	}
	
	private void scoutBattle() {
		
		BigDecimal atacantes = tropasAtacantes.get(Unidade.EXPLORADOR);
		BigDecimal defensores = tropasDefensoras.get(Unidade.EXPLORADOR);
		
		if (atacantes.compareTo(BigDecimal.ZERO) > 0){
			
			if (defensores.divide(atacantes, 30, RoundingMode.HALF_EVEN).compareTo(new BigDecimal("2")) > 0)
				tropasAtacantes.put(Unidade.EXPLORADOR, BigDecimal.ZERO);
			else {
				// remaining = initial - round (initial * (defenders / 2*initial)^1.5 )
				
				BigDecimal remaining = atacantes.multiply(
						BigOperation.pow(defensores.divide(new BigDecimal("2").multiply(atacantes), 30, RoundingMode.HALF_EVEN), new BigDecimal("1.5")));
				
				remaining = atacantes.subtract(remaining.setScale(0, RoundingMode.HALF_EVEN));		
						
				tropasAtacantes.put(Unidade.EXPLORADOR, remaining);
				tropasPerdidasAtaque.put(Unidade.EXPLORADOR, atacantes.subtract(remaining));
				
			}
			
		}
		
	}
	
	private void setAtaques(Map<Unidade, BigDecimal> tropas) {
		
		ataqueTotal = BigDecimal.ZERO;
		
		ataqueTipos.put(Type.Geral, BigDecimal.ZERO);
		ataqueTipos.put(Type.Cavalo, BigDecimal.ZERO);
		ataqueTipos.put(Type.Arqueiro, BigDecimal.ZERO);
		ataqueTipos.put(Type.unspecified, BigDecimal.ZERO);
		
		for (Entry<Unidade, BigDecimal> i : tropas.entrySet()) {
			
			ataqueTipos.put( i.getKey().type() , 
					ataqueTipos.get( i.getKey().type() ).add( i.getValue().multiply(new BigDecimal(i.getKey().ataque(nívelTropasAtaque.get(i.getKey()))) ) ));
			
			ataqueTotal = ataqueTotal.add( i.getValue().multiply( new BigDecimal(i.getKey().ataque(nívelTropasAtaque.get(i.getKey()))) ) );
		
		}
		
		// Calculando os modificadores (moral, sorte e religião)
		
		double religião;
		
		if (religiãoAtacante == true)
			religião = 1;
		else
			religião = 0.5;
			
		for (Type i : Type.values())
			ataqueTipos.put(i, ataqueTipos.get(i).multiply(new BigDecimal(moral).divide(new BigDecimal("100")))
												 .multiply(BigDecimal.ONE.add(new BigDecimal(sorte).divide(new BigDecimal("100")))
												 .multiply(new BigDecimal(religião))));
		
		ataqueTotal = ataqueTotal.multiply(new BigDecimal(moral).divide(new BigDecimal("100")))
								 .multiply(BigDecimal.ONE.add(new BigDecimal(sorte).divide(new BigDecimal("100")))
								 .multiply(new BigDecimal(religião)));
		
	}
	
	private void setDefesas(Map<Unidade, BigDecimal> tropas) {
		
		defesaTipos.put(Type.Geral, BigDecimal.ZERO);
		defesaTipos.put(Type.Cavalo, BigDecimal.ZERO);
		defesaTipos.put(Type.Arqueiro, BigDecimal.ZERO);
		defesaTipos.put(Type.unspecified, BigDecimal.ZERO);
		
		for (Entry<Unidade, BigDecimal> i : tropas.entrySet()) {
			
			// I don't even know
			
			defesaTipos.put(Type.Geral, 
					defesaTipos.get(Type.Geral).add( i.getValue().multiply(new BigDecimal(i.getKey().defGeral(nívelTropasDefesa.get(i.getKey()))) ) ));
			
			defesaTipos.put(Type.Cavalo, 
					defesaTipos.get(Type.Cavalo).add( i.getValue().multiply(new BigDecimal(i.getKey().defCav(nívelTropasDefesa.get(i.getKey()))) ) ));
			
			defesaTipos.put(Type.Arqueiro, 
					defesaTipos.get(Type.Arqueiro).add( i.getValue().multiply(new BigDecimal(i.getKey().defArq(nívelTropasDefesa.get(i.getKey()))) ) ));
			
		}

		// Calculando os modificadores (moral, sorte e religião)
		
		double religião;
				
		if (religiãoDefensor == true)
			religião = 1;
		else
			religião = 0.5;
				
		for (Type i : Type.values())
			defesaTipos.put(i, defesaTipos.get(i).multiply(new BigDecimal(religião)));
				

	}
		
	private void BattleArcherWorld() {
		
		// doing battles until only one survives
		
		// Maps com as unidades restantes de cada exército. Inicialmente são iguais às enviadas, depois são diminuídas
		// conforme cada rodada de batalhas
	
		Map<Unidade, BigDecimal> atacantesSobrando = new HashMap<Unidade, BigDecimal>();
		
		for (Entry<Unidade, BigDecimal> i : tropasAtacantes.entrySet())
			atacantesSobrando.put(i.getKey(), i.getValue());
		
		Map<Unidade, BigDecimal> defensoresSobrando = new HashMap<Unidade, BigDecimal>();
		
		for (Entry<Unidade, BigDecimal> i : tropasDefensoras.entrySet())
			defensoresSobrando.put(i.getKey(), i.getValue());
		
		while (hasUnits(atacantesSobrando) && hasUnits(defensoresSobrando)) {
			
			// Define o valor do ataque de cada tipo presente no exército, além do ataque total (soma dos 3 tipos)
			setAtaques(atacantesSobrando);
			
			// Define o valor de defesa para cada tipo de tropa
			setDefesas(defensoresSobrando);
			
			// Porcentagem de tropas perdidas para cada tipo de unidade
			// No ataque, as perdas são apenas para o tipo de tropa representado
			Map<Type, BigDecimal> ataqueRatioLoss = new HashMap<Type, BigDecimal>();
			// Na defesa, é feita uma média ponderada das razões, e todas as tropas perdidas são nessa razão
			Map<Type, BigDecimal> defenseRatioLoss = new HashMap<Type, BigDecimal>();
			
			for (Type i : Type.values()) {
				
				// Adiciona o bônus de muralha na defesa
				defesaTipos.put(i, defesaTipos.get(i).multiply(Edifício.MURALHA.bônusPercentual(muralhaBônusPercentual))
						.add(Edifício.MURALHA.bônusFlat(muralhaBônusFlat)));
				
				// ratio loss defesa = ( ataque total / defesa ) ^ 3/2
				
				// ratio loss ataque = 1 / ratio loss defesa
				
				if (ataqueTotal.compareTo(BigDecimal.ZERO) == 0 && defesaTipos.get(i).compareTo(BigDecimal.ZERO) == 0){
					ataqueRatioLoss.put(i, BigDecimal.ZERO);
					defenseRatioLoss.put(i, BigDecimal.ZERO);
				} else if (ataqueTotal.compareTo(defesaTipos.get(i)) == 1 ){
					ataqueRatioLoss.put(i, BigOperation.pow(defesaTipos.get(i).divide(ataqueTotal, 30, RoundingMode.HALF_EVEN), new BigDecimal("1.5")));
					defenseRatioLoss.put(i, BigDecimal.ONE);
				} else {
					ataqueRatioLoss.put(i, BigDecimal.ONE);
					defenseRatioLoss.put(i, BigOperation.pow(ataqueTotal.divide(defesaTipos.get(i), 30, RoundingMode.HALF_EVEN), new BigDecimal("1.5")));
				}
					
			}
			
			for (Entry<Unidade, BigDecimal> i : atacantesSobrando.entrySet()) {
				// Colocar as tropas perdidas na batalha num map para poder usar depois
				tropasPerdidasAtaque.put(i.getKey(), tropasPerdidasAtaque.get(i.getKey()).add(i.getValue().
						multiply(ataqueRatioLoss.get(i.getKey().type())).setScale(0, RoundingMode.HALF_UP)));
				// remover as tropas perdidas do mapa adequado
				i.setValue(i.getValue().multiply(BigDecimal.ONE.subtract(ataqueRatioLoss.get(i.getKey().type()))).setScale(0, RoundingMode.HALF_UP));
			}
			
			BigDecimal defenseTotalLoss = BigDecimal.ZERO;
			
			// Fazer a média ponderada das perdas defensivas
			
			for (Type i : Type.values())
				defenseTotalLoss = defenseTotalLoss.add(ataqueTipos.get(i).multiply(defenseRatioLoss.get(i)));
				
			defenseTotalLoss = defenseTotalLoss.divide(ataqueTotal, 30, RoundingMode.HALF_EVEN);
			
			
			for (Entry<Unidade, BigDecimal> i : defensoresSobrando.entrySet()) {
				// Colocar as tropas perdidas na batalha num map para poder usar depois
				tropasPerdidasDefesa.put(i.getKey(), tropasPerdidasDefesa.get(i.getKey()).add(i.getValue().
						multiply(defenseTotalLoss)).setScale(0, RoundingMode.HALF_UP));
				// Remover as tropas perdidas do mapa adequado
				i.setValue(i.getValue().multiply(BigDecimal.ONE.subtract(defenseTotalLoss)).setScale(0, RoundingMode.HALF_UP));
			}
			
			// After first round of attacks, wall becomes 0
			
			muralhaBônusFlat = 0;
			
		}
		
	}
	
	
	/**
	 * Método para a batalha em mundos sem arqueiros
	 */
	private void BattleNonArcherWorld() {
		
		setAtaques(tropasAtacantes);
		
		setDefesas(tropasDefensoras);
		
		BigDecimal defesaTotal = BigDecimal.ZERO;
		
		// Dando o valor ponderado de cada defesa, com base na proporção de força atacante
		for (Type i : Type.values())
			defesaTotal = defesaTotal.add(defesaTipos.get(i).multiply(ataqueTipos.get(i)));
		
		defesaTotal = defesaTotal.divide(ataqueTotal, 30, RoundingMode.HALF_EVEN);
		
		// Adicionar bônus de muralha
		defesaTotal = defesaTotal.multiply(Edifício.MURALHA.bônusPercentual(muralhaBônusPercentual));
		
		defesaTotal = defesaTotal.add(Edifício.MURALHA.bônusFlat(muralhaBônusFlat));
		
		
		// ratio loss defesa = ( ataque total / defesa ) ^ 3/2
		
		// ratio loss ataque = 1 / ratio loss defesa
		
		BigDecimal ataqueRatioLoss;
		
		BigDecimal defenseRatioLoss;
		
		if (ataqueTotal.compareTo(BigDecimal.ZERO) == 0 && defesaTotal.compareTo(BigDecimal.ZERO) == 0){
			ataqueRatioLoss = BigDecimal.ZERO;
			defenseRatioLoss = BigDecimal.ZERO;
		} else if (ataqueTotal.compareTo(defesaTotal) == 1 ){
			ataqueRatioLoss = BigOperation.pow(defesaTotal.divide(ataqueTotal, 30, RoundingMode.HALF_EVEN), new BigDecimal("1.5"));
			defenseRatioLoss = BigDecimal.ONE;
		} else {
			ataqueRatioLoss = BigDecimal.ONE;
			defenseRatioLoss = BigOperation.pow(ataqueTotal.divide(defesaTotal, 30, RoundingMode.HALF_EVEN), new BigDecimal("1.5"));
		}
		
		for (Entry<Unidade, BigDecimal> i : tropasDefensoras.entrySet()) {
			// Colocar as tropas perdidas na batalha num map para poder usar depois
			tropasPerdidasDefesa.put(i.getKey(), i.getValue().multiply(defenseRatioLoss)).setScale(0, RoundingMode.HALF_UP);
			// Remover as tropas perdidas do mapa adequado
			i.setValue(i.getValue().multiply(BigDecimal.ONE.subtract(defenseRatioLoss)).setScale(0, RoundingMode.HALF_UP));
		}
		
		for (Entry<Unidade, BigDecimal> i : tropasAtacantes.entrySet()) {
			
			if (!i.getKey().equals(Unidade.EXPLORADOR)){
				// Colocar as tropas perdidas na batalha num map para poder usar depois
				tropasPerdidasAtaque.put(i.getKey(), i.getValue().multiply(ataqueRatioLoss)).setScale(0, RoundingMode.HALF_UP);
				// Remover as tropas perdidas do mapa adequado
				i.setValue(i.getValue().multiply(BigDecimal.ONE.subtract(ataqueRatioLoss)).setScale(0, RoundingMode.HALF_UP));
			}
		}
		
	}
	
	private void destroyBuildingAndWall() {
		
		// Sum of lost troops and total troops
		
		BigDecimal somaTropasAtacantes = BigDecimal.ZERO;
		for (BigDecimal i : tropasAtacantes.values())
			somaTropasAtacantes = somaTropasAtacantes.add(i);
			
		BigDecimal somaTropasAtacantesPerdidas = BigDecimal.ZERO;
		for (BigDecimal i : tropasPerdidasAtaque.values())
			somaTropasAtacantesPerdidas = somaTropasAtacantesPerdidas.add(i);
		
		BigDecimal somaTropasDefensoras = BigDecimal.ZERO;
		for (BigDecimal i : tropasDefensoras.values())
			somaTropasDefensoras = somaTropasDefensoras.add(i);
		
		BigDecimal somaTropasDefensorasPerdidas = BigDecimal.ZERO;
		for (BigDecimal i : tropasPerdidasDefesa.values())
			somaTropasDefensorasPerdidas = somaTropasDefensorasPerdidas.add(i);
		
		// Downgrading wall
		
		if (tropasAtacantes.get(Unidade.ARÍETE).compareTo(BigDecimal.ZERO) > 0 && muralhaInicial > 0) {
			
			
			if (tropasPerdidasDefesa.equals(tropasDefensoras))   // Defensor perdeu
				
				// final = initial - round(max * (2- lostAttackers/totalAttackers))
				
				muralhaFinal = muralhaInicial - new BigDecimal(maxLevelLowered).multiply((new BigDecimal("2").subtract( 
						somaTropasAtacantesPerdidas.divide(somaTropasAtacantes, 30, RoundingMode.HALF_EVEN)))).setScale(0, RoundingMode.HALF_DOWN).intValue();
			else												// Atacante perdeu
				
				// final = initial - round(max * lostDefenders/totalDefenders)
				
				muralhaFinal = muralhaInicial - new BigDecimal(maxLevelLowered).multiply(somaTropasDefensorasPerdidas)
						.divide(somaTropasDefensoras, 30 , RoundingMode.HALF_EVEN).setScale(0, RoundingMode.HALF_DOWN).intValue();
			
			if (muralhaFinal < 0 )
				muralhaFinal = 0;
			
		} 
		
		
		// Downgrading Building
		
		if (tropasAtacantes.get(Unidade.CATAPULTA).compareTo(BigDecimal.ZERO) > 0 && edifícioInicial > 0 ) {
			
			BigDecimal levelsLowered = tropasAtacantes.get(Unidade.CATAPULTA).multiply(new BigDecimal(Unidade.CATAPULTA.ataque(nívelTropasAtaque.get(Unidade.CATAPULTA))));
			
			levelsLowered = levelsLowered.divide(new BigDecimal("600").multiply(
					BigOperation.pow(new BigDecimal("1.09"), new BigDecimal(edifícioInicial))), 30, RoundingMode.HALF_EVEN);
			
			
			if (tropasPerdidasDefesa.equals(tropasDefensoras))   // Defensor perdeu
				
				// final = initial - round(max * (2- lostAttackers/totalAttackers))
				
				edifícioFinal = edifícioInicial - levelsLowered.multiply((new BigDecimal("2").subtract( 
						somaTropasAtacantesPerdidas.divide(somaTropasAtacantes, 30, RoundingMode.HALF_EVEN)))).setScale(0, RoundingMode.HALF_DOWN).intValue();
			else												// Atacante perdeu
				
				// final = initial - round(max * lostDefenders/totalDefenders)
				
				edifícioFinal = edifícioInicial - levelsLowered.multiply(somaTropasDefensorasPerdidas)
						.divide(somaTropasDefensoras, 30 , RoundingMode.HALF_EVEN).setScale(0, RoundingMode.HALF_DOWN).intValue();
			
			if (edifícioFinal < 0 )
				edifícioFinal = 0;
			
		} 
		
	}
	
	
	protected void printLostUnits() {
		
		System.out.println("Unidades Ofensivas");
		
		for (Unidade i : tropasPerdidasAtaque.keySet())
			System.out.println(i.nome()+": "+tropasPerdidasAtaque.get(i).setScale(0, RoundingMode.HALF_DOWN));
		
		System.out.println();
		System.out.println();
		System.out.println("Unidades Defensivas");
		
		for (Unidade i : tropasPerdidasDefesa.keySet())
			System.out.println(i.nome()+": "+tropasPerdidasDefesa.get(i).setScale(0, RoundingMode.HALF_DOWN));
		
		System.out.println();
		System.out.println("Nível Muralha: "+muralhaFinal);
		
	}
	
	/**
	 * Checks to see if the map has any units left.
	 * @param map The map with the units and the amounts
	 * @return If the map has more than 0 units, returns true. Else, returns false;
	 */
	private boolean hasUnits(Map<Unidade, BigDecimal> map) {
		
		for (Entry<Unidade, BigDecimal> i : map.entrySet()) {
			
			if (i.getValue().compareTo(BigDecimal.ZERO) == 1)
				return true;
			
			
		}
		
		return false;
		
	}
	
	
}
