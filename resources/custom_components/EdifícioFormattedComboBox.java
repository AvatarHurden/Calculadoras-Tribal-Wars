package custom_components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import database.Cores;
import database.Edifício;

/**
 * {@link JComboBox} that only allows the maximum level of the edifício<br>
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
public abstract class EdifícioFormattedComboBox extends JComboBox<Integer> {

	/**
	 * @param Edifício
	 *            ao qual a textField se refere
	 */
	public EdifícioFormattedComboBox(final Edifício ed, int initial, Color cor) {

		for (int i = 0; i < ed.nívelMáximo() + 1; i++)
			addItem(i);

		setSelectedItem(initial);

		setOpaque(true);

		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));

		// Make it centered
		ListCellRenderer<Object> renderer = new DefaultListCellRenderer();

		((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel) renderer).setOpaque(true);

		setRenderer(renderer);

		// remove button
		setUI(new BasicComboBoxUI() {
			@Override
			protected JButton createArrowButton() {
				return new JButton() {
					@Override
					public int getWidth() {
						return 0;
					}
				};
			}
		});

		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				go();

			}
		});

	}

	public abstract void go();

}
