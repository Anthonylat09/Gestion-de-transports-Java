package GestionTransport;
import java.time.LocalTime;

/**
 * Classe représentant un trajet simple avec une gare de départ, d'arrivée, une
 * heure de départ et d'arrivée , une durée et un moyen de transport
 * @author NDIAYE Antoine et VERBAERE Corentin
 *
 */
public class TrajetSimple {
	Station stationDepart;
	Station stationArrivee;
	LocalTime heureDepart;
	LocalTime heureArrivee;
	int duree;
	MoyenTransport moyen;
	
	

	public TrajetSimple(MoyenTransport moyen) {
		this.moyen = moyen;
	}



	@Override
	public String toString() {
		return "TrajetSimple [stationDepart=" + stationDepart + ", StationArrivee=" + stationArrivee + ", heureDepart="
				+ heureDepart + ", heureArrivee=" + heureArrivee + ", duree=" + duree + ", moyen=" + moyen + "]";
	}


	protected TrajetSimple clone() {
		TrajetSimple clone=null;
		try { clone = (TrajetSimple) super.clone(); }
		catch (CloneNotSupportedException e) { e.printStackTrace();}
		return (TrajetSimple)clone;
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



	public int getDuree() {
		return duree;
	}



	public void setDuree(int duree) {
		this.duree = duree;
	}



	public MoyenTransport getMoyen() {
		return moyen;
	}



	public void setMoyen(MoyenTransport moyen) {
		this.moyen = moyen;
	}
	
	
	
}
