package src;// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Hannah Bast <bast@cs.uni-freiburg.de>.
// Author: Julian Korell.

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * Implementation of a very simple inverted index.
 */
public class InvertedIndex {

  /**
   * The inverted lists = a sorted list of document ids, one for each word that
   * occurs at least once in the given collection.
   */
  protected HashMap<String, ArrayList<Pair>> invertedLists;

  public ArrayList<String> documents;
  public ArrayList<Integer> documentLengths;
  public double averageDocumentLength = 0;
  public HashMap<String, Integer>  documentFrequencies;

  /**
   * Contructor creates empty index.
   */
  public InvertedIndex() {
    invertedLists = new HashMap<String, ArrayList<Pair>>();
    documents = new ArrayList<String>();
    documentLengths = new ArrayList<Integer>();
    documentFrequencies = new HashMap<>();
  }

  /**
   * Merge sorted lists, keeping duplicates.
   */
  public ArrayList<Pair> merge(ArrayList<Pair> list1,
                               ArrayList<Pair> list2) {

//    if (list1.size() == 0) {
//      return list2;
//    }
//
//    if (list2.size() == 0) {
//      return list1;
//    }

    int l1 = 0;
    int l2 = 0;
    ArrayList<Pair> mergedList = new ArrayList<Pair>();

    while (l1 < list1.size() || l2 < list2.size()) {
      if (l1 == list1.size()) {
        mergedList.add(list2.get(l2));
        l2++;
      } else if (l2 == list2.size()) {
        mergedList.add(list1.get(l1));
        l1++;
      } else if (list1.get(l1).documentId < list2.get(l2).documentId) {
        mergedList.add(list1.get(l1));
        l1++;
      } else if (list1.get(l1).documentId == list2.get(l2).documentId) {
        Pair pair1 = list1.get(l1);
        Pair pair2 = list2.get(l2);
        mergedList.add(new Pair(pair1.documentId, pair1.count + pair2.count));
        l1++;
        l2++;
      } else {
        mergedList.add(list2.get(l2));
        l2++;
      }
    }
    return mergedList;
  }

  /**
   * Build inverted lists from given text file.  expected format of the file is
   * one line per document with each line containing the text from the document
   * without newlines.
   */
  public void buildFromTextFile(String fileName) throws IOException {
    FileReader fileReader = new FileReader(fileName);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    int documentId = 0;
    while (true) {
      String line = bufferedReader.readLine();
      if (line == null) { break; }
      documentId++;
      documents.add(line);
      documentLengths.add(line.length());
      String[] words = line.split("\\W+");
      // System.out.println(Arrays.toString(words));
      for (String word : words) {
        word = word.toLowerCase();
        int count = 1;
        if (!invertedLists.containsKey(word)) {
          invertedLists.put(word, new ArrayList<Pair>());
          invertedLists.get(word).add(new Pair(documentId, 0));
          documentFrequencies.put(word, count);
        }
        Pair lastPair = invertedLists.get(word).get(invertedLists.get(word)
                .size() - 1);
        if (lastPair.documentId == documentId) {
          lastPair.raiseCount();
        } else {
          invertedLists.get(word).add(new Pair(documentId, 1));
          int docFrequency = documentFrequencies.get(word);
          documentFrequencies.put(word, docFrequency + 1);
        }
      }
    }
    double sum = 0;
    for (int length : documentLengths) {
      sum += length;
    }
    averageDocumentLength = sum / documentLengths.size();
    computeScores();
  }

  /**
   * Compute BM25 scores for every word in the documents.
   */
  public void computeScores() {
    for (Map.Entry<String, ArrayList<Pair>> entry : invertedLists.entrySet()) {
      String word = entry.getKey();
      ArrayList<Pair> listOfPairs = entry.getValue();
      for (Pair pair : listOfPairs) {
        double tf = pair.count;
        double df = documentFrequencies.get(word);
        double dl = documentLengths.get(pair.documentId - 1);
        double avdl = averageDocumentLength;
        double numberOfDocuments = documents.size();
        double k = 1.75;
        double b = 0.75;
        double idf = Math.log(numberOfDocuments / df) / Math.log(2.0d);

        double score = tf * (k + 1) / (k * (1 - b + b * dl / avdl) + tf)
                * idf;
        pair.count = score;

      }
    }
  }

  /**
   * Returns ArrayList of Pairs for all given words.
   */
  public ArrayList<Pair> processQuery(String[] words) {
    ArrayList<Pair> mergeResult = new ArrayList<Pair>();
    for (String word : words) {

      if (!invertedLists.containsKey(word)) { continue; }

      ArrayList<Pair> listOfPairs = invertedLists.get(word);
      mergeResult = merge(listOfPairs, mergeResult);
    }
    Collections.sort(mergeResult);
    return mergeResult;
  }
}
