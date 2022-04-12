package Exceptions;

public class FormatDureeException extends Exception {

	public FormatDureeException()
	{
		super("Attention, Durée non renseingée ou renseignée au mauvais format");
	}
}
