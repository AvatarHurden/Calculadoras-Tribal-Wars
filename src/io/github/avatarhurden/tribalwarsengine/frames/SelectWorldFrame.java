package io.github.avatarhurden.tribalwarsengine.frames;

import io.github.avatarhurden.tribalwarsengine.listeners.TWEWindowListener;
import io.github.avatarhurden.tribalwarsengine.panels.SelectWorldPanel;
import io.github.avatarhurden.tribalwarsengine.panels.WorldInfoPanel;

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

import config.Lang;
import database.Cores;

public class SelectWorldFrame extends JFrame {

    public WorldInfoPanel informationTable;
    private SelectWorldPanel selectionPanel;

    private JPanel panelMundo;
    private static final SelectWorldFrame instance = new SelectWorldFrame();

    /**
     * Frame inicial, no qual ocorre a escolha do mundo. Ele possui:
     * - Logo do programa
     * - Tabela com as informações do mundo selecionado
     * - Lista dos mundos disponíveis
     * - Botão para abrir o "MainWindow"
     */
    public SelectWorldFrame() {
        getContentPane().setBackground(Cores.ALTERNAR_ESCURO);

        setTitle(Lang.Titulo.toString());

        addWindowListener(new TWEWindowListener());

        setIconImage(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/Icon.png")));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{546, 1, 350};
        gridBagLayout.rowHeights = new int[]{0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;
        constraints.gridy = 0;

        constraints.gridwidth = 3;
        constraints.insets = new Insets(10, 5, 10, 5);
        addImage(constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 5, 20, 5);
        addWorldPanel(constraints);

        JTextPane lblAuthor = new JTextPane();
        lblAuthor.setContentType("text/html");
        lblAuthor.setText(Lang.Criador.toString());
        lblAuthor.setEditable(false);
        lblAuthor.setOpaque(false);
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 5, 5, 5);
        add(lblAuthor, constraints);

        updateWorldInfoPanel();

        getRootPane().setDefaultButton(selectionPanel.getStartButton());

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static SelectWorldFrame getInstance() {
        return instance;
    }

    /**
     * Cria um JLabel com a imagem do logo, e adiciona no frame
     *
     * @param c GridBagConstraints para adicionar
     */
    private void addImage(GridBagConstraints c) {

        JLabel lblTítulo = new JLabel("");

        // No ideia how or why this works, but do not remove "resources" folder
        // from src

        /*
        * Irei criar um classe só pra carregar os recursos de forma estatica,
        * Assim, poderemos manter o projeto mais organizado e mover todos os pacotes para dentro da SRC
        */
        lblTítulo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/logo_engine_centralized.png"))));

        add(lblTítulo, c);

    }

    /**
     * Cria um JPanel com a tabela de informações do mundo, lista de mundos e
     * botão para iniciar e o adiciona no frame
     *
     * @param c GridBagConstraints para adicionar
     */
    private void addWorldPanel(GridBagConstraints c) {

        panelMundo = new JPanel();

        panelMundo.setBackground(Cores.FUNDO_CLARO);

        panelMundo.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{506, 1, 310};
        gridBagLayout.rowHeights = new int[]{};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panelMundo.setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(25, 5, 25, 5);

        // Tabela de informações
        informationTable = new WorldInfoPanel();
        informationTable.changeProperties();

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
        selectionPanel = new SelectWorldPanel(this);

        panelMundo.add(selectionPanel, constraints);

        add(panelMundo, c);

    }

    /**
     * Muda as informações da tabela, chamado toda vez que o mundo selecionado é
     * alterado
     */

    public void updateWorldInfoPanel() {
        informationTable.changeProperties();
    }

}
