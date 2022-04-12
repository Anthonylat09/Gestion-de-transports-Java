package Exceptions;

public class StationRenseigneeException extends Exception {

	public StationRenseigneeException()
	{
		super("Attention, station n'appartenant pas a l'ensemble des stations possibles");
	}
}
