package org.example.io;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

@JsonInclude(JsonInclude.Include.ALWAYS)       // (default) include everything

public class Output {
    public final List<Result> results;
    public Output(List<Result> results) {
        this.results = results;
    }

    public static class Result {
        public final int graph_id;
        public final Map<String,Integer> input_stats;
        public final Object scc; // list of components
        public final List<String> topoOrder; // condensation topo
        public final DagResult dag; // optional DAG results
        public Result(int graph_id, Map<String,Integer> input_stats, Object scc, List<String> topoOrder, DagResult dag) {
            this.graph_id = graph_id; this.input_stats = input_stats; this.scc = scc; this.topoOrder = topoOrder; this.dag = dag;
        }
    }

    public static class DagResult {
        public final Map<String,Long> shortestDistances;
        public final Map<String,Long> longestDistances;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final List<String> shortestPathExample;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final List<String> longestPathExample;
        public final double shortestTimeMs;
        public final double longestTimeMs;

        public DagResult(Map<String,Long> shortestDistances, Map<String,Long> longestDistances,
                         List<String> shortestPathExample, List<String> longestPathExample,
                         double shortestTimeMs, double longestTimeMs) {
            this.shortestDistances = shortestDistances;
            this.longestDistances = longestDistances;
            this.shortestPathExample = shortestPathExample;
            this.longestPathExample = longestPathExample;
            this.shortestTimeMs = shortestTimeMs;
            this.longestTimeMs = longestTimeMs;
        }
    }
}
