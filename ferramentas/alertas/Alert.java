package alertas;

import java.util.Date;
import java.util.Map;

import database.Unidade;

public class Alert {

	protected enum Tipo {
		Geral, Ataque, Apoio, Saque;
	}
	
	protected static class Aldeia {
		protected final int x, y;
		protected final String nome;
		protected Aldeia(String nome, int x, int y) {
			this.nome = nome;
			this.x = x;
			this.y = y;
		}
		
		public String toString() {
			return nome + " - (" + x + "|" + y + ")";
		}
	}
	
	private Tipo tipo;
	private String nome;
	private Date hor�rio;
	private Date repete;
	private Map<Unidade, Integer> tropas;
	private Aldeia origem;
	private Aldeia destino;
	
	public Alert() {
		
		origem = new Aldeia("oi", 100, 200);
		
	}

	protected void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	protected void setNome(String nome) {
		this.nome = nome;
	}

	protected void setHor�rio(Date hor�rio) {
		this.hor�rio = hor�rio;
	}
	
	protected void setRepete(Date repete) {
		this.repete = repete;
	}

	protected void setTropas(Map<Unidade, Integer> tropas) {
		this.tropas = tropas;
	}

	protected void setOrigem(Aldeia origem) {
		this.origem = origem;
	}

	protected void setDestino(Aldeia destino) {
		this.destino = destino;
	}

	protected Tipo getTipo() {
		return tipo;
	}

	protected String getNome() {
		return nome;
	}

	protected Date getHor�rio() {
		return hor�rio;
	}
	
	protected Date getRepete() {
		return repete;
	}

	protected Map<Unidade, Integer> getTropas() {
		return tropas;
	}

	protected Aldeia getOrigem() {
		return origem;
	}

	protected Aldeia getDestino() {
		return destino;
	}
	
}
