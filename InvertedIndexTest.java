// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Hannah Bast <bast@cs.uni-freiburg.de>.
// Author: Julian Korell.

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Assert;
import src.InvertedIndex;
import src.Pair;

import java.io.IOException;

/**
 * One unit test for each non-trivial method in the InvertedIndex class.
 */
public class InvertedIndexTest {

  @Test
  public void buildFromTextFile() throws IOException {
    src.InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example2.txt");
    src.Pair testPairfirst0 = new Pair(1, 0.9997378915);
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
    ii.buildFromTextFile("example2.txt");
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
    ii.buildFromTextFile("example.txt");
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
    ii.buildFromTextFile("example.txt");
    Assert.assertEquals(12.3, ii.averageDocumentLength, 0.1);
  }
  @Test
  public void documentFrequencies() throws IOException {
    InvertedIndex ii = new InvertedIndex();
    ii.buildFromTextFile("example2.txt");
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
    ii.buildFromTextFile("example2.txt");
    double scoreForFirst = 0.999729;
    Pair pairOfFirst = ii.invertedLists.get("first").get(0);
    Assert.assertEquals(scoreForFirst, pairOfFirst.count, 0.1);
  }
}
