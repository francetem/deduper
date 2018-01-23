package org.ehu.dedupe.derive.common;

import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.Objects;
import java.util.function.Function;

public class NonNullEqualsDeriver<S> extends ReflectionDeriver<Boolean, S> {

    public NonNullEqualsDeriver(String propertyName, Function<S, Object> getter) {
        super(propertyName, (left, right) -> {
            Object leftValue = getter.apply(left);
            Object rightValue = getter.apply(right);
            return new SimpleResult<>(bothNonNulls(leftValue, rightValue) && Objects.equals(leftValue, rightValue));
        });
    }

    public static boolean bothNonNulls(Object leftValue, Object rightValue) {
        return leftValue != null && rightValue != null;
    }
}
