package SliderPuzzle.codes.attempt1;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;

public class Board {
  private final int dimension;
  private final int[][] tiles;
  private final int blankTileRow;
  private final int blankTileCol;
  private final int hamming;
  private final int manhattan;
  private final String tilesString;

  public Board(int[][] tiles) {
    boolean flag = false;
    if (tiles[0][0] == 0 && tiles[0][1] == 0) {
      dimension = tiles[0].length - 1;
      flag = true;
    } else
      dimension = tiles[0].length;

    int wrongTiles = 0;
    int distance = 0;
    int blankTileRowLocal = 0;
    int blankTileColLocal = 0;
    String stringBoard = dimension + "\n";

    int n = dimension + 1;
    this.tiles = new int[n][n];
    int start = bias(0, flag);
    int end = bias(dimension, flag);

    for (int i = start; i < end; i++) {
      for (int j = start; j < end; j++) {
        int row = bias(i, !flag);
        int col = bias(j, !flag);

        this.tiles[row][col] = tiles[i][j];
        int tileVal = this.tiles[row][col];
        if (tileVal == 0) {
          blankTileRowLocal = row;
          blankTileColLocal = col;
        } else if (tileVal != goalTileValOf(row, col)) {
          wrongTiles++;
          distance += computeDistanceFromGoalTo(row, col, tileVal);
        }
        stringBoard += tiles[i][j] + " ";
      }
      stringBoard += "\n";
    }

    tilesString = stringBoard;
    hamming = wrongTiles;
    manhattan = distance;
    blankTileRow = blankTileRowLocal;
    blankTileCol = blankTileColLocal;
  }

  public String toString() {
    return tilesString;
  }

  public int dimension() {
    return dimension;
  }

  public int hamming() {
    return hamming;
  }

  public int manhattan() {
    return manhattan;
  }

  public boolean isGoal() {
    return hamming == 0;
  }

  public boolean equals(Object y) {
    if (y == null)
      return false;

    Board that = (Board) y;
    if (that.dimension == dimension && Arrays.equals(that.tiles, tiles))
      return true;
    return false;
  }

  public Iterable<Board> neighbors() {
    var neighborBoards = new ArrayList<Board>();

    boolean[] canMoveTowards = findNeighborsDirections();
    if (canMoveTowards[0])
      neighborBoards.add(new Board(swapBlankTileTo(blankTileRow + 1, blankTileCol)));
    if (canMoveTowards[1])
      neighborBoards.add(new Board(swapBlankTileTo(blankTileRow - 1, blankTileCol)));
    if (canMoveTowards[2])
      neighborBoards.add(new Board(swapBlankTileTo(blankTileRow, blankTileCol - 1)));
    if (canMoveTowards[3])
      neighborBoards.add(new Board(swapBlankTileTo(blankTileRow, blankTileCol + 1)));

    return neighborBoards;
  }

  public Board twin() {
    if (blankTileRow + 1 <= dimension)
      return new Board(swapBlankTileTo(blankTileRow + 1, blankTileCol));
    else
      return new Board(swapBlankTileTo(blankTileRow - 1, blankTileCol));
  }

  private int bias(int n, boolean flag) {
    if (flag)
      return n + 1;
    return n;
  }

  private int goalTileValOf(int row, int col) {
    if (row == dimension && col == dimension)
      return 0;
    return (row - 1) * dimension + col;
  }

  private int computeDistanceFromGoalTo(int row, int col, int tileVal) {
    int goalRow = tileVal / dimension + 1;
    int goalCol = tileVal % dimension;
    if (goalCol == 0) {
      goalCol = dimension;
      goalRow -= 1;
    }
    return Math.abs(row - goalRow) + Math.abs(col - goalCol);
  }

  private int[][] swapBlankTileTo(int destRow, int destCol) {
    int[][] aCopy = copyOf(tiles);

    int temp = aCopy[destRow][destCol];
    aCopy[destRow][destCol] = aCopy[blankTileRow][blankTileCol];
    aCopy[blankTileRow][blankTileCol] = temp;
    return aCopy;
  }

  private boolean[] findNeighborsDirections() {
    boolean[] directions = new boolean[] { false, false, false, false };

    // direct[0] is up, direct[1] is down, direct[2] is left, direct[3] is right
    if (blankTileRow + 1 <= dimension)
      directions[0] = true;
    if (blankTileRow - 1 >= 1)
      directions[1] = true;
    if (blankTileCol - 1 >= 1)
      directions[2] = true;
    if (blankTileCol + 1 <= dimension)
      directions[3] = true;

    return directions;
  }

  private int[][] copyOf(int[][] origin) {
    int size = origin.length;
    int[][] aCopy = new int[size][size];
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        aCopy[i][j] = origin[i][j];
    return aCopy;
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    System.out.println(initial);
    for (var each : initial.neighbors()) {
      System.out.println(each);
    }

    System.out.println(initial.twin());
  }
}
