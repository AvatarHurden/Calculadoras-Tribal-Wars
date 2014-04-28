package config;


public enum Lang {
	
	// TODO change config file and "Property_Escolha" to better suit this (maybe make noble prices a "Escolha")
	
	// GERAL
	
	Titulo(				"Tribal Wars Engine"),
	
	// Frame de sele��o de mundo
		
	Criador(			"<html><div align=right width=100px>"
							+ "Criado por Arthur Vedana<br>"
							+ "agieselvedana@gmail.com</div></html>"),
	Padrao(				"Padr�o"),
	Editar(				"Editar"),
	Iniciar(			"Iniciar"),
	
	// FERRAMENTAS
	
	FerramentaUnidade(	"C�lculo de Unidades"),
	FerramentaDistancia("C�lculo de Dist�ncia"),
	FerramentaOD(		"C�lculo de OD"),
	FerramentaPontos(	"C�lculo de Pontos"),
	FerramentaRecruta(	"Tempo de Recruta"),
	FerramentaSimulador("Simulador"),
	
	// PALAVRAS-CHAVE
	
	Unidade(			"Unidade"),
	Nivel(				"N�vel"),
	Quantidade(			"Quantidade"),
	Ataque(				"Ataque"),
	DefGeral(			"Def. Geral"),
	DefCavalo(			"Def. Cav."),
	DefArqueiro(		"Def. Arq."),
	Saque(				"Saque"),
	Madeira(			"Madeira"),
	Argila(				"Argila"),
	Ferro(				"Ferro"),
	
	Populacao(			"Popula��o"),
	PopDisponivel(		"Popula��o Dispon�vel"),		
	
	Tempo(				"Tempo"),
	TempoUnidade(		"Tempo por Unidade"),
	TempoTotal(			"Tempo Total"),	
	
	Edificio(			"Edif�cio"),
	
	Modelos(			"Modelos"),
	// TODO figure out how to do the building level on recrutamento
	
	ODAtaque(			"OD Ataque"),
	ODDefesa(			"OD Defesa"),
	ODTotal(			"OD Total"),
	
	// Simulador
	
	Atacante(			"Atacante"),
	Defensor(			"Defensor"),
	
	Religioso(			"Religioso"),
	BonusNoturno(		"B�nus Noturno"),
	Sorte(				"Sorte"),
	Moral(				"Moral"),
	ItemPaladino(		"Item Paladino"),
	Bandeira(			"Bandeira"),
	
		
	// Aldeias
	
	AldeiaOrigem(		"Aldeia de Origem"),
	AldeiaDestino(		"Aldeia de Destino"),
	
	// BOT�ES
	
	BtnCalcular(		"Calcular"),
	BtnNovo(			"Novo"),
	BtnSalvar(			"Salvar"),
	BtnDeletar(			"Deletar"),
	BtnResetar(			"Resetar"),
	
	
	// EditDialog
	
	DeleteOptionDialog(	"<html>Tem certeza que deseja deletar?"
							+ "<br>Essa a��o n�o " +
							"pode ser desfeita.</html>"),
	Sim(				"Sim"),
	Nao(				"N�o"),
	AvisoNomeUsado(		"<html>Esse nome j� est� sendo utilizado."
							+ "<br>Favor escolher outro.</html>"),
	NomeUsado(			"Nome j� utilizado"),
	
	// BANDEIRAS
	
	BandeiraSorte(		"Equilibra a sorte em "),
	BandeiraRecursos(	"produ��o de recursos"),
	BandeiraRecruta(	"velocidade de recrutamento"),
	BandeiraAtaque(		"for�a de ataque"),
	BandeiraDefesa(		"b�nus de defesa"),
	BandeiraPopulacao(	"popula��o"),
	BandeiraMoeda(		"custo de moedas"),
	BandeiraSaque(		"capacidade de saque"),
	BandeiraNull(		"Nenhuma"),

	// ITEM PALADINO
	
	ItemAlabarda(		"Alabarda de Guan Yu"),
	ItemEspada(			"Espada Longa de Ullrich"),
	ItemMachado(		"Machado de Guerra de Thogard"),
	ItemArco(			"Arco Longo de Nimrod"),
	ItemTelescopio(		"Telesc�pio de Kalid"),
	ItemLanca(			"Lan�a de Miesko"),
	ItemEstandarte(		"Estandarte de Baptiste"),
	ItemArcoComposto(	"Arco Composto de Nimrod"),
	ItemEstrela(		"Estrela de Manh� de Carol"),
	ItemFogueira(		"Fogueira de Aletheia"),
	ItemCetro(			"Cetro de Vasco"),
	ItemNull(			"Nenhum Item"),
	
	DescripAlabarda(	"Aumenta ataque de lanceiros em %.0f%% e defesa em %.0f%%"),
	DescripEspada(		"Aumenta ataque de espadachins em %.0f%% e defesa em %.0f%%"),
	DescripMachado(		"Aumenta ataque de b�rbaros em %.0f%% e defesa em %.0f%%"),
	DescripArco(		"Aumenta ataque de arqueiros em %.0f%% e defesa em %.0f%%"),
	DescripTelescopio(	"Exploradores sempre veem as tropas de uma aldeia"),
	DescripLanca(		"Aumenta ataque de cavalaria leve em %.0f%% e defesa em %.0f%%"),
	DescripEstandarte(	"Aumenta ataque de cavalaria pesada em %.0f%% e defesa em %.0f%%"),
	DescripArcoComposto("Aumenta ataque de arqueiro a cavalo em %.0f%% e defesa em %.0f%%"),
	DescripEstrela(		"Aumenta ataque de ar�etes em %.0f%%"),
	DescripFogueira(	"Aumenta ataque de catapultas em %.0f%% e defesa em %.0f%%"),
	DescripCetro(		"Nobres reduzem a lealdade por, no m�nimo, 30"),
	DescripItemNull(	""),

	// MODELOS
	
	NovoModelo(			"Novo Modelo"),
	
	// MUNDOS
	
	NovoMundo(			"Novo Mundo"),
	
	PropNomeName(		"Nome"),
	PropArqueiroName(	"Arqueiro"),
	PropMiliciaName(	"Mil�cia"),
	PropPaladinoName(	"Paladino"),
	PropItensName(		"Itens Aprimorados"),
	PropIgrejaName(		"Igreja"),
	PropMoralName(		"Moral"),
	PropBandeiraName(	"Bandeiras"),
	PropNoiteName(		"B�nus Noturno"),
	PropMoedasName(		"Cunhagem de Moedas"),
	PropVelocidadeName(	"Velocidade"),
	PropModificadorName("Modificar de Unidade"),
	
	PropPesquisaName(	"Sistema de Pesquisa"),
	PropPesquisaSimples("Pesquisa Simples"),
	PropPesquisaNiveis(	"Pesquisa de 3 N�veis"),
	
	PropAcademiaName(	"Pre�os crescentes para nobres"),
	PropAcademiaMoeda(	"Cunhagem de Moedas"),
	PropAcademiaNiveis(	"Academia de N�veis"),
	
	// PROPERTIES
	
	PropAtivado(		"Ativado"),
	PropDesativado(		"Desativado"),
	
	// UNIDADES
					// Portugu�s			Ingl�s
	Lanceiro(			"Lanceiro"),
	Espadachim(			"Espadachim"),
	Barbaro(			"B�rbaro"),
	Arqueiro(			"Arqueiro"),
	Explorador(			"Explorador"),
	CavaloLeve(			"Cavalaria Leve"),
	ArcoCavalo(			"Arqueiro a Cavalo"),
	CavaloPesado(		"Cavalaria Pesada"),
	Ariete(				"Ar�ete"),
	Catapulta(			"Catapulta"),
	Nobre(				"Nobre"),
	Milicia(			"Mil�cia"),
	
	// EDIF�CIOS
					// Portugu�s	
	EdPrincipal(		"Edif�cio Principal"),
	Quartel(			"Quartel"),
	Estabulo(			"Est�bulo"),
	Oficina(			"Oficina"),
	Igreja(				"Igreja"),
	PrimeiraIgreja(		"Primeira Igreja"),
	Academia(			"Academia"),
	Ferreiro(			"Ferreiro"),
	PracaReuniao(		"Pra�a de Reuni�o"),
	Estatua(			"Est�tua"),
	Mercado(			"Mercado"),
	Bosque(				"Bosque"),
	PocoArgila(			"Po�o de Argila"),
	MinaFerro(			"Mina de Ferro"),
	Fazenda(			"Fazenda"),
	Armazem(			"Armaz�m"),
	Esconderijo(		"Esconderijo"),
	Muralha(			"Muralha");
	
	String portugu�s, ingl�s;
	
	Lang(String portugu�s) {
		
		this.portugu�s = portugu�s;
	}
	
	public String toString() {
		
		return portugu�s;
		
	}
	
}
