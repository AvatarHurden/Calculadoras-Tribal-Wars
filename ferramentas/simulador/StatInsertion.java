package simulador;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;

import config.World_Reader;

import database.Cores;
import database.MundoSelecionado;
import database.Unidade;

public class StatInsertion extends JPanel{

	// Enum com os tipos diferentes que o panel pode possuir
	// Eles diferem na presen�a ou n�o de mil�cia e nas informa��es adicionais
	// que podem receber
	private enum Tipo {
		atacante, defensor;
	}
	
	private Tipo tipo;
	
	private Map<Unidade, JTextField> mapQuantidades = new HashMap<Unidade, JTextField>();
	private Map<Unidade, JComboBox> mapNiveis = new HashMap<Unidade, JComboBox>();
	
	private JCheckBox religi�o, noite;
	
	private JTextField sorte, moral, muralha, edif�cio;
	
	private JComboBox item, bandeira;
	
	// vari�vel para a cor dos panels
	int loop = 0;
	
	/**
	 * @param tipo Se o panel � de atacante ou defensor
	 */
	public StatInsertion(Tipo tipo) {
		
		this.tipo = tipo;
		
		add(addUnitPanel());
		
	}
	
	private JPanel addUnitPanel() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {80,30};
		layout.rowHeights = new int[] {0};
		layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		if (MundoSelecionado.isPesquisaDeN�veis())
			c.gridwidth = 1;
		else
			c.gridwidth = 2;
		
		// Adding the headers
		
		panel.add(new JLabel("Quantidade"),c);
		
		if (MundoSelecionado.isPesquisaDeN�veis()){
			c.gridx = 1;
			panel.add(new JLabel("N�vel"), c);
		}
		
		
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 2;
		c.gridx = 0;
			
		for (Unidade i : MundoSelecionado.getUnidades()) {
			
			if (i != null && (!i.equals(Unidade.MIL�CIA) || tipo == Tipo.defensor)) {
				
			JPanel tropaPanel = new JPanel();
			tropaPanel.setLayout(layout);
			tropaPanel.setBackground(Cores.getAlternar(loop));
			
			GridBagConstraints tropaC = new GridBagConstraints();
			tropaC.insets = new Insets(5,5,5,5);
			tropaC.gridx = 0;
			tropaC.gridy = 0;
			if (MundoSelecionado.isPesquisaDeN�veis())
				tropaC.gridwidth = 1;
			else
				tropaC.gridwidth = 2;
			
			JTextField txt = new JTextField(6);
			
			mapQuantidades.put(i, txt);
			
			tropaPanel.add(txt, tropaC);
			
				if (MundoSelecionado.isPesquisaDeN�veis()) {
					
					// Coloca a cor padr�o para os comboBox
					UIManager.put("ComboBox.selectionBackground", Cores.FUNDO_ESCURO); 
					UIManager.put("ComboBox.background", Cores.getAlternar(loop)); 
					
					JComboBox<Integer> n�vel = new JComboBox<Integer>(new Integer[]{1,2,3});
					
					n�vel.setOpaque(false);
					
					// Cria um renderer para set usado no combox, centralizando o texto
					ListCellRenderer<Object> renderer = new DefaultListCellRenderer();
					((JLabel)renderer).setHorizontalAlignment( SwingConstants.CENTER );
					((JLabel)renderer).setOpaque(true);
					
					n�vel.setRenderer(renderer);
					
					// Zera a largura do bot�o
					n�vel.setUI(new BasicComboBoxUI() {
					    @SuppressWarnings("serial")
						@Override
					    protected JButton createArrowButton() {
					    	return new JButton() {
					    		@Override
					    		public int getWidth() {
					    			return 0;
					    		}
					    	};
					    }	
					});
					
					tropaC.gridx = 1;
					tropaPanel.add(n�vel, tropaC);
					
				}
				
				loop++;
				
				c.gridy++;
				panel.add(tropaPanel, c);
				
			}
			
		}
		
		return panel;
		
	}
	
	private JPanel addReligi�o() {
		return null;
	}
	
	private JPanel addNoite() {
		return null;
	}
	
	private JPanel addSorte() {
		return null;
	}
	
	private JPanel addMoral() {
		return null;
	}
	
	private JPanel addMuralha() {
		return null;
	}
	
	private JPanel addEdif�cio() {
		return null;
	}
	
	private JPanel addItemPaladino() {
		return null;
	}
	
	private JPanel addBandeira() {
		return null;
	}
	
	public static void main (String args[]) {
		
		World_Reader.read();
		
		MundoSelecionado.setMundo(World_Reader.getMundo(2));
		
		JFrame test = new JFrame();
		
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		StatInsertion stat = new StatInsertion(Tipo.atacante);
		
		test.add(stat);
		test.pack();
		test.setVisible(true);
		
	}
	
	
}
