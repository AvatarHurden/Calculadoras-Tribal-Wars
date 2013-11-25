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
	// Eles diferem na presen�a ou n�o de mil�cia e nas informa��es adicionais
	// que podem receber
	private enum Tipo {
		atacante, defensor;
	}
	
	private Tipo tipo;
	
	private Map<Unidade, JTextField> mapQuantidades = new HashMap<Unidade, JTextField>();
	private Map<Unidade, JComboBox<Integer>> mapNiveis = new HashMap<Unidade, JComboBox<Integer>>();
	
	private JCheckBox religi�o, noite;
	
	private JTextField sorte, moral, muralha, edif�cio;
	
	private JComboBox<ItemPaladino> item;
	private JComboBox<Bandeira> bandeira;
	
	// vari�vel para a cor dos panels
	int loop = 0;
	
	/**
	 * @param tipo Se o panel � de atacante ou defensor
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
		
		// Diferenciando os diferentes tipos de inser��o
		
		if (tipo == Tipo.atacante) {
			
			// Todos possuem a mudan�a dos insets para garantir que seja mudado,
			// n�o importando a configura��o do mundo em quest�o
			if (MundoSelecionado.hasIgreja()) {
				c.gridy++;
				add(addReligi�o(), c);
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
				add(addReligi�o(), c);
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
			add(addEdif�cio(), c);
			
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
		if (MundoSelecionado.isPesquisaDeN�veis())
			c.gridwidth = 1;
		else
			c.gridwidth = 2;
		
		// Adding the headers
		
		panel.add(new JLabel("Quantidade"), c);
		
		if (MundoSelecionado.isPesquisaDeN�veis()){
			c.gridx = 1;
			panel.add(new JLabel("N�vel"), c);
		}
			
		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 2;
		c.gridx = 0;
			
		for (Unidade i : MundoSelecionado.getUnidades()) {
			
			if (i != null && (!i.equals(Unidade.MIL�CIA) || tipo == Tipo.defensor)) {
				
			JPanel tropaPanel = new JPanel();
			tropaPanel.setLayout(layout);
			tropaPanel.setBackground(Cores.getAlternar(loop));
			tropaPanel.setBorder(new MatteBorder(1,0,0,0,Cores.SEPARAR_CLARO));
			
			GridBagConstraints tropaC = new GridBagConstraints();
			tropaC.insets = new Insets(5,5,5,5);
			tropaC.gridx = 0;
			tropaC.gridy = 0;
			if (MundoSelecionado.isPesquisaDeN�veis())
				tropaC.gridwidth = 1;
			else
				tropaC.gridwidth = 2;
			
			// Creating the TextField for the quantity of troops
			JTextField txt = new JTextField(9);
			// Adding the text to a map with the units
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
					mapNiveis.put(i, n�vel);
					
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
		religi�o = new JCheckBox();
		religi�o.setBackground(Cores.getAlternar(loop));
		
		c.gridx++;
		panel.add(religi�o, c);
		
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
		
		panel.add(new JLabel("B�nus Noturno"), c);
		
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
		// Por ser sempre o �ltimo nos atacantes, fecha as bordas inteiramente
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
				    
				    //Permite a entrada somente de n�meros e sinais e no m�ximo 3 d�gitos
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
				    
				    //Permite a entrada somente de n�meros e no m�ximo 3 d�gitos
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
				    
				    //Permite a entrada somente de n�meros e no m�ximo 3 d�gitos
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
	
	private JPanel addEdif�cio() {
		
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
		
		panel.add(new JLabel("Edif�cio"), c);
		
		// Creating the checkbox to select option
		edif�cio = new JTextField(3);
		edif�cio.setHorizontalAlignment(SwingConstants.CENTER);
		edif�cio.setDocument(new PlainDocument() {
			
			@Override
			 public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				    if (str == null)
				      return;
				    
				    //Permite a entrada somente de n�meros e no m�ximo 3 d�gitos
				    if ((getLength() + str.length()) <= 2 && ( Character.isDigit(str.charAt(0)))) 
				    	super.insertString(offset, str, attr);
				     
				  }
		});
		
		c.gridx++;
		panel.add(edif�cio, c);
		
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
		
		// Coloca a cor padr�o para os comboBox
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
