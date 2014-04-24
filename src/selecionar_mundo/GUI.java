package selecionar_mundo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import config.Mundo_Reader;
import database.Cores;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	Informações_de_mundo informationTable;
	Escolha_de_mundo selectionPanel;

	JPanel panelMundo;

	/**
	 * Frame inicial, no qual ocorre a escolha do mundo. Ele possui:
	 * <br> - Logo do programa 
	 * <br>- Tabela com as informações do mundo selecionado 
	 * <br>- Lista dos mundos disponíveis 
	 * <br>- Botão para abrir o "MainWindow"
	 */
	public GUI() {

		getContentPane().setBackground(Cores.ALTERNAR_ESCURO);

		setIconImage(Toolkit.getDefaultToolkit().getImage(
				GUI.class.getResource("/images/test.png")));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 546, 1, 350 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		constraints.gridwidth = 3;
		addImage(constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(30, 5, 20, 5);
		addWorldPanel(constraints);

		JTextPane lblAuthor = new JTextPane();
		lblAuthor.setContentType("text/html");
		lblAuthor
				.setText("<html><div align=right width=100px>"
						+ "Criado por Arthur Vedana<br>agieselvedana@gmail.com</div></html>");
		lblAuthor.setEditable(false);
		lblAuthor.setOpaque(false);
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 5, 5, 5);
		add(lblAuthor, constraints);

		changeInformationPanel();
		
		getRootPane().setDefaultButton(selectionPanel.getStartButton());
	}

	/**
	 * Cria um JLabel com a imagem do logo, e adiciona no frame
	 * 
	 * @param c  GridBagConstraints para adicionar
	 */
	private void addImage(GridBagConstraints c) {

		JLabel lblTítulo = new JLabel("");

		// No ideia how or why this works, but do not remove "resources" folder
		// from src
		lblTítulo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				GUI.class.getResource("/images/Título.png"))));

		add(lblTítulo, c);

	}

	/**
	 * Cria um JPanel com a tabela de informações do mundo, lista de mundos e
	 * botão para iniciar e o adiciona no frame
	 * 
	 * @param c  GridBagConstraints para adicionar
	 */
	private void addWorldPanel(GridBagConstraints c) {

		panelMundo = new JPanel();

		panelMundo.setBackground(Cores.FUNDO_CLARO);

		panelMundo.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 506, 1, 310 };
		gridBagLayout.rowHeights = new int[] {};
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelMundo.setLayout(gridBagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;

		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(25, 5, 25, 5);

		// Tabela de informações
		informationTable = new Informações_de_mundo();
		
		// Makes the table have a size that will be used as the parameter for te=he size
		// of the selection panel
		informationTable.changeProperties(Mundo_Reader.getMundoList().get(0));
		informationTable.revalidate();

		panelMundo.add(informationTable, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = new Insets(25, 0, 25, 0);

		JSeparator test = new JSeparator(SwingConstants.VERTICAL);
		test.setForeground(Cores.SEPARAR_ESCURO);
		constraints.fill = GridBagConstraints.VERTICAL;
		panelMundo.add(test, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.insets = new Insets(25, 5, 25, 5);

		// Lista dos mundos com o botão para inciar
		selectionPanel = new Escolha_de_mundo(this);

		panelMundo.add(selectionPanel, constraints);

		add(panelMundo, c);

	}

	/**
	 * Muda as informações da tabela, chamado toda vez que o mundo selecionado é
	 * alterado
	 */
	public void changeInformationPanel() {

		informationTable.changeProperties(Mundo_Reader.getMundoList().get(selectionPanel
				.getSelectedIndex()));
		informationTable.revalidate();

	}
	
}
