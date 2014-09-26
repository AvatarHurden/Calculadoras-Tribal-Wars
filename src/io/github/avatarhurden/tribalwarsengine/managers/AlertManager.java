package io.github.avatarhurden.tribalwarsengine.managers;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertEditor;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertTable;
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
import java.util.concurrent.TimeUnit;

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
	private JSONObject config;

	private static AlertManager instance;
	
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
	private AlertTable table;
	
	private AlertManager() {
		
		timer = new Timer();
		
		dates = new TreeSet<AlertStack>(getComparator()) {
			
			public boolean remove(Object o){
				
				if (o instanceof Alert) {
					for (AlertStack s : this)
						if (s.alert.equals(o))
							return super.remove(s);
					return false;
				}
				return super.remove(o);
			}
			
		};		

		popups = new PopupManager();
	
		fileManager = new AlertFileManager(Configuration.alertFolder + "/br");
		
		config = fileManager.getConfig();
		loadSaved(fileManager.getAlertList());
		loadPast(fileManager.getPastAlertList());
	}
	
	private void loadSaved(JSONArray array) {
		Gson gson = new Gson();
		
		for (int i = 0; i < array.length(); i++)
			addAlert(gson.fromJson(array.get(i).toString(), Alert.class));
	}
	
	private void loadPast(JSONArray array) {
		Gson gson = new Gson();
		
		for (int i = 0; i < array.length(); i++)
			addAlert(gson.fromJson(array.get(i).toString(), Alert.class));
	}
	
	public void save() {
		Gson gson = new GsonBuilder().create();
		JSONArray current = new JSONArray();
		JSONArray past = new JSONArray();
		
		for (Alert a : alerts)
			if (a.isPast())
				past.put(new JSONObject(gson.toJson(a)));
			else
				current.put(new JSONObject(gson.toJson(a)));
		
		for (Alert a : pastAlerts)
			if (!shouldDelete(a))
				past.put(new JSONObject(gson.toJson(a)));
		
		fileManager.saveAlertList(current);
		fileManager.savePastAlertList(past);
		fileManager.saveConfig(config);
	}
	
	/**
	 * Abre o AlertEditor vazio para a criação de um novo Alert. A decisão de adicionar ou não
	 * esse Alert à lista é feita por essa função, dependendo se o usuário clicou em "salvar" ou
	 * "cancelar"
	 */
	public Alert createAlert() {
		
		AlertEditor editor = new AlertEditor();
		
		editor.setModal(true);
		editor.setVisible(true);
		
		Alert alerta = editor.getAlerta();
		
		if (alerta != null)
			addAlert(alerta);
		
		return alerta;
	}
	
	/**
	 * Abre o AlertEditor pré-preenchido para a criação (ou edição) de um novo Alert.
	 * @param alerta pré-feito
	 * @param existente, se ele já está estava incluído na lista ou não.
	 */
	public Alert createAlert(Alert oldAlerta, boolean existente) {
		
		AlertEditor editor = new AlertEditor(oldAlerta);
		
		editor.setModal(true);
		editor.setVisible(true);
		
		Alert alerta = editor.getAlerta();
		
		if (alerta != null)
			if (existente)
				editAlert(oldAlerta, alerta);
			else
				addAlert(alerta);
		
		return alerta;
	}
	
	/**
	 * Adiciona um Alert à lista de alertas existentes
	 * @param alerta a ser adicionado
	 */
	public void addAlert(Alert alerta) {
		addToSchedule(alerta);
	}
	
	/**
	 * Edita um Alert que está na lista dos existentes
	 * @param alerta a ser modificado
	 */
	public void editAlert(Alert alerta, Alert oldAlerta) {	
		removeAlert(oldAlerta);
		addAlert(alerta);
	}
	
	/**
	 * Remove um Alert da lista
	 * @param alerta a ser removido
	 */
	public void removeAlert(Alert alerta) {
		if (alerts.contains(alerta)) {
			alerts.remove(alerta);
			removeFromSchedule(alerta); 
		} else if (pastAlerts.contains(alerta))
			pastAlerts.remove(alerta);
	}
	
	public static AlertManager getInstance() {
		if (instance == null)
			instance = new AlertManager();
		return instance;
	}
	
	public List<Alert> getAlertList() {
		return alerts;
	}
	
	public List<Alert> getPastAlertList() {
		return pastAlerts;
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
			if (it.next().before(new Date()))
				it.remove();
		
		AlertStack stack = new AlertStack(alerta, avisos);
		
		putAlertInList(stack);
		
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
		
		final Date date = a.getDate();
		
		if (date == null) {
			dates.remove(a);
			
			if (!dates.isEmpty())
				schedule(dates.first());
			
		} else if (a != null) {
			
			final AlertStack next = dates.first();
			
			tasksRodando.put(a.alert, new TimerTask() {
				
				public void run() {

					popups.showNewPopup(a.alert, table, date);
					tasksRodando.remove(a.alert);
					
					a.alert.setPast(true);
					
					if (table != null)
						table.changedAlert();
					
					cancel();
					if (next != null)
						schedule(next);
					else
						timer.cancel();
					timer.purge();
					
					timer.schedule(transferToPast(a.alert), 60000);
				}
			});
			
			timer.schedule(tasksRodando.get(a.alert), date);
			lastScheduledDate = date; 
		}
	}
	
	private TimerTask transferToPast(final Alert a) {
		return new TimerTask() {
			@Override
			public void run() {
				pastAlerts.add(a);
				alerts.remove(a);
				
				if (table != null)
					table.removePast(a);
			}
		};
	}
	
	public void rescheduleAlert(Alert a) {
		a.reschedule();
		editAlert(a, a);
		if (table != null)
			table.rescheduledAlert(a);
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
	
	private void putAlertInList(AlertStack stack) {
		if (stack.dates.isEmpty()) {
					
			if (!shouldDelete(stack.alert)) {
				stack.alert.setPast(true);
				pastAlerts.add(stack.alert);
			}

		} else {
			
			stack.alert.setPast(false);
			alerts.add(stack.alert);
			dates.add(stack);
		
		}
	}
	
	private boolean shouldDelete(Alert a) {		
		Date toDelete = new Date(System.currentTimeMillis() 
				- TimeUnit.HOURS.toMillis(config.optInt("deletion_time", 24)));
		
		return a.getHorário().before(toDelete) && config.optBoolean("delete_past", true);
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
	
	public void setTable(AlertTable table) {
		this.table = table;
	}
	
	public JSONObject getConfig() {
		return config;
	}
	
	public Object getConfig(String name, Object value) {
		try {
			return config.get(name);
		} catch (Exception e) {
			return value;
		}
	}
	
	public void setConfig(String name, Object value) {
		config.put(name, value);
	}
}
