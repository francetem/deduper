package org.ehu.dedupe.derive;

public class SimpleResult<F> implements Result<F> {
    private final F v;

    public SimpleResult(F v) {
        this.v = v;
    }

    @Override
    public F process() {
        return v;
    }
}
