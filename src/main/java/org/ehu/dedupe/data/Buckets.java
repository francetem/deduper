package org.ehu.dedupe.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Buckets<I> {

    private final Map<I, Set<Integer>> inverseBuckets;
    private final Map<Integer, Set<I>> buckets;

    public Buckets(Map<I, Set<Integer>> inverseBuckets) {
        this.inverseBuckets = inverseBuckets;
        this.buckets = new HashMap<>();
        for (Map.Entry<I, Set<Integer>> entry : inverseBuckets.entrySet()) {
            for (Integer id : entry.getValue()) {
                if (!buckets.containsKey(id)) {
                    buckets.put(id, new HashSet<>());
                }
                buckets.get(id).add(entry.getKey());
            }
        }
    }

    public static <I> Buckets<I> from(List<Set<I>> clusters) {
        Map<I, Set<Integer>> inverses = new HashMap<>();
        for (I element : clusters.stream().flatMap(Collection::stream).collect(Collectors.toSet())) {
            inverses.put(element, new HashSet<>());
        }

        for (int i = 0; i < clusters.size(); i++) {
            for (I x : clusters.get(i)) {
                inverses.get(x).add(i);
            }
        }
        return new Buckets<>(inverses);
    }

    public Boolean isSameBucket(I id1, I id2) {
        return inverseBuckets.containsKey(id1) && inverseBuckets.containsKey(id2) && inverseBuckets.get(id1).stream().anyMatch(x -> inverseBuckets.get(id2).contains(x));
    }

    public Collection<Set<I>> clusters() {
        return buckets.values();
    }

    public Collection<Integer> containing(I t) {
        return inverseBuckets.get(t);
    }

    public Set<I> get(Integer clusterId) {
        return buckets.get(clusterId);
    }
}
