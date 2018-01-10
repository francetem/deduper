package org.ehu.dedupe.graph.log4j;

import iot.jcypher.graph.GrNode;
import iot.jcypher.graph.GrRelation;
import iot.jcypher.graph.Graph;
import org.ehu.dedupe.graph.Vertex;
import org.ehu.dedupe.graph.VertexSet;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Neo4jStoreTest {

    @Mock
    Graph graph;

    @Mock
    GrNode grNode1, grNode2;

    @Mock
    GrRelation grRelation;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStore() throws Exception {
        Vertex<Integer> vertex1 = new Vertex<>(1);
        Vertex<Integer> vertex2 = new Vertex<>(2);
        vertex1.addNeighbour(vertex2);

        when(graph.createNode()).thenReturn(grNode1, grNode2);
        when(graph.createRelation(Neo4jStore.RELATION_NAME, grNode1, grNode2)).thenReturn(grRelation);
        Neo4jStore.store(new VertexSet<>(Stream.of(vertex1, vertex2).collect(Collectors.toSet())), "test", graph);

        verify(grNode1).addLabel("test");
        verify(grNode1).addProperty("id", 1);

        verify(grNode2).addLabel("test");
        verify(grNode2).addProperty("id", 2);

        verify(graph).store();
    }

}