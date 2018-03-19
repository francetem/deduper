package org.ehu.dedupe;

import org.ehu.dedupe.data.Source;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class SourcesCollector<K extends Comparable<K>, S extends Source<K>> implements Collector<S, SourcesBuilder<K, S>, Sources<K, S>> {

    @Override
    public Supplier<SourcesBuilder<K, S>> supplier() {
        return SourcesBuilder::new;
    }

    @Override
    public BiConsumer<SourcesBuilder<K, S>, S> accumulator() {
        return SourcesBuilder::add;
    }

    @Override
    public BinaryOperator<SourcesBuilder<K, S>> combiner() {
        return SourcesBuilder::combine;
    }

    @Override
    public Function<SourcesBuilder<K, S>, Sources<K, S>> finisher() {
        return SourcesBuilder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}
