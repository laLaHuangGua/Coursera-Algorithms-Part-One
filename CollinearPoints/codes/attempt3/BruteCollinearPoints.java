// Attempt3: get scores 85/100, 'index out of bound' occurs
package attempt3;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
  private LineSegment[] lineSegments;

  public BruteCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Null object occurs");
    lineSegments = new LineSegment[points.length];

    Point[] po = new Point[points.length];
    for (int i = 0; i < points.length; i++) {
      validateNullPoint(points[i]);
      po[i] = points[i];
    }
    scanPoints(po);
  }

  public int numberOfSegments() {
    return lineSegments.length;
  }

  public LineSegment[] segments() {
    LineSegment[] copyOfLineSegments = new LineSegment[lineSegments.length];
    for (int i = 0; i < lineSegments.length; i++)
      copyOfLineSegments[i] = lineSegments[i];
    return copyOfLineSegments;
  }

  private void scanPoints(Point[] po) {
    Arrays.sort(po);

    double slopToJ = 0.0;
    double slopToK = 0.0;
    double slopToP = 0.0;

    int count = 0;
    for (int i = 0; i < po.length; i++)
      for (int j = i + 1; j < po.length; j++) {
        slopToJ = slopBetween(po[i], po[j]);
        for (int k = j + 1; k < po.length; k++) {
          slopToK = slopBetween(po[i], po[k]);
          for (int p = k + 1; p < po.length; p++) {
            slopToP = slopBetween(po[i], po[p]);

            if (equal(slopToJ, slopToK, slopToP))
              lineSegments[count++] = new LineSegment(po[i], po[p]);
          }
        }
      }
    resizeLineSegments(count);
  }

  private double slopBetween(Point po1, Point po2) {
    double slopToPo2 = po1.slopeTo(po2);
    validateRepeatedPoint(slopToPo2);
    return slopToPo2;
  }

  private boolean equal(double slop1, double slop2, double slop3) {
    if (Double.compare(slop1, slop2) == 0
        && Double.compare(slop2, slop3) == 0)
      return true;
    return false;
  }

  private void validateRepeatedPoint(double slop) {
    if (Double.compare(slop, Double.NEGATIVE_INFINITY) == 0)
      throw new IllegalArgumentException("Repeated point occurs");
  }

  private void validateNullPoint(Point po) {
    if (po == null)
      throw new IllegalArgumentException("Null object occurs");
  }

  private void resizeLineSegments(int length) {
    LineSegment[] newLineSegments = new LineSegment[length];
    for (int i = 0; i < length; i++)
      newLineSegments[i] = lineSegments[i];
    lineSegments = newLineSegments;
  }

  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
