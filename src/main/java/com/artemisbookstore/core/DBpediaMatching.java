/*
 * Copyright (C) 2018 Philippe Genoud - LIG Steamer - Université Grenoble Alpes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.artemisbookstore.core;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;

/**
 *
 * @author Philippe Genoud - LIG Steamer - Université Grenoble Alpes
 */
public class DBpediaMatching {

    static final String FOAF_PREFIX = "http://xmlns.com/foaf/0.1/";
    static final String ABO_PREFIX = "http://artemisBookstore.com/ontology#";

    public static final String dbpediaDataFileName = "data/dbpediaAmericanWriters.csv";
    static final String artemisDataFileName = "data/artemisBookstoreData-v1-en.ttl";

    public static void main(String[] args) throws IOException {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(artemisDataFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + artemisDataFileName + " not found");
        }

        // read the Turtle file
        model.read(in, "", "TURTLE");

        Property foafNameProp = model.createProperty(FOAF_PREFIX, "name");
        try (BufferedReader br = Files.newBufferedReader(Paths.get(dbpediaDataFileName));) {
            String line;
            br.readLine(); // to skip the column titles line
            int nbMatching = 0;
            while ((line = br.readLine()) != null) {
                //    System.out.println(line);
                String[] lineTokens = line.split(",");
                String dbpediaURI = lineTokens[0];
                String dbpediaWriterName = lineTokens[1];
                List<Resource> artemisAuthors = model.listResourcesWithProperty(foafNameProp,
                        model.createLiteral(dbpediaWriterName, "en")).toList();
                if (!artemisAuthors.isEmpty()) {
                    System.out.println("match ###########################");
                    for (Resource author : artemisAuthors) {
                        nbMatching++;
                        System.out.println(dbpediaWriterName + " " + dbpediaURI + " " + author);
                        Resource dbpediaAuthor = model.createResource(dbpediaURI);
                        model.add(model.createStatement(author, OWL.sameAs, dbpediaAuthor));
                    }
                }
            }
            System.out.println("#########\nNumber of matching " + nbMatching);
            FileOutputStream out = new FileOutputStream("data/artemisBookstoreData-v2-en.ttl");
            model.setNsPrefix("dbr", "http://dbpedia.org/resource/");
            model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
            model.write(out,"Turtle");
        }

    }
}
