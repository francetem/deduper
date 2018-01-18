package org.ehu.dedupe.derive.geo;

import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.math.BigDecimal;
import java.util.function.Function;

public class GeoDeriver<S> extends ReflectionDeriver<BigDecimal, S> {

    public GeoDeriver(String propertyName, Function<S, Coordinates> getter) {
        super(propertyName, (left, right) -> new SimpleResult<>(getter.apply(left).distFrom(getter.apply(right))));
    }
}
