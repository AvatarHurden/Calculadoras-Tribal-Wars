package io.github.avatarhurden.tribalwarsengine.ferramentas.dados_de_unidade;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Lang;
import database.Cores;

public class DadosDeUnidadePanel extends Ferramenta {
	
    private Army army;
    private ArmyEditPanel armyEdit;
    
    private List<InformaçãoTropaPanel> panelTropaList = new ArrayList<InformaçãoTropaPanel>();

    private InformaçãoTropaPanel armyInformationPanel;
    
    private OnChange onChange;
    
    /**
     * Ferramenta com informações de unidades. Possui:
     * <br>- Ataque
     * <br>- Defesas
     * <br>- Saque
     * <br>- Custo de recursos para produção
     * <br>- Uso de população
     * <p/>
     * Em caso de mundo com níveis, é possível escolher o nível das unidades
     * (não é limitado a 15 níveis)
     * <p/>
     * Não consigo entender a gambiarra ( Os recursos tecnicos avançados ) que você usou nesse jPanel... Se puder ajeitar eu agradeceria :)
     */
    public DadosDeUnidadePanel() {
        super(Lang.FerramentaUnidade.toString());
        
        onChange = new OnChange() {
			public void run() {
				armyEdit.saveValues();
				for (int i = 0; i < panelTropaList.size(); i++)
					panelTropaList.get(i).changeValues(army.getTropas().get(i));

				armyInformationPanel.changeValues(army);
			}
		};
		
        army = new Army(Army.getAvailableUnits());
        armyEdit = army.getEditPanelFull(onChange, 30);
        
        makePanels();
        
        setGUI();
    }
    
    private void setGUI() {
    	GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
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
        
        gbc.gridx++;
        add(dadosPrincipaisPanel(), gbc);
        
        gbc.gridx++;
        add(dadosCustoPanel(), gbc);
    }
    
    private void makePanels() {
	   
	   for (int i = 0; i < army.getTropas().size(); i++) {
		   InformaçãoTropaPanel panel = new InformaçãoTropaPanel(getNextColor());
		   panel.setBorder(new MatteBorder(0, 1, 0, 1, Cores.SEPARAR_ESCURO));
		   panelTropaList.add(panel);
	   }
	   
	   // Fixes the border of first and last panel
	   panelTropaList.get(0).setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));
	   panelTropaList.get(panelTropaList.size()-1).setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));
	   
	   armyInformationPanel = new InformaçãoTropaPanel(getNextColor());
	   armyInformationPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    }
    
    private JPanel dadosPrincipaisPanel() {
	   
	   JPanel panel = new JPanel();
	   panel.setOpaque(false);
	   panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	   
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   for (InformaçãoTropaPanel u : panelTropaList)
		   panel.add(u.getDadosPanel());
	   
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   panel.add(armyInformationPanel.getDadosPanel());
	   
	   return panel;
    }
    
    private JPanel dadosPrincipaisHeader() {
	   
    	JPanel header = new JPanel();
    	header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	header.setBackground(Cores.FUNDO_ESCURO);
    	header.setLayout(panelTropaList.get(0).getDadosPanel().getLayout());
	   
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = 0;
    	c.gridy = 0;
    	c.fill = GridBagConstraints.VERTICAL;
	   
    	header.add(new JLabel("Ataque"), c);
	   
    	c.gridx++;
    	header.add(makeSeparator(), c);
	   
    	c.gridx++;
    	header.add(new JLabel("Def. Geral"), c);
	   
    	c.gridx++;
    	header.add(makeSeparator(), c);
	   
    	c.gridx++;
    	header.add(new JLabel("Def. Cav."), c);
	   
    	c.gridx++;
    	header.add(makeSeparator(), c);
	   
    	c.gridx++;
    	header.add(new JLabel("Def. Arq."), c);
		   
    	c.gridx++;
    	header.add(makeSeparator(), c);
    	
    	c.gridx++;
    	header.add(new JLabel("Saque"), c);
   
    	return header;	   
	   
    }

    private JPanel dadosCustoPanel() {
	   
    	JPanel panel = new JPanel();
    	panel.setOpaque(false);
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	  
    	panel.add(dadosCustoHeader());
    	panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
    	for (InformaçãoTropaPanel u : panelTropaList)
    		panel.add(u.getCustoPanel());
    	
    	panel.add(Box.createRigidArea(new Dimension(0, 5)));
    	panel.add(dadosCustoHeader());
    	panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
    	panel.add(armyInformationPanel.getCustoPanel());
	   
    	return panel;
    }

    private JPanel dadosCustoHeader() {
	   
    	JPanel header = new JPanel();
    	header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	header.setBackground(Cores.FUNDO_ESCURO);
    	header.setLayout(panelTropaList.get(0).getCustoPanel().getLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = 0;
    	c.gridy = 0;
    	c.fill = GridBagConstraints.VERTICAL;
    	
    	header.add(new JLabel("Madeira"), c);
    	
    	c.gridx++;
    	header.add(makeSeparator(), c);
    	
    	c.gridx++;
    	header.add(new JLabel("Argila"), c);
    	
    	c.gridx++;
    	header.add(makeSeparator(), c);
    	
    	c.gridx++;
    	header.add(new JLabel("Ferro"), c);
    	
    	c.gridx++;
    	header.add(makeSeparator(), c);
    	
    	c.gridx++;
    	header.add(new JLabel("População"), c);
    	
    	return header;	   	
    }
   
    private JSeparator makeSeparator() {
    	
    	JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
    	separator.setForeground(Cores.SEPARAR_ESCURO);              
	                                                                  
    	return separator;                                              
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
