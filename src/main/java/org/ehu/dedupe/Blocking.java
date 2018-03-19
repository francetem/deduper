package org.ehu.dedupe;

import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Blocking<K extends Comparable<K>, S extends Source<K>> {
    private final Stream<Pair<S, S>> blockResult;

    public Blocking(Stream<Pair<S, S>> blockResult) {
        this.blockResult = blockResult;
    }

    public <D extends DataRow<K>> DerivingBuilder<K, D, S> deriving() {
        return new DerivingBuilder<>(blockResult);
    }

    public List<Pair<S, S>> getPairs() {
        return blockResult.collect(Collectors.toList());
    }
}
