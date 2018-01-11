package org.ehu.dedupe.graph.log4j;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;
import iot.jcypher.graph.GrNode;
import iot.jcypher.graph.GrRelation;
import iot.jcypher.graph.Graph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.graph.Vertex;
import org.ehu.dedupe.graph.VertexSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Neo4jStore {

    public static final String RELATION_NAME = "IS_SAME";
    public static final String WEIGHT_PROPERTY_NAME = "weight";
    private final IDBAccess remote;

    public Neo4jStore(String serverRootUri) {
        this(DBAccessFactory.createDBAccess(DBType.REMOTE, getProperties(serverRootUri)));
    }

    private static Properties getProperties(String serverRootUri) {
        Properties props = new Properties();
        props.setProperty(DBProperties.SERVER_ROOT_URI, serverRootUri);
        props.setProperty(DBProperties.SERVER_ROOT_URI, "bolt://localhost:7687");
        props.setProperty(DBProperties.DATABASE_DIR, "C:/NEO4J_DBS/01");
        return props;
    }

    public Neo4jStore(IDBAccess idbAccess) {
        remote = idbAccess;
    }

    static <T> void store(VertexSet<T> vertexSet, String label, Graph graph) {
        store(vertexSet, label, Collections.emptyMap(), graph);
    }

    public <T> void store(VertexSet<T> vertexSet, String label, Map<Pair<T, T>, Double> weights) {
        Graph graph = Graph.create(remote);
        store(vertexSet, label, weights, graph);
    }

    static <T> void store(VertexSet<T> x, String label, Map<Pair<T, T>, Double> weights, Graph graph) {
        Map<Vertex<T>, GrNode> toNode = new HashMap<>();

        for (Vertex<T> vertex : x.getVertexes()) {
            T id = vertex.getId();
            GrNode vertexNode = graph.createNode();

            vertexNode.addLabel(label);
            vertexNode.addProperty("id", id);

            toNode.put(vertex, vertexNode);
        }

        for (Vertex<T> vertex : x.getVertexes()) {
            GrNode startNode = toNode.get(vertex);
            for (Vertex<T> y : vertex.neighbourSet().getVertexes()) {
                GrRelation relation = graph.createRelation(RELATION_NAME, startNode, toNode.get(y));
                Double weight = weights.get(new ImmutablePair<>(vertex.getId(), y.getId()));
                if (weight != null) {
                    relation.addProperty(WEIGHT_PROPERTY_NAME, weight);
                }
            }
        }
        graph.store();
    }
}
