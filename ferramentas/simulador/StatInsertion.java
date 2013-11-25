package simulador;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import config.World_Reader;
import database.Bandeira;
import database.Cores;
import database.ItemPaladino;
import database.MundoSelecionado;
import database.Unidade;
import database.Bandeira.CategoriaBandeira;

@SuppressWarnings("serial")
public class StatInsertion extends JPanel{

	// Enum com os tipos diferentes que o panel pode possuir
	// Eles diferem na presença ou não de milícia e nas informações adicionais
	// que podem receber
	private enum Tipo {
		atacante, defensor;
	}
	
	private Tipo tipo;
	
	private Map<Unidade, JTextField> mapQuantidades = new HashMap<Unidade, JTextField>();
	private Map<Unidade, JComboBox<Integer>> mapNiveis = new HashMap<Unidade, JComboBox<Integer>>();
	
	private JCheckBox religião, noite;
	
	private JTextField sorte, moral, muralha, edifício;
	
	private JComboBox<ItemPaladino> item;
	private JComboBox<Bandeira> bandeira;
	
	// variável para a cor dos panels
	int loop = 0;
	
	/**
	 * @param tipo Se o panel é de atacante ou defensor
	 */
	public StatInsertion(Tipo tipo) {
		
		this.tipo = tipo;
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {110};
		layout.rowHeights = new int[] {0};
		layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		
		
		add(addUnitPanel(), c);
		
		c.insets = new Insets(5,5,0,5);
		
		// Diferenciando os diferentes tipos de inserção
		
		if (tipo == Tipo.atacante) {
			
			// Todos possuem a mudança dos insets para garantir que seja mudado,
			// não importando a configuração do mundo em questão
			if (MundoSelecionado.hasIgreja()) {
				c.gridy++;
				add(addReligião(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			if (MundoSelecionado.hasPaladino()) {
				c.gridy++;
				add(addItemPaladino(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			if (MundoSelecionado.hasBandeira()) {
				c.gridy++;
				add(addBandeira(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			if (MundoSelecionado.hasMoral()) {
				c.gridy++;
				add(addMoral(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			c.gridy++;
			add(addSorte(), c);
			
		} else {
			
			if (MundoSelecionado.hasIgreja()) {
				c.gridy++;
				add(addReligião(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			if (MundoSelecionado.hasPaladino()) {
				c.gridy++;
				add(addItemPaladino(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			if (MundoSelecionado.hasBandeira()) {
				c.gridy++;
				add(addBandeira(), c);
				c.insets = new Insets(0,5,0,5);
			}
			
			c.gridy++;
			add(addMuralha(), c);
			
			c.gridy++;
			add(addEdifício(), c);
			
			c.gridy++;
			add(addNoite(), c);
			
		}
		
	}
	
	private JPanel addUnitPanel() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
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
		if (MundoSelecionado.isPesquisaDeNíveis())
			c.gridwidth = 1;
		else
			c.gridwidth = 2;
		
		// Adding the headers
		
		panel.add(new JLabel("Quantidade"), c);
		
		if (MundoSelecionado.isPesquisaDeNíveis()){
			c.gridx = 1;
			panel.add(new JLabel("Nível"), c);
		}
			
		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 2;
		c.gridx = 0;
			
		for (Unidade i : MundoSelecionado.getUnidades()) {
			
			if (i != null && (!i.equals(Unidade.MILÍCIA) || tipo == Tipo.defensor)) {
				
			JPanel tropaPanel = new JPanel();
			tropaPanel.setLayout(layout);
			tropaPanel.setBackground(Cores.getAlternar(loop));
			tropaPanel.setBorder(new MatteBorder(1,0,0,0,Cores.SEPARAR_CLARO));
			
			GridBagConstraints tropaC = new GridBagConstraints();
			tropaC.insets = new Insets(5,5,5,5);
			tropaC.gridx = 0;
			tropaC.gridy = 0;
			if (MundoSelecionado.isPesquisaDeNíveis())
				tropaC.gridwidth = 1;
			else
				tropaC.gridwidth = 2;
			
			// Creating the TextField for the quantity of troops
			JTextField txt = new JTextField(9);
			// Adding the text to a map with the units
			mapQuantidades.put(i, txt);
			
			tropaPanel.add(txt, tropaC);
			
				if (MundoSelecionado.isPesquisaDeNíveis()) {
					
					// Coloca a cor padrão para os comboBox
					UIManager.put("ComboBox.selectionBackground", Cores.FUNDO_ESCURO); 
					UIManager.put("ComboBox.background", Cores.getAlternar(loop)); 
					
					JComboBox<Integer> nível = new JComboBox<Integer>(new Integer[]{1,2,3});
					
					nível.setOpaque(false);
					
					// Cria um renderer para set usado no combox, centralizando o texto
					ListCellRenderer<Object> renderer = new DefaultListCellRenderer();
					((JLabel)renderer).setHorizontalAlignment( SwingConstants.CENTER );
					((JLabel)renderer).setOpaque(true);
					
					nível.setRenderer(renderer);
					
					// Zera a largura do botão
					nível.setUI(new BasicComboBoxUI() {
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
					
					// Adding the comboBox to the map with units
					mapNiveis.put(i, nível);
					
					tropaC.gridx = 1;
					tropaPanel.add(nível, tropaC);
					
				}
				
				loop++;
				
				c.gridy++;
				panel.add(tropaPanel, c);
				
			}
			
		}
		
		return panel;
		
	}
	
	private JPanel addReligião() {
		
		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
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
		
		panel.add(new JLabel("Religioso"), c);
		
		// Creating the checkbox to select option
		religião = new JCheckBox();
		religião.setBackground(Cores.getAlternar(loop));
		
		c.gridx++;
		panel.add(religião, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	private JPanel addNoite() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
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
		
		panel.add(new JLabel("Bônus Noturno"), c);
		
		// Creating the checkbox to select option
		noite = new JCheckBox();
		noite.setBackground(Cores.getAlternar(loop));
		
		c.gridx++;
		panel.add(noite, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	private JPanel addSorte() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		// Por ser sempre o último nos atacantes, fecha as bordas inteiramente
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
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
		
		panel.add(new JLabel("Sorte"), c);
		
		// Creating the checkbox to select option
		sorte = new JTextField(3);
		sorte.setHorizontalAlignment(SwingConstants.CENTER);
		sorte.setDocument(new PlainDocument() {
			
			@Override
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    //Permite a entrada somente de números e sinais e no máximo 3 dígitos
				    if ((getLength() + str.length()) <= 3 && ( Character.isDigit(str.charAt(0)) || str.charAt(0) == '-')) {
				    	if (getLength() == 0)
				    		 super.insertString(offset, str, attr);
				    	else if (Math.abs(Integer.parseInt(getText(0, getLength())+str)) <= 100 )
				    		 super.insertString(offset, str, attr);
				     
				    }
				  }
		});
		
		c.gridx++;
		panel.add(sorte, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;		
		
	}
	
	private JPanel addMoral() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
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
		
		panel.add(new JLabel("Moral"), c);
		
		// Creating the checkbox to select option
		moral = new JTextField(3);
		moral.setHorizontalAlignment(SwingConstants.CENTER);
		moral.setDocument(new PlainDocument() {
			
			@Override
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    //Permite a entrada somente de números e no máximo 3 dígitos
				    if ((getLength() + str.length()) <= 3 && ( Character.isDigit(str.charAt(0)))) {
				    	if (Math.abs(Integer.parseInt(getText(0, getLength())+str)) <= 100 )
				    		 super.insertString(offset, str, attr);
				     
				    }
				  }
		});
		
		c.gridx++;
		panel.add(moral, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	private JPanel addMuralha() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
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
		
		panel.add(new JLabel("Muralha"), c);
		
		// Creating the checkbox to select option
		muralha = new JTextField(3);
		muralha.setHorizontalAlignment(SwingConstants.CENTER);
		muralha.setDocument(new PlainDocument() {
			
			@Override
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    //Permite a entrada somente de números e no máximo 3 dígitos
				    if ((getLength() + str.length()) <= 2 && ( Character.isDigit(str.charAt(0)))) 
				    	super.insertString(offset, str, attr);
				     
				  }
		});
		
		c.gridx++;
		panel.add(muralha, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	private JPanel addEdifício() {
		
		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
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
		
		panel.add(new JLabel("Edifício"), c);
		
		// Creating the checkbox to select option
		edifício = new JTextField(3);
		edifício.setHorizontalAlignment(SwingConstants.CENTER);
		edifício.setDocument(new PlainDocument() {
			
			@Override
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    //Permite a entrada somente de números e no máximo 3 dígitos
				    if ((getLength() + str.length()) <= 2 && ( Character.isDigit(str.charAt(0)))) 
				    	super.insertString(offset, str, attr);
				     
				  }
		});
		
		c.gridx++;
		panel.add(edifício, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	private JPanel addItemPaladino() {
		
		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {110};
		layout.rowHeights = new int[] {0};
		layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		
		panel.add(new JLabel("Item Paladino"), c);
		
		// Coloca a cor padrão para os comboBox
		UIManager.put("ComboBox.selectionBackground", new Color(163, 184, 204)); 
		UIManager.put("ComboBox.background", Color.white); 
		
		// Creating the checkbox to select option
		item = new JComboBox<ItemPaladino>(ItemPaladino.values());
		
		item.setFont(new Font(getFont().getName(), getFont().getStyle(), 11));
		
		//TODO Adicionar tooltip para cada item.
		
		item.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent arg0) {
				item.setToolTipText(((ItemPaladino) item.getSelectedItem()).getDescription()); 
				
			}
		});
		
		
		c.gridy++;
		panel.add(item, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	private JPanel addBandeira() {
		
		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		panel.setBackground(Cores.getAlternar(loop));
		panel.setBorder(new MatteBorder(1,1,0,1,Cores.SEPARAR_ESCURO));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {110};
		layout.rowHeights = new int[] {0};
		layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		
		panel.add(new JLabel("Bandeira"), c);
		
		// Creating the checkbox to select option
		bandeira = new JComboBox<Bandeira>();
		
		bandeira.setFont(new Font(getFont().getName(), getFont().getStyle(), 11));
		
		for (Bandeira.CategoriaBandeira b : Bandeira.CategoriaBandeira.values()) {
			
			if (!b.getTipo().equals(CategoriaBandeira.Tipo.Produtivo)) {
				for(int i = 0; i<9; i++)
					bandeira.addItem(new Bandeira(b, i));
			}
			
		}
		
		c.gridy++;
		panel.add(bandeira, c);
		
		// setting variable for next panel
		loop++;
		
		return panel;
		
	}
	
	public static void main (String args[]) {
		
		World_Reader.read();
		
		MundoSelecionado.setMundo(World_Reader.getMundo(22));
		
		JFrame test = new JFrame();
		
		test.setLayout(new GridBagLayout());
		
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		StatInsertion stat = new StatInsertion(Tipo.atacante);
		
		StatInsertion stat2 = new StatInsertion(Tipo.defensor);
		
		test.add(stat);
		test.add(stat2);
		test.pack();
		test.setVisible(true);
		
	}
	
	
}
