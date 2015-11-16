// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Hannah Bast <bast@cs.uni-freiburg.de>.
// Author: Julian Korell.

import java.io.IOException;
import java.io.Console;
import java.util.ArrayList;
/**
 * Print query results in form of document text, document ID and count.
 */
public class InvertedIndexMain {

  public static void main(String[] args) throws IOException {
    InvertedIndex invertedIndex = new InvertedIndex();
    if (args.length != 1) {
      System.out.println("Usage: java -jar InvertedIndexMain.jar <file>");
      System.exit(1);
    }
    invertedIndex.buildFromTextFile(args[0], 1.75, 0.75);
    Console console = System.console();

    /**
     * Loop through queries. Return "No hits!" if query String has no results.
     * Use exit! to exit the loop and programm.
     * If query has a result, print result and highlight the query words.
     */

    while (true) {
      String query = console.readLine("Enter your search"
              + " query (separate words with spaces, enter bench!"
              + " to get benchmark results):")
              .toLowerCase();


      if (query.equals("bench!")) {
        EvaluateBenchmark bench = new EvaluateBenchmark(invertedIndex);
        Tripe result = bench.readBenchmarkFile();
        result.prettyPrint();
        break;
      }

      String[] words = query.split("\\W+");

      ArrayList<Pair> queryResult = invertedIndex.processQuery(words);

      if (queryResult.isEmpty() || query.isEmpty()) {
        System.out.println("No hits!");
      } else {
        for (int i = 0; i < 3 && i < queryResult.size(); i++) {
          int docId = queryResult.get(i).documentId;
          String text = invertedIndex.documents.get(docId - 1);
          String ansiRed = "\u001B[31m";
          String ansiReset = "\u001B[0m";
          for (String word : words) {
            text = text.replaceAll("((?i)" + word + ")", ansiRed + "$1"
                    + ansiReset);
          }
          System.out.println("\n" + text + "\n");
          queryResult.get(i).prettyPrint();
        }
      }
    }
  }
}
