package org.ehu.dedupe.derive.common;

import org.testng.annotations.Test;

import java.util.function.Function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class IntersectionDeriverTest {

    @Test
    public void testDeriver() {
        IntersectionDeriver<String, String> deriver = new IntersectionDeriver<>("any", Function.identity());
        assertEquals(deriver.calculate("any", "any", null).getResult().process(), "any");
        assertNull(deriver.calculate("any", "none", null).getResult().process());
    }

}