// Attempt2: get scores 97/100, fail in correctness
package KdTrees.attempt2;

import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
  private SET<Point2D> pointSet;

  public PointSET() {
    pointSet = new SET<Point2D>();
  }

  public boolean isEmpty() {
    return pointSet.isEmpty();
  }

  public int size() {
    return pointSet.size();
  }

  public void insert(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();

    if (!pointSet.contains(p))
      pointSet.add(p);
  }

  public boolean contains(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();
    return pointSet.contains(p);
  }

  public void draw() {
    for (var each : pointSet)
      each.draw();
  }

  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null)
      throw new IllegalArgumentException();

    ArrayList<Point2D> pointsInRange = new ArrayList<Point2D>();
    for (var each : pointSet) {
      if (each.x() < rect.xmin()
          || each.x() > rect.xmax()
          || each.y() < rect.ymin()
          || each.y() > rect.ymax())
        continue;
      pointsInRange.add(each);
    }
    return pointsInRange;
  }

  public Point2D nearest(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();
    if (pointSet.isEmpty())
      return null;

    Point2D closestPoint = null;
    double minDistance = 10.0;
    for (var each : pointSet) {
      if (minDistance > p.distanceSquaredTo(each)) {
        closestPoint = each;
        minDistance = p.distanceSquaredTo(each);
      }
    }
    return closestPoint;
  }

  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    PointSET brute = new PointSET();

    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      brute.insert(p);
    }

    System.out.println(brute.nearest(new Point2D(0.100, 0.100)));
  }
}
