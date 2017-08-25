package monTableur;

import java.util.*;

import igtableur.ErreurFormule;

public class Formule extends Cellule implements CelluleNumerique{
	private Operation op;
	private ArrayList<CelluleNumerique> parametres;

	/**
	 * Constructeur prend un type d'operation et une liste de paramètre pour
	 * effectuer l'opération. Ces paramètres sont des CelluleNumerique de type
	 * ConstanteNumérique ou Formule.
	 * 
	 * @param o
	 *            l'opérateur (un enum Operation)
	 * @param m
	 *            la list des paramètres de l'opération
	 */
	public Formule(String coordonnees, String inputIg, Operation o, ArrayList<CelluleNumerique> m) {
		super(coordonnees, inputIg);
		op = o;
		parametres = m;
	}

	/**
	 * <pre>
	 * Calcul la valeur d'une formule selon l'opérateuret sa liste de paramètres 
	 * Condition : 
	 * 	L'absence de paramètre est refusé
	 * 	Selon l'opération un seul paramètre est accepté ou pas. exemple : la multiplication ne le permet pas 
	 * Remarques : 
	 * 	l'opération à un paramètre est acceptée dans l'éventualité d'une mise à jour 
	 * 	pour des opérations sur des listes contigües de type : =plus(A1:A2)
	 * </pre>
	 * 
	 * @return double
	 * @throws ErreurFormule
	 */
	public double val() throws ErreurFormule {
		double res;
		switch (op) {
		case PLUS:
			if (parametres.isEmpty())
				throw new ErreurFormule("Vous n'avez pas d'arguments à votre formule");
			res = 0;
			for (CelluleNumerique iterable_element : parametres) {
				res += iterable_element.val();
			}
			return res;
		case MOINS:
			if (parametres.isEmpty())
				throw new ErreurFormule("Vous n'avez pas d'arguments à votre formule");
			if (parametres.size() == 1)
				return 0 - parametres.get(0).val();
			res = parametres.get(0).val() * 2;
			// Pour rétablir le signe du premier élement
			for (CelluleNumerique iterable_element : parametres) {
				res -= iterable_element.val();
			}
			return res;
		case FOIS:
			if (parametres.isEmpty())
				throw new ErreurFormule("Vous n'avez pas d'arguments à votre formule");
			if (parametres.size() == 1)
				throw new ErreurFormule("Vous n'avez qu'un seul paramètre");
			res = 1;
			for (CelluleNumerique iterable_element : parametres) {
				res = res * iterable_element.val();
			}
			return res;
		case DIV:
			if (parametres.isEmpty())
				throw new ErreurFormule("Vous n'avez pas d'arguments à votre formule");
			if (parametres.size() == 1)
				throw new ErreurFormule("Vous n'avez qu'un seul paramètre");
			res = parametres.get(0).val();
			for (int i = 1; i < parametres.size(); i++) {
				if (parametres.get(i).val() == 0)
					throw new ErreurFormule("Division par zéro impossible");
				res = res / parametres.get(i).val();
			}
			return res;
		}
		throw new Error("Cette opération n'existe pas");
	}

	@Override
	public String toString() {
		// QUESTION comment faire pour faire un throw et pas un try catch car il
		// n'y a pas de message d'erreur pour l'opération =Div(2;0)
		try {
			return "" + this.val();
		} catch (ErreurFormule e) {
			return "#ERREUR";
		}
	}

	/**
	 * 
	 * @return un arrayList des paramètres de la formule
	 */
	public ArrayList<CelluleNumerique> getMesExpr() {
		return parametres;
	}

}
