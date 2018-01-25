package org.ehu.dedupe.derive.common;


import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EqualsDeriver<S> extends ReflectionDeriver<Boolean, S> {

    public EqualsDeriver(String propertyName, Function<S, Object> getter) {
        this(propertyName, getter, EqualsDeriver::isEquals);
    }

    protected EqualsDeriver(String propertyName, Function<S, Object> getter, BiFunction<Object, Object, Boolean> merger) {
        super(propertyName, (left, right) -> {
            Object applyLeft = getter.apply(left);
            Object applyRight = getter.apply(right);
            return new SimpleResult<>(merger.apply(applyLeft, applyRight));
        });
    }


    public static boolean isEquals(Object applyLeft, Object applyRight) {
        return Objects.equals(applyLeft, applyRight);
    }
}
