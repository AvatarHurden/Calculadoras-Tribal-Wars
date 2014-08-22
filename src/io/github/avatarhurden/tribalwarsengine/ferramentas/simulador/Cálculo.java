package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.InputInfo;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.OutputInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import config.Mundo_Reader;
import database.Bandeira;
import database.BigOperation;
import database.Edifício;
import database.ItemPaladino;
import database.Unidade;
import database.Unidade.UnidadeTipo;

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

	private Map<Unidade, BigDecimal> tropasAtacantes = new HashMap<Unidade, BigDecimal>();
	private Map<Unidade, BigDecimal> tropasDefensoras = new HashMap<Unidade, BigDecimal>();

	private Map<Unidade, Integer> nívelTropasAtaque = new HashMap<Unidade, Integer>();
	private Map<Unidade, Integer> nívelTropasDefesa = new HashMap<Unidade, Integer>();

	private int muralhaInicial;

	private int edifícioInicial;

	private int moral;

	private ItemPaladino itemAtacante, itemDefensor;

	private boolean itemCatapulta;

	private Bandeira bandeiraAtacante, bandeiraDefensor;

	private boolean religiãoAtacante, religiãoDefensor;

	private int sorte;

	private boolean noite;

	// Calculated Values that are maintaned during all combat

	private BigDecimal ataqueTotal;

	private Map<Unidade.UnidadeTipo, BigDecimal> ataqueTipos = new HashMap<Unidade.UnidadeTipo, BigDecimal>();
	private Map<Unidade.UnidadeTipo, BigDecimal> defesaTipos = new HashMap<Unidade.UnidadeTipo, BigDecimal>();

	private Map<Unidade, BigDecimal> tropasPerdidasAtaque = new HashMap<Unidade, BigDecimal>();
	private Map<Unidade, BigDecimal> tropasPerdidasDefesa = new HashMap<Unidade, BigDecimal>();

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

		if (Mundo_Reader.MundoSelecionado.hasArqueiro())
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

		tropasAtacantes = input.getTropasAtacantes();
		tropasDefensoras = input.getTropasDefensoras();

		nívelTropasAtaque = input.getNívelTropasAtaque();
		nívelTropasDefesa = input.getNívelTropasDefesa();

		muralhaInicial = input.getMuralha();

		edifícioInicial = input.getEdifício();
		
		moral = input.getMoral();

		sorte = input.getSorte();

		religiãoAtacante = input.getReligiãoAtacante();

		religiãoDefensor = input.getReligiãoDefensor();
		
		// Aceita o item do paladino apenas se houver paladino
		if (tropasAtacantes.get(Unidade.PALADINO).compareTo(BigDecimal.ZERO) == 1)
			itemAtacante = input.getItemAtacante();
		else
			itemAtacante = ItemPaladino.NULL;

		if (tropasDefensoras.get(Unidade.PALADINO).compareTo(BigDecimal.ZERO) == 1)
			itemDefensor = input.getItemDefensor();
		else
			itemDefensor = ItemPaladino.NULL;
		bandeiraAtacante = input.getBandeiraAtacante();

		bandeiraDefensor = input.getBandeiraDefensor();

		noite = input.getNoite();

		for (Unidade i : tropasAtacantes.keySet())
			tropasPerdidasAtaque.put(i, BigDecimal.ZERO);

		for (Unidade i : tropasDefensoras.keySet())
			tropasPerdidasDefesa.put(i, BigDecimal.ZERO);

	}

	private void setOutputVariables() {

		output.setTropasAtacantesPerdidas(tropasPerdidasAtaque);
		output.setTropasDefensorasPerdidas(tropasPerdidasDefesa);

		output.setMuralha(muralhaFinal);
		output.setEdifício(edifícioFinal);

	}

	private void damageWall() {

		// max = #rams*religion*attackOfRams / (8* 1.09^wallLevel)

		BigDecimal attack = Unidade.ARÍETE.getAtaque(
				nívelTropasAtaque.get(Unidade.ARÍETE), itemAtacante);

		BigDecimal levelsLowered = tropasAtacantes.get(Unidade.ARÍETE)
				.multiply(attack);

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

		if (muralhaBônusFlat < new BigDecimal(muralhaInicial).divide(attack)
				.setScale(0, RoundingMode.HALF_UP).intValue())
			muralhaBônusFlat = new BigDecimal(muralhaInicial).divide(attack)
					.setScale(0, RoundingMode.HALF_UP).intValue();

		if (muralhaBônusFlat < 0)
			muralhaBônusFlat = 0;

		muralhaBônusPercentual = muralhaBônusFlat;

		muralhaFinal = muralhaInicial;

	}

	private void scoutBattle() {

		BigDecimal atacantes = tropasAtacantes.get(Unidade.EXPLORADOR);
		BigDecimal defensores = tropasDefensoras.get(Unidade.EXPLORADOR);

		if (atacantes.compareTo(BigDecimal.ZERO) > 0) {

			if (defensores.divide(atacantes, rounding, RoundingMode.HALF_EVEN)
					.compareTo(new BigDecimal("2")) > 0)
				tropasAtacantes.put(Unidade.EXPLORADOR, BigDecimal.ZERO);
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

				tropasAtacantes.put(Unidade.EXPLORADOR, remaining);
				tropasPerdidasAtaque.put(Unidade.EXPLORADOR,
						atacantes.subtract(remaining));

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

		Map<Unidade, BigDecimal> atacantesSobrando = new HashMap<Unidade, BigDecimal>();

		for (Entry<Unidade, BigDecimal> i : tropasAtacantes.entrySet())
			atacantesSobrando.put(i.getKey(), i.getValue());

		Map<Unidade, BigDecimal> defensoresSobrando = new HashMap<Unidade, BigDecimal>();

		for (Entry<Unidade, BigDecimal> i : tropasDefensoras.entrySet())
			defensoresSobrando.put(i.getKey(), i.getValue());
		
		// Only does the calculation if there are attacking units
		if (hasUnits(atacantesSobrando))
		do {

			// Define o valor do ataque de cada tipo presente no exército, além
			// do ataque total (soma dos 3 tipos)
			setAtaques(atacantesSobrando);
			
			// Define o valor de defesa para cada tipo de tropa
			setDefesas(defensoresSobrando);

			// Porcentagem de tropas perdidas para cada tipo de unidade
			// No ataque, as perdas são apenas para o tipo de tropa representado
			Map<UnidadeTipo, BigDecimal> ataqueRatioLoss = new HashMap<UnidadeTipo, BigDecimal>();
			// Na defesa, é feita uma média ponderada das razões, e todas as
			// tropas perdidas são nessa razão
			Map<UnidadeTipo, BigDecimal> defenseRatioLoss = new HashMap<UnidadeTipo, BigDecimal>();

			for (UnidadeTipo i : UnidadeTipo.values()) {

				// Adiciona o bônus de muralha na defesa
				defesaTipos.put(i,
								defesaTipos.get(i).multiply(
									Edifício.MURALHA.bônusPercentual(muralhaBônusPercentual)));

				defesaTipos.put(i,
						defesaTipos.get(i).add(
								Edifício.MURALHA.bônusFlat(muralhaBônusFlat)));

				// ratio loss defesa = ( ataque total / defesa ) ^ 3/2

				// ratio loss ataque = 1 / ratio loss defesa

				if (ataqueTotal.compareTo(BigDecimal.ZERO) == 0
						&& defesaTipos.get(i).compareTo(BigDecimal.ZERO) == 0) {
					
					ataqueRatioLoss.put(i, BigDecimal.ZERO);
					defenseRatioLoss.put(i, BigDecimal.ZERO);
					
				} else if (ataqueTotal.compareTo(defesaTipos.get(i)) == 1) {
					
					ataqueRatioLoss.put(i, BigOperation.pow(
							defesaTipos.get(i).divide(ataqueTotal, rounding,
									RoundingMode.HALF_EVEN), new BigDecimal(
									"1.5")));
					defenseRatioLoss.put(i, BigDecimal.ONE);
					
				} else {
					
					ataqueRatioLoss.put(i, BigDecimal.ONE);
					defenseRatioLoss.put(i, BigOperation.pow(ataqueTotal
							.divide(defesaTipos.get(i), rounding,
									RoundingMode.HALF_EVEN), new BigDecimal(
							"1.5")));
					
				}

			}

			for (Entry<Unidade, BigDecimal> i : atacantesSobrando.entrySet()) {
				// Colocar as tropas perdidas na batalha num map para poder usar
				// depois
				// Adiciona às tropas perdidas a quantidade que foi morta nessa batalha
				tropasPerdidasAtaque.put(i.getKey(),
								tropasPerdidasAtaque.get(i.getKey())
										.add(i.getValue().multiply(
												ataqueRatioLoss.get(i.getKey().getType())))
										.setScale(0, RoundingMode.HALF_UP));
				
				// remover as tropas perdidas de atacantesSobrando
				i.setValue(i.getValue().multiply(
								BigDecimal.ONE.subtract(ataqueRatioLoss.get(
										i.getKey().getType())))
						.setScale(0, RoundingMode.HALF_UP));
			}

			BigDecimal defenseTotalLoss = BigDecimal.ZERO;

			// Fazer a média ponderada das perdas defensivas

			for (UnidadeTipo i : UnidadeTipo.values())
				defenseTotalLoss = defenseTotalLoss.add(ataqueTipos.get(i)
						.multiply(defenseRatioLoss.get(i)));

			defenseTotalLoss = defenseTotalLoss.divide(ataqueTotal, rounding,
					RoundingMode.HALF_EVEN);

			for (Entry<Unidade, BigDecimal> i : defensoresSobrando.entrySet()) {
				// Colocar as tropas perdidas na batalha num map para poder usar
				// depois
				tropasPerdidasDefesa.put(i.getKey(),
						tropasPerdidasDefesa.get(i.getKey())
								.add(i.getValue().multiply(defenseTotalLoss))
								.setScale(0, RoundingMode.HALF_UP));
				
				// Remover as tropas perdidas do mapa adequado
				i.setValue(i.getValue()
						.multiply(BigDecimal.ONE.subtract(defenseTotalLoss))
						.setScale(0, RoundingMode.HALF_UP));
			}

			// After first round of attacks, wall becomes 0 and religions become
			// true

			muralhaBônusFlat = 0;
			religiãoAtacante = true;
			religiãoDefensor = true;

		} while (hasUnits(atacantesSobrando) && hasUnits(defensoresSobrando));

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

		if (tropasAtacantes.get(Unidade.ARÍETE).compareTo(BigDecimal.ZERO) > 0
				&& muralhaInicial > 0) {

			if (somaTropasDefensoras.equals(BigDecimal.ZERO)) // Defensor
															// perdeu

				// final = initial - round(max * (2-
				// lostAttackers/totalAttackers))

				muralhaFinal = muralhaInicial
						- maxLevelLowered
								.multiply(
										(new BigDecimal("2")
												.subtract(somaTropasAtacantesPerdidas
														.divide(somaTropasAtacantes,
																rounding,
																RoundingMode.HALF_EVEN))))
								.setScale(0, RoundingMode.HALF_EVEN).intValue();
			else
				// Atacante perdeu

				// final = initial - round(max * lostDefenders/totalDefenders)

				muralhaFinal = muralhaInicial
						- maxLevelLowered
								.multiply(somaTropasDefensorasPerdidas)
								.divide(somaTropasDefensoras, rounding,
										RoundingMode.HALF_EVEN)
								.setScale(0, RoundingMode.HALF_DOWN).intValue();

			if (muralhaFinal < 0)
				muralhaFinal = 0;

		}

		// Downgrading Building
		
		edifícioFinal = edifícioInicial;

		if (tropasAtacantes.get(Unidade.CATAPULTA).compareTo(BigDecimal.ZERO) > 0
				&& edifícioInicial > 0) {

			BigDecimal levelsLowered = tropasAtacantes.get(Unidade.CATAPULTA)
					.multiply(
							Unidade.CATAPULTA.getAtaque(
									nívelTropasAtaque.get(Unidade.CATAPULTA),
									itemAtacante));

			levelsLowered = levelsLowered.divide(new BigDecimal("600")
					.multiply(BigOperation.pow(new BigDecimal("1.09"),
							new BigDecimal(edifícioInicial))), rounding,
					RoundingMode.HALF_EVEN);

			if (somaTropasDefensoras.equals(BigDecimal.ZERO)) // Defensor
															// perdeu

				// final = initial - round(max * (2-
				// lostAttackers/totalAttackers))

				edifícioFinal = edifícioInicial
						- levelsLowered
								.multiply(
										(new BigDecimal("2")
												.subtract(somaTropasAtacantesPerdidas
														.divide(somaTropasAtacantes,
																rounding,
																RoundingMode.HALF_EVEN))))
								.setScale(0, RoundingMode.HALF_DOWN).intValue();
			else
				// Atacante perdeu

				// final = initial - round(max * lostDefenders/totalDefenders)

				edifícioFinal = edifícioInicial
						- levelsLowered
								.multiply(somaTropasDefensorasPerdidas)
								.divide(somaTropasDefensoras, rounding,
										RoundingMode.HALF_EVEN)
								.setScale(0, RoundingMode.HALF_DOWN).intValue();

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
	private boolean hasUnits(Map<Unidade, BigDecimal> map) {

		for (Entry<Unidade, BigDecimal> i : map.entrySet()) {
			
			if (!i.getKey().equals(Unidade.EXPLORADOR))
				if (i.getValue().compareTo(BigDecimal.ZERO) == 1)
					return true;

		}

		return false;

	}

}
