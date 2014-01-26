package custom_components;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JDialog;

import config.File_Manager;
import config.ModeloTropas_Reader;
import config.Mundo_Reader;
import database.ModeloTropas;

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
	
	public EditDialog(List<ModeloTropas> objects) throws IllegalArgumentException, IllegalAccessException {
		
		for (Object i : objects)
			i.toString();
		
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
	
	public static void main (String args[]) {
		
		File_Manager.read();
		
		File_Manager.defineMundos();
		
		Mundo_Reader.setMundoSelecionado(Mundo_Reader.getMundo(23));
		
		File_Manager.defineModelos();
		
		try {
			EditDialog test = new EditDialog(ModeloTropas_Reader.getListModelos());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
