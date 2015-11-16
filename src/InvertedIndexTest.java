// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Hannah Bast <bast@cs.uni-freiburg.de>.
// Author: Julian Korell.

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

/**
 * One unit test for each non-trivial method in the InvertedIndex class.
 */
public class InvertedIndexTest {

  double k = 1.75;
  double b = 0.75;

  @Test
  public void buildFromTextFile() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example2.txt", k, b);
    Pair testPairfirst0 = new Pair(1, 0.9997378915);
    Pair testPairfirst1 = new Pair(3, 0.5575685894099074);
    Pair testPairfirst2 = new Pair(2, 0);
    Pair getPair0 = ii.invertedLists.get("first").get(0);
    Pair getPair1 = ii.invertedLists.get("first").get(1);
    Pair getPair2 = ii.invertedLists.get("docum").get(1);
    Assert.assertEquals(testPairfirst0.documentId, getPair0.documentId);
    Assert.assertEquals(testPairfirst0.count, getPair0.count, 0.1);
    Assert.assertEquals(testPairfirst1.documentId, getPair1.documentId);
    Assert.assertEquals(testPairfirst1.count, getPair1.count, 0.1);
    Assert.assertEquals(testPairfirst2.documentId, getPair2.documentId);
    Assert.assertEquals(testPairfirst2.count, getPair2.count, 0.1);
  }
  @Test
  public void merge() {
    ArrayList<Pair> list1 = new ArrayList<Pair>();
    list1.add(new Pair(1, 1));
    list1.add(new Pair(2, 2));
    list1.add(new Pair(3, 3));
    list1.add(new Pair(8, 1));
    list1.add(new Pair(9, 2));
    ArrayList<Pair> list2 = new ArrayList<Pair>();
    list2.add(new Pair(1, 1));
    list2.add(new Pair(2, 1));
    list2.add(new Pair(3, 3));
    ArrayList<Pair> list3 = new ArrayList<Pair>();
    list3.add(new Pair(1, 2));
    list3.add(new Pair(2, 3));
    list3.add(new Pair(3, 6));
    list3.add(new Pair(8, 1));
    list3.add(new Pair(9, 2));
    ArrayList<Pair> listEmpty = new ArrayList<Pair>();
    InvertedIndex ii = new InvertedIndex();
    ArrayList<Pair> mergeResult = ii.merge(list1, list2);
    ArrayList<Pair> mergeResult2 = ii.merge(list1, listEmpty);
    ArrayList<Pair> mergeResult3 = ii.merge(listEmpty, list2);
    Assert.assertEquals(list3.get(0).count, mergeResult.get(0).count, 0.1);
    Assert.assertEquals(list3.get(1).count, mergeResult.get(1).count, 0.1);
    Assert.assertEquals(list3.get(2).count, mergeResult.get(2).count, 0.1);
    Assert.assertEquals(list3.get(3).count, mergeResult.get(3).count, 0.1);
    Assert.assertEquals(list3.get(4).count, mergeResult.get(4).count, 0.1);
    Assert.assertEquals(list1.get(0).count, mergeResult2.get(0).count, 0.1);
    Assert.assertEquals(list1.get(1).count, mergeResult2.get(1).count, 0.1);
    Assert.assertEquals(list1.get(2).count, mergeResult2.get(2).count, 0.1);
    Assert.assertEquals(list2.get(0).count, mergeResult3.get(0).count, 0.1);
    Assert.assertEquals(list2.get(1).count, mergeResult3.get(1).count, 0.1);
    Assert.assertEquals(list2.get(2).count, mergeResult3.get(2).count, 0.1);

  }
  @Test
  public void mergeQuery() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example2.txt", k, b);
    ArrayList<Pair> list1 = new ArrayList<Pair>();
    list1.add(new Pair(1, 0.9997378915));
    list1.add(new Pair(3, 0.5575685894099074));
    ArrayList<Pair> mergeResult = ii.processQuery(new String[]{"first"});
    ArrayList<Pair> mergeResult2 = ii.processQuery(new String[]{"first",
      "docum"});
    Assert.assertEquals(list1.get(0).documentId, mergeResult.get(0).documentId);
    Assert.assertEquals(list1.get(1).documentId, mergeResult.get(1).documentId);
    Assert.assertEquals(list1.get(0).count, mergeResult.get(0).count, 0.0001);
    Assert.assertEquals(list1.get(1).count, mergeResult.get(1).count, 0.0001);
    Assert.assertEquals(list1.get(0).documentId, mergeResult2.get(0)
            .documentId);
    Assert.assertEquals(list1.get(1).count, mergeResult2.get(1).count, 0.0001);
  }
  @Test
  public void documentLengths() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example.txt", k, b);
    int docLength1 = ii.documentLengths.get(0);
    int docLength2 = ii.documentLengths.get(1);
    int docLength3 = ii.documentLengths.get(2);
    Assert.assertEquals(12, docLength1);
    Assert.assertEquals(13, docLength2);
    Assert.assertEquals(12, docLength3);
  }
  @Test
  public void averageDocumentLength() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example.txt", k, b);
    Assert.assertEquals(12.3, ii.averageDocumentLength, 0.1);
  }
  @Test
  public void documentFrequencies() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example2.txt", k, b);
    int docFrequency1 = ii.documentFrequencies.get("first");
    int docFrequency2 = ii.documentFrequencies.get("docum");
    int docFrequency3 = ii.documentFrequencies.get("second");
    Assert.assertEquals(2, docFrequency1);
    Assert.assertEquals(3, docFrequency2);
    Assert.assertEquals(2, docFrequency3);
  }
  @Test
  public void computeScores() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example2.txt", k, b);
    double scoreForFirst = 0.999729;
    Pair pairOfFirst = ii.invertedLists.get("first").get(0);
    Assert.assertEquals(scoreForFirst, pairOfFirst.count, 0.1);
  }
  @Test
  public void precisionAtK() {
    InvertedIndex ii = new InvertedIndex();
    EvaluateBenchmark bench = new EvaluateBenchmark(ii);
    ArrayList<Pair> listOfPairs1 = new ArrayList<>();
    ArrayList<Pair> listOfPairs2 = new ArrayList<>();
    ArrayList<Pair> listOfPairs3 = new ArrayList<>();
    ArrayList<Integer> relevantIds = new ArrayList<>();
    listOfPairs1.add(new Pair(1, 2));
    listOfPairs1.add(new Pair(2, 1));
    listOfPairs1.add(new Pair(3, 1));
    listOfPairs2.add(new Pair(2, 1));
    listOfPairs2.add(new Pair(4, 3));
    listOfPairs2.add(new Pair(5, 3));
    listOfPairs2.add(new Pair(1, 3));
    listOfPairs3.add(new Pair(1, 3));
    listOfPairs3.add(new Pair(3, 3));
    relevantIds.add(1);
    relevantIds.add(2);
    relevantIds.add(3);
    double result1 = bench.precisionAtK(listOfPairs1, relevantIds, 3);
    double result2 = bench.precisionAtK(listOfPairs2, relevantIds, 3);
    double result3 = bench.precisionAtK(listOfPairs3, relevantIds, 3);
    Assert.assertEquals(1, result1, 0.0001);
    Assert.assertEquals(0.333333, result2, 0.0001);
    Assert.assertEquals(0.666666, result3, 0.0001);
  }
  @Test
  public void averagePrecision() {
    InvertedIndex ii = new InvertedIndex();
    EvaluateBenchmark bench = new EvaluateBenchmark(ii);
    ArrayList<Pair> listOfPairs1 = new ArrayList<>();
    ArrayList<Pair> listOfPairs2 = new ArrayList<>();
    ArrayList<Pair> listOfPairs3 = new ArrayList<>();
    ArrayList<Pair> listOfPairs4 = new ArrayList<>();
    ArrayList<Integer> relevantIds = new ArrayList<>();
    ArrayList<Integer> relevantIds2 = new ArrayList<>();
    ArrayList<Integer> relevantIds3 = new ArrayList<>();
    ArrayList<Integer> relevantIds4 = new ArrayList<>();
    listOfPairs1.add(new Pair(1, 2));
    listOfPairs1.add(new Pair(2, 1));
    listOfPairs1.add(new Pair(3, 1));
    listOfPairs2.add(new Pair(2, 1));
    listOfPairs2.add(new Pair(4, 3));
    listOfPairs2.add(new Pair(5, 3));
    listOfPairs2.add(new Pair(1, 3));
    listOfPairs3.add(new Pair(1, 3));
    listOfPairs3.add(new Pair(3, 3));
    listOfPairs4.add(new Pair(101, 3));
    listOfPairs4.add(new Pair(333, 1));
    listOfPairs4.add(new Pair(23, 3));
    listOfPairs4.add(new Pair(463, 2));
    listOfPairs4.add(new Pair(153, 3));
    listOfPairs4.add(new Pair(8, 6));
    listOfPairs4.add(new Pair(43, 3));

    relevantIds.add(1);
    relevantIds.add(2);
    relevantIds.add(3);
    relevantIds2.add(7);
    relevantIds2.add(8);
    relevantIds2.add(9);
    relevantIds3.add(1);
    relevantIds3.add(2);
    relevantIds3.add(3);
    relevantIds3.add(4);
    relevantIds3.add(5);
    relevantIds4.add(8);
    relevantIds4.add(23);
    relevantIds4.add(333);

    double result1 = bench.averagePrecision(listOfPairs1, relevantIds);
    double result2 = bench.averagePrecision(listOfPairs2, relevantIds);
    double result3 = bench.averagePrecision(listOfPairs3, relevantIds);
    double result4 = bench.averagePrecision(listOfPairs1, relevantIds2);
    double result5 = bench.averagePrecision(listOfPairs1, relevantIds3);
    double result6 = bench.averagePrecision(listOfPairs4, relevantIds4);
    Assert.assertEquals(1, result1, 0.0001);
    Assert.assertEquals(0.5, result2, 0.0001);
    Assert.assertEquals(0.66666, result3, 0.0001);
    Assert.assertEquals(0, result4, 0.0001);
    Assert.assertEquals(0.6, result5, 0.0001);
    Assert.assertEquals(0.55555, result6, 0.0001);
  }
}
