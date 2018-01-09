package org.ehu.dedupe.derive.address;

public class Range {

    public static final Range EMPTY = new Range();

    private final Integer min;
    private final Integer max;


    public Range(Integer min, Integer max) {
        if (min == null || max == null) {
            throw new NullPointerException("Not allowed null values");
        }
        this.min = min;
        this.max = max;
    }

    public Range(Integer unique) {
        this(unique, unique);
    }

    private Range() {
        min = null;
        max = null;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public boolean contains(int value) {
        return min <= value && value <= max;
    }

    @Override
    public String toString() {
        return "" + min + "-" + max;
    }

    public boolean same(Range normalizedAddress) {
        return this == normalizedAddress || (this != EMPTY && normalizedAddress != EMPTY) && (contains(normalizedAddress) || normalizedAddress.contains(this));

    }

    private boolean contains(Range normalizedAddress) {
        return this.contains(normalizedAddress.getMin()) && this.contains(normalizedAddress.getMax());
    }
}
