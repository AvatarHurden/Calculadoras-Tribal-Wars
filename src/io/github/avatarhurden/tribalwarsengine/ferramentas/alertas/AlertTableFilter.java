package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class AlertTableFilter extends JPanel{

	private JScrollPane tablePane;
	private TWSimpleButton filtrar;
	private JPanel filtersPanel;
	
	private JTextField name;
	private JCheckBox geral, apoio, ataque, saque;
	private JCheckBox selectedWorld;
	
	public AlertTableFilter(JScrollPane pane) {
		setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		setBackground(Cores.FUNDO_CLARO);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		filtersPanel = makeFilterPanel();
		filtersPanel.setVisible(false);
		tablePane = pane;
		
		add(makeSwitchPanel());
		add(filtersPanel);
	}
	
	private JPanel makeSwitchPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		filtrar = new TWSimpleButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                AlertasPanel.class.getResource("/images/up_arrow.png"))));
		
		filtrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int width = tablePane.getPreferredSize().width;
				int height = tablePane.getPreferredSize().height;
				int diff = filtersPanel.getPreferredSize().height;
				
				if (!filtersPanel.isVisible()) {
					filtersPanel.setVisible(true);
					filtrar.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                AlertasPanel.class.getResource("/images/down_arrow.png"))));
					tablePane.setPreferredSize(new Dimension(width, height - diff));
				} else {
					filtersPanel.setVisible(false);
					filtrar.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			                AlertasPanel.class.getResource("/images/up_arrow.png"))));
					tablePane.setPreferredSize(new Dimension(width, height + diff));
				}
				
			}
		});
		
		panel.add(filtrar);
		panel.add(new JLabel("Filtros"));
		
		return panel;
	}
	
	private JPanel makeFilterPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		panel.add(makeNamePanel(), c);
		
		c.gridx++;
		panel.add(makeTipoPanel(), c);
		
		c.gridx = 0;
		c.gridy++;
		panel.add(makeSelectedWorldPanel(), c);
		
		return panel;
	}
	
	private JPanel makeNamePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Nome"));
		
		name = new JTextField(15);
		panel.add(name);
		
		return panel;
	}
	
	private JPanel makeTipoPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Tipo"));
	
		geral = new JCheckBox("Geral");
		geral.setOpaque(false);
		ataque = new JCheckBox("Ataque");
		ataque.setOpaque(false);
		apoio = new JCheckBox("Apoio");
		apoio.setOpaque(false);
		saque = new JCheckBox("Saque");
		saque.setOpaque(false);
		
		panel.add(geral);
		panel.add(ataque);
		panel.add(apoio);
		panel.add(saque);
		
		return panel;
	}
	
	private JPanel makeSelectedWorldPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_CLARO), "Mundos"));
		
		selectedWorld = new JCheckBox("Apenas o mundo atual");
		selectedWorld.setOpaque(false);
		
		panel.add(selectedWorld);
		
		return panel;
	}
	
	private boolean isSelectedType(Tipo tipo) {
		if (!geral.isSelected() && !apoio.isSelected() && !ataque.isSelected() && !saque.isSelected())
			return true;
		
		else switch (tipo) {
			case Geral:
				return geral.isSelected();
			case Ataque:
				return ataque.isSelected();
			case Apoio:
				return apoio.isSelected();
			case Saque:
				return saque.isSelected();
			default:
				return true;
		}
	}
	
	private boolean isName(String name) {
		return true;
	}
	
	public boolean include(Alert a) {
		return isSelectedType(a.getTipo()) && isName(a.getNome());	
	}
	
}