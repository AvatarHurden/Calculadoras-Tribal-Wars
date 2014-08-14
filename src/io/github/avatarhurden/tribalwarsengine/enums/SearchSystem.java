package io.github.avatarhurden.tribalwarsengine.enums;

/**
 * @author Wesley Nascimento
 */
public enum SearchSystem {
    SIMPLE(1), THREE_LEVELS(3), TEN_LEVELS(10);

    private int research;

    private SearchSystem(int availableResearch) {
        this.research = availableResearch;
    }

    public int getResearch() {
        return research;
    }

    /**
     * Converte um numero inteiro em um Objeto do enum SearchSystem
     *
     * @param searchs - Numero de pesquisas
     * @return
     */
    public static SearchSystem ConvertInteger(int searchs) {
        switch (searchs) {
            case 1:
                return SearchSystem.SIMPLE;
            case 3:
                return SearchSystem.THREE_LEVELS;
            case 10:
                return SearchSystem.TEN_LEVELS;
        }

        //por padrão retorna
        return SearchSystem.SIMPLE;
    }
}
