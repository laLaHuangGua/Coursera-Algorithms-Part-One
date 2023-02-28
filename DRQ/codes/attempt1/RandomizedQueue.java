// Attempt1: get scores 99/100, fail in styles and a corner case
package DRQ.codes.attempt1;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private static final int INITIAL_CAPACITY = 6;
  private Item[] items;
  private int N;

  public RandomizedQueue() {
    items = (Item[]) new Object[INITIAL_CAPACITY];
    N = 0;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  public void enqueue(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Argument can't be null");
    if (items.length == N)
      resize(2 * items.length);
    items[N++] = item;
  }

  public Item dequeue() {
    if (this.isEmpty())
      throw new NoSuchElementException("Queue is empty");
    if (N > 0 && N == items.length / 4)
      resize(items.length / 2);

    int target = StdRandom.uniformInt(N);

    Item temp = items[target];
    items[target] = items[N - 1];
    items[N - 1] = temp;

    Item item = items[--N];
    items[N] = null;

    return item;
  }

  private void resize(int capacity) {
    Item[] copy = (Item[]) new Object[capacity];
    for (int i = 0; i < N; i++)
      copy[i] = items[i];
    items = copy;
  }

  public Item sample() {
    if (this.isEmpty())
      throw new NoSuchElementException("Queue is empty");
    int target = StdRandom.uniformInt(N);
    return items[target];
  }

  @Override
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    int count;
    int[] randomOrder;

    public RandomizedQueueIterator() {
      count = 0;
      int[] normalOrder = new int[N];
      for (int i = 0; i < N; i++)
        normalOrder[i] = i;
      StdRandom.shuffle(normalOrder);
      randomOrder = normalOrder;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Don't support remove operation");
    }

    @Override
    public boolean hasNext() {
      return count < N;
    }

    @Override
    public Item next() {
      if (!hasNext())
        throw new NoSuchElementException("There is no more item to return");
      Item item = items[randomOrder[count]];
      count++;
      return item;
    }
  }

  public static void main(String[] args) {
    var queue = new RandomizedQueue<String>();
    queue.enqueue("hello");
    queue.enqueue("world");
    queue.enqueue("I");
    queue.enqueue("I love you");
    queue.enqueue("hahhaa");
    queue.dequeue();
    for (var each : queue) {
      System.out.println(each);
    }
  }

}
