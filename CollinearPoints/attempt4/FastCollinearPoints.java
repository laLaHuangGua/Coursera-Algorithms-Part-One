package attempt4;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private Point[] heads;
  private Point[] ends;
  private double[] slops;
  private int count;

  public FastCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Null object occurs");

    int size = points.length;
    count = 0;
    heads = new Point[size];
    ends = new Point[size];
    slops = new double[size];

    Point[] po = new Point[size];
    for (int i = 0; i < size; i++) {
      validateNullPoint(points[i]);
      po[i] = points[i];
    }
    scanPoints(po);
  }

  public int numberOfSegments() {
    return count;
  }

  public LineSegment[] segments() {
    LineSegment[] lineSegments = new LineSegment[count];
    for (int i = 0; i < count; i++)
      lineSegments[i] = new LineSegment(heads[i], ends[i]);
    return lineSegments;
  }

  private void scanPoints(Point[] po) {
    int poSize = po.length;
    int maxIndex = poSize - 1;

    if (poSize <= 1)
      return;
    Point[] initPo = new Point[poSize];
    for (int index = 0; index < poSize; index++)
      initPo[index] = po[index];

    for (int i = 0; i < poSize; i++) {
      Arrays.sort(po, initPo[i].slopeOrder());

      int n = 1;
      int steps = 1;
      int start = 1;
      boolean flag = true;
      double slop = slopBetween(initPo[i], po[1]);

      while (n < poSize) {
        if (n < maxIndex) {
          if (Double.compare(slop, slopBetween(initPo[i], po[++n])) != 0)
            flag = false;
          if (flag) {
            steps++;
            continue;
          }
        }

        boolean nIsReachEnd = (n == maxIndex);
        if (steps >= 3) {
          int size = n - start + 1;
          if (nIsReachEnd && flag)
            size++;

          Point[] collinearPoints = new Point[size];
          collinearPoints[0] = initPo[i];
          for (int j = start, k = 1; k < size; j++, k++)
            collinearPoints[k] = po[j];
          Arrays.sort(collinearPoints);

          addOrDropIfExists(collinearPoints[0], collinearPoints[size - 1], slop);
        }

        if (nIsReachEnd)
          break;
        start = n;
        steps = 1;
        flag = true;
        slop = initPo[i].slopeTo(po[n]);
      }
    }
  }

  private double slopBetween(Point po1, Point po2) {
    double slopToPo2 = po1.slopeTo(po2);
    validateRepeatedPoint(slopToPo2);
    return slopToPo2;
  }

  private void addOrDropIfExists(Point head, Point end, double slop) {
    boolean lineIsExists = false;
    for (int i = 0; i < count; i++) {
      if (slops[i] == slop && heads[i].compareTo(head) == 0) {
        lineIsExists = true;
        break;
      }
    }

    if (!lineIsExists) {
      heads[count] = head;
      ends[count] = end;
      slops[count] = slop;
      count++;
    }
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
