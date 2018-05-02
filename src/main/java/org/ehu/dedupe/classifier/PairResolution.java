package org.ehu.dedupe.classifier;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.graph.Vertex;
import org.ehu.dedupe.graph.VertexSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PairResolution {

    private final Map<String, Set<String>> duplicates;
    private final Map<Pair<String, String>, Double> weights;
    private static final int MAX_SIZE = 50;

    public PairResolution(Map<String, Set<String>> duplicates, Map<Pair<String, String>, Double> weights) {
        this.duplicates = duplicates;
        this.weights = weights;
    }

    private boolean overMax(Set<Vertex<String>> x) {
        return x.size() > MAX_SIZE;
    }

    public Buckets<String> toNormalizedClusters() {
        return toNormalizedClusters(PairResolution::shouldSplit);
    }

    public Buckets<String> toNormalizedClusters(Predicate<VertexSet<String>> shouldSplit) {
        return toClusters(vertexSet -> normalize(vertexSet, shouldSplit));
    }

    public Buckets<String> toPlainClusters() {
        return toClusters(Collections::singletonList);
    }

    public Buckets<String> toClusters(Function<VertexSet<String>, Collection<VertexSet<String>>> normalizationFunction) {
        Set<VertexSet<String>> plain = renderGraphs();
        return toClusters(normalizationFunction, plain);
    }

    public static Buckets<String> toClusters(Function<VertexSet<String>, Collection<VertexSet<String>>> normalizationFunction, Set<VertexSet<String>> plain) {
        List<Set<String>> dups = plain.parallelStream().map(normalizationFunction).flatMap(Collection::stream).map(VertexSet::asSet).collect(Collectors.toList());
        return Buckets.from(dups);
    }

    public Set<VertexSet<String>> renderGraphs() {
        Map<String, Set<String>> duplicates = new HashMap<>(getDuplicates());
        return new HashSet<>(duplicates.keySet())
                .stream()
                .filter(duplicates::containsKey)
                .map(key -> toVertexSet(duplicates, key))
                .peek(vertexSet -> vertexSet.getVertexes().stream().map(Vertex::getId).forEach(duplicates::remove))
                .collect(Collectors.toSet());
    }

    public VertexSet<String> toVertexSet(Map<String, Set<String>> duplicates, String key) {
        VertexSet<String> graph = VertexSet.renderGraph(duplicates, new Vertex<>(key));
        return reduce(graph, duplicates, key);
    }

    private VertexSet<String> reduce(VertexSet<String> vertexSet, Map<String, Set<String>> duplicates, String key) {
        if (!overMax(vertexSet.getVertexes())) {
            return vertexSet;
        } else {
            Set<String> toReduce = vertexSet.getVertexes().stream()
                    .map(Vertex::getId)
                    .collect(Collectors.toSet());

            Map<Pair<String, String>, Double> weights = getWeights();

            final MutableInt count = new MutableInt();
            Set<Pair<String, String>> pairs = weights.keySet().stream()
                    .filter(x -> toReduce.contains(x.getLeft()) && toReduce.contains(x.getRight()))
                    .sorted(Comparator.comparingDouble(weights::get).reversed())
                    .filter(x -> {
                        if (weights.get(x) == 1.0) {
                            count.increment();
                            return true;
                        }

                        if (count.getValue() > MAX_SIZE) {
                            return false;
                        }
                        count.increment();
                        return true;
                    })
                    .collect(Collectors.toSet());

            toReduce.forEach(x -> {
                Set<String> duplicateSet = duplicates.get(x);
                duplicates.put(x, duplicateSet.stream().filter(y -> pairs.contains(new ImmutablePair<>(x, y)) || pairs.contains(new ImmutablePair<>(y, x))).collect(Collectors.toSet()));
            });
            return VertexSet.renderGraph(duplicates, new Vertex<>(key));
        }
    }

    public Collection<VertexSet<String>> normalize(VertexSet<String> vertexSet, Predicate<VertexSet<String>> shouldSplit) {
        return normalize(vertexSet, getWeights(), shouldSplit);
    }

    public static Collection<VertexSet<String>> normalize(VertexSet<String> vertexSet, Map<Pair<String, String>, Double> weights, Predicate<VertexSet<String>> predicate) {
        boolean split = predicate.test(vertexSet);
        if (split) {
            return VertexSet.normalize(vertexSet, weights, predicate);
        } else {
            return Collections.singleton(vertexSet);
        }
    }

    public static <T> boolean shouldSplit(VertexSet<T> vertexSet) {
        Set<VertexSet<T>> vertexSets = VertexSet.bronKerbosch(vertexSet);
        return vertexSets.size() > 1;
    }

    public Map<String, Set<String>> getDuplicates() {
        return duplicates;
    }

    public Map<Pair<String, String>, Double> getWeights() {
        return weights;
    }
}
