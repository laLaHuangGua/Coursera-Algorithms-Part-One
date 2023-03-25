package KdTrees.attempt1;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
  private static final boolean X_COORDINATE = true;
  private Node root = null;
  private int size = 0;

  private class Node implements Comparable<Node> {
    Point2D point;
    Node parent;
    Node left;
    Node right;
    RectHV rect;
    boolean flag;

    public Node(Point2D p) {
      this.point = p;
    }

    @Override
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

  public void insert(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();
    if (root == null) {
      root = new Node(p);
      root.flag = X_COORDINATE;
      root.parent = null;
      root.rect = new RectHV(0, 0, 1, 1);
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
    if (cmp > 0)
      x.right = put(x.right, p, cmp);
    else if (cmp < 0)
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

  public boolean contains(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException();

    Node x = root;
    Node node = new Node(p);
    boolean isExists = false;

    while (x != null) {
      int cmp = node.compareTo(x);
      if (cmp > 0)
        x = x.right;
      else if (cmp < 0)
        x = x.left;
      else {
        if (node.point.compareTo(x.point) == 0)
          isExists = true;
        break;
      }
    }
    return isExists;
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

    Node x = root;
    Point2D nearest = new Point2D(root.point.x(), root.point.y());
    nearest = search(x, p, nearest);

    return nearest;
  }

  private Point2D search(Node x, Point2D query, Point2D nearest) {
    if (x == null)
      return null;

    double distance = query.distanceTo(x.point);
    boolean canGoLeft = x.left != null && x.left.point.distanceTo(query) < distance;
    boolean canGoRight = x.right != null && x.right.point.distanceTo(query) < distance;

    if (canGoLeft && canGoRight) {
      if (x.left.rect.contains(query))
        nearest = search(x.left, query, x.left.point);
      else if (x.right.rect.contains(query))
        nearest = search(x.right, query, x.right.point);
    } else if (canGoLeft && !canGoRight) {
      nearest = search(x.left, query, x.left.point);
    } else if (!canGoLeft && canGoRight) {
      nearest = search(x.right, query, x.right.point);
    } else {
      return nearest;
    }
    return nearest;
  }

  public static void main(String[] args) {

    KdTree tree = new KdTree();
    tree.insert(new Point2D(0.7, 0.2));
    tree.insert(new Point2D(0.5, 0.4));
    tree.insert(new Point2D(0.2, 0.3));
    tree.insert(new Point2D(0.4, 0.7));
    tree.insert(new Point2D(0.9, 0.6));

    Point2D query = new Point2D(0.3, 0.2);
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    query.draw();

    System.out.println(tree.nearest(query));
    tree.draw();

    StdDraw.show();
  }
}
