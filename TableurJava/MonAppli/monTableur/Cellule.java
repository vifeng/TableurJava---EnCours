package monTableur;

import java.util.ArrayList;

import igtableur.ErreurFormule;

public class Cellule {
	private String coordonnees;
	protected String inputIg;
	private Cellule celluleReferante;

	public Cellule(String coordonnees, String inputIg) {
		this.coordonnees = coordonnees;
		this.inputIg = inputIg;
		celluleReferante = null;
	}

	public String getInputIg() {
		return inputIg;
	}

	public Cellule getCelluleReferante() {
		return celluleReferante;
	}

	public void setCelluleReferante(Cellule suivante) {
		celluleReferante = suivante;
	}

	public boolean celluleEstNumerique() {
		String s = this.getClass().getName();
		if (s == "monTableur.CelluleNumerique" || s == "monTableur.ConstanteNumerique" || s == "monTableur.Formule")
			return true;
		else
			return false;
	}

	/**
	 * Vérifie que ce ne soit pas une référence à une cellule numérique qui
	 * fasse appel à elle même. Prends en compte les formules imbriquées.
	 * 
	 * @param ordonnee
	 * @param abscisse
	 * @return true si la référence est circulaire false si elle ne l'est pas
	 * @throws ErreurFormule
	 */
	public boolean verifRefCirculaireNumerique(String celluleAcreer){
		System.out.println("passe celluleReferante " + this.coordonnees);
		if (celluleReferante == null) {
			System.out.println("passe null");
			return false;
		}
		else if (celluleAcreer.equals(this.coordonnees)) {
			return true;
		} else {
			return this.celluleReferante.verifRefCirculaireNumerique(celluleAcreer);
		}
	}

	/**
	 * Vérifie que ce ne soit pas une référence à une Cellule Label (non
	 * numérique => texte) qui fasse appel à elle même
	 * 
	 * @param ordonnee
	 * @param abscisse
	 * @return true si la référence est circulaire false si elle ne l'est pas
	 * @throws ErreurFormule
	 */
	public boolean verifRefCirculaireString(String celluleAcreer) {
		// FIXME A ECRIRE selon verifRefCirculaireNumerique(String celluleAcreer)
		if (celluleReferante == null) {
			System.out.println("passe null");
			return false;
		}
		else if (celluleAcreer.equals(this.coordonnees)) {
			return true;
		} else {
			return this.celluleReferante.verifRefCirculaireNumerique(celluleAcreer);
		}
	}

	

}
