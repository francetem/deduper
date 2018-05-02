package org.ehu.dedupe.data;

import com.google.gson.reflect.TypeToken;
import org.ehu.dedupe.io.JsonFileReader;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Buckets<I> {

    private final Map<I, Set<Integer>> inverseBuckets;
    private final Map<Integer, Set<I>> buckets;

    public Buckets(Map<I, Set<Integer>> inverseBuckets) {
        this.inverseBuckets = new HashMap<>(inverseBuckets);
        this.buckets = new HashMap<>();
        update(inverseBuckets);
    }

    public void update(Map<I, Set<Integer>> inverseBuckets) {
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
        return new Buckets<>(toInverse(clusters, 0));
    }

    public static <I> Map<I, Set<Integer>> toInverse(List<Set<I>> clusters, int max) {
        Map<I, Set<Integer>> inverses = new HashMap<>();
        for (I element : clusters.stream().flatMap(Collection::stream).collect(Collectors.toSet())) {
            inverses.put(element, new HashSet<>());
        }

        for (int i = 0; i < clusters.size(); i++) {
            for (I x : clusters.get(i)) {
                inverses.get(x).add(max + i);
            }
        }
        return inverses;
    }

    public void addAll(List<Set<I>> clusters) {
        Map<I, Set<Integer>> inverse = toInverse(clusters, buckets.keySet().stream().max(Integer::compareTo).orElse(-1) + 1);
        inverseBuckets.putAll(inverse);
        update(inverse);
    }

    public Boolean isSameBucket(I id1, I id2) {
        return inverseBuckets.containsKey(id1) && inverseBuckets.containsKey(id2) && inverseBuckets.get(id1).stream().anyMatch(x -> inverseBuckets.get(id2).contains(x));
    }

    public Set<I> duplicates(I id) {
        return inverseBuckets.containsKey(id) ? inverseBuckets.get(id).stream().flatMap(x -> buckets.get(x).stream()).filter(x -> !Objects.equals(id, x)).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public Collection<Set<I>> clusters() {
        return buckets.values();
    }

    public Collection<Integer> containing(I t) {
        return inverseBuckets.get(t);
    }

    public boolean contains(I t) {
        return inverseBuckets.containsKey(t);
    }

    public Set<I> get(Integer clusterId) {
        return buckets.get(clusterId);
    }

    public Set<I> keySet() {
        return clusters().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public void removeAll(Collection<I> toRemove) {
        toRemove.forEach(x -> {
            if (inverseBuckets.containsKey(x)) {
                Set<Integer> remove = inverseBuckets.remove(x);
                remove.forEach(y -> buckets.get(y).remove(x));
            }
        });
        Set<I> inverseBucketsKeySet = new HashSet<>(inverseBuckets.keySet());
        inverseBucketsKeySet.stream().filter(x -> inverseBuckets.get(x).isEmpty()).forEach(inverseBuckets::remove);
        Set<Integer> bucketsKeySet = new HashSet<>(buckets.keySet());
        bucketsKeySet.stream().filter(x -> buckets.get(x).isEmpty()).forEach(buckets::remove);
    }

    public static <T> Buckets<T> empty() {
        return new Buckets<>(Collections.emptyMap());
    }

    public static Buckets<String> load(String fileName) throws IOException {
        return Buckets.from(JsonFileReader.readJsonFile(new TypeToken<List<Set<String>>>() {
        }, fileName));
    }
}
