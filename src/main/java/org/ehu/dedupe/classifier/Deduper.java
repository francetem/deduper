package org.ehu.dedupe.classifier;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.graph.Vertex;
import org.ehu.dedupe.graph.VertexSet;
import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Deduper {

    private static final Logger LOGGER = Logger.getLogger("deduper");

    public static Buckets<String> dedup(Instances dataSet, AbstractClassifier... classifiers) throws Exception {
        Map<String, Set<String>> duplicates = new HashMap<>();

        Iterator<Instance> iterator = dataSet.iterator();
        int index = dataSet.classAttribute().indexOfValue("true");
        Map<Pair<String, String>, Double> weights = new HashMap<>();
        while (iterator.hasNext()) {
            Instance instance = iterator.next();

            double value = 0;
            int count = 0;
            for (AbstractClassifier classifier : classifiers) {
                try {
                    value = classifier.distributionForInstance(instance)[index];
                } catch (Exception e) {
                    LOGGER.warning("couldn't evaluate: " + instance.stringValue(0) + " " + instance.stringValue(1) + " for classifier: " + count);
                }
                count++;
            }
            if (count == 0) {
                LOGGER.severe("no classification for: " + instance.stringValue(0) + " " + instance.stringValue(1));
                continue;
            }
            value /= count;
            if (value > 0.5) {
                String id1;
                String id2;
                if (instance.attribute(0).isNumeric()) {
                    id1 = String.valueOf(instance.value(0));
                    id2 = String.valueOf(instance.value(1));
                } else {
                    id1 = instance.stringValue(0);
                    id2 = instance.stringValue(1);
                }
                weights.put(new ImmutablePair<>(id1, id2), value);
                weights.put(new ImmutablePair<>(id2, id1), value);
                duplicates.computeIfAbsent(id1, x -> new HashSet<>()).add(id2);
                duplicates.computeIfAbsent(id2, x -> new HashSet<>()).add(id1);
            }
        }

        Set<VertexSet<String>> bucks = new HashSet<>();

        for (String key : new HashSet<>(duplicates.keySet())) {
            if (!duplicates.containsKey(key)) {
                continue;
            }
            VertexSet<String> vertexSet = VertexSet.renderGraph(duplicates, new Vertex<>(key));
            vertexSet.getVertexes().stream().map(Vertex::getId).forEach(duplicates::remove);
            //neo4jStore.store(vertexSet, "instance", weights);
            Set<VertexSet<String>> vertexSets = VertexSet.bronKerbosch(vertexSet);
            if (vertexSets.size() > 1) {
                bucks.addAll(VertexSet.normalize(vertexSet, weights));
            } else {
                bucks.addAll(vertexSets);
            }
        }
        List<Set<String>> dups = bucks.stream().map(VertexSet::asSet).collect(Collectors.toList());
        return Buckets.from(dups);
    }
}
