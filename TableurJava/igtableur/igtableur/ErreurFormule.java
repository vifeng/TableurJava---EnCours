package igtableur;

public class ErreurFormule extends Exception{
    ErreurFormule(){}
    public ErreurFormule(String msg){
	super(msg);
    }
}
