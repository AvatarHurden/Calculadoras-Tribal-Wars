package alertas;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

import database.Cores;

public class Editor extends JDialog{
	
	JTextField nome;
	
	public Editor() {
		
		getContentPane().setBackground(Cores.FUNDO_CLARO);
		setOpacity(1);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		// Adding name panel
		add(makeNamePanel(), c);
		
		c.gridy++;
		add(makeTipoPanel(), c);
		
	}
	
	private JPanel makeNamePanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		panel.add(new JLabel("Nome"));
		
		nome = new JTextField(16);
		panel.add(nome);
		
		return panel;
	}
	
	private JPanel makeTipoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		
		panel.add(new JLabel("Tipo"), c);
		
		JPanel geral = new JPanel();
		JPanel ataque = new JPanel();
		JPanel apoio = new JPanel();
		JPanel saque = new JPanel();
		
		geral.add(new JLabel("Geral"));
		ataque.add(new JLabel("Ataque"));
		apoio.add(new JLabel("Apoio"));
		saque.add(new JLabel("Saque"));
		
		final JPanel[] panels = new JPanel[] {geral, ataque, apoio, saque};
		
		for (JPanel p : panels) {
			p.setBackground(Cores.FUNDO_CLARO);
			p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		}
		
		MouseAdapter listener = new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				
				for (JPanel p : panels) {
					p.setBackground(Cores.FUNDO_CLARO);
					p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				}
					
				((JPanel) e.getSource()).setBackground(Cores.FUNDO_ESCURO);
				((JPanel) e.getSource()).setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				
			}
		};
		
		for (JPanel p : panels) {
			p.addMouseListener(listener);
			c.gridx++;
			panel.add(p, c);
		}
		
		return panel;
	}
	
	public static void main (String args[]) {
		
		Editor ed = new Editor();
		
		ed.setVisible(true);
		ed.pack();
		ed.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

}
