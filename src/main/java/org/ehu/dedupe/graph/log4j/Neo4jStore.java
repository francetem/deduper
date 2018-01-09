package org.ehu.dedupe.graph.log4j;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;
import iot.jcypher.graph.GrNode;
import iot.jcypher.graph.GrRelation;
import iot.jcypher.graph.Graph;
import iot.jcypher.query.result.JcError;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.graph.Vertex;
import org.ehu.dedupe.graph.VertexSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class Neo4jStore {

    private final IDBAccess remote;

    public Neo4jStore(String serverRootUri) {
        Properties props = new Properties();
        props.setProperty(DBProperties.SERVER_ROOT_URI, serverRootUri);
        props.setProperty(DBProperties.SERVER_ROOT_URI, "bolt://localhost:7687");
        props.setProperty(DBProperties.DATABASE_DIR, "C:/NEO4J_DBS/01");
        remote = DBAccessFactory.createDBAccess(DBType.REMOTE, props);
    }

    public <T> void store(VertexSet<T> x, String label, Map<Pair<String, String>, Double> weights) {

        Graph graph = Graph.create(remote);

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
                if (toNode.get(y) == null) {
                    System.out.println(y.getId() + " doesn't exists");
                }

                if (startNode == null) {
                    System.out.println(y.getId() + " doesn't exists");
                }
                GrRelation relation = graph.createRelation("IS_SAME", startNode, toNode.get(y));
                Double weight = weights.get(new ImmutablePair<>(vertex.getId(), y.getId()));
                relation.addProperty("weight", weight);
            }

        }
        List<JcError> errors = graph.store();
    }
}
