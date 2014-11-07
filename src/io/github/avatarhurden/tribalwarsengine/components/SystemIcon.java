package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.main.Main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SystemIcon implements ActionListener {
    private Main main;
    private TrayIcon trayIcon;

    public SystemIcon(Main main) {
        this.main = main;

        trayIcon = getTrayIcon();

        trayIcon.setImageAutoSize(true);

        PopupMenu popup = new PopupMenu();

        MenuItem item = new MenuItem("Escolher Mundos");
        item.setActionCommand("select_world_frame");
        item.addActionListener(this);
        /*TODO for this to work, I need to be able to remove all the running alerts and whatnot.
         * For that to be possible, I need to redo how the system works (first try with threads).
         * So, only in the next version will this be possible.
         */
        //popup.add(item);

        item = new MenuItem("Janela principal");
        item.setActionCommand("main_window_frame");
        item.addActionListener(this);
        popup.add(item);

        item = new MenuItem("Procurar por atualizações");
        item.setActionCommand("look_for_updates");
        item.addActionListener(this);
        popup.add(item);

        item = new MenuItem("Sair");
        item.addActionListener(this);
        item.setActionCommand("exit");
        popup.add(item);

        trayIcon.setPopupMenu(popup);
        
        trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					Main.openMainFrame();
			}
		});
        
        try {
            SystemTray.getSystemTray().add(trayIcon);
        }
        //Se não conseguir criar o icone no sistema... Faz nada :(
        catch (AWTException e) {
        }
    }

    private TrayIcon getTrayIcon() {
        return new java.awt.TrayIcon(Imagens.getImage("Icon.png"));
    }
    
    public TrayIcon getIcon() {
    	return trayIcon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();
        switch (action) {
            case "select_world_frame":
                Main.openWorldSelection();
                break;
            case "main_window_frame":
                Main.openMainFrame();
                break;
            case "look_for_updates":
                main.lookForUpdate();
                break;
            case "exit":
            	Main.exitProgram();
        }
    }

}
