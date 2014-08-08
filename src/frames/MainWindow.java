package frames;

import config.Config_Gerais;
import config.File_Manager;
import config.Lang;
import custom_components.Ferramenta;
import database.Cores;
import selecionar_mundo.GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements WindowListener {

    private JPanel tabs;
    private JPanel body;

    //Configura tamanho fixo para todos os frames do APP
    //as dimensoes abaixo são as mesmas da GUI do selecionar_mundo
    private int MAX_WIDTH = 920;
    private int MAX_HEIGHT = 700;

    public List<Ferramenta> ferramentas = new ArrayList<Ferramenta>();
    public Ferramenta ferramentaSelecionada;

    //instancia da janela, para chamados estaticos
    private static final MainWindow instance = new MainWindow();
    ;

    public static MainWindow getInstance() {
        return instance;
    }

    /**
     * Frame que contém todas as ferramentas
     */
    public MainWindow() {

        Dimension dimension = new Dimension(MAX_WIDTH, MAX_HEIGHT);

        // Setting the visuals for the frame
        setTitle(Lang.Titulo.toString());
        setPreferredSize(dimension);
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                GUI.class.getResource("/images/Icon.png")));

        getContentPane().setBackground(Cores.ALTERNAR_ESCURO);
        setBackground(Cores.FUNDO_CLARO);

        addWindowListener(this);

        tabs = new JPanel();
        tabs.setBounds(0, 0, (int) dimension.getWidth(), 35);
        tabs.setBackground(Cores.FUNDO_CLARO);
        tabs.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 6));

        body = new JPanel();
        body.setBounds(0, 36, (int) dimension.getWidth(), (int) dimension.getHeight() - 36);
        //body.setLayout(new FlowLayout());
        body.setBackground(Cores.FUNDO_CLARO);
        body.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

        Container contentPane = getContentPane();
        contentPane.add(body);
        contentPane.add(tabs);

        // Adds listener for ctrl+tab funcionality
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_TAB && e.isControlDown())
                    // Only do this once per key press
                    if (e.getID() == KeyEvent.KEY_PRESSED) {

                        // If it is not the last tool, go to the next one. Otherwise, go to first
                        if (ferramentas.indexOf(ferramentaSelecionada) < ferramentas.size() - 1)
                            ferramentas.get(ferramentas.indexOf(ferramentaSelecionada) + 1).changeSelection();
                        else
                            ferramentas.get(0).changeSelection();

                    }

                if (e.isControlDown() && e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_1:
                            ferramentas.get(0).changeSelection();
                            break;
                        case KeyEvent.VK_2:
                            if (ferramentas.size() >= 2)
                                ferramentas.get(1).changeSelection();
                            break;
                        case KeyEvent.VK_3:
                            if (ferramentas.size() >= 3)
                                ferramentas.get(2).changeSelection();
                            break;
                        case KeyEvent.VK_4:
                            if (ferramentas.size() >= 4)
                                ferramentas.get(3).changeSelection();
                            break;
                        case KeyEvent.VK_5:
                            if (ferramentas.size() >= 5)
                                ferramentas.get(4).changeSelection();
                            break;
                        case KeyEvent.VK_6:
                            if (ferramentas.size() >= 6)
                                ferramentas.get(5).changeSelection();
                            break;
                        case KeyEvent.VK_7:
                            if (ferramentas.size() >= 7)
                                ferramentas.get(6).changeSelection();
                            break;
                        case KeyEvent.VK_8:
                            if (ferramentas.size() >= 8)
                                ferramentas.get(7).changeSelection();
                            break;
                        // On ctrl-9, go to last tool
                        case KeyEvent.VK_9:
                            ferramentas.get(ferramentas.size() - 1).changeSelection();
                            break;
                    }
                }


                return false;
            }
        });

    }

    /* IMPLEMENTAÇÔES AO WINDOW LISTENER */
    public void windowClosed(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        File_Manager.save();
        Config_Gerais.save();

        try {
            if (Config_Gerais.getOnClose() == true)
                System.exit(0);
            else
                ((JFrame) e.getSource()).dispose();
        } catch (Exception e1) {

            Object[] options = {"Fechar o programa", "Colocar em segundo plano"};

            JCheckBox check = new JCheckBox("Não me perguntar novamente");
            String mensagem = "<html>Você deseja fechar o programa ou apenas colocá-lo<br>em segundo "
                    + "plano?<br><br>(É possível fechá-lo com o ícone da barra de tarefas)<br></html>";

            int n = JOptionPane.showOptionDialog((Component) e.getSource(), new Object[]{mensagem, check},
                    "Encerrar programa?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (check.isSelected()) {
                Config_Gerais.setOnClose((n == 0) ? true : false);
                Config_Gerais.save();
            }

            if (n == 0) {
                System.exit(0);
            } else {
                ((JFrame) e.getSource()).dispose();
            }
        }

    }

    /**
     * Adiciona uma ferramenta nova ao frame, colocando a "tab" no local
     * adequado
     *
     * @param tool Ferramenta a ser adicionada
     */
    public void addPanel(Ferramenta tool) {

        tool.setFrame(this);

        ferramentas.add(tool);

        tabs.add(tool.getTab());

        body.add(tool);

    }

    /**
     * Seleciona a primeira ferramenta do frame
     */
    public void selectFirst() {

        for (Ferramenta i : ferramentas)
            i.setSelected(false);

        if (ferramentas.size() > 0) {
            ferramentas.get(0).setSelected(true);
            ferramentaSelecionada = ferramentas.get(0);
        }
    }

}
