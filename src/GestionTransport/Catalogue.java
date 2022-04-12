package GestionTransport;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Automates.AutomateIntercite;
import Automates.AutomateMetro;
import Automates.AutomateTrain;
import Automates.AutomateTram;
 
 
/**
 * Classe représentant un catalogue de trajets, permettant la gestion de trajets simples et composés
 * @author NDIAYE Antoine et VERBAERE Corentin
 *
 */
public class Catalogue {
		
		public static ArrayList<TrajetSimple> listeTrajets = new ArrayList<TrajetSimple>();
		
        Map<Station, List<TrajetSimple>> catalogue;
 
        Catalogue() { catalogue = new EnumMap<>(Station.class);}
 
        /**
         * Remplit la liste en paramètre grace aux automates
         */
        public static void remplirListe()
    	{
    		AutomateTram.Lire();
    		AutomateMetro.Lire();
    		AutomateIntercite.Lire();
    		AutomateTrain.Lire();
    		
    		listeTrajets.addAll(AutomateTram.listeTrajets);
    		listeTrajets.addAll(AutomateMetro.listeTrajets);
    		listeTrajets.addAll(AutomateIntercite.listeTrajets);
    		listeTrajets.addAll(AutomateTrain.listeTrajets);
    	}
        
        void addTrajetSimple(TrajetSimple trajet) {
                catalogue.compute(trajet.stationDepart,
                                (v, l) -> {if(l==null) {l = new ArrayList<>();} l.add(trajet); return l;});
        }
 
        
        public void creerCatalogue()
        {
                List<TrajetSimple> trajets = new ArrayList<>();
                remplirListe();
                for (TrajetSimple t : listeTrajets) trajets.add(t);
                catalogue = trajets.stream().collect(Collectors.groupingBy(TrajetSimple::getStationDepart));
                //for (TrajetSimple t : trajets) System.out.println(t);
        }
        
        /**
         * Trouve les trajets simples entre depart et arrivee
         *
         * @param depart
         *            station de depart
         * @param arrivee
         *            station d'arrivee
         * @param heureDepart
         *                              heure de depart
         * @param delaiMax
         *                              minutes de retard autorisees
         * @return une liste de tous les trajets simples entre depart et arrivee
         */
        List<TrajetSimple> trouveCheminsDirects(Station depart,Station arrivee, LocalTime heureDepart, int delaiMax) {
                List<TrajetSimple> cheminsDirects = null;
                List<TrajetSimple> trajets = catalogue.get(depart);
                if (trajets != null)
                {
                        cheminsDirects = new ArrayList<>(List.copyOf(trajets));
                        LocalTime dateDepartMax = heureDepart.plusMinutes(delaiMax);
                        cheminsDirects.removeIf(t->(t.getStationArrivee() != arrivee  || t.getHeureDepart().isBefore(heureDepart) || t.getHeureDepart().isAfter(dateDepartMax) ) );
                        if (cheminsDirects.isEmpty()) cheminsDirects = null;
                }
                return cheminsDirects ;
        }
 
        /**
         * calcule les chemins directs et indirects possibles entre 2 Stations
         * a partir d'une date donne avec un retard et un delai entre voyage autorise<br>
         *
         *
         * @param depart
         *            Station de depart
         * @param arrivee
         *            Station d'arrivee
         * @param momentDepart
         *            Heure depart
         * @param delai
         *            delai maximal autorise avant de partir, ou entre 2 voyages
         * @param voyageEnCours
         *            voyage en train d'etre construit
         * @param via
         *            liste des Stations visitees par le voyage
         * @param results
         *            liste de tous les chemins indirects possibles
         * @return true si au moins un chemin a ete trouve
         */
        public boolean trouverCheminIndirect(Station depart, Station arrivee, LocalTime momentDepart, int delai, List<TrajetSimple> voyageEnCours,
                                                                                  List<Station> via, List<TrajetCompose> results) {
                boolean result;
                via.add(depart);
                
                List<TrajetSimple> liste = new ArrayList<>(catalogue.get(depart));//On cherche des trajets à partir de la station de depart
                if (liste.isEmpty()) return false;
                
                LocalTime heureDepartMax = momentDepart.plusMinutes(delai);//date de depart au plus tard
                
                liste.removeIf(t->(t.heureDepart.isBefore(momentDepart))||t.heureDepart.isAfter(heureDepartMax));//On enleve les trajets avant et apres les limites
                for (TrajetSimple t : liste) {
                        
                        if (t.stationArrivee == arrivee) {//si on trouve un trajet menant à l'arrivée
                                
                                voyageEnCours.add(t);//on l'ajoute au voyage en cours
                                
                                TrajetCompose compo = new TrajetCompose();//on cree un nouveau trajet compose reprenant le detail du voyage
                                compo.add(List.copyOf(voyageEnCours));
                                compo.setHeureDepart(voyageEnCours.get(0).getHeureDepart());
                                compo.setHeureArrivee(voyageEnCours.get(voyageEnCours.size() - 1).getHeureArrivee());
                                
                                results.add(compo);//on l'ajoute au resultat
                                
                                voyageEnCours.remove(voyageEnCours.size() - 1);//on retire le dernier trajet pour en chercher un autre 
                        } else {
                                
                                if (!via.contains(t.stationArrivee)) {//si le trajet ne mène pas à l'arrivee mais donc à un via
                                        
                                        voyageEnCours.add(t); //on l'ajoute au voyage en cours
                                        
                                        trouverCheminIndirect(t.stationArrivee, arrivee, t.heureArrivee, delai, voyageEnCours, via, results);//on cherche à partir du via vers l'arrivee
                                        //on retire les derniers ajouts pour chercher d'autres chemins
                                        via.remove(t.stationArrivee);
                                        voyageEnCours.remove(t);
                                }
                        }
                }
                result = !results.isEmpty();
                return result;
        }
 
        public static void main(String[] args) {
                Catalogue cata = new Catalogue();
                cata.creerCatalogue();
 
 
                List<TrajetCompose> voyages = new ArrayList<>();
                Station A = Station.Singha;
                Station B = Station.Ecole;
                boolean result = cata.trouverCheminIndirect(A,B , LocalTime.of(8,30), 15,  new ArrayList<>(), new ArrayList<>(), voyages);
                System.out.println("Ensemble des trajets composés menant de "+String.valueOf(A)+" a "+String.valueOf(B)+" partant apres l'heure renseignee");
                if(result) voyages.forEach(System.out::println);
                else System.out.println("aucun trajet trouve");
 
                TrajetCompose plusRapide = Collections.min(voyages, Comparator.comparingInt(TrajetCompose::getDureeTotale));
                System.out.println();
                System.out.println("plus rapide = " + plusRapide);
                System.out.println("Composition du trajet le plus rapide:");
                plusRapide.getTrajets().forEach(System.out::println);
 
        }
}