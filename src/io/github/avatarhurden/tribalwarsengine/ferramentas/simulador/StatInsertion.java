package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.enums.ItemPaladino;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.InputInfo;
import io.github.avatarhurden.tribalwarsengine.managers.ServerManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.tools.ToolManager;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import config.Lang;
import database.Bandeira;
import database.Bandeira.CategoriaBandeira;
import database.Cores;

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

	private Army army;
	private ArmyEditPanel armyEdit;
	
	private JCheckBox religião, noite;

	private JTextField sorte, moral, muralha, edifício;

	private JComboBox<ItemPaladino> item;
	private JComboBox<Bandeira> bandeira;

	// variável para a cor dos panels
	int loop = 1;

	/**
	 * @param tipo
	 *            Se o panel é de atacante ou defensor
	 */
	public StatInsertion(Tipo tipo, InputInfo info, ToolManager tool) {
		
		this.tipo = tipo;
		this.info = info;
		
		if (tipo.equals(Tipo.Atacante))
			army = new Army(Army.getAttackingUnits());
		else
			army = new Army(Army.getAvailableUnits());
		
		if (ServerManager.getSelectedServer().getWorld().getResearchSystemLevels()> 1)
			armyEdit = army.getEditPanelFull(new OnChange() {
				public void run() {
				}
			}, 26);
		else {
			armyEdit = army.getEditPanelFullNoHeader(new OnChange() {
				public void run() {
				}
			}, 26);
			armyEdit.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		}
		setLayout(new GridBagLayout());

		setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		c.insets = new Insets(5, 0, 5, 0);
		c.anchor = GridBagConstraints.EAST;
		add(tool.addModelosTropasPanel(true, armyEdit), c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		
		c.insets = new Insets(5, 0, 5, 0);
		c.gridy++;
		add(addTipoPanel(), c);
		
		// Adding the space to allow for militia on defensive side
		if (tipo == Tipo.Atacante && Army.containsUnit("militia"))
			c.insets = new Insets(0, 0, 31, 0);
		else
			c.insets = new Insets(0, 0, 5, 0);
		
		c.gridy++;
		add(addUnitPanel(), c);
		
		c.insets = new Insets(5, 0, 0, 0);
		// Diferenciando os diferentes tipos de inserção

		if (ServerManager.getSelectedServer().getWorld().isChurchWorld()) {
			c.gridy++;
			add(addReligião(), c);
			c.insets = new Insets(0, 0, 0, 0);
		}
		
		if (ServerManager.getSelectedServer().getWorld().isPaladinWorld()) {
			c.gridy++;
			add(addItemPaladino(), c);
			c.insets = new Insets(0, 0, 0, 0);
		}
		
//		if (Mundo_Reader.MundoSelecionado.hasBandeira()) {
//			c.gridy++;
//			add(addBandeira(), c);
//			c.insets = new Insets(0, 0, 0, 0);
//		}
		
		if (tipo == Tipo.Atacante) {

			if (ServerManager.getSelectedServer().getWorld().isMoralWorld()) {
				c.gridy++;
				add(addMoral(), c);
				c.insets = new Insets(0, 0, 0, 0);
			}

			c.gridy++;
			add(addSorte(), c);

		} else {

			c.gridy++;
			add(addMuralha(), c);
			c.insets = new Insets(0, 0, 0, 0);

			c.gridy++;
			add(addEdifício(), c);

			if (ServerManager.getSelectedServer().getWorld().isNightBonusWorld()) {
				c.gridy++;
				add(addNoite(), c);
			}

		}

	}

	private JPanel addTipoPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		panel.add(new JLabel(tipo.toString()));
		
		panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 30));
		
		return panel;
	}
	
	private JPanel addUnitPanel() {

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		panel.add(armyEdit, c);
		
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

		panel.add(new JLabel(Lang.Religioso.toString()), c);

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
		// não criar uma borda dupla
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

		panel.add(new JLabel(Lang.BonusNoturno.toString()), c);

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

		panel.add(new JLabel(Lang.Sorte.toString()), c);

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
						super.insertString(offset, str, attr);
						
				if (!super.getText(0, getLength()).equals("-") &&
						Math.abs(Integer.parseInt(getText(0, getLength()))) > 25) {
					
					if (super.getText(0, 1).equals("-")) {
						super.remove(1, getLength()-1);
						super.insertString(1, "25", attr);
					} else {
						super.remove(0, getLength());
						super.insertString(0, "25", attr);
					}
					
				}


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

		panel.add(new JLabel(Lang.Moral.toString()), c);

		// Creating the checkbox to select option
		moral = new IntegerFormattedTextField(0, 3, 100) {
			public void go() {}
		};
		moral.setHorizontalAlignment(SwingConstants.CENTER);
		
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

		panel.add(new JLabel(Lang.Muralha.toString()), c);

		// Creating the checkbox to select option
		muralha = new IntegerFormattedTextField(0, 2, 20) {
			public void go() {}
		};
		muralha.setHorizontalAlignment(SwingConstants.CENTER);
		
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
		panel.setToolTipText("Edifício alvo das catapultas");

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

		panel.add(new JLabel(Lang.Edificio.toString()), c);

		// Creating the checkbox to select option
		edifício = new IntegerFormattedTextField(0, 2) {
			public void go() {}
		};
		edifício.setHorizontalAlignment(SwingConstants.CENTER);
		
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

		panel.add(new JLabel(Lang.ItemPaladino.toString()), c);

		// Coloca a cor padrão para os comboBox
		UIManager.put("ComboBox.selectionBackground", new Color(163, 184, 204));
		UIManager.put("ComboBox.background", Color.white);

		// Creating the checkbox to select option
		item = new JComboBox<ItemPaladino>(ItemPaladino.values());
		
		item.setRenderer(new DefaultListCellRenderer() {
			
			 @Override
			 public Component getListCellRendererComponent(JList<?> list, Object value,
			        int index, boolean isSelected, boolean cellHasFocus) {
				 
				 JComponent comp = (JComponent) super.getListCellRendererComponent(list,
			                value, index, isSelected, cellHasFocus);
				 
				 if (index > -1 && index < ItemPaladino.values().length)
				 list.setToolTipText(ItemPaladino.values()[index].getDescription());
				 
				 return comp;
				 
			 }
			
		});
		
		item.setFont(new Font(getFont().getName(), getFont().getStyle(), 11));

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

		panel.add(new JLabel(Lang.Bandeira.toString()), c);

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
		armyEdit.saveValues();
		
		if (tipo == Tipo.Atacante)
			info.setArmyAttacker(army);
		else
			info.setArmyDefender(army);

		// Religião

		if (ServerManager.getSelectedServer().getWorld().isChurchWorld()) {

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

		if (ServerManager.getSelectedServer().getWorld().isPaladinWorld()) {

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

//		if (Mundo_Reader.MundoSelecionado.hasBandeira()) {
//
//			if (tipo == Tipo.Atacante)
//				info.setBandeiraAtacante((Bandeira) bandeira.getSelectedItem());
//			else
//				info.setBandeiraDefensor((Bandeira) bandeira.getSelectedItem());
//
//		} else {
//
//			if (tipo == Tipo.Atacante)
//				info.setBandeiraAtacante(new Bandeira(CategoriaBandeira.NULL, 0));
//			else
//				info.setBandeiraDefensor(new Bandeira(CategoriaBandeira.NULL, 0));
//
//		}

		// Moral e Sorte

		if (tipo == Tipo.Atacante) {

			if (ServerManager.getSelectedServer().getWorld().isMoralWorld()
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

			if (ServerManager.getSelectedServer().getWorld().isNightBonusWorld())
				info.setNoite(noite.isSelected());
			else
				info.setNoite(false);

		}

	}

	public void resetValues() {
	
		armyEdit.resetComponents();
		
		if (religião != null)
			religião.setSelected(false);
		if (noite != null)
			noite.setSelected(false);
		
		if (tipo == Tipo.Atacante) {
			sorte.setText("");
			if (moral != null)
				moral.setText("");
		} else {
			muralha.setText("");
			edifício.setText("");
		}
		
		if (item != null)
			item.setSelectedIndex(0);
		if (bandeira != null)
			bandeira.setSelectedIndex(0);
	}
	
}
