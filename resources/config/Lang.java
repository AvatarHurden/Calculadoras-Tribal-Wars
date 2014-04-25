package config;

public enum Lang {
	
	// TODO add best attack composition on simulador (percentages)
	
	// TODO change config file and "Property_Escolha" to better suit this (maybe make noble prices a "Escolha")
	
	// UNIDADES
					// Portugu�s			Ingl�s
	Lanceiro(		"Lanceiro"),
	Espadachim(		"Espadachim"),
	Barbaro(		"B�rbaro"),
	Arqueiro(		"Arqueiro"),
	Explorador(		"Explorador"),
	CavaloLeve(		"Cavalaria Leve"),
	ArcoCavalo(		"Arqueiro a Cavalo"),
	CavaloPesado(	"Cavalaria Pesada"),
	Ariete(			"Ar�ete"),
	Catapulta(		"Catapulta"),
	Nobre(			"Nobre"),
	Milicia(		"Mil�cia"),
	
	// EDIF�CIOS
					// Portugu�s	
	EdPrincipal(	"Edif�cio Principal"),
	Quartel(		"Quartel"),
	Estabulo(		"Est�bulo"),
	Oficina(		"Oficina"),
	Igreja(			"Igreja"),
	PrimeiraIgreja(	"Primeira Igreja"),
	Academia(		"Academia"),
	Ferreiro(		"Ferreiro"),
	PracaReuniao(	"Pra�a de Reuni�o"),
	Estatua(		"Est�tua"),
	Mercado(		"Mercado"),
	Bosque(			"Bosque"),
	PocoArgila(		"Po�o de Argila"),
	MinaFerro(		"Mina de Ferro"),
	Fazenda(		"Fazenda"),
	Armazem(		"Armaz�m"),
	Esconderijo(	"Esconderijo"),
	Muralha(		"Muralha");
	
	String portugu�s, ingl�s;
	
	Lang(String portugu�s) {
		
		this.portugu�s = portugu�s;
	}
	
	public String toString() {
		
		return portugu�s;
		
	}
	
}
