package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertasPanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.assistente_saque.AssistenteSaquePanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.dados_de_unidade.DadosDeUnidadePanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.distância.DistânciaPanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.oponentes_derrotados.OponentesDerrotadosPanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.pontos.PontosPanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.recrutamento.RecrutamentoPanel;
import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.frames.TrayIconClass;

import java.awt.Font;
import java.io.IOException;
import java.net.URL;

import javax.swing.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import config.File_Manager;

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
        mainFrame.addPanel(new RecrutamentoPanel());
        mainFrame.addPanel(new DadosDeUnidadePanel());
        mainFrame.addPanel(new PontosPanel());
        mainFrame.addPanel(new DistânciaPanel());
        mainFrame.addPanel(new OponentesDerrotadosPanel());
        mainFrame.addPanel(new SimuladorPanel());
        mainFrame.addPanel(new AssistenteSaquePanel());
        mainFrame.addPanel(new AlertasPanel());

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
