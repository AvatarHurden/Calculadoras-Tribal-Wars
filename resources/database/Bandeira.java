package database;

public class Bandeira {

	private CategoriaBandeira categoria;
	private int n�vel;

	/**
	 * @param categoria
	 * @param n�vel
	 *            de 0 a 8
	 */
	public Bandeira(CategoriaBandeira categoria, int n�vel) {

		this.categoria = categoria;
		this.n�vel = n�vel;

	}

	@Override
	public String toString() {

		return categoria.toString(n�vel);

	}

	/**
	 * Retorna o valor a ser multiplicado (militar ou produtivo) ou adicionado
	 * ao n�mero base, levando em conta o n�vel da bandeira.
	 * 
	 * @return
	 */
	public double getValue() {

		// A bandeira de sorte � muito confusa, pois n�o possui um crescimento
		// cont�nuo nos �ltimos n�veis
		if (categoria.tipo.equals(CategoriaBandeira.Tipo.Sorte)) {

			if (n�vel == 7)
				return 19;
			if (n�vel == 8)
				return 20;
			else
				return categoria.inicial + n�vel * categoria.aumento;

		} else
			return (double) ((categoria.inicial + (n�vel * categoria.aumento)) + 100) / 100;

	}

	/**
	 * @return Tipo da bandeira (militar, produtivo ou sorte)
	 */
	public database.Bandeira.CategoriaBandeira.Tipo getTipo() {
		return categoria.tipo;
	}

	// Categorias diferentes que cada bandeira pode ter. Cada uma possui seu
	// valor base e
	// aumento por n�vel, al�m de um "tipo" criado por mim para facilitar
	// funcionamento
	// e em quais situa��es ela existe.
	public enum CategoriaBandeira {

		RECURSOS(Tipo.Produtivo, "produ��o de recursos", 4, 2), RECRUTAMENTO(
				Tipo.Produtivo, "velocidade de recrutamento", 6, 2), ATAQUE(
				Tipo.Militar, "for�a de ataque", 2, 1), DEFESA(Tipo.Militar,
				"b�nus de defesa", 2, 1), SORTE(Tipo.Sorte, "", 6, 2), POPULA��O(
				Tipo.Produtivo, "popula��o", 2, 1), MOEDAS(Tipo.Produtivo,
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

		public String toString(int n�vel) {

			if (this == NULL) {

				return "Nenhuma";

			} else if (tipo.equals(Tipo.Sorte)) {

				if (n�vel == 7)
					return "Equilibra a sorte em 19%";
				if (n�vel == 8)
					return "Equilibra a sorte em 20%";
				else
					return "Equilibra a sorte em "
							+ (inicial + n�vel * aumento) + "%";

			} else {

				String s = "";

				if (!equals(MOEDAS))
					s += "+";

				s += (inicial + n�vel * aumento);

				s += "% " + frase;

				return s;

			}

		}
	}

}
