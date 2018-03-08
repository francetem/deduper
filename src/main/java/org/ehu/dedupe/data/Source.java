package org.ehu.dedupe.data;

import java.util.Objects;

public class Source<I> {

    private final I id;

    public Source(I id) {
        this.id = id;
    }

    public I getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;
        Source<?> source = (Source<?>) o;
        return Objects.equals(id, source.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
