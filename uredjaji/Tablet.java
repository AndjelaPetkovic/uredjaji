package uredjaji;

public class Tablet extends Artikal {
	
	private int odobrenPopust;

	public Tablet(String naziv, float cena, int odobrenPopust) {
		super(naziv, cena);
		this.odobrenPopust = odobrenPopust;
	}

	@Override
	public float prodajnaCena() {
		float cena = getCena() - (getCena()*(this.odobrenPopust/100.0f));
		return cena;
	}

	@Override
	public String toString() {
		return super.toString() + " prodajna cena: " + prodajnaCena() + " rsd";
	}
	
	

}
