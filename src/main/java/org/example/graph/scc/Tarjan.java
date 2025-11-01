package org.example.graph.scc;

import org.example.graph.digraph.*;
import org.example.metrics.Metrics;

import java.util.*;

public class Tarjan {
    private final Graph g;
    private final Metrics metrics;
    private final Map<String, Integer> index = new HashMap<>();
    private final Map<String, Integer> lowlink = new HashMap<>();
    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();
    private final List<List<String>> sccs = new ArrayList<>();
    private int currentIndex = 0;

    public Tarjan(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public List<List<String>> run() {
        metrics.startTimer();
        for (String v : g.getVertices()) {
            if (!index.containsKey(v)) {
                strongConnect(v);
            }
        }
        metrics.stopTimer();
        return sccs;
    }

    private void strongConnect(String v) {
        index.put(v, currentIndex);
        lowlink.put(v, currentIndex);
        currentIndex++;
        stack.push(v);
        onStack.add(v);
        metrics.incDfsVisits();

        for (var e : g.getAdjacency().getOrDefault(v, Collections.emptyList())) {
            metrics.incDfsEdges();
            String w = e.getTo();
            if (!index.containsKey(w)) {
                strongConnect(w);
                lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(w)));
            } else if (onStack.contains(w)) {
                lowlink.put(v, Math.min(lowlink.get(v), index.get(w)));
            }
        }

        if (lowlink.get(v).equals(index.get(v))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(v));
            sccs.add(comp);
        }
    }
}
