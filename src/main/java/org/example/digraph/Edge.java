package org.example.digraph;

public final class Edge {
    private  String from;
    private  String to;
    private  long weight;

    public void DirectedEdge(String from, String to, long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public long getWeight() { return weight; }

    @Override
    public String toString() {
        return String.format("%s->%s(%d)", from, to, weight);
    }
}

