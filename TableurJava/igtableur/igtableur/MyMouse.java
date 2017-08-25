package igtableur;

import javax.swing.event.*;
import java.awt.event.*;


public class MyMouse extends MouseInputAdapter{
	private IGTableur igt;
	private int x, y;
	MyMouse(IGTableur oi, int xx, int yy){
		igt = oi;
		x = xx;
		y = yy;
	}
	public void mouseClicked(MouseEvent e){
		igt.recordClick(x,y);
	}
}
