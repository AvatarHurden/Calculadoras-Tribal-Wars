package selecionar_mundo;

import config.File_Manager;
import config.Lang;
import config.Mundo_Reader;
import custom_components.EditDialog;
import database.Mundo;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@SuppressWarnings("serial")
public class Escolha_de_mundo extends JPanel{

	private JComboBox<String> selectionBox;
	
	private JButton startButton;
	
	private JButton padr�oButton;
	
	private JButton editButton;
	
	private GUI gui;
	
	/**
	 * JPanel com um comboBox para escolher o mundo e um bot�o para iniciar o frame de ferramentas
	 * 
	 * @param gui Frame em que ser� inserido
	 */
	public Escolha_de_mundo (final GUI gui) {
		
		this.gui = gui;
		
		int height = gui.informationTable.getPreferredSize().height-80;
		
		setOpaque(false);
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 300 };
		layout.rowHeights = new int[] { height/2, 40, 40, height/2 };
		layout.columnWeights = new double[] { 0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		selectionBox = new JComboBox<String>();
		
		setSelectionBox();
		
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		add(selectionBox, constraints);
		
		padr�oButton = new JButton(Lang.BtnPadrao.toString());
		
		padr�oButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
				// Define o mundo padr�o a ser usado
				File_Manager.setMundoPadr�o(selectionBox.getSelectedItem().toString());
				
				changePadr�oButton();
			}
		});
		
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.SOUTHEAST;
		add(padr�oButton, constraints);
		
		editButton = new JButton(Lang.BtnEditar.toString());
		
		editButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					new EditDialog(Mundo.class, Mundo_Reader.getMundoList(),
							"variableList", getSelectedIndex(), null);
					
					selectionBox.removeItemListener(selectionBox.getItemListeners()[0]);
					selectionBox.removeAllItems();
					
					setSelectionBox();
					
					gui.changeInformationPanel();
					
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
					e.printStackTrace();
				}
				
				
			}
		});

		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		add(editButton, constraints);
		
		startButton = new JButton(Lang.BtnIniciar.toString());
		
		startButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				gui.setVisible(false);
				
				// Define as caracter�sticas da classe est�tica "MundoSelecionado", que ser�
				// utilizado por todas as ferramentas
				Mundo_Reader.setMundoSelecionado(
						Mundo_Reader.getMundoList().get(selectionBox.getSelectedIndex()));
				
				Main.openMainFrame();
			}
		});
		
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(startButton, constraints);
		
		changePadr�oButton();
		
		editButton.setFocusable(false);
		selectionBox.setFocusable(false);
		selectionBox.setFocusable(true);
		
		startButton.requestFocusInWindow();

	}
	
	private void setSelectionBox() {
		
		// Adiciona o nome de todos os mundos para a lista que ser� utilizada
		// no comboBox
		for (Mundo m : Mundo_Reader.getMundoList())
			selectionBox.addItem(m.toString());
				
		selectionBox.setSelectedItem(File_Manager.getMundoPadr�o());
			
		selectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
						
				// Cada vez que o mundo selecionado � alterado, altera as informa��es da tabela
				gui.changeInformationPanel();
						
				changePadr�oButton();
					
			}
		});
		
	}
	
	private void changePadr�oButton() {
		
		if (selectionBox.getSelectedItem() != null && selectionBox.getSelectedItem().toString()
				.equals(File_Manager.getMundoPadr�o()))
			padr�oButton.setEnabled(false);
		else
			padr�oButton.setEnabled(true);
		
	}
	
	/**
	 * @return The selected index of the comboBox
	 */
	public int getSelectedIndex() {
		
		return selectionBox.getSelectedIndex();
		
	}	
	
	public JButton getStartButton() {
		return startButton;
	}
}
