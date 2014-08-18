package io.github.avatarhurden.tribalwarsengine.enums;

/**
 * @author Wesley Nascimento
 */
public enum ResearchSystem {
    SIMPLE(1, "Pesquisa simplificada"),
    THREE_LEVELS(3, "Sistema de 3 n�veis"),
    TEN_LEVELS(10, "Sistema cl�ssico");

    private int research;
    private String name;

    private ResearchSystem(int availableResearch, String name) {
        this.research = availableResearch;
        this.name = name;
    }

    public int getResearch() {
        return research;
    }

    public String getName() {
        return name;
    }

    /**
     * Converte um numero inteiro em um Objeto do enum SearchSystem
     *
     * @param searchs - Numero de pesquisas
     * @return
     */
    public static ResearchSystem ConvertInteger(int searchs) {
        switch (searchs) {
            case 1:
                return ResearchSystem.SIMPLE;
            case 3:
                return ResearchSystem.THREE_LEVELS;
            case 10:
                return ResearchSystem.TEN_LEVELS;
        }

        //por padr�o retorna
        return ResearchSystem.SIMPLE;
    }
}
