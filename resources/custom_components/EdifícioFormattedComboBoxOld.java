package custom_components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import database.Cores;
import database.Edif�cio;

/**
 * {@link JComboBox} that only allows the maximum level of the edif�cio<br>
 * 
 * Abstract method is called on every change of text<br>
 * 
 * By default, contains: <br>
 * - Center horizontal text alignment <br>
 * - PlainDocument to only allow <code>2</code> digits<br>
 * - DocumentListener with abstract class that activates on every change<br>
 * - FocusListener to update when focus is lost
 * 
 * @author Arthur
 * 
 */
@SuppressWarnings("serial")
public abstract class Edif�cioFormattedComboBoxOld extends JTextField {

	/**
	 * @param Edif�cio ao qual a textField se refere
	 */
	public Edif�cioFormattedComboBoxOld(final Edif�cio ed, int initial) {

		setHorizontalAlignment(SwingConstants.CENTER);
		setDocument(new PlainDocument() {

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null)
					return;

				// Permite a entrada somente de n�meros e no m�ximo 3 d�gitos
				if ((getLength() + str.length()) <= 2
						&& (Character.isDigit(str.charAt(0))))
					super.insertString(offset, str, attr);
				
				if (Math.abs(Integer.parseInt(getText(0, getLength()))) > ed.n�velM�ximo()) {
					super.remove(0, getLength());
					super.insertString(0, String.valueOf(ed.n�velM�ximo()), attr);
				}

			}
		});

		setOpaque(false);

		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

		// Make it centered
		ListCellRenderer<Object> renderer = new DefaultListCellRenderer();

		((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel) renderer).setOpaque(true);

		// remove button
//		setUI(new BasicComboBoxUI() {
//			@Override
//			protected JButton createArrowButton() {
//				return new JButton() {
//					@Override
//					public int getWidth() {
//						return 0;
//					}
//				};
//			}
//		});
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				go();

			}
		});

	}

	public abstract void go();
	
	public int getValue() {
		
		return Integer.parseInt(getText());
		
	}
	
}
