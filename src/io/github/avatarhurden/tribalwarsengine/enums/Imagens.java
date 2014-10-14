package io.github.avatarhurden.tribalwarsengine.enums;

import java.awt.Image;
import java.awt.Toolkit;

public class Imagens {
	
	public static Image getImage(String name) {
		return Toolkit.getDefaultToolkit().getImage(
                Imagens.class.getResource("/images/" + name));
	}
	
}
