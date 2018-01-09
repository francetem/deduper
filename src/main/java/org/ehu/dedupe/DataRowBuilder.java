package org.ehu.dedupe;

import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.derive.FeatureCalculator;

import java.util.List;
import java.util.function.BiPredicate;

public class DataRowBuilder<I, E extends Source<I>, D extends DataRow<I>> {
    private List<E> sources;
    private BiPredicate<E, E> blockingPredicate;
    private Buckets buckets;
    private List<? extends FeatureCalculator> featureCalculators;
    private Class<D> dataRowClass;

    public DataRowBuilder() {

    }

    public List<E> getSources() {
        return sources;
    }

    public BiPredicate<E, E> getBlockingPredicate() {
        return blockingPredicate;
    }

    public Buckets getBuckets() {
        return buckets;
    }

    public List<? extends FeatureCalculator> getFeatureCalculators() {
        return featureCalculators;
    }

    public Class<D> getDataRowClass() {
        return dataRowClass;
    }

    public DataRowBuilder<I, E, D> withSources(List<E> sources) {
        this.sources = sources;
        return this;
    }

    public DataRowBuilder<I, E, D> withBlockingPredicate(BiPredicate<E, E> blockingPredicate) {
        this.blockingPredicate = blockingPredicate;
        return this;
    }

    public DataRowBuilder<I, E, D> withBuckets(Buckets buckets) {
        this.buckets = buckets;
        return this;
    }

    public DataRowBuilder<I, E, D> withFeatureCalculators(List<? extends FeatureCalculator> featureCalculators) {
        this.featureCalculators = featureCalculators;
        return this;
    }

    public DataRowBuilder<I, E, D> withDataRowClass(Class<D> datarowClass) {
        this.dataRowClass = datarowClass;
        return this;
    }
}
