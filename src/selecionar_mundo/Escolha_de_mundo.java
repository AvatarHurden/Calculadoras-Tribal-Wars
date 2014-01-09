package selecionar_mundo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import main.Main;
import config.File_Manager;
import config.Mundo_Reader;

@SuppressWarnings("serial")
public class Escolha_de_mundo extends JPanel{

	private JComboBox<String> selectionBox;
	
	private JButton startButton;
	
	private JButton padrãoButton;
	
	/**
	 * JPanel com um comboBox para escolher o mundo e um botão para iniciar o frame de ferramentas
	 * 
	 * @param gui Frame em que será inserido
	 */
	public Escolha_de_mundo(final GUI gui) {
		
		setOpaque(false);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		String[] nomeMundos = new String[Mundo_Reader.getMundoList().size()];
	
		// Adiciona o nome de todos os mundos para a lista que será utilizada
		// no comboBox
		for (int i = 0; i < Mundo_Reader.getMundoList().size(); i++)
			nomeMundos[i] = Mundo_Reader.getMundo(i).getNome();
		
		selectionBox = new JComboBox<String>(nomeMundos);
		
		selectionBox.setSelectedItem(File_Manager.getMundoPadrão());
		
		selectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				
				// Cada vez que o mundo selecionado é alterado, altera as informações da tabela
				gui.changeInformationPanel();
				
				changePadrãoButton();
				
			}
		});
		
		add(selectionBox, constraints);
		
		padrãoButton = new JButton("Padrão");
		
		padrãoButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
				// Define o mundo padrão a ser usado
				File_Manager.setMundoPadrão(selectionBox.getSelectedItem().toString());
				
				changePadrãoButton();
			}
		});
		
		constraints.gridx = 1;
		add(padrãoButton, constraints);
		
		startButton = new JButton("Iniciar");
		
		startButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
				// Define as características da classe estática "MundoSelecionado", que será
				// utilizado por todas as ferramentas
				Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundoList().get(selectionBox.getSelectedIndex()));
				
				Main.openMainFrame();
				
			}
		});
		
		constraints.gridy = 1;
		constraints.gridx = 0;
		add(startButton, constraints);
		
		changePadrãoButton();
		
	}
	
	private void changePadrãoButton() {
		
		if (selectionBox.getSelectedItem().toString()
				.equals(File_Manager.getMundoPadrão()))
			padrãoButton.setEnabled(false);
		else
			padrãoButton.setEnabled(true);
		
	}
	
	/**
	 * @return The selected index of the comboBox
	 */
	public int getSelectedIndex() {
		
		return selectionBox.getSelectedIndex();
		
	}	
}
