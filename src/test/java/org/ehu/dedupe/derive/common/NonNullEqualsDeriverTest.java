package org.ehu.dedupe.derive.common;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class NonNullEqualsDeriverTest {


    @Test
    public void testDeriverOnNull() throws Exception {
        NonNullEqualsDeriver<String> equalsDeriver = new NonNullEqualsDeriver<>("any", t -> "none".equals(t) ? null : t);

        assertFalse(equalsDeriver.calculate("any", "none", null).getResult().process());
        assertFalse(equalsDeriver.calculate("none", "any", null).getResult().process());
        assertNull(equalsDeriver.calculate("none", "none", null).getResult().process());
        assertTrue(equalsDeriver.calculate("any", "any", null).getResult().process());
    }
}