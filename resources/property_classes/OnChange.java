package property_classes;

/**
 * Classe para ser passada pelo EditDialog como par�metro para todas as Property.
 * <br>Elas utilizam o m�todo <code>run</code> quando ocorre qualquer mudan�a em seus
 * valores.
 * 
 * @author Arthur
 *
 */
public abstract class OnChange {

	public abstract void run();
	
}
