package io.github.avatarhurden.tribalwarsengine.main;

import io.github.avatarhurden.tribalwarsengine.components.SystemIcon;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.objects.Army;
import io.github.avatarhurden.tribalwarsengine.objects.ArmyModel;
import io.github.avatarhurden.tribalwarsengine.objects.Buildings;
import io.github.avatarhurden.tribalwarsengine.objects.VillageModel;
import io.github.avatarhurden.tribalwarsengine.objects.World;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.EditPanelCreator;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import config.File_Manager;
import config.Mundo_Reader;
import database.Unidade;

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

    private static JFrame currentFrame;
    private SystemIcon trayicon;

    public static void main(String[] args) {
        new Main().init(args);
    }

    /**
     * Cria e mostra o frame de ferramentas, fechando o frame de seleção de
     * mundo e salvando todas as configurações de mundo na pasta de
     * configurações
     */
    public static void openMainFrame() {
    	
    	  LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
          
          map.put("name", "Nome");
          map.put("buildings", "");
          map.put("scope", "");
//          map.put("unit_modifier", "Modificador");
//          map.put("moral", "Moral");
//          map.put("researchsystem", "Sistema de Pesquisa");
//          map.put("church", "Igreja");
//          map.put("nightbonus", "Bônus Noturno");
//          map.put("flag", "Bandeiras");
//          map.put("archer", "Arqueiros");
//          map.put("paladin", "Paladino");
//          map.put("betteritems", "Itens Aprimorados");
//          map.put("militia", "Milícia");
//          map.put("coining", "Cunhagem de Moedas");
          
          JFrame frame = new JFrame();
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
          frame.add(new JScrollPane(new EditPanelCreator(new VillageModel().getJson(), 
          		map, new OnChange() {
  					public void run() {}
  				})));
          
          frame.pack();
          
    	
        File_Manager.defineModelos();
        mainFrame.packPanels(selectWorldFrame);
        selectWorldFrame.dispose();
        mainFrame.setVisible(true);
        currentFrame = mainFrame;
    }

    public void init(String[] args) {
        lookForUpdate();

        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN));

        File_Manager.read();
        File_Manager.defineMundos();
        Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundo(2));
        
        Army army = new Army();
        army.addTropa(Unidade.CATAPULTA, 1, 100);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONObject json = new JSONObject();
        json.put("army", new JSONObject(gson.toJson(army)));
        	
        try {
        	Army m = gson.fromJson(json.get("army").toString(), Army.class);
        	System.out.println(m.getQuantidade(Unidade.CATAPULTA));
			JSON.createJSONFile(json, new File("test.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        trayicon = new SystemIcon(this);
        mainFrame = MainWindow.getInstance();
        selectWorldFrame = SelectWorldFrame.getInstance();
        openWorldSelection();
    }

    /**
     * Cria e mostra o frame de seleção de mundo
     */
    public void openWorldSelection() {
        mainFrame.dispose();
        selectWorldFrame.setVisible(true);
        currentFrame = selectWorldFrame;
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


}
