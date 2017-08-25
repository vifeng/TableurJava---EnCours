package monTableur;

import java.io.IOException;

import igtableur.ErreurFormule;


public class Modele {
	/**
	 * Tableau de Cellules dont la taille correspond à celle de l'igt (interface
	 * graphique)
	 */
	
	// A passer en private une fois le raffraichissement réécris.
	public Cellule[][] tableurCell = new Cellule[10][10]; 
	
	public Cellule getCell(char col, int lig) {
		return tableurCell[correspondance(col)][lig];
	}

	public void setCell(char col, int lig, Cellule c) {
		tableurCell[correspondance(col)][lig] = c;
	}

	/**
	 * Donne la correspondance entre une lettre de l'alphabet et son numéro de
	 * colonne
	 * 
	 * @param col
	 *            colonne de la cellule (lettre)
	 * @return le numéro de la colonne
	 */
	public int correspondance(char col) {
		// de A à J <=> de 65 à 74 en ASCII
		return (int) ((int) col - 65);
	}

	/**
	 * Donne la correspondance entre un numéro de colonne et sa lettre
	 * 
	 * @param col
	 *            colonne de la cellule (lettre)
	 * @return le numéro de la colonne
	 */
	public char correspondanceInverse(int col) {
		// de 0 à 9 = de A à J (48 à 57 <=> 65 à 74 en ASCII)
		return (char) ((char) '0' + col + 17);
	}

	/**
	 * Vérifie si un string est une coordonnée du tableur
	 * 
	 * @param s
	 * @return true si c'est vrai
	 */
	public boolean verifRefCellExiste(String s) {
		int lettre = (int) s.charAt(0);
		int chiffre = (int) s.charAt(1);
		if (s != null && s.length() == 2 && lettre >= 'A' && lettre <= 'J' && chiffre >= '0' && chiffre <= '9')
			return true;
		return false;
	}

	
	
	
	// A SUPPRIMER UNE FOIS LE RAFFRAICHISSEMENT REECRIS
	
	
	
	
	
	/**
	 * Fais une copie de la matrice de Cellule passé en paramètre
	 * 
	 * @param mat
	 * @return
	 */
	public Cellule[][] copie() {
		Cellule[][] copie = new Cellule[tableurCell.length][tableurCell.length];

		for (int i = 0; i <= tableurCell.length - 1; i++) {
			for (int j = 0; j <= tableurCell[i].length - 1; j++) {
				copie[i][j] = tableurCell[i][j];
			}
		}
		return copie;
	}

	/**
	 * Compare deux matrices de Cellules passé en paramètre et renvoi un boolean
	 * 
	 * @param mat
	 * @param mat2
	 * @return faux si les deux matrices ne sont pas égales vrai si égales
	 * @throws ErreurFormule
	 */
	public boolean compare(Cellule[][] mat, Cellule[][] mat2) throws ErreurFormule {

		for (int i = 0; i <= mat.length - 1; i++) {
			for (int j = 0; j <= mat[i].length - 1; j++) {
				if (mat[i][j] != null && mat2[i][j] != null) {
					if (mat[i][j].celluleEstNumerique()) {
						if (((CelluleNumerique) mat[i][j]).val() != (((CelluleNumerique) mat2[i][j]).val())) {
							return false;
						}
					} else {
						if (!mat[i][j].toString().equals(mat2[i][j].toString())) {
							return false;
						}
					}
				} else if (mat[i][j] == null && mat2[i][j] != null || mat[i][j] != null && mat2[i][j] == null)
					return false;
			}
		}
		return true;
	}

	


}
