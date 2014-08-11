package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.Unidade;

public class ResultDataDisplay extends JPanel {

	private Map<Unidade, JTextField> tropasAtacantes = new HashMap<Unidade, JTextField>();

	private Map<Unidade, JTextField> tropasDefensoras = new HashMap<Unidade, JTextField>();

	private JLabel muralha, edifício;

	public ResultDataDisplay() {

	}
}
