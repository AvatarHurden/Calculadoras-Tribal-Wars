package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * Painel para criar novos alertas. Deve ser adicionado em ferramentas que possuem horário, como
 * calculadora de distância ou planejador de saque.
 * @author Arthur
 *
 */
public class AlertCreatorPanel extends TWSimpleButton implements ActionListener {
	
	private TimeFormattedJLabel datelbl, intervallbl;
	private CoordenadaPanel origem, destino;
	private ArmyEditPanel armyEdit;
	
	public AlertCreatorPanel(TimeFormattedJLabel datelbl, TimeFormattedJLabel intervallbl,
			CoordenadaPanel origem, CoordenadaPanel destino, ArmyEditPanel armyEdit) {
		
		super("Marcar Alerta");
		
		this.datelbl = datelbl;
		this.intervallbl = intervallbl;
		this.origem = origem;
		this.destino = destino;
		this.armyEdit = armyEdit;
		
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		
		Alert alert = new Alert();
		
		if (datelbl != null)
			alert.setHorário(datelbl.getDate());
		
		if (intervallbl != null)
			alert.setRepete(intervallbl.getTime());
			
		if (origem != null)
			alert.setOrigem(new Aldeia(null, origem.getCoordenadaX(), origem.getCoordenadaY()));
		
		if (destino != null)
			alert.setDestino(new Aldeia(null, destino.getCoordenadaX(), destino.getCoordenadaY()));
		
		if (armyEdit != null)
			alert.setArmy(armyEdit.getArmy());
		
		alert.setTipo(decideTipo());
		
		alert.setAvisos(new ArrayList<Date>());
		
		AlertManager.getInstance().createAlert(alert, false);
	}
	
	private Tipo decideTipo() {	
		if (armyEdit == null)
			return Tipo.Geral;
		
		if (armyEdit.getArmy().getQuantidade("ram") > 0 
				|| armyEdit.getArmy().getQuantidade("axe") > 0
				|| armyEdit.getArmy().getQuantidade("marcher") > 0
				|| armyEdit.getArmy().getTropa("spy").getPopulation() == armyEdit.getArmy().getPopulação())
			return Tipo.Ataque;
	
		if (armyEdit.getArmy().getQuantidade("light") > 0)
			return Tipo.Saque;
		
		return Tipo.Apoio;
	}
}
