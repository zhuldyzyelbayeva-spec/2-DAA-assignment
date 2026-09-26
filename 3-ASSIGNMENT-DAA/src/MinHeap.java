public class MinHeap {

    private int[] heap;
    private int size;

    private long comparisonCount;

    public MinHeap() {
        heap = new int[10];
        size = 0;
    }


    public void resetMetrics() {
        comparisonCount = 0;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    private void resize() {

        int[] newHeap = new int[heap.length * 2];

        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
    }


    public void insert(int x) {

        if (size == heap.length) {
            resize();
        }

        heap[size] = x;

        int current = size;
        size++;

        while (current > 0) {

            int parent = (current - 1) / 2;

            comparisonCount++;

            if (heap[current] >= heap[parent]) {
                break;
            }

            int temp = heap[current];
            heap[current] = heap[parent];
            heap[parent] = temp;

            current = parent;
        }
    }


    public int peekMin() {

        if (size == 0) {
            throw new RuntimeException("Empty heap");
        }

        return heap[0];
    }


    public int extractMin() {

        if (size == 0) {
            throw new RuntimeException("Empty heap");
        }

        int min = heap[0];

        heap[0] = heap[size - 1];
        size--;

        int current = 0;

        while (true) {

            int left = 2 * current + 1;
            int right = 2 * current + 2;

            int smallest = current;

            if (left < size) {

                comparisonCount++;

                if (heap[left] < heap[smallest]) {
                    smallest = left;
                }
            }

            if (right < size) {

                comparisonCount++;

                if (heap[right] < heap[smallest]) {
                    smallest = right;
                }
            }

            if (smallest == current) {
                break;
            }

            int temp = heap[current];
            heap[current] = heap[smallest];
            heap[smallest] = temp;

            current = smallest;
        }

        return min;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isValidHeap() {

        for (int i = 0; i < size; i++) {

            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < size && heap[i] > heap[left]) {
                return false;
            }

            if (right < size && heap[i] > heap[right]) {
                return false;
            }
        }

        return true;
    }
}