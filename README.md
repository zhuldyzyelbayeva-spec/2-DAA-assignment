# 3-ASSIGNMENT-DAA

## 1. Overview

This project implements three data structures from scratch:

- Dynamic Array
- Singly Linked List
- Min-Heap

The project includes:

- manual implementations of all three data structures;
- correctness tests;
- time-complexity and space-complexity analysis;
- loop-invariant proofs;
- four experimental workloads;
- CSV result tables;
- performance plots.

The benchmark uses `System.nanoTime()` for execution-time measurement and `Random(42)` to generate reproducible input data and queries. Each configuration is executed five times after JVM warm-up, and the average execution time is reported.

Execution times may vary between runs because of JVM behaviour, JIT compilation, CPU cache effects, memory allocation, garbage collection, and operating-system activity.

---

## 2. Complexity Analysis

The complexity tables use Ω for the best case, Θ for the average case, and O for the worst case.

### Dynamic Array

| Operation | Best | Average | Worst |
| --- | --- | --- | --- |
| `get(i)` | Θ(1) | Θ(1) | Θ(1) |
| `contains(x)` | Ω(1) | Θ(n) | O(n) |
| `add(x)` | Θ(1) | Θ(1) amortized | O(n) |
| `add(i,x)` | Ω(1) | Θ(n) | O(n) |
| `remove(i)` | Ω(1) | Θ(n) | O(n) |

The array provides constant-time indexed access. Insertions and removals may require shifting elements. Resizing requires copying existing elements.

The auxiliary space used by an operation is O(1), excluding the storage of the data structure itself. During resizing, a new array is temporarily allocated, so additional temporary space can reach O(n).

### Singly Linked List

| Operation | Best | Average | Worst |
| --- | --- | --- | --- |
| `get(i)` | Ω(1) | Θ(n) | O(n) |
| `contains(x)` | Ω(1) | Θ(n) | O(n) |
| `add(x)` | Θ(1) | Θ(1) | Θ(1) |
| `add(i,x)` | Ω(1) | Θ(n) | O(n) |
| `remove(i)` | Ω(1) | Θ(n) | O(n) |

The list supports constant-time insertion at the front and appending using the tail reference. Arbitrary indexed access requires traversal.

The auxiliary space used by individual operations is O(1), excluding the nodes that store the data structure itself.

### Min-Heap

| Operation | Best | Average | Worst |
| --- | --- | --- | --- |
| `insert(x)` | Θ(1) | Θ(log n) | O(log n) |
| `peekMin()` | Θ(1) | Θ(1) | Θ(1) |
| `extractMin()` | Θ(1) | Θ(log n) | O(log n) |

The heap stores elements in an array and maintains the min-heap property.

The auxiliary space used by `insert`, `peekMin`, and `extractMin` is O(1), excluding the storage of the heap itself. Resizing may temporarily require O(n) additional space.

Overall storage for each structure is Θ(n).

---

## 3. Correctness

Correctness is checked in `Tests.java`.

The tests cover:

- empty structures;
- one and multiple elements;
- duplicate values;
- valid and invalid indices;
- large inputs;
- searching for present and absent values;
- heap property;
- heap property after extraction;
- non-decreasing extraction order.

The complete test suite was executed successfully with Java assertions enabled. All tests passed for the Dynamic Array, Singly Linked List, and Min-Heap.

### Loop-Invariant Proof 1 — Dynamic Array Insertion

The insertion loop shifts elements one position to the right to create an empty position at the required insertion index.

**Invariant:**

At the beginning of each iteration, every element in the range from `i + 1` to `size` contains the value that originally occupied the previous position. Therefore, all elements to the right of the current position `i` have already been shifted correctly.

Initially, `i = size`, so no elements have been shifted yet and the invariant holds.

During each iteration, the value at position `i - 1` is copied to position `i`. Then `i` is decreased by one. Thus, the next position is ready to be processed and the invariant is preserved.

When the loop terminates with `i == index`, all elements from `index` to `size - 1` have been shifted one position to the right. Position `index` is therefore available for the new element.

After placing the new element at `index` and increasing the size, the array contains the original elements in their original relative order with the new element inserted at the requested position.

### Loop-Invariant Proof 2 — Heap Extraction

During `extractMin()`, after replacing the root with the last element, the min-heap property may be violated only at the current position.

**Invariant:**

At the beginning of each iteration, every subtree not rooted at the current position satisfies the min-heap property. The only possible violation is between the element at the current position and one or both of its children.

Initially, the current position is the root. All other subtrees were valid before the extraction, so the invariant holds.

During each iteration, the smallest child is selected. If the current element is already smaller than or equal to both children, no violation exists and the loop terminates.

Otherwise, the current element is swapped with its smallest child. The element moved upward is no greater than its children, so the heap property is restored at the previous position. The only possible violation moves to the child's old position, which becomes the new current position. Therefore, the invariant is preserved.

When the loop terminates, the current element is smaller than or equal to both children, and all other subtrees satisfy the heap property. Therefore, the entire structure is a valid min-heap.

---

## 4. Experimental Setup

Four workloads were tested with the following input sizes:

- 100
- 1,000
- 10,000
- 100,000

Each configuration was executed five times.

Before the measured benchmark runs, three JVM warm-up runs are performed. This allows commonly executed code to be exercised before timing begins.

The benchmark uses `System.nanoTime()` for timing. Test data and queries are generated using `Random(42)` outside the timed sections. The same generated data and queries are used for the corresponding Dynamic Array and Linked List comparisons.

Execution times are averaged over five measured runs.

Measured execution time can still vary between separate executions because of JVM warm-up, JIT compilation, CPU cache behaviour, memory allocation, garbage collection, and operating-system activity. Therefore, execution-time results should be interpreted as measurements for the test environment rather than machine-independent constants.

### Workload 1 — Random Access

10,000 random `get(i)` operations are performed on both structures using the same indices.

The array provides constant-time indexed access, while the linked list must traverse nodes to reach the requested position.

Element accesses are also counted to provide an operation-level comparison between the two structures.

### Workload 2 — Search

1,000 searches are performed:

- 500 present values;
- 500 absent values.

The same queries are used for both structures. Execution time and element comparisons are recorded.

The number of comparisons is the same for the Dynamic Array and Linked List because both structures perform the same linear search on the same data using the same sequence of queries.

### Workload 3 — Insert/Remove

Front and middle insertion and removal are tested using up to 1,000 operations.

For removal, the number of operations is limited by the current structure size using `Math.min(1000, n)`.

For middle operations, the index is recalculated from the current structure size.

Element movements for the Dynamic Array and traversal/access operations for the Linked List are recorded.

### Workload 4 — Min-Heap

For each input size, values are inserted into the heap and then all elements are removed using `extractMin()`.

The heap property is verified after all insertions, and the extracted values are checked to be in non-decreasing order.

The number of comparisons and execution time are recorded separately for insertion and extraction.

---

## 5. Results

### Workload 1 — Random Access

| n | Array Time (ns) | List Time (ns) | Array Accesses | List Accesses |
| --- | ---: | ---: | ---: | ---: |
| 100 | 206,080 | 778,220 | 10,000 | 511,327 |
| 1,000 | 35,960 | 7,239,100 | 10,000 | 5,021,262 |
| 10,000 | 12,160 | 71,696,780 | 10,000 | 50,180,278 |
| 100,000 | 86,760 | 739,898,980 | 10,000 | 505,028,648 |

![Workload 1 Accesses](results/plots/workload1_accesses.png)

![Workload 1 Time](results/plots/workload1_time.png)

### Workload 2 — Search

| n | Array Time (ns) | List Time (ns) | Array Comparisons | List Comparisons |
| --- | ---: | ---: | ---: | ---: |
| 100 | 248,240 | 182,680 | 75,861 | 75,861 |
| 1,000 | 296,920 | 1,418,420 | 734,862 | 734,862 |
| 10,000 | 2,423,880 | 18,749,860 | 7,523,058 | 7,523,058 |
| 100,000 | 27,072,040 | 147,165,980 | 75,177,945 | 75,177,945 |

![Workload 2 Comparisons](results/plots/workload2_comparisons.png)

![Workload 2 Time](results/plots/workload2_time.png)

### Workload 3 — Insert/Remove

#### Front Operations

| n | Array Front Insert | List Front Insert | Array Front Remove | List Front Remove |
| --- | ---: | ---: | ---: | ---: |
| 100 | 688,900 | 26,980 | 3,540 | 2,200 |
| 1,000 | 315,800 | 24,520 | 106,220 | 20,240 |
| 10,000 | 2,112,340 | 24,620 | 1,610,100 | 12,580 |
| 100,000 | 21,048,200 | 17,040 | 19,308,060 | 7,740 |

#### Middle Operations

| n | Array Middle Insert | List Middle Insert | Array Middle Remove | List Middle Remove |
| --- | ---: | ---: | ---: | ---: |
| 100 | 246,500 | 503,900 | 4,520 | 7,720 |
| 1,000 | 180,440 | 1,014,160 | 92,600 | 423,340 |
| 10,000 | 1,111,260 | 8,236,620 | 828,220 | 6,774,340 |
| 100,000 | 10,601,240 | 67,217,380 | 9,409,360 | 62,959,040 |

The detailed movement and access measurements for all insert/remove operations are stored in `results/tables/workload3_insert_remove.csv`.

![Workload 3 Movements](results/plots/workload3_movements.png)

![Workload 3 Time](results/plots/workload3_time.png)

### Workload 4 — Min-Heap

| n | Insert Time (ns) | Extract Time (ns) | Insert Comparisons | Extract Comparisons |
| --- | ---: | ---: | ---: | ---: |
| 100 | 3,460 | 2,660 | 206 | 863 |
| 1,000 | 32,320 | 49,300 | 2,326 | 14,996 |
| 10,000 | 339,020 | 736,260 | 22,753 | 216,531 |
| 100,000 | 1,609,980 | 8,832,620 | 227,857 | 2,831,426 |

![Workload 4 Comparisons](results/plots/workload4_comparisons.png)

![Workload 4 Time](results/plots/workload4_time.png)

---

## 6. Discussion

The experimental results generally agree with the theoretical analysis.

The Dynamic Array provides constant-time indexed access, while linked-list access requires traversal. As the input size increases, the measured execution time and number of list accesses increase substantially.

Both structures perform linear search, so the number of comparisons grows with `n`. The comparison counts are identical because the same data and search queries are used for both structures.

For front insertion and removal, the linked list requires only a small number of pointer updates, while the Dynamic Array must shift elements when inserting or removing at the front. The measured execution times reflect these different operations.

For middle operations, the Dynamic Array performs element movement to maintain contiguous storage, while the Linked List must traverse nodes to reach the target position. The results show the different costs associated with these two representations.

The Min-Heap measurements show increasing execution time and comparison counts as the input size increases. Since the benchmark performs `n` insertions and `n` extractions, the total amount of work is consistent with the expected approximately `n log n` behaviour.

The measured execution times vary between runs because of JVM behaviour, JIT compilation, CPU cache behaviour, memory allocation, garbage collection, and operating-system activity. Therefore, the measured execution times should be interpreted as experimental observations for the test environment rather than exact machine-independent constants.

---

## 7. Design Recommendations

A Dynamic Array is suitable when fast indexed access is important.

A Linked List is suitable when frequent insertion or removal at the front is required and random access is not important.

A Min-Heap is suitable when the minimum element must be repeatedly inserted and extracted efficiently.

The experimental results should be considered together with the theoretical complexity because execution time depends on the runtime environment.

---

## 8. Conclusion

The project successfully implements and evaluates a Dynamic Array, Singly Linked List, and Min-Heap.

The correctness tests verify normal operations, boundary cases, invalid inputs, duplicates, large inputs, heap behaviour, and extraction order. The complete test suite passed successfully.

The experimental study evaluates random access, search, insertion and removal, and heap operations for input sizes from 100 to 100,000 elements.

The results demonstrate the expected differences between the data structures and generally support the theoretical complexity analysis.

The benchmark also demonstrates that measured execution time can be affected by factors such as JVM behaviour, JIT compilation, caching, memory allocation, garbage collection, and operating-system activity.

The project contains the following main files and directories:

- `src/` — Java implementations, benchmark, and tests
- `results/tables/` — CSV benchmark results
- `results/plots/` — generated PNG plots
- `plot_results.py` — plotting script
- `README.md` — project documentation

```text
src/
├── DynamicArray.java
├── MyLinkedList.java
├── MinHeap.java
├── Tests.java
└── Benchmark.java

results/
├── tables/
└── plots/

plot_results.py
README.md
