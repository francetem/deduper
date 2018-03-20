package org.ehu.dedupe.metrics.distance;

import org.apache.commons.collections4.CollectionUtils;
import org.ehu.dedupe.data.Buckets;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class GMD {

    private final Collection<Collection<Collection>> splits = new ArrayList<>();
    private final Collection<Collection<Collection>> merges = new ArrayList<>();

    public <I> BigDecimal cost(Buckets<I> er, Buckets<I> gt) {
        return cost(er, gt, this::split, this::merge);
    }

    private <I> BigDecimal merge(Collection<Collection<I>> clusters) {
        return update(clusters);
    }

    private <I> BigDecimal split(Collection<Collection<I>> clusters) {
        return update(clusters);
    }

    private <I> BigDecimal update(Collection<Collection<I>> clusters) {
        return BigDecimal.valueOf(clusters.size() - 1);
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
    public <I> BigDecimal cost(Buckets<I> er, Buckets<I> gt, Function<Collection<Collection<I>>, BigDecimal> fSplit, Function<Collection<Collection<I>>, BigDecimal> fMerge) {

        Map<Integer, Collection<Collection<I>>> merging = new HashMap<>();
        Collection<Collection<Collection<I>>> splitting = new ArrayList<>();
        for (Set<I> es : er.clusters()) {
            Set<Integer> tIds = intersectingIds(gt, es);

            if (tIds.size() > 1) {
                Collection<Collection<I>> currentSplits = new ArrayList<>();
                for (Integer id : tIds) {
                    Set<I> set = gt.get(id);
                    Collection<I> intersection = CollectionUtils.intersection(set, es);
                    currentSplits.add(intersection);
                }
                splitting.add(currentSplits);
            }

            for (Integer id : tIds) {
                Set<I> set = gt.get(id);
                Collection<I> intersection = CollectionUtils.intersection(set, es);
                if (intersection.size() > 0 && intersection.size() < set.size()) {
                    if (!merging.containsKey(id)) {
                        merging.put(id, new ArrayList<>());
                    }
                    merging.get(id).add(intersection);
                }
            }
        }

        merges.addAll(new HashSet(merging.values()));
        splits.addAll(new HashSet(splitting));
        BigDecimal mergeCost = merging.values().stream()
                .map(fMerge)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal splitCost = splitting.stream()
                .map(fSplit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return mergeCost.add(splitCost);
    }

    public <I> Set<Integer> intersectingIds(Buckets<I> gt, Set<I> es) {
        Set<Integer> tIds = new HashSet<>();
        for (I t : es) {
            if (gt.contains(t)) {
                Collection<Integer> containing = gt.containing(t);
                tIds.addAll(containing);
            }
        }
        return tIds;
    }

    public Collection<Collection<Collection>> getSplits() {
        return splits;
    }

    public Collection<Collection<Collection>> getMerges() {
        return merges;
    }
}
