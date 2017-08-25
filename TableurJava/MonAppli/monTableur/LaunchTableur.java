package monTableur;

import igtableur.IGTableur;

/**
 * Lance le modèle, le programme et l'interface graphique pour un tableur
 */
public class LaunchTableur {

	public static void main(String[] args) {
		Modele mem = new Modele();
		Controleur appliTableur = new Controleur(mem);
		IGTableur igt = new IGTableur(appliTableur);
		appliTableur.setInterface(igt);
		
	}
}
