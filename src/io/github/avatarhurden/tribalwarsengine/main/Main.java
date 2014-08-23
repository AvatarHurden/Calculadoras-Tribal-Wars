package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.components.SystemIcon;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;

import java.awt.Font;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tribal Wars Engine, uma ferramenta completa para o jogo Tribal Wars
 *
 * @author Arthur
 * @date 25/07/2013
 * <p/>
 * Contribui��es de Wesley Nascimento vulgo Sorriso
 */
public class Main {

    public static double VERSION = 1.101; //Vers�o atual do TWE
    private static SelectWorldFrame selectWorldFrame;
    private static MainWindow mainFrame;
    private static String REMOTE_URL = "https://raw.githubusercontent.com/AvatarHurden/Tribal-Wars-Engine/master/last_update.json";

    private static JFrame currentFrame;
    private SystemIcon trayicon;
    
    public static void main(String[] args) {
    	new Main().init(args);
    }

    /**
     * Cria e mostra o frame de ferramentas, fechando o frame de sele��o de
     * mundo e salvando todas as configura��es de mundo na pasta de
     * configura��es
     */
    public static void openMainFrame() {
    	
        mainFrame.packPanels(selectWorldFrame);
        selectWorldFrame.dispose();
        mainFrame.setVisible(true);
        currentFrame = mainFrame;
    }

    public void init(String[] args) {
        lookForUpdate();

        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN));

//        File_Manager.read();
//        File_Manager.defineMundos();
        
        trayicon = new SystemIcon(this);
        mainFrame = MainWindow.getInstance();
        selectWorldFrame = SelectWorldFrame.getInstance();
        openWorldSelection();
    }

    /**
     * Cria e mostra o frame de sele��o de mundo
     */
    public void openWorldSelection() {
        mainFrame.dispose();
        selectWorldFrame.setVisible(true);
        currentFrame = selectWorldFrame;
    }

    /**
     * Cria uma thread paralela pra verificar se existe uma nova vers�o disponivel
     */
    public void lookForUpdate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonUpdate = JSON.getJSON(new URL(REMOTE_URL));

                    double version = jsonUpdate.getDouble("version");

                    if (version > VERSION) {
                        Updater updater = new Updater(version, jsonUpdate.getString("update_url"));
                        updater.start();
                    }
                }
                //Se n�o encontrar o arquivo
                catch (IOException e) {
                }
                //Se estiver algum erro no json
                catch (JSONException e) {
                }
            }
        }).start();
    }


}
