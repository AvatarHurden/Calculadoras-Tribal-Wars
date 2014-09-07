package io.github.avatarhurden.tribalwarsengine.ferramentas.oponentes_derrotados;

import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Lang;
import database.Cores;

@SuppressWarnings("serial")
public class OponentesDerrotadosPanel extends Ferramenta {
	
	private Army army;
	private ArmyEditPanel armyEdit;
	
	private OnChange onChange;

	private List<ODTropaPanel> panelUnidadeList = new ArrayList<ODTropaPanel>();

	private ODTropaPanel armyInformationPanel;
	
	JPanel panelButtons;
	JRadioButton buttonDefesa;
	JRadioButton buttonAtaque;
	
	public OponentesDerrotadosPanel() {

		super(Lang.FerramentaOD.toString());
		
		onChange = new OnChange() {
			public void run() {
				armyEdit.saveValues();
				for (ODTropaPanel p : panelUnidadeList)
					if (buttonDefesa.isSelected())
						p.changeODDefesa(army.getTropas().get(panelUnidadeList.indexOf(p)));
					else
						p.changeODAtaque(army.getTropas().get(panelUnidadeList.indexOf(p)));
				if (buttonDefesa.isSelected())
					armyInformationPanel.changeODDefesa(army);
				else
					armyInformationPanel.changeODAtaque(army);
			}
		};

		army = new Army(Army.getAvailableUnits());
		armyEdit = army.getEditPanelNoLevels(onChange, 30);
		
		makePanels();
	}
	
	protected void makeGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), gbc);
		
		gbc.anchor = GridBagConstraints.EAST;
		add(tools.addModelosTropasPanel(true, armyEdit), gbc);
		
		gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy++;
        add(armyEdit, gbc);
        
		createPanelButtons();
		gbc.gridy++;
		add(panelButtons, gbc);

		gbc.gridheight = 2;
		gbc.gridy--;
        gbc.gridx++;
        add(dadosPrincipaisPanel(), gbc);
	}
	
	private void makePanels() {
		
		for (int i = 0; i < army.getTropas().size(); i++) {
			   ODTropaPanel panel = new ODTropaPanel(getNextColor());
			   panel.setBorder(new MatteBorder(0, 1, 0, 1, Cores.SEPARAR_ESCURO));
			   panelUnidadeList.add(panel);
		   }
		   
		// Fixes the border of first and last panel
		panelUnidadeList.get(0).setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));
		panelUnidadeList.get(panelUnidadeList.size()-1).setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));
		   
		armyInformationPanel = new ODTropaPanel(getNextColor());
		armyInformationPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
	}
	
	private JPanel dadosPrincipaisPanel() {
	   
	   JPanel panel = new JPanel();
	   panel.setOpaque(false);
	   panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	   
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   for (ODTropaPanel u : panelUnidadeList)
		   panel.add(u);
	   
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   panel.add(armyInformationPanel);
	   
	   return panel;
    }
	    
    private JPanel dadosPrincipaisHeader() {
    	JPanel header = new JPanel();
    	header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	header.setBackground(Cores.FUNDO_ESCURO);
    	header.setPreferredSize(new Dimension(100, 32));
    	header.setLayout(new GridBagLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.insets = new Insets(5, 5, 5, 5);

    	header.add(new JLabel("OD"), c);
	   
    	return header;	   
    }

	
	// Cria um painel com os botões para selecionar se o OD mostrado é de ataque
	// ou defesa
	private void createPanelButtons() {

		buttonAtaque = new JRadioButton(Lang.ODAtaque.toString());
		buttonAtaque.setOpaque(false);

		buttonAtaque.addItemListener(new buttonChangeListener());

		buttonDefesa = new JRadioButton(Lang.ODDefesa.toString());
		buttonDefesa.setOpaque(false);

		buttonDefesa.addItemListener(new buttonChangeListener());

		ButtonGroup group = new ButtonGroup();
		group.add(buttonAtaque);
		group.add(buttonDefesa);

		buttonAtaque.setSelected(true);

		panelButtons = new JPanel(new GridBagLayout());
		panelButtons.add(buttonAtaque);
		panelButtons.add(buttonDefesa);

		panelButtons.setOpaque(false);

	}

	// Classe para os botões de ataque e defesa
	class buttonChangeListener implements ItemListener {
		public void itemStateChanged(ItemEvent arg0) {
			onChange.run();
		}
	}
	
	private ActionListener getResetButtonAction() {
    	
    	ActionListener action = new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {	
            	armyEdit.resetComponents();
            }
        };

        return action;
    }

}
