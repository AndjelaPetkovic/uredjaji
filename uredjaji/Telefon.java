package uredjaji;

public class Telefon extends Artikal {
	
	private boolean naPretplatu;

	public Telefon(String naziv, float cena, boolean naPretplatu) {
		super(naziv, cena);
		this.naPretplatu = naPretplatu;
	}

	@Override
	public float prodajnaCena() {
		float cena = (this.naPretplatu) ? 1 : getCena();
		return cena;
	}
	
	public static boolean pretplata(String pretplataS) {
		
		boolean pretplata = (pretplataS.equals("DA")) ? true : false;
 		return pretplata;		
	}

	@Override
	public String toString() {
		return super.toString() + " prodajna cena: " + prodajnaCena() + " rsd";
	}
	

}
