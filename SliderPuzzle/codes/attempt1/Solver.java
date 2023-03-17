package SliderPuzzle.codes.attempt1;

import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
  private final ArrayList<Board> solution;
  private final int solutionMoves;

  private static class Node implements Comparable<Node> {
    Board board;
    int moves;
    Node prev;
    int priority;

    public Node(Board current, Node previous) {
      board = current;
      prev = previous;
      moves = (previous == null) ? 0 : previous.moves + 1;
      priority = current.manhattan() + moves;
    }

    @Override
    public int compareTo(Node that) {
      if (priority > that.priority)
        return 1;
      if (priority < that.priority)
        return -1;
      return 0;
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null)
      throw new IllegalArgumentException("Argument is null");

    Node initNode = new Node(initial, null);
    Node initNodeTwin = new Node(initial.twin(), null);

    MinPQ<Node> queuMinPQ = initializeQueue(initial, initNode);
    MinPQ<Node> queuMinPQTwin = initializeQueue(initial.twin(), initNodeTwin);

    solution = new ArrayList<Board>();
    solution.add(initNode.board);

    Node current = queuMinPQ.delMin();
    Node currentTwin = queuMinPQTwin.delMin();
    while (current.board.manhattan() != 0 && currentTwin.board.manhattan() != 0) {
      solution.add(current.board);
      for (var each : current.board.neighbors()) {
        boolean flag = false;
        if (!flag && each.equals(current.prev.board)) {
          flag = true;
          continue;
        }
        queuMinPQ.insert(new Node(each, current));
      }
      current = queuMinPQ.delMin();

      for (var each : currentTwin.board.neighbors()) {
        boolean flag = false;
        if (!flag && each.equals(currentTwin.prev.board)) {
          flag = true;
          continue;
        }
        queuMinPQTwin.insert(new Node(each, currentTwin));
      }
      currentTwin = queuMinPQTwin.delMin();
    }

    if (currentTwin.board.manhattan() == 0) {
      solution.clear();
      solutionMoves = -1;
    } else {
      solution.add(current.board);
      solutionMoves = current.moves;
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return moves() != -1;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    return solutionMoves;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    return solution;
  }

  private MinPQ<Node> initializeQueue(Board initial, Node initNode) {
    MinPQ<Node> queuMinPQ = new MinPQ<Node>();
    for (var each : initial.neighbors()) {
      Node eachNode = new Node(each, initNode);
      queuMinPQ.insert(eachNode);
    }
    return queuMinPQ;
  }

  // test client (see below)
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}
