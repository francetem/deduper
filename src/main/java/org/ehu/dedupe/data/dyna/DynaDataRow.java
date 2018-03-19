package org.ehu.dedupe.data.dyna;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.ehu.dedupe.data.DataRow;

public class DynaDataRow<T extends Comparable<T>> extends DataRow<T> {

    private final DynaBean dynaBean = new LazyDynaBean();

    public DynaDataRow(T id1, T id2, Boolean duplicate) {
        super(id1, id2, duplicate);
    }

    @Override
    public DynaBean implementor() {
        return dynaBean;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        //we want the exact parent equals
        return super.equals(o);
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        //we want the exact parent equals
        return super.hashCode();
    }
}
