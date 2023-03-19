package SliderPuzzle.codes.attempt2;

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
    public boolean equals(Object obj) {
      if (obj == null)
        return false;
      if (obj.getClass() != this.getClass())
        return false;
      Node objNode = (Node) obj;
      return board.equals(objNode.board);
    }

    @Override
    public int compareTo(Node that) {
      if (priority > that.priority)
        return 1;
      else if (priority < that.priority)
        return -1;
      return 0;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + moves;
      result = prime * result + priority;
      return result;
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null)
      throw new IllegalArgumentException("Argument is null");

    solution = new ArrayList<Board>();
    if (initial.manhattan() == 0) {
      solutionMoves = 0;
      solution.add(initial);
    } else
      solutionMoves = search(initial);
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

  private int search(Board initial) {
    Node initNode = new Node(initial, null);
    MinPQ<Node> queuMinPQ = new MinPQ<Node>();

    for (var each : initial.neighbors())
      queuMinPQ.insert(new Node(each, initNode));
    Node current = queuMinPQ.delMin();

    while (!current.board.isGoal()) {
      Board currentPrevBoard = current.prev.board;

      for (var board : current.board.neighbors())
        if (!board.equals(currentPrevBoard))
          queuMinPQ.insert(new Node(board, current));

      current = queuMinPQ.delMin();
    }

    int moves = current.moves;
    ArrayList<Board> temp = new ArrayList<Board>();
    while (current != null) {
      temp.add(current.board);
      current = current.prev;
    }

    for (int i = temp.size() - 1; i >= 0; i--)
      solution.add(temp.get(i));

    return moves;
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
