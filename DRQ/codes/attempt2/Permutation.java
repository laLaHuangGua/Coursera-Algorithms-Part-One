package DRQ.codes.attempt2;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
  public static void main(String[] args) {
    int count = Integer.parseInt(args[0]);
    var queue = new RandomizedQueue<String>();

    while (!StdIn.isEmpty()) {
      String item = StdIn.readString();
      queue.enqueue(item);
    }

    for (String each : queue) {
      count--;
      if (count < 0)
        break;
      System.out.println(each);
    }
  }
}
