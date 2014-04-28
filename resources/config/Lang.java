package config;


public enum Lang {
	
	// TODO change config file and "Property_Escolha" to better suit this (maybe make noble prices a "Escolha")
	
	// GERAL
	
	Titulo(				"Tribal Wars Engine"),
	
	// Frame de seleção de mundo
		
	Criador(			"<html><div align=right width=100px>"
							+ "Criado por Arthur Vedana<br>"
							+ "agieselvedana@gmail.com</div></html>"),
	Padrao(				"Padrão"),
	Editar(				"Editar"),
	Iniciar(			"Iniciar"),
	
	// FERRAMENTAS
	
	FerramentaUnidade(	"Cálculo de Unidades"),
	FerramentaDistancia("Cálculo de Distância"),
	FerramentaOD(		"Cálculo de OD"),
	FerramentaPontos(	"Cálculo de Pontos"),
	FerramentaRecruta(	"Tempo de Recruta"),
	FerramentaSimulador("Simulador"),
	
	// PALAVRAS-CHAVE
	
	Unidade(			"Unidade"),
	Nivel(				"Nível"),
	Quantidade(			"Quantidade"),
	Ataque(				"Ataque"),
	DefGeral(			"Def. Geral"),
	DefCavalo(			"Def. Cav."),
	DefArqueiro(		"Def. Arq."),
	Saque(				"Saque"),
	Madeira(			"Madeira"),
	Argila(				"Argila"),
	Ferro(				"Ferro"),
	
	Populacao(			"População"),
	PopDisponivel(		"População Disponível"),		
	
	Tempo(				"Tempo"),
	TempoUnidade(		"Tempo por Unidade"),
	TempoTotal(			"Tempo Total"),	
	
	Edificio(			"Edifício"),
	
	Modelos(			"Modelos"),
	// TODO figure out how to do the building level on recrutamento
	
	ODAtaque(			"OD Ataque"),
	ODDefesa(			"OD Defesa"),
	ODTotal(			"OD Total"),
	
	// Simulador
	
	Atacante(			"Atacante"),
	Defensor(			"Defensor"),
	
	Religioso(			"Religioso"),
	BonusNoturno(		"Bônus Noturno"),
	Sorte(				"Sorte"),
	Moral(				"Moral"),
	ItemPaladino(		"Item Paladino"),
	Bandeira(			"Bandeira"),
	
		
	// Aldeias
	
	AldeiaOrigem(		"Aldeia de Origem"),
	AldeiaDestino(		"Aldeia de Destino"),
	
	// BOTÕES
	
	BtnCalcular(		"Calcular"),
	BtnNovo(			"Novo"),
	BtnSalvar(			"Salvar"),
	BtnDeletar(			"Deletar"),
	BtnResetar(			"Resetar"),
	
	
	// EditDialog
	
	DeleteOptionDialog(	"<html>Tem certeza que deseja deletar?"
							+ "<br>Essa ação não " +
							"pode ser desfeita.</html>"),
	Sim(				"Sim"),
	Nao(				"Não"),
	AvisoNomeUsado(		"<html>Esse nome já está sendo utilizado."
							+ "<br>Favor escolher outro.</html>"),
	NomeUsado(			"Nome já utilizado"),
	
	// BANDEIRAS
	
	BandeiraSorte(		"Equilibra a sorte em "),
	BandeiraRecursos(	"produção de recursos"),
	BandeiraRecruta(	"velocidade de recrutamento"),
	BandeiraAtaque(		"força de ataque"),
	BandeiraDefesa(		"bônus de defesa"),
	BandeiraPopulacao(	"população"),
	BandeiraMoeda(		"custo de moedas"),
	BandeiraSaque(		"capacidade de saque"),
	BandeiraNull(		"Nenhuma"),

	// ITEM PALADINO
	
	ItemAlabarda(		"Alabarda de Guan Yu"),
	ItemEspada(			"Espada Longa de Ullrich"),
	ItemMachado(		"Machado de Guerra de Thogard"),
	ItemArco(			"Arco Longo de Nimrod"),
	ItemTelescopio(		"Telescópio de Kalid"),
	ItemLanca(			"Lança de Miesko"),
	ItemEstandarte(		"Estandarte de Baptiste"),
	ItemArcoComposto(	"Arco Composto de Nimrod"),
	ItemEstrela(		"Estrela de Manhã de Carol"),
	ItemFogueira(		"Fogueira de Aletheia"),
	ItemCetro(			"Cetro de Vasco"),
	ItemNull(			"Nenhum Item"),
	
	DescripAlabarda(	"Aumenta ataque de lanceiros em %.0f%% e defesa em %.0f%%"),
	DescripEspada(		"Aumenta ataque de espadachins em %.0f%% e defesa em %.0f%%"),
	DescripMachado(		"Aumenta ataque de bárbaros em %.0f%% e defesa em %.0f%%"),
	DescripArco(		"Aumenta ataque de arqueiros em %.0f%% e defesa em %.0f%%"),
	DescripTelescopio(	"Exploradores sempre veem as tropas de uma aldeia"),
	DescripLanca(		"Aumenta ataque de cavalaria leve em %.0f%% e defesa em %.0f%%"),
	DescripEstandarte(	"Aumenta ataque de cavalaria pesada em %.0f%% e defesa em %.0f%%"),
	DescripArcoComposto("Aumenta ataque de arqueiro a cavalo em %.0f%% e defesa em %.0f%%"),
	DescripEstrela(		"Aumenta ataque de aríetes em %.0f%%"),
	DescripFogueira(	"Aumenta ataque de catapultas em %.0f%% e defesa em %.0f%%"),
	DescripCetro(		"Nobres reduzem a lealdade por, no mínimo, 30"),
	DescripItemNull(	""),

	// MODELOS
	
	NovoModelo(			"Novo Modelo"),
	
	// MUNDOS
	
	NovoMundo(			"Novo Mundo"),
	
	PropNomeName(		"Nome"),
	PropArqueiroName(	"Arqueiro"),
	PropMiliciaName(	"Milícia"),
	PropPaladinoName(	"Paladino"),
	PropItensName(		"Itens Aprimorados"),
	PropIgrejaName(		"Igreja"),
	PropMoralName(		"Moral"),
	PropBandeiraName(	"Bandeiras"),
	PropNoiteName(		"Bônus Noturno"),
	PropMoedasName(		"Cunhagem de Moedas"),
	PropVelocidadeName(	"Velocidade"),
	PropModificadorName("Modificar de Unidade"),
	
	PropPesquisaName(	"Sistema de Pesquisa"),
	PropPesquisaSimples("Pesquisa Simples"),
	PropPesquisaNiveis(	"Pesquisa de 3 Níveis"),
	
	PropAcademiaName(	"Preços crescentes para nobres"),
	PropAcademiaMoeda(	"Cunhagem de Moedas"),
	PropAcademiaNiveis(	"Academia de Níveis"),
	
	// PROPERTIES
	
	PropAtivado(		"Ativado"),
	PropDesativado(		"Desativado"),
	
	// UNIDADES
					// Português			Inglês
	Lanceiro(			"Lanceiro"),
	Espadachim(			"Espadachim"),
	Barbaro(			"Bárbaro"),
	Arqueiro(			"Arqueiro"),
	Explorador(			"Explorador"),
	CavaloLeve(			"Cavalaria Leve"),
	ArcoCavalo(			"Arqueiro a Cavalo"),
	CavaloPesado(		"Cavalaria Pesada"),
	Ariete(				"Aríete"),
	Catapulta(			"Catapulta"),
	Nobre(				"Nobre"),
	Milicia(			"Milícia"),
	
	// EDIFÍCIOS
					// Português	
	EdPrincipal(		"Edifício Principal"),
	Quartel(			"Quartel"),
	Estabulo(			"Estábulo"),
	Oficina(			"Oficina"),
	Igreja(				"Igreja"),
	PrimeiraIgreja(		"Primeira Igreja"),
	Academia(			"Academia"),
	Ferreiro(			"Ferreiro"),
	PracaReuniao(		"Praça de Reunião"),
	Estatua(			"Estátua"),
	Mercado(			"Mercado"),
	Bosque(				"Bosque"),
	PocoArgila(			"Poço de Argila"),
	MinaFerro(			"Mina de Ferro"),
	Fazenda(			"Fazenda"),
	Armazem(			"Armazém"),
	Esconderijo(		"Esconderijo"),
	Muralha(			"Muralha");
	
	String português, inglês;
	
	Lang(String português) {
		
		this.português = português;
	}
	
	public String toString() {
		
		return português;
		
	}
	
}
