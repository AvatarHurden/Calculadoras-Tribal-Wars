package io.github.avatarhurden.tribalwarsengine.ferramentas.recrutamento;

import io.github.avatarhurden.tribalwarsengine.objects.building.BuildingBlock;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Lang;
import database.Cores;
import database.Edif�cio;
import database.Unidade;

@SuppressWarnings("serial")
public class RecrutamentoPanel extends Ferramenta {
	
	private List<RecruitmentPanel> panels;
	private OnChange onChange;
	
	/**
	 * Ferramenta para calcular o tempo necess�rio de produ��o de unidades. 
	 * <br>Ela mostra: 
	 * <br>- tempo de produ��o unit�ria de cada unidade 
	 * <br>- tempo de produ��o total para cada unidade 
	 * <br>- tempo de produ��o total para cada edif�cio
	 */
	public RecrutamentoPanel() {

		super(Lang.FerramentaRecruta.toString());

		setOpaque(true);
		
		onChange = new OnChange() {
			public void run() {
				for (RecruitmentPanel panel : panels)
					panel.changeValues();
			}
		};
		
		createRecruitmentPanels();
		
		makeGUI();
		
		onChange.run();
	}
	
	private void makeGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 125, 100, 100, 100 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 100, 100, 50, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		c.anchor = GridBagConstraints.EAST;
		c.gridx++;
		add(getModeloTropasPanel(), c);
		
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy++;
		add(makeHeader(), c);
		
		c.insets = new Insets(0, 5, 5, 5);
		for (RecruitmentPanel panel : panels) {
			c.gridy++;
			add(panel, c);
		}
	}
	
	private void createRecruitmentPanels() {
		
		panels = new ArrayList<RecruitmentPanel>();
		
		List<Unidade> barrackList = new ArrayList<Unidade>();
		barrackList.add(Unidade.LANCEIRO);
		barrackList.add(Unidade.ESPADACHIM);
		barrackList.add(Unidade.B�RBARO);
		if (Army.getAvailableUnits().contains(Unidade.ARQUEIRO))
			barrackList.add(Unidade.ARQUEIRO);
		
		panels.add(new RecruitmentPanel(onChange, new BuildingBlock(Edif�cio.QUARTEL), 
				new Army(barrackList)));
		
		List<Unidade> stableList = new ArrayList<Unidade>();
		stableList.add(Unidade.EXPLORADOR);
		stableList.add(Unidade.CAVALOLEVE);
		if (Army.getAvailableUnits().contains(Unidade.ARQUEIRO))
			stableList.add(Unidade.ARCOCAVALO);
		stableList.add(Unidade.CAVALOPESADO);
		
		panels.add(new RecruitmentPanel(onChange, new BuildingBlock(Edif�cio.EST�BULO), 
				new Army(stableList)));
		
		panels.add(new RecruitmentPanel(onChange, new BuildingBlock(Edif�cio.OFICINA), 
				new Army(Unidade.AR�ETE, Unidade.CATAPULTA)));
		
		if (Army.getAvailableUnits().contains(Unidade.PALADINO))
			panels.add(new RecruitmentPanel(onChange, new BuildingBlock(Edif�cio.EST�TUA), 
				new Army(Unidade.PALADINO)));
		
		if (BuildingBlock.getAvailableBuildings().contains(Edif�cio.ACADEMIA_1N�VEL))
			panels.add(new RecruitmentPanel(onChange, new BuildingBlock(Edif�cio.ACADEMIA_1N�VEL), 
					new Army(Unidade.NOBRE)));
		else
			panels.add(new RecruitmentPanel(onChange, new BuildingBlock(Edif�cio.ACADEMIA_3N�VEIS), 
					new Army(Unidade.NOBRE)));
		
	}

	private JPanel getModeloTropasPanel() {
		
		ArmyEditPanel[] editPanels = new ArmyEditPanel[panels.size()];
		
		for (int i = 0; i < editPanels.length; i++)
			editPanels[i] = panels.get(i).getArmyEditPanel();
				
		return tools.addModelosTropasPanel(true, editPanels);

	}
	
    private JPanel makeHeader() {
    	JPanel header = new JPanel();
    	header.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
    	header.setBackground(Cores.FUNDO_ESCURO);

    	GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 125, 100, 100, 100 };
		layout.rowHeights = new int[] { 30 };
		header.setLayout(layout);
		
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = 0;
    	c.gridy = 0;
    	
    	header.add(new JLabel("Unidade"), c);
    	
    	c.gridx++;
    	header.add(new JLabel("Quantidade"), c);
    	
    	c.gridx++;
    	header.add(new JLabel("Tempo Unit�rio"), c);

    	c.gridx++;
    	header.add(new JLabel("Tempo Total"), c);
	   
    	return header;	   
    }
	
	private ActionListener getResetButtonAction() {
    	
    	ActionListener action = new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			for (RecruitmentPanel panel : panels)
    				panel.getArmyEditPanel().resetComponents();
            }
        };

        return action;
    }
}
