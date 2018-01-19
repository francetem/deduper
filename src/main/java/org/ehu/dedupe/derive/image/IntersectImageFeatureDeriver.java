package org.ehu.dedupe.derive.image;

import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.Set;
import java.util.function.Function;

public class IntersectImageFeatureDeriver<S> extends ReflectionDeriver<Boolean, S> {

    public IntersectImageFeatureDeriver(String propertyName, Function<S, Set<String>> imagesGetter, ImagePHash imagePHash, int minimumDistance) {
        super(propertyName, (left, right) -> new SimpleResult<>(imagesGetter.apply(left).stream().anyMatch(x -> imagesGetter.apply(right).stream().anyMatch(y -> {
            Integer hammingDistance = imagePHash.get(x).match(imagePHash.get(y)).getHammingDistance();
            return hammingDistance != null && hammingDistance <= minimumDistance;
        }))));
    }
}
