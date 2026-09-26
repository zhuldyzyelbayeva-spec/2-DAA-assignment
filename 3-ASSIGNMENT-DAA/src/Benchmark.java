import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {
            100, 1_000, 10_000, 100_000
    };

    private static final int RUNS = 5;
    private static final int WARMUP_RUNS = 3;

    private static final int RANDOM_ACCESS_OPS = 10_000;
    private static final int SEARCH_OPS = 1_000;
    private static final int INSERT_REMOVE_OPS = 1_000;

    private static final long RANDOM_SEED = 42L;

    private static volatile long blackHole = 0;

    public static void main(String[] args) throws Exception {

        createResultDirectories();

        System.out.println("=====================================");
        System.out.println("Benchmark");
        System.out.println("=====================================");

        warmUp();

        workload1RandomAccess();
        workload2Search();
        workload3InsertRemove();
        workload4Heap();

        System.out.println();
        System.out.println("=====================================");
        System.out.println("Benchmark completed.");
        System.out.println("Checksum = " + blackHole);
        System.out.println("=====================================");
    }

    private static void createResultDirectories() {
        new File("results/tables").mkdirs();
        new File("results/plots").mkdirs();
    }

    private static void warmUp() {

        System.out.println();
        System.out.println("Running JVM warm-up...");

        for (int run = 0; run < WARMUP_RUNS; run++) {

            int n = 10_000;
            Random random = new Random(RANDOM_SEED);

            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            MinHeap heap = new MinHeap();

            for (int i = 0; i < n; i++) {
                int value = random.nextInt();

                array.add(value);
                list.add(value);
                heap.insert(value);
            }

            for (int i = 0; i < 1_000; i++) {
                int index = i % n;

                blackHole ^= array.get(index);
                blackHole ^= list.get(index);
            }

            for (int i = 0; i < 100; i++) {
                int value = i;

                blackHole ^= array.contains(value) ? 1 : 0;
                blackHole ^= list.contains(value) ? 1 : 0;
            }

            for (int i = 0; i < 100; i++) {
                array.add(0, i);
                list.add(0, i);
            }

            for (int i = 0; i < 100; i++) {
                array.remove(0);
                list.remove(0);
            }

            long heapSum = 0;

            while (!heap.isEmpty()) {
                heapSum += heap.extractMin();
            }

            blackHole ^= heapSum;
        }

        System.out.println("JVM warm-up completed.");
    }

    private static void workload1RandomAccess()
            throws IOException {

        File file = new File(
                "results/tables/workload1_random_access.csv"
        );

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

            out.println(
                    "n,array_time_ns,list_time_ns," +
                            "array_accesses,list_accesses"
            );

            System.out.println();
            System.out.println("=====================================");
            System.out.println("WORKLOAD 1: RANDOM ACCESS");
            System.out.println("=====================================");

            for (int n : SIZES) {

                long totalArrayTime = 0;
                long totalListTime = 0;

                long totalArrayAccesses = 0;
                long totalListAccesses = 0;

                for (int run = 0; run < RUNS; run++) {

                    Random random =
                            new Random(RANDOM_SEED);

                    DynamicArray array =
                            new DynamicArray();

                    MyLinkedList list =
                            new MyLinkedList();

                    for (int i = 0; i < n; i++) {
                        int value = random.nextInt();

                        array.add(value);
                        list.add(value);
                    }

                    int[] indices =
                            new int[RANDOM_ACCESS_OPS];

                    for (int i = 0;
                         i < RANDOM_ACCESS_OPS;
                         i++) {

                        indices[i] = random.nextInt(n);
                    }

                    array.resetMetrics();

                    long start = System.nanoTime();

                    long arraySum = 0;

                    for (int index : indices) {
                        arraySum += array.get(index);
                    }

                    long end = System.nanoTime();

                    totalArrayTime += end - start;
                    totalArrayAccesses +=
                            array.getAccessCount();

                    blackHole ^= arraySum;

                    list.resetMetrics();

                    start = System.nanoTime();

                    long listSum = 0;

                    for (int index : indices) {
                        listSum += list.get(index);
                    }

                    end = System.nanoTime();

                    totalListTime += end - start;
                    totalListAccesses +=
                            list.getAccessCount();

                    blackHole ^= listSum;
                }

                long avgArrayTime =
                        totalArrayTime / RUNS;

                long avgListTime =
                        totalListTime / RUNS;

                long avgArrayAccesses =
                        totalArrayAccesses / RUNS;

                long avgListAccesses =
                        totalListAccesses / RUNS;

                out.println(
                        n + "," +
                                avgArrayTime + "," +
                                avgListTime + "," +
                                avgArrayAccesses + "," +
                                avgListAccesses
                );

                System.out.println(
                        "n=" + n +
                                " | Array Avg=" +
                                avgArrayTime +
                                " ns" +
                                " | List Avg=" +
                                avgListTime +
                                " ns" +
                                " | Array accesses=" +
                                avgArrayAccesses +
                                " | List accesses=" +
                                avgListAccesses
                );
            }
        }
    }

    private static void workload2Search()
            throws IOException {

        File file = new File(
                "results/tables/workload2_search.csv"
        );

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

            out.println(
                    "n,array_time_ns,list_time_ns," +
                            "array_comparisons,list_comparisons"
            );

            System.out.println();
            System.out.println("=====================================");
            System.out.println("WORKLOAD 2: SEARCH");
            System.out.println("=====================================");

            for (int n : SIZES) {

                long totalArrayTime = 0;
                long totalListTime = 0;

                long totalArrayComparisons = 0;
                long totalListComparisons = 0;

                for (int run = 0; run < RUNS; run++) {

                    Random random =
                            new Random(RANDOM_SEED);

                    DynamicArray array =
                            new DynamicArray();

                    MyLinkedList list =
                            new MyLinkedList();

                    for (int i = 0; i < n; i++) {

                        int value =
                                random.nextInt(
                                        Integer.MAX_VALUE
                                );

                        array.add(value);
                        list.add(value);
                    }

                    int[] queries =
                            new int[SEARCH_OPS];

                    int presentQueries =
                            SEARCH_OPS / 2;

                    for (int i = 0;
                         i < presentQueries;
                         i++) {

                        int index = random.nextInt(n);
                        queries[i] = array.get(index);
                    }

                    for (int i = presentQueries;
                         i < SEARCH_OPS;
                         i++) {

                        queries[i] =
                                -1 -
                                        random.nextInt(
                                                Integer.MAX_VALUE
                                        );
                    }

                    array.resetMetrics();

                    long start = System.nanoTime();

                    long arrayFound = 0;

                    for (int query : queries) {
                        if (array.contains(query)) {
                            arrayFound++;
                        }
                    }

                    long end = System.nanoTime();

                    totalArrayTime += end - start;
                    totalArrayComparisons +=
                            array.getComparisonCount();

                    blackHole ^= arrayFound;

                    list.resetMetrics();

                    start = System.nanoTime();

                    long listFound = 0;

                    for (int query : queries) {
                        if (list.contains(query)) {
                            listFound++;
                        }
                    }

                    end = System.nanoTime();

                    totalListTime += end - start;
                    totalListComparisons +=
                            list.getComparisonCount();

                    blackHole ^= listFound;
                }

                long avgArrayTime =
                        totalArrayTime / RUNS;

                long avgListTime =
                        totalListTime / RUNS;

                long avgArrayComparisons =
                        totalArrayComparisons / RUNS;

                long avgListComparisons =
                        totalListComparisons / RUNS;

                out.println(
                        n + "," +
                                avgArrayTime + "," +
                                avgListTime + "," +
                                avgArrayComparisons + "," +
                                avgListComparisons
                );

                System.out.println(
                        "n=" + n +
                                " | Array Avg=" +
                                avgArrayTime +
                                " ns" +
                                " | List Avg=" +
                                avgListTime +
                                " ns" +
                                " | Array comparisons=" +
                                avgArrayComparisons +
                                " | List comparisons=" +
                                avgListComparisons
                );
            }
        }
    }

    private static void workload3InsertRemove()
            throws IOException {

        File file = new File(
                "results/tables/workload3_insert_remove.csv"
        );

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

            out.println(
                    "n," +
                            "array_front_insert," +
                            "list_front_insert," +
                            "array_front_remove," +
                            "list_front_remove," +
                            "array_middle_insert," +
                            "list_middle_insert," +
                            "array_middle_remove," +
                            "list_middle_remove," +
                            "array_front_insert_moves," +
                            "list_front_insert_accesses," +
                            "array_front_remove_moves," +
                            "list_front_remove_accesses," +
                            "array_middle_insert_moves," +
                            "list_middle_insert_accesses," +
                            "array_middle_remove_moves," +
                            "list_middle_remove_accesses"
            );

            System.out.println();
            System.out.println("=====================================");
            System.out.println("WORKLOAD 3: INSERT / REMOVE");
            System.out.println("=====================================");

            for (int n : SIZES) {

                long totalArrayFrontInsertTime = 0;
                long totalListFrontInsertTime = 0;

                long totalArrayFrontRemoveTime = 0;
                long totalListFrontRemoveTime = 0;

                long totalArrayMiddleInsertTime = 0;
                long totalListMiddleInsertTime = 0;

                long totalArrayMiddleRemoveTime = 0;
                long totalListMiddleRemoveTime = 0;

                long totalArrayFrontInsertMoves = 0;
                long totalListFrontInsertAccesses = 0;

                long totalArrayFrontRemoveMoves = 0;
                long totalListFrontRemoveAccesses = 0;

                long totalArrayMiddleInsertMoves = 0;
                long totalListMiddleInsertAccesses = 0;

                long totalArrayMiddleRemoveMoves = 0;
                long totalListMiddleRemoveAccesses = 0;

                for (int run = 0;
                     run < RUNS;
                     run++) {

                    DynamicArray array =
                            createDynamicArray(n);

                    MyLinkedList list =
                            createLinkedList(n);

                    array.resetMetrics();
                    list.resetMetrics();

                    long start = System.nanoTime();

                    for (int i = 0;
                         i < INSERT_REMOVE_OPS;
                         i++) {

                        array.add(0, i);
                    }

                    long end = System.nanoTime();

                    totalArrayFrontInsertTime +=
                            end - start;

                    totalArrayFrontInsertMoves +=
                            array.getMovementCount();

                    start = System.nanoTime();

                    for (int i = 0;
                         i < INSERT_REMOVE_OPS;
                         i++) {

                        list.add(0, i);
                    }

                    end = System.nanoTime();

                    totalListFrontInsertTime +=
                            end - start;

                    totalListFrontInsertAccesses +=
                            list.getAccessCount();

                    array = createDynamicArray(n);
                    list = createLinkedList(n);

                    array.resetMetrics();
                    list.resetMetrics();

                    int removeOps =
                            Math.min(
                                    INSERT_REMOVE_OPS,
                                    n
                            );

                    start = System.nanoTime();

                    for (int i = 0;
                         i < removeOps;
                         i++) {

                        array.remove(0);
                    }

                    end = System.nanoTime();

                    totalArrayFrontRemoveTime +=
                            end - start;

                    totalArrayFrontRemoveMoves +=
                            array.getMovementCount();

                    start = System.nanoTime();

                    for (int i = 0;
                         i < removeOps;
                         i++) {

                        list.remove(0);
                    }

                    end = System.nanoTime();

                    totalListFrontRemoveTime +=
                            end - start;

                    totalListFrontRemoveAccesses +=
                            list.getAccessCount();

                    array = createDynamicArray(n);
                    list = createLinkedList(n);

                    array.resetMetrics();
                    list.resetMetrics();

                    start = System.nanoTime();

                    for (int i = 0;
                         i < INSERT_REMOVE_OPS;
                         i++) {

                        int middle =
                                array.size() / 2;

                        array.add(middle, i);
                    }

                    end = System.nanoTime();

                    totalArrayMiddleInsertTime +=
                            end - start;

                    totalArrayMiddleInsertMoves +=
                            array.getMovementCount();

                    start = System.nanoTime();

                    for (int i = 0;
                         i < INSERT_REMOVE_OPS;
                         i++) {

                        int middle =
                                list.size() / 2;

                        list.add(middle, i);
                    }

                    end = System.nanoTime();

                    totalListMiddleInsertTime +=
                            end - start;

                    totalListMiddleInsertAccesses +=
                            list.getAccessCount();

                    array = createDynamicArray(n);
                    list = createLinkedList(n);

                    array.resetMetrics();
                    list.resetMetrics();

                    start = System.nanoTime();

                    for (int i = 0;
                         i < removeOps;
                         i++) {

                        int middle =
                                array.size() / 2;

                        array.remove(middle);
                    }

                    end = System.nanoTime();

                    totalArrayMiddleRemoveTime +=
                            end - start;

                    totalArrayMiddleRemoveMoves +=
                            array.getMovementCount();

                    start = System.nanoTime();

                    for (int i = 0;
                         i < removeOps;
                         i++) {

                        int middle =
                                list.size() / 2;

                        list.remove(middle);
                    }

                    end = System.nanoTime();

                    totalListMiddleRemoveTime +=
                            end - start;

                    totalListMiddleRemoveAccesses +=
                            list.getAccessCount();
                }

                long avgArrayFrontInsert =
                        totalArrayFrontInsertTime / RUNS;

                long avgListFrontInsert =
                        totalListFrontInsertTime / RUNS;

                long avgArrayFrontRemove =
                        totalArrayFrontRemoveTime / RUNS;

                long avgListFrontRemove =
                        totalListFrontRemoveTime / RUNS;

                long avgArrayMiddleInsert =
                        totalArrayMiddleInsertTime / RUNS;

                long avgListMiddleInsert =
                        totalListMiddleInsertTime / RUNS;

                long avgArrayMiddleRemove =
                        totalArrayMiddleRemoveTime / RUNS;

                long avgListMiddleRemove =
                        totalListMiddleRemoveTime / RUNS;

                long avgArrayFrontInsertMoves =
                        totalArrayFrontInsertMoves / RUNS;

                long avgListFrontInsertAccesses =
                        totalListFrontInsertAccesses / RUNS;

                long avgArrayFrontRemoveMoves =
                        totalArrayFrontRemoveMoves / RUNS;

                long avgListFrontRemoveAccesses =
                        totalListFrontRemoveAccesses / RUNS;

                long avgArrayMiddleInsertMoves =
                        totalArrayMiddleInsertMoves / RUNS;

                long avgListMiddleInsertAccesses =
                        totalListMiddleInsertAccesses / RUNS;

                long avgArrayMiddleRemoveMoves =
                        totalArrayMiddleRemoveMoves / RUNS;

                long avgListMiddleRemoveAccesses =
                        totalListMiddleRemoveAccesses / RUNS;

                out.println(
                        n + "," +
                                avgArrayFrontInsert + "," +
                                avgListFrontInsert + "," +
                                avgArrayFrontRemove + "," +
                                avgListFrontRemove + "," +
                                avgArrayMiddleInsert + "," +
                                avgListMiddleInsert + "," +
                                avgArrayMiddleRemove + "," +
                                avgListMiddleRemove + "," +
                                avgArrayFrontInsertMoves + "," +
                                avgListFrontInsertAccesses + "," +
                                avgArrayFrontRemoveMoves + "," +
                                avgListFrontRemoveAccesses + "," +
                                avgArrayMiddleInsertMoves + "," +
                                avgListMiddleInsertAccesses + "," +
                                avgArrayMiddleRemoveMoves + "," +
                                avgListMiddleRemoveAccesses
                );

                System.out.println(
                        "n=" + n +
                                " | Front Insert: Array=" +
                                avgArrayFrontInsert +
                                " ns, List=" +
                                avgListFrontInsert +
                                " ns" +
                                " | Front Remove: Array=" +
                                avgArrayFrontRemove +
                                " ns, List=" +
                                avgListFrontRemove +
                                " ns" +
                                " | Middle Insert: Array=" +
                                avgArrayMiddleInsert +
                                " ns, List=" +
                                avgListMiddleInsert +
                                " ns" +
                                " | Middle Remove: Array=" +
                                avgArrayMiddleRemove +
                                " ns, List=" +
                                avgListMiddleRemove +
                                " ns"
                );
            }
        }
    }

    private static void workload4Heap()
            throws IOException {

        File file = new File(
                "results/tables/workload4_heap.csv"
        );

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

            out.println(
                    "n,insert_time_ns,extract_time_ns," +
                            "insert_comparisons,extract_comparisons"
            );

            System.out.println();
            System.out.println("=====================================");
            System.out.println("WORKLOAD 4: MIN HEAP");
            System.out.println("=====================================");

            for (int n : SIZES) {

                long totalInsertTime = 0;
                long totalExtractTime = 0;

                long totalInsertComparisons = 0;
                long totalExtractComparisons = 0;

                for (int run = 0;
                     run < RUNS;
                     run++) {

                    Random random =
                            new Random(RANDOM_SEED);

                    int[] values = new int[n];

                    for (int i = 0; i < n; i++) {
                        values[i] = random.nextInt();
                    }

                    MinHeap heap = new MinHeap();

                    heap.resetMetrics();

                    long start = System.nanoTime();

                    for (int value : values) {
                        heap.insert(value);
                    }

                    long end = System.nanoTime();

                    totalInsertTime += end - start;

                    totalInsertComparisons +=
                            heap.getComparisonCount();

                    assert heap.isValidHeap();

                    heap.resetMetrics();

                    int[] extractedValues = new int[n];

                    start = System.nanoTime();

                    for (int i = 0; i < n; i++) {
                        extractedValues[i] =
                                heap.extractMin();
                    }

                    end = System.nanoTime();

                    totalExtractTime += end - start;

                    totalExtractComparisons +=
                            heap.getComparisonCount();

                    long extractedSum = 0;

                    for (int i = 0; i < n; i++) {
                        extractedSum += extractedValues[i];
                    }

                    boolean sorted = true;

                    for (int i = 1; i < n; i++) {
                        if (extractedValues[i] <
                                extractedValues[i - 1]) {

                            sorted = false;
                            break;
                        }
                    }

                    assert sorted;
                    assert heap.isEmpty();

                    blackHole ^= extractedSum;
                }

                long avgInsertTime =
                        totalInsertTime / RUNS;

                long avgExtractTime =
                        totalExtractTime / RUNS;

                long avgInsertComparisons =
                        totalInsertComparisons / RUNS;

                long avgExtractComparisons =
                        totalExtractComparisons / RUNS;

                out.println(
                        n + "," +
                                avgInsertTime + "," +
                                avgExtractTime + "," +
                                avgInsertComparisons + "," +
                                avgExtractComparisons
                );

                System.out.println(
                        "n=" + n +
                                " | Insert Avg=" +
                                avgInsertTime +
                                " ns" +
                                " | Extract Avg=" +
                                avgExtractTime +
                                " ns" +
                                " | Insert comparisons=" +
                                avgInsertComparisons +
                                " | Extract comparisons=" +
                                avgExtractComparisons
                );
            }
        }
    }

    private static DynamicArray createDynamicArray(int n) {

        DynamicArray array =
                new DynamicArray();

        for (int i = 0; i < n; i++) {
            array.add(i);
        }

        return array;
    }

    private static MyLinkedList createLinkedList(int n) {

        MyLinkedList list =
                new MyLinkedList();

        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        return list;
    }
}