package alertas;

import java.util.Date;
import java.util.Map;

import database.Unidade;

public class Alert {

	private enum Tipo {
		Geral, Ataque, Apoio, Saque;
	}
	
	@SuppressWarnings("unused")
	private class Aldeia {
		public final int x, y;
		public final String nome;
		public Aldeia(String nome, int x, int y) {
			this.nome = nome;
			this.x = x;
			this.y = y;
		}
	}
	
	private Tipo tipo;
	private String nome;
	private Date horário;
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

	protected void setHorário(Date horário) {
		this.horário = horário;
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

	protected Date getHorário() {
		return horário;
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
