package database;

public class Bandeira {

	private CategoriaBandeira categoria;
	private int nível;

	/**
	 * @param categoria
	 * @param nível
	 *            de 0 a 8
	 */
	public Bandeira(CategoriaBandeira categoria, int nível) {

		this.categoria = categoria;
		this.nível = nível;

	}

	@Override
	public String toString() {

		return categoria.toString(nível);

	}

	/**
	 * Retorna o valor a ser multiplicado (militar ou produtivo) ou adicionado
	 * ao número base, levando em conta o nível da bandeira.
	 * 
	 * @return
	 */
	public double getValue() {

		// A bandeira de sorte é muito confusa, pois não possui um crescimento
		// contínuo nos últimos níveis
		if (categoria.tipo.equals(CategoriaBandeira.Tipo.Sorte)) {

			if (nível == 7)
				return 19;
			if (nível == 8)
				return 20;
			else
				return categoria.inicial + nível * categoria.aumento;

		} else
			return (double) ((categoria.inicial + (nível * categoria.aumento)) + 100) / 100;

	}

	/**
	 * @return Tipo da bandeira (militar, produtivo ou sorte)
	 */
	public database.Bandeira.CategoriaBandeira.Tipo getTipo() {
		return categoria.tipo;
	}

	// Categorias diferentes que cada bandeira pode ter. Cada uma possui seu
	// valor base e
	// aumento por nível, além de um "tipo" criado por mim para facilitar
	// funcionamento
	// e em quais situações ela existe.
	public enum CategoriaBandeira {

		RECURSOS(Tipo.Produtivo, "produção de recursos", 4, 2), RECRUTAMENTO(
				Tipo.Produtivo, "velocidade de recrutamento", 6, 2), ATAQUE(
				Tipo.Militar, "força de ataque", 2, 1), DEFESA(Tipo.Militar,
				"bônus de defesa", 2, 1), SORTE(Tipo.Sorte, "", 6, 2), POPULAÇÃO(
				Tipo.Produtivo, "população", 2, 1), MOEDAS(Tipo.Produtivo,
				"custo de moedas", -10, -2), SAQUE(Tipo.Produtivo,
				"capacidade de saque", 2, 1), NULL(Tipo.Null, "", 0, 0);

		public enum Tipo {
			Militar, Produtivo, Sorte, Null;
		}

		private final Tipo tipo;
		private final String frase;
		private final int inicial;
		private final int aumento;

		private CategoriaBandeira(Tipo tipo, String frase, int inicial,
				int aumento) {

			this.tipo = tipo;
			this.frase = frase;
			this.inicial = inicial;
			this.aumento = aumento;

		}

		public Tipo getTipo() {
			return tipo;
		}

		public String toString(int nível) {

			if (this == NULL) {

				return "Nenhuma";

			} else if (tipo.equals(Tipo.Sorte)) {

				if (nível == 7)
					return "Equilibra a sorte em 19%";
				if (nível == 8)
					return "Equilibra a sorte em 20%";
				else
					return "Equilibra a sorte em "
							+ (inicial + nível * aumento) + "%";

			} else {

				String s = "";

				if (!equals(MOEDAS))
					s += "+";

				s += (inicial + nível * aumento);

				s += "% " + frase;

				return s;

			}

		}
	}

}
