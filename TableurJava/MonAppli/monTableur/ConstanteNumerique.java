package monTableur;

public class ConstanteNumerique extends Cellule implements CelluleNumerique{
	private double cons;

	public ConstanteNumerique(String coordonnees, String inputIg, double c) {
		super(coordonnees, inputIg);
		cons = c;
	}

	public double val() {
		return cons;
	}

	public String toString() {
		return "" + cons;
	}

}
