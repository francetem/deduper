package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class PreProcessDeriver<F, S, D extends DataRow> implements FeatureDeriver<F, S, D> {

    @Override
    public void postProcess(D dataRow) {
        //Nothing
    }

    protected <T> void process(S x, S y, Function<S, T> getter, BiConsumer<T, T> setter) {
        T left = getter.apply(x);
        T right = getter.apply(y);

        setter.accept(left, right);
    }
}
