package simulador;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import config.MundoSelecionado;
import config.Mundo_Reader;
import database.Bandeira;
import database.Ferramenta;
import database.ItemPaladino;
import database.Unidade;
import database.Bandeira.CategoriaBandeira;

public class GUI_old extends Ferramenta {

	private JTextField txtSorte;
	
	private JTextField txtMoral;
	
	private JTextField txtMuralha;
	
	private JCheckBox checkReligiãoAtacante;
	
	private JCheckBox checkReligiãoDefensor;
	
	private Map<Unidade, BigDecimal> tropasAtacante;
	
	private Map<Unidade, BigDecimal> tropasDefensor;
	
	private ItemPaladino itemAtacante, itemDefensor;
	
	private Bandeira bandeiraAtacante, bandeiraDefensor;
	
	private boolean noite;
	
	public GUI_old(Map<Unidade, BigDecimal> tropasAtacante, Map<Unidade, BigDecimal> tropasDefensor, int moral, boolean noite, int muralha, int sorte,
			boolean religiãoAtacante, boolean religiãoDefensor, ItemPaladino itemAtacante, ItemPaladino itemDefensor, Bandeira bandeiraAtacante,
			Bandeira bandeiraDefensor) {
		
		txtMoral = new JTextField(String.valueOf(moral));
		
		txtMuralha = new JTextField(String.valueOf(muralha));
		
		txtSorte = new JTextField(String.valueOf(sorte));
		
		checkReligiãoAtacante = new JCheckBox();
		checkReligiãoAtacante.setSelected(religiãoAtacante);
		
		checkReligiãoDefensor = new JCheckBox();
		checkReligiãoDefensor.setSelected(religiãoDefensor);
		
		this.tropasAtacante = tropasAtacante;
		this.tropasDefensor = tropasDefensor;
		
		this.itemAtacante = itemAtacante;
		this.itemDefensor = itemDefensor;
		
		this.bandeiraAtacante = bandeiraAtacante;
		this.bandeiraDefensor = bandeiraDefensor;
		
		this.noite = noite;
		
//		Calculadora_de_perdas test = new Calculadora_de_perdas(this);
		
		Cálculo teste = new Cálculo(this);
	}
	
	/**
	 * @return the bandeiraAtacante
	 */
	public Bandeira getBandeiraAtacante() {
		return bandeiraAtacante;
	}

	/**
	 * @return the bandeiraDefensor
	 */
	public Bandeira getBandeiraDefensor() {
		return bandeiraDefensor;
	}

	/**
	 * @return sorte, do ponto de visto do atacante
	 */
	protected int getSorte() {
		return Integer.parseInt(txtSorte.getText());
	}
	
	/**
	 * @return moral das tropas atacantes
	 */
	protected int getMoral() {
		return Integer.parseInt(txtMoral.getText());
	}
	
	/**
	 * @return noite
	 */
	protected boolean getNoite() {
		return noite;
	}
	
	/**
	 * @return religião das tropas atacantes
	 */
	protected boolean getReligiãoAtacante() {
		return checkReligiãoAtacante.isSelected();
	}
	
	/**
	 * @return religião das tropas defensoras
	 */
	protected boolean getReligiãoDefensores() {
		return checkReligiãoDefensor.isSelected();
	}
	
	
	
	/**
	 * @return the itemAtacante
	 */
	public ItemPaladino getItemAtacante() {
		return itemAtacante;
	}

	/**
	 * @return the itemDefensor
	 */
	public ItemPaladino getItemDefensor() {
		return itemDefensor;
	}

	/**
	 * @return nível da muralha do defensor
	 */
	protected int getNívelMuralha() {
		return Integer.parseInt(txtMuralha.getText());
	}
	
	/**
	 * @return lista de tropas atacantes
	 */
	protected Map<Unidade, BigDecimal> getListaAtacante() {
		return tropasAtacante;
	}
	
	/**
	 * @return lista de tropas defensoras
	 */
	protected Map<Unidade, BigDecimal> getListaDefensor() {
		return tropasDefensor;
	}
	
	public static void main (String args[]) {
		
		Mundo_Reader.read();
		
		MundoSelecionado.setMundo(Mundo_Reader.getMundo(29));
		
		System.out.println(Mundo_Reader.getMundo(29).getNome());
		
		Map<Unidade, BigDecimal> tropasAtacante = new HashMap<Unidade, BigDecimal>();
		
		Map<Unidade, BigDecimal> tropasDefensor = new HashMap<Unidade, BigDecimal>();
		
		tropasAtacante.put(Unidade.LANCEIRO, new BigDecimal(0));
		tropasAtacante.put(Unidade.ESPADACHIM, new BigDecimal(0));
		tropasAtacante.put(Unidade.ARQUEIRO, new BigDecimal(100));
		tropasAtacante.put(Unidade.BÁRBARO, new BigDecimal(0));
		tropasAtacante.put(Unidade.EXPLORADOR, new BigDecimal(0));
		tropasAtacante.put(Unidade.CAVALOLEVE, new BigDecimal(0));
		tropasAtacante.put(Unidade.ARCOCAVALO, new BigDecimal(0));
		tropasAtacante.put(Unidade.CAVALOPESADO, new BigDecimal(0));
		tropasAtacante.put(Unidade.ARÍETE, new BigDecimal(0));
		tropasAtacante.put(Unidade.CATAPULTA, new BigDecimal(0));
		tropasAtacante.put(Unidade.PALADINO, new BigDecimal(1));
		tropasAtacante.put(Unidade.NOBRE, new BigDecimal(0));
		
		tropasDefensor.put(Unidade.LANCEIRO, new BigDecimal(100));
		tropasDefensor.put(Unidade.ESPADACHIM, new BigDecimal(0));
		tropasDefensor.put(Unidade.ARQUEIRO, new BigDecimal(0));
		tropasDefensor.put(Unidade.BÁRBARO, new BigDecimal(0));
		tropasDefensor.put(Unidade.EXPLORADOR, new BigDecimal(0));
		tropasDefensor.put(Unidade.CAVALOLEVE, new BigDecimal(0));
		tropasDefensor.put(Unidade.ARCOCAVALO, new BigDecimal(0));
		tropasDefensor.put(Unidade.CAVALOPESADO, new BigDecimal(0));
		tropasDefensor.put(Unidade.ARÍETE, new BigDecimal(0));
		tropasDefensor.put(Unidade.CATAPULTA, new BigDecimal(0));
		tropasDefensor.put(Unidade.PALADINO, new BigDecimal(0));
		tropasDefensor.put(Unidade.NOBRE, new BigDecimal(0));
		tropasDefensor.put(Unidade.MILÍCIA, new BigDecimal(0));
		
		int muralha = 20;
		
		int moral = 100;
		
		boolean religiãoAtacante = true;
		boolean religiãoDefensor = true;
		
		ItemPaladino itemAtacante = ItemPaladino.NULL;
		ItemPaladino itemDefensor = ItemPaladino.NULL;
		
		Bandeira bandeiraAtacante = new Bandeira(CategoriaBandeira.ATAQUE, 8);
		Bandeira bandeiraDefensor = new Bandeira(CategoriaBandeira.NULL, 8);
		
//		Bandeira bandeiraAtacante = null;
//		Bandeira bandeiraDefensor = null;
		
		int sorte = 0	;
		
		boolean noite = false;
		
		GUI_old test = new GUI_old(tropasAtacante, tropasDefensor, moral, noite, muralha, sorte, religiãoAtacante, religiãoDefensor, itemAtacante, itemDefensor
				, bandeiraAtacante, bandeiraDefensor);
		
		
		
	}
	
}
