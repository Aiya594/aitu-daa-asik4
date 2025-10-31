package org.example.metrics;

public class Metrics {
    private long dfsVisits = 0;
    private long dfsEdges = 0;
    private long kahnPush = 0;
    private long kahnPop = 0;
    private long relaxations = 0;
    private long startTimeNs = 0;
    private long time = 0;

    public void startTimer() {
        startTimeNs = System.nanoTime();
    }
    public void stopTimer() {
        time = System.nanoTime() - startTimeNs;
    }

    public void incDfsVisits() {
        dfsVisits++;
    }
    public void incDfsEdges() {
        dfsEdges++;
    }
    public void incKahnPush() {
        kahnPush++;
    }
    public void incKahnPop() {
        kahnPop++;
    }
    public void incRelaxations() {
        relaxations++;
    }

    public long getDfsVisits() {
        return dfsVisits;
    }
    public long getDfsEdges() {
        return dfsEdges;
    }
    public long getKahnPush() {
        return kahnPush;
    }
    public long getKahnPop() {
        return kahnPop;
    }
    public long getRelaxations() {
        return relaxations;
    }
    public double getTimeMs() {
        return time / 1_000_000.0;
    }
}
