// Attempt1: get scores 99/100, fail in styles and a corner case
package DRQ.codes.attempt1;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

  private Node<Item> first;
  private Node<Item> last;
  private int size;

  private static class Node<Item> {
    Item item;
    Node<Item> next;
    Node<Item> prev;

    @Override
    public String toString() {
      return "Node [item=" + item + "]";
    }
  }

  public Deque() {
    first = null;
    last = null;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void addFirst(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Argument can't be null");
    Node<Item> oldfirst = first;
    first = new Node<Item>();
    first.item = item;
    first.prev = null;
    first.next = oldfirst;
    if (this.isEmpty())
      last = first;
    else
      oldfirst.prev = first;
    size++;
  }

  public void addLast(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Argument can't be null");
    Node<Item> oldlast = last;
    last = new Node<Item>();
    last.item = item;
    last.next = null;
    last.prev = oldlast;
    if (this.isEmpty())
      first = last;
    else
      oldlast.next = last;
    size++;
  }

  public Item removeFirst() {
    if (this.isEmpty())
      throw new NoSuchElementException("Deque is empty");
    Item item = first.item;
    first = first.next;
    if (first != null)
      first.prev = null;
    size--;

    if (this.isEmpty())
      last = first;
    return item;

  }

  public Item removeLast() {
    if (this.isEmpty())
      throw new NoSuchElementException("Deque is empty");
    Item item = last.item;
    last = last.prev;
    if (last != null)
      last.next = null;
    size--;

    if (this.isEmpty())
      first = null;
    return item;
  }

  @Override
  public Iterator<Item> iterator() {
    return new DequeIterator(first);
  }

  private class DequeIterator implements Iterator<Item> {
    private Node<Item> current;

    public DequeIterator(Node<Item> first) {
      current = first;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Don't support remove operation");
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public Item next() {
      if (!hasNext())
        throw new NoSuchElementException("There is no more item to return");
      Item item = current.item;
      current = current.next;
      return item;
    }
  }

  public static void main(String[] args) {
    Deque<String> deque = new Deque<String>();

    while (!StdIn.isEmpty()) {
      String s = StdIn.readString();
      if (!s.equals("-")) {
        if (deque.size() % 2 == 0)
          deque.addFirst(s);
        else
          deque.addLast(s);
      } else if (!deque.isEmpty()) {

        if (deque.size() % 2 == 0)
          StdOut.print(deque.removeFirst() + " ");
        else
          StdOut.print(deque.removeLast() + " ");
      }
    }
  }

}
