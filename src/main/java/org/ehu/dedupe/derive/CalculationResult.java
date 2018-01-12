package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;

public class CalculationResult<F, D extends DataRow> {
    private final FeatureCalculator deriver;
    private final D dataRow;
    private final F result;

    public CalculationResult(FeatureCalculator deriver, D dataRow, F result) {
        this.deriver = deriver;
        this.dataRow = dataRow;
        this.result = result;
    }

    public FeatureCalculator getDeriver() {
        return deriver;
    }

    public D getDataRow() {
        return dataRow;
    }

    public F getResult() {
        return result;
    }

    public void assign() {
        deriver.assign(this);
    }
}
