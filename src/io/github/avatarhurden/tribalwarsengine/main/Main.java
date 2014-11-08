package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.components.SystemIcon;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.managers.AlertManager;
import io.github.avatarhurden.tribalwarsengine.panels.Initialization;

import java.awt.Font;
import java.awt.SystemTray;
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
 * Contribuições de Wesley Nascimento vulgo Sorriso
 */
public class Main {

    public static double VERSION = 1.200; //Versão atual do TWE
    private static SelectWorldFrame selectWorldFrame;
    private static MainWindow mainFrame;
    private static String REMOTE_URL = "https://raw.githubusercontent.com/AvatarHurden/Tribal-Wars-Engine/master/last_update.json";

    private static JFrame currentFrame;
    private static SystemIcon trayIcon;
    
    private static Initialization initializer;
    
    public static void main(String[] args) {
    	new Main().init(args);
    }

    public void init(String[] args) {
        lookForUpdate();
                
        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN));
        
        trayIcon = new SystemIcon(this);
        mainFrame = MainWindow.getInstance();
        selectWorldFrame = SelectWorldFrame.getInstance();
        selectWorldFrame.setVisible(true);
        currentFrame = selectWorldFrame;
        
        initializer = new Initialization();
        initializer.initializeProgram();
        
        selectWorldFrame.setInitializationPanel(initializer.getPanel());    
    }

    public static void loadWorld() {
    	initializer.initializeWorld();
    	selectWorldFrame.setInitializationPanel(initializer.getPanel());
    }
    
    /**
     * Cria e mostra o frame de seleção de mundo
     */
    public static void openWorldSelection() {
        mainFrame.dispose();
        selectWorldFrame.setVisible(true);
        currentFrame = selectWorldFrame;

        selectWorldFrame.addWorldPanel();
    }
    
    /**
     * Cria e mostra o frame de ferramentas, fechando o frame de seleção de
     * mundo e salvando todas as configurações de mundo na pasta de
     * configurações
     */
    public static void openMainFrame() {
        mainFrame.packPanels(selectWorldFrame);
        selectWorldFrame.dispose();
        mainFrame.setExtendedState(JFrame.NORMAL);
        mainFrame.setVisible(true);
        currentFrame = mainFrame;
    }

    /**
     * Cria uma thread paralela pra verificar se existe uma nova versão disponivel
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
                //Se não encontrar o arquivo
                catch (IOException e) {
                }
                //Se estiver algum erro no json
                catch (JSONException e) {
                }
                
            }
        }).start();
    }
    
    public static void exitProgram() {
    	
    	Configuration config = Configuration.get();
    	
    	try {
    		AlertManager.getInstance().save();
    	} catch (Exception e) {}
    	
    	JSONObject location = new JSONObject();
    	location.put("x", currentFrame.getLocationOnScreen().x);
    	location.put("y", currentFrame.getLocationOnScreen().y);
    	
    	config.setConfig("location", location);
    	
    	SystemTray.getSystemTray().remove(trayIcon.getIcon());
    	
    	System.exit(-1);
    }

    public static JFrame getCurrentFrame() {
    	return currentFrame;
    }
    
}
