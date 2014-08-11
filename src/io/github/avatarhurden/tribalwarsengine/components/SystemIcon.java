package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.main.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemIcon implements ActionListener {
    private Main main;
    private SystemTray systemTray = SystemTray.getSystemTray();

    public SystemIcon(Main main) {
        this.main = main;

        TrayIcon trayIcon = getTrayIcon();

        trayIcon.setImageAutoSize(true);

        PopupMenu popup = new PopupMenu();

        MenuItem item = new MenuItem("Escolher Mundos");
        item.setActionCommand("select_world_frame");
        item.addActionListener(this);
        popup.add(item);
        /*
        item = new MenuItem("Janela principal");
        item.setActionCommand("main_window_frame");
        item.addActionListener(this);
        popup.add(item);
        */

        item = new MenuItem("Procurar por atualizações.");
        item.setActionCommand("look_for_updates");
        item.addActionListener(this);
        popup.add(item);

        item = new MenuItem("Sair");
        item.addActionListener(this);
        item.setActionCommand("exit");
        popup.add(item);

        trayIcon.setPopupMenu(popup);

        try {
            systemTray.add(trayIcon);
        }
        //Se não conseguir criar o icone no sistema... Faz nada :(
        catch (AWTException e) {
        }
    }

    private TrayIcon getTrayIcon() {
        return new java.awt.TrayIcon(
                Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/Icon.png"))
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();
        switch (action) {
            case "select_world_frame":
                main.openWorldSelection();
                break;
            case "main_window_frame":
                main.openMainFrame();
                break;
            case "look_for_updates":
                main.lookForUpdate();
                break;
            case "exit":
                System.exit(-1);
        }
    }

}
