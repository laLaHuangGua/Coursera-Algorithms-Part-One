package DRQ.codes.attempt1;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private static final int INITIAL_CAPACITY = 6;
  private Item[] items;
  private int size;
  private int N;

  public RandomizedQueue() {
    items = (Item[]) new Object[INITIAL_CAPACITY];
    size = 0;
    N = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void enqueue(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Argument can't be null");
  }

  public Item dequeue() {
    if (this.isEmpty())
      throw new NoSuchElementException("Queue is empty");
    return null;
  }

  public Item sample() {
    if (this.isEmpty())
      throw new NoSuchElementException("Queue is empty");
    return null;
  }

  public Iterator<Item> iterator() {
    return null;
  }

  public static void main(String[] args) {
  }

}
