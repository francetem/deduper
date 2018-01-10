package org.ehu.dedupe.derive.address;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RangeTest {

    private Range range1 = new Range(1, 1);
    private Range range2 = new Range(1, 2);


    @Test
    public void testContains() throws Exception {
        assertTrue(range1.contains(1));
        assertFalse(range1.contains(2));

        assertTrue(range2.contains(1));
        assertTrue(range2.contains(2));
    }

    @Test
    public void testSame() throws Exception {
        Range range3 = new Range(2, 3);
        assertTrue(range1.same(range2));
        assertFalse(range1.same(range3));


    }

}