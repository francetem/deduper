package org.ehu.dedupe.derive.strings.levenshtein;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.function.Function;

public class LevenshteinFeatureDeriver<S> extends ReflectionDeriver<Integer, S> {

    public LevenshteinFeatureDeriver(String propertyName, Function<S, String> getter) {
        super(propertyName, (left, right) -> new SimpleResult<>(LevenshteinDistance.getDefaultInstance().apply(StringUtils.defaultString(getter.apply(left)), StringUtils.defaultString(getter.apply(right)))));
    }
}

