package Percolation.codes;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private static final double CONFIDENCE_95 = 1.96;

  private double[] threshold;
  private double sampleMean;
  private double sampleStdDev;

  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0)
      throw new IllegalArgumentException(
          "Both numbers need to be greater than 0");

    threshold = new double[trials];

    for (int i = 0; i < trials; i++) {
      Percolation p = new Percolation(n);
      while (!p.percolates()) {
        int openSiteRow = StdRandom.uniformInt(1, n + 1);
        int openSiteCol = StdRandom.uniformInt(1, n + 1);
        if (!p.isOpen(openSiteRow, openSiteCol))
          p.open(openSiteRow, openSiteCol);
      }
      threshold[i] = (double) p.numberOfOpenSites() / (n * n);
    }

    sampleMean = StdStats.mean(threshold);
    sampleStdDev = StdStats.stddev(threshold);
  }

  public double mean() {
    return sampleMean;
  }

  public double stddev() {
    return sampleStdDev;
  }

  public double confidenceLo() {
    return sampleMean
        - CONFIDENCE_95 * sampleStdDev
            / Math.sqrt((double) threshold.length);
  }

  public double confidenceHi() {
    return sampleMean
        + CONFIDENCE_95 * sampleStdDev
            / Math.sqrt((double) threshold.length);
  }

  public static void main(String[] args) {
    int dimension = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats pStats = new PercolationStats(dimension, trials);

    StdOut.print("mean = " + pStats.mean() + "\n");
    StdOut.print("stddev = " + pStats.stddev() + "\n");
    StdOut.print(
        "95% confidence interval = "
            + "["
            + pStats.confidenceLo()
            + ", "
            + pStats.confidenceHi()
            + "]"
            + "\n");
  }
}
