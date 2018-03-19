package org.ehu.dedupe;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.Source;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sources<K extends Comparable<K>, S extends Source<K>> {

    private final List<S> sources;

    private Sources(List<S> sources) {
        this.sources = sources;
    }

    public static <K extends Comparable<K>, S extends Source<K>> Sources<K, S> from(List<S> sources) {
        return new Sources<>(sources);
    }

    public Blocking<K, S> block(BiPredicate<S, S> blockingPredicate) {

        return new Blocking<>(IntStream
                .range(0, size() - 1)
                .boxed()
                .parallel()
                .flatMap(index1 -> {
                    S theOtherSource = get(index1);
                    return IntStream.range(index1 + 1, this.size())
                            .boxed()
                            .parallel()
                            .map(this::get)
                            .filter(targetSource -> blockingPredicate.test(targetSource, theOtherSource))
                            .map(x -> new ImmutablePair<>(theOtherSource, x));
                }));
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
