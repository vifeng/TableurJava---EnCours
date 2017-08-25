package monTableur;

import java.io.IOException;
import igtableur.*;

/**
 * Class qui fait l'interface entre le programme et l'interface graphique
 */
public class Controleur implements Application {
	private IGTableur igt;
	Modele m;

	/**
	 * <pre>
	 * Constructeur
	 * Condition:
	 * 	doit prendre en paramètre un tableau de Cellule de la même taille que celui de l'igt
	 * </pre>
	 * 
	 * @param m
	 */
	public Controleur(Modele m) {
		this.m = m;
	}

	/*
	 * Méthodes de l'interface ______________
	 */
	/**
	 * Utilisee par l'IG pour afficher contenu de la cellule couramment
	 * selectionnee dans la fenetre d'edition des cellules. Retourne sous forme
	 * de chaine le contenu de la cellule de coordonnees:
	 * 
	 * @param col
	 *            colonne de la cellule (lettre)
	 * @param lig
	 *            no. ligne de la cellule (entier)
	 * @return chaine à afficher dans fenetre de dialogue.
	 */
	public String getContenu(char col, int lig) {
		if (m.getCell(col, lig) == null)
			return null;
		else
			return m.getCell(col, lig).getInputIg();
	}

	/**
	 * Chargee de modifier la cellule dans l'application metier ET de rafraichir
	 * l'IG. Doit analyser l'entree {@param s} puis, selon le cas:
	 * <ul>
	 * <li>creer/modifier la cellule dans l'application metier et rafraichir
	 * l'IG pour toutes les cellules pertinentes
	 * <li>ou signaler une erreur par une exception.
	 * </ul>
	 * 
	 * @param col
	 *            colonne de la cellule (lettre)
	 * @param lig
	 *            no. ligne de la cellule (entier)
	 * @param s
	 *            texte entre dans fenetre de dialogue
	 * @throws ErreurFormule
	 *             si @param s (entree) errone.
	 * @throws IOException
	 */
	public void setContenu(char col, int lig, String s) throws ErreurFormule {
		AnalyseSyntaxique ana = new AnalyseSyntaxique(m, col, lig, s);
		try {
			m.setCell(col, lig, ana.lireString());
			igt.modifieCellule(col, lig, m.getCell(col, lig).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		parcoursMaj();

	}

	/**
	 * Raffraichi l'interface graphique et le modèle(m Memoire) autant de fois
	 * que la réaction en chaine des opérations imbriquées ou des références à
	 * d'autres cellules le nécessite
	 * 
	 * @throws ErreurFormule
	 */
	void parcoursMaj() throws ErreurFormule {
		
		// TODO A REECRIRE (en utilisant Cellule.celluleReferante ) ET A METTRE DANS MODELE.
		
		Cellule[][] copie = m.copie();
		for (int i = 0; i <= m.tableurCell.length - 1; i++) {
			for (int j = 0; j <= m.tableurCell[i].length - 1; j++) {
				if (m.getCell(m.correspondanceInverse(i), j) != null) {
					AnalyseSyntaxique ana = new AnalyseSyntaxique(m, m.correspondanceInverse(i), j,
							m.getCell(m.correspondanceInverse(i), j).getInputIg());
					try {
						m.tableurCell[i][j] = ana.lireString();
						igt.modifieCellule(m.correspondanceInverse(i), j,
								m.getCell(m.correspondanceInverse(i), j).toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if ((m.compare(m.tableurCell, copie)) == false) {
			parcoursMaj();
		}
	}

	public void setInterface(IGTableur i) {
		igt = i;
	}

}
