package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressStatus extends JPanel {

	private ProgressBar bar;
	private JLabel message;
	
	public ProgressStatus() {
		setOpaque(false);
		
		bar = new ProgressBar();
		message = new JLabel();
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		
		add(bar, c);
		c.gridy++;
		add(message, c);
	}
	
	public void setMessage(String s) {
		message.setText(s);
	}
	
	public void setProgress(double progress) {
		bar.setProgress(progress);
	}
	
	public void incrementProgress(double increment) {
		bar.setProgress(bar.progress + increment);
	}
	
	public void listIncrement(int counter, int size, double upper) {
		setProgress(bar.progress + (upper - bar.progress) * counter / size);
	}
	
	public void setSubProgressEnd(int end) {
		bar.isSub = true;
		bar.startSub = bar.progress;
		bar.endSub = end;
	}
	
	public void endSubProgress() {
		bar.isSub = false;
	}
	
	private class ProgressBar extends JPanel {
		
		private double progress;
		private double startSub, endSub;
		private boolean isSub = false;
		
		private ProgressBar() {
			setOpaque(false);

			setPreferredSize(new Dimension(150, 16));
		}
		
		@Override
		public void paint(Graphics g) {
			g.setColor(ProgressStatus.this.getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.GREEN.darker());
	        g.fillRect(0, getHeight()/2 - 2, (int) (getWidth() * progress/100.0) - 2, 8);

	        g.setColor(Cores.SEPARAR_ESCURO);
	        g.drawRect(0, getHeight()/2 - 2, getWidth() - 2, 8);
		}
		
		private void setProgress(double progress) {
			double prog = progress;
			if (isSub) {
				prog = startSub + (endSub - startSub) * progress / 100;
				if (prog >= endSub)
					isSub = false;
			}
			
			if (prog >= 0 && prog <= 100)
				this.progress = prog;
			repaint();
		}
	}
}
