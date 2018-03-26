package org.ehu.dedupe.classifier;

import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.graph.Vertex;
import org.ehu.dedupe.graph.VertexSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PairResolution {

    private final Map<String, Set<String>> duplicates;
    private final Map<Pair<String, String>, Double> weights;

    public PairResolution(Map<String, Set<String>> duplicates, Map<Pair<String, String>, Double> weights) {
        this.duplicates = duplicates;
        this.weights = weights;
    }

    public Buckets<String> toNormalizedClusters() {
        return toClusters(this::normalize);
    }

    public Buckets<String> toPlainClusters() {
        return toClusters(Collections::singletonList);
    }

    public Buckets<String> toClusters(Function<VertexSet<String>, Collection<VertexSet<String>>> normalizationFunction) {
        Set<VertexSet<String>> plain = renderGraphs();
        return toClusters(normalizationFunction, plain);
    }

    public static Buckets<String> toClusters(Function<VertexSet<String>, Collection<VertexSet<String>>> normalizationFunction, Set<VertexSet<String>> plain) {
        List<Set<String>> dups = plain.stream().map(normalizationFunction).flatMap(Collection::stream).map(VertexSet::asSet).collect(Collectors.toList());
        return Buckets.from(dups);
    }

    public Set<VertexSet<String>> renderGraphs() {
        Map<String, Set<String>> duplicates = new HashMap<>(getDuplicates());
        return new HashSet<>(duplicates.keySet())
                .stream()
                .filter(duplicates::containsKey)
                .map(key -> VertexSet.renderGraph(duplicates, new Vertex<>(key)))
                .peek(vertexSet -> vertexSet.getVertexes().stream().map(Vertex::getId).forEach(duplicates::remove))
                .collect(Collectors.toSet());
    }

    public Collection<VertexSet<String>> normalize(VertexSet<String> vertexSet) {
        return normalize(vertexSet, getWeights());
    }

    public static Collection<VertexSet<String>> normalize(VertexSet<String> vertexSet, Map<Pair<String, String>, Double> weights) {
        Set<VertexSet<String>> vertexSets = VertexSet.bronKerbosch(vertexSet);
        if (vertexSets.size() > 1) {
            return VertexSet.normalize(vertexSet, weights);
        } else {
            return vertexSets;
        }
    }

    public Map<String, Set<String>> getDuplicates() {
        return duplicates;
    }

    public Map<Pair<String, String>, Double> getWeights() {
        return weights;
    }
}
