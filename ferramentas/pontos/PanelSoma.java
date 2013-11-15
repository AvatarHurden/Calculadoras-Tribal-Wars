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
import database.Edif�cio;

public class PanelSoma {

	private JPanel dadosPanel;
	private JPanel popula��oRestantePanel;
	
	private JLabel popula��o, pontos, popula��oRestante;
	
//	private List<PanelEdif�cio> panelEdif�cioList;
	private Map<Edif�cio, PanelEdif�cio> panelEdif�cioMap = new HashMap<Edif�cio, PanelEdif�cio>();
	
	private Map<String, BigInteger> somaTotal = new HashMap<String, BigInteger>();
	
	/**
	 * Painel com a soma dos pontos e popula��es
	 */
	public PanelSoma() {
		
		setDadosPanel();
		setPopula��oRestantePanel();
		
	}
	
	/**
	 * Define a lista de pain�is que ser�o somados e a cor dos pain�is
	 * @param list dos pain�is
	 * @param cor
	 */
	protected void setPanelListAndColor(List<PanelEdif�cio> list, Color cor) {
		
		for (PanelEdif�cio i : list)
		panelEdif�cioMap.put(i.getEdif�cio(), i);
		
		dadosPanel.setBackground(cor);
		popula��oRestantePanel.setBackground(cor);
		
	}
	
	// Adiciona panel com as caracter�sticas principais
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
		
		popula��o = new JLabel();
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.gridx++;
		dadosPanel.add(popula��o, constraints);
		
	}
	
	private void setPopula��oRestantePanel() {
		
		popula��oRestantePanel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		
		gbl.columnWidths = new int[] {131};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		popula��oRestantePanel.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		
		popula��oRestante = new JLabel();
		constraints.insets = new Insets(5, 5, 5, 5);
		popula��oRestantePanel.add(popula��oRestante, constraints);
		
	}
	
	// Adiciona um JSeparator vertical para separar as caracter�sticas
	private void addSeparator(GridBagConstraints c, JPanel panel) {
			
		JSeparator test = new JSeparator(SwingConstants.VERTICAL);
		test.setForeground(Cores.SEPARAR_CLARO);
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx++;
		panel.add(test, c);
			
		}
	
	// Precisa throw a exce��o porque java � estranho
	private void soma() throws ParseException {
		
		// Somando pontos
		BigInteger pontos = new BigInteger("0");
		for (PanelEdif�cio i : panelEdif�cioMap.values())
			if (i.getN�vel() != 0)
				pontos = pontos.add(new BigInteger(NumberFormat.getNumberInstance(Locale.GERMANY)
						.parse(i.getPontos().getText()).toString()));
		
		somaTotal.put("pontos", pontos);
		
		// Somando custo de popula��o
		BigInteger popula��o = new BigInteger("0");
		for (PanelEdif�cio i : panelEdif�cioMap.values())
			if (i.getN�vel() != 0)
				popula��o = popula��o.add(new BigInteger(NumberFormat.getNumberInstance(Locale.GERMANY)
						.parse(i.getPopula��o().getText()).toString()));
									
		somaTotal.put("popula��o", popula��o);
		
		// Calculando popula��o restante
		int popRestante = Edif�cio.FAZENDA.popula��o(panelEdif�cioMap.get(Edif�cio.FAZENDA).getN�vel());
		
		BigInteger popula��oRestante = new BigInteger(String.valueOf(popRestante));
		
		popula��oRestante = popula��oRestante.subtract(popula��o);
		
		somaTotal.put("popula��oRestante", popula��oRestante);
	}

	// Define o texto das labels
	protected void setTotal() {
	
	try {
		soma();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	
	// Checa se os valores s�o diferentes de zero
	if (!somaTotal.get("pontos").equals(BigInteger.ZERO)) {
	
		pontos.setText(String.format("%,d", somaTotal.get("pontos")));
		popula��o.setText(String.format("%,d", somaTotal.get("popula��o")));
		popula��oRestante.setText(String.format("%,d", somaTotal.get("popula��oRestante")));
	
	} else {
		pontos.setText("");
		popula��o.setText("");
		popula��oRestante.setText("");
	}

	}
	
	/**
	 * @return JPanel com as labels dos dados principais
	 */
	protected JPanel getDadosPrincipais() { return dadosPanel; }
	
	protected JPanel getPopula��oRestantePanel() { return popula��oRestantePanel; }
	
}
