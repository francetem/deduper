package org.ehu.dedupe.derive.common;

import org.testng.annotations.Test;

import java.util.function.Function;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class EqualsDeriverTest {

    @Test
    public void testDeriver() throws Exception {
        EqualsDeriver<String> equalsDeriver = new EqualsDeriver<>("any", Function.identity());

        assertTrue(equalsDeriver.calculate("any", "any", null).getResult().process());
        assertFalse(equalsDeriver.calculate("any", "none", null).getResult().process());
    }
}