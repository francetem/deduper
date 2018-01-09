package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;

public interface FeatureCalculator<S, D extends DataRow> {

    void calculate(S x, S y, D dataRow);

    void postProcess(D dataRow);
}
