import csv
import os

import matplotlib.pyplot as plt


TABLES_DIR = "results/tables"
PLOTS_DIR = "results/plots"


def read_csv(filename):
    path = os.path.join(TABLES_DIR, filename)

    with open(path, "r", newline="") as file:
        return list(csv.DictReader(file))


def values(rows, column):
    return [float(row[column]) for row in rows]


def sizes(rows):
    return [int(row["n"]) for row in rows]


def save_plot(filename):
    path = os.path.join(PLOTS_DIR, filename)
    plt.tight_layout()
    plt.savefig(path, dpi=150)
    plt.close()


def plot_workload1():
    rows = read_csv("workload1_random_access.csv")
    x = sizes(rows)

    # Random access time
    plt.figure()
    plt.plot(x, values(rows, "array_time_ns"), marker="o", label="Dynamic Array")
    plt.plot(x, values(rows, "list_time_ns"), marker="o", label="Linked List")

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 1: Random Access Time")
    plt.legend()
    plt.grid(True)

    save_plot("workload1_random_access_time.png")

    # Accesses
    plt.figure()
    plt.plot(x, values(rows, "array_accesses"), marker="o", label="Dynamic Array")
    plt.plot(x, values(rows, "list_accesses"), marker="o", label="Linked List")

    plt.xlabel("Input size (n)")
    plt.ylabel("Access operations")
    plt.title("Workload 1: Random Access Operations")
    plt.legend()
    plt.grid(True)

    save_plot("workload1_random_access_operations.png")


def plot_workload2():
    rows = read_csv("workload2_search.csv")
    x = sizes(rows)

    # Search time
    plt.figure()
    plt.plot(x, values(rows, "array_time_ns"), marker="o", label="Dynamic Array")
    plt.plot(x, values(rows, "list_time_ns"), marker="o", label="Linked List")

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 2: Search Time")
    plt.legend()
    plt.grid(True)

    save_plot("workload2_search_time.png")

    # Comparisons
    plt.figure()
    plt.plot(
        x,
        values(rows, "array_comparisons"),
        marker="o",
        label="Dynamic Array"
    )
    plt.plot(
        x,
        values(rows, "list_comparisons"),
        marker="o",
        label="Linked List"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Comparisons")
    plt.title("Workload 2: Search Comparisons")
    plt.legend()
    plt.grid(True)

    save_plot("workload2_search_comparisons.png")


def plot_workload3():
    rows = read_csv("workload3_insert_remove.csv")
    x = sizes(rows)

    # ---------------------------------------------------------
    # Front insertion
    # ---------------------------------------------------------

    plt.figure()
    plt.plot(
        x,
        values(rows, "array_front_insert"),
        marker="o",
        label="Dynamic Array"
    )
    plt.plot(
        x,
        values(rows, "list_front_insert"),
        marker="o",
        label="Linked List"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 3: Front Insertion")
    plt.legend()
    plt.grid(True)

    save_plot("workload3_front_insertion_time.png")

    # ---------------------------------------------------------
    # Front removal
    # ---------------------------------------------------------

    plt.figure()
    plt.plot(
        x,
        values(rows, "array_front_remove"),
        marker="o",
        label="Dynamic Array"
    )
    plt.plot(
        x,
        values(rows, "list_front_remove"),
        marker="o",
        label="Linked List"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 3: Front Removal")
    plt.legend()
    plt.grid(True)

    save_plot("workload3_front_removal_time.png")

    # ---------------------------------------------------------
    # Middle insertion
    # ---------------------------------------------------------

    plt.figure()
    plt.plot(
        x,
        values(rows, "array_middle_insert"),
        marker="o",
        label="Dynamic Array"
    )
    plt.plot(
        x,
        values(rows, "list_middle_insert"),
        marker="o",
        label="Linked List"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 3: Middle Insertion")
    plt.legend()
    plt.grid(True)

    save_plot("workload3_middle_insertion_time.png")

    # ---------------------------------------------------------
    # Middle removal
    # ---------------------------------------------------------

    plt.figure()
    plt.plot(
        x,
        values(rows, "array_middle_remove"),
        marker="o",
        label="Dynamic Array"
    )
    plt.plot(
        x,
        values(rows, "list_middle_remove"),
        marker="o",
        label="Linked List"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 3: Middle Removal")
    plt.legend()
    plt.grid(True)

    save_plot("workload3_middle_removal_time.png")

    # ---------------------------------------------------------
    # Operation metrics
    # ---------------------------------------------------------

    plt.figure()
    plt.plot(
        x,
        values(rows, "array_front_insert_moves"),
        marker="o",
        label="Array front insert moves"
    )
    plt.plot(
        x,
        values(rows, "list_front_insert_accesses"),
        marker="o",
        label="List front insert accesses"
    )
    plt.plot(
        x,
        values(rows, "array_front_remove_moves"),
        marker="o",
        label="Array front remove moves"
    )
    plt.plot(
        x,
        values(rows, "list_front_remove_accesses"),
        marker="o",
        label="List front remove accesses"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Operations")
    plt.title("Workload 3: Front Operation Metrics")
    plt.legend()
    plt.grid(True)

    save_plot("workload3_front_operation_metrics.png")

    plt.figure()
    plt.plot(
        x,
        values(rows, "array_middle_insert_moves"),
        marker="o",
        label="Array middle insert moves"
    )
    plt.plot(
        x,
        values(rows, "list_middle_insert_accesses"),
        marker="o",
        label="List middle insert accesses"
    )
    plt.plot(
        x,
        values(rows, "array_middle_remove_moves"),
        marker="o",
        label="Array middle remove moves"
    )
    plt.plot(
        x,
        values(rows, "list_middle_remove_accesses"),
        marker="o",
        label="List middle remove accesses"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Operations")
    plt.title("Workload 3: Middle Operation Metrics")
    plt.legend()
    plt.grid(True)

    save_plot("workload3_middle_operation_metrics.png")


def plot_workload4():
    rows = read_csv("workload4_heap.csv")
    x = sizes(rows)

    # Insert and extract time
    plt.figure()
    plt.plot(
        x,
        values(rows, "insert_time_ns"),
        marker="o",
        label="Insert"
    )
    plt.plot(
        x,
        values(rows, "extract_time_ns"),
        marker="o",
        label="Extract"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 4: Min-Heap Operation Time")
    plt.legend()
    plt.grid(True)

    save_plot("workload4_heap_time.png")

    # Comparisons
    plt.figure()
    plt.plot(
        x,
        values(rows, "insert_comparisons"),
        marker="o",
        label="Insert"
    )
    plt.plot(
        x,
        values(rows, "extract_comparisons"),
        marker="o",
        label="Extract"
    )

    plt.xlabel("Input size (n)")
    plt.ylabel("Comparisons")
    plt.title("Workload 4: Min-Heap Comparisons")
    plt.legend()
    plt.grid(True)

    save_plot("workload4_heap_comparisons.png")


def main():
    os.makedirs(TABLES_DIR, exist_ok=True)
    os.makedirs(PLOTS_DIR, exist_ok=True)

    plot_workload1()
    plot_workload2()
    plot_workload3()
    plot_workload4()

    print("All plots generated successfully.")


if __name__ == "__main__":
    main()