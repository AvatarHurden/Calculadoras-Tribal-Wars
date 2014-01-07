package main;

import javax.swing.JFrame;

import config.File_Reader;
import config.Mundo_Reader;
import frames.MainWindow;

public class Main {

	static selecionar_mundo.GUI selecionar;
	static MainWindow mainFrame;
	
	public static void main(String[] args) {
		
		File_Reader.read();
		
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
		
	}
	
	/**
	 * Cria e mostra o frame de ferramentas, fechando o frame de seleção de mundo
	 * e salvando todas as configurações de mundo na pasta de configurações
	 */
	public static void openMainFrame() {
		
		selecionar.dispose();
		
//		Mundo_Reader.save();
		
		mainFrame = new MainWindow();
		//Adicionando todas as ferramentas criadas
		mainFrame.addPanel(new recrutamento.GUI());
		mainFrame.addPanel(new dados_de_unidade.GUI());
		mainFrame.addPanel(new pontos.GUI());
		mainFrame.addPanel(new distância.GUI());
		mainFrame.addPanel(new oponentes_derrotados.GUI());
		mainFrame.addPanel(new simulador.GUI());
		
		mainFrame.selectFirst();
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.pack();
		mainFrame.setResizable(false);
		
	}
	
}
