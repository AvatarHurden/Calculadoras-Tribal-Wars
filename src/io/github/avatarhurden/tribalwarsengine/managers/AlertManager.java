package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertEditor;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que cuida de todos os alertas. Possui instância estática, à qual deve ser adicionado
 * qualquer alerta a ser criado.
 * 
 * @author Arthur
 *
 */
public class AlertManager {
	
	private AlertFileManager fileManager;

	private static AlertManager instance = new AlertManager();
	
	// Lista de alertas existentes
	private List<Alert> alerts = new ArrayList<Alert>();
	// Lista de alertas que foram exibidos durante a execução do programa
	private List<Alert> pastAlerts = new ArrayList<Alert>();
	
	// Conjunto de objetos que ligam alertas a stacks de datas, com os horários ainda não mostrados
	private TreeSet<AlertStack> dates;
	// Horário em que o último TimerTask marcado vai rodar, para evitar ter muitos Threads rodando
	Date lastScheduledDate;
	
	// Mapa que liga alertas a tarefas rodando, para poder cancelar se necessário
	private Map<Alert, TimerTask> tasksRodando = new HashMap<Alert, TimerTask>();
	// Timer com o qual marcar as tasks
	Timer timer;
	
	private PopupManager popups;
	
	private AlertManager() {
		
		timer = new Timer();
		
		dates = new TreeSet<AlertStack>(getComparator()) {
			
			public boolean remove(Object o){
				
				if (o instanceof Alert)
					for (AlertStack s : this)
						if (s.alert.equals(o))
							return super.remove(s);
				
				return super.remove(o);
			}
			
		};		

		popups = new PopupManager();
	
		fileManager = new AlertFileManager(Configuration.alertFolder);
		
		loadSaved(fileManager.getAlertList());
	}
	
	private void loadSaved(JSONArray array) {
		Gson gson = new Gson();
		
		for (int i = 0; i < array.length(); i++)
			addAlert(gson.fromJson(array.get(i).toString(), Alert.class));
	}
	
	public void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JSONArray array = new JSONArray();
		
		for (Alert a : alerts)
			array.put(new JSONObject(gson.toJson(a)));
		
		fileManager.saveAlertList(array);
	}
	
	/**
	 * Abre o AlertEditor vazio para a criação de um novo Alert. A decisão de adicionar ou não
	 * esse Alert à lista é feita por essa função, dependendo se o usuário clicou em "salvar" ou
	 * "cancelar"
	 */
	public void createAlert() {
		
		AlertEditor editor = new AlertEditor();
		
		editor.setModal(true);
		editor.setVisible(true);
		
		Alert alerta = editor.getAlerta();
		
		if (alerta != null)
			addAlert(alerta);
		
	}
	
	/**
	 * Abre o AlertEditor pré-preenchido para a criação (ou edição) de um novo Alert.
	 * @param alerta pré-feito
	 * @param existente, se ele já está estava incluído na lista ou não.
	 */
	public void createAlert(Alert oldAlerta, boolean existente) {
		
		AlertEditor editor = new AlertEditor(oldAlerta);
		
		editor.setModal(true);
		editor.setVisible(true);
		
		Alert alerta = editor.getAlerta();
		
		if (alerta != null)
			if (existente)
				editAlert(oldAlerta, alerta);
			else
				addAlert(alerta);
		
	}
	
	/**
	 * Adiciona um Alert à lista de alertas existentes
	 * @param alerta a ser adicionado
	 */
	public void addAlert(Alert alerta) {

		alerts.add(alerta);
		
		addToSchedule(alerta);
		
	}
	
	/**
	 * Edita um Alert que está na lista dos existentes
	 * @param alerta a ser modificado
	 */
	public void editAlert(Alert alerta, Alert oldAlerta) {
		
		int position = alerts.indexOf(oldAlerta);
		
		alerts.remove(position);
		alerts.add(position, alerta);
		
		removeFromSchedule(oldAlerta);
		addToSchedule(alerta);
	}
	
	/**
	 * Remove um Alert da lista
	 * @param alerta a ser removido
	 */
	public void removeAlert(Alert alerta) {
	
		alerts.remove(alerta);
		
		removeFromSchedule(alerta);
		
	}
	
	public static AlertManager getInstance() {
		return instance;
	}
	
	public List<Alert> getAlertList() {
		return alerts;
	}
	
	/**
	 * Adiciona o Alert à dates e, se necessário, inicia um TimerTask para ele
	 * @param alerta
	 */
	private void addToSchedule(Alert alerta) {
		
		Stack<Date> avisos = alerta.getAvisos();
		// Aparecem avisos tanto nos alertas quanto no horário real
		avisos.add(0, alerta.getHorário());
		
		// Remove todos os avisos que já deveriam ter sido mostrados (ou seja, Dates do passado)
		Iterator<Date> it = avisos.iterator();
		while (it.hasNext())
			if (it.next().compareTo(new Date()) < 1)
				it.remove();
		
		AlertStack stack = new AlertStack(alerta, avisos);

		dates.add(stack);
		
		if (tasksRodando.isEmpty() || lastScheduledDate == null 
				|| (!avisos.isEmpty() && lastScheduledDate.compareTo(avisos.peek()) > 0))
			schedule(stack);
		
	}
	
	/**
	 * Remove o Alert de dates e também cancela os TimerTasks relativos a ele
	 * @param alerta
	 */
	private void removeFromSchedule(Alert alerta) {
		
		dates.remove(alerta);
		
		for (Entry<Alert, TimerTask> e : tasksRodando.entrySet())
			if (e.getKey().equals(alerta))
				e.getValue().cancel();
		
		timer.purge();
		
	}
	
	/**
	 * Cria um TimerTask para mostrar o próximo popup. Esse TimerTask, quando executado, chama novamente
	 * schedule, marcando o próximo popup. Isso é feito para minimizar o load no JVM.
	 * @param a AlertStack para ser marcado
	 */
	private void schedule(final AlertStack a) {
		
		Date date = a.getDate();
		
		if (date == null) {
			dates.remove(a);
			
			if (!dates.isEmpty())
				schedule(dates.first());
			
		} else if (a != null) {
			
			final AlertStack next = dates.first();
			
			tasksRodando.put(a.alert, new TimerTask() {
				
				public void run() {

					popups.showNewPopup(a.alert);
					tasksRodando.remove(a.alert);
					pastAlerts.add(a.alert);
					cancel();
					if (next != null)
						schedule(next);
					else
						timer.cancel();
					timer.purge();
				}
			});
			
			timer.schedule(tasksRodando.get(a.alert), date);
			lastScheduledDate = date; 
		}
	}
	
	/**
	 * Classe que liga um alerta a um stack de dates. Cada vez que um popup para o alerta é marcado,
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
		 * Retorna a próxima data a ser feita. Remove-a da lista. Caso não hajam mais datas, retorna null.
		 */
		private Date getDate() {
			if (dates.isEmpty())
				return null;
			else
				return dates.pop();
		}
		
	}
	
	/**
	 * Cria um comparador entre aldeias. No comparador, o menor valor é aquele que possui um horário mais
	 * cedo.
	 */
	private Comparator<AlertStack> getComparator() {
		
		return new Comparator<AlertStack>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(AlertStack a1, AlertStack a2) {
				
				Stack<Date> avisos1 = (Stack<Date>) a1.dates.clone();
				Stack<Date> avisos2 = (Stack<Date>) a2.dates.clone();
				
				int compare;
				while (!avisos1.isEmpty() && !avisos2.isEmpty()) {
					compare = avisos1.pop().compareTo(avisos2.pop());
					if (compare != 0)
						return compare;
				}
				
				if (avisos1.isEmpty() && avisos2.isEmpty())
					return 0;
				else if (avisos1.isEmpty())
					return -1;
				else 
					return 1;
			}
		};

	}
	
}
