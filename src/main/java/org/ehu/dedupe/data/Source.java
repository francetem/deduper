package org.ehu.dedupe.data;

public class Source<I> {

    private final I id;

    public Source(I id) {
        this.id = id;
    }

    public I getId() {
        return id;
    }
}
