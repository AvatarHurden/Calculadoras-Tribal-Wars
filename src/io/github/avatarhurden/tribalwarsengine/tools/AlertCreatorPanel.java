package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertManager;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Unit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Painel para criar novos alertas. Deve ser adicionado em ferramentas que possuem horário, como
 * calculadora de distância ou planejador de saque.
 * @author Arthur
 *
 */
public class AlertCreatorPanel extends TWSimpleButton implements ActionListener {
	
	private TimeFormattedJLabel datelbl;
	private CoordenadaPanel origem, destino;
	private Map<Unit, IntegerFormattedTextField> tropas;
	
	public AlertCreatorPanel(TimeFormattedJLabel datelbl, CoordenadaPanel origem, CoordenadaPanel destino,
			Map<Unit, IntegerFormattedTextField> tropas) {
		
		super("Marcar Alerta");
		
		this.datelbl = datelbl;
		this.origem = origem;
		this.destino = destino;
		this.tropas = tropas;
		
		addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent arg0) {
		
		Alert alert = new Alert();
		
		if (datelbl != null)
			if (datelbl.getDate() != null)
				alert.setHorário(datelbl.getDate());
			else
				alert.setRepete(datelbl.getTime());
		
		if (origem != null)
			alert.setOrigem(new Aldeia(null, origem.getCoordenadaX(), origem.getCoordenadaY()));
		
		if (destino != null)
			alert.setDestino(new Aldeia(null, destino.getCoordenadaX(), destino.getCoordenadaY()));
		
		if (tropas != null) {
			Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
			for (Entry<Unidade, IntegerFormattedTextField> e : tropas.entrySet())
				map.put(e.getKey(), e.getValue().getValue().intValue());
	
			alert.setArmy(map);
		}
		
		if (origem == null && destino == null && tropas == null)
			alert.setTipo(Tipo.Geral);
		else
			alert.setTipo(Tipo.Ataque);
		
		alert.setAvisos(new ArrayList<Date>());
		
		AlertManager.getInstance().createAlert(alert, false);
	}
}
