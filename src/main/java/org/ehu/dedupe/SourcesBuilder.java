package org.ehu.dedupe;

import org.ehu.dedupe.data.Source;

import java.util.ArrayList;
import java.util.List;

public class SourcesBuilder<K extends Comparable<K>, S extends Source<K>> {

    private final List<S> sources = new ArrayList<>();

    public void add(S sSource) {
        sources.add(sSource);
    }

    public SourcesBuilder<K, S> combine(SourcesBuilder<K, S> tSourcesBuilder) {
        sources.addAll(tSourcesBuilder.sources);
        return this;
    }

    public Sources<K, S> build() {
        return Sources.from(sources);
    }
}
