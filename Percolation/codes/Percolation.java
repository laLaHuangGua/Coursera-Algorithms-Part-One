package Percolation.codes;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private static final int ABOVE = 0;
  private static final int BELOW = 1;
  private static final int LEFT = 2;
  private static final int RIGHT = 3;

  private WeightedQuickUnionUF sites;
  private boolean[][] hasOpen;
  private boolean[] reachBottom;
  private int totalOpenSites;
  private int sitesPerCol;
  private int virtualSite;

  public Percolation(int n) {
    if (n <= 0)
      throw new IllegalArgumentException(
          "the number is not greater than 0");

    sitesPerCol = n;
    reachBottom = new boolean[n * n + 1];
    int firstSiteOfLastRow = computeSerialNum(n, 1);

    // create a n*n grid plus one virtual site
    sites = new WeightedQuickUnionUF(n * n + 1);

    // all sites(except one virtual site) are initially blocked (represent by 0)
    hasOpen = new boolean[n + 1][n + 1];
    totalOpenSites = 0;

    // the serial num of (top most) virtual site
    virtualSite = n * n;

    // connect the sites[n*n]: row n, col 1 to the top row of grid
    for (int i = 0; i < n; i++) {
      sites.union(n * n, i);
      reachBottom[firstSiteOfLastRow + i] = true;
    }
  }

  public void open(int row, int col) {
    validateColAndRow(row, col);

    if (hasOpen[row][col])
      return;

    hasOpen[row][col] = true;
    totalOpenSites++;

    int site = computeSerialNum(row, col);

    boolean[] canUnionSite = confirmStates(row, col);

    if (canUnionSite[ABOVE]) {
      int siteAbove = site - sitesPerCol;
      if (oneOfSitesReachBottom(site, siteAbove)) {
        sites.union(siteAbove, site);
        reachBottom[rootOf(site)] = true;
      } else {
        sites.union(siteAbove, site);
      }
    }

    if (canUnionSite[BELOW]) {
      int siteBelow = site + sitesPerCol;
      if (oneOfSitesReachBottom(site, siteBelow)) {
        sites.union(siteBelow, site);
        reachBottom[rootOf(site)] = true;
      } else {
        sites.union(siteBelow, site);
      }
    }

    if (canUnionSite[LEFT]) {
      int siteLeft = site - 1;
      if (oneOfSitesReachBottom(site, siteLeft)) {
        sites.union(siteLeft, site);
        reachBottom[rootOf(site)] = true;
      } else {
        sites.union(siteLeft, site);
      }
    }

    if (canUnionSite[RIGHT]) {
      int siteRight = site + 1;
      if (oneOfSitesReachBottom(site, siteRight)) {
        sites.union(siteRight, site);
        reachBottom[rootOf(site)] = true;
      } else {
        sites.union(siteRight, site);
      }
    }
  }

  public boolean isOpen(int row, int col) {
    validateColAndRow(row, col);
    return hasOpen[row][col];

  }

  public boolean isFull(int row, int col) {
    validateColAndRow(row, col);
    int site = computeSerialNum(row, col);
    return hasOpen[row][col]
        && sites.find(site) == sites.find(virtualSite);
  }

  public int numberOfOpenSites() {
    return totalOpenSites;
  }

  public boolean percolates() {
    int virtualSiteRoot = sites.find(virtualSite);
    if (totalOpenSites < 1)
      return false;
    if (sitesPerCol == 1 && totalOpenSites == 1)
      return true;
    if (reachBottom[virtualSiteRoot])
      return true;
    return false;
  }

  private void validate(int num) {
    if (num <= 0 || num > sitesPerCol) {
      throw new IllegalArgumentException(
          "row or col " + num + " is not between 1 and " + sitesPerCol);
    }
  }

  private int rootOf(int num) {
    return sites.find(num);
  }

  private boolean oneOfSitesReachBottom(int site1, int site2) {
    return reachBottom[rootOf(site1)] || reachBottom[rootOf(site2)];
  }

  private void validateColAndRow(int row, int col) {
    validate(col);
    validate(row);
  }

  private int computeSerialNum(int row, int col) {
    return bias(row) * sitesPerCol + bias(col);
  }

  private boolean[] confirmStates(int row, int col) {
    // this initialization seems to be stupid, but if I use the code:
    // "boolean[] positionStates = new boolean[4]"
    // the ASSESS DETAILS will say,
    // "The numeric literal '4' appears to be unnecessary."
    // this statement makes me frustrated
    boolean[] positionStates = { false, false, false, false };

    // if the site above exists and is opened
    if (row - 1 > 0 && hasOpen[row - 1][col])
      positionStates[0] = true;

    // if the site below exists and is opened
    if (row + 1 <= sitesPerCol && hasOpen[row + 1][col])
      positionStates[1] = true;

    // if the site on the left exists and is opened
    if (col - 1 > 0 && hasOpen[row][col - 1])
      positionStates[2] = true;

    // if the site on the right exists and is opened
    if (col + 1 <= sitesPerCol && hasOpen[row][col + 1])
      positionStates[3] = true;
    return positionStates;
  }

  private int bias(int num) {
    return num - 1;
  }

  public static void main(String[] args) {
    int n = 4;
    Percolation p = new Percolation(n);

    while (!p.percolates()) {
      int openSiteRow = StdRandom.uniformInt(1, n + 1);
      int openSiteCol = StdRandom.uniformInt(1, n + 1);
      if (!p.isOpen(openSiteRow, openSiteCol)) {
        System.out.println("Open: row " + openSiteRow + ", col " + openSiteCol);
        p.open(openSiteRow, openSiteCol);
      }
    }
    System.out.println("total open site: " + p.totalOpenSites);

  }
}