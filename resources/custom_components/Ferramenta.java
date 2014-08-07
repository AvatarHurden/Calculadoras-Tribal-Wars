package custom_components;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

import database.Cores;
import frames.MainWindow;

@SuppressWarnings("serial")
/**
 * Adiciona a tab para a inserção na MainWindow
 */
public class Ferramenta extends JPanel{

	private JPanel tab;
	
	private MainWindow frame = MainWindow.getInstance();
	
	protected ToolPanel tools;
	
	private boolean corEscuraUsada;
	
	public Ferramenta() {}
	
	public Ferramenta(String nome) {
		
		setBackground(Cores.FUNDO_CLARO);

		tools = new ToolPanel();
		
//		setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		
		tab = new JPanel();
		tab.add(new JLabel(nome));
		
		tab.setBackground(Cores.SEPARAR_CLARO);
		tab.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		
		tab.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {}
			
			public void mousePressed(MouseEvent arg0) {}
			
			public void mouseExited(MouseEvent arg0) {}
			
			public void mouseEntered(MouseEvent arg0) {}
			
			public void mouseClicked(MouseEvent arg0) {
				
				changeSelection();
				
			}
		});
		
	}
	
	// Methods to be used by the tool section
	
	public void changeSelection() {
		
		tools.refresh();
		
		frame.ferramentaSelecionada.setSelected(false);
		
		setSelected(true);
		frame.ferramentaSelecionada = Ferramenta.this;
		
		frame.pack();
		
	}
	
	public void setSelected(boolean isSelected) {
		
		if (isSelected) {
			setVisible(true);
			tab.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			tab.setBackground(Cores.FUNDO_ESCURO);
		} else {
			setVisible(false);
			tab.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
			tab.setBackground(Cores.FUNDO_CLARO);
		}
	}
	
	public boolean isSelected() {
		
		return isVisible();
		
	}
	
	public JPanel getTab() { return tab; }
	
	public void setFrame(MainWindow frame) {
		
		this.frame = frame;
		
	}
	
	// Methods to be used inside a "ferramenta"
	
	/**
	 * Retorna a cor oposta à utilizada no último PanelUnidade (CLARO ou ESCURO)
	 * @return Color não utilizada
	 */
	public Color getNextColor() {
		
		if (corEscuraUsada) {
			corEscuraUsada = false;
			return Cores.ALTERNAR_CLARO;
		} else {
			corEscuraUsada = true;
			return Cores.ALTERNAR_ESCURO;
		}
	}
	
}
