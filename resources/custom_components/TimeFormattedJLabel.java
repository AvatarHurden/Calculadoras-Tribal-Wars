package custom_components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;

/**
 * A JLabel that receives a long in milliseconds, displaying the formatted time
 * @author Arthur
 *
 */
@SuppressWarnings("serial")
public class TimeFormattedJLabel extends JLabel {
	
	private boolean showMillis;
	
//	// This code for the Spinner
//	SpinnerDateModel model = new SpinnerDateModel();
//			
//	JSpinner date = new JSpinner(model);
//	date.setEditor(new JSpinner.DateEditor(date, "dd/MM/yyyy"));
	
	public TimeFormattedJLabel(boolean showMillis) {
		
		this.showMillis = showMillis;
		
	}
	
	/**
	 * Formats the time and shows it as days, hours, minutes, seconds. If <code>showMillis</code> is true
	 * , also shows millisseconds.
	 * @param value The time in milliseconds
	 */
	public void setTime(long value) {
		
		long d = TimeUnit.MILLISECONDS.toDays(value);
		long h = TimeUnit.MILLISECONDS.toHours(value-TimeUnit.DAYS.toMillis(d));
		long m = TimeUnit.MILLISECONDS.toMinutes(value-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h));
		long s = TimeUnit.MILLISECONDS.toSeconds(value-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h)-TimeUnit.MINUTES.toMillis(m));
		long ms = TimeUnit.MILLISECONDS.toSeconds(value-TimeUnit.DAYS.toMillis(d)-TimeUnit.HOURS.toMillis(h)-TimeUnit.MINUTES.toMillis(m)-TimeUnit.SECONDS.toMillis(s));
		
		if (d > 0)
			setText(String.format("%dd %02d:%02d:%02d", d, h, m, s));
		else
			setText(String.format("%02d:%02d:%02d", h, m, s));
		
		if (showMillis)
			setText(getText()+String.format("%03d", ms));
			
	}
	
	/**
	 * Formats the given date and shows it as dd//MM/yyyy HH:mm:ss.
	 * If <code>showMillis</code> is true, also shows milliseconds.
	 * @param date the Date object
	 */
	public void setDate(Date date) {
		
		if (showMillis)
			setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(date));
		else
			setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date));
		
	}
	
}
