package DRQ.codes.attempt2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] items;
  private int n;

  public RandomizedQueue() {
    items = (Item[]) new Object[6];
    n = 0;
  }

  public boolean isEmpty() {
    return n == 0;
  }

  public int size() {
    return n;
  }

  public void enqueue(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Argument can't be null");
    if (items.length == n)
      resize(2 * items.length);
    items[n++] = item;
  }

  public Item dequeue() {
    if (this.isEmpty())
      throw new NoSuchElementException("Queue is empty");
    if (n > 0 && n == items.length / 4)
      resize(items.length / 2);

    int target = StdRandom.uniformInt(n);

    Item temp = items[target];
    items[target] = items[n - 1];
    items[n - 1] = temp;

    Item item = items[--n];
    items[n] = null;

    return item;
  }

  private void resize(int capacity) {
    Item[] copy = (Item[]) new Object[capacity];
    for (int i = 0; i < n; i++)
      copy[i] = items[i];
    items = copy;
  }

  public Item sample() {
    if (this.isEmpty())
      throw new NoSuchElementException("Queue is empty");
    int target = StdRandom.uniformInt(n);
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
      int[] normalOrder = new int[n];
      for (int i = 0; i < n; i++)
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
      return count < n;
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
