package org.ehu.dedupe.derive.image;

import org.apache.commons.text.similarity.HammingDistance;

public class PHashedImage {

    public static final PHashedImage EMPTY = new PHashedImage("", "");

    private static final transient HammingDistance HAMMING = new HammingDistance();

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
        return new Match(this, right, this.hamming(right));
    }

    Integer hamming(PHashedImage right) {
        String leftHash = getHash();
        String rightHash = right.getHash();
        if (leftHash == null || rightHash == null) {
            return null;
        }
        int leftLen = leftHash.length();
        int rightLen = rightHash.length();
        if (leftLen != rightLen) {
            return Math.max(leftLen, rightLen);
        }
        return HAMMING.apply(leftHash, rightHash);
    }
}
