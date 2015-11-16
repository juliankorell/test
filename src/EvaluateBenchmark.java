// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Julian Korell.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Julian on 11.11.2015.
 */
public class EvaluateBenchmark {

  InvertedIndex ii;

  public EvaluateBenchmark(InvertedIndex ii) {
    this.ii = ii;
  }

  public Tripe readBenchmarkFile() throws IOException {
    String fileName = "movies-benchmark.txt";
    FileReader fileReader = new FileReader(fileName);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    double resultPat3 = 0;
    double resultPatR = 0;
    double resultAp = 0;

    double mpAt3;
    double mpAtR;
    double map;
    int count = 0;

    while (true) {

      ArrayList<Integer> documentIds = new ArrayList<>();

      String line = bufferedReader.readLine();
      if (line == null) {
        break;
      }

      count++;

      String[] twoParts = line.split("\\t");
      String[] qWords = twoParts[0].split(" ");
      String[] docIds = twoParts[1].split(" ");

      for (String docId : docIds) {
        int documentId = Integer.parseInt(docId);
        documentIds.add(documentId);
      }

      ArrayList<Pair> queryResult = ii.processQuery(qWords);

      resultPat3 += precisionAtK(queryResult, documentIds, 3);
      resultPatR += precisionAtK(queryResult, documentIds, documentIds.size());
      resultAp += averagePrecision(queryResult, documentIds);
    }
    mpAt3 = resultPat3 / count;
    mpAtR = resultPatR / count;
    map = resultAp / count;

    return new Tripe(mpAt3, mpAtR, map);
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
    ArrayList<Integer> averagePrecisionList = new ArrayList<>();
    ArrayList<Integer> resultIdsInt = new ArrayList<>();
    for (Pair pair : resultIds) {
      resultIdsInt.add(pair.documentId);
    }

    for (int id : relevantIds) {
      int relevantId = resultIdsInt.indexOf(id) + 1;
      averagePrecisionList.add(relevantId);
    }
    Collections.sort(averagePrecisionList);

    double count = 0;
    double countNonZero = 1;
    double aPsum = 0;

    for (int idPosition : averagePrecisionList) {
      if (idPosition == 0) {
        count++;
        continue;
      } else {
        aPsum += countNonZero / idPosition;
        countNonZero++;
        count++;
      }
    }
    return aPsum / count;
  }
}
