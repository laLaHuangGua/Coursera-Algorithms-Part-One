package attempt6;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private ArrayList<Point> heads = new ArrayList<Point>();
  private ArrayList<Double> slops = new ArrayList<Double>();
  private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

  private static class NewPoint implements Comparable<NewPoint> {
    Point point;
    double slop;

    public NewPoint(Point po, double s) {
      point = po;
      slop = s;
    }

    @Override
    public int compareTo(NewPoint that) {
      return Double.compare(slop, that.slop);
    }
  }

  public FastCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Null object occurs");

    int size = points.length;

    Point[] po = new Point[size];
    for (int i = 0; i < size; i++) {
      validateNullPoint(points[i]);
      po[i] = points[i];
    }

    validateRepeatedPoint(po);
    scanPoints(po);
  }

  public int numberOfSegments() {
    return lineSegments.size();
  }

  public LineSegment[] segments() {
    LineSegment[] lineSegmentsArray = new LineSegment[lineSegments.size()];
    for (int i = 0; i < lineSegments.size(); i++)
      lineSegmentsArray[i] = lineSegments.get(i);
    return lineSegmentsArray;
  }

  private void scanPoints(Point[] po) {
    int poSize = po.length;
    int maxIndex = poSize - 1;

    if (poSize <= 3)
      return;

    for (int i = 0; i < poSize; i++) {

      NewPoint[] newPo = new NewPoint[poSize];
      for (int j = 0; j < poSize; j++) {
        if (j == 0)
          newPo[j] = new NewPoint(po[j], Double.NEGATIVE_INFINITY);
        else
          newPo[j] = new NewPoint(po[j], po[i].slopeTo(po[j]));
      }

      Arrays.sort(newPo);

      // double[] iSlops = new double[poSize];
      // for (int j = 0; j < poSize; j++)
      // iSlops[j] = newPo[j].slop;

      int n = 1;
      int start = 1;
      boolean match = false;

      while (n < maxIndex) {
        match = equal(newPo[start].slop, newPo[++n].slop);
        if (match && n != maxIndex) {
          continue;
        } else {
          int size = n - start + 1;
          if (n == maxIndex && match)
            size++;

          if (size >= 4) {
            Point[] collinrPo = new Point[size];
            collinrPo[0] = po[i];
            for (int j = start, k = 1; k < size; j++, k++)
              collinrPo[k] = newPo[j].point;
            addOrDropIfExists(collinrPo, newPo[start].slop);
          }

          if (n >= maxIndex - 1)
            break;

          start = n;
          match = false;
        }
      }
    }
  }

  private boolean equal(double a, double b) {
    return Double.compare(a, b) == 0;
  }

  private boolean equal(Point po1, Point po2) {
    return po1.compareTo(po2) == 0;
  }

  private void addOrDropIfExists(Point[] po, double slop) {
    boolean lineIsExists = false;
    for (int i = 0; i < heads.size(); i++) {
      if (equal(slops.get(i), slop) && equal(heads.get(i).slopeTo(po[0]), slop)) {
        lineIsExists = true;
        break;
      }
    }

    if (!lineIsExists) {
      Arrays.sort(po);
      heads.add(po[0]);
      slops.add(slop);
      lineSegments.add(new LineSegment(po[0], po[po.length - 1]));
    }
  }

  private void validateRepeatedPoint(Point[] po) {
    Arrays.sort(po);
    for (int i = 1; i < po.length; i++)
      if (equal(po[i], po[i - 1]))
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
