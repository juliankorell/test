/**
 * Created by julianAir on 12.11.15.
 */
public class Tripe {
  public double mpAt3;
  public double mpAtR;
  public double map;


  public Tripe(double mpAt3, double mpAtR, double map) {
    this.mpAt3 = mpAt3;
    this.mpAtR = mpAtR;
    this.map = map;
  }

  public void prettyPrint() {
    System.out.println("MP@3: " + mpAt3 + " MP@R: " + mpAtR + " MAP " + map
            + "\n");
  }
}
