package Exceptions;

public class TrajetRenseigneException extends Exception {
	
	public TrajetRenseigneException()
	{
		super("Attention, trajet n'appartenant pas a l'esnemble des trajets renseignes");
	}
}
