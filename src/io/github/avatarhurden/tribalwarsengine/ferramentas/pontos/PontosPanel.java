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
    
    private List<InformaçõesEdifícioPanel> panelEdifícioList = new ArrayList<InformaçõesEdifícioPanel>();

    private InformaçõesEdifícioPanel buildingsInformationPanel;
    private JLabel populaçãoRestanteLabel;
    
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
    public PontosPanel() {
        super(Lang.FerramentaPontos.toString());
        
        onChange = new OnChange() {
			public void run() {
				buildingsEdit.saveValues();
				for (InformaçõesEdifícioPanel p : panelEdifícioList)
					p.changeValues(buildings.getBuildings().get(panelEdifícioList.indexOf(p)));
				buildingsInformationPanel.changeValues(buildings);
				setPopulaçãoRestante();
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
        add(populaçãoRestantePanel(), gbc);
        
    }
    
    private void makePanels() {
	   
	   for (int i = 0; i < buildings.getEdifícios().size(); i++) {
		   InformaçõesEdifícioPanel panel = new InformaçõesEdifícioPanel(getNextColor());
		   panel.setBorder(new MatteBorder(0, 1, 0, 1, Cores.SEPARAR_ESCURO));
		   panelEdifícioList.add(panel);
	   }
	   
	   // Fixes the border of first and last panel
	   panelEdifícioList.get(0).setBorder(new MatteBorder(1, 1, 0, 1, Cores.SEPARAR_ESCURO));
	   panelEdifícioList.get(panelEdifícioList.size()-1).setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));
	   
	   buildingsInformationPanel = new InformaçõesEdifícioPanel(getNextColor());
	   buildingsInformationPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    }
    
    private JPanel dadosPrincipaisPanel() {
	   
	   JPanel panel = new JPanel();
	   panel.setOpaque(false);
	   panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	   
	   panel.add(dadosPrincipaisHeader());
	   panel.add(Box.createRigidArea(new Dimension(0, 5)));
	   
	   for (InformaçõesEdifícioPanel u : panelEdifícioList)
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
    			panelEdifícioList.get(0).getDadosPanel().getLayout()).columnWidths;
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
    	header.add(new JLabel("População"), c);
    	
    	return header;	   
	   
    }
    
    private JPanel populaçãoRestantePanel() {
    	
    	JPanel panel = new JPanel();
 	   	panel.setOpaque(false);
 	   	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
 	   
 	   	panel.add(populaçãoRestanteHeader());
 	   	panel.add(Box.createRigidArea(new Dimension(0, 5)));
 	   	
 	   	panel.add(populaçãoRestanteLabel());
 	   
 	   	return panel;
    }
    
    private JPanel populaçãoRestanteLabel() {
    	
    	JPanel panel = new JPanel();
    	panel.setPreferredSize(new Dimension(131, 30));
    	panel.setBackground(buildingsInformationPanel.getDadosPanel().getBackground());
    	panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	panel.setLayout(new GridBagLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.insets = new Insets(5, 5, 5, 5);
    	
		populaçãoRestanteLabel = new JLabel();
		panel.add(populaçãoRestanteLabel, c);
		
		return panel;
    }
    
    private JPanel populaçãoRestanteHeader() {
    	
    	JPanel panel = new JPanel();
    	panel.setPreferredSize(new Dimension(131, 32));
    	panel.setBackground(Cores.FUNDO_ESCURO);
    	panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	panel.setLayout(new GridBagLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.insets = new Insets(5, 5, 5, 5);
    	
		panel.add(new JLabel("População Restante"), c);
    	
		return panel;
    }
    
    private JSeparator makeSeparator() {
    	
    	JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
    	separator.setForeground(Cores.SEPARAR_ESCURO);              
	                                                                  
    	return separator;                                              
    }
    
    private void setPopulaçãoRestante() {
    	
    	if (buildings.getPopulaçãoUsada() == 0)
    		populaçãoRestanteLabel.setText("");
    	else {
    		int text = buildings.getPopulaçãoRestante();
    		populaçãoRestanteLabel.setText(
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
