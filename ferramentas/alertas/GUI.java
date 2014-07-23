package alertas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import alertas.Alert.Aldeia;
import alertas.Alert.Tipo;
import custom_components.Ferramenta;
import database.Unidade;

public class GUI extends Ferramenta {
	
	List<Alert> alertas = new ArrayList<Alert>();
	AlertTable table = new AlertTable(alertas);
	
	public GUI() {
		
		super("Alertas");
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 400 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		for (int i = 0; i < 4; i++) {
			
			Alert alerta = new Alert();
			
			alerta.setNome("Nome"+i);
			
			alerta.setNotas(i+" MEU NOME É ESSE QUE ESTOU MOSTRANDO PARA VOC~ES AQUI, AGORA, NESSE DIA PARA A AL ELREAIOD HAUSD ASD ASOD HASOD HASDO AHSDO HSADO YASID AISD");
			
			alerta.setTipo(Tipo.values()[i % 4]);
			alerta.setOrigem(new Aldeia("Origem"+i, i*111, i*55));
			alerta.setDestino(new Aldeia("Destino"+i, i*11, i*555));
			
			if (i > 0) {
			Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
			for (Unidade u : Unidade.values())
				map.put(u, (int) (Math.random()*100 + 1));
			alerta.setTropas(map);
			} else {
				Map<Unidade, Integer> map = new HashMap<Unidade, Integer>();
				map.put(Unidade.LANCEIRO, 34);
				map.put(Unidade.ESPADACHIM, 342);
				map.put(Unidade.ARCOCAVALO, 32);
				alerta.setTropas(map);
				
			}
			
			Date now = new Date();

			alerta.setHorário(new Date(now.getTime()+i*100000000));
			
			alerta.setRepete((long) (Math.random()*100000000));
			
			alertas.add(alerta);
			
		}
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(1000,500));
		table.setFillsViewportHeight(true);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		add(scrollPane, c);
		
		c.gridy++;
		add(makeButtonPanels(), c);
		
	}
	
	private JPanel makeButtonPanels() {
	
		JPanel panel = new JPanel();
		
		JButton addAlerta = new JButton("Criar Novo");
		addAlerta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Editor editor = new Editor();
				
				editor.setModal(true);
				editor.setVisible(true);
				
				Alert alerta = editor.getAlerta();
				
				table.addAlert(alerta);
				
			}
		});
		
		JButton editAlerta = new JButton("Editar");
		editAlerta.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Alert selected = (Alert) table.getModel().getValueAt(
						table.convertRowIndexToModel(table.getSelectedRow()), -1);
				
				Editor editor = new Editor(selected);
				
				editor.setModal(true);
				editor.setVisible(true);
				
				Alert alerta = editor.getAlerta();
				
				table.changeAlert(alerta, table.convertRowIndexToModel(table.getSelectedRow()));
				
				
			}
		});
		
		JButton deleteAlerta = new JButton("Deletar");
		
		panel.add(addAlerta);
		panel.add(editAlerta);
		
		return panel;
	}
	
}
