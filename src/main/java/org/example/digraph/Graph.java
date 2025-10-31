package org.example.digraph;

import java.util.*;

public class Graph {

    private final Set<String> vertices;
    private final List<Edge> edges;
    private final Map<String, List<Edge>> adj;

    public Graph(Collection<String> vertices, Collection<Edge> edges) {
        this.vertices = new LinkedHashSet<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.adj = new HashMap<>();
        for (String v : this.vertices) adj.put(v, new ArrayList<>());
        for (Edge e : this.edges) {
            if (!this.vertices.contains(e.getFrom()) || !this.vertices.contains(e.getTo())) {
                throw new IllegalArgumentException("Edge references unknown vertex: " + e);
            }
            this.adj.get(e.getFrom()).add(e);
        }
    }

    public Set<String> getVertices() { return Collections.unmodifiableSet(vertices); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
    public Map<String, List<Edge>> getAdjacency() {
        Map<String, List<Edge>> r = new HashMap<>();
        adj.forEach((k,v) -> r.put(k, Collections.unmodifiableList(v)));
        return Collections.unmodifiableMap(r);
    }
    public int vertexCount() {
        return vertices.size();
    }
    public int edgeCount() {
        return edges.size();
    }
}
