package custom_components;

import java.sql.Time;

/**
 * A JLabel that receives a long in milliseconds, displaying the formatted time
 * @author Arthur
 *
 */
public class TimeFormattedJLabel {

	private int days;
	private boolean showDays;
	
	public TimeFormattedJLabel(boolean showDays) {
		
		this.showDays = showDays;
		
	}
	
	public void setTime(long value) {
		
		Time time = new Time(value);
		
	}
	
}
