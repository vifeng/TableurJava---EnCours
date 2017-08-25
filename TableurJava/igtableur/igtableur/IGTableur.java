package igtableur;

import javax.swing.*;

import monTableur.LaunchTableur;
import monTableur.Modele;
import monTableur.Controleur;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

@SuppressWarnings("serial")
public class IGTableur extends JFrame {
	private JTextField[][] tab = new JTextField[10][10];
	private JLabel nomcase = new JLabel("");
	private JTextField input = new JTextField(25);
	private JPanel haut = new JPanel();
	private JPanel feuille = new JPanel();
	private JLabel errmsg = new JLabel();
	private char col;
	private int lig;
	private Application appli;
	private DecimalFormat df = new DecimalFormat("#######.##");

	/**
	 * Constructeur prennant en parametre un objet a qui correspond à votre
	 * application métier (qui contient une répresentation de l'état courant du
	 * tableur et les méthodes pour le modifier).
	 * 
	 * @param a
	 *            application metier avec representation courante du tableur.
	 */
	public IGTableur(Application a) {
		super("Tableur");
		appli = a;
		JLabel[] horlab = new JLabel[10];
		JLabel[] verlab = new JLabel[10];
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("annuler");
		//JButton surprise = new JButton("Surprise");
		//JButton fermer = new JButton("Quitter");
		haut.setLayout(new FlowLayout());
		haut.add(nomcase);
		input.setEditable(true);
		haut.add(input);
		haut.add(ok);
		haut.add(cancel);
//		haut.add(fermer);
//		haut.add(surprise);
		ok.addActionListener(new ButtonListener(this));
		cancel.addActionListener(new ButtonListener(this));
//		surprise.addActionListener(new ButtonListener(this));
//		fermer.addActionListener(new ButtonListener(this));
		feuille.setLayout(new GridLayout(11, 11));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				tab[i][j] = new JTextField(10);
				tab[i][j].setEditable(false);
				tab[i][j].addMouseListener(new MyMouse(this, i, j));
				tab[i][j].setHorizontalAlignment(JTextField.LEFT);
			}
		}
		feuille.add(new JLabel(""));
		char c = 'A';
		for (int i = 0; i < 10; i++, c++) {
			verlab[i] = new JLabel("" + c, JLabel.CENTER);
			feuille.add(verlab[i]);
		}
		for (int i = 0; i < 10; i++) {
			horlab[i] = new JLabel("" + i + "    ", JLabel.RIGHT);
			feuille.add(horlab[i]);
			for (int j = 0; j < 10; j++) {
				feuille.add(tab[i][j]);
			}
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel rehaut = new JPanel();
		rehaut.setLayout(new GridLayout(2, 1));
		rehaut.add(haut);
		errmsg.setForeground(Color.RED);
		errmsg.setHorizontalAlignment(JLabel.CENTER);
		rehaut.add(errmsg);
		haut.setVisible(false);
		JPanel tout = new JPanel();
		tout.setLayout(new BorderLayout());
		tout.add(rehaut, BorderLayout.NORTH);
		tout.add(feuille, BorderLayout.CENTER);
		setContentPane(tout);
		pack();
		setVisible(true);
	}

	/**
	 * Action pour sélection d'une case: - appel a getContenu sur les
	 * coordonnees de la case - idealement, getContenu retourne contenu "source"
	 * de la case pour permettre son edition
	 * 
	 * @param x
	 *            ligne case
	 * @param y
	 *            colonne case
	 */
	void recordClick(int x, int y) {
		if (!haut.isVisible()) {
			col = ((char) ('A' + y));
			lig = x;
			haut.setVisible(true);
			nomcase.setText("case " + col + x);
			input.setText(appli.getContenu(col, lig));
			errmsg.setVisible(false);
			input.requestFocus();
		}
	}

	/**
	 * Action de click ok pour modification de cellule. Affiche éventuellement
	 * message d'erreur accompagnant exception. Idealement, doit modifier la
	 * structure du tableur, puis rafraichir toutes les cases de l'interface
	 * graphique.
	 */
	void clickOK() {
		try {
			appli.setContenu(col, lig, input.getText());
			haut.setVisible(false);
		} catch (ErreurFormule e) {
			errmsg.setText("Erreur de formule " + e.getMessage());
			errmsg.setVisible(true);
		}
	}

	void clickCancel() {
		haut.setVisible(false);
		errmsg.setVisible(false);
	}

	void clickSurprise() {
		haut.setVisible(false);
		errmsg.setVisible(false);
		tab[1][3].setBackground(Color.BLUE);
		tab[2][3].setBackground(Color.BLUE);
		tab[1][5].setBackground(Color.BLUE);
		tab[2][5].setBackground(Color.BLUE);
		tab[3][4].setBackground(Color.BLUE);
		tab[4][4].setBackground(Color.BLUE);
		tab[5][2].setBackground(Color.BLUE);
		tab[6][3].setBackground(Color.BLUE);
		tab[7][4].setBackground(Color.BLUE);
		tab[6][5].setBackground(Color.BLUE);
		tab[5][6].setBackground(Color.BLUE);
	}


	void clickFermer() {
		fermer();
	}

	/**
	 * Provoque affichage de chaine @param v pour case de coordonnees
	 * 
	 * @param c
	 *            colonne (char)
	 * @param l
	 *            ligne (int)
	 * @param v
	 *            chaine qui sera affichee
	 */
	public void modifieCellule(char c, int l, String v) {
		int co = c - 'A';
		String aff;
		try {
			double d = Double.parseDouble(v);
			aff = df.format(d) + ' ';
			tab[l][co].setHorizontalAlignment(JTextField.RIGHT);
		} catch (NumberFormatException e) {
			tab[l][co].setHorizontalAlignment(JTextField.LEFT);
			aff = ' ' + v;
		}
		tab[l][co].setText(aff);
	}

	/**
	 * Provoque la fermeture de la fenetre graphique.
	 */
	public void fermer() {
		this.dispose();
	}
}
