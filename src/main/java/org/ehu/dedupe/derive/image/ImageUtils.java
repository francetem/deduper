package org.ehu.dedupe.derive.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {
    public ImageUtils() {
    }

    InputStream getInput(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}