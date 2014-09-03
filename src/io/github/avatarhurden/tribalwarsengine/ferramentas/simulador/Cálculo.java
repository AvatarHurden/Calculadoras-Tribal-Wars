package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.InputInfo;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.OutputInfo;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit.UnitType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import database.Bandeira;
import database.BigOperation;
import database.ItemPaladino;

/**
 * Classe que calcula a quantidade de tropas perdidas pelo atacante e defensor
 * 
 * Requer o nível de muralha, moral, sorte, tropas atacantes e tropas
 * defensoras.
 * 
 * Variáveis disponíveis são: 
 * <br>- Porcentagem de tropas defensivas perdidas (percDefesa) 
 * <br>- Porcentagem de tropas ofensivas geral perdidas (percGeral) 
 * <br>- Porcentagem de tropas ofensivas cavalaria perdidas (percCavalaria) 
 * <br>- Porcentagem de troaps ofensivas arqueiro perdidas (percArqueiro) 
 * <br>- HashMap com tropas perdidas (ataque e defesa) 
 * <br>- HashMap com tropas restantes (ataque e defesa)
 * 
 * @author Arthur
 * 
 */
public class Cálculo {

	// Values taken directly from SimuladorPanel

	private InputInfo input;

	private OutputInfo output;

	private Army atacante;
	private Army defensor;
	
	private int muralhaInicial;

	private int edifícioInicial;

	private int moral;

	private ItemPaladino itemAtacante, itemDefensor;

	private boolean itemCatapulta;

	private Bandeira bandeiraAtacante, bandeiraDefensor;

	private boolean religiãoAtacante, religiãoDefensor;

	private int sorte;

	private boolean noite;

	// Calculated Values that are maintained during all combat

	private BigDecimal ataqueTotal;

	private Army perdidoAtacante;
	private Army perdidoDefensor;
	
	// nível da muralha para adição de bônus (afetado por rams)

	private int muralhaBônusFlat;
	private int muralhaBônusPercentual;

	// níveis dos edifícios depois de serem destruídos

	BigDecimal maxLevelLowered;

	private int muralhaFinal;
	private int edifícioFinal;

	private int rounding = 10;

	public Cálculo(InputInfo input, OutputInfo output) {

		this.input = input;

		this.output = output;
		
		perdidoAtacante = new Army();
		perdidoDefensor = new Army();
	}

	public void calculate() {

		setInputVariables();

		damageWall();

		scoutBattle();

		if (itemAtacante == ItemPaladino.FOGUEIRA)
			itemCatapulta = true;

		// Removes ram and catapult item during combat
		if (itemAtacante == ItemPaladino.ESTRELA
				|| itemAtacante == ItemPaladino.FOGUEIRA)
			itemAtacante = ItemPaladino.NULL;

		if (Army.containsUnit("archer"))
			BattleArcherWorld();
		else
			BattleNonArcherWorld();

		// adds catapult item back to destroy buildings
		if (itemCatapulta == true)
			itemAtacante = ItemPaladino.FOGUEIRA;

		destroyBuildingAndWall();
		
		setOutputVariables();
		
	}

	private void setInputVariables() {

		atacante = input.getArmyAttacker();
		defensor = input.getArmyDefender();

		muralhaInicial = input.getMuralha();

		edifícioInicial = input.getEdifício();
		
		moral = input.getMoral();

		sorte = input.getSorte();

		religiãoAtacante = input.getReligiãoAtacante();

		religiãoDefensor = input.getReligiãoDefensor();
		
		// Aceita o item do paladino apenas se houver paladino
		if (Army.containsUnit("knight") && atacante.getQuantidade("knight") > 0)
			itemAtacante = input.getItemAtacante();
		else
			itemAtacante = ItemPaladino.NULL;

		atacante.setItem(itemAtacante);
		atacante.setLuck(sorte/100.0);
		atacante.setMoral(moral/100.0);
		atacante.setReligious(religiãoAtacante);
		
		if (Army.containsUnit("knight") && defensor.getQuantidade("knight") > 0)
			itemDefensor = input.getItemDefensor();
		else
			itemDefensor = ItemPaladino.NULL;

		bandeiraAtacante = input.getBandeiraAtacante();

		bandeiraDefensor = input.getBandeiraDefensor();

		noite = input.getNoite();
		
		defensor.setItem(itemDefensor);
		defensor.setReligious(religiãoDefensor);
		defensor.setNight(noite);
		defensor.setWall(muralhaInicial);
		
		for (Unit i : atacante.getUnits())
			perdidoAtacante.addTropa(i, 0, 1);

		for (Unit i : defensor.getUnits())
			perdidoAtacante.addTropa(i, 0, 1);

	}

	private void setOutputVariables() {

		output.setLostAttacker(perdidoAtacante);
		output.setLostDefender(perdidoDefensor);

		output.setMuralha(muralhaFinal);
		output.setEdifício(edifícioFinal);

	}

	private void damageWall() {

		// max = #rams*religion*attackOfRams / (8* 1.09^wallLevel)
		
		BigDecimal attack;
		
		if (itemAtacante.isUnit(atacante.getUnit("ram")))
			attack = new BigDecimal(atacante.getUnit("ram").getAttack() 
					* itemAtacante.getModifierAtk());
		else
			attack = new BigDecimal(atacante.getUnit("ram").getAttack());
		
		BigDecimal levelsLowered = new BigDecimal(atacante.getQuantidade("ram")).multiply(attack);

		if (religiãoAtacante == false)
			levelsLowered = levelsLowered.divide(new BigDecimal("2"));

		levelsLowered = levelsLowered.divide(new BigDecimal("8")
				.multiply(BigOperation.pow(new BigDecimal("1.09"),
						new BigDecimal(muralhaInicial))), rounding,
				RoundingMode.HALF_EVEN);

		maxLevelLowered = levelsLowered;
		
		muralhaBônusFlat = muralhaInicial
				- maxLevelLowered.setScale(0, RoundingMode.HALF_EVEN)
						.intValue();
		
//		if (muralhaBônusFlat < new BigDecimal(muralhaInicial).divide(attack)
//				.setScale(0, RoundingMode.HALF_UP).intValue())
//			muralhaBônusFlat = new BigDecimal(muralhaInicial).divide(attack)
//					.setScale(0, RoundingMode.HALF_UP).intValue();

		if (muralhaBônusFlat < 0)
			muralhaBônusFlat = 0;

		muralhaBônusPercentual = muralhaBônusFlat;

		muralhaFinal = muralhaInicial;
	}

	private void scoutBattle() {

		BigDecimal atacantes = new BigDecimal(atacante.getQuantidade("spy"));
		BigDecimal defensores = new BigDecimal(defensor.getQuantidade("spy"));

		if (atacantes.compareTo(BigDecimal.ZERO) > 0) {

			if (defensores.divide(atacantes, rounding, RoundingMode.HALF_EVEN)
					.compareTo(new BigDecimal("2")) > 0)
				atacante.addTropa(atacante.getUnit("spy"), 0, 1);
			else {
				// remaining = initial - round (initial * (defenders /
				// 2*initial)^1.5 )

				BigDecimal remaining = atacantes.multiply(BigOperation.pow(
						defensores.divide(
								new BigDecimal("2").multiply(atacantes),
								rounding, RoundingMode.HALF_EVEN),
						new BigDecimal("1.5")));

				remaining = atacantes.subtract(remaining.setScale(0,
						RoundingMode.HALF_EVEN));

				atacante.addTropa(atacante.getUnit("spy"), remaining.intValue(), 1);
				perdidoAtacante.addTropa(atacante.getUnit("spy"),
						atacantes.subtract(remaining).intValue(), 1);

			}

		}

	}

	private void setAtaques(Map<Unidade, BigDecimal> tropas) {

		ataqueTotal = BigDecimal.ZERO;

		ataqueTipos.put(UnidadeTipo.Geral, BigDecimal.ZERO);
		ataqueTipos.put(UnidadeTipo.Cavalo, BigDecimal.ZERO);
		ataqueTipos.put(UnidadeTipo.Arqueiro, BigDecimal.ZERO);
		ataqueTipos.put(UnidadeTipo.unspecified, BigDecimal.ZERO);

		for (Entry<Unidade, BigDecimal> i : tropas.entrySet()) {

			BigDecimal ataque = i.getKey().getAtaque(
					nívelTropasAtaque.get(i.getKey()), itemAtacante);

			ataqueTipos.put(
					i.getKey().getType(),
					ataqueTipos.get(i.getKey().getType()).add(
							i.getValue().multiply(ataque)));

		}

		// Calculando os modificadores (moral, sorte e religião)

		String religião;

		if (religiãoAtacante == true)
			religião = "1";
		else
			religião = "0.5";

		for (UnidadeTipo i : UnidadeTipo.values())
			ataqueTipos.put( i,
					ataqueTipos.get(i).multiply(
							new BigDecimal(moral).divide(new BigDecimal("100")))
								.multiply(BigDecimal.ONE.add(
											new BigDecimal(sorte)
													.divide(new BigDecimal("100")))
													.multiply(new BigDecimal(religião))));

		if (bandeiraAtacante != null)
			for (UnidadeTipo i : UnidadeTipo.values())
				ataqueTipos.put(i,	ataqueTipos.get(i).multiply(
								new BigDecimal(bandeiraAtacante.getValue())));

		for (UnidadeTipo i : UnidadeTipo.values())
			ataqueTotal = ataqueTotal.add(ataqueTipos.get(i));

	}

	private void setDefesas(Map<Unidade, BigDecimal> tropas) {

		defesaTipos.put(UnidadeTipo.Geral, BigDecimal.ZERO);
		defesaTipos.put(UnidadeTipo.Cavalo, BigDecimal.ZERO);
		defesaTipos.put(UnidadeTipo.Arqueiro, BigDecimal.ZERO);
		defesaTipos.put(UnidadeTipo.unspecified, BigDecimal.ZERO);

		for (Entry<Unidade, BigDecimal> i : tropas.entrySet()) {

			// I don't even know

			defesaTipos.put(UnidadeTipo.Geral,
					defesaTipos.get(UnidadeTipo.Geral).add(
							i.getValue().multiply(i.getKey().getDefGeral(
											nívelTropasDefesa.get(i.getKey()),
											itemDefensor))));

			defesaTipos.put(
					UnidadeTipo.Cavalo,
					defesaTipos.get(UnidadeTipo.Cavalo).add(
							i.getValue().multiply(
									i.getKey().getDefCav(
											nívelTropasDefesa.get(i.getKey()),
											itemDefensor))));

			defesaTipos.put(
					UnidadeTipo.Arqueiro,
					defesaTipos.get(UnidadeTipo.Arqueiro).add(
							i.getValue().multiply(
									i.getKey().getDefArq(
											nívelTropasDefesa.get(i.getKey()),
											itemDefensor))));

		}

		// Calculando os modificadores (religião, noite e bandeira)

		if (religiãoDefensor == false)
			for (UnidadeTipo i : UnidadeTipo.values())
				defesaTipos.put(
						i,
						defesaTipos.get(i).divide(new BigDecimal("2"),
								rounding, RoundingMode.HALF_EVEN));

		if (noite == true)
			for (UnidadeTipo i : UnidadeTipo.values())
				defesaTipos.put(i,
						defesaTipos.get(i).multiply(new BigDecimal("2")));

		if (bandeiraDefensor != null)
			for (UnidadeTipo i : UnidadeTipo.values())
				defesaTipos.put(
						i,
						defesaTipos.get(i).multiply(
								new BigDecimal(bandeiraDefensor.getValue())));

	}

	private void BattleArcherWorld() {
		
		// doing battles until only one survives

		// Maps com as unidades restantes de cada exército. Inicialmente são
		// iguais às enviadas, depois são diminuídas
		// conforme cada rodada de batalhas

		Army restanteAtacante = new Army();
		
		for (Troop i : atacante.getTropas())
			restanteAtacante.addTropa(i);

		Army restanteDefensor = new Army();
		
		for (Troop i : defensor.getTropas())
			restanteDefensor.addTropa(i);
		
		restanteAtacante.setItem(itemAtacante);
		restanteAtacante.setMoral(moral/100.0);
		restanteAtacante.setLuck(sorte/100.0);
		restanteAtacante.setReligious(religiãoAtacante);
		
		restanteDefensor.setItem(itemDefensor);
		restanteDefensor.setWall(muralhaBônusFlat);
		restanteDefensor.setNight(noite);
		restanteDefensor.setReligious(religiãoDefensor);
		boolean isFirst = true;

		
		// Only does the calculation if there are attacking units
		if (hasUnits(restanteAtacante))
		do {
			
			Map<UnitType, Double> attackLossRatio = new HashMap<UnitType, Double>();
			Map<UnitType, Double> defenseLossRatio = new HashMap<UnitType, Double>();
			
			double totalAttack = restanteAtacante.getAtaque();
			
			Map<UnitType, Double> attackTypes = new HashMap<UnitType, Double>();
			attackTypes.put(UnitType.General, (double) restanteAtacante.getAtaqueGeral());
			attackTypes.put(UnitType.Cavalry, (double) restanteAtacante.getAtaqueCavalaria());
			attackTypes.put(UnitType.Archer, (double) restanteAtacante.getAtaqueArqueiro());
			attackTypes.put(UnitType.unspecified, 0.0);
			
			Map<UnitType, Double> defenseTypes = new HashMap<UnitType, Double>();
			defenseTypes.put(UnitType.General, (double) restanteDefensor.getDefesaGeral());
			defenseTypes.put(UnitType.Cavalry, (double) restanteDefensor.getDefesaCavalaria());
			defenseTypes.put(UnitType.Archer, (double) restanteDefensor.getDefesaArqueiro());
			defenseTypes.put(UnitType.unspecified, (double) (20 + 50 * muralhaBônusFlat));
			
			if (!isFirst)
				for (Entry<UnitType, Double> entry : defenseTypes.entrySet())
					entry.setValue(entry.getValue() - (20 + 50 * muralhaBônusFlat));
			
			for (Entry<UnitType, Double> entry : defenseTypes.entrySet()) {
				if (totalAttack == 0 && entry.getValue() == 0) {
					
					attackLossRatio.put(entry.getKey(), 0.0);
					defenseLossRatio.put(entry.getKey(), 0.0);
					
				}  else if (totalAttack > entry.getValue()) {
					
					attackLossRatio.put(entry.getKey(), Math.pow(
							entry.getValue() / totalAttack, 1.5));
					defenseLossRatio.put(entry.getKey(), 1.0);
					
				} else {
					
					attackLossRatio.put(entry.getKey(), 1.0);
					defenseLossRatio.put(entry.getKey(), Math.pow(
							totalAttack / entry.getValue(), 1.5));
					
				}	
			}
			
			List<Troop> listAttack = new ArrayList<Troop>();
			for (Troop t : restanteAtacante.getTropas())
				listAttack.add(t);
			
			for (Troop t : listAttack) {
				
				UnitType type = t.getUnit().getType();
				
				perdidoAtacante.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * attackLossRatio.get(type)), t.getLevel());
			
				restanteAtacante.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * (1 - attackLossRatio.get(type))), t.getLevel());
			}
			
			double defenseTotalLoss = 0;
			
			for (UnitType t : defenseLossRatio.keySet())
				defenseTotalLoss += attackTypes.get(t) * defenseLossRatio.get(t);

			defenseTotalLoss /= totalAttack;
			
			List<Troop> listDefense = new ArrayList<Troop>();
			for (Troop t : restanteDefensor.getTropas())
				listDefense.add(t);
			
			for (Troop t : listDefense) {
				
				perdidoDefensor.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * defenseTotalLoss), t.getLevel());
			
				restanteDefensor.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * (1 - defenseTotalLoss)), t.getLevel());
			
			}

			restanteDefensor.setReligious(true);
			restanteAtacante.setReligious(true);
			isFirst = false;

		} while (hasUnits(restanteAtacante) && hasUnits(restanteDefensor));

	}

	/**
	 * Método para a batalha em mundos sem arqueiros
	 */
	private void BattleNonArcherWorld() {

		setAtaques(tropasAtacantes);

		setDefesas(tropasDefensoras);

		BigDecimal defesaTotal = BigDecimal.ZERO;

		// Dando o valor ponderado de cada defesa, com base na proporção de
		// força atacante
		for (UnidadeTipo i : UnidadeTipo.values())
			defesaTotal = defesaTotal.add(defesaTipos.get(i).multiply(
					ataqueTipos.get(i)));

		defesaTotal = defesaTotal.divide(ataqueTotal, rounding,
				RoundingMode.HALF_EVEN);

		// Adicionar bônus de muralha
		defesaTotal = defesaTotal.multiply(Edifício.MURALHA
				.bônusPercentual(muralhaBônusPercentual));

		defesaTotal = defesaTotal.add(Edifício.MURALHA
				.bônusFlat(muralhaBônusFlat));

		// ratio loss defesa = ( ataque total / defesa ) ^ 3/2

		// ratio loss ataque = 1 / ratio loss defesa

		BigDecimal ataqueRatioLoss;

		BigDecimal defenseRatioLoss;

		if (ataqueTotal.compareTo(BigDecimal.ZERO) == 0
				&& defesaTotal.compareTo(BigDecimal.ZERO) == 0) {
			ataqueRatioLoss = BigDecimal.ZERO;
			defenseRatioLoss = BigDecimal.ZERO;
		} else if (ataqueTotal.compareTo(defesaTotal) == 1) {
			ataqueRatioLoss = BigOperation.pow(defesaTotal.divide(ataqueTotal,
					rounding, RoundingMode.HALF_EVEN), new BigDecimal("1.5"));
			defenseRatioLoss = BigDecimal.ONE;
		} else {
			ataqueRatioLoss = BigDecimal.ONE;
			defenseRatioLoss = BigOperation.pow(ataqueTotal.divide(defesaTotal,
					rounding, RoundingMode.HALF_EVEN), new BigDecimal("1.5"));
		}

		for (Entry<Unidade, BigDecimal> i : tropasDefensoras.entrySet()) {
			// Colocar as tropas perdidas na batalha num map para poder usar
			// depois
			tropasPerdidasDefesa.put(i.getKey(),
					i.getValue().multiply(defenseRatioLoss)
					.setScale(0,RoundingMode.HALF_UP));
			// Remover as tropas perdidas do mapa adequado
			i.setValue(i.getValue()
					.multiply(BigDecimal.ONE.subtract(defenseRatioLoss))
					.setScale(0, RoundingMode.HALF_UP));
		}

		for (Entry<Unidade, BigDecimal> i : tropasAtacantes.entrySet()) {

			if (!i.getKey().equals(Unidade.EXPLORADOR)) {
				// Colocar as tropas perdidas na batalha num map para poder usar
				// depois
				tropasPerdidasAtaque.put(i.getKey(),
						i.getValue().multiply(ataqueRatioLoss)
						.setScale(0, RoundingMode.HALF_UP));
				// Remover as tropas perdidas do mapa adequado
				i.setValue(i.getValue()
						.multiply(BigDecimal.ONE.subtract(ataqueRatioLoss))
						.setScale(0, RoundingMode.HALF_UP));
			}
		}

	}

	private void destroyBuildingAndWall() {

		// Sum of lost troops and total troops

		int somaTropasAtacantes = 0;
		for (Troop i : atacante.getTropas())
			somaTropasAtacantes += i.getQuantity();

		int somaTropasAtacantesPerdidas = 0;
		for (Troop i : perdidoAtacante.getTropas())
			somaTropasAtacantes += i.getQuantity();

		int somaTropasDefensoras = 0;
		for (Troop i : defensor.getTropas())
			somaTropasDefensoras += i.getQuantity();

		int somaTropasDefensorasPerdidas = 0;
		for (Troop i : perdidoDefensor.getTropas())
			somaTropasDefensorasPerdidas += i.getQuantity();
		
		// Downgrading wall

		if (atacante.getQuantidade("ram") > 0 && muralhaInicial > 0) {

			if (somaTropasDefensoras == 0) // Defensor perdeu

				// final = initial - round(max * (2-
				// lostAttackers/totalAttackers))

				muralhaFinal = muralhaInicial - (int) Math.round(maxLevelLowered.intValue() * 
						(2 - (double) somaTropasAtacantesPerdidas/ somaTropasAtacantes));
			else
				// Atacante perdeu

				// final = initial - round(max * lostDefenders/totalDefenders)

				muralhaFinal = muralhaInicial - (int) Math.round(maxLevelLowered.intValue() *
						(double) somaTropasDefensorasPerdidas / somaTropasDefensoras);

			if (muralhaFinal < 0)
				muralhaFinal = 0;

		}

		// Downgrading Building
		
		edifícioFinal = edifícioInicial;

		if (atacante.getQuantidade("catapult") > 0 && edifícioInicial > 0) {

			double levelsLowered = atacante.getTropa("catapult").getAttack(itemAtacante);

			levelsLowered /= 600.0 * Math.pow(1.09, edifícioInicial);

			if (somaTropasDefensoras == 0) // Defensor perdeu

				// final = initial - round(max * (2-
				// lostAttackers/totalAttackers))
				
				edifícioFinal = edifícioInicial - (int) Math.round(levelsLowered * 
						(2 - (double) somaTropasAtacantesPerdidas/ somaTropasAtacantes));
			else
				// Atacante perdeu

				// final = initial - round(max * lostDefenders/totalDefenders)

				edifícioFinal = edifícioInicial - (int) Math.round(levelsLowered *
						(double) somaTropasDefensorasPerdidas / somaTropasDefensoras);

			if (edifícioFinal < 0)
				edifícioFinal = 0;

		}

	}

	protected void printLostUnits() {

		System.out.println("Unidades Ofensivas");

		for (Unidade i : tropasPerdidasAtaque.keySet())
			System.out.println(i.getNome()
					+ ": "
					+ tropasPerdidasAtaque.get(i).setScale(0,
							RoundingMode.HALF_DOWN));

		System.out.println();
		System.out.println();
		System.out.println("Unidades Defensivas");

		for (Unidade i : tropasPerdidasDefesa.keySet())
			System.out.println(i.getNome()
					+ ": "
					+ tropasPerdidasDefesa.get(i).setScale(0,
							RoundingMode.HALF_DOWN));

		System.out.println();
		System.out.println("Nível Muralha: " + muralhaFinal);

	}

	/**
	 * Checks to see if the map has any units left.
	 * 
	 * @param map
	 *            The map with the units and the amounts
	 * @return If the map has more than 0 units, returns true. Else, returns
	 *         false;
	 */
	private boolean hasUnits(Army army) {

		return army.getPopulação() - army.getTropa("spy").getPopulation() > 0;
		
	}

}
