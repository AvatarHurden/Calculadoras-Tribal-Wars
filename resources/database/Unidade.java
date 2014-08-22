package database;

import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;

public enum Unidade {

	//  			Nome				tipo            		  mad	 arg      fer    pop  atk   dg    dc    da    mov   saq  tempo
	LANCEIRO	  ("Lanceiro", 			UnidadeTipo.Geral,        50,    30,	  10,	 1,	  10,   15,   45,   20,	  18,   25,  680),
	ESPADACHIM	  ("Espadachim",		UnidadeTipo.Geral,        30,    30,	  70,	 1,	  25,   50,   15,   40,	  22,   15,  1000),
	BÁRBARO		  ("Bárbaro", 			UnidadeTipo.Geral,        60,    30,	  40,	 1,	  40,   10,   5,    10,	  18,   10,  880),
	ARQUEIRO	  ("Arqueiro",			UnidadeTipo.Arqueiro,     100,   30,	  60,	 1,	  15,   50,   40,   5, 	  18,   10,  1200),
	EXPLORADOR	  ("Explorador",		UnidadeTipo.unspecified,  50,    50,	  20,	 2,	  0,	2,    1,    2,    9,    0,   600),
	CAVALOLEVE	  ("Cavalaria Leve",	UnidadeTipo.Cavalo,       125,   100,	  250,	 4,	  130,  30,   40,   30,   10,   80,  1200),
	ARCOCAVALO	  ("Arqueiro a Cavalo", UnidadeTipo.Arqueiro,     250,   100,	  150,	 5,	  120,  40,   30,   50,   10,   50,  1800),
	CAVALOPESADO  ("Cavalaria Pesada",  UnidadeTipo.Cavalo,       200,   150,	  600,	 6,	  150,  200,  80,   180,  11,   50,  2400),
	ARÍETE		  ("Aríete", 			UnidadeTipo.Geral,        300,   200,	  200,	 5,	  2,	20,   50,   20,   30,   0,   3200),
	CATAPULTA	  ("Catapulta", 		UnidadeTipo.Geral,        320,   400,	  100,	 8,	  100,  100,  50,   100,  30,   0,   4800),
	PALADINO	  ("Paladino", 			UnidadeTipo.Cavalo,       20,    20,	  40,	 10,  150,  250,  400,  150,  10,   100, 21600),
	NOBRE         ("Nobre", 		    UnidadeTipo.Geral,        40000, 50000,  50000, 100, 30,   100,  50,   100,  35,   0,   12000),
	MILÍCIA		  ("Milícia", 			UnidadeTipo.unspecified,  0,     0,	  0,	 0,	  0,	15,   45,   25,   0, 0,   0);


	private final static double lvl2 = 1.25;
	private final static double lvl3 = 1.4;

	private final String nome;
	private final UnidadeTipo type;
	private final int madeira;
	private final int argila;
	private final int ferro;
	private final int população;
	private final int ataque;
	private final int defGeral;
	private final int defCav;
	private final int defArq;
	private final double velocidade;
	private final int saque;
	private final int tempoProdução;

	public enum UnidadeTipo {
		Geral, Arqueiro, Cavalo, unspecified;
	}

	/**
	 * @param nome
	 *            da unidade
	 * @param custo
	 *            de madeira
	 * @param custo
	 *            de argila
	 * @param custo
	 *            de ferro
	 * @param população
	 *            utilizada
	 * @param força
	 *            de ataque
	 * @param defesa
	 *            geral
	 * @param defesa
	 *            cavalaria
	 * @param defesa
	 *            arqueiros
	 * @param velocidade
	 *            base
	 * @param saque
	 * @param tempo
	 *            de produção em segundos (100%)
	 */
	private Unidade(String nome, UnidadeTipo tipo, int madeira, int argila, int ferro,
			int população, int ataque, int defGeral, int defCav, int defArq,
			double velocidade, int saque, int tempoProdução) {
		this.nome = nome;
		this.type = tipo;
		this.madeira = madeira;
		this.argila = argila;
		this.ferro = ferro;
		this.população = população;
		this.ataque = ataque;
		this.defGeral = defGeral;
		// Def cavalo
		this.defArq = defArq;
		this.velocidade = velocidade;
		this.saque = saque;
		this.tempoProdução = tempoProdução;

		// Alterações para unidades específicas
		// As características flutuantes são adicionadas depois, visto que são
		// final

		if (!WorldManager.get().getSelectedWorld().isArcherWorld()
				&& nome.equals("Espadachim"))
			this.defCav = 25;
		else
			this.defCav = defCav;
	}
	
	public String toString() {
		return nome;
	}

	/**
	 * @return tipo da unidade, na classe "Type"
	 */
	public UnidadeTipo getType() {
		return type;
	}

	/**
	 * @return custo de madeira da unidade
	 */
	public int getMadeira() {
		return madeira;
	}

	/**
	 * @return custo de argila da unidade
	 */
	public int getArgila() {
		return argila;
	}

	/**
	 * @return custo de ferro da unidade
	 */
	public int getFerro() {
		return ferro;
	}

	/**
	 * @return custo de população da unidade
	 */
	public int getPopulação() {
		return população;
	}

	/**
	 * @return força de ataque da unidade
	 */
	public int getAtaque() {
		return getAtaque(1, ItemPaladino.NULL);
	}

	public int getAtaque(int nível) {
		return getAtaque(nível, ItemPaladino.NULL);
	}

	public int getAtaque(int nível, ItemPaladino item) {

		if (item.getUnit() == this)
			return (int) Math.round(upgrade(nível, ataque) * item.getModifierAtk());
		else
			return upgrade(nível, ataque);

	}

	/**
	 * @return defesa geral da unidade
	 */
	public int getDefGeral() {
		return getDefGeral(1, ItemPaladino.NULL);
	}

	public int getDefGeral(int nível) {
		return getDefGeral(nível, ItemPaladino.NULL);
	}

	public int getDefGeral(int nível, ItemPaladino item) {

		if (item.getUnit() == this)
			return (int) Math.round(upgrade(nível, defGeral) * item.getModifierDef());
		else
			return upgrade(nível, defGeral);

	}

	/**
	 * @return defesa de cavalaria da unidade
	 */
	public int getDefCav() {
		return getDefCav(1, ItemPaladino.NULL);
	}

	public int getDefCav(int nível) {
		return getDefCav(nível, ItemPaladino.NULL);
	}

	public int getDefCav(int nível, ItemPaladino item) {

		if (item.getUnit() == this)
			return (int) Math.round(upgrade(nível, defCav) * item.getModifierDef());
		else
			return upgrade(nível, defCav);

	}

	/**
	 * @return defesa de arqueiro da unidade
	 */
	public int getDefArq() {
		return getDefArq(1, ItemPaladino.NULL);
	}

	public int getDefArq(int nível) {
		return getDefArq(nível, ItemPaladino.NULL);
	}

	public int getDefArq(int nível, ItemPaladino item) {

		if (item.getUnit() == this)
			return (int) Math.round(upgrade(nível, defArq) * item.getModifierDef());
		else
			return upgrade(nível, defArq);

	}

	/**
	 * @return velocidade base da unidade, em minutos/campo
	 */
	public double getVelocidade() {
		return velocidade;
	}

	/**
	 * @return capacidade de saque da unidade
	 */
	public int getSaque() {
		return saque;
	}

	/**
	 * @return tempo de produção da unidade em segundos (100%)
	 */
	public int getTempoDeProdução() {
		return tempoProdução;
	}

	/**
	 * @return nome da unidade, com primeira letra da cada palavra maiúscula
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Aumenta a quantidade dada pela razão do nível fornecido. Se o nível for
	 * diferente de 2 ou 3, retorna o valor dado
	 */
	private int upgrade(int lvl, int value) {

		double total = value;
		
		if (WorldManager.get().getSelectedWorld().getResearchSystem().getResearch() == 3) {
			if (lvl == 2)
				total = total * lvl2;
			else if (lvl == 3)
				total = total * lvl3;
			else
				return value;
		} else if (WorldManager.get().getSelectedWorld().getResearchSystem().getResearch() == 10)
			total = total * Math.pow(1.04605, lvl-1);

		return (int) Math.round(total);

	}

}
