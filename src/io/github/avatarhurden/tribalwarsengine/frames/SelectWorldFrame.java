package io.github.avatarhurden.tribalwarsengine.frames;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.listeners.TWEWindowListener;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.panels.SelectWorldPanel;
import io.github.avatarhurden.tribalwarsengine.panels.WorldInfoPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.json.JSONObject;

import config.Lang;

public class SelectWorldFrame extends JFrame {

    public WorldInfoPanel informationTable;
    private SelectWorldPanel selectionPanel;

    private JPanel loadingPanel;
    private static final SelectWorldFrame instance = new SelectWorldFrame();

    private final int MAX_WIDTH = 1024;
    private final int MAX_HEIGHT = 700;
    /**
     * Frame inicial, no qual ocorre a escolha do mundo. Ele possui:
     * - Logo do programa
     * - Tabela com as informações do mundo selecionado
     * - Lista dos mundos disponíveis
     * - Botão para abrir o "MainWindow"
     */
    public SelectWorldFrame() {
    	
    	setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
    	
        getContentPane().setBackground(Cores.ALTERNAR_ESCURO);
        setTitle(Lang.Titulo.toString());

        addWindowListener(new TWEWindowListener());

        setIconImage(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/icon.png")));
        
        setGUI();

    }

    public static SelectWorldFrame getInstance() {
        return instance;
    }
    
    private void setGUI() {
    	GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{600, 1, 393};
        layout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 0;

        c.gridwidth = 3;
        c.insets = new Insets(10, 5, 10, 5);
        add(makeLogoLabel(), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 20, 5);
        add(makeLoadingPanel(), c);

        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 5, 5);
        add(makeAuthorPane(), c);

        pack();
        setResizable(false);

        JSONObject location = Configuration.get().getConfig("location", new JSONObject());
        setLocation(location.optInt("x", 0), location.optInt("y", 0));
    }

    /**
     * Cria um JLabel com a imagem do logo, e adiciona no frame
     *
     * @param c GridBagConstraints para adicionar
     */
    private JLabel makeLogoLabel() {

        JLabel lblTítulo = new JLabel("");

        /*
        * Irei criar um classe só pra carregar os recursos de forma estatica,
        * Assim, poderemos manter o projeto mais organizado e mover todos os pacotes para dentro da SRC
        */
        lblTítulo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/logo_engine_centralized.png"))));

       return lblTítulo;
    }
    
    private JPanel makeLoadingPanel() {
    	loadingPanel = new JPanel(new GridBagLayout());
    	loadingPanel.setPreferredSize(new Dimension(824, 370));
    	
    	loadingPanel.setBackground(Cores.FUNDO_CLARO);
        loadingPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        
        loadingPanel.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/loading.gif")))), c);
        
        c.gridy++;
    	loadingPanel.add(new JLabel("Carregando Mundos..."), c);
    	
    	return loadingPanel;
    }

    private JTextPane makeAuthorPane() {
    	JTextPane lblAuthor = new JTextPane();
    	lblAuthor.setEditable(false);
        lblAuthor.setOpaque(false);
        
        lblAuthor.setContentType("text/html");
        lblAuthor.setText(Lang.Criador.toString());
        
        return lblAuthor;
    }
    
    private JPanel makeSelectionPanel() {
    	JPanel panel = new JPanel();
        panel.setBackground(Cores.FUNDO_CLARO);
        panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{506, 1, 310};
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        
        // Tabela de informações
        informationTable = new WorldInfoPanel();
        informationTable.changeProperties();
        
        c.gridy = 1;
        c.insets = new Insets(25, 5, 25, 5);
        panel.add(informationTable, c);

        c.gridx = 1;
        c.insets = new Insets(25, 0, 25, 0);

        JSeparator test = new JSeparator(SwingConstants.VERTICAL);
        test.setForeground(Cores.SEPARAR_ESCURO);
        c.fill = GridBagConstraints.VERTICAL;
        panel.add(test, c);

        c.gridx = 2;
        c.insets = new Insets(25, 5, 25, 5);

        // Lista dos mundos com o botão para inciar
        selectionPanel = new SelectWorldPanel(this);
        
        panel.add(selectionPanel, c);
        
        return panel;
    }
    
    /**
     * Cria um JPanel com a tabela de informações do mundo, lista de mundos e
     * botão para iniciar e o adiciona no frame
     *
     * @param c GridBagConstraints para adicionar
     */
    public void addWorldPanel() {
    	JPanel worldPanel = makeSelectionPanel();

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 20, 5);
        remove(loadingPanel);
        add(worldPanel, c);
        
        updateWorldInfoPanel();

        addKeyListener();
        repaint();
        selectionPanel.getComboBox().requestFocus();
    }
    
    private void addKeyListener() {
    	
    	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    	
    	manager.addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_PRESSED)
					selectionPanel.getStartButton().doClick();
				return false;
			}
		});
    	
    }
    
    /**
     * Muda as informações da tabela, chamado toda vez que o mundo selecionado é
     * alterado
     */

    public void updateWorldInfoPanel() {
        informationTable.changeProperties();
    }

}
