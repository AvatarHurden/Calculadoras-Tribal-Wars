package custom_components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * JTextField that formats the number to add thousands separator
 * 
 * <br>Abstract method is called on every change of text
 * 
 * <br>By default, contains: 
 * <br>- Left horizontal text alignment
 * <br>- PlainDocument to only allow <code>length</code> digits
 * <br>- DocumentListener with abstract clas that activates on every change
 * <br>- FocusListener to update when focus is lost (to not add another, since
 * focus is lost on every edit of the textField)
 * 
 * @author Arthur
 * 
 */
@SuppressWarnings("serial")
public abstract class TroopFormattedTextField extends JTextField {

	// Location on format
	// TODO make location changeable, depending on language;
	public NumberFormat numberFormat = NumberFormat
			.getNumberInstance(Locale.GERMANY);

	/**
	 * @param length
	 *            Maximum number of digits allowed
	 */
	public TroopFormattedTextField(final int length) {

		setHorizontalAlignment(SwingConstants.LEFT);

		setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de números e no máximo "length"
				// dígitos
				if ((getLength() + str.length()) <= length
						&& Character.isDigit(str.charAt(0))) {
					super.insertString(offset, str, attr);
				}

				if (getLength() > 3 && !str.contains("."))
					setText(getFormattedNumber(getUnformattedNumber(getText(0,
							getLength()))));

			}

			@Override
			public void removeUpdate(AbstractDocument.DefaultDocumentEvent chng) {

				setFocusable(false);
				setFocusable(true);
				requestFocus();

			}

		});

		getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				go();

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				go();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}

		});

		addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {

				if (!isFormatted(getText()))
					setText(getFormattedNumber(getUnformattedNumber(getText())));

			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		setColumns(6);

	}

	// Returns the unformatted value of a formatted string
	private String getUnformattedNumber(String input) {

		String unformated = "";
		try {
			unformated = numberFormat.parse(input).toString();
		} catch (ParseException e) {
		}

		return unformated;

	}

	// Returns the formatted value of an unformatted string
	private String getFormattedNumber(String input) {

		String formated = numberFormat.format(Integer.parseInt(input));

		return formated;

	}

	private boolean isFormatted(String input) {

		List<String> pieces = Arrays.asList(input.split("\\."));

		for (String s : pieces) {

			if (pieces.indexOf(s) != 0 && s.length() != 3)
				return false;

		}

		return true;

	}

	public abstract void go();

	/**
	 * @return BigDecimal the value of the text
	 */
	public BigDecimal getValue() {
		try {
			return new BigDecimal(getUnformattedNumber(getText()));
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

}
