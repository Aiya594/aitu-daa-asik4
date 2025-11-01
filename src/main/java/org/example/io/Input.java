package org.example.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.graph.digraph.Edge;

import java.util.*;

public class Input {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GraphEntry {
        public final int id;
        public final List<String> nodes;
        public final List<Edge> edges;
        @JsonCreator
        public GraphEntry(@JsonProperty("id") int id,
                          @JsonProperty("nodes") List<String> nodes,
                          @JsonProperty("edges") List<Edge> edges) {
            this.id = id; this.nodes = nodes; this.edges = edges;
        }
    }
    public final List<GraphEntry> graphs;
    @JsonCreator
    public Input(@JsonProperty("graphs") List<GraphEntry> graphs) {
        this.graphs = graphs;
    }

}
