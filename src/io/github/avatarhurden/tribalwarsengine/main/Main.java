package io.github.avatarhurden.tribalwarsengine.main;

import config.Config_Gerais;
import config.File_Manager;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.frames.TrayIconClass;
import org.json.JSONException;
import org.json.JSONObject;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Tribal Wars Engine, uma ferramenta completa para o jogo Tribal Wars
 *
 * @author Arthur
 * @date 25/07/2013
 * <p/>
 * Contribuições de Wesley Nascimento vulgo Sorriso
 */
public class Main {

    public static double VERSION = 1.101; //Versão atual do TWE
    private static SelectWorldFrame selectWorldFrame;
    private static MainWindow mainFrame;
    private static String REMOTE_URL = "https://raw.githubusercontent.com/AvatarHurden/Tribal-Wars-Engine/master/last_update.json";

    public static void main(String[] args) {
        new Main().init();
    }

    /**
     * Cria e mostra o frame de ferramentas, fechando o frame de seleção de
     * mundo e salvando todas as configurações de mundo na pasta de
     * configurações
     */
    public static void openMainFrame() {
        File_Manager.defineModelos();

        mainFrame = MainWindow.getInstance();

        // Adicionando todas as ferramentas criadas
        mainFrame.addPanel(new recrutamento.GUI());
        mainFrame.addPanel(new dados_de_unidade.GUI());
        mainFrame.addPanel(new pontos.GUI());
        mainFrame.addPanel(new distância.GUI());
        mainFrame.addPanel(new oponentes_derrotados.GUI());
        mainFrame.addPanel(new simulador.GUI());
        mainFrame.addPanel(new assistente_saque.GUI());
        mainFrame.addPanel(new alertas.GUI());

        mainFrame.selectFirst();

        mainFrame.pack();
        mainFrame.setResizable(false);

        mainFrame.setLocationRelativeTo( selectWorldFrame );
        selectWorldFrame.dispose();
        mainFrame.setVisible(true);
    }

    /*
     * @return mainFrame
     */
    public static MainWindow getMainWindow() {
        return mainFrame;
    }

    public void init() {
        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN));
        Config_Gerais.read();
        File_Manager.read();
        File_Manager.defineMundos();
        new TrayIconClass();
        openWorldSelection();
        //Inicia as configurações
        Configuration.get();

        lookForUpdate();
    }

    /**
     * Cria e mostra o frame de seleção de mundo
     */
    public void openWorldSelection() {
        selectWorldFrame = new SelectWorldFrame();
        selectWorldFrame.pack();
        selectWorldFrame.setVisible(true);
        selectWorldFrame.setResizable(false);
        selectWorldFrame.setLocationRelativeTo(null);
    }

    /*
     * Cria uma thread paralela pra verificar se existe uma nova versão disponivel
     *
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

}
