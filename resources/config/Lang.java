package config;

public enum Lang {
	
	// TODO add best attack composition on simulador (percentages)
	
	// TODO change config file and "Property_Escolha" to better suit this (maybe make noble prices a "Escolha")
	
	// UNIDADES
					// Português			Inglês
	Lanceiro(		"Lanceiro"),
	Espadachim(		"Espadachim"),
	Barbaro(		"Bárbaro"),
	Arqueiro(		"Arqueiro"),
	Explorador(		"Explorador"),
	CavaloLeve(		"Cavalaria Leve"),
	ArcoCavalo(		"Arqueiro a Cavalo"),
	CavaloPesado(	"Cavalaria Pesada"),
	Ariete(			"Aríete"),
	Catapulta(		"Catapulta"),
	Nobre(			"Nobre"),
	Milicia(		"Milícia"),
	
	// EDIFÍCIOS
					// Português	
	EdPrincipal(	"Edifício Principal"),
	Quartel(		"Quartel"),
	Estabulo(		"Estábulo"),
	Oficina(		"Oficina"),
	Igreja(			"Igreja"),
	PrimeiraIgreja(	"Primeira Igreja"),
	Academia(		"Academia"),
	Ferreiro(		"Ferreiro"),
	PracaReuniao(	"Praça de Reunião"),
	Estatua(		"Estátua"),
	Mercado(		"Mercado"),
	Bosque(			"Bosque"),
	PocoArgila(		"Poço de Argila"),
	MinaFerro(		"Mina de Ferro"),
	Fazenda(		"Fazenda"),
	Armazem(		"Armazém"),
	Esconderijo(	"Esconderijo"),
	Muralha(		"Muralha");
	
	String português, inglês;
	
	Lang(String português) {
		
		this.português = português;
	}
	
	public String toString() {
		
		return português;
		
	}
	
}
