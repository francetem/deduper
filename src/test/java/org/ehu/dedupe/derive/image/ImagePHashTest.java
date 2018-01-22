package org.ehu.dedupe.derive.image;

import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;

public class ImagePHashTest {

    @Test
    public void testGetHash() throws Exception {
        ImagePHash imagePHash = new ImagePHash();
        String hash = imagePHash.getHash(ImagePHashTest.class.getResourceAsStream("bilbao.jpg"));
        assertEquals(hash, "0000000001010011101000000100110000001001100010001");
    }

    @Test
    public void testGet() throws Exception {
        String url = "url";
        String pHash = "11111111111111111111";
        ImagePHash imagePHash = new ImagePHash(Collections.singletonMap(url, new PHashedImage(url, pHash)));
        assertEquals(imagePHash.get(url).getHash(), pHash);
    }

    @Test
    public void testGetCache() throws Exception {
    }

}