package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;

public interface FeatureDeriver<F, S, D extends DataRow> extends FeatureCalculator<S, D> {

    F get(D dataRow);

    String getName();
}
