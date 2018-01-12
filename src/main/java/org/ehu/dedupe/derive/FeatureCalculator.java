package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;

public interface FeatureCalculator<S, D extends DataRow> {

    CalculationResult calculate(S x, S y, D dataRow);

    void assign(CalculationResult calculationResult);
}
