# Assignment 4 — Smart City / Smart Campus Scheduling

* **Course:** Design and Analysis of Algorithms
* **Student:** Aiya Zhakupova

---

## Goal

This project combines two main topics into one practical case: **“Smart City / Smart Campus Scheduling.”**
It focuses on:

1. Finding **Strongly Connected Components (SCCs)** and **Topological Order**
2. Calculating **Shortest and Longest Paths in DAGs (Directed Acyclic Graphs)**

---

## Implemented Algorithms

### 1. Strongly Connected Components (SCC) — *Tarjan’s Algorithm*

* Input: directed graph (shows tasks and their dependencies).
* Output: groups of connected tasks (SCCs) and their sizes.
* Complexity: O(V + E).
* The algorithm explores vertices with DFS, keeps track of discovery order, and identifies when a full SCC has been found.

### 2. Condensation Graph and Topological Sort — *Kahn’s Algorithm*

* After SCCs are found, each component becomes a single node to form a DAG.
* Kahn’s algorithm removes nodes with zero incoming edges, giving a valid order for task execution.
* Guarantees that no cycles exist in the resulting order.

### 3. Shortest and Longest Paths in DAG

* Chosen model: edge weights.
* Outputs both the shortest and the longest path.

---

## Data and Input

All datasets are stored in the `/data/` folder.

Example format:

```json
{
  "graphs": [
    {
      "id": 1,
      "name": "Small Graph 1 – Simple DAG",
      "nodes": ["A","B","C","D","E","F"],
      "edges": [
        {"from": "A", "to": "B", "weight": 3},
        {"from": "A", "to": "C", "weight": 2},
        {"from": "B", "to": "D", "weight": 4},
        {"from": "C", "to": "E", "weight": 1},
        {"from": "D", "to": "F", "weight": 5},
        {"from": "E", "to": "F", "weight": 3}
      ]
    }
  ]
}
```

### Dataset Types

A total of **9 graphs** were created and tested (3 of each type).

| Type   | Nodes (n) | Description                                |
| ------ | --------- | ------------------------------------------ |
| Small  | 6–10      | Simple graphs with 1–2 cycles or pure DAGs |
| Medium | 10–20     | Mixed structures with several SCCs         |
| Large  | 20–50     | Used for timing and performance testing    |

---

## Metrics

A shared `Metrics` interface was used to track both **time** and **operations**.
Execution time was measured with `System.nanoTime()` and shown in milliseconds.

Tracked operations included:

* DFS calls and edge traversals (for SCC detection)
* Queue operations (for topological sort)
* Relaxations during shortest path computation

---

## Results Summary

All graphs were processed through this pipeline:
**Tarjan’s SCC → Condensed DAG → Kahn’s Topological Sort → DAG Shortest & Longest Paths.**

---

### Small Graphs (6–10 nodes)

| Graph ID | Graph Name                 | Vertices | Edges | SCCs | Shortest Time (ms) | Longest Time (ms) |
| :------: | :------------------------- | :------: | :---: | :--: | :----------------: | :---------------: |
|     1    | Small Graph 1 – Simple DAG |     6    |   6   |   6  |        0.082       |       0.032       |
|     2    | Small Graph 2 – One Cycle  |     7    |   7   |   5  |        0.016       |       0.011       |
|     3    | Small Graph 3 – Two Cycles |     8    |   9   |   5  |        0.014       |       0.009       |

**Note:**
All small graphs finished in less than **0.1 ms**.
Graphs with cycles took a bit longer because of extra recursive steps in Tarjan’s algorithm.

---

### Medium Graphs (10–20 nodes)

| Graph ID | Graph Name                             | Vertices | Edges | SCCs | Shortest Time (ms) | Longest Time (ms) |
| :------: | :------------------------------------- | :------: | :---: | :--: | :----------------: | :---------------: |
|     4    | Medium Graph 1 – Mixed DAG and Cycles  |    12    |   12  |  10  |        0.022       |       0.023       |
|     5    | Medium Graph 2 – Several SCCs          |    15    |   16  |  12  |        0.030       |       0.024       |
|     6    | Medium Graph 3 – Pure DAG for SP Tests |    13    |   13  |  13  |        0.115       |       0.036       |

**Note:**
All medium graphs stayed under **0.12 ms**.
The pure DAG (Graph 6) took slightly longer because more relaxations were needed for shortest paths.

---

### Large Graphs (20–50 nodes)

| Graph ID | Graph Name                             | Vertices | Edges | SCCs | Shortest Time (ms) | Longest Time (ms) |
| :------: | :------------------------------------- | :------: | :---: | :--: | :----------------: | :---------------: |
|     7    | Large Graph 1 – Dense Cyclic Structure |    20    |   21  |  15  |        0.030       |       0.171       |
|     8    | Large Graph 2 – Mixed DAG with SCCs    |    24    |   24  |  22  |        0.055       |       0.033       |
|     9    | Large Graph 3 – Pure DAG for Timing    |    26    |   26  |  26  |        0.045       |       0.044       |

**Note:**
As graphs grow, the number of SCCs also increases.
Even with 20–26 nodes, all algorithms stayed below **0.2 ms**, showing good linear scaling.
Graph 7 took longer because of many cycles.

---

## Output Files

* `data/output_test.json`
* `data/results_test.csv`

---

## Analysis

| Part                   | Summary                                                  |
| ---------------------- |----------------------------------------------------------|
| **SCC Detection**      | Fast and linear; most graphs had fewer than 15 SCCs.     |
| **Condensation Graph** | Always turned into a DAG after compression.              |
| **Topological Sort**   | Stable and easy to reproduce using Kahn’s method.        |
| **Shortest Path**      | Efficient even on dense graphs.                          |
| **Longest Path**       | Identifies the critical route in task planning.          |

---

## Conclusions

* **Tarjan’s algorithm** quickly detects loops or circular dependencies between city tasks.
* **Kahn’s topological sort** provides a clear execution order once cycles are removed.
* **Shortest and longest path algorithms** help find the most efficient and the most time-consuming task chains.

---

## Repository Structure

```
daa-asik4/
├── data/
│   ├── input.json
│   ├── output.json
│   ├── output_test.json
│   ├── results.csv
│   └── results_test.csv
├── src/
│   ├── main/
│   │   └── java/org/example/
│   │      ├── graph/
│   │      │   ├── dag/Path.java
│   │      │   ├── digraph/Edge.java, Graph.java
│   │      │   ├── scc/Tarjan.java
│   │      │   └── topo/KahnAlgorithm.java
│   │      ├── io/Input.java, Output.java
│   │      ├── metrics/Metrics.java
│   │      └── Main.java
│   └── test/
│       └── java/tests.java
└── README.md
```

