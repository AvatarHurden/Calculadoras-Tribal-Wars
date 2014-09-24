package io.github.avatarhurden.tribalwarsengine.enums;

import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.AlertasPanel;

import java.awt.Image;
import java.awt.Toolkit;

public enum Server {
	
	INTERNATIONAL("en", "tribalwars.net"),
	UNITED_KINGDOM("uk", "tribalwars.co.uk"),
	GERMANY("de", "die-staemme.de"),
	SWITZERLAND("ch", "staemme.ch"),
	("nl", "tribalwars.nl"),
	("pl", "plemiona.pl"),
	("se", "tribalwars.se"),
	BRAZIL("br", "tribalwars.com.br"),
	PORTUGAL("pt", "tribalwars.com.pt"),
	("cz", "divokekmeny"),
	ROMANIA("ro", "triburile.ro"),
	RUSSIA("ru", "voyna-plemyon.ru"),
	GREECE("gr", "fyletikesmaxes.gr"),
	NORWAY("no", "tribalwars.no.com"),
	("sk", "divoke-kmene.sk"),
	("hu", "klanhaboru.hu"),
	("dk", "tribalwars.dk"),
	("ba", "plemena.net"),
	ITALY("it", "tribals.it"),
	("tr", "klanla.org"),
	FRANCE("fr", "guerretribale.fr"),
	SPAIN("es", "guerrastribales.es"),
	FINLAND("fi", "tribalwars.fi"),
	("ae", "tribalwars.ae"),
	("si", "vojnaplemen.si"),
	("lt", "genciukarai.lt"),
	("il", "tribal-wars.co.il"),
	("hr", "plemena.com.hr"),
	("id", "perangkaum.net"),
	JAPAN("jp", "tribalwars.jp"),
	("th", "tribalwars.asia"),
	UNITED_STATES("us", "tribalwars.us"),
	BETA("zz", "beta.tribalwars.net"),
	MASTERS("ts", "tribalwarsmasters.net");
	
	private String name;
	private String url;
	
	private Server(String name, String url) {
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getURL() {
		return url;
	}
	
	public Image getFlag() {
		return Toolkit.getDefaultToolkit().getImage(
                AlertasPanel.class.getResource("/images/flag_" + name + ".png"));
	}
}
