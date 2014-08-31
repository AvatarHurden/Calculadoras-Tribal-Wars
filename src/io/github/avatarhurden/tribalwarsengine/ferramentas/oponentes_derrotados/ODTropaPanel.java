package io.github.avatarhurden.tribalwarsengine.ferramentas.oponentes_derrotados;

import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Troop;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ODTropaPanel extends JPanel {

	private JLabel lblOD;

	/**
	 * @param Cor
	 *            que será pintado
	 * @param Unidade
	 *            que representa
	 */
	protected ODTropaPanel(Color cor) {
		setBackground(cor);
		createPanel();
	}

	protected void createPanel() {
		
		GridBagLayout layout = new GridBagLayout();
		layout.rowHeights = new int[]{ 30 };
        setLayout(layout);
		
		lblOD = new JLabel();
		add(lblOD);

	}

	protected void changeODAtaque(Object obj) {

		if (obj instanceof Army)
			changeODAtaqueArmy((Army) obj);
		else
			changeODAtaqueTropa((Troop) obj);
	}
	
	protected void changeODDefesa(Object obj) {

		if (obj instanceof Army)
			changeODDefesaArmy((Army) obj);
		else
			changeODDefesaTropa((Troop) obj);

	}
	
	private void changeODAtaqueArmy(Army army) {
		
		if (army.getPopulação() == 0) {
			resetValues();
			return;
		}
		
		lblOD.setText(getFormattedNumber(army.getODAtaque()));
	}
	
	private void changeODAtaqueTropa(Troop tropa) {
		
		if (tropa.getQuantity() == 0) {
			resetValues();
			return;
		}
		
		lblOD.setText(getFormattedNumber(tropa.getODAttacker()));
	}
	
	private void changeODDefesaArmy(Army army) {
		
		if (army.getPopulação() == 0) {
			resetValues();
			return;
		}
		
		lblOD.setText(getFormattedNumber(army.getODDefesa()));
	}
	
	private void changeODDefesaTropa(Troop tropa) {
		
		if (tropa.getQuantity() == 0) {
			resetValues();
			return;
		}
		
		lblOD.setText(getFormattedNumber(tropa.getODDefender()));
	}
	
	protected void resetValues() {
		lblOD.setText("");
	}
	
	private String getFormattedNumber(int input) {
		return NumberFormat.getNumberInstance(Locale.GERMANY).format(input);
	}

}
