package org.ehu.dedupe.derive.strings.cosine;

import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.strings.DocFrequency;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Function;

public class CosineSimilarityFeatureDeriver<S> extends ReflectionDeriver<BigDecimal, S> {

    public CosineSimilarityFeatureDeriver(String propertyName, Function<S, String> getter, DocFrequency docFrequency) {
        super(propertyName, (x, y) -> {
            String xGet = getter.apply(x);
            String yGet = getter.apply(y);

            Set<String> words1 = docFrequency.document(xGet);
            Set<String> words2 = docFrequency.document(yGet);

            return new CosineResult(words1, words2, docFrequency);
        });
    }

}
