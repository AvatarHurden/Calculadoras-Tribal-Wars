package distância;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import database.Cores;
import database.MundoSelecionado;
import database.Unidade;

@SuppressWarnings("serial")
public class PanelTempoUnidade extends JPanel{

	private JLabel tempo;
	
	private Color cor;
	private Unidade unidade;
	
	private BigDecimal velocidadeReal;
	
	/**
	 * Panel de cada unidade, com seu tempo total
	 * 
	 * @param cor Cor do Panel
	 * @param unidade Unidade que ele representa
	 */
	public PanelTempoUnidade(Color cor, Unidade unidade) {
		
		this.cor = cor;
		this.unidade = unidade;
		
		// Define a velocidade real de cada unidade. Multiplica por 60 porque o tempo das unidades
		// é em minutos, não em segundos.
		
		velocidadeReal = unidade.velocidade()
				.divide(MundoSelecionado.getVelocidade(), 30, BigDecimal.ROUND_HALF_EVEN)
				.divide(MundoSelecionado.getModificarUnidaes(), 30, BigDecimal.ROUND_HALF_EVEN)
				.multiply(new BigDecimal(60));
		
		createUnitPanel();
	}
	
	public PanelTempoUnidade() {
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 1, 100};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0};
		setLayout(gbl);
		
		setBackground(Cores.FUNDO_ESCURO);
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		JLabel nome = new JLabel("Unidade");
		nome.setHorizontalAlignment(SwingConstants.CENTER);
		add(nome, constraints);
		
		addSeparator(constraints, this);
		
		tempo = new JLabel("Tempo");
		tempo.setHorizontalAlignment(SwingConstants.CENTER);
		
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx++;
		
		add(tempo, constraints);
		
	}
	
	private void createUnitPanel() {
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {125, 1, 100};
		gbl.rowHeights = new int[] {30};
		gbl.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0};
		setLayout(gbl);
		
		setBackground(cor);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		JLabel nome = new JLabel(unidade.nome());
		add(nome, constraints);
		
		addSeparator(constraints, this);
		
		tempo = new JLabel();
		tempo.setHorizontalAlignment(SwingConstants.CENTER);
		
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx++;
		
		add(tempo, constraints);
				
	}
	
	/**
	 * Define o tempo no JLabel
	 * 
	 * @param distância que ele irá percorrer
	 */
	protected void setTempo(BigDecimal distância) {
		
		if (distância == null)
			tempo.setText("");
		else {
			BigDecimal time = velocidadeReal.multiply(distância).setScale(0,RoundingMode.HALF_DOWN);
		
			tempo.setText(Cálculos.format(time));
		}
	}
	
	// Adiciona um JSeparator vertical para separar as características
	private void addSeparator(GridBagConstraints c, JPanel panel) {
		
		JSeparator test = new JSeparator(SwingConstants.VERTICAL);
		if (cor == null)
			test.setForeground(Cores.SEPARAR_ESCURO);
		else
			test.setForeground(Cores.SEPARAR_CLARO);
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx++;
		panel.add(test, c);
		
	}
	
}
