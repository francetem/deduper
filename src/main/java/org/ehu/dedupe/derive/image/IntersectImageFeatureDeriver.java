package org.ehu.dedupe.derive.image;

import org.ehu.dedupe.derive.ReflectionDeriver;
import org.ehu.dedupe.derive.SimpleResult;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IntersectImageFeatureDeriver<S> extends ReflectionDeriver<Boolean, S> {

    public IntersectImageFeatureDeriver(String propertyName, Function<S, Set<String>> imagesGetter, ImagePHash imagePHash, int minimumDistance) {
        this(propertyName, imagesGetter.andThen(urls -> urls.stream().map(imagePHash::get).collect(Collectors.toSet())), minimumDistance);
    }

    public IntersectImageFeatureDeriver(String propertyName, Function<S, Set<PHashedImage>> imagesGetter, int minimumDistance) {
        super(propertyName, (left, right) -> new SimpleResult<>(imagesGetter.apply(left).stream().anyMatch(x -> imagesGetter.apply(right).stream().anyMatch(y -> {
            Integer hammingDistance = x.hamming(y);
            return hammingDistance != null && hammingDistance <= minimumDistance;
        }))));
    }
}
