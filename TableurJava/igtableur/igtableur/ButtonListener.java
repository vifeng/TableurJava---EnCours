package igtableur;

import java.awt.event.*;

public class ButtonListener implements ActionListener{
	private IGTableur igt;
	ButtonListener(IGTableur o){
		igt=o;
	}
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "OK":
			igt.clickOK();
			break;
		case "annuler":
			igt.clickCancel();
			break;
		case "Surprise":
			igt.clickSurprise();
			break;
		case "Fermer":
			igt.clickFermer();
			break;
		default:
			break;
		}
	}
}
