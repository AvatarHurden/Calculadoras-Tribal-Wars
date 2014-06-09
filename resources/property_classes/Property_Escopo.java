package property_classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import config.Mundo_Reader;
import database.Mundo;

/**
 * O escopo do objeto, podendo ser global ou apenas um (ou mais) mundos.
 * 
 * @author Arthur
 *
 */
public class Property_Escopo implements Property {
	
	private List<Mundo> mundos = new ArrayList<Mundo>();
	
	public Property_Escopo(List<Mundo> mundos) {
		
		// Remove todos os mundos que não foram encontrados
		for (Mundo i : mundos)
			if (i != null)
				this.mundos.add(i);
				
	}
	
	public JPanel makeEditDialogPanel(JPanel panel, OnChange change) {
		
		
		return panel;
	}

	/**
	 * Não se aplica ao escopo
	 */
	public String getName() {
		return null;
	}

	
	public String getValueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retorna a lista de mundos no qual está disponível.
	 * Caso seja de alcance global, a lista será todos os mundos
	 */
	public List<Mundo> getListMundos() {

		return mundos;
		
	}

}
