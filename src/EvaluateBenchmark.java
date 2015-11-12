// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Julian Korell.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Julian on 11.11.2015.
 */
public class EvaluateBenchmark {

  InvertedIndex ii;
  public ArrayList<Double> precisionAt3;

  public EvaluateBenchmark(InvertedIndex ii) {
    this.ii = ii;
    precisionAt3 = new ArrayList<>();
  }

  public void readBenchmarkFile() throws IOException {
    String fileName = "benchmarktest.txt";
    FileReader fileReader = new FileReader(fileName);
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    while (true) {

      ArrayList<Integer> documentIds = new ArrayList<>();

      String line = bufferedReader.readLine();
      if (line == null) {
        break;
      }
      String[] twoParts = line.split("\t");
      String[] qWords = twoParts[0].split(" ");
      String[] docIds = twoParts[1].split(" ");

      for (String docId : docIds) {
        int documentId = Integer.parseInt(docId);
        documentIds.add(documentId);
      }
      ArrayList<Pair> queryResult = ii.processQuery(qWords);
      double result = precisionAtK(queryResult, documentIds, 3);
      precisionAt3.add(result);
    }
  }

  public double precisionAtK(ArrayList<Pair> resultIds,
                               ArrayList<Integer> relevantIds, double k) {
    int resultIdsLength = resultIds.size();
    double count = 0;
    for (int i = 0; i < k && i  < resultIdsLength; i++) {
      int docId = resultIds.get(i).documentId;
      if (relevantIds.contains(docId)) {
        count++;
      }
    }
    return count / k;
  }
  
  public double averagePrecision(ArrayList<Pair> resultIds, 
                                 ArrayList<Integer> relevantIds) {
    double numberOfRelevantIds = relevantIds.size();
    double sum = 0;

    for (int i = 0; i < numberOfRelevantIds; i++) {
      sum += precisionAtK(resultIds, relevantIds, i + 1);
    }
    return sum / numberOfRelevantIds;
  }
}
