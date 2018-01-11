package org.ehu.dedupe.graph;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class VertexTest {
    @Test
    public void testAddNeighbour() throws Exception {
        Vertex<Integer> vertex1 = new Vertex<>(1);
        Vertex<Integer> vertex2 = new Vertex<>(2);
        vertex1.addNeighbour(vertex2);

        assertTrue(vertex1.neighbourSet().getVertexes().contains(vertex2));
        assertTrue(vertex2.neighbourSet().getVertexes().contains(vertex1));
    }

}