package org.ehu.dedupe.metrics.distance;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class GMD {

    private final List<Pair<Set, Set>> splits = new ArrayList<>();
    private final List<Pair<Set, Set>> merges = new ArrayList<>();

    <T> BigDecimal unitCostFunction(Set<T> left, Set<T> right) {
        if (left.isEmpty() || right.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ONE;
    }

    public <I> BigDecimal cost(Buckets<I> er, Buckets<I> gt) {
        return cost(er, gt, this::split, this::merge);
    }

    private <I> BigDecimal merge(Set<I> left, Set<I> right) {
        return update(left, right, merges);
    }

    private <I> BigDecimal split(Set<I> left, Set<I> right) {
        return update(left, right, splits);
    }

    private <I> BigDecimal update(Set<I> left, Set<I> right, List<Pair<Set, Set>> pairs) {
        BigDecimal result = unitCostFunction(left, right);
        if (!BigDecimal.ZERO.equals(result)) {
            pairs.add(new ImmutablePair<>(left, right));
        }
        return result;
    }

    /**
     * Calculate the cost of splitting and merging from gt to er
     * Any element in er not contained in gt will be ignores
     *
     * @param er     entity resolution clusters
     * @param gt     ground truth
     * @param fSplit split function calculation
     * @param fMerge merge function calculation
     * @param <I>    type of elements
     * @return The GMD cost from er to gt
     */
    public <I> BigDecimal cost(Buckets<I> er, Buckets<I> gt, BiFunction<Set<I>, Set<I>, BigDecimal> fSplit, BiFunction<Set<I>, Set<I>, BigDecimal> fMerge) {

        Map<Integer, Set<Set<I>>> merging = new HashMap<>();
        BigDecimal cost = BigDecimal.ZERO;
        for (Set<I> es : er.clusters()) {
            Set<Integer> tIds = new HashSet<>();
            for (I t : es) {
                if (gt.contains(t)) {
                    Collection<Integer> containing = gt.containing(t);
                    tIds.addAll(containing);
                }
            }
            Set<I> splitting = new HashSet<>(es);
            for (Integer id : tIds) {
                Collection<I> intersection = CollectionUtils.intersection(splitting, gt.get(id));
                splitting = new HashSet<>(CollectionUtils.subtract(splitting, intersection));
                cost = cost.add(fSplit.apply(new HashSet<>(intersection), splitting));
                if (!merging.containsKey(id)) {
                    merging.put(id, new HashSet<>());
                }
                merging.get(id).add(new HashSet<I>(intersection));
            }
        }
        for (Map.Entry<Integer, Set<Set<I>>> toMerge : merging.entrySet()) {
            Iterator<Set<I>> iterator = toMerge.getValue().iterator();
            Set<I> merge = new HashSet<>(iterator.next());
            while (iterator.hasNext()) {
                cost = cost.add(fMerge.apply(merge, iterator.next()));
            }
        }
        return cost;
    }

    public List<Pair<Set, Set>> getSplits() {
        return splits;
    }

    public List<Pair<Set, Set>> getMerges() {
        return merges;
    }
}
