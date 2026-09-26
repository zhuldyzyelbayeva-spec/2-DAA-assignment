public class MyLinkedList {

    private static class Node {

        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    private long accessCount;
    private long pointerUpdateCount;
    private long comparisonCount;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void resetMetrics() {
        accessCount = 0;
        pointerUpdateCount = 0;
        comparisonCount = 0;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public long getPointerUpdateCount() {
        return pointerUpdateCount;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public void add(int x) {

        Node newNode = new Node(x);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public void add(int index, int x) {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }

        Node newNode = new Node(x);

        if (index == 0) {

            newNode.next = head;
            head = newNode;

            if (size == 0) {
                tail = newNode;
            }

            pointerUpdateCount++;
            size++;
            return;
        }

        if (index == size) {

            tail.next = newNode;
            tail = newNode;

            pointerUpdateCount++;
            size++;
            return;
        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
            accessCount++;
        }

        newNode.next = current.next;
        current.next = newNode;

        pointerUpdateCount += 2;
        size++;
    }

    public int remove(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }

        if (index == 0) {

            int removedValue = head.data;

            head = head.next;

            if (size == 1) {
                tail = null;
            }

            pointerUpdateCount++;
            size--;

            return removedValue;
        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
            accessCount++;
        }

        Node nodeToRemove = current.next;

        int removedValue = nodeToRemove.data;

        current.next = nodeToRemove.next;

        pointerUpdateCount++;

        if (nodeToRemove == tail) {
            tail = current;
        }

        size--;

        return removedValue;
    }

    public int get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
            accessCount++;
        }

        accessCount++;

        return current.data;
    }

    public boolean contains(int x) {

        Node current = head;

        while (current != null) {

            accessCount++;
            comparisonCount++;

            if (current.data == x) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;

        resetMetrics();
    }
}