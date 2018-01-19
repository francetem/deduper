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
            return (F) PropertyUtils.getProperty(dataRow.implementor(), getName());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(wrongPropertyMessage("not able to get property: "), e);
        }
    }

    public String wrongPropertyMessage(String s) {
        return s + getName();
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
            set(calculationResult.getDataRow().implementor(), calculationResult.getResult().process());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(wrongPropertyMessage("not able to set property: "), e);
        }
    }

    public void set(Object dataRow, Object process) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PropertyUtils.setProperty(dataRow, getName(), process);
    }
}
