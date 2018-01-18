package org.ehu.dedupe.derive.common;


import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.function.Function;

public class EqualsDeriver<S> extends ReflectionDeriver<Boolean, S> {

    public EqualsDeriver(String propertyName, Function<S, Object> getter) {
        super(propertyName, (left, right) -> new SimpleResult<>(getter.apply(left).equals(getter.apply(right))));
    }
}