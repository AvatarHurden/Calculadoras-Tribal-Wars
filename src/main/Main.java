package main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import selecionar_mundo.GUI;
import config.File_Manager;
import frames.MainWindow;

/**
 * Tribal Wars Engine, uma ferramenta completa para o jogo Tribal Wars
 * 
 * @author Arthur
 * @date 25/07/2013
 */
public class Main {

	static selecionar_mundo.GUI selecionar;
	static MainWindow mainFrame;

	public static void main(String[] args) {
		
		File_Manager.read();

		File_Manager.defineMundos();

		openSelection();

	}

	/**
	 * Cria e mostra o frame de seleção de mundo
	 */
	public static void openSelection() {

		selecionar = new selecionar_mundo.GUI();

		selecionar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		selecionar.pack();
		selecionar.setVisible(true);
		selecionar.setResizable(false);
		
		selecionar.setLocationRelativeTo(null);

		selecionar.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			
			public void windowClosing(WindowEvent arg0) {
//				File_Manager.save();
			}
			
			public void windowClosed(WindowEvent arg0) {}
			public void windowActivated(WindowEvent arg0) {}
		});
		
	}

	/**
	 * Cria e mostra o frame de ferramentas, fechando o frame de seleção de
	 * mundo e salvando todas as configurações de mundo na pasta de
	 * configurações
	 */
	public static void openMainFrame() {
		
		File_Manager.defineModelos();

		mainFrame = new MainWindow();
		
		// Adicionando todas as ferramentas criadas
		mainFrame.addPanel(new recrutamento.GUI());
		mainFrame.addPanel(new dados_de_unidade.GUI());
		mainFrame.addPanel(new pontos.GUI());
		mainFrame.addPanel(new distância.GUI());
		mainFrame.addPanel(new oponentes_derrotados.GUI());
		mainFrame.addPanel(new simulador.GUI());
		mainFrame.addPanel(new assistente_saque.GUI());

		mainFrame.selectFirst();

		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setResizable(false);
		
		// Puts the mainFrame where the world-selection frame was
		mainFrame.setLocationRelativeTo(selecionar);
		selecionar.dispose();
		
		mainFrame.setVisible(true);
		
		mainFrame.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			
			public void windowClosing(WindowEvent arg0) {
				File_Manager.save();
			}
			
			public void windowClosed(WindowEvent arg0) {}
			public void windowActivated(WindowEvent arg0) {}
		});
		
		final SystemTray tray = SystemTray.getSystemTray();
		
		try {
			final TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(
					GUI.class.getResource("/images/Icon.png")));
			
			icon.setImageAutoSize(true);
			
			PopupMenu popup = new PopupMenu();
			
			MenuItem item = new MenuItem("Escolher Mundos");
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
					File_Manager.save();
					mainFrame.dispose();
					main(null);
					tray.remove(icon);
					
				}
			});
			popup.add(item);
			item = new MenuItem("Sair");
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
					tray.remove(icon);
					
				}
			});
			popup.add(item);
			
			icon.setPopupMenu(popup);
			
			icon.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					mainFrame.setVisible(true);
					mainFrame.toFront();
					mainFrame.repaint();
					
				}
			});
			
			tray.add(icon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		Date time1 = new Date();
		time1.setTime(time1.getTime()+5000);
		
		Date time2 = new Date();
		time2.setTime(time2.getTime()+10000);
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				System.out.println("Timer 1 has done");
				
			}
		}, time1);
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				System.out.println("Timer 2 has done");
				
			}
		}, time2);
		
	}

}
