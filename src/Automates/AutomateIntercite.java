package Automates;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.*;
import GestionTransport.MoyenTransport;
import GestionTransport.Station;
import GestionTransport.TrajetSimple;

public class AutomateIntercite {

	public static ArrayList<TrajetSimple> listeTrajets = new ArrayList<TrajetSimple>();
	
	public static boolean dansListe(TrajetSimple trajet, ArrayList<TrajetSimple> trajets)
	{
		boolean retour = false;
		for (TrajetSimple t : trajets)
		{
			if ((trajet.getStationDepart() == t.getStationDepart() && trajet.getStationArrivee() == t.getStationArrivee())
			  ||(trajet.getStationArrivee() == t.getStationDepart() && trajet.getStationDepart() == t.getStationArrivee()))
			  {
				retour = true;
			  }
		}
		return retour;
	}
	public static void Lire()
	{
		File f = new File("InterCites.txt");
		Pattern heure = Pattern.compile("\\d{2}");  //Pattern pour lire exactement deux chiffres
		Pattern ville = Pattern.compile("[a-zA-Z]+"); //Pattern pour litre des lettres de l'alphabet
		Pattern duree = Pattern.compile("\\d+"); //Pattern pour lire des entiers
		try {
			Scanner sc = new Scanner(f);
			
			/* Dans cette boucle on lit les lignes jusqua tomber sur la lignz ayant un % suivi d'un espace*/
			do
			{		
				String ligne = sc.nextLine();
				if(!ligne.startsWith("%")) throw new FormatFichierException();
				if(ligne.equals("% ")) break;	
			} while (sc.hasNextLine());
			if (!sc.hasNextLine()) throw new FormatFichierException();
			//On cree une liste de trajets potentiels nous servant de reference
			ArrayList <TrajetSimple> trajetsPotentiels = new ArrayList<TrajetSimple>();
			do
			{		
				String ligne = sc.nextLine();
				if(ligne.equals("//")) break; // On sort de la boucle si on rencontre un double slash	
				
				TrajetSimple trajet = new TrajetSimple(MoyenTransport.Intercite);
				Matcher mVille = ville.matcher(ligne);
				Matcher mDuree = duree.matcher(ligne);
				
				mVille.find();
				trajet.setStationDepart(Station.valueOf(mVille.group()));
				mVille.find();
				trajet.setStationArrivee(Station.valueOf(mVille.group()));
				
				
				if (!mDuree.find()) throw new FormatDureeException();
				trajet.setDuree(Integer.parseInt(mDuree.group()));
				trajetsPotentiels.add(trajet); // On ajoute le trajet potentiel a la liste 
				
			} while (sc.hasNextLine());
			if(!sc.hasNextLine()) throw new FormatFichierException();
			
			/* Cest dans cette boucle qu'on va effectivement creer les trajets*/
			do
			{		
				String ligne = sc.nextLine(); //on lit une nouvelle ligne a chaque iteration
				TrajetSimple trajet = new TrajetSimple(MoyenTransport.Intercite);
				Matcher mVille = ville.matcher(ligne);
				Matcher mHeure = heure.matcher(ligne);
				
				mVille.find();
				trajet.setStationDepart(Station.valueOf(mVille.group())); // ville de depart
				mVille.find();
				trajet.setStationArrivee(Station.valueOf(mVille.group())); //ville darrivee
				
				if(!mHeure.find()) throw new FormatHeureException();
				int h = Integer.parseInt(mHeure.group()); //heure de depart
				
				if(!mHeure.find()) throw new FormatHeureException();
				int m = Integer.parseInt(mHeure.group()); // partie minute de lheure de depart
				
				LocalTime time = LocalTime.of(h, m);
				trajet.setHeureDepart(time);
				
				for (TrajetSimple t : trajetsPotentiels)
				{
					if ((t.getStationDepart() == trajet.getStationDepart() && t.getStationArrivee() == trajet.getStationArrivee())
					  ||(t.getStationDepart() == trajet.getStationArrivee() && t.getStationArrivee() == trajet.getStationDepart()))//On cree le trajet uniquement si on a un trajets potentiels
					{
						trajet.setDuree(t.getDuree());
						trajet.setHeureArrivee(trajet.getHeureDepart().plusMinutes(t.getDuree())); // Dans ce cas on lui donne comme heure darrivee lheure de depart a laquelle on ajoute la valeur de la duree quon obtient grace au tableau de trajets potentiels
						break;
					}
				}
				if(!dansListe(trajet,trajetsPotentiels)) throw new TrajetRenseigneException();
				listeTrajets.add(trajet);
				//System.out.println(trajet);
				//System.out.println(ligne);	
			} while (sc.hasNextLine());
				
			sc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		Lire();
	}
}
