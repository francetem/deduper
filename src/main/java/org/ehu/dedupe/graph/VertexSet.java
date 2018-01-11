package org.ehu.dedupe.graph;

import edu.princeton.GlobalMincut;
import edu.princeton.cs.algorithms.Edge;
import edu.princeton.cs.algorithms.EdgeWeightedGraph;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VertexSet<T> {

    public static final VertexSet EMPTY = new VertexSet();

    private Set<Vertex<T>> vertexes;

    public VertexSet(Set<Vertex<T>> vertexes, Vertex<T> v) {
        Set<Vertex<T>> newVertexes = new HashSet<>(vertexes);
        newVertexes.add(v);
        this.vertexes = newVertexes;
    }

    public VertexSet(Set<Vertex<T>> vertexes) {
        this.vertexes = new HashSet<>(vertexes);
    }

    public VertexSet() {
        this.vertexes = new HashSet<>();
    }

    private static <T> Set<VertexSet<T>> bronKerbosch(VertexSet<T> r, VertexSet<T> p, VertexSet<T> x) {
        Set<VertexSet<T>> cliques = new HashSet<>();

        if (p.isEmpty() && x.isEmpty()) {
            cliques.add(r);
        }
        for (Vertex<T> v : p.getVertexes()) {
            VertexSet<T> vNeighbours = v.neighbourSet();
            cliques.addAll(bronKerbosch(r.union(v), p.intersect(vNeighbours), x.intersect(vNeighbours)));
            p = p.substract(v);
            x = x.union(v);
        }

        return cliques;
    }

    public static <T> Set<VertexSet<T>> bronKerbosch(VertexSet<T> vertexSet) {
        return bronKerbosch(empty(), vertexSet, empty());
    }

    public static <T> VertexSet<T> renderGraph(Map<T, Set<T>> duplicates, Vertex<T> keyVertex) {
        VertexSet<T> vertexSet = new VertexSet<>(Stream.of(keyVertex).collect(Collectors.toSet()));
        vertexSet.renderGraph(keyVertex, duplicates);
        return vertexSet;
    }

    private void renderGraph(Vertex<T> keyVertex, Map<T, Set<T>> duplicates) {
        T id = keyVertex.getId();
        if (!duplicates.containsKey(id)) {
            return;
        }
        Set<T> duplicated = duplicates.get(id);
        for (T duplicate : duplicated) {
            Vertex<T> vertex = new Vertex<>(duplicate);
            keyVertex.addNeighbour(vertex);
            boolean recurse = !contains(duplicate);
            add(vertex);
            if (recurse) {
                this.renderGraph(vertex, duplicates);
            }
        }
    }

    public boolean isEmpty() {
        return vertexes.isEmpty();
    }

    public Set<Vertex<T>> getVertexes() {
        return vertexes;
    }

    private VertexSet<T> union(Vertex<T> v) {
        return new VertexSet<>(vertexes, v);
    }

    private VertexSet<T> intersect(VertexSet<T> vertexSet) {
        return new VertexSet<T>(new HashSet<>(CollectionUtils.intersection(vertexes, vertexSet.getVertexes())));
    }

    private VertexSet<T> substract(Vertex v) {
        Set<Vertex<T>> vertexes = new HashSet<>(this.vertexes);
        vertexes.remove(v);
        return new VertexSet<>(vertexes);
    }

    void add(Vertex<T> vertex4) {
        vertexes.add(vertex4);
    }

    public <T> boolean contains(Vertex tVertex) {
        return vertexes.contains(tVertex);
    }

    public boolean contains(T duplicate) {
        return vertexes.contains(new Vertex<>(duplicate));
    }

    public static <T> VertexSet<T> empty() {
        return VertexSet.EMPTY;

    }

    public Set<T> asSet() {
        return vertexes.stream().map(Vertex::getId).collect(Collectors.toSet());
    }

    public static <T> Collection<VertexSet<T>> normalize(VertexSet<T> vertexSet, Map<Pair<T, T>, Double> weights) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(vertexSet.size());
        List<Vertex<T>> vertices = new ArrayList<>(vertexSet.getVertexes());
        Map<Vertex<T>, Integer> vertexMap = IntStream.range(0, vertices.size()).boxed().collect(Collectors.toMap(vertices::get, Function.identity()));
        for (Vertex<T> vertex : vertices) {
            for (Vertex<T> next : vertex.neighbourSet().getVertexes()) {
                if (!vertexMap.containsKey(next)) {
                    continue;
                }
                ImmutablePair<T, T> key = new ImmutablePair<>(vertex.getId(), next.getId());
                edgeWeightedGraph.addEdge(new Edge(vertexMap.get(vertex), vertexMap.get(next), weights.get(key)));
            }
        }
        GlobalMincut globalMincut = new GlobalMincut(edgeWeightedGraph);
        VertexSet<T> left = new VertexSet<>();
        VertexSet<T> right = new VertexSet<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex<T> tVertex = vertices.get(i);
            if (globalMincut.cut(i)) {
                left.add(tVertex);
            } else {
                right.add(tVertex);
            }
        }
        purge(left);
        purge(right);

        Collection<VertexSet<T>> sets = new HashSet<>();
        update(weights, left, sets);
        update(weights, right, sets);
        return sets;
    }

    private static <T> void purge(VertexSet<T> vertexSet) {
        for (Vertex<T> vertex : vertexSet.getVertexes()) {
            VertexSet<T> vertexSet1 = vertex.neighbourSet();
            Set<Vertex<T>> vertexes = new HashSet<>(vertexSet1.getVertexes());
            for (Vertex<T> vertex1 : vertexes) {
                if (!vertexSet.getVertexes().contains(vertex1)) {
                    vertexSet1.getVertexes().remove(vertex1);
                }
            }
        }
    }

    private static <T> void update(Map<Pair<T, T>, Double> weights, VertexSet<T> vertexSet, Collection<VertexSet<T>> sets) {
        Set<VertexSet<T>> vertexSets = bronKerbosch(vertexSet);
        if (vertexSets.size() > 1) {
            sets.addAll(normalize(vertexSet, weights));
        } else {
            sets.add(vertexSet);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VertexSet<?> vertexSet = (VertexSet<?>) o;

        return new EqualsBuilder()
                .append(vertexes, vertexSet.vertexes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(vertexes)
                .toHashCode();
    }


    private int size() {
        return vertexes.size();
    }
}
