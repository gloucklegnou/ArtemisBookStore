#Fichiers de données contenus dans ce répertoire

## artemisBookstoreData-v1.csv
Données CSV d'Artemis Bookstore

## artemisBookstoreData-v1.ttl
La version initiale du graphe de données d'ArtemisBookStore construit  par le
programme GenerateBookStore à partir des données CSV.

## artemisBookstoreData-v2.ttl

Le graphe de données d'ArtemisBookStore enrichi de liens `owl:sameAs` par 
le programme `DBpediaMatching` à partir des données CSV concernant les écrivains
américains récupérés dans DBpedia.

##artemisBookstoreOnto.ttl
Vocabulaire RDFS pour modéliser les données d'Artemis Bookstore

##dbpediaAmericanWriters.csv
Données CVS extraites de DBpedia par la requête `dbpediaAmericanWriters.rq` 
et utilisées par le pgm java DBpediaMatching pour créer les liens owl:sameAs

##dbpediaAmericanWriters.rq
requête SPARQL pour extraire les noms des écrivains Américains contenus dans DBpedia


