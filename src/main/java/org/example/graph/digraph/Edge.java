package org.example.graph.digraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Edge {
    private  String from;
    private  String to;
    private  long weight;

    @JsonCreator
    public Edge(
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("weight") long weight) {
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

