package config;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Scanner;

/**
 * Classe para pegar dados de um mundo através de texto extraído do site do tribal wars.
 * Atualmente não pode ser usado no programa, servindo apenas para teste. 
 * 
 * @author Arthur
 *
 */
public class InfoFinder {

	public static void main(String[] args) {	
		
		while (true) {
		
		Scanner input = new Scanner(System.in);

		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		String text = "";
		
		do {
			text += input.nextLine()+"\n";
		} while (!text.contains("terminar"));
		
		Scanner string = new Scanner(text);
		
		String nome = "";
		
		double velocidade = 0;
		
		double velocidadeUnidade = 0;
		
		boolean moral = false;
		
		boolean pesquisaDeNíveis = false;
		
		boolean igreja = false;
		
		boolean bonusNoturno = false;
		
		boolean bandeira = false;
		
		boolean arqueiro = false;
		
		boolean paladino = false;
		
		boolean itensAprimorados = false;
		
		boolean milícia = false;
		
		boolean academiaDeNíveis = false;
		
		System.out.println(text);
		
		while (string.hasNextLine()) {
			
			String next = string.nextLine();
			
			if (next.contains("Mundo"))
				nome = next.replace("Configurações de mundo ", "");
			
			if (next.contains("Velocidade do jogo"))
				velocidade = Double.parseDouble(next.replace("Velocidade do jogo", ""));
			
			if (next.contains("Velocidade das unidades"))
				velocidadeUnidade = Double.parseDouble(next.replace("Velocidade das unidades", ""));
		
			if (next.toLowerCase().contains("moral"))
				if (next.toLowerCase().contains("inativo"))
					moral = false;
				else
					moral = true;
			
			if (next.toLowerCase().contains("sistema de pesquisa"))
				if (next.toLowerCase().contains("pesquisa simplificada"))
					pesquisaDeNíveis = false;
				else
					pesquisaDeNíveis = true;
			
			if (next.toLowerCase().contains("igreja"))
				if (next.toLowerCase().contains("inativo"))
					igreja = false;
				else
					igreja = true;
			
			if (next.toLowerCase().contains("bônus noturno") || next.toLowerCase().contains("modo noturno ativado"))
				if (next.toLowerCase().contains("inativo"))
					bonusNoturno = false;
				else
					bonusNoturno = true;
			
			if (next.toLowerCase().contains("bandeiras"))
				if (next.toLowerCase().contains("desativado"))
					bandeira = false;
				else
					bandeira = true;
			
			if (next.toLowerCase().contains("arqueiros"))
				if (next.toLowerCase().contains("inativo"))
					arqueiro = false;
				else
					arqueiro = true;
			
			if (next.toLowerCase().contains("paladino"))
				if (next.toLowerCase().contains("inativo"))
					paladino = false;
				else
					paladino = true;
			
			if (next.toLowerCase().contains("paladino") || next.toLowerCase().contains("os itens"))
				if (next.toLowerCase().contains("aprimorados"))
					itensAprimorados = true;
				else
					itensAprimorados = false;
			
			if (next.toLowerCase().contains("milícia"))
				if (next.toLowerCase().contains("desativado"))
					milícia = false;
				else
					milícia = true;

			if (next.toLowerCase().contains("preços crescentes para nobres"))
				if (next.toLowerCase().contains("moedas de ouro"))
					academiaDeNíveis = false;
				else
					academiaDeNíveis = true;
		}
		
		input.close();
		
		string.close();
		
		System.out.println();
		
		String output = "";
		
		output +=("#"+"\n");
		output +=("nome="+nome+"\n");
		output +=("velocidade="+velocidade+"\n");
		output +=("modificador="+velocidadeUnidade+"\n");
		output +=("moral="+moral+"\n");
		output +=("pesquisaDeNiveis="+pesquisaDeNíveis+"\n");
		output +=("igreja="+igreja+"\n");
		output +=("bonusNoturno="+bonusNoturno+"\n");
		output +=("bandeira="+bandeira+"\n");
		output +=("arqueiro="+arqueiro+"\n");
		output +=("paladino="+paladino+"\n");
		output +=("itensAprimorados="+itensAprimorados+"\n");
		output +=("milicia="+milícia+"\n");
		output +=("academiaDeNiveis="+academiaDeNíveis+"\n");
		
		clip.setContents(new StringSelection(output), null);
		
		System.out.println(output);
		
		}
		
	}
	
}
