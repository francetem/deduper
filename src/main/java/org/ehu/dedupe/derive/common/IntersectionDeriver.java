package org.ehu.dedupe.derive.common;


import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.function.Function;

public class IntersectionDeriver<E, S> extends ReflectionDeriver<E, S> {

    public IntersectionDeriver(String propertyName, Function<S, E> getter) {
        super(propertyName, (left, right) -> {
            E apply = getter.apply(left);
            return new SimpleResult<>(apply.equals(getter.apply(right)) ? apply : null);
        });
    }
}
