package io.github.avatarhurden.tribalwarsengine.listeners;

import io.github.avatarhurden.tribalwarsengine.main.Configuration;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 * N�o sei como descrever esta classe... Mas voc� seber� pra que serve!
 *
 * @author WesleyNascimento
 */
public class TWEWindowListener implements WindowListener {

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
    	
//    	VillageModelManager.get().save();
//    	ArmyModelManager.get().save();

        Configuration config = Configuration.get();

        Window window = e.getWindow();
        
        if (config.getConfig("always_close", false)) {
            System.exit(-1);
        } else if (config.getConfig("always_dispose", false)) {
            window.dispose();
        }
        //Se nunca selecionou a op��o "N�o me perguntar novamente"
        else {
            Object[] options = {"Fechar o programa", "Colocar em segundo plano"};

            JCheckBox check = new JCheckBox("N�o me perguntar novamente");
            String mensagem = "<html>Voc� deseja fechar o programa ou apenas coloc�-lo<br>em segundo "
                    + "plano?<br><br>(� poss�vel fech�-lo com o �cone da barra de tarefas)<br></html>";

            int choose = JOptionPane.showOptionDialog((Component) e.getSource(), new Object[]{mensagem, check},
                    "Encerrar programa?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (check.isSelected()) {
                if (choose == 0) {
                    config.setConfig("always_close", true);
                } else {
                    config.setConfig("always_dispose", true);
                }
            }

            if (choose == 0) {
                System.exit(-1);
            } else {
                window.dispose();
            }
        }
    }
}
