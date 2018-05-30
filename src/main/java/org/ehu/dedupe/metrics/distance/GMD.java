package org.ehu.dedupe.metrics.distance;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.ehu.dedupe.data.Buckets;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GMD {

    public <I> GmdCost<I> cost(Buckets<I> er, Buckets<I> gt) {
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
    public <I> GmdCost<I> cost(final Buckets<I> er, final Buckets<I> gt, Function<Collection<Collection<I>>, BigDecimal> fSplit, Function<Collection<Collection<I>>, BigDecimal> fMerge) {

        List<Set<I>> erClusters = new ArrayList<>(er.clusters());
        List<Set<I>> gtClusters = new ArrayList<>(gt.clusters());

        Set<I> missingEr = gtClusters.stream()
                .flatMap(Collection::stream)
                .filter(x -> !er.contains(x))
                .collect(Collectors.toSet());

        Set<Set<I>> missingAsClusters = missingEr.stream().map(Collections::singleton).collect(Collectors.toSet());
        erClusters.addAll(missingAsClusters);

        return cost(fSplit, fMerge, erClusters, gtClusters);
    }

    private <T> GmdCost<T> cost(Function<Collection<Collection<T>>, BigDecimal> fSplit, Function<Collection<Collection<T>>, BigDecimal> fMerge, List<Set<T>> erClusters, List<Set<T>> gtClusters) {
        Map<T, Integer> indexedGtElements = index(gtClusters);

        final Collection<Collection<Collection<T>>> splits = new ArrayList<>();

        Collection<List<Map.Entry<ImmutablePair<Integer, Integer>, List<T>>>> splitPartitions = IntStream.range(0, erClusters.size())
                .boxed()
                .map(x -> erClusters.get(x).stream().collect(Collectors.groupingBy(y -> new ImmutablePair<>(x, indexedGtElements.get(y)))))
                .peek(x -> updateSplits(splits, x))
                .flatMap(x -> x.entrySet().stream())
                .collect(Collectors.groupingBy(x -> x.getKey().getRight()))
                .values();

        Collection<Collection<Collection<T>>> merges = splitPartitions
                .stream()
                .filter(x -> x.size() > 1)
                .map(x -> new ArrayList<Collection<T>>(x.stream().map(Map.Entry::getValue).collect(Collectors.toList())))
                .collect(Collectors.toList());

        BigDecimal splitCost = splits.stream()
                .map(fSplit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal mergeCost = merges.stream()
                .map(fMerge)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cost = splitCost.add(mergeCost);

        return new GmdCost<>(splits, merges, cost);
    }

    private <I> void updateSplits(Collection<Collection<Collection<I>>> splits, Map<ImmutablePair<Integer, Integer>, List<I>> x) {
        if (x.size() > 1) {
            splits.add(new ArrayList<>(x.values()));
        }
    }

    private <I> Map<I, Integer> index(List<Set<I>> gtClusters) {
        Map<I, Integer> indexes = new HashMap<>();
        for (int i = 0; i < gtClusters.size(); i++) {
            final int index = i;
            gtClusters.get(i).forEach(x -> indexes.put(x, index));
        }
        return indexes;
    }
}
