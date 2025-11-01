package org.example.graph.topo;

import org.example.graph.digraph.Edge;
import org.example.metrics.Metrics;

import java.util.*;

public class KahnAlgorithm {
    private final Map<String, List<Edge>> adj;
    private final Metrics metrics;

    public KahnAlgorithm(Map<String, List<Edge>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }

    public Optional<List<String>> topoSort() {
        metrics.startTimer();
        //compute indegree
        Map<String, Integer> indeg = new HashMap<>();
        for (String v : adj.keySet()) indeg.put(v, 0);
        for (var kv : adj.entrySet()) {
            for (var e : kv.getValue()) {
                indeg.put(e.getTo(), indeg.getOrDefault(e.getTo(), 0) + 1);
            }
        }
        Deque<String> queue = new ArrayDeque<>();
        for (var kv : indeg.entrySet()) {
            if (kv.getValue() == 0) {
                queue.add(kv.getKey());
                metrics.incKahnPush();
            }
        }
        List<String> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            String v = queue.removeFirst();
            metrics.incKahnPop();
            order.add(v);
            for (var e : adj.getOrDefault(v, Collections.emptyList())) {
                String w = e.getTo();
                indeg.put(w, indeg.get(w) - 1);
                if (indeg.get(w) == 0) {
                    queue.add(w);
                    metrics.incKahnPush();
                }
            }
        }
        metrics.stopTimer();
        //if order covers all vertices -> DAG. else cycle present.
        if (order.size() != indeg.size()) {
            return Optional.empty();
        }
        return Optional.of(order);
    }
}
