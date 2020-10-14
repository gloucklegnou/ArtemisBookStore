# ArtemisBookStore
Code pour le TP *Artemis Bookstore*, TP d'introduction au Framework [Apache Jena](https://jena.apache.org/)

La structure de ce projet est la suivante :

# répertoire Data

## artemisBookstoreData-v1.csv
Données CSV d'Artemis Bookstore utilisées en entrée du programme `GenerateBookStoreGraph`
pour créé un fichier TURTLE correspondant au graphe de données initial d'artemis BookStore

## artemisBookstoreOnto.ttl
Vocabulaire RDFS pour modéliser les données d'Artemis Bookstore

## dbpediaAmericanWriters.csv
Données CVS extraites de DBpedia par la requête `dbpediaAmericanWriters.rq` 
et utilisées par le pgm java DBpediaMatching pour créer les liens owl:sameAs

## dbpediaAmericanWriters.rq
requête SPARQL pour extraire les noms des écrivains Américains contenus dans DBpedia

# répertoire src

## DBpediaMatching
Programme Java liant les données RDF d'Artemis Bookstore avec les données de 
DBpedia et utilisant l'API Jena Core (TP2 exo 3)

## Listbooks
Programme Java utilisant l'API Jena Core pour obtenir la liste des livres d'un auteur
donné (TP1 exo 5).

## GenerateBookStoreGraph
Programme Java générant les données RDF à partir des données CSV (TP1 exo 4).





