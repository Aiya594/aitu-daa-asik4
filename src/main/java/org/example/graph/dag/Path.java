package org.example.graph.dag;

import org.example.graph.digraph.Edge;
import org.example.metrics.Metrics;
import java.util.*;

public class Path {
    private final Map<String, List<Edge>> adj;
    private final Metrics metrics;

    public Path(Map<String, List<Edge>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }


    //shortest path
    public Result shortestPathsFrom(String source, List<String> topoOrder) {
        metrics.startTimer();
        Map<String, Long> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        for (String v : adj.keySet()) dist.put(v, Long.MAX_VALUE);
        dist.put(source, 0L);

        //process nodes in topo order
        boolean seenSource = false;
        for (String u : topoOrder) {
            if (!seenSource && u.equals(source)) seenSource = true;
            // relax edges only when u reachable
            if (dist.get(u) != Long.MAX_VALUE) {
                for (var e : adj.getOrDefault(u, Collections.emptyList())) {
                    metrics.incRelaxations();
                    String v = e.getTo();
                    long nd = dist.get(u) + e.getWeight();
                    if (nd < dist.get(v)) {
                        dist.put(v, nd);
                        parent.put(v, u);
                    }
                }
            }
        }
        metrics.stopTimer();
        return new Result(dist, parent, metrics.getTimeMs());
    }

    //longest path
    public Result longestPathsFrom(String source, List<String> topoOrder) {
        metrics.startTimer();
        Map<String, Long> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        for (String v : adj.keySet()) dist.put(v, Long.MIN_VALUE);
        dist.put(source, 0L);

        for (String u : topoOrder) {
            if (dist.get(u) != Long.MIN_VALUE) {
                for (var e : adj.getOrDefault(u, Collections.emptyList())) {
                    metrics.incRelaxations();
                    String v = e.getTo();
                    long nd = dist.get(u) + e.getWeight();
                    if (nd > dist.get(v)) {
                        dist.put(v, nd);
                        parent.put(v, u);
                    }
                }
            }
        }
        metrics.stopTimer();
        return new Result(dist, parent, metrics.getTimeMs());
    }

    public static class Result {
        public final Map<String, Long> distances;
        public final Map<String, String> parent;
        public final double timeMs;
        public Result(Map<String, Long> distances, Map<String, String> parent, double timeMs) {
            this.distances = distances;
            this.parent = parent;
            this.timeMs = timeMs;
        }

        public List<String> reconstructPath(String source, String target) {
            if (!distances.containsKey(target)) return Collections.emptyList();
            List<String> path = new ArrayList<>();
            String cur = target;
            while (cur != null && !cur.equals(source)) {
                path.add(cur);
                cur = parent.get(cur);
            }
            if (cur == null) return Collections.emptyList();
            path.add(source);
            Collections.reverse(path);
            return path;
        }
    }

}
