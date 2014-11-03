package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.main.Main;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AlertTableFilterEditor extends JPanel {

	private Ferramenta parent;
	private TWSimpleButton filterButton;
	private JDialog dialog;
	
	private OnChange onChange;

	private JTextField nameField;
	private JCheckBox geral, apoio, ataque, saque;
	private JCheckBox selectedWorld;
	private ArmyEditPanel army;
	
	public AlertTableFilterEditor(Ferramenta parent, OnChange onChange) {
		this.parent = parent;
		
		this.onChange = onChange;

		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		setBackground(Cores.FUNDO_CLARO);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		makeFilterDialog();
		
		add(makeSwitchPanel());
	}
	
	private JPanel makeSwitchPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		filterButton = new TWSimpleButton(new ImageIcon(Imagens.getImage("down_arrow.png")));
		
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (dialog.isVisible()) {
					dialog.setAlwaysOnTop(false);
					dialog.setVisible(false);
					filterButton.setIcon(new ImageIcon(Imagens.getImage("down_arrow.png")));
				} else {
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
					dialog.setLocation(getLocationOnScreen().x, 
							getLocationOnScreen().y + 30);
					filterButton.setIcon(new ImageIcon(Imagens.getImage("up_arrow.png")));
				}
					
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
		dialog.setAlwaysOnTop(false);
		
		Main.getCurrentFrame().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				if (dialog.isVisible())
					dialog.setLocation(getLocationOnScreen().x, 
						getLocationOnScreen().y + 30);
			}
		});
		
		
		Main.getCurrentFrame().addWindowListener(new WindowAdapter() {
			
			public void windowActivated(WindowEvent arg0) {
				if (dialog.isVisible())
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
		
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event instanceof MouseEvent)
					if (SwingUtilities.isDescendingFrom(
							(Component) event.getSource(), parent)) {
						MouseEvent e = (MouseEvent) event;
						if (e.getID() == MouseEvent.MOUSE_CLICKED && e.getClickCount() == 2)
							if (dialog.isVisible())
								filterButton.doClick();
							
					}
						
			}
		}, AWTEvent.MOUSE_EVENT_MASK);
		
		parent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && dialog.isVisible())
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
	
	public String getFilterName() {
		return nameField.getText();
	}
	
	public boolean getFilterType(Tipo tipo) {
		switch (tipo) {
		case Geral:
			return geral.isSelected();
		case Ataque:
			return ataque.isSelected();
		case Apoio:
			return apoio.isSelected();
		case Saque:
			return saque.isSelected();
		default:
			return false;
		}
	}
	
	public boolean getFilterCurrentWorld() {
		return selectedWorld.isSelected();
	}
	
	public Army getFilterArmy() {
		return army.getArmy();
	}
	
}
