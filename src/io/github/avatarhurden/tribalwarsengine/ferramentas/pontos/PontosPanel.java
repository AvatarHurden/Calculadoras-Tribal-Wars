package io.github.avatarhurden.tribalwarsengine.ferramentas.pontos;

import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock;
import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock.BuildingsEditPanel;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

public class PontosPanel extends Ferramenta {

    private BuildingBlock buildings;
    private BuildingsEditPanel buildingsEdit;
    
    private List<Informa��esEdif�cioPanel> panelEdif�cioList = new ArrayList<Informa��esEdif�cioPanel>();

    private Informa��esEdif�cioPanel buildingsInformationPanel;
    private JLabel popula��oRestanteLabel;
    
    private OnChange onChange;
    
    /**
     * Ferramenta com informa��es de unidades. Possui:
     * <br>- Ataque
     * <br>- Defesas
     * <br>- Saque
     * <br>- Custo de recursos para produ��o
     * <br>- Uso de popula��o
     * <p/>
     * Em caso de mundo com n�veis, � poss�vel escolher o n�vel das unidades
     * (n�o � limitado a 15 n�veis)
     * <p/>
     * N�o consigo entender a gambiarra ( Os recursos tecnicos avan�ados ) que voc� usou nesse jPanel... Se puder ajeitar eu agradeceria :)
     */
    public PontosPanel() {
        super(Lang.FerramentaPontos.toString());
        
        onChange = new OnChange() {
			public void run() {
				buildingsEdit.saveValues();
				for (Informa��esEdif�cioPanel p : panelEdif�cioList)
					p.changeValues(buildings.getBuildings().get(panelEdif�cioList.indexOf(p)));
				buildingsInformationPanel.changeValues(buildings);
				setPopula��oRestante();
			}
		};
		
        buildings = new BuildingBlock(BuildingBlock.getAvailableBuildings());
        buildingsEdit = buildings.getEditPanelFull(onChange);
        
        makePanels();
    }
    
    protected void makeGUI() {
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
        
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.EAST;
        add(tools.addModelosAldeiasPanel(true, buildingsEdit), gbc);
        
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy++;
        gbc.gridx = 0;
        add(buildingsEdit, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx += 2;
        add(dadosPrincipaisPanel(), gbc);
        
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.gridx++;
        add(popula��oRestantePanel(), gbc);
        
    }
    
    private void makePanels() {
	   
	   for (int i = 0; i < buildings.getEdif�cios().size(); i++) {
		   Informa��esEdif�cioPanel panel = new Informa��esEdif�cioPanel(getNextColor());
		   panel.setBorder(new MatteBorder(0, 1, 0, 1, Cores.SEPARAR_ESCURO));
		   panelEdif�cioList.add(panel);
	   }
	   
	   // Fixes the border of first and last panel
	   panelEdif�cioList.get(0).setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));
	   panelEdif�cioList.get(panelEdif�cioList.size()-1).setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));
	   
	   buildingsInformationPanel = new Informa��esEdif�cioPanel(getNextColor());
	   buildingsInformationPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    }
    
    private JPanel dadosPrincipaisPanel() {
	   
	   JPanel panel = new JPanel();
	   panel.setOpaque(false);
	   panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	   
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   for (Informa��esEdif�cioPanel u : panelEdif�cioList)
		   panel.add(u.getDadosPanel());
	   
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   panel.add(buildingsInformationPanel.getDadosPanel());
	   
	   return panel;
    }
    
    private JPanel dadosPrincipaisHeader() {
	   
    	JPanel header = new JPanel();
    	header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	header.setBackground(Cores.FUNDO_ESCURO);
    	
    	GridBagLayout layout = new GridBagLayout();
    	layout.columnWidths = ((GridBagLayout)
    			panelEdif�cioList.get(0).getDadosPanel().getLayout()).columnWidths;
    	layout.rowHeights = new int[] { 30 };
    	header.setLayout(layout);
 	   
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = 0;
    	c.gridy = 0;
    	c.fill = GridBagConstraints.VERTICAL;
	   
    	header.add(new JLabel("Pontos"), c);
	   
    	c.gridx++;
    	header.add(makeSeparator(), c);
	   
    	c.gridx++;
    	header.add(new JLabel("Popula��o"), c);
    	
    	return header;	   
	   
    }
    
    private JPanel popula��oRestantePanel() {
    	
    	JPanel panel = new JPanel();
 	   	panel.setOpaque(false);
 	   	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
 	   
 	   	panel.add(popula��oRestanteHeader());
 	   	panel.add(Box.createRigidArea(new Dimension(0, 5)));
 	   	
 	   	panel.add(popula��oRestanteLabel());
 	   
 	   	return panel;
    }
    
    private JPanel popula��oRestanteLabel() {
    	
    	JPanel panel = new JPanel();
    	panel.setPreferredSize(new Dimension(131, 30));
    	panel.setBackground(buildingsInformationPanel.getDadosPanel().getBackground());
    	panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	panel.setLayout(new GridBagLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.insets = new Insets(5, 5, 5, 5);
    	
		popula��oRestanteLabel = new JLabel();
		panel.add(popula��oRestanteLabel, c);
		
		return panel;
    }
    
    private JPanel popula��oRestanteHeader() {
    	
    	JPanel panel = new JPanel();
    	panel.setPreferredSize(new Dimension(131, 32));
    	panel.setBackground(Cores.FUNDO_ESCURO);
    	panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	panel.setLayout(new GridBagLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.insets = new Insets(5, 5, 5, 5);
    	
		panel.add(new JLabel("Popula��o Restante"), c);
    	
		return panel;
    }
    
    private JSeparator makeSeparator() {
    	
    	JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
    	separator.setForeground(Cores.SEPARAR_ESCURO);              
	                                                                  
    	return separator;                                              
    }
    
    private void setPopula��oRestante() {
    	
    	if (buildings.getPopula��oUsada() == 0)
    		popula��oRestanteLabel.setText("");
    	else {
    		int text = buildings.getPopula��oRestante();
    		popula��oRestanteLabel.setText(
    				NumberFormat.getNumberInstance(Locale.GERMANY).format(text));
    	}
    	
    }
 
    private ActionListener getResetButtonAction() {
    	
    	ActionListener action = new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {	
            	buildingsEdit.resetComponents();
            }
        };

        return action;
    }

}
