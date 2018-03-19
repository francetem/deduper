package org.ehu.dedupe.data.dyna;

import org.ehu.dedupe.DataRowException;
import org.ehu.dedupe.DatarowFactory;

public class DynaFactory<K extends Comparable<K>> implements DatarowFactory<DynaDataRow<K>, K> {

    @Override
    public DynaDataRow<K> create(K id1, K id2, Boolean sameBucket) throws DataRowException {
        return new DynaDataRow<>(id1, id2, sameBucket);
    }
}
