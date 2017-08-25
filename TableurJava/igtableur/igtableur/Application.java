package igtableur;



public interface Application {
	
	/** 
	 * Utilisee par l'IG pour afficher contenu de la cellule 
	 * couramment selectionnee dans la fenetre d'edition des cellules.
	 * Retourne sous forme de chaine le contenu   
	 *  de la cellule de coordonnees:
	 * @param col  colonne de la cellule (lettre)
	 * @param lig  no. ligne de la cellule (entier)
	 * @return     chaine à afficher dans fenetre de dialogue.
	 */
	  String getContenu(char col, int lig);
	  
	  /** 
	   * Chargee de modifier la une cellule dans l'application metier
	   *  ET de rafraichir l'IG. 
	   *  Doit analyser l'entree {@code s} puis, selon le cas:
       *  <ul>
	   *    <li> creer/modifier la cellule  dans l'appliacation metier
	   *      et rafraichir l'IG pour toutes les cellules pertinentes
	   *    <li> ou signaler une erreur par une exception.
       *  </ul>
	   * @param col  colonne de la cellule (lettre)
	   * @param lig  no. ligne de la cellule (entier)
	   * @param s    texte entre dans fenetre de dialogue
	   * @throws ErreurFormule si  @param s (entree) errone. 
	   */
	  void setContenu(char col, int lig, String s) throws ErreurFormule;
}
