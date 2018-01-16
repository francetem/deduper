package org.ehu.dedupe.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SourceTest {

    private final Source<Integer> source = new Source<>(1);

    @Test
    public void testEquals() throws Exception {

        assertFalse(source.equals(null));
        assertFalse(source.equals(""));

        assertTrue(source.equals(source));
        assertTrue(source.equals(new Source<>(1)));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(source.hashCode(), new Source<>(1).hashCode());
    }

}