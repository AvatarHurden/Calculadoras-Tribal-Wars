package io.github.avatarhurden.tribalwarsengine.frames;

import config.File_Manager;
import io.github.avatarhurden.tribalwarsengine.main.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class TrayIconClass {

    final SystemTray tray = SystemTray.getSystemTray();

    public TrayIconClass() {

        try {
            final TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(
                    SelectWorldFrame.class.getResource("/images/Icon.png")));

            icon.setImageAutoSize(true);

            PopupMenu popup = new PopupMenu();

            MenuItem item = new MenuItem("Escolher Mundos");
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    //mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
                    File_Manager.save();
                    Main.getMainWindow().dispose();
                    tray.remove(icon);

                }
            });
            popup.add(item);
            item = new MenuItem("Sair");
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Main.getMainWindow().dispatchEvent(new WindowEvent(Main.getMainWindow(), WindowEvent.WINDOW_CLOSING));
                    tray.remove(icon);

                }
            });
            popup.add(item);

            icon.setPopupMenu(popup);

            icon.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Main.getMainWindow().setVisible(true);
                    Main.getMainWindow().toFront();
                    Main.getMainWindow().repaint();

                }
            });

            tray.add(icon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

}
