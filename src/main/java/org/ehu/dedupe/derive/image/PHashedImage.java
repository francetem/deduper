package org.ehu.dedupe.derive.image;

public class PHashedImage {

    private final String url;
    private final String hash;

    public PHashedImage(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

    public Match match(PHashedImage right) {
        return new Match(this, right, false);
    }
}
