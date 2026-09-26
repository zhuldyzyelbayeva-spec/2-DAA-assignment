public class DynamicArray {

    private int[] data;
    private int size;

    private long accessCount;
    private long movementCount;
    private long comparisonCount;

    public DynamicArray() {
        data = new int[10];
        size = 0;
    }


    public void resetMetrics() {
        accessCount = 0;
        movementCount = 0;
        comparisonCount = 0;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public long getMovementCount() {
        return movementCount;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }


    private void resize() {

        int[] newData = new int[data.length * 2];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            movementCount++;
        }

        data = newData;
    }

    public void add(int x) {

        if (size == data.length) {
            resize();
        }

        data[size] = x;
        size++;
    }

    public void add(int index, int x) {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }

        if (size == data.length) {
            resize();
        }

        for (int i = size; i > index; i--) {

            data[i] = data[i - 1];
            movementCount++;
        }

        data[index] = x;
        size++;
    }


    public int remove(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }

        int removed = data[index];

        for (int i = index; i < size - 1; i++) {

            data[i] = data[i + 1];
            movementCount++;
        }

        size--;

        return removed;
    }


    public int get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }

        accessCount++;

        return data[index];
    }


    public boolean contains(int x) {

        for (int i = 0; i < size; i++) {

            accessCount++;
            comparisonCount++;

            if (data[i] == x) {
                return true;
            }
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

        size = 0;

        accessCount = 0;
        movementCount = 0;
        comparisonCount = 0;
    }
}