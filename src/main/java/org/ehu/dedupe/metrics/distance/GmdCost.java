package org.ehu.dedupe.metrics.distance;

import java.math.BigDecimal;
import java.util.Collection;

public class GmdCost<I> {

    private final Collection<Collection<Collection<I>>> splits;
    private final Collection<Collection<Collection<I>>> merges;
    private final BigDecimal cost;

    public GmdCost(Collection<Collection<Collection<I>>> splits, Collection<Collection<Collection<I>>> merges, BigDecimal cost) {
        this.splits = splits;
        this.merges = merges;
        this.cost = cost;
    }

    public Collection<Collection<Collection<I>>> getSplits() {
        return splits;
    }

    public Collection<Collection<Collection<I>>> getMerges() {
        return merges;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
