package uredjaji;

import java.util.List;

public abstract class Artikal {
	
	private String naziv;
	private float cena;
	
	public Artikal(String naziv, float cena) {		
		this.naziv = naziv;
		this.cena = cena;
	}

	public String getNaziv() {
		return naziv;
	}

	public float getCena() {
		return cena;
	}

	@Override
	public String toString() {
		return naziv + " ** regularna cena: " + cena + " rsd ** ";
	}
	
	public abstract float prodajnaCena();
	
	public static String listajUredjaje(List<Artikal> uredjaji) {
		StringBuilder sb = new StringBuilder();
		for (Artikal uredjaj : uredjaji) {
			sb.append(uredjaj.toString() + "\n");
		}
		return sb.toString();
	}
}
