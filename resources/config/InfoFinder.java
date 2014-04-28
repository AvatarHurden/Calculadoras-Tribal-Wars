package config;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Classe para pegar dados de um mundo atrav�s de texto extra�do do site do
 * tribal wars. Atualmente n�o pode ser usado no programa, servindo apenas para
 * teste.
 * 
 * @author Arthur
 * 
 */
public class InfoFinder {

	public static void main(String[] args) throws HeadlessException, UnsupportedFlavorException, IOException {

//		while (true) {

			Scanner input = new Scanner(System.in);
//
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//			String text = "";
//
//			do {
//				text += input.nextLine() + "\n";
//			} while (!text.contains(""));

			Scanner string = new Scanner(
					(String) clip.getData(DataFlavor.stringFlavor));

			String nome = "";

			double velocidade = 0;

			double velocidadeUnidade = 0;

			boolean moral = false;

			String pesquisaDeN�veis = "Pesquisa_Simples";

			boolean igreja = false;

			boolean bonusNoturno = false;

			boolean bandeira = false;

			boolean arqueiro = false;

			boolean paladino = false;

			boolean itensAprimorados = false;

			boolean mil�cia = false;

			boolean cunhagemDeMoedas = false;

			while (string.hasNextLine()) {

				String next = string.nextLine();

				if (next.contains("Mundo"))
					nome = next.replace("Configura��es de mundo ", "");

				if (next.contains("Velocidade do jogo"))
					velocidade = Double.parseDouble(next.replace(
							"Velocidade do jogo", ""));

				if (next.contains("Velocidade das unidades"))
					velocidadeUnidade = Double.parseDouble(next.replace(
							"Velocidade das unidades", ""));

				if (next.toLowerCase().contains("moral"))
					if (next.toLowerCase().contains("inativo"))
						moral = false;
					else
						moral = true;

				if (next.toLowerCase().contains("sistema de pesquisa"))
					if (next.toLowerCase().contains("pesquisa simplificada"))
						pesquisaDeN�veis = "Pesquisa_Simples";
					else
						pesquisaDeN�veis = "Pesquisa_de_3_N�veis";

				if (next.toLowerCase().contains("igreja"))
					if (next.toLowerCase().contains("inativo"))
						igreja = false;
					else
						igreja = true;

				if (next.toLowerCase().contains("b�nus noturno")
						|| next.toLowerCase().contains("modo noturno ativado"))
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

				if (next.toLowerCase().contains("paladino")
						|| next.toLowerCase().contains("os itens"))
					if (next.toLowerCase().contains("aprimorados"))
						itensAprimorados = true;
					else
						itensAprimorados = false;

				if (next.toLowerCase().contains("mil�cia"))
					if (next.toLowerCase().contains("desativado"))
						mil�cia = false;
					else
						mil�cia = true;

				if (next.toLowerCase()
						.contains("pre�os crescentes para nobres"))
					if (next.toLowerCase().contains("moedas de ouro"))
						cunhagemDeMoedas = true;
					else
						cunhagemDeMoedas = false;
			}

			input.close();

			string.close();

			System.out.println();

			String output = "";

			output += ("\tnome=" + nome + "\n");
			output += ("\tvelocidade=" + velocidade + "\n");
			output += ("\tmodificador=" + velocidadeUnidade + "\n");
			output += ("\tmoral=" + moral + "\n");
			output += ("\tsistemaDePesquisa=" + pesquisaDeN�veis + "\n");
			output += ("\tigreja=" + igreja + "\n");
			output += ("\tbonusNoturno=" + bonusNoturno + "\n");
			output += ("\tbandeira=" + bandeira + "\n");
			output += ("\tarqueiro=" + arqueiro + "\n");
			output += ("\tpaladino=" + paladino + "\n");
			output += ("\titensAprimorados=" + itensAprimorados + "\n");
			output += ("\tmilicia=" + mil�cia + "\n");
			output += ("\tcunhagemDeMoedas=" + cunhagemDeMoedas + "\n");

			clip.setContents(new StringSelection(output), null);

			System.out.println(output);

		}

//	}

}
