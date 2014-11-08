package io.github.avatarhurden.tribalwarsengine.enums;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Imagens {
	
	public static Image getImage(String name) {
		URL t = Imagens.class.getResource("/images/" + name);
		return Toolkit.getDefaultToolkit().getImage(t);
	}
	
}
