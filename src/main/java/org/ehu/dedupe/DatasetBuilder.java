package org.ehu.dedupe;

import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.data.dyna.DynaFactory;
import org.ehu.dedupe.derive.FeatureDeriver;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class DatasetBuilder<K extends Comparable<K>, S extends Source<K>, D extends DataRow<K>> {

    private List<S> sources;
    private BiPredicate<S, S> blockingPredicate = (S x, S y) -> true;
    private Buckets<K> buckets = Buckets.from(Collections.emptyList());
    private List<? extends FeatureDeriver> featureDerivers = Collections.emptyList();
    private boolean inBucket = false;
    private DatarowFactory<D, K> factory;

    public DatasetBuilder(DatarowFactory<D, K> factory) {
        this.factory = factory;
    }

    public DatasetBuilder() {
        this.factory = new DynaFactory();
    }

    public DatarowFactory<D, K> getDatarowFactory() {
        return factory;
    }

    public List<S> getSources() {
        return sources;
    }

    public BiPredicate<S, S> getBlockingPredicate() {
        return blockingPredicate;
    }

    public Buckets<K> getBuckets() {
        return buckets;
    }

    public List<? extends FeatureDeriver> getFeatureDerivers() {
        return featureDerivers;
    }


    public DatasetBuilder<K, S, D> withSources(List<S> sources) {
        this.sources = sources;
        return this;
    }

    public DatasetBuilder<K, S, D> withBlockingPredicate(BiPredicate<S, S> blockingPredicate) {
        this.blockingPredicate = blockingPredicate;
        return this;
    }

    public DatasetBuilder<K, S, D> withFeatureDerivers(List<? extends FeatureDeriver> featureCalculators) {
        this.featureDerivers = featureCalculators;
        return this;
    }

    public DatasetBuilder<K, S, D> withBuckets(Buckets<K> buckets) {
        this.buckets = buckets;
        return this;
    }

    public boolean onlyInBucket() {
        return inBucket;
    }

    public DatasetBuilder<K, S, D> inBucket(boolean inBucket) {
        this.inBucket = inBucket;
        return this;
    }
}
