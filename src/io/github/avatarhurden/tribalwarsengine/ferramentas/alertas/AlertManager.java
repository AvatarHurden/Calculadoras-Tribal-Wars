package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TimerTask;
import java.util.TreeSet;

/**
 * Classe que cuida de todos os alertas. Possui inst�ncia est�tica, � qual deve ser adicionado
 * qualquer alerta a ser criado.
 * 
 * @author Arthur
 *
 */
public class AlertManager {

	private static final AlertManager instance = new AlertManager();
	
	// Lista de alertas existentes
	private List<Alert> alerts = new ArrayList<Alert>();
	
	// Conjunto de objetos que ligam alertas a stacks de datas, com os hor�rios ainda n�o mostrados
	private TreeSet<AlertStack> dates;
	
	// Mapa que liga alertas a tarefas rodando, para poder cancelar se necess�rio
	private Map<Alert, TimerTask> tasksRodando = new HashMap<Alert, TimerTask>();
	
	private AlertManager() {
		
	}
	
	public void addAlert(Alert alerta) {
		
	}
	
	public void editarAlert(Alert alerta) {
		
	}
	
	public void removeAlert(Alert alerta) {
		
	}
	
	public AlertManager getInstance() {
		return instance;
	}
	
	/**
	 * Classe que liga um alerta a um stack de dates. Cada vez que um popup para o alerta � marcado,
	 * remove-se o date do stack.
	 */
	private class AlertStack {
		
		private Alert alert;
		private Stack<Date> dates;
		
		private AlertStack(Alert alert, Stack<Date> dates){
			this.alert = alert;
			this.dates = dates;
		}
		
		/**
		 * Retorna a pr�xima data a ser feita. Remove-a da lista. Caso n�o hajam mais datas, retorna null.
		 */
		private Date getDate() {
			if (dates.isEmpty())
				return null;
			else
				return dates.pop();
		}
		
	}
	
}
