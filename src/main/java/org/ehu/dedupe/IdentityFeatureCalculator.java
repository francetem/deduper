package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.CalculationResult;
import org.ehu.dedupe.derive.FeatureCalculator;

final class IdentityFeatureCalculator implements FeatureCalculator {

    @Override
    public void assign(CalculationResult calculationResult) {
        //nothing
    }

    @Override
    public CalculationResult calculate(Object x, Object y, DataRow dataRow) {
        return new CalculationResult(this, dataRow, null);
    }
}
