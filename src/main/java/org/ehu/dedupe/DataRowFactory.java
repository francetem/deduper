package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.derive.CalculationResult;
import org.ehu.dedupe.derive.FeatureCalculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataRowFactory {

    public <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> Set<D> from(DataRowBuilder<I, E, D> dataRowBuilder) {
        return streamFrom(dataRowBuilder).collect(Collectors.toSet());
    }

    public <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> Stream<D> streamFrom(DataRowBuilder<I, E, D> dataRowBuilder) {
        BiFunction<DataRowBuilder<I, E, D>, Integer, Stream<E>> targetFunction = (dataRowBuilderParam, index) -> target(dataRowBuilder, index);
        return streamFrom(dataRowBuilder, targetFunction);
    }

    /**
     * Return the dataset resulting, will filter out any source not contained in the buckets if specified in dataRowBuilder
     *
     * @param dataRowBuilder containing the information necessary to generate the dataset
     * @param targetFunction comparison target
     * @param <I>            type of element
     * @param <E>            source type
     * @param <D>            datarow type
     * @return a stream of datarows
     */
    public <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> Stream<D> streamFrom(DataRowBuilder<I, E, D> dataRowBuilder, BiFunction<DataRowBuilder<I, E, D>, Integer, Stream<E>> targetFunction) {
        List<CalculationResult<FeatureCalculator, D>> calculationResults = calculateParallel(dataRowBuilder, targetFunction);

        return calculationResults
                .parallelStream()
                .peek(CalculationResult::assign)
                .map(CalculationResult::getDataRow);
    }

    private <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> List<CalculationResult<FeatureCalculator, D>> calculateParallel(DataRowBuilder<I, E, D> dataRowBuilder, BiFunction<DataRowBuilder<I, E, D>, Integer, Stream<E>> targetFunction) {

        List<E> dataRowBuilderSources = dataRowBuilder.getSources();

        if (dataRowBuilder.onlyInBucket()) {
            dataRowBuilderSources = dataRowBuilderSources.stream().filter(x -> dataRowBuilder.getBuckets().contains(x.getId())).collect(Collectors.toList());
        }

        final List<E> sources = dataRowBuilderSources;

        return IntStream
                .range(0, dataRowBuilderSources.size() - 1)
                .boxed()
                .parallel()
                .flatMap(index1 -> {
                    Stream<E> target = targetFunction.apply(dataRowBuilder, index1);
                    return toDataRow(dataRowBuilder, index1, target, sources.get(index1));
                }).collect(Collectors.toList());
    }

    private <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> Stream<CalculationResult<FeatureCalculator, D>> toDataRow(DataRowBuilder<I, E, D> dataRowBuilder, Integer index1, Stream<E> target, E source) {
        return target
                .filter(targetSource -> dataRowBuilder.getBlockingPredicate().test(targetSource, dataRowBuilder.getSources().get(index1)))
                .flatMap(y -> {
                    I id1 = source.getId();
                    I id2 = y.getId();

                    try {
                        Constructor<D> classConstructor = dataRowBuilder.getDataRowClass().getConstructor(id1.getClass(), id2.getClass(), Boolean.class);
                        D dataRow = classConstructor.newInstance(id1, id2, dataRowBuilder.getBuckets().isSameBucket(id1, id2));
                        Stream<CalculationResult<FeatureCalculator, D>> calculationResultStream = dataRowBuilder.getFeatureCalculators().stream().parallel().map(feature -> feature.calculate(source, y, dataRow));
                        return calculationResultStream;
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        throw new InvalidDataRowConstruction(e);
                    }
                });
    }

    private <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> Stream<E> target(DataRowBuilder<I, E, D> dataRowBuilder, Integer index1) {
        return IntStream.range(index1 + 1, dataRowBuilder.getSources().size())
                .boxed()
                .parallel()
                .map(dataRowBuilder.getSources()::get);
    }

}
