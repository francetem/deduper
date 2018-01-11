package org.ehu.dedupe.derive.address;

public class Range {

    private final int min;
    private final int max;

    public Range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Range(int unique) {
        this(unique, unique);
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


    public boolean same(Range normalizedAddress) {
        return this == normalizedAddress || contains(normalizedAddress) || normalizedAddress.contains(this);
    }

    private boolean contains(Range normalizedAddress) {
        return this.contains(normalizedAddress.getMin()) && this.contains(normalizedAddress.getMax());
    }

    @Override
    public String toString() {
        return "" + min + "-" + max;
    }

}
