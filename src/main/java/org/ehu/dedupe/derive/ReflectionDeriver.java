package org.ehu.dedupe.derive;

import org.apache.commons.beanutils.PropertyUtils;
import org.ehu.dedupe.data.DataRow;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

public class ReflectionDeriver<F, S> implements FeatureDeriver<F, S, DataRow> {

    private final String propertyName;
    private final BiFunction<S, S, Result<F>> calculator;

    public ReflectionDeriver(String propertyName, BiFunction<S, S, Result<F>> calculator) {
        this.propertyName = propertyName;
        this.calculator = calculator;
    }

    @Override
    public F get(DataRow dataRow) {
        try {
            return (F) PropertyUtils.getProperty(dataRow, getName());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("not able to get property: ", e);
        }
    }

    @Override
    public String getName() {
        return propertyName;
    }

    @Override
    public CalculationResult<F, DataRow> calculate(S o1, S o2, DataRow dataRow) {
        return new CalculationResult<F, DataRow>(this, dataRow, calculator.apply(o1, o2));
    }

    public void assign(CalculationResult calculationResult) {
        try {
            PropertyUtils.setProperty(calculationResult.getDataRow(), getName(), calculationResult.getResult().process());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("not able to get property: ", e);
        }
    }
}
