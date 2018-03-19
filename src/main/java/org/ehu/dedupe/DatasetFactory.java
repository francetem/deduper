package org.ehu.dedupe;

import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.derive.FeatureDeriver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class DatasetFactory {

    public <K extends Comparable<K>, E extends Source<K>, D extends DataRow<K>> DataSet<K> from(DatasetBuilder<K, E, D> datasetBuilder) {

        BiPredicate<E, E> blockingPredicate = datasetBuilder.getBlockingPredicate();

        Sources<K, E> sources = Sources.from(new ArrayList<>(datasetBuilder.getSources()));

        Buckets<K> buckets = datasetBuilder.getBuckets();
        if (datasetBuilder.onlyInBucket()) {
            sources = sources.onlyIn(buckets);
        }
        List<? extends FeatureDeriver> featureDerivers = datasetBuilder.getFeatureDerivers();
        DatarowFactory<D, K> datarowFactory = datasetBuilder.getDatarowFactory();

        return sources
                .block(blockingPredicate)
                .<D>deriving()
                .withFeatureDerivers(featureDerivers)
                .withDatarowFactory(datarowFactory)
                .withBuckets(buckets)
                .derive();
    }

}
