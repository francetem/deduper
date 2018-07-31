package org.ehu.dedupe;

import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.data.dyna.DynaFactory;
import org.ehu.dedupe.derive.CalculationResult;
import org.ehu.dedupe.derive.FeatureCalculator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deriving<I extends Comparable<I>, D extends DataRow<I>> {
    private final Buckets<I> buckets;
    private final DatarowFactory<D, I> datarowFactory;
    private final List<? extends FeatureCalculator> featureCalculators;

    public Deriving(Buckets<I> buckets, DatarowFactory<D, I> datarowFactory, List<? extends FeatureCalculator> featureCalculators) {
        this.buckets = buckets;
        this.datarowFactory = datarowFactory;
        this.featureCalculators = featureCalculators;
    }

    public Deriving(List<? extends FeatureCalculator> featureCalculators) {
        this.buckets = Buckets.empty();
        this.datarowFactory = new DynaFactory();
        this.featureCalculators = featureCalculators;
    }

    private static <I extends Comparable<I>, S extends Source<I>, D extends DataRow<I>> Function<Pair<S, S>, Stream<? extends CalculationResult<FeatureCalculator, D>>> calculate(List<? extends FeatureCalculator> featureCalculators, DatarowFactory<D, I> diReflectionDatarowFactory, Buckets<I> buckets) {
        return y -> calculate(featureCalculators, diReflectionDatarowFactory, buckets, y);
    }

    private static <I extends Comparable<I>, S extends Source<I>, D extends DataRow<I>> Stream<? extends CalculationResult<FeatureCalculator, D>> calculate(List<? extends FeatureCalculator> featureCalculators, DatarowFactory<D, I> datarowFactory, Buckets<I> buckets, Pair<S, S> y) {
        S left = y.getLeft();
        I id1 = left.getId();
        S right = y.getRight();
        I id2 = right.getId();

        try {
            D dataRow = datarowFactory.create(id1, id2, buckets.isSameBucket(id1, id2));
            return featureCalculators.stream().parallel().map(feature -> feature.calculate(left, right, dataRow));
        } catch (DataRowException e) {
            throw new InvalidDataRowConstruction(e);
        }
    }

    public <E extends Source<I>> Stream<D> derive(Stream<Pair<E, E>> block) {

        List<CalculationResult<FeatureCalculator, D>> calculationResults = block
                .flatMap(calculate(getFeatureCalculators(), getDatarowFactory(), getBuckets()))
                .collect(Collectors.toList());

        return calculationResults
                .parallelStream()
                .peek(CalculationResult::assign)
                .map(CalculationResult::getDataRow);
    }

    public Buckets<I> getBuckets() {
        return buckets;
    }

    public DatarowFactory<D, I> getDatarowFactory() {
        return datarowFactory;
    }

    public List<? extends FeatureCalculator> getFeatureCalculators() {
        return featureCalculators;
    }
}
