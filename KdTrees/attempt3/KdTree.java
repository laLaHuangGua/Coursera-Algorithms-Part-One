package KdTrees.attempt3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
  private static final boolean X_COORDINATE = true;
  private Node root = null;
  private int size = 0;

  private static class Node {
    Point2D point;
    Node parent;
    Node left;
    Node right;
    RectHV rect;
    boolean flag;

    public Node(Point2D p) {
      this.point = p;
    }

    public int compareTo(Node that) {
      if (that.flag == X_COORDINATE)
        return Double.compare(point.x(), that.point.x());
      else
        return Double.compare(point.y(), that.point.y());
    }
  }

  public KdTree() {
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public boolean contains(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();

    Node x = root;
    Node node = new Node(p);
    boolean isExists = false;

    while (x != null) {
      int cmp = node.compareTo(x);
      if (cmp >= 0) {
        if (cmp == 0 && node.point.equals(x.point)) {
          isExists = true;
          break;
        }
        x = x.right;
      } else
        x = x.left;
    }
    return isExists;
  }

  public void insert(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();
    if (root == null) {
      root = new Node(p);
      root.flag = X_COORDINATE;
      root.parent = null;
      root.rect = new RectHV(0, 0, 1, 1);
      size++;
      return;
    }

    if (!contains(p)) {
      Node node = new Node(p);
      root = put(root, node, 0);
      size++;
    }
  }

  private Node put(Node x, Node p, int direct) {
    if (x == null) {
      fillChildAttribute(p, p.parent, direct);
      return p;
    }

    p.parent = x;
    int cmp = p.compareTo(x);
    if (cmp >= 0)
      x.right = put(x.right, p, cmp);
    else
      x.left = put(x.left, p, cmp);
    return x;
  }

  private void fillChildAttribute(Node child, Node parent, int direct) {
    child.parent = parent;
    child.flag = !parent.flag;
    child.rect = computeRectBy(parent, direct);
  }

  private RectHV computeRectBy(Node parent, int direct) {
    if (parent.flag == X_COORDINATE) {
      if (direct < 0)
        return new RectHV(
            parent.rect.xmin(),
            parent.rect.ymin(),
            parent.point.x(),
            parent.rect.ymax());
      else
        return new RectHV(
            parent.point.x(),
            parent.rect.ymin(),
            parent.rect.xmax(),
            parent.rect.ymax());
    } else {
      if (direct < 0)
        return new RectHV(
            parent.rect.xmin(),
            parent.rect.ymin(),
            parent.rect.xmax(),
            parent.point.y());
      else
        return new RectHV(
            parent.rect.xmin(),
            parent.point.y(),
            parent.rect.xmax(),
            parent.rect.ymax());
    }
  }

  public void draw() {
    for (var each : points()) {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      StdDraw.point(each.point.x(), each.point.y());

      if (each.flag == X_COORDINATE) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(each.point.x(), each.rect.ymin(), each.point.x(), each.rect.ymax());
      } else {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        StdDraw.line(each.rect.xmin(), each.point.y(), each.rect.xmax(), each.point.y());
      }
    }
  }

  private Iterable<Node> points() {
    Queue<Node> q = new Queue<Node>();
    inOrder(root, q);
    return q;
  }

  private void inOrder(Node x, Queue<Node> q) {
    if (x == null)
      return;
    inOrder(x.left, q);
    q.enqueue(x);
    inOrder(x.right, q);
  }

  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null)
      throw new IllegalArgumentException();
    Queue<Point2D> q = new Queue<Point2D>();
    Node x = root;
    search(x, q, rect);
    return q;
  }

  private void search(Node x, Queue<Point2D> q, RectHV rect) {
    if (x == null)
      return;
    if (!x.rect.intersects(rect))
      return;
    if (rect.contains(x.point))
      q.enqueue(x.point);
    search(x.left, q, rect);
    search(x.right, q, rect);
    return;
  }

  public Point2D nearest(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();
    if (root == null)
      return null;

    Node x = root;
    Point2D nearest = new Point2D(root.point.x(), root.point.y());
    nearest = search(x, p, nearest);

    return nearest;
  }

  private Point2D search(Node x, Point2D query, Point2D nearest) {
    if (x == null)
      return null;

    double nearestDistance = query.distanceSquaredTo(nearest);
    double distance = query.distanceSquaredTo(x.point);
    if (distance < nearestDistance)
      nearest = x.point;
    else
      distance = nearestDistance;

    boolean canGoLeft = x.left != null
        && x.left.rect.distanceSquaredTo(query) <= distance;
    boolean canGoRight = x.right != null
        && x.right.rect.distanceSquaredTo(query) <= distance;

    if (canGoLeft && canGoRight) {
      if (x.left.rect.contains(query)) {
        nearest = search(x.left, query, nearest);
        nearest = search(x.right, query, nearest);
      } else {
        nearest = search(x.right, query, nearest);
        nearest = search(x.left, query, nearest);
      }
    } else if (canGoLeft && !canGoRight) {
      nearest = search(x.left, query, nearest);
    } else if (!canGoLeft && canGoRight) {
      nearest = search(x.right, query, nearest);
    } else {
      return nearest;
    }
    return nearest;
  }

  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }

    System.out.println(kdtree.nearest(new Point2D(0.8, 0.1)));

    // StdDraw.clear();
    // StdDraw.setPenColor(StdDraw.BLACK);
    // StdDraw.setPenRadius(0.01);
    // kdtree.draw();
    // StdDraw.show();
  }
}
