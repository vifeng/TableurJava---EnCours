package monTableur;

public class CelluleTexte extends Cellule{
	private String contenu;

	public CelluleTexte(String coordonnees, String inputIg, String contenu) {
		super(coordonnees, inputIg);
		this.contenu = contenu;
	}
	public String val() {
		return contenu;
	}
	@Override
	public String toString() {
		return contenu;
	}
	
}
