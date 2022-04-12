package Automates;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Exceptions.*;
import GestionTransport.MoyenTransport;
import GestionTransport.Station;
import GestionTransport.TrajetSimple;

public class AutomateTram {

	public static ArrayList<TrajetSimple> listeTrajets = new ArrayList<TrajetSimple>();
	
	public static boolean dansListe(Station station,ArrayList<Station> stations)
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
		File f = new File("tram.xml");
		Pattern heure = Pattern.compile("\\d{2}");
		Pattern ville = Pattern.compile("[a-zA-Z]+");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(f);
				doc.getDocumentElement().normalize();
				
				String stringStationsPotentielles = doc.getElementsByTagName("stations").item(0).getTextContent();
				
				String[]tabStationsPotentielles = stringStationsPotentielles.split(" ");
				
				ArrayList<Station> stationsPotentielles = new ArrayList<Station>();
				for (String s : tabStationsPotentielles) stationsPotentielles.add(Station.valueOf(s));
					
				//System.out.println(stationsPotentielles);
				NodeList nodeList = doc.getElementsByTagName("ligne");
				for (int i = 0; i< nodeList.getLength(); i++)
				{
					Node node = nodeList.item(i);
					
					if (node.getNodeType() == node.ELEMENT_NODE)
					{
						Element eElement = (Element) node;
						String stations = eElement.getElementsByTagName("stations").item(0).getTextContent();
						for (int j = 0; j < eElement.getElementsByTagName("heures-passage").getLength(); j++)
						{
							String passages = eElement.getElementsByTagName("heures-passage").item(j).getTextContent();
							Matcher mHeureDepart = heure.matcher(passages);
							Matcher mHeureArrivee = heure.matcher(passages);
							
							mHeureDepart.find();
							int hd = Integer.parseInt(mHeureDepart.group());
							if (hd < 0 || hd > 23) throw new FormatHeureException();
							
							mHeureDepart.find();
							int md = Integer.parseInt(mHeureDepart.group());
							if (md < 0 || md > 59) throw new FormatHeureException();
							
							mHeureArrivee.find();
							mHeureArrivee.find();
							
							mHeureArrivee.find();
							int ha = Integer.parseInt(mHeureArrivee.group());
							if (ha < 0 || ha > 23) throw new FormatHeureException();
							
							mHeureArrivee.find();
							int ma = Integer.parseInt(mHeureArrivee.group());
							if (ma < 0 || ma > 59) throw new FormatHeureException();
							
							Matcher mStationDepart = ville.matcher(stations);
							Matcher mStationArrivee = ville.matcher(stations);
							if(!mStationDepart.find()) throw new FormatFichierException();
							if(!mStationArrivee.find()) throw new FormatFichierException();
							if(!mStationArrivee.find()) throw new FormatFichierException();
							
							LocalTime depart = LocalTime.of(hd, md);
							LocalTime arrivee = LocalTime.of(ha, ma);
							int duree = (int) depart.until(arrivee, MINUTES);
							
							TrajetSimple trajet = new TrajetSimple(MoyenTransport.Tram);
							trajet.setHeureDepart(depart);
							trajet.setHeureArrivee(arrivee);
							
							Station stationDepart = Station.valueOf(mStationDepart.group());
							if (!dansListe(stationDepart,stationsPotentielles)) throw new StationRenseigneeException();
							trajet.setStationDepart(stationDepart);
							
							Station stationArrivee = Station.valueOf(mStationArrivee.group());
							if (!dansListe(stationArrivee,stationsPotentielles)) throw new StationRenseigneeException();
							trajet.setStationArrivee(stationArrivee);
							
							trajet.setDuree(duree);
							//System.out.println(trajet);
							listeTrajets.add(trajet);
							/* Cette boucle permet de récypérer progressivement des couples de villes
							 * pour créer des trajets simples*/
							for(int k = 0; k < stations.split(" ").length - 2; k++)
							{
								mStationDepart.find();
								mStationArrivee.find();
								
								
								mHeureDepart.find();
								int hd1 = Integer.parseInt(mHeureDepart.group());
								if (hd1 < 0 || hd1 > 23) throw new FormatHeureException();
								
								mHeureDepart.find();
								int md1 = Integer.parseInt(mHeureDepart.group());
								if (md1 < 0 || md1 > 59) throw new FormatHeureException();
								
								mHeureArrivee.find();
								int ha1 = Integer.parseInt(mHeureArrivee.group());
								if (ha1 < 0 || ha1 > 23) throw new FormatHeureException();

								mHeureArrivee.find();
								int ma1 = Integer.parseInt(mHeureArrivee.group());
								if (ma1 < 0 || ma1 > 59) throw new FormatHeureException();

								LocalTime depart1 = LocalTime.of(hd1, md1);
								LocalTime arrivee1 = LocalTime.of(ha1, ma1);
								
								int duree1 = (int) depart1.until(arrivee1, MINUTES);
								TrajetSimple t = new TrajetSimple(MoyenTransport.Tram);
								t.setHeureDepart(depart1);
								t.setHeureArrivee(arrivee1);
								
								Station stationDepart1 = Station.valueOf(mStationDepart.group());
								if (!dansListe(stationDepart1,stationsPotentielles)) throw new StationRenseigneeException();
								t.setStationDepart(stationDepart1);
								
								Station stationArrivee1 = Station.valueOf(mStationArrivee.group());
								if (!dansListe(stationArrivee1,stationsPotentielles)) throw new StationRenseigneeException();
								t.setStationArrivee(stationArrivee1);
								
								t.setDuree(duree1);
								listeTrajets.add(t);
								//System.out.println(t);
							}
						}					
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public static void main(String[] args) {
		Lire();
	}
}
