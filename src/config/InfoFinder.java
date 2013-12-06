package config;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Scanner;

/**
 * Classe para pegar dados de um mundo atrav�s de texto extra�do do site do tribal wars.
 * Atualmente n�o pode ser usado no programa, servindo apenas para teste. 
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
		
		boolean pesquisaDeN�veis = false;
		
		boolean igreja = false;
		
		boolean bonusNoturno = false;
		
		boolean bandeira = false;
		
		boolean arqueiro = false;
		
		boolean paladino = false;
		
		boolean itensAprimorados = false;
		
		boolean mil�cia = false;
		
		boolean academiaDeN�veis = false;
		
		System.out.println(text);
		
		while (string.hasNextLine()) {
			
			String next = string.nextLine();
			
			if (next.contains("Mundo"))
				nome = next.replace("Configura��es de mundo ", "");
			
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
					pesquisaDeN�veis = false;
				else
					pesquisaDeN�veis = true;
			
			if (next.toLowerCase().contains("igreja"))
				if (next.toLowerCase().contains("inativo"))
					igreja = false;
				else
					igreja = true;
			
			if (next.toLowerCase().contains("b�nus noturno") || next.toLowerCase().contains("modo noturno ativado"))
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
			
			if (next.toLowerCase().contains("mil�cia"))
				if (next.toLowerCase().contains("desativado"))
					mil�cia = false;
				else
					mil�cia = true;

			if (next.toLowerCase().contains("pre�os crescentes para nobres"))
				if (next.toLowerCase().contains("moedas de ouro"))
					academiaDeN�veis = false;
				else
					academiaDeN�veis = true;
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
		output +=("pesquisaDeNiveis="+pesquisaDeN�veis+"\n");
		output +=("igreja="+igreja+"\n");
		output +=("bonusNoturno="+bonusNoturno+"\n");
		output +=("bandeira="+bandeira+"\n");
		output +=("arqueiro="+arqueiro+"\n");
		output +=("paladino="+paladino+"\n");
		output +=("itensAprimorados="+itensAprimorados+"\n");
		output +=("milicia="+mil�cia+"\n");
		output +=("academiaDeNiveis="+academiaDeN�veis+"\n");
		
		clip.setContents(new StringSelection(output), null);
		
		System.out.println(output);
		
		}
		
	}
	
}
