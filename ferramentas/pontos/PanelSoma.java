package pontos;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import database.Cores;
import database.Edifício;

public class PanelSoma {

	private JPanel dadosPanel;
	private JPanel populaçãoRestantePanel;
	
	private JLabel população, pontos, populaçãoRestante;
	
//	private List<PanelEdifício> panelEdifícioList;
	private Map<Edifício, PanelEdifício> panelEdifícioMap = new HashMap<Edifício, PanelEdifício>();
	
	private Map<String, BigInteger> somaTotal = new HashMap<String, BigInteger>();
	
	/**
	 * Painel com a soma dos pontos e populações
	 */
	public PanelSoma() {
		
		setDadosPanel();
		setPopulaçãoRestantePanel();
		
	}
	
	/**
	 * Define a lista de painéis que serão somados e a cor dos painéis
	 * @param list dos painéis
	 * @param cor
	 */
	protected void setPanelListAndColor(List<PanelEdifício> list, Color cor) {
		
		for (PanelEdifício i : list)
		panelEdifícioMap.put(i.getEdifício(), i);
		
		dadosPanel.setBackground(cor);
		populaçãoRestantePanel.setBackground(cor);
		
	}
	
	// Adiciona panel com as características principais
	private void setDadosPanel() {
		
		dadosPanel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		
		gbl.columnWidths = new int[] {75, 1, 75};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		dadosPanel.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		
		pontos = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx = 0;
		dadosPanel.add(pontos, constraints);
		
		addSeparator(constraints, dadosPanel);
		
		população = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPanel.add(população, constraints);
		
	}
	
	private void setPopulaçãoRestantePanel() {
		
		populaçãoRestantePanel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		
		gbl.columnWidths = new int[] {131};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		populaçãoRestantePanel.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		
		populaçãoRestante = new JLabel();
		constraints.insets = new Insets(5, 5, 5, 5);
		populaçãoRestantePanel.add(populaçãoRestante, constraints);
		
	}
	
	// Adiciona um JSeparator vertical para separar as características
	private void addSeparator(GridBagConstraints c, JPanel panel) {
			
		JSeparator test = new JSeparator(SwingConstants.VERTICAL);
		test.setForeground(Cores.SEPARAR_CLARO);
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx++;
		panel.add(test, c);
			
		}
	
	// Precisa throw a exceção porque java é estranho
	private void soma() throws ParseException {
		
		// Somando pontos
		BigInteger pontos = new BigInteger("0");
		for (PanelEdifício i : panelEdifícioMap.values())
			if (i.getNível() != 0)
				pontos = pontos.add(new BigInteger(NumberFormat.getNumberInstance(Locale.GERMANY)
						.parse(i.getPontos().getText()).toString()));
		
		somaTotal.put("pontos", pontos);
		
		// Somando custo de população
		BigInteger população = new BigInteger("0");
		for (PanelEdifício i : panelEdifícioMap.values())
			if (i.getNível() != 0)
				população = população.add(new BigInteger(NumberFormat.getNumberInstance(Locale.GERMANY)
						.parse(i.getPopulação().getText()).toString()));
									
		somaTotal.put("população", população);
		
		// Calculando população restante
		int popRestante = Edifício.FAZENDA.população(panelEdifícioMap.get(Edifício.FAZENDA).getNível());
		
		BigInteger populaçãoRestante = new BigInteger(String.valueOf(popRestante));
		
		populaçãoRestante = populaçãoRestante.subtract(população);
		
		somaTotal.put("populaçãoRestante", populaçãoRestante);
	}

	// Define o texto das labels
	protected void setTotal() {
	
	try {
		soma();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	
	// Checa se os valores são diferentes de zero
	if (!somaTotal.get("pontos").equals(BigInteger.ZERO)) {
	
		pontos.setText(String.format("%,d", somaTotal.get("pontos")));
		população.setText(String.format("%,d", somaTotal.get("população")));
		populaçãoRestante.setText(String.format("%,d", somaTotal.get("populaçãoRestante")));
	
	} else {
		pontos.setText("");
		população.setText("");
		populaçãoRestante.setText("");
	}

	}
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPrincipais() { return dadosPanel; }
	
	protected JPanel getPopulaçãoRestantePanel() { return populaçãoRestantePanel; }
	
}
