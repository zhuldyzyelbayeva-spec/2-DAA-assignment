public class Tests {

    private static void expectException(Runnable action) {
        boolean thrown = false;

        try {
            action.run();
        } catch (RuntimeException e) {
            thrown = true;
        }

        assert thrown : "Expected RuntimeException was not thrown";
    }

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();

        System.out.println();
        System.out.println("ALL TESTS PASSED");
    }


    private static void testDynamicArray() {
        DynamicArray array = new DynamicArray();

        assert array.isEmpty();
        assert array.size() == 0;

        array.add(10);
        array.add(20);
        array.add(30);

        assert array.size() == 3;
        assert !array.isEmpty();

        int value = array.get(0);
        assert value == 10;

        value = array.get(1);
        assert value == 20;

        value = array.get(2);
        assert value == 30;

        array.add(20);

        assert array.size() == 4;

        boolean found = array.contains(20);
        assert found;

        found = array.contains(30);
        assert found;

        found = array.contains(999);
        assert !found;

        array.add(0, 5);

        assert array.size() == 5;

        value = array.get(0);
        assert value == 5;

        value = array.get(1);
        assert value == 10;

        array.add(2, 15);

        assert array.size() == 6;

        value = array.get(0);
        assert value == 5;

        value = array.get(1);
        assert value == 10;

        value = array.get(2);
        assert value == 15;

        value = array.get(3);
        assert value == 20;

        array.add(array.size(), 40);

        assert array.size() == 7;

        value = array.get(array.size() - 1);
        assert value == 40;

        array.remove(0);

        assert array.size() == 6;

        value = array.get(0);
        assert value == 10;

        array.remove(1);

        assert array.size() == 5;

        value = array.get(0);
        assert value == 10;

        value = array.get(1);
        assert value == 20;

        int lastIndex = array.size() - 1;
        array.remove(lastIndex);

        assert array.size() == 4;

        array.clear();

        assert array.isEmpty();
        assert array.size() == 0;

        found = array.contains(10);
        assert !found;

        array.add(100);
        array.add(200);

        assert array.size() == 2;

        value = array.get(0);
        assert value == 100;

        value = array.get(1);
        assert value == 200;

        expectException(() -> array.get(-1));
        expectException(() -> array.get(array.size()));

        expectException(() -> array.remove(-1));
        expectException(() -> array.remove(array.size()));

        expectException(() -> array.add(-1, 50));
        expectException(() -> array.add(array.size() + 1, 50));

        DynamicArray large = new DynamicArray();

        for (int i = 0; i < 10000; i++) {
            large.add(i);
        }

        assert large.size() == 10000;

        value = large.get(0);
        assert value == 0;

        value = large.get(1);
        assert value == 1;

        value = large.get(999);
        assert value == 999;

        value = large.get(5000);
        assert value == 5000;

        value = large.get(9999);
        assert value == 9999;

        System.out.println("DynamicArray tests passed.");
    }


    private static void testLinkedList() {
        MyLinkedList list = new MyLinkedList();

        assert list.isEmpty();
        assert list.size() == 0;

        list.add(10);
        list.add(20);
        list.add(30);

        assert list.size() == 3;
        assert !list.isEmpty();

        int value = list.get(0);
        assert value == 10;

        value = list.get(1);
        assert value == 20;

        value = list.get(2);
        assert value == 30;

        list.add(20);

        assert list.size() == 4;

        boolean found = list.contains(20);
        assert found;

        found = list.contains(30);
        assert found;

        found = list.contains(999);
        assert !found;

        list.add(0, 5);

        assert list.size() == 5;

        value = list.get(0);
        assert value == 5;

        value = list.get(1);
        assert value == 10;

        list.add(2, 15);

        assert list.size() == 6;

        value = list.get(0);
        assert value == 5;

        value = list.get(1);
        assert value == 10;

        value = list.get(2);
        assert value == 15;

        value = list.get(3);
        assert value == 20;

        list.add(list.size(), 40);

        assert list.size() == 7;

        value = list.get(list.size() - 1);
        assert value == 40;

        list.remove(0);

        assert list.size() == 6;

        value = list.get(0);
        assert value == 10;

        list.remove(1);

        assert list.size() == 5;

        value = list.get(0);
        assert value == 10;

        value = list.get(1);
        assert value == 20;

        int lastIndex = list.size() - 1;
        list.remove(lastIndex);

        assert list.size() == 4;

        list.clear();

        assert list.isEmpty();
        assert list.size() == 0;

        found = list.contains(10);
        assert !found;

        list.add(100);
        list.add(200);

        assert list.size() == 2;

        value = list.get(0);
        assert value == 100;

        value = list.get(1);
        assert value == 200;

        expectException(() -> list.get(-1));
        expectException(() -> list.get(list.size()));

        expectException(() -> list.remove(-1));
        expectException(() -> list.remove(list.size()));

        expectException(() -> list.add(-1, 50));
        expectException(() -> list.add(list.size() + 1, 50));

        MyLinkedList large = new MyLinkedList();

        for (int i = 0; i < 10000; i++) {
            large.add(i);
        }

        assert large.size() == 10000;

        value = large.get(0);
        assert value == 0;

        value = large.get(1);
        assert value == 1;

        value = large.get(999);
        assert value == 999;

        value = large.get(5000);
        assert value == 5000;

        value = large.get(9999);
        assert value == 9999;

        System.out.println("MyLinkedList tests passed.");
    }


    private static void testMinHeap() {
        MinHeap heap = new MinHeap();

        assert heap.isEmpty();
        assert heap.size() == 0;

        heap.insert(50);
        heap.insert(20);
        heap.insert(40);
        heap.insert(10);
        heap.insert(30);

        assert heap.size() == 5;
        assert !heap.isEmpty();
        assert heap.isValidHeap();

        int value = heap.peekMin();
        assert value == 10;

        value = heap.extractMin();
        assert value == 10;
        assert heap.isValidHeap();

        value = heap.extractMin();
        assert value == 20;
        assert heap.isValidHeap();

        value = heap.extractMin();
        assert value == 30;
        assert heap.isValidHeap();

        value = heap.extractMin();
        assert value == 40;
        assert heap.isValidHeap();

        value = heap.extractMin();
        assert value == 50;
        assert heap.isValidHeap();

        assert heap.isEmpty();
        assert heap.size() == 0;

        heap.insert(5);
        heap.insert(5);
        heap.insert(10);
        heap.insert(1);

        assert heap.size() == 4;
        assert heap.isValidHeap();

        value = heap.peekMin();
        assert value == 1;

        value = heap.extractMin();
        assert value == 1;

        value = heap.extractMin();
        assert value == 5;

        value = heap.extractMin();
        assert value == 5;

        value = heap.extractMin();
        assert value == 10;

        assert heap.isEmpty();

        heap.insert(-10);
        heap.insert(5);
        heap.insert(-30);
        heap.insert(0);

        assert heap.isValidHeap();

        value = heap.peekMin();
        assert value == -30;

        value = heap.extractMin();
        assert value == -30;

        value = heap.extractMin();
        assert value == -10;

        value = heap.extractMin();
        assert value == 0;

        value = heap.extractMin();
        assert value == 5;

        assert heap.isEmpty();

        expectException(heap::peekMin);
        expectException(heap::extractMin);

        MinHeap large = new MinHeap();

        for (int i = 10000; i >= 0; i--) {
            large.insert(i);
            assert large.isValidHeap();
        }

        assert large.size() == 10001;

        int previous = Integer.MIN_VALUE;

        while (!large.isEmpty()) {
            int current = large.extractMin();

            assert current >= previous;
            assert large.isValidHeap();

            previous = current;
        }

        assert large.isEmpty();
        assert large.size() == 0;

        System.out.println("MinHeap tests passed.");
    }
}