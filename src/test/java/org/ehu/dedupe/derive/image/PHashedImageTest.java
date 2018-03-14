package org.ehu.dedupe.derive.image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    @Test
    public void testJsonSerialization() throws Exception {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(new PHashedImage("url1", "1"));
        assertEquals(json, "{\"url\":\"url1\",\"hash\":\"1\"}");
    }

}
