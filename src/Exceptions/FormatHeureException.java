package Exceptions;

public class FormatHeureException extends Exception {

	public FormatHeureException()
	{
		super("Mauvais format de l'heure (Attention, 4 caractères, 0 <= heure < 24 et 0 <= minute < 60)");
	}
}
