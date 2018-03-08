package org.ehu.dedupe.data.dyna;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaBeanPropertyMapDecorator;
import org.apache.commons.beanutils.LazyDynaBean;
import org.ehu.dedupe.data.Source;

import java.util.Map;

public class DynaSource<I> extends Source<I> {

    private final DynaBean dynaBean;

    public DynaSource(DynaBean dynaBean) {
        super((I) dynaBean.get("id"));
        this.dynaBean = dynaBean;
    }

    public static DynaSource<String> wrap(Map<String, Object> map) {
        DynaBean dynaBean = new LazyDynaBean();
        map.forEach(dynaBean::set);
        return new DynaSource<>(dynaBean);
    }

    void set(String propertyName, Object value) {
        dynaBean.set(propertyName, value);
    }

    public Object get(String property) {
        return dynaBean.get(property);
    }

    public Map<String, Object> asMap() {
        return new DynaBeanPropertyMapDecorator(dynaBean);
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        //we only want equality by id
        return super.equals(o);
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        //the same hashcode as super
        return super.hashCode();
    }
}
