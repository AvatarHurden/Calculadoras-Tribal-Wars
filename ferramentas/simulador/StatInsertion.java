package simulador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

import simulador.GUI.InputInfo;
import config.Mundo_Reader;
import custom_components.ToolPanel;
import custom_components.TroopFormattedTextField;
import database.Bandeira;
import database.Bandeira.CategoriaBandeira;
import database.Cores;
import database.ItemPaladino;
import database.Unidade;

@SuppressWarnings("serial")
public class StatInsertion extends JPanel {

	// Enum com os tipos diferentes que o panel pode possuir
	// Eles diferem na presença ou não de milícia e nas informações adicionais
	// que podem receber
	protected enum Tipo {
		Atacante, Defensor;
	}

	private Tipo tipo;

	private InputInfo info;

	private Map<Unidade, TroopFormattedTextField> mapQuantidades = new HashMap<Unidade, TroopFormattedTextField>();
	private Map<Unidade, JComboBox<Integer>> mapNiveis = new HashMap<Unidade, JComboBox<Integer>>();

	private JCheckBox religião, noite;

	private JTextField sorte, moral, muralha, edifício;

	private JComboBox<ItemPaladino> item;
	private JComboBox<Bandeira> bandeira;

	// variável para a cor dos panels
	int loop = 0;
	
	private ToolPanel tools;

	/**
	 * @param tipo
	 *            Se o panel é de atacante ou defensor
	 */
	public StatInsertion(Tipo tipo, InputInfo info) {

		this.tipo = tipo;

		this.info = info;
		
		//TODO update both panels on edit
		tools = new ToolPanel((tipo == Tipo.Atacante), null, mapQuantidades);
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 110 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(layout);

		setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		c.insets = new Insets(5, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		add(tools.getModelosPanel(), c);

		
		// Adding the space to allow for militia on defensive side
		if (tipo == Tipo.Atacante && Mundo_Reader.MundoSelecionado.hasMilícia())
			c.insets = new Insets(0, 0, 30, 0);
		else
			c.insets = new Insets(0, 0, 5, 0);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy++;
		add(addUnitPanel(), c);

		c.insets = new Insets(5, 0, 0, 0);
		
		// If militia is active, ensures that the right colors are given to all
		loop = Mundo_Reader.MundoSelecionado.getNúmeroDeTropas();

		// Diferenciando os diferentes tipos de inserção

		if (tipo == Tipo.Atacante) {

			// Todos possuem a mudança dos insets para garantir que seja mudado,
			// não importando a configuração do mundo em questão
			if (Mundo_Reader.MundoSelecionado.hasIgreja()) {
				c.gridy++;
				add(addReligião(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			if (Mundo_Reader.MundoSelecionado.hasPaladino()) {
				c.gridy++;
				add(addItemPaladino(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			if (Mundo_Reader.MundoSelecionado.hasBandeira()) {
				c.gridy++;
				add(addBandeira(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			if (Mundo_Reader.MundoSelecionado.hasMoral()) {
				c.gridy++;
				add(addMoral(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			c.gridy++;
			add(addSorte(), c);

		} else {

			if (Mundo_Reader.MundoSelecionado.hasIgreja()) {
				c.gridy++;
				add(addReligião(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			if (Mundo_Reader.MundoSelecionado.hasPaladino()) {
				c.gridy++;
				add(addItemPaladino(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			if (Mundo_Reader.MundoSelecionado.hasBandeira()) {
				c.gridy++;
				add(addBandeira(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			c.gridy++;
			add(addMuralha(), c);
			c.insets = new Insets(0, 0, 0, 0);

			c.gridy++;
			add(addEdifício(), c);

			if (Mundo_Reader.MundoSelecionado.hasBonusNoturno()) {
				c.gridy++;
				add(addNoite(), c);
			}

		}

	}

	private JPanel addUnitPanel() {

		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		
		// Adding the headers

		JLabel lblTipo = new JLabel(tipo.toString());
		lblTipo.setPreferredSize(new Dimension(
				lblTipo.getPreferredSize().width, 26));
		lblTipo.setBackground(Cores.FUNDO_ESCURO);
		lblTipo.setOpaque(true);
		lblTipo.setHorizontalAlignment(SwingConstants.CENTER);

		// Caso o mundo possua nível de tropas, torna a borda mais grossa para
		// facilitar a visualização da separação.
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis())
			lblTipo.setBorder(new MatteBorder(0, 0, 2, 0, Cores.SEPARAR_ESCURO));
		else
			lblTipo.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_ESCURO));
		
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		panel.add(lblTipo, c);
		
		if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis()) {

			c.fill = GridBagConstraints.NONE;
			c.insets = new Insets(5, 5, 5, 5);
			c.gridwidth = 1;
			c.gridy++;
			panel.add(new JLabel("Quantidade"), c);

			c.gridx = 1;
			panel.add(new JLabel("Nível"), c);

		}

		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx = 0;

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {

			if (i != null
					&& (!i.equals(Unidade.MILÍCIA) || tipo == Tipo.Defensor)) {

				JPanel tropaPanel = new JPanel();
				tropaPanel.setLayout(layout);
				tropaPanel.setBackground(Cores.getAlternar(loop));
				// tropaPanel.setBorder(new
				// MatteBorder(1,0,0,0,Cores.SEPARAR_CLARO));

				GridBagConstraints tropaC = new GridBagConstraints();
				tropaC.insets = new Insets(3, 5, 3, 5);
				tropaC.gridx = 0;
				tropaC.gridy = 0;
				if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis())
					tropaC.gridwidth = 1;
				else
					tropaC.gridwidth = 2;

				// Creating the TextField for the quantity of troops
				TroopFormattedTextField txt = new TroopFormattedTextField(9) {
					public void go() {}
				};
				// Adding the text to a map with the units
				mapQuantidades.put(i, txt);

				tropaPanel.add(txt, tropaC);

				if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis()) {

					// Coloca a cor padrão para os comboBox
					UIManager.put("ComboBox.selectionBackground",
							Cores.FUNDO_ESCURO);
					UIManager.put("ComboBox.background",
							Cores.getAlternar(loop));

					JComboBox<Integer> nível = new JComboBox<Integer>(
							new Integer[] { 1, 2, 3 });

					nível.setOpaque(false);

					// Cria um renderer para set usado no combox, centralizando
					// o texto
					ListCellRenderer<Object> renderer = new DefaultListCellRenderer();
					((JLabel) renderer)
							.setHorizontalAlignment(SwingConstants.CENTER);
					((JLabel) renderer).setOpaque(true);

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
		panel.setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
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
		// Como o painel de edifício fecha embaixo, este não possui em cima para
		// não
		// criar uma borda dupla
		panel.setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
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
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Sorte"), c);

		// Creating the checkbox to select option
		sorte = new JTextField(3);
		sorte.setHorizontalAlignment(SwingConstants.CENTER);
		sorte.setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de números e sinais e no máximo 3
				// dígitos
				if ((getLength() + str.length()) <= 3
						&& (Character.isDigit(str.charAt(0)) || str.charAt(0) == '-')) {
					if (getLength() == 0)
						super.insertString(offset, str, attr);
					else if (Math.abs(Integer.parseInt(getText(0, getLength())
							+ str)) <= 100)
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
		panel.setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Moral"), c);

		// Creating the checkbox to select option
		moral = new JTextField(3);
		moral.setHorizontalAlignment(SwingConstants.CENTER);
		moral.setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de números e no máximo 3 dígitos
				if ((getLength() + str.length()) <= 3
						&& (Character.isDigit(str.charAt(0)))) {
					if (Math.abs(Integer
							.parseInt(getText(0, getLength()) + str)) <= 100)
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
		panel.setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Muralha"), c);

		// Creating the checkbox to select option
		muralha = new JTextField(3);
		muralha.setHorizontalAlignment(SwingConstants.CENTER);
		muralha.setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de números e no máximo 3 dígitos
				if ((getLength() + str.length()) <= 2
						&& (Character.isDigit(str.charAt(0))))
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
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Edifício"), c);

		// Creating the checkbox to select option
		edifício = new JTextField(3);
		edifício.setHorizontalAlignment(SwingConstants.CENTER);
		edifício.setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de números e no máximo 3 dígitos
				if ((getLength() + str.length()) <= 2
						&& (Character.isDigit(str.charAt(0))))
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
		panel.setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 110 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Item Paladino"), c);

		// Coloca a cor padrão para os comboBox
		UIManager.put("ComboBox.selectionBackground", new Color(163, 184, 204));
		UIManager.put("ComboBox.background", Color.white);

		// Creating the checkbox to select option
		item = new JComboBox<ItemPaladino>(ItemPaladino.values());

		item.setFont(new Font(getFont().getName(), getFont().getStyle(), 11));

		// TODO Adicionar tooltip para cada item.

		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				item.setToolTipText(((ItemPaladino) item.getSelectedItem())
						.getDescription());

			}
		});

		c.insets = new Insets(0, 5, 5, 5);
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
		panel.setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 110 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Bandeira"), c);

		// Creating the checkbox to select option
		bandeira = new JComboBox<Bandeira>();

		bandeira.setFont(new Font(getFont().getName(), getFont().getStyle(), 11));

		bandeira.addItem(new Bandeira(CategoriaBandeira.NULL, 0));

		if (tipo == Tipo.Atacante)
			for (int i = 0; i < 9; i++)
				bandeira.addItem(new Bandeira(CategoriaBandeira.ATAQUE, i));
		else
			for (int i = 0; i < 9; i++)
				bandeira.addItem(new Bandeira(CategoriaBandeira.DEFESA, i));

		c.insets = new Insets(0, 5, 5, 5);
		c.gridy++;
		panel.add(bandeira, c);

		// setting variable for next panel
		loop++;

		return panel;

	}

	protected void setInputInfo() {

		// Quantidade de tropas

		Map<Unidade, BigDecimal> tropas = new HashMap<Unidade, BigDecimal>();

		for (Unidade i : Unidade.values())
			if ((!i.equals(Unidade.MILÍCIA) || tipo == Tipo.Defensor)) {
				if (Mundo_Reader.MundoSelecionado.containsUnidade(i))
					tropas.put(i, mapQuantidades.get(i).getValue());
				else
					tropas.put(i, BigDecimal.ZERO);
			} else
				tropas.put(i, BigDecimal.ZERO);

		if (tipo == Tipo.Atacante)
			info.setTropasAtacantes(tropas);
		else
			info.setTropasDefensoras(tropas);

		// Nivel de tropas

		Map<Unidade, Integer> níveis = new HashMap<Unidade, Integer>();

		for (Unidade i : Unidade.values()) {
			if ((!i.equals(Unidade.MILÍCIA) || tipo == Tipo.Defensor)) {
				if (Mundo_Reader.MundoSelecionado.isPesquisaDeNíveis()
						&& Mundo_Reader.MundoSelecionado.containsUnidade(i))
					níveis.put(i, ((int) mapNiveis.get(i).getSelectedItem()));
				else
					níveis.put(i, 1);
			} else
				níveis.put(i, 1);
		}

		if (tipo == Tipo.Atacante)
			info.setNívelTropasAtaque(níveis);
		else
			info.setNívelTropasDefesa(níveis);

		// Religião

		if (Mundo_Reader.MundoSelecionado.hasIgreja()) {

			if (tipo == Tipo.Atacante)
				info.setReligiãoAtacante(religião.isSelected());
			else
				info.setReligiãoDefensor(religião.isSelected());

		} else {

			if (tipo == Tipo.Atacante)
				info.setReligiãoAtacante(true);
			else
				info.setReligiãoDefensor(true);

		}

		// Item do Paladino

		if (Mundo_Reader.MundoSelecionado.hasPaladino()) {

			if (tipo == Tipo.Atacante)
				info.setItemAtacante((ItemPaladino) item.getSelectedItem());
			else
				info.setItemDefensor((ItemPaladino) item.getSelectedItem());

		} else {

			if (tipo == Tipo.Atacante)
				info.setItemAtacante(ItemPaladino.NULL);
			else
				info.setItemDefensor(ItemPaladino.NULL);

		}

		// Bandeira

		if (Mundo_Reader.MundoSelecionado.hasBandeira()) {

			if (tipo == Tipo.Atacante)
				info.setBandeiraAtacante((Bandeira) bandeira.getSelectedItem());
			else
				info.setBandeiraDefensor((Bandeira) bandeira.getSelectedItem());

		} else {

			if (tipo == Tipo.Atacante)
				info.setBandeiraAtacante(new Bandeira(CategoriaBandeira.NULL, 0));
			else
				info.setBandeiraDefensor(new Bandeira(CategoriaBandeira.NULL, 0));

		}

		// Moral e Sorte

		if (tipo == Tipo.Atacante) {

			if (Mundo_Reader.MundoSelecionado.hasMoral()
					&& !moral.getText().equals(""))
				info.setMoral(Integer.parseInt(moral.getText()));
			else
				info.setMoral(100);

			if (!sorte.getText().equals(""))
				info.setSorte(Integer.parseInt(sorte.getText()));
			else
				info.setSorte(0);

		}

		// Muralha, Edifício e Noite

		if (tipo == Tipo.Defensor) {

			if (!muralha.getText().equals(""))
				info.setMuralha(Integer.parseInt(muralha.getText()));
			else
				info.setMuralha(0);

			if (!edifício.getText().equals(""))
				info.setEdifício(Integer.parseInt(edifício.getText()));
			else
				info.setEdifício(0);

			if (Mundo_Reader.MundoSelecionado.hasBonusNoturno())
				info.setNoite(noite.isSelected());
			else
				info.setNoite(false);

		}

	}
	
	public ToolPanel getToolPanel(){
		return tools;
	}

}
