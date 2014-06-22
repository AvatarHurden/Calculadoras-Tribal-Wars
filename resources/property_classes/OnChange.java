package property_classes;

/**
 * Classe para ser passada pelo EditDialog como parâmetro para todas as Property.
 * <br>Elas utilizam o método <code>run</code> quando ocorre qualquer mudança em seus
 * valores.
 * 
 * @author Arthur
 *
 */
public abstract class OnChange {

	public abstract void run();
	
}
