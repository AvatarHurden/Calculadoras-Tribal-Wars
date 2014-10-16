package io.github.avatarhurden.tribalwarsengine.panels;

import io.github.avatarhurden.tribalwarsengine.components.ProgressStatus;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.main.Main;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * Class that initializes the program, loading all necessary files and downloading
 * the ones that are not in the system.
 * 
 * @author Arthur
 *
 */
public class Initialization {

	private ProgressStatus progressBar;
	private JPanel panel;
	
	public Initialization() {
		progressBar = new ProgressStatus();
		
		makePanel();
	}
	
	private void makePanel() {
		panel = new JPanel(new GridBagLayout());
    	panel.setPreferredSize(new Dimension(824, 370));
    	
    	panel.setBackground(Cores.FUNDO_CLARO);
        panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        
        panel.add(new JLabel(new ImageIcon(Imagens.getImage("loading.gif"))), c);
        
        progressBar.setBackground(Cores.FUNDO_CLARO);
        
        c.gridy++;
        panel.add(progressBar, c);
	}
	
	public void initializeProgram() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(10);
				progressBar.setMessage("Carregando Configurações");
				Configuration.get();
				
				progressBar.setProgress(30);
				progressBar.setSubProgressEnd(80);
				progressBar.setMessage("Carregando Mundos");
				WorldManager.initialize(progressBar);
				
				progressBar.setProgress(100);
				progressBar.setMessage("Criando Interface");
				Main.openWorldSelection();
			}
		}).start();
	}
	
	public void initializeWorld() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(50);
				progressBar.setMessage("Carregando Mundo");
				progressBar.setSubProgressEnd(80);
				WorldManager.getSelectedWorld().setInfo(progressBar);
				
				progressBar.setProgress(90);
				progressBar.setMessage("Carregando Alertas");
				AlertManager.initialize();

				progressBar.setProgress(100);
				progressBar.setMessage("Criando Interface");
				Main.openMainFrame();
			}
		}).start();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
}
