package DRQ.codes.attempt1;

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
      System.out.println(each);
      count--;
      if (count <= 0)
        break;
    }
  }
}
