package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.graph.dag.Path;
import org.example.graph.digraph.Edge;
import org.example.graph.digraph.Graph;
import org.example.graph.scc.Tarjan;
import org.example.graph.topo.KahnAlgorithm;
import org.example.io.Input;
import org.example.io.Output;
import org.example.metrics.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String INPUT_PATH = "data/input.json";
    private static final String OUTPUT_JSON_PATH = "data/output.json";
    private static final String OUTPUT_CSV_PATH = "data/results.csv";

    public static void main(String[] args) throws Exception {

        // === 1. Load input data ===
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Input input = mapper.readValue(new File(INPUT_PATH), Input.class);
        if (input.graphs == null || input.graphs.isEmpty()) {
            System.err.println("No graphs found in input file!");
            return;
        }

        List<Output.Result> allResults = new ArrayList<>();

        // === 2. Process each graph ===
        for (var gEntry : input.graphs) {
            Graph g = new Graph(gEntry.nodes, gEntry.edges);

            // === 1. SCC (Tarjan) ===
            Metrics sccMetrics = new Metrics();
            Tarjan tarjan = new Tarjan(g, sccMetrics);
            List<List<String>> sccs = tarjan.run();

            Set<String> all = new HashSet<>();
            for (List<String> comp : sccs) all.addAll(comp);

            // === 2. Build Condensation Graph ===
            Map<String, Integer> compIndex = new HashMap<>();
            for (int i = 0; i < sccs.size(); i++) {
                for (String v : sccs.get(i)) compIndex.put(v, i);
            }
            Map<String, List<Edge>> condAdj = new LinkedHashMap<>();
            for (int i = 0; i < sccs.size(); i++) condAdj.put("C" + i, new ArrayList<>());
            for (Edge e : g.getEdges()) {
                int u = compIndex.get(e.getFrom());
                int v = compIndex.get(e.getTo());
                if (u != v)
                    condAdj.get("C" + u).add(new Edge("C" + u, "C" + v, e.getWeight()));
            }

            // === Build SCC label map (C0, C1, ...) ===
            Map<String, List<String>> sccLabels = new LinkedHashMap<>();
            for (int i = 0; i < sccs.size(); i++) {
                sccLabels.put("C" + i, sccs.get(i));
            }

            // === 3. Topological Sort (Kahn) ===
            Metrics topoMetrics = new Metrics();
            KahnAlgorithm topo = new KahnAlgorithm(condAdj, topoMetrics);
            Optional<List<String>> orderOpt = topo.topoSort();
            if (orderOpt.isEmpty()) continue;

            List<String> topoOrder = orderOpt.get();
            String source = topoOrder.get(0);

            // === 4. Shortest and Longest Paths (DAG SP) ===
            // Shortest paths
            Path dagSP = new Path(condAdj, new Metrics());
            Path.Result shortest = dagSP.shortestPathsFrom(source, topoOrder);

            // Longest paths
            Path dagSP2 = new Path(condAdj, new Metrics());
            Path.Result longest = dagSP2.longestPathsFrom(source, topoOrder);

            String target = topoOrder.get(topoOrder.size() - 1);
            List<String> shortestExample = shortest.reconstructPath(source, target);
            List<String> longestExample = longest.reconstructPath(source, target);

            // === Collect data for analysis ===
            Output.DagResult dagResult = new Output.DagResult(
                    shortest.distances,
                    longest.distances,
                    shortestExample,
                    longestExample,
                    shortest.timeMs,
                    longest.timeMs
            );
            Map<String, Integer> stats = new HashMap<>();
            stats.put("vertices", gEntry.nodes.size());
            stats.put("edges", gEntry.edges.size());
            stats.put("scc_count", sccs.size());

            allResults.add(new Output.Result(
                    gEntry.id, stats, sccs, topoOrder, dagResult, sccLabels
            ));

            System.out.printf("Graph %d processed: %d SCCs, DAG nodes=%d, time=%.3f ms%n",
                    gEntry.id, sccs.size(), condAdj.size(), sccMetrics.getTimeMs());
        }

        // === 3. Write output JSON ===
        mapper.writeValue(new File(OUTPUT_JSON_PATH), new Output(allResults));

        // === 4. Write CSV ===
        try (PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_CSV_PATH))) {
            pw.println("GraphID,Vertices,Edges,SCCs,ShortestTime(ms),LongestTime(ms)");
            for (Output.Result r : allResults) {
                pw.printf(Locale.CANADA, "%d,%d,%d,%d,%.3f,%.3f%n",
                        r.graph_id,
                        r.input_stats.get("vertices"),
                        r.input_stats.get("edges"),
                        r.input_stats.get("scc_count"),
                        r.dag.shortestTimeMs,
                        r.dag.longestTimeMs
                );
            }
        }

        System.out.printf("Processing complete. JSON: %s, CSV: %s%n",
                OUTPUT_JSON_PATH, OUTPUT_CSV_PATH);
    }
}