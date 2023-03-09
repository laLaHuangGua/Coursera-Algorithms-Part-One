package attempt2;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
  private ArrayList<Double> segmentSlops = new ArrayList<Double>();

  public FastCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Null object occurs");
    scanPoints(points);
  }

  public int numberOfSegments() {
    return lineSegments.size();
  }

  public LineSegment[] segments() {
    LineSegment[] arrayLinesegments = new LineSegment[lineSegments.size()];
    int i = 0;
    for (var each : lineSegments) {
      arrayLinesegments[i] = each;
      i++;
    }
    return arrayLinesegments;
  }

  private void scanPoints(Point[] po) {
    Point[] initPo = new Point[po.length];
    for (int index = 0; index < po.length; index++)
      initPo[index] = po[index];

    for (int i = 0; i < po.length; i++) {
      Arrays.sort(po, initPo[i].slopeOrder());

      int n = 1;
      int steps = 1;
      int start = 1;
      double slop = slopBetween(initPo[i], po[1]);

      while (n < po.length - 1) {
        if (Double.compare(slop, slopBetween(initPo[i], po[++n])) == 0)
          steps++;
        else {
          if (steps >= 3) {
            int size = n - start + 1;
            Point[] collinearPoints = new Point[size];

            collinearPoints[0] = initPo[i];
            for (int j = start, k = 1; k < size; j++, k++)
              collinearPoints[k] = po[j];
            Arrays.sort(collinearPoints);

            if (!segmentSlops.contains(slop)) {
              lineSegments.add(
                  new LineSegment(
                      collinearPoints[0], collinearPoints[size - 1]));
              segmentSlops.add(slop);
            }
          }
          start = n;
          steps = 1;
          slop = initPo[i].slopeTo(po[n]);
        }
      }
    }
  }

  private double slopBetween(Point po1, Point po2) {
    validateNullPoint(po1);
    validateNullPoint(po2);
    double slopToPo2 = po1.slopeTo(po2);
    validateRepeatedPoint(slopToPo2);
    return slopToPo2;
  }

  private void validateRepeatedPoint(double slop) {
    if (Double.compare(slop, Double.NEGATIVE_INFINITY) == 0)
      throw new IllegalArgumentException("Repeated point occurs");
  }

  private void validateNullPoint(Point po) {
    if (po == null)
      throw new IllegalArgumentException("Null object occurs");
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
