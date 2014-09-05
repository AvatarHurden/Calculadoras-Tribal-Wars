package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Contém informações sobre um alerta criado pelo usuário. Este alerta possui várias configurações, e
 * funciona como um despertador, aparecendo um popup na tela do usuário no horário informado.
 * 
 * @author Arthur
 */
public class Alert {

	public enum Tipo {
        Geral, Ataque, Apoio, Saque;
    }

    public static class Aldeia {
        protected final int x, y;
        protected final String nome;

        public Aldeia(String nome, int x, int y) {
            this.nome = nome;
            this.x = x;
            this.y = y;
        }

        public String toString() {
            if (nome == null || nome.equals(""))
                return x + "|" + y;
            else
                return "<html>" + nome + "<br>" + "(" + x + "|" + y + ")</html>";
        }
    }

    private Tipo tipo;
    private String nome;
    private String notas;
    private Date horário;
    // Decidir se manter isso ou mudar a maneira
    private Long repete;
    
    private Army army;
    private Aldeia origem;
    private Aldeia destino;
    private List<Date> avisos;
    
    /**
     * Cria um alerta vazio. Para definir as características, utilize os setters
     */
    public Alert() {}

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setHorário(Date horário) {
        this.horário = horário;
    }

    public void setRepete(long repete) {
        this.repete = repete;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

    public void setOrigem(Aldeia origem) {
        this.origem = origem;
    }

    public void setDestino(Aldeia destino) {
        this.destino = destino;
    }
    
    public void setAvisos(List<Date> avisos) {
        this.avisos = avisos;
        
        Collections.sort(this.avisos, new Comparator<Date>() {

			@Override
			public int compare(Date o1, Date o2) {
				
				return - o1.compareTo(o2);
				
			}
		});
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getNotas() {
        return notas;
    }

    public Date getHorário() {
        return horário;
    }

    public Long getRepete() {
        return repete;
    }

    public Army getArmy() {
    	if (!tipo.equals(Tipo.Geral))
    		return army;
    	else
    		return null;
    }

    public Aldeia getOrigem() {
    	if (!tipo.equals(Tipo.Geral))
    		return origem;
    	else
    		return null;
    }

    public Aldeia getDestino() {
    	if (!tipo.equals(Tipo.Geral))
    		return destino;
    	else
    		return null;
    }

    /**
     * Retorna um stack dos avisos, com o topo sendo ocupado pelo aviso mais cedo
     */
    public Stack<Date> getAvisos() {
    	
    	Stack<Date> retorno = new Stack<Date>();
    	retorno.addAll(avisos);
    	
        return retorno;
    }
}
