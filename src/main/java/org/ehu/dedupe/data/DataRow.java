package org.ehu.dedupe.data;

import java.util.Objects;

public class DataRow<I extends Comparable<I>> {
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

    @Override
    public boolean equals(Object o) {
        DataRow<I> left = this;
        if (left == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRow<?> dataRow = (DataRow<?>) o;
        return equals(left, dataRow);
    }

    private boolean equals(DataRow left, DataRow dataRow) {
        return (Objects.equals(left.id1, dataRow.id1) &&
                Objects.equals(left.id2, dataRow.id2) || Objects.equals(left.id1, dataRow.id2) && Objects.equals(left.id2, dataRow.id1)) &&
                Objects.equals(left.duplicate, dataRow.duplicate);
    }

    @Override
    public int hashCode() {
        return id1.compareTo(id2) > 0 ? Objects.hash(id1, id2, duplicate) : Objects.hash(id2, id1, duplicate);
    }
}
