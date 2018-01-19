package org.ehu.dedupe.derive.image;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PHashedImageTest {

    @Test
    public void testMatch() throws Exception {
        PHashedImage left = new PHashedImage("url1", "1");
        PHashedImage right = new PHashedImage("url2", "1");
        Match match = left.match(right);

        assertEquals(match.getLeft(), left);
        assertEquals(match.getRight(), right);
        assertEquals(match.getHammingDistance(), Integer.valueOf(0));
    }

}
