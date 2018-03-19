package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionDatarowFactory<D extends DataRow<I>, I extends Comparable<I>> implements DatarowFactory<D, I> {
    private final Class<D> dataRowClass;

    public ReflectionDatarowFactory(Class<D> dataRowClass) {
        this.dataRowClass = dataRowClass;
    }

    @Override
    public D create(I id1, I id2, Boolean sameBucket) throws DataRowException {
        try {
            Constructor<D> classConstructor = getDataRowClass().getConstructor(id1.getClass(), id2.getClass(), Boolean.class);
            return classConstructor.newInstance(id1, id2, sameBucket);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new DataRowException(e);
        }

    }

    public Class<D> getDataRowClass() {
        return dataRowClass;
    }

}
