package org.ehu.dedupe.graph;

import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class VertexSetTest {

    private final Map<Integer, Set<Integer>> duplicates = new HashMap<>();

    @Test
    public void testBronKerbosch() throws Exception {
        Vertex<Integer> vertex1 = new Vertex<>(1);
        Vertex<Integer> vertex4 = new Vertex<>(4);
        Vertex<Integer> vertex5 = new Vertex<>(5);
        Vertex<Integer> vertex6 = new Vertex<>(6);
        vertex6.addNeighbour(vertex4);
        Vertex<Integer> vertex3 = new Vertex<>(3);
        vertex4.addNeighbour(vertex3);
        vertex4.addNeighbour(vertex5);
        Vertex<Integer> vertex2 = new Vertex<>(2);
        vertex3.addNeighbour(vertex2);
        vertex5.addNeighbour(vertex2);
        vertex5.addNeighbour(vertex1);
        vertex2.addNeighbour(vertex1);
        VertexSet<Integer> vertexSet = new VertexSet<>(Stream.of(vertex1, vertex2, vertex3, vertex4, vertex5, vertex6).collect(Collectors.toSet()));
        Set<VertexSet<Integer>> vertexSets = VertexSet.bronKerbosch(vertexSet);
        assertEquals(5, vertexSets.size());
    }

    @Test
    public void testRenderGraph() throws Exception {

        Vertex<Integer> key = new Vertex<>(1);
        VertexSet<Integer> graph = VertexSet.renderGraph(duplicates, key);

        assertEquals(graph.getVertexes().size(), 6);
        assertTrue(key.neighbourSet().contains(new Vertex<>(2)));
        assertTrue(key.neighbourSet().contains(new Vertex<>(3)));
    }

    @Test
    public void testNormalize() throws Exception {
        Vertex<Integer> key = new Vertex<>(1);
        VertexSet<Integer> graph = VertexSet.renderGraph(duplicates, key);

        Map<Pair<Integer, Integer>, Double> weights = new HashMap<>();
        weights.put(Pair.of(1, 2), 1d);
        weights.put(Pair.of(1, 3), 1d);
        weights.put(Pair.of(2, 1), 1d);
        weights.put(Pair.of(2, 3), 1d);
        weights.put(Pair.of(2, 4), 0.75d);
        weights.put(Pair.of(3, 1), 1d);
        weights.put(Pair.of(3, 2), 1d);
        weights.put(Pair.of(3, 4), 0.5d);
        weights.put(Pair.of(4, 2), 1d);
        weights.put(Pair.of(4, 3), 1d);
        weights.put(Pair.of(4, 5), 1d);
        weights.put(Pair.of(4, 6), 1d);
        weights.put(Pair.of(5, 4), 1d);
        weights.put(Pair.of(5, 6), 1d);
        weights.put(Pair.of(6, 4), 1d);
        weights.put(Pair.of(6, 5), 1d);

        Collection<VertexSet<Integer>> normalized = VertexSet.normalize(graph, weights);
        assertEquals(normalized.size(), 2);
    }

    @BeforeMethod
    private void setUp() {
        duplicates.put(1, Stream.of(2, 3).collect(Collectors.toSet()));
        duplicates.put(2, Stream.of(1, 3, 4).collect(Collectors.toSet()));
        duplicates.put(3, Stream.of(1, 2, 4).collect(Collectors.toSet()));
        duplicates.put(4, Stream.of(2, 3, 5, 6).collect(Collectors.toSet()));
        duplicates.put(5, Stream.of(4, 6).collect(Collectors.toSet()));
        duplicates.put(6, Stream.of(4, 5).collect(Collectors.toSet()));
    }


}