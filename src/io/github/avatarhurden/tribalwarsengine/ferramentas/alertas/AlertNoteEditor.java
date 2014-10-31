package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class AlertNoteEditor extends JDialog {

	private Alert alert;
	
	private JTextArea notas;
	private JButton saveButton, cancelButton;
	
	public AlertNoteEditor(Alert alert, Point point) {
	    this.alert = alert;  
		
	    setUndecorated(true);
	    setFocusable(true);
	    
	    JPanel panel = makeContentPanel();
	      		
	    JScrollPane scroll = new JScrollPane(panel);
	    scroll.setPreferredSize(new Dimension(scroll.getPreferredSize().width+16, scroll.getPreferredSize().height));
	    scroll.setOpaque(false);
	    
	  	add(scroll);
	    		
	  	pack();
	    		
	  	setVisible(true);
	  	toFront();
	  	
	  	setLocation(point.x - getPreferredSize().width/2, point.y - getPreferredSize().height/2);
	  	
	  	addWindowFocusListener(new WindowFocusListener() {
					
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
					
			public void windowGainedFocus(WindowEvent e) {}
	  	});
	     
	}
	
	private JPanel makeContentPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		panel.setOpaque(true);
		panel.setBackground(Cores.FUNDO_CLARO);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 118, 118 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(gridBagLayout);

		GridBagConstraints c = new GridBagConstraints();
  		c.insets = new Insets(5, 5, 5, 5);
  		c.gridx = 0;
  		c.gridy = 0;
  		
  		JLabel nameLabel = new JLabel("Notas");
		nameLabel.setBorder(new MatteBorder(0, 0, 1, 0, Cores.SEPARAR_CLARO));
		
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		panel.add(nameLabel, c);
		
		createNotas();
		
		c.gridy++;
		panel.add(notas, c);
		
		createSaveButton();
		
		c.gridwidth = 1;
		c.gridy++;
		c.anchor = GridBagConstraints.EAST;
		panel.add(saveButton, c);
		
		createCancelButton();
		
		c.gridx++;
		c.anchor = GridBagConstraints.WEST;
		panel.add(cancelButton, c);

  		return panel;
	}
	
	private void createNotas() {
		notas = new JTextArea(alert.getNotas(), 5, 20);
		notas.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		notas.setLineWrap(true);
		notas.setWrapStyleWord(true);
	}
	
	private void createSaveButton() {
		saveButton = new TWSimpleButton("Salvar");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alert.setNotas(notas.getText());
				dispose();
			}
		});
	}
	
	private void createCancelButton() {
		cancelButton = new TWSimpleButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	
}
