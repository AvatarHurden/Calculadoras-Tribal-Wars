package simulador;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import config.World_Reader;
import database.Ferramenta;
import database.MundoSelecionado;
import database.Unidade;

public class GUI extends Ferramenta {

	private JTextField txtSorte;
	
	private JTextField txtMoral;
	
	private JTextField txtMuralha;
	
	private JCheckBox checkReligiãoAtacante;
	
	private JCheckBox checkReligiãoDefensor;
	
	private Map<Unidade, BigDecimal> tropasAtacante;
	
	private Map<Unidade, BigDecimal> tropasDefensor;
	
	public GUI(Map<Unidade, BigDecimal> tropasAtacante, Map<Unidade, BigDecimal> tropasDefensor, int moral, int muralha, int sorte,
			boolean religiãoAtacante, boolean religiãoDefensor) {
		
		txtMoral = new JTextField(String.valueOf(moral));
		
		txtMuralha = new JTextField(String.valueOf(muralha));
		
		txtSorte = new JTextField(String.valueOf(sorte));
		
		checkReligiãoAtacante = new JCheckBox();
		checkReligiãoAtacante.setSelected(religiãoAtacante);
		
		checkReligiãoDefensor = new JCheckBox();
		checkReligiãoDefensor.setSelected(religiãoDefensor);
		
		this.tropasAtacante = tropasAtacante;
		this.tropasDefensor = tropasDefensor;
		
//		Calculadora_de_perdas test = new Calculadora_de_perdas(this);
		
		Cálculo teste = new Cálculo(this);
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
		
		World_Reader.read();
		
		MundoSelecionado.setMundo(World_Reader.getMundo(0));
		
		Map<Unidade, BigDecimal> tropasAtacante = new HashMap<Unidade, BigDecimal>();
		
		Map<Unidade, BigDecimal> tropasDefensor = new HashMap<Unidade, BigDecimal>();
		
		tropasAtacante.put(Unidade.LANCEIRO, new BigDecimal(0));
		tropasAtacante.put(Unidade.ESPADACHIM, new BigDecimal(0));
		tropasAtacante.put(Unidade.ARQUEIRO, new BigDecimal(0));
		tropasAtacante.put(Unidade.BÁRBARO, new BigDecimal(1000));
		tropasAtacante.put(Unidade.EXPLORADOR, new BigDecimal(0));
		tropasAtacante.put(Unidade.CAVALOLEVE, new BigDecimal(0));
		tropasAtacante.put(Unidade.ARCOCAVALO, new BigDecimal(0));
		tropasAtacante.put(Unidade.CAVALOPESADO, new BigDecimal(0));
		tropasAtacante.put(Unidade.ARÍETE, new BigDecimal(0));
		tropasAtacante.put(Unidade.CATAPULTA, new BigDecimal(0));
		tropasAtacante.put(Unidade.PALADINO, new BigDecimal(0));
		tropasAtacante.put(Unidade.NOBRE, new BigDecimal(0));
		
		tropasDefensor.put(Unidade.LANCEIRO, new BigDecimal(1000));
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
		
		int muralha = 0;
		
		int moral = 100;
		
		boolean religiãoAtacante = true;
		boolean religiãoDefensor = true;
		
		int sorte = -25;
		
		GUI test = new GUI(tropasAtacante, tropasDefensor, moral, muralha, sorte, religiãoAtacante, religiãoDefensor);
		
		
		
	}
	
}
