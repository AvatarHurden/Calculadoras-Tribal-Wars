package io.github.avatarhurden.tribalwarsengine.enums;

import java.awt.Image;

public enum Server {
	
	GERMANY("de", "http://www.die-staemme.de"),
	SWITZERLAND("ch", "http://www.staemme.ch"),
	INTERNATIONAL("en", "http://www.tribalwars.net"),
	NETHERLANDS("nl", "http://www.tribalwars.nl"),
	POLAND("pl", "http://www.plemiona.pl"),
	SWEDEN("se", "http://www.tribalwars.se"),
	BRAZIL("br", "http://www.tribalwars.com.br"),
	PORTUGAL("pt", "http://www.tribalwars.com.pt"),
	CHECK_REPUBLIC("cz", "http://www.divokekmeny.cz"),
	SOUTH_KOREA("kr", "http://www.bujokjeonjaeng.org"),
	ROMANIA("ro", "http://www.triburile.ro"),
	RUSSIA("ru", "http://www.voyna-plemyon.ru"),
	GREECE("gr", "http://www.fyletikesmaxes.gr"),
	NORWAY("no", "http://www.tribalwars.no.com"),
	SLOVAKIA("sk", "http://www.divoke-kmene.sk"),
	HUNGARY("hu", "http://www.klanhaboru.hu"),
	DENMARK("dk", "http://www.tribalwars.dk"),
	ITALY("it", "http://www.tribals.it"),
	TURKEY("tr", "http://www.klanla.org"),
	FRANCE("fr", "http://www.guerretribale.fr"),
	SPAIN("es", "http://www.guerrastribales.es"),
	FINLAND("fi", "http://www.tribalwars.fi"),
	UNITED_ARAB_EMIRATES("ae", "http://www.tribalwars.ae"),
	UNITED_KINGDOM("uk", "http://www.tribalwars.co.uk"),
	SLOVENIA("si", "http://www.vojnaplemen.si"),
	LITHUANIA("lt", "http://www.genciukarai.lt"),
	CROATIA("hr", "http://www.plemena.com.hr"),
	THAILAND("th", "http://www.tribalwars.asia"),
	BETA("zz", "http://www.beta.tribalwars.net"),
	UNITED_STATES("us", "http://www.tribalwars.us"),
	MASTERS("ts", "http://www.tribalwarsmasters.net");
//	BOSNIA_AND_HERZEGOVINA("ba", "http://www.plemena.net"),
//	ISRAEL("il", "http://www.tribal-wars.co.il"),
//	INDONESIA("id", "http://www.perangkaum.net"),
//	JAPAN("jp", "http://www.tribalwars.jp"),
	
	private String name;
	private String url;
	private Image flag;
	
	private Server(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String getName(){
		return name;
	}
	
	public String getURL() {
		return url;
	}
	
	public String getShortURL() {
		return url.replace("http://www.", "");
	}
	
	public Image getFlag() {
		if (flag == null)
			flag = Imagens.getImage("flag/" + name + ".png");
		return flag;
	}
	
	public static Server getServer(String name) {
		for (Server s : Server.values())
			if (s.getName().equals(name))
				return s;
		return null;
	}
	
	public String toString() {
		return name;
	}
}
