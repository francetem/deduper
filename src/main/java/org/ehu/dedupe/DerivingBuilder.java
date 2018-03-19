package org.ehu.dedupe;

import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.data.dyna.DynaFactory;
import org.ehu.dedupe.derive.FeatureDeriver;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DerivingBuilder<K extends Comparable<K>, D extends DataRow<K>, S extends Source<K>> {
    private final Stream<Pair<S, S>> blockResult;
    private List<? extends FeatureDeriver> featureDerivers;
    private DatarowFactory<D, K> datarowFactory = new DynaFactory();
    private Buckets<K> buckets = Buckets.empty();

    public DerivingBuilder(Stream<Pair<S, S>> blockResult) {
        this.blockResult = blockResult;
    }

    public DerivingBuilder<K, D, S> withFeatureDerivers(List<? extends FeatureDeriver> featureCalculators) {
        this.featureDerivers = featureCalculators;
        return this;
    }

    public DerivingBuilder<K, D, S> withDatarowFactory(DatarowFactory<D, K> datarowFactory) {
        this.datarowFactory = datarowFactory;
        return this;
    }

    public DataSet<K> derive() {
        Set<DataRow<K>> collect = new Deriving<>(buckets, datarowFactory, featureDerivers).derive(blockResult).collect(Collectors.toSet());
        return new DataSet<>(collect, featureDerivers);
    }

    public DerivingBuilder<K, D, S> withBuckets(Buckets<K> buckets) {
        this.buckets = buckets;
        return this;
    }
}
