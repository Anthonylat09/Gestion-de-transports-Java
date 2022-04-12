package Automates;
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

import static java.time.temporal.ChronoUnit.MINUTES;
public class AutomateTrain {

	public static ArrayList<TrajetSimple> listeTrajets = new ArrayList<TrajetSimple>();
	
	public static void Lire()
	{
		File f = new File("train.xml");
		Pattern heure = Pattern.compile("\\d{2}");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.getDocumentElement().normalize();
			NodeList lines = doc.getElementsByTagName("junction"); // On recupere les elements dans une liste de noeuds
			for (int i = 0; i< lines.getLength(); i++)
			{
				Node node = lines.item(i);
				
				//On recupere les elements dans les noeuds un par un
				if (node.getNodeType() == node.ELEMENT_NODE)
				{
					Element eElement = (Element) node;
					String stationDepart = eElement.getElementsByTagName("start-station").item(0).getTextContent();
					String stationArrivee = eElement.getElementsByTagName("arrival-station").item(0).getTextContent();
					
					String heureDepart = eElement.getElementsByTagName("start-hour").item(0).getTextContent();
					if (!heureDepart.matches("\\d{4}")) throw new FormatHeureException();
					String heureArrivee = eElement.getElementsByTagName("arrival-hour").item(0).getTextContent();
					if (!heureArrivee.matches("\\d{4}")) throw new FormatHeureException();
					
					Matcher mHeureDepart = heure.matcher(heureDepart);
					
					//On recupere l'heure depart
					mHeureDepart.find();
					int hd = Integer.parseInt(mHeureDepart.group());
					if (hd < 0 || hd > 23) throw new FormatHeureException();
					
					mHeureDepart.find();
					int md = Integer.parseInt(mHeureDepart.group());
					if (md < 0 || md > 59) throw new FormatHeureException();
					
					Matcher mHeureArrivee = heure.matcher(heureArrivee);
					
					//On recupere lheure d'arrivee
					mHeureArrivee.find();
					int ha = Integer.parseInt(mHeureArrivee.group());
					if (ha < 0 || ha > 23) throw new FormatHeureException();
					
					mHeureArrivee.find();
					int ma = Integer.parseInt(mHeureArrivee.group());
					if (ma < 0 || ma > 59) throw new FormatHeureException();
					
					LocalTime depart = LocalTime.of(hd, md);
					
					LocalTime arrivee = LocalTime.of(ha, ma);
					
					int duree = (int) depart.until(arrivee, MINUTES);
					
					TrajetSimple trajet = new TrajetSimple(MoyenTransport.Train);
					trajet.setStationDepart(Station.valueOf(stationDepart));
					trajet.setStationArrivee(Station.valueOf(stationArrivee));
					trajet.setHeureDepart(depart);
					trajet.setHeureArrivee(arrivee);
					trajet.setDuree(duree);
					listeTrajets.add(trajet);
					//System.out.println(trajet);
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Lire();
		
	}

}
