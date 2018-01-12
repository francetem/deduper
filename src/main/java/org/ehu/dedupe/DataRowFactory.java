package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.derive.CalculationResult;
import org.ehu.dedupe.derive.FeatureCalculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataRowFactory {

    public <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> List<D> from(DataRowBuilder<I, E, D> dataRowBuilder) {
        BiFunction<DataRowBuilder<I, E, D>, Integer, Stream<E>> targetFunction = (dataRowBuilderParam, index) -> target(dataRowBuilder, index);
        return from(dataRowBuilder, targetFunction);
    }

    private <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> List<D> from(DataRowBuilder<I, E, D> dataRowBuilder, BiFunction<DataRowBuilder<I, E, D>, Integer, Stream<E>> targetFunction) {
        return IntStream
                .range(0, dataRowBuilder.getSources().size() - 1)
                .boxed()
                .flatMap(index1 -> {
                    Stream<E> target = targetFunction.apply(dataRowBuilder, index1);
                    return toDataRow(dataRowBuilder, index1, target, dataRowBuilder.getSources().get(index1));
                })
                .peek(CalculationResult::assign)
                .map(CalculationResult::getDataRow)
                .collect(Collectors.toList());
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
                        Stream<CalculationResult<FeatureCalculator, D>> calculationResultStream = dataRowBuilder.getFeatureCalculators().stream().map(feature -> feature.calculate(source, y, dataRow));
                        return calculationResultStream;
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        throw new InvalidDataRowConstruction(e);
                    }
                });
    }

    private <I extends Comparable<I>, E extends Source<I>, D extends DataRow<I>> Stream<E> target(DataRowBuilder<I, E, D> dataRowBuilder, Integer index1) {
        return IntStream.range(index1 + 1, dataRowBuilder.getSources().size())
                .boxed()
                .map(dataRowBuilder.getSources()::get);
    }

}
