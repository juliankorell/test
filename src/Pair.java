package src;

// Copyright 2013, University of Freiburg,
// Chair of Algorithms and Data Structures.
// Author: Julian Korell.

/**
 * Implementation of pairs.
 */

public class Pair  implements Comparable<Pair> {
  public int documentId;
  public double count;

  public Pair(int documentId, double count) {
    this.documentId = documentId;
    this.count = count;
  }

  public void prettyPrint() {
    System.out.println("documentId: " + documentId + " count: " + count + "\n");
  }

  public void raiseCount() {
    this.count++;
  }

  public int compareTo(Pair other) {
    if (this.count > other.count) { return -1; }
    if (this.count == other.count) { return 0; }
    if (this.count < other.count) { return 1; }
    return 0;
  }
}
