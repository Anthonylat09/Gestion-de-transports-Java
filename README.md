# Gestion-de-transports-Java

Contexte et objectif :
Des exploitants de transports en commun souhaitent développer des services de création d’itinéraires à
l’intention des usagers. Pour cela, ils doivent échanger des données relatives à leur réseau (stations desservies
et horaires). Il y a 4 exploitants : train, métro, tram, car inter-cites. Le réseau complet est fourni ci-dessous,
il n’est pas à l’échelle, les valeurs sur les arcs sont les temps en minutes entre stations.
Les exploitants fournissent leurs données sous des formats différents (des exemples de fichiers de données
pour chacun d’eux sont à disposition sur Moodle) :
— des formats textes simples pour les exploitants des cars inter-cités et du métro ;
— du XML pour l’exploitant des trains et trams.

Réalisation :
1. comprendre quelles sont les données à représenter : voir les exemples de fichiers mis à disposition ;
2. créer en Java les classes nécessaires pour stocker et manipuler ces données ;
3. certains mots dans ces fichiers suivent un format spécifique. Décrire ces formats en utilisant vos
connaissances (expressions régulières par exemple). Voir comment cela est représenté dans l’API "regex" (lien dans les ressources ci-dessous) ;
4. décrire les étapes de lecture des fichiers par des automates (au moins un automate par type de fichier) ;
5. sur la base de ces automates et expressions, développer les fonctionnalités permettant de lire les fichiers
de données transport. La lecture permet de stocker les données par la création d’objets instances des
classes prévues au point 2.
Attention, la lecture doit déclencher des erreurs lorsque les données ne sont pas au format prévu :
lorsqu’une erreur a été détectée, l’analyse s’arrête et affiche l’erreur détectée ainsi que le numéro de
ligne du fichier où elle apparaît. ;
6. fournir un programme de recherche d’itinéraire, par le plus court chemin dans le réseau, selon les
stations de départ et d’arrivée, et l’heure de départ au plus tôt : "je souhaite partir de X pour aller à
Y après Z h".
