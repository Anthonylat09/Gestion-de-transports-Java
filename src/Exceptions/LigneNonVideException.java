package Exceptions;

public class LigneNonVideException extends Exception {

	public LigneNonVideException()
	{
		super("Attention, ligne vide non rencontrée");
	}
}
