package main;

import config.Config_Gerais;
import config.File_Manager;
import frames.MainWindow;
import frames.TrayIconClass;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Tribal Wars Engine, uma ferramenta completa para o jogo Tribal Wars
 * 
 * @author Arthur
 * @date 25/07/2013
 *
 * Contribuições de Wesley Nascimento vulgo Sorriso
 */
public class Main {

	private static selecionar_mundo.GUI selecionar;
	private static MainWindow mainFrame;

    /* Informações para o updater */
    public static double VERSION = 1.101;
    private final String UPDATEFILE = "https://raw.githubusercontent.com/AvatarHurden/Tribal-Wars-Engine/master/last_update.json";


	public static void main(String[] args) {
        new Main().init();
	}

    public void init(){
        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN));
        Config_Gerais.read();
        File_Manager.read();
        File_Manager.defineMundos();
        new TrayIconClass();
        openSelection();

        lookForUpdates();
    }

	/**
	 * Cria e mostra o frame de seleção de mundo
	 */
	public void openSelection() {
		selecionar = new selecionar_mundo.GUI();

		selecionar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		selecionar.pack();
		selecionar.setVisible(true);
		selecionar.setResizable(false);
		
		selecionar.setLocationRelativeTo(null);
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

		mainFrame.setLocationRelativeTo(selecionar);
		selecionar.dispose();
	}

    /*
    * Cria um thread em paralelo para checar se existe alguma atualização
    *
    */
    public void lookForUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Recebe o JSON do servidor
                    JSONObject jsonUpdate = JSON.getJSON( new URL( UPDATEFILE ) );
                    double version = jsonUpdate.getDouble("version");

                    if ( version > VERSION) {
                        Updater updater = new Updater( version, jsonUpdate.getString("update_url") );
                        updater.start();
                    }
                }
                //Se ocorrer algum erro durante o Download.
                catch (IOException e) {
                    e.printStackTrace();
                }
                //Se ocorrer algum erro na leitura do JSON
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
	
	public static MainWindow getMainWindow() {
		return mainFrame;
	}

}
