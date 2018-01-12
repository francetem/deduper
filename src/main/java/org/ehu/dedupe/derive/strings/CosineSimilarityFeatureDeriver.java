package org.ehu.dedupe.derive.strings;

import org.ehu.dedupe.derive.ReflectionDeriver;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Function;

public class CosineSimilarityFeatureDeriver<S> extends ReflectionDeriver<BigDecimal, S> {

    public CosineSimilarityFeatureDeriver(String propertyName, Function<S, String> getter, DocFrequency docFrequency) {
        super(propertyName, (x, y) -> {
            Set<String> words1 = words(getter.apply(x));
            Set<String> words2 = words(getter.apply(y));
            docFrequency.documentCount(words1);
            docFrequency.documentCount(words2);
            return new CosineResult(words1, words2, docFrequency);
        });
    }

    private static Set<String> words(String name) {
        StringTokenizer tokenizer = new StringTokenizer(name);
        Set<String> words1 = new HashSet<>();
        while (tokenizer.hasMoreTokens()) {
            words1.add(tokenizer.nextToken());
        }
        return words1;
    }

}
