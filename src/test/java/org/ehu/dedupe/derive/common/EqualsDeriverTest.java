package org.ehu.dedupe.derive.common;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class EqualsDeriverTest {

    @Test
    public void testDeriver() throws Exception {
        EqualsDeriver<String> equalsDeriver = new EqualsDeriver<>("any", t -> t);

        assertTrue(equalsDeriver.calculate("any", "any", null).getResult().process());
        assertFalse(equalsDeriver.calculate("any", "none", null).getResult().process());
    }

    @Test
    public void testDeriverOnNull() throws Exception {
        EqualsDeriver<String> equalsDeriver = new EqualsDeriver<>("any", t -> "none".equals(t) ? null : t);

        assertFalse(equalsDeriver.calculate("any", "none", null).getResult().process());
        assertFalse(equalsDeriver.calculate("none", "any", null).getResult().process());
        assertTrue(equalsDeriver.calculate("none", "none", null).getResult().process());
    }
}