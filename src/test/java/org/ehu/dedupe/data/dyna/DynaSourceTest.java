package org.ehu.dedupe.data.dyna;

import org.apache.commons.beanutils.LazyDynaBean;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class DynaSourceTest {

    @Test
    public void testWrap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("someKey", "someValue");
        DynaSource<String> dynaSource = DynaSource.wrap(map);
        assertEquals(dynaSource.get("someKey"), "someValue");
    }

    @Test
    public void testAsMap() throws Exception {
        DynaSource<String> dynaSource = new DynaSource<>(new LazyDynaBean());
        dynaSource.set("none", "something");
        assertEquals("something", dynaSource.asMap().get("none"));
    }

}