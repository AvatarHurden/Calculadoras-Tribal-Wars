package custom_components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.File_Manager;
import config.ModeloTropas_Reader;
import config.Mundo_Reader;
import database.Cores;

/**
 * Dialog that contains info to edit a list (Mundos and ModeloTropas)
 * 
 * 
 * 
 * @author Arthur
 *
 */
public class EditDialog extends JDialog {
	
	// Pass the list of objects as one parameter: they will be used for the name and to be
	// edited
	
	// Pass another list with the properties of every object: this will be used to make the
	// right panel
	// OR
	// Interpret the varialbes for every object, creating the right panel according to it
	
	List<Object> objects;

	List<Properties> propertiesList;
	
	Map<Object, JComponent> mapping;
	
	//TODO create an interface called variable. WIthin it, different classes with types
	// that I want (2-choices, boolean, map, etc). Have each class contain a list of
	// all the variables that they have, using that as a parameter for this dialog;
	
	public EditDialog(List objects) throws IllegalArgumentException, IllegalAccessException {
		
		for (Object i : objects)
			System.out.println(i.toString());
		
		Field[] field = objects.get(0).getClass().getDeclaredFields();
		
		System.out.println(field.length);
		
		
		for (Field t : field) {
			System.out.println(t.getName()+" "+t.getType());
			if (t.getType().equals(Map.class)) {
				System.out.println("it is a boolean");
				for (Entry<Object, Object> x : ((Map<Object, Object>) t.get(objects.get(0))).entrySet())
					System.out.println(x.getKey()+": "+x.getValue());
			}
			
		}
		
	}
	
	private JPanel makeFieldPanel(Field field) {
		
		JPanel panel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {100, 120};
		layout.rowHeights = new int[] {20};
		layout.columnWeights = new double[]{0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(layout);

		panel.setOpaque(false);
		panel.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		
		GridBagConstraints c = new GridBagConstraints();
		
		if (field.getType().equals(Map.class)) {
			
			// Mapping the entries to the first object, since only the name will be used
			for (Entry<Object, Object> x : ((Map<Object, Object>) field.get(objects.get(0))).entrySet()) {
				
				
				
				
			}
			
		}
		
		if (field.getType().equals(boolean.class)) {
			
			
			
			GridBagConstraints nameC = new GridBagConstraints();
			nameC.insets = new Insets(5,5,5,5);
			nameC.gridy = 0;
			nameC.gridx = 0;
			nameC.anchor = GridBagConstraints.WEST;
			
			panel.add(new JLabel("Nome"), nameC);
			
			if (modelo.getNome() != null)
				name.setText(modelo.getNome());
			
			nameC.anchor = GridBagConstraints.EAST;
			nameC.gridy++;
			nameC.gridwidth = 2;
			panel.add(name, nameC);

			
		}
		
		return panel;
	}
	
	public static void main (String args[]) {
		
		File_Manager.read();
		
		File_Manager.defineMundos();
		
		Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundo(23));
		
		File_Manager.defineModelos();
		
		try {
			EditDialog test = new EditDialog(Mundo_Reader.getMundoList());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
