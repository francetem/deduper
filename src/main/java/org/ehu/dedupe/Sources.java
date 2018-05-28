package org.ehu.dedupe;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.Source;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sources<K extends Comparable<K>, S extends Source<K>> {

    private final List<S> sources;

    private Sources(List<S> sources) {
        this.sources = sources;
    }

    public static <K extends Comparable<K>, S extends Source<K>> Sources<K, S> from(List<S> sources) {
        return new Sources<>(sources);
    }

    public Blocking<K, S> block(BiPredicate<S, S> blockingPredicate) {
        return block(blockingPredicate, Integer.MAX_VALUE, (x, y) -> 0, Integer.MAX_VALUE);
    }

    public Blocking<K, S> block(BiPredicate<S, S> blockingPredicate, Comparator<S> sComparator, int maxSize, int cutoff) {
        return block(blockingPredicate, maxSize, sComparator, cutoff);
    }

    public Blocking<K, S> block(BiPredicate<S, S> blockingPredicate, Comparator<S> sComparator, int maxSize) {
        return block(blockingPredicate, maxSize, sComparator, Integer.MAX_VALUE);
    }

    public Blocking<K, S> block(BiPredicate<S, S> blockingPredicate, int maxSize, Comparator<S> sComparator, int cutoff) {
        return new Blocking<>(IntStream
                .range(0, size() - 1)
                .boxed()
                .parallel()
                .flatMap(index -> generateCombinations(blockingPredicate, maxSize, sComparator, index, cutoff)));
    }

    public Stream<? extends Pair<S, S>> generateCombinations(BiPredicate<S, S> blockingPredicate, int maxSize, Comparator<S> comparator, Integer index, long cutoff) {
        S comparingSource = get(index);
        return IntStream.range(index + 1, this.size())
                .boxed()
                .parallel()
                .map(this::get)
                .filter(targetSource -> blockingPredicate.test(targetSource, comparingSource))
                .limit(cutoff)
                .sorted(Comparator.comparingInt(x -> comparator.compare(x, comparingSource)))
                .limit(maxSize)
                .map(x -> new ImmutablePair<>(comparingSource, x));
    }

    public static <K extends Comparable<K>, S extends Source<K>> Collector<S, ?, Sources<K, S>> collector() {
        return new SourcesCollector<>();
    }

    public Sources<K, S> onlyIn(Buckets<K> buckets) {
        return Sources.from(sources.stream().filter(x -> buckets.contains(x.getId())).collect(Collectors.toList()));
    }

    public int size() {
        return sources.size();
    }

    public S get(Integer index) {
        return sources.get(index);
    }
}
