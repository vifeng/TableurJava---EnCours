package monTableur;

import java.io.*;
import java.util.ArrayList;
import igtableur.ErreurFormule;

/**
 * utilisé dans l'interface graphique. prend un string et renvoi un découpage de
 * chaque mot retourne une création de Cellule avec le constructeur de la classe
 * Cellule adapté
 */
public class AnalyseSyntaxique {
	private StreamTokenizer tok;
	Reader monReader;
	Modele m;
	/**
	 * userInput est passé en pour récupérer la saisie d'origine quand on
	 * reclick sur la case
	 */
	String userInput;
	/**
	 * nouvelleCellCoord : Coordonnées de la cellule pour laquelle on fait le
	 * résultat syntaxique
	 */
	String nouvelleCellCoord;

	/**
	 * Constructeur crée un reader et garde en mémoire le string d'origine
	 * 
	 * @param s
	 */
	AnalyseSyntaxique(Modele m, char c, int l, String s) {
		userInput = s;
		monReader = new StringReader(userInput);
		this.m = m;
		nouvelleCellCoord = "" + c + l;
	}

	/**
	 * Analyse le string de saisi et pour chaque mot approprié retourne une
	 * création de Cellule avec le constructeur adapté
	 * 
	 * @throws IOException
	 * @throws ErreurFormule
	 */
	public Cellule lireString() throws IOException, ErreurFormule {
		tok = new StreamTokenizer(monReader);
		tok.nextToken();
		if (tok.ttype == StreamTokenizer.TT_WORD) {
			return lireLabel();
		} else if (tok.ttype == StreamTokenizer.TT_NUMBER) {
			return lireConstanteHorsFormule();
		} else if (tok.ttype == '=') {
			tok.nextToken();
			if (tok.ttype != StreamTokenizer.TT_WORD)
				error("erreur de saisi formule ou RefCell");
			if (m.verifRefCellExiste(tok.sval))
				return lireRefCell();
			else
				return (Cellule) lireFormule();
		} else {
			error("erreur de saisi, caractère inconnnu " + tok.toString());
		}
		return null;// a garder
	}

	/**
	 * <pre>
	 * Crée une Cellule RefCell. 
	 * Condition : 
	 * 	la chaine ne doit pas contenir d'autre caractère après la référence de la cellule. 
	 * La cellule à laquelle on fait référence doit exister dans le tableau, être non null 
	 * et être de nature numérique et ne pas faire référence à elle même
	 * </pre>
	 * 
	 * @return
	 * @throws IOException
	 * @throws ErreurFormule
	 */
	private Cellule lireRefCell() throws IOException, ErreurFormule {
		String coord = tok.sval;
		tok.nextToken();
		if (tok.ttype != StreamTokenizer.TT_EOF)
			error("saisie incorrect : la référence de la cellule est suivi de qlqch");
		char ordonnee = (char) coord.charAt(0);
		int abscisse = Character.getNumericValue(coord.charAt(1));
		if (nouvelleCellCoord.equals(coord))
			error("Référence circulaire");
		else if (m.getCell(ordonnee, abscisse) == null)
			error("La cellule à laquelle vous faites référence est vide");
		else {
			if (m.getCell(ordonnee, abscisse).celluleEstNumerique()) {
				if (m.getCell(ordonnee, abscisse).verifRefCirculaireNumerique(nouvelleCellCoord) == true){
					error("Référence circulaire lireRefCell()"); //FIXME lireRefCell()
				} else if (m.getCell(ordonnee, abscisse).verifRefCirculaireNumerique(nouvelleCellCoord) == false) {
					ConstanteNumerique newCellN = new ConstanteNumerique(nouvelleCellCoord, userInput,
							((CelluleNumerique) m.getCell(ordonnee, abscisse)).val());
					newCellN.setCelluleReferante(m.getCell(ordonnee, abscisse));
					return newCellN;
				}
			} else {
				if (!m.getCell(ordonnee, abscisse).verifRefCirculaireString(nouvelleCellCoord)) {
					CelluleTexte newCellT = new CelluleTexte(nouvelleCellCoord, userInput,
							m.getCell(ordonnee, abscisse).toString());
					newCellT.setCelluleReferante(m.getCell(ordonnee, abscisse));
					return newCellT;
					// référence à une celluleTexte
				} else {
					error("Référence circulaire");
				}

			}
		}
		return null;

	}

	/**
	 * <pre>
	 * Crée une Cellule ConstanteNumérique si la constante numérique n'est pas
	 * dans une formule. 
	 * Condition : 
	 * 	la chaine ne doit pas contenir d'autre caractère derrière la constante numérique
	 * </pre>
	 * 
	 * @return
	 * @throws IOException
	 * @throws ErreurFormule
	 */
	private Cellule lireConstanteHorsFormule() throws IOException, ErreurFormule {
		// Remarque : les espaces avant un nombre sont supprimés et le nombre
		// est créé
		double ajout = tok.nval;
		tok.nextToken();
		if (tok.ttype != StreamTokenizer.TT_EOF)
			error("entier suivi d'un caractère inconnu " + tok.toString());
		return new ConstanteNumerique(nouvelleCellCoord, userInput, ajout);
	}

	/**
	 * Créer une cellule Label qui prends en compte la chaine jusqu'à la fin de
	 * la saisie.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ErreurFormule
	 */
	private Cellule lireLabel() throws IOException, ErreurFormule {
		return new CelluleTexte(nouvelleCellCoord, userInput, userInput);
	}

	/**
	 * <pre>
	 * Créer une Cellule Formule. 
	 * Conditions : 
	 * Une opération est composée d'un opérateur, une parenthèse ouvrante, de paramètres séparés par un point
	 * virgule et une parenthèse fermante L'opérateur doit faire partie des enum et opérations prévues. 
	 * Remarque : 
	 * 	l'opérateur est transformé en majuscule
	 * pour éviter les fautes de casse 
	 * Exemples Bon : 
	 * 	=plus(2;2)
	 * 	=plus(2;fois(3;3)) 
	 * Exemples faux: 
	 * 	=plus(;2) =plus() 
	 * Remarques : 
	 * 	plus(2;2)
	 * est considéré comme du texte
	 * </pre>
	 * 
	 * @see Voir les Junitests pour plus d'infos
	 * 
	 * @return
	 * @throws IOException
	 * @throws ErreurFormule
	 */
	private CelluleNumerique lireFormule() throws IOException, ErreurFormule {
		if (!verifEnum(tok.sval)) {
			error("Cette Opération est inconnue " + tok.toString());
		} else {
			String majuscule = tok.sval.toUpperCase();
			Operation op = creerEnum(majuscule);
			
			tok.nextToken();
			if (tok.ttype != '(')
				error("Vous avez oubliez les parenthèses (");
			ArrayList<CelluleNumerique> parametres = new ArrayList<CelluleNumerique>();
			tok.nextToken();
			while (tok.ttype != ')') {
				if (tok.ttype == ';')
					error("l'operation se fini par ;");
				parametres.add(lireParametre());
				tok.nextToken();// ;
				if (tok.ttype != ')')
					tok.nextToken();
			}
			// WISH if (tok.ttype != ')') error("Vous avez oubliez les parenthèses )");
			Formule fo =new Formule(nouvelleCellCoord, userInput, op, parametres);
			for (CelluleNumerique cel : parametres){
				Cellule cell = (Cellule) cel;
				if (cell.getCelluleReferante() != null)
					fo.setCelluleReferante(cell.getCelluleReferante());
			}
			return fo;
		}
		return null; // a garder
	}

	/**
	 * <pre>
	 * Pour une formule : Créer une cellule de type ConstanteNumérique ou une
	 * autre formule imbriquée ou une référence. Les références à une autre cellule crée des
	 * ConstanteNumérique (si elles existent, sont non null et sont de nature
	 * numérique et ne font pas référence à elle même)
	 * </pre>
	 * 
	 * @return
	 * @throws IOException
	 * @throws ErreurFormule
	 */
	private CelluleNumerique lireParametre() throws IOException, ErreurFormule {
		if (tok.ttype == StreamTokenizer.TT_NUMBER) {
			return new ConstanteNumerique(nouvelleCellCoord, userInput, tok.nval);
		} else if (tok.ttype == StreamTokenizer.TT_WORD) {
			String mot = tok.sval;
			if (m.verifRefCellExiste(mot)) {
				char ordonnee = (char) mot.charAt(0);
				int abscisse = Character.getNumericValue(mot.charAt(1));
				//TODO faire tous les tests de refCell dans une fonction 
				if (nouvelleCellCoord.equals(mot))
					error("Référence circulaire");
				else if (m.getCell(ordonnee, abscisse) == null)
					error("La cellule à laquelle vous faites référence est vide");
				else {
					if (m.getCell(ordonnee, abscisse).celluleEstNumerique()) {
						if (m.getCell(ordonnee, abscisse).verifRefCirculaireNumerique(nouvelleCellCoord) == false) {
							ConstanteNumerique newCellN = new ConstanteNumerique(nouvelleCellCoord, userInput,
									((CelluleNumerique) m.getCell(ordonnee, abscisse)).val());
							newCellN.setCelluleReferante(m.getCell(ordonnee, abscisse));
							return newCellN;
						}else if (m.getCell(ordonnee, abscisse).verifRefCirculaireNumerique(nouvelleCellCoord) == true){
							error("Référence circulaire lireParametre()"); //FIXME lireParametre()
						}
					} else {
						error("Référence à une cellule texte");
					}
				}

			} else if (verifEnum(mot)) {
				return lireFormule();
			} else {
				error("paramètre inconnu" + tok.toString());
			}
		} else {
			error("paramètre inconnu" + tok.toString());
		}

		return null; // a garder
	}

	/**
	 * Vérifie si un string est un enum Operation (=> un opérateur pour une
	 * formule) retourne vrai si c'est le cas
	 * 
	 * @param s
	 * @return true si l'opérateur existe, false s'il existe pas
	 */
	private boolean verifEnum(String s) {
		for (Operation op : Operation.values()) {
			if (op.toString().equals(s.toUpperCase()))
				return true;
		}
		return false;
	}

	/**
	 * Créer un objet Operation à partir d'un string
	 * 
	 * @param s
	 * @return
	 */
	private Operation creerEnum(String s) {
		switch (s) {
		case "PLUS":
			return Operation.PLUS;
		case "MOINS":
			return Operation.MOINS;
		case "FOIS":
			return Operation.FOIS;
		case "DIV":
			return Operation.DIV;
		}
		return null;
	}

	/**
	 * crée une erreur du message passé en paramètre
	 * 
	 * @param message
	 * @throws ErreurFormule
	 */
	private void error(String message) throws ErreurFormule {
		throw new ErreurFormule(message);
	}

}
