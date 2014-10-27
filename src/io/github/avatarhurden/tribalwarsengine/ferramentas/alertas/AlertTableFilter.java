package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertTable.AlertTableModel;
import io.github.avatarhurden.tribalwarsengine.main.Main;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class AlertTableFilter extends JPanel{

	private Ferramenta parent;
	
	private TableRowSorter<AlertTableModel> sorter;
	private TWSimpleButton filterButton;
	private JDialog dialog;
	
	private JTextField nameField;
	private JCheckBox geral, apoio, ataque, saque;
	private JCheckBox selectedWorld;
	private ArmyEditPanel army;
	
	private OnChange onChange;
	
	public AlertTableFilter(Ferramenta parent) {
		
		this.parent = parent;
		
		onChange = new OnChange() {
			@Override
			public void run() {
//				army.saveValues();
				setFilter();
			}
		};
		
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		setBackground(Cores.FUNDO_CLARO);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		makeFilterDialog();
		
		add(makeSwitchPanel());
	}
	
	@SuppressWarnings("unchecked")
	public void setSorter(RowSorter<? extends TableModel> rowSorter) {
		this.sorter = (TableRowSorter<AlertTableModel>) rowSorter;
	}
	
	private JPanel makeSwitchPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		filterButton = new TWSimpleButton(new ImageIcon(Imagens.getImage("down_arrow.png")));
		
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (!dialog.isVisible()) {
					filterButton.setIcon(new ImageIcon(Imagens.getImage("up_arrow.png")));
					dialog.setLocation(getLocationOnScreen().x, 
							getLocationOnScreen().y + 30);
					
				} else 
					filterButton.setIcon(new ImageIcon(Imagens.getImage("down_arrow.png")));

				dialog.setVisible(!dialog.isVisible());
				
			}
		});
		
		panel.add(filterButton);
		panel.add(new JLabel("Filtros"));
		
		return panel;
	}
	
	private void makeFilterDialog() {
		dialog = new JDialog(Main.getCurrentFrame(), false);
		dialog.setUndecorated(true);
		dialog.getContentPane().setBackground(Cores.FUNDO_CLARO);
		
		dialog.add(makeFilterPanel());
		dialog.pack();
		dialog.setVisible(false);
		dialog.setAlwaysOnTop(true);
		
		Main.getCurrentFrame().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				dialog.setLocation(getLocationOnScreen().x, 
						getLocationOnScreen().y + 30);
			}
		});
		
		dialog.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				if (Main.getCurrentFrame().hasFocus())
					dialog.setAlwaysOnTop(true);
				else
					dialog.setAlwaysOnTop(false);
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				//Main.getCurrentFrame().requestFocus();
			}
		});
		
		Main.getCurrentFrame().addWindowListener(new WindowAdapter() {
			
			public void windowActivated(WindowEvent arg0) {
				dialog.setAlwaysOnTop(true);
			}
			
			public void windowDeactivated(WindowEvent arg0) {
				dialog.setAlwaysOnTop(false);
			}
		});
		
		parent.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent arg0) {
				if (dialog.isVisible())
					filterButton.doClick();
			}
		});	
	}
	
	private JPanel makeFilterPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		panel.add(makeNamePanel(), c);
		
		c.gridx++;
		panel.add(makeTipoPanel(), c);
		
//		c.gridx++;
//		c.gridheight = 2;
//		panel.add(makeArmyPanel(), c);

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
		
		if ((boolean) AlertManager.getInstance().getConfig("show_only_selected", false)) {
			selectedWorld.setSelected(true);
			selectedWorld.setEnabled(false);
		}
		
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
	
	private boolean isSelectedWorld(String name) {
		if (selectedWorld.isSelected())
			return name.equals(WorldManager.getSelectedWorld().getPrettyName());
		
		return true;
	}
	
	public void setFilter() {
		
		RowFilter<TableModel, Integer> filter = new RowFilter<TableModel, Integer>() {

			@Override
			public boolean include(
					javax.swing.RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
				boolean toAdd = true;
				
				toAdd &= RowFilter.regexFilter(nameField.getText(), 0).include(entry);
				toAdd &= isSelectedType((Tipo) entry.getValue(1));
//				toAdd &= hasArmy((Army) entry.getValue(4));
				toAdd &= isSelectedWorld((String) entry.getValue(8));
				
				return toAdd;
			}
		};
		
		sorter.setRowFilter(filter);
		
	}
	
}