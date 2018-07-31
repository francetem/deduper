package org.ehu.dedupe.classifier;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class Deduper {

    private static final Logger LOGGER = Logger.getLogger("deduper");

    private final Instances dataSet;
    private final Classifier classifier;

    public Deduper(Instances dataSet, Classifier classifier) {
        this.dataSet = dataSet;
        this.classifier = classifier;
    }

    public Buckets<String> dedup() {
        return dedup(0.5);
    }

    /**
     * Clusterize the instances according to the classifications provided by the classifiers
     * Any pair referencing the same element will be ignored.
     *
     * @param threshold value above which probability we will consider a duped
     * @return The resulting clusters
     */
    public Buckets<String> dedup(double threshold) {
        return doPairResolution(threshold, classifier).toNormalizedClusters();
    }

    public PairResolution doPairResolution(double threshold) {
        return doPairResolution(threshold, classifier);
    }

    private PairResolution doPairResolution(double threshold, Classifier classifier) {

        Iterator<Instance> iterator = dataSet.iterator();

        Map<Pair<String, String>, Double> weights = new HashMap<>();
        while (iterator.hasNext()) {
            Instance instance = iterator.next();

            String id1 = getId(instance, 0);
            String id2 = getId(instance, 1);

            if (Objects.equals(id1, id2)) {
                continue;
            }

            double value;
            try {
                value = classifier.classify(instance);
            } catch (ClassifierException e) {
                LOGGER.severe(e.getMessage());
                continue;
            }

            weights.put(new ImmutablePair<>(id1, id2), value);
            weights.put(new ImmutablePair<>(id2, id1), value);

        }

        return doPairResolution(threshold, weights);
    }

    public static PairResolution doPairResolution(double threshold, Map<Pair<String, String>, Double> weights) {
        Map<String, Set<String>> duplicates = new HashMap<>();
        for(Map.Entry<Pair<String, String>, Double> entry : weights.entrySet()){
            Pair<String, String> key = entry.getKey();
            String id1 = key.getLeft();
            String id2 = key.getRight();
            Set<String> duplicates1 = duplicates.computeIfAbsent(id1, x -> new HashSet<>());
            Set<String> duplicates2 = duplicates.computeIfAbsent(id2, x -> new HashSet<>());
            if (entry.getValue() >= threshold) {
                duplicates1.add(id2);
                duplicates2.add(id1);
            }
        }

        return new PairResolution(duplicates, weights);
    }

    private static String getId(Instance instance, int indexId) {
        String id1;
        if (instance.attribute(indexId).isNumeric()) {
            id1 = String.valueOf(instance.value(indexId));
        } else {
            id1 = instance.stringValue(indexId);
        }
        return id1;
    }

}
