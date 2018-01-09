package org.ehu.dedupe.data;

public class DataRow<I> {
    private final I id1;
    private final I id2;
    private final Boolean duplicate;

    public DataRow(I id1, I id2, Boolean duplicate) {
        this.id1 = id1;
        this.id2 = id2;
        this.duplicate = duplicate;
    }

    public I getId1() {
        return id1;
    }

    public I getId2() {
        return id2;
    }

    public Boolean isDuplicate() {
        return duplicate;
    }
}
