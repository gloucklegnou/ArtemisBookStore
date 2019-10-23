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
package com.artemisbookstore.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This program generates a Turtle file from the ArtemisBookstore CSV File
 *
 * @author Philippe Genoud - LIG Steamer - Université Grenoble Alpes
 */
public class GenerateBookStore {

    private static final Map<String, UUID> authors = new HashMap<>();
    private static final Map<String, UUID> publishers = new HashMap<>();

    /**
     *
     * @param csvFileName name of the input CSV file
     * @param turtleFileName name of the output turtle file
     *
     * @throws java.io.IOException
     */
    public static void generateTurtle(String csvFileName, String turtleFileName) throws IOException {

        try (
                BufferedReader br = Files.newBufferedReader(Paths.get(csvFileName));
                BufferedWriter bw = Files.newBufferedWriter(Paths.get(turtleFileName))) {
            // using Files.newBufferedReader and Files.newBufferedWrite enforce charEncoding to UTF-8
            // Files.newBufferedWriter(Paths.get(turtleFileName)) is equivalent to
            // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(turtleFileName), StandardCharsets.UTF_8));

            // write the used prefixes
            bw.write("@prefix foaf: <http://xmlns.com/foaf/0.1/> .");
            bw.newLine();
            bw.write("@prefix dcterms: <http://purl.org/dc/terms/> .");
            bw.newLine();
            bw.write("@prefix abo: <http://artemisBookstore.com/ontology#> .");
            bw.newLine();
            bw.write("@prefix abr: <http://artemisBookstore.com/abr/> .");
            bw.newLine();
            bw.write("@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .");
            bw.newLine();

            bw.newLine();

            String line;
            br.readLine(); // to skip the column titles line
            while ((line = br.readLine()) != null) {
                //    System.out.println(line);
                String[] lineTokens = line.split(";");

                String authorFamilyName = lineTokens[0];
                String authorGivenName = lineTokens[1];
                String bookTitle = lineTokens[2];
                int pagesNb = Integer.parseInt(lineTokens[3]);
                String isbn = lineTokens[4];
                String publisherName = lineTokens[5];

                String authorFullName = authorGivenName + " " + authorFamilyName;

                UUID authorUUID = authors.get(authorFullName);
                if (authorUUID == null) {
                    // this is the first occurence of this author
                    // create triples describing him
                    authorUUID = UUID.randomUUID();
                    authors.put(authorFullName, authorUUID);
                    bw.write("abr:" + authorUUID + " a abo:Writer ;");
                    bw.newLine();
                    bw.write("\t foaf:givenName \"" + authorGivenName + "\"@en ;");
                    bw.newLine();
                    bw.write("\t foaf:familyName \"" + authorFamilyName + "\"@en ;");
                    bw.newLine();
                    bw.write("\t foaf:name \"" + authorGivenName + " " + authorFamilyName + "\"@en .");
                    bw.newLine();
                    bw.newLine();
                }
                UUID publisherUUID = publishers.get(publisherName);
                if (publisherUUID == null) {
                    // this is the first occurence of this publisher
                    // create triples describing it
                    publisherUUID = UUID.randomUUID();
                    publishers.put(publisherName, publisherUUID);

                    bw.write("abr:" + publisherUUID + " a abo:Publisher ;");
                    bw.newLine();
                    bw.write("\t foaf:name \"" + publisherName + "\"@en .");
                    bw.newLine();
                    bw.newLine();
                }
                UUID bookUUID = UUID.randomUUID();
                bw.write("abr:" + bookUUID + " a abo:Book ;");
                bw.newLine();
                bw.write("\t dcterms:title \"" + bookTitle + "\"@en ;");
                bw.newLine();
                bw.write("\t abo:author abr:" + authorUUID + " ;");
                bw.newLine();
                bw.write("\t abo:isbn \"" + isbn + "\" ;");
                bw.newLine();
                bw.write("\t abo:pages \"" + pagesNb + "\"^^xsd:int ;");
                bw.newLine();
                bw.write("\t dcterms:publisher abr:" + publisherUUID + " .");
                bw.newLine();
                bw.newLine();
            } // end of file
        }
    }

    public static void main(String[] args) throws IOException {
        generateTurtle("data/artemisBookstoreData-v1.csv", "data/artemisBookstoreData-v1-en.ttl");
    }

}
