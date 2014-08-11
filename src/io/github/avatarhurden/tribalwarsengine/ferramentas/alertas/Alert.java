package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import database.Unidade;

/**
 * Contém informações sobre um alerta criado pelo usuário. Este alerta possui várias configurações, e
 * funciona como um despertador, aparecendo um popup na tela do usuário no horário informado.
 * 
 * @author Arthur
 */
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
    
    private Map<Unidade, Integer> tropas;
    private Aldeia origem;
    private Aldeia destino;
    private List<Date> avisos;
    
    /**
     * Cria um alerta vazio. Para definir as características, utilize os setters
     */
    public Alert() {}

    protected void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    protected void setNome(String nome) {
        this.nome = nome;
    }

    protected void setNotas(String notas) {
        this.notas = notas;
    }

    protected void setHorário(Date horário) {
        this.horário = horário;
    }

    protected void setRepete(long repete) {
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
    
    protected void setAvisos(List<Date> avisos) {
        this.avisos = avisos;
        
        Collections.sort(this.avisos, new Comparator<Date>() {

			@Override
			public int compare(Date o1, Date o2) {
				
				return - o1.compareTo(o2);
				
			}
		});
    }

    protected Tipo getTipo() {
        return tipo;
    }

    protected String getNome() {
        return nome;
    }

    protected String getNotas() {
        return notas;
    }

    protected Date getHorário() {
        return horário;
    }

    protected Long getRepete() {
        return repete;
    }

    protected Map<Unidade, Integer> getTropas() {
    	if (!tipo.equals(Tipo.Geral))
    		return tropas;
    	else
    		return null;
    }

    protected Aldeia getOrigem() {
    	if (!tipo.equals(Tipo.Geral))
    		return origem;
    	else
    		return null;
    }

    protected Aldeia getDestino() {
    	if (!tipo.equals(Tipo.Geral))
    		return destino;
    	else
    		return null;
    }

    /**
     * Retorna um stack dos avisos, com o topo sendo ocupado pelo aviso mais cedo
     */
    protected Stack<Date> getAvisos() {
    	
    	Stack<Date> retorno = new Stack<Date>();
    	retorno.addAll(avisos);
    	
        return retorno;
    }
}
