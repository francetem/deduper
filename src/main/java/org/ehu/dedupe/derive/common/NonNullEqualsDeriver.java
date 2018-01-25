package org.ehu.dedupe.derive.common;

import java.util.function.Function;

public class NonNullEqualsDeriver<S> extends EqualsDeriver<S> {

    public NonNullEqualsDeriver(String propertyName, Function<S, Object> getter) {
        super(propertyName, getter, (leftValue, rightValue) -> bothNulls(leftValue, rightValue) ? null : EqualsDeriver.isEquals(leftValue, rightValue));
    }

    public static boolean bothNulls(Object leftValue, Object rightValue) {
        return leftValue == null && rightValue == null;
    }
}
