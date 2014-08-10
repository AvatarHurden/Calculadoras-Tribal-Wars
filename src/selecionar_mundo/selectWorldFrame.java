package selecionar_mundo;

import io.github.avatarhurden.tribalwarsengine.listeners.TWEWindowListener;

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
import config.Mundo_Reader;
import database.Cores;

@SuppressWarnings("serial")
public class selectWorldFrame extends JFrame {

    public Informações_de_mundo informationTable;
    private Escolha_de_mundo selectionPanel;

    private JPanel panelMundo;

    /**
     * Frame inicial, no qual ocorre a escolha do mundo. Ele possui:
     * - Logo do programa
     * - Tabela com as informações do mundo selecionado
     * - Lista dos mundos disponíveis
     * - Botão para abrir o "MainWindow"
     */
    public selectWorldFrame() {
        getContentPane().setBackground(Cores.ALTERNAR_ESCURO);

        setTitle(Lang.Titulo.toString());

        addWindowListener(new TWEWindowListener());

        setIconImage(Toolkit.getDefaultToolkit().getImage(
                selectWorldFrame.class.getResource("/images/Icon.png")));

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

        changeInformationPanel();

        getRootPane().setDefaultButton(selectionPanel.getStartButton());

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
                selectWorldFrame.class.getResource("/images/logo_engine_centralized.png"))));

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
        informationTable = new Informações_de_mundo();

        // Makes the table have a size that will be used as the parameter for te=he size
        // of the selection panel
        if (Mundo_Reader.getMundoList().size() > 0) {
            informationTable.changeProperties(Mundo_Reader.getMundoList().get(0));
            informationTable.revalidate();
        }

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

        if (selectionPanel.getSelectedIndex() > -1) {
            informationTable.changeProperties(Mundo_Reader.getMundoList().get(selectionPanel.getSelectedIndex()));
            informationTable.revalidate();
        }
    }

}
