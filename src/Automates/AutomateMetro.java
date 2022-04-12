package Automates;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GestionTransport.MoyenTransport;
import GestionTransport.Station;
import GestionTransport.TrajetSimple;

import Exceptions.*;

public class AutomateMetro {

	public static ArrayList<TrajetSimple> listeTrajets = new ArrayList<TrajetSimple>();
	
	/**
	 * Verifie si une station donnée est dans une liste de stations, utile pour la lecture du fichier
	 * @param station
	 * @param stations
	 * @return
	 */
	public static boolean dansListe(Station station, ArrayList<Station> stations)
	{
		boolean retour = false;
		for (Station s : stations)
		{
			if (station == s) retour = true;
		}
		return retour;
	}
	
	public static void Lire()
	{
		File f = new File("metro.txt");
		Pattern heure = Pattern.compile("\\d{2}");//Pattern pour lire exactement deux chiffres
		Pattern ville = Pattern.compile("[a-zA-Z]+");//Pattern pour litre des lettres de l'alphabet
		Pattern duree = Pattern.compile("\\d+");//Pattern pour lire des entiers
		
		Scanner sc;
		try {
			sc = new Scanner(f);
			
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();
			String ligne3 = sc.nextLine();
			String[] stations = ligne3.split(" ");
			
			ArrayList <Station> stationsPotentielles = new ArrayList();
			
			for (int i = 0; i < stations.length; i++)
			{
				Station s = Station.valueOf(stations[i]);
				stationsPotentielles.add(s);
			}
			
			if(!sc.nextLine().equals("")) throw new LigneNonVideException();
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();

			ArrayList <TrajetSimple> trajetsPotentiels = new ArrayList<TrajetSimple>(); // on cree ensuite une liste de trajets potentiels
			do
			{		
				String ligne = sc.nextLine();
				if(ligne.equals("")) break; // on lit tant quon ne rencontre pas de ligne vide
				
				TrajetSimple trajet = new TrajetSimple(MoyenTransport.Metro);
				Matcher mVille = ville.matcher(ligne);
				Matcher mDuree = duree.matcher(ligne);
				
				mVille.find();
				Station villeDepart = Station.valueOf(mVille.group());
				if (!dansListe(villeDepart,stationsPotentielles)) throw new StationRenseigneeException();
				trajet.setStationDepart(villeDepart); //depart
				
				mVille.find();
				Station villeArrivee = Station.valueOf(mVille.group());
				if (!dansListe(villeArrivee,stationsPotentielles)) throw new StationRenseigneeException();
				trajet.setStationArrivee(villeArrivee); //arrivee

				if (!mDuree.find()) throw new FormatDureeException();
				trajet.setDuree(Integer.parseInt(mDuree.group())); //duree
				trajetsPotentiels.add(trajet);
			} while (sc.hasNextLine());
			
			if (!sc.hasNextLine()) throw new FormatFichierException();
			
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();
			String premierDepart = sc.nextLine(); //a partir de
			if(!premierDepart.matches("\\d{4}")) throw new FormatHeureException();
			
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();
			String stringIntervalle = sc.nextLine();
			if(!stringIntervalle.matches("\\d+")) throw new FormatDureeException();
			int intervalle = Integer.parseInt(stringIntervalle); //toutes les x minutes
			
			if (!sc.nextLine().startsWith("%")) throw new FormatFichierException();
			String dernierDepart = sc.nextLine(); //derniers departs de gare
			if(!dernierDepart.matches("\\d{4}")) throw new FormatHeureException();

			Matcher mHeurePremierDepart = heure.matcher(premierDepart);
			
			mHeurePremierDepart.find();
			int hpd = Integer.parseInt(mHeurePremierDepart.group()); //heure du premier depart
			if (hpd < 0 || hpd > 23) throw new FormatHeureException();
			
			mHeurePremierDepart.find();
			int mpd = Integer.parseInt(mHeurePremierDepart.group());//minute de lheure du premier depart
			if (mpd < 0 || mpd > 59) throw new FormatHeureException();
			
			Matcher mHeureDernierDepart = heure.matcher(dernierDepart);
			
			mHeureDernierDepart.find();
			int hdd = Integer.parseInt(mHeureDernierDepart.group()); //heure du dernier depart
			if (hdd < 0 || hdd > 23) throw new FormatHeureException();
			
			mHeureDernierDepart.find();
			int mdd = Integer.parseInt(mHeureDernierDepart.group());// minute de lheure du dernier depart
			if (mdd < 0 || mdd > 59) throw new FormatHeureException();
			
			LocalTime last = LocalTime.of(hdd, mdd);//dernier depart
			for (TrajetSimple t : trajetsPotentiels)
			{
				LocalTime time = LocalTime.of(hpd, mpd); //premier depart a 07:00
				do {
					TrajetSimple nouveauTrajet = new TrajetSimple(MoyenTransport.Metro);
					nouveauTrajet.setStationDepart(t.getStationDepart());
					nouveauTrajet.setStationArrivee(t.getStationArrivee());
					nouveauTrajet.setDuree(t.getDuree());
					nouveauTrajet.setHeureDepart(time);
					nouveauTrajet.setHeureArrivee(time.plusMinutes(t.getDuree()));
					time = time.plusMinutes(intervalle); // on incremente le temps de lintervalle a chaque fois
					listeTrajets.add(nouveauTrajet);
					//System.out.println(nouveauTrajet);				
				} while(time.isBefore(last));
			}
			
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
