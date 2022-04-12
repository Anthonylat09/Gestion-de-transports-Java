package GestionTransport;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Classe représentant un trajt composé de plusieurs trajets simples
 * @author NDIAYE Antoine et VERBAERE Corentin
 *
 */
public class TrajetCompose {

	ArrayList<TrajetSimple> trajets;
	Station stationDepart;
	Station stationArrivee;
	LocalTime heureDepart;
	LocalTime heureArrivee;
	int dureeTotale;
	
	public TrajetCompose()
	{
		trajets = new ArrayList<>();
	}
	
	
	/**
	 * 
	 * @param nouveau représente le trajet à ajouter
	 *
	 * @return un booleen indiquant si l'ajout a pu etre fait correctement
	 */
	boolean add(TrajetSimple nouveau) 
	{
        boolean ajout=false;
        TrajetSimple dernier = null;
        if(trajets==null || trajets.isEmpty())
        {
        	if(trajets==null) trajets = new ArrayList<>();
        	stationDepart = nouveau.getStationDepart();
        	heureDepart = nouveau.getHeureDepart();
        	ajout=true;
        }
        else
        {
            dernier = trajets.get(trajets.size()-1);
            if(dernier.getStationArrivee() == nouveau.getStationDepart() && dernier.getHeureArrivee().isBefore(nouveau.getHeureDepart()))
                        ajout = true;
        }
        if(ajout)
                {
                        trajets.add(nouveau);
                        stationArrivee = nouveau.getStationArrivee();
                        heureArrivee = nouveau.getHeureArrivee();
                        dureeTotale += nouveau.getDuree();
                }
        calcule();
        return ajout;
	}
	
	public void add(List<TrajetSimple> listeTrajets) {
        trajets.addAll(listeTrajets);
        calcule();
}
	
	void calcule()
    {
            TrajetSimple premier = trajets.get(0);
            TrajetSimple dernier = trajets.get(trajets.size()-1);
            stationDepart = premier.getStationDepart();
            stationArrivee = dernier.getStationArrivee();
            dureeTotale = (int) ChronoUnit.MINUTES.between( premier.getHeureDepart(), dernier.getHeureArrivee() );
    }
	



	public ArrayList<TrajetSimple> getTrajets() {
		return trajets;
	}



	public void setTrajets(ArrayList<TrajetSimple> trajets) {
		this.trajets = trajets;
	}



	public Station getStationDepart() {
		return stationDepart;
	}



	public void setStationDepart(Station stationDepart) {
		this.stationDepart = stationDepart;
	}



	public Station getStationArrivee() {
		return stationArrivee;
	}



	public void setStationArrivee(Station stationArrivee) {
		this.stationArrivee = stationArrivee;
	}



	public LocalTime getHeureDepart() {
		return heureDepart;
	}



	public void setHeureDepart(LocalTime heureDepart) {
		this.heureDepart = heureDepart;
	}



	public LocalTime getHeureArrivee() {
		return heureArrivee;
	}



	public void setHeureArrivee(LocalTime heureArrivee) {
		this.heureArrivee = heureArrivee;
	}



	public int getDureeTotale() {
		return dureeTotale;
	}



	public void setDureeTotale(int dureeTotale) {
		this.dureeTotale = dureeTotale;
	}



	@Override
	public String toString() {
		return "TrajetCompose [stationDepart=" + stationDepart + ", stationArrivee="
				+ stationArrivee + ", heureDepart=" + heureDepart + ", heureArrivee=" + heureArrivee + ", dureeTotale="
				+ dureeTotale + "]";
	}
	
	public static void main(String args[])
	{
		TrajetCompose trajets = new TrajetCompose();
		
		TrajetSimple trajet1 = new TrajetSimple(MoyenTransport.Metro);
		trajet1.setStationDepart(Station.Arly);
		trajet1.setStationArrivee(Station.Limo);
		LocalTime depart = LocalTime.of(9, 20);
		trajet1.setHeureDepart(depart);
		LocalTime arrivee = LocalTime.of(9, 30);
		trajet1.setHeureArrivee(arrivee);
		trajet1.setDuree((int)depart.until(arrivee, MINUTES));
		
		TrajetSimple trajet2 = new TrajetSimple(MoyenTransport.Metro);
		trajet2.setStationDepart(Station.Limo);
		trajet2.setStationArrivee(Station.Singha);
		LocalTime depart1 = LocalTime.of(9, 31);
		trajet2.setHeureDepart(depart1);
		LocalTime arrivee1 = LocalTime.of(9, 50);
		trajet2.setHeureArrivee(arrivee1);
		trajet2.setDuree((int)depart1.until(arrivee1, MINUTES));

		trajets.add(trajet1);
		trajets.add(trajet2);
		trajets.calcule();
		
		System.out.println(trajets);

	}
}
