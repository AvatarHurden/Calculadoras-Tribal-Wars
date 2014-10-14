package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class AlertTableFilter extends JPanel{

	private TableRowSorter<TableModel> sorter;
	private JScrollPane tablePane;
	private TWSimpleButton filtrar;
	private JPanel filtersPanel;
	
	private JTextField nameField;
	private JCheckBox geral, apoio, ataque, saque;
	private JCheckBox selectedWorld;
	private ArmyEditPanel army;
	
	private OnChange onChange;
	
	public AlertTableFilter(JScrollPane pane) {
		
		onChange = new OnChange() {
			@Override
			public void run() {
				army.saveValues();
				setFilter();
			}
		};
		
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		setBackground(Cores.FUNDO_CLARO);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		filtersPanel = makeFilterPanel();
		filtersPanel.setVisible(false);
		tablePane = pane;
		
		add(makeSwitchPanel());
		add(filtersPanel);
	}
	
	public void setSorter(TableRowSorter<? extends TableModel> rowSorter) {
		this.sorter = (TableRowSorter<TableModel>) rowSorter;
	}
	
	private JPanel makeSwitchPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		filtrar = new TWSimpleButton(new ImageIcon(Imagens.getImage("down_arrow.png")));
		
		filtrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int width = tablePane.getPreferredSize().width;
				int height = tablePane.getPreferredSize().height;
				int diff = filtersPanel.getPreferredSize().height;
				
				if (!filtersPanel.isVisible()) {
					filtersPanel.setVisible(true);
					filtrar.setIcon(new ImageIcon(Imagens.getImage("up_arrow.png")));
					tablePane.setPreferredSize(new Dimension(width, height - diff));
				} else {
					filtersPanel.setVisible(false);
					filtrar.setIcon(new ImageIcon(Imagens.getImage("down_arrow.png")));
					tablePane.setPreferredSize(new Dimension(width, height + diff));
				}
				
			}
		});
		
		panel.add(filtrar);
		panel.add(new JLabel("Filtros"));
		
		return panel;
	}
	
	private JPanel makeFilterPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		panel.add(makeNamePanel(), c);
		
		c.gridx++;
		panel.add(makeTipoPanel(), c);
		
		c.gridx++;
		c.gridheight = 2;
		panel.add(makeArmyPanel(), c);

		c.gridheight = 1;
		c.gridx = 0;
		c.gridy++;
		panel.add(makeSelectedWorldPanel(), c);
		
		return panel;
	}
	
	private JPanel makeNamePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Nome"));
		
		nameField = new JTextField(15);
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent arg0) {
				onChange.run();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				onChange.run();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {}
		});
		panel.add(nameField);
		
		return panel;
	}
	
	private JPanel makeTipoPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Tipo"));
	
		geral = new JCheckBox("Geral");
		geral.setOpaque(false);
		geral.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				onChange.run();
			}
		});
		
		ataque = new JCheckBox("Ataque");
		ataque.setOpaque(false);
		ataque.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				onChange.run();
			}
		});
		
		apoio = new JCheckBox("Apoio");
		apoio.setOpaque(false);
		apoio.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				onChange.run();
			}
		});
		
		saque = new JCheckBox("Saque");
		saque.setOpaque(false);
		saque.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				onChange.run();
			}
		});
		
		panel.add(geral);
		panel.add(ataque);
		panel.add(apoio);
		panel.add(saque);
		
		return panel;
	}
	
	private JPanel makeSelectedWorldPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Mundos"));
		
		selectedWorld = new JCheckBox("Apenas o mundo atual");
		selectedWorld.setOpaque(false);
		selectedWorld.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				onChange.run();
			}
		});
		
		panel.add(selectedWorld);
		
		return panel;
	}
	
	private JPanel makeArmyPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Tropas"));
		
		army = new Army().getEditPanelSelectionNoHeader(onChange, 30);
		//Custom(onChange, 30, false, true, true, true, false);
		army.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		panel.add(army);
		
		return panel;
	}
	
	private boolean isSelectedType(Tipo tipo) {
		if (!geral.isSelected() && !apoio.isSelected() && !ataque.isSelected() && !saque.isSelected())
			return true;
		
		else switch (tipo) {
			case Geral:
				return geral.isSelected();
			case Ataque:
				return ataque.isSelected();
			case Apoio:
				return apoio.isSelected();
			case Saque:
				return saque.isSelected();
			default:
				return true;
		}
	}
	
	private boolean hasArmy(Army a) {
		boolean res = true;
		
		for (Troop t : army.getArmy().getTropas())
			if (t.getQuantity() > 0) {
				if (a == null) {
					res = false;
					break;
				} else
					res = a.getQuantidade(t.getName()) > 0;
			}
				
		return res;
	}
	
	public void setFilter() {
		
		RowFilter<TableModel, Integer> filter = new RowFilter<TableModel, Integer>() {

			@Override
			public boolean include(
					javax.swing.RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
				boolean toAdd = true;
				
				toAdd &= RowFilter.regexFilter(nameField.getText(), 0).include(entry);
				toAdd &= isSelectedType((Tipo) entry.getValue(1));
				toAdd &= hasArmy((Army) entry.getValue(4));
				
				return toAdd;
			}
		};
		
		sorter.setRowFilter(filter);
		
	}
	
}