package edu.princeton;

/**
 * This helper class represents the <em>cut-of-the-phase</em>. The
 * cut-of-the-phase is a <em>minimum s-t-cut</em> in the current graph,
 * where {@code s} and {@code t} are the two vertices added last in the
 * phase.
 */
class CutPhase {
    double weight; // the weight of the minimum s-t cut
    int s;         // the vertex s
    int t;         // the vertex t

    public CutPhase(double weight, int s, int t) {
        this.weight = weight;
        this.s = s;
        this.t = t;
    }
}
