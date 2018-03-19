package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;

public interface DatarowFactory<D extends DataRow<I>, I extends Comparable<I>> {
    D create(I id1, I id2, Boolean sameBucket) throws DataRowException;
}
