package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.enums.ItemPaladino;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.InputInfo;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.OutputInfo;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private ItemPaladino itemAtacante, itemDefensor;

	private boolean religiãoAtacante;
	
	private boolean itemCatapulta;

	// Calculated Values that are maintained during all combat

	private Army perdidoAtacante;
	private Army perdidoDefensor;
	
	// nível da muralha para adição de bônus (afetado por rams)

	private int muralhaCombate;

	// níveis dos edifícios depois de serem destruídos

	private double maxLevelLowered;

	private int muralhaFinal;
	private int edifícioFinal;

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
		
		atacante.setMoral(input.getMoral() / 100.0);
		atacante.setLuck(input.getSorte() / 100.0);
		atacante.setReligious(input.getReligiãoAtacante());
		
		// Aceita o item do paladino apenas se houver paladino
		if (Army.containsUnit("knight") && atacante.getQuantidade("knight") > 0)
			itemAtacante = input.getItemAtacante();
		else
			itemAtacante = ItemPaladino.NULL;
		atacante.setItem(itemAtacante);
		
		defensor.setReligious(input.getReligiãoDefensor());
		defensor.setNight(input.getNoite());
		
		if (Army.containsUnit("knight") && defensor.getQuantidade("knight") > 0)
			itemDefensor = input.getItemDefensor();
		else
			itemDefensor = ItemPaladino.NULL;
		defensor.setItem(itemDefensor);
		
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
		
		Army rams = new Army("ram");
		rams.addTropa(rams.getUnit("ram"), 1, atacante.getTropa("ram").getLevel());
		rams.setItem(itemAtacante);
		
		int attack = rams.getAtaque();
		
		double levelsLowered = atacante.getQuantidade("ram")* attack;

		if (!religiãoAtacante)
			levelsLowered /= 2;

		levelsLowered /= 8 * Math.pow(1.09, muralhaInicial);
				
		maxLevelLowered = levelsLowered;
		
		muralhaCombate = muralhaInicial - (int) maxLevelLowered;
		
		if (muralhaCombate < (double) muralhaInicial / attack)
			muralhaCombate = (int) Math.round((double) muralhaInicial / attack);

		if (muralhaCombate < 0)
			muralhaCombate = 0;

		muralhaFinal = muralhaInicial;
		defensor.setWall(muralhaCombate);
	}

	private void scoutBattle() {

		int atacantes = atacante.getQuantidade("spy");
		int defensores = defensor.getQuantidade("spy");

		if (atacantes > 0) {

			if (defensores / atacantes > 2)
				atacante.addTropa(atacante.getUnit("spy"), 0, 1);
			else {
				// remaining = initial - round (initial * (defenders /
				// 2*initial)^1.5 )
				
				double remaining = atacantes * Math.pow(defensores / (2.0 * atacantes), 1.5);

				remaining = atacantes - (int) remaining;

				atacante.addTropa(atacante.getUnit("spy"), (int) remaining, 1);
				perdidoAtacante.addTropa(atacante.getUnit("spy"),
						atacantes - (int) remaining, 1);

			}

		}

	}

	private void BattleArcherWorld() {
		
		// doing battles until only one survives
		boolean isFirst = true;

		// Only does the calculation if there are attacking units
		if (hasUnits(atacante))
		do {
			
			Map<UnitType, Double> attackLossRatio = new HashMap<UnitType, Double>();
			Map<UnitType, Double> defenseLossRatio = new HashMap<UnitType, Double>();
			
			double totalAttack = atacante.getAtaque();
			
			Map<UnitType, Double> attackTypes = new HashMap<UnitType, Double>();
			attackTypes.put(UnitType.General, (double) atacante.getAtaqueGeral());
			attackTypes.put(UnitType.Cavalry, (double) atacante.getAtaqueCavalaria());
			attackTypes.put(UnitType.Archer, (double) atacante.getAtaqueArqueiro());
			attackTypes.put(UnitType.unspecified, 0.0);
			
			Map<UnitType, Double> defenseTypes = new HashMap<UnitType, Double>();
			defenseTypes.put(UnitType.General, (double) defensor.getDefesaGeral());
			defenseTypes.put(UnitType.Cavalry, (double) defensor.getDefesaCavalaria());
			defenseTypes.put(UnitType.Archer, (double) defensor.getDefesaArqueiro());
			defenseTypes.put(UnitType.unspecified, (double) (20 + 50 * muralhaCombate));
			
			if (!isFirst)
				for (Entry<UnitType, Double> entry : defenseTypes.entrySet())
					entry.setValue(entry.getValue() - (20 + 50 * muralhaCombate));
			
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
			for (Troop t : atacante.getTropas())
				listAttack.add(t);
			
			for (Troop t : listAttack) {
				
				UnitType type = t.getUnit().getType();
				
				perdidoAtacante.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * attackLossRatio.get(type)), t.getLevel());
			
				atacante.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * (1 - attackLossRatio.get(type))), t.getLevel());
			}
			
			double defenseTotalLoss = 0;
			
			for (UnitType t : defenseLossRatio.keySet())
				defenseTotalLoss += attackTypes.get(t) * defenseLossRatio.get(t);

			defenseTotalLoss /= totalAttack;
			
			List<Troop> listDefense = new ArrayList<Troop>();
			for (Troop t : defensor.getTropas())
				listDefense.add(t);
			
			for (Troop t : listDefense) {
				
				perdidoDefensor.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * defenseTotalLoss), t.getLevel());
			
				defensor.addTropa(t.getUnit(), (int) Math.round(
						t.getQuantity() * (1 - defenseTotalLoss)), t.getLevel());
			
			}

			defensor.setReligious(true);
			atacante.setReligious(true);
			isFirst = false;

		} while (hasUnits(atacante) && hasUnits(defensor));

	}

	/**
	 * Método para a batalha em mundos sem arqueiros
	 */
	private void BattleNonArcherWorld() {

		double totalAttack = atacante.getAtaque();
		
		Map<UnitType, Double> attackTypes = new HashMap<UnitType, Double>();
		attackTypes.put(UnitType.General, (double) atacante.getAtaqueGeral());
		attackTypes.put(UnitType.Cavalry, (double) atacante.getAtaqueCavalaria());
		attackTypes.put(UnitType.Archer, (double) atacante.getAtaqueArqueiro());
		attackTypes.put(UnitType.unspecified, 0.0);
		
		Map<UnitType, Double> defenseTypes = new HashMap<UnitType, Double>();
		defenseTypes.put(UnitType.General, (double) defensor.getDefesaGeral());
		defenseTypes.put(UnitType.Cavalry, (double) defensor.getDefesaCavalaria());
		defenseTypes.put(UnitType.Archer, (double) defensor.getDefesaArqueiro());
		defenseTypes.put(UnitType.unspecified, (double) (20 + 50 * muralhaCombate));
		
		double totalDefense = 0;

		// Dando o valor ponderado de cada defesa, com base na proporção de
		// força atacante
		for (UnitType i : UnitType.values())
			totalDefense += defenseTypes.get(i) * attackTypes.get(i);
		totalDefense /= totalAttack;

		// ratio loss defesa = ( ataque total / defesa ) ^ 3/2

		// ratio loss ataque = 1 / ratio loss defesa

		double ataqueRatioLoss;
		double defenseRatioLoss;

		if (totalAttack == 0 && totalDefense == 0) {
			ataqueRatioLoss = 0;
			defenseRatioLoss = 0;
		} else if (totalAttack > totalDefense) {
			ataqueRatioLoss = Math.pow(totalDefense / totalAttack, 1.5);
			defenseRatioLoss = 1;
		} else {
			ataqueRatioLoss = 1;
			defenseRatioLoss = Math.pow(totalAttack / totalDefense, 1.5);;
		}
		
		List<Troop> listDefense = new ArrayList<Troop>();
		for (Troop t : defensor.getTropas())
			listDefense.add(t);
		
		for (Troop t : listDefense) {
			
			perdidoDefensor.addTropa(t.getUnit(), (int) Math.round(
					t.getQuantity() * defenseRatioLoss), t.getLevel());
		
			defensor.addTropa(t.getUnit(), (int) Math.round(
					t.getQuantity() * (1 - defenseRatioLoss)), t.getLevel());
		}

		List<Troop> listAttack = new ArrayList<Troop>();
		for (Troop t : atacante.getTropas())
			listAttack.add(t);
		
		for (Troop t : listAttack) {
			if (t.getName().equals("spy"))
				continue;
			
			perdidoAtacante.addTropa(t.getUnit(), (int) Math.round(
					t.getQuantity() * ataqueRatioLoss), t.getLevel());
		
			atacante.addTropa(t.getUnit(), (int) Math.round(
					t.getQuantity() * (1 - ataqueRatioLoss)), t.getLevel());
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

				muralhaFinal = muralhaInicial - (int) Math.round(maxLevelLowered * 
						(2 - (double) somaTropasAtacantesPerdidas/ somaTropasAtacantes));
			else
				// Atacante perdeu

				// final = initial - round(max * lostDefenders/totalDefenders)

				muralhaFinal = muralhaInicial - (int) Math.round(maxLevelLowered *
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

				// final = initial - round(max * (2-  lostAttackers/totalAttackers))
				
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

	private boolean hasUnits(Army army) {
		return army.getPopulação() - army.getTropa("spy").getPopulation() > 0;
	}

}
