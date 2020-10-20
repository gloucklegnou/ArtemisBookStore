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

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.FileManager;

/**
 *
 * @author Philippe Genoud - LIG Steamer - Université Grenoble Alpes
 */
public class ListBooks {

    private static final String FOAF_PREFIX = "http://xmlns.com/foaf/0.1/";
    private static final String ABO_PREFIX = "http://artemisBookstore.com/ontology#";

    private static final String AB_FILE_NAME = "data/generated/artemisBookstoreData-v1-en.ttl";

    private static final Scanner SC = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return SC.nextLine();
    }

    public static void main(String[] args) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(AB_FILE_NAME);
        if (in == null) {
            throw new IllegalArgumentException("File: " + AB_FILE_NAME + " not found");
        }

        // read the Turtle file
        model.read(in, "", "TURTLE");

        Property foafGivenNameProp = model.createProperty(FOAF_PREFIX, "givenName");
        Property foafFamilyNameProp = model.createProperty(FOAF_PREFIX, "familyName");

        String stop = null;
        do {
            String authorGivenName = null;
            String authorFamilyName = readString("Family name : ");
            List<Statement> stmts = model.listStatements(null, foafFamilyNameProp, model.createLiteral(authorFamilyName, "en")).toList();
            Resource author = null;
            if (stmts.size() > 1) {
                System.out.println("There are more than one author with this name ");
                authorGivenName = readString("Given name : ");
                for (Statement stmt : stmts) {
                    System.out.println("object " + stmt.getObject());
                    List<Statement> stmts2 = model.listStatements(stmt.getSubject(),
                            foafGivenNameProp, model.createLiteral(authorGivenName, "en")).toList();
                    if (!stmts2.isEmpty()) {
                        author = stmt.getSubject();
                    }
                }
            } else if (stmts.size() == 1) {
                author = stmts.get(0).getSubject();
            }
            if (author != null) {
                // TODO complete with Jena Core code that finds all the books written by author.
                System.out.println("books");
            } else {
                System.out.println("Author " + ((authorGivenName == null)?authorGivenName:"") + " " + 
                        authorFamilyName + " does not exist" );
            }
            stop = readString("continue ? Y/N: ");
        } while (!"N".equals(stop.toUpperCase()));

        System.out.println("finished");
    }

}
