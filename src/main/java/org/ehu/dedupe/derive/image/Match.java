package org.ehu.dedupe.derive.image;

import org.apache.commons.text.similarity.HammingDistance;

public class Match {
    private final PHashedImage left;
    private final PHashedImage right;
    private final Integer lev;
    private final boolean preProcessed;

    private HammingDistance hamming = new HammingDistance();

    public Match(PHashedImage left, PHashedImage right, boolean preProcessed) {
        this.left = left;
        this.right = right;
        this.preProcessed = preProcessed;
        this.lev = hamming(left, right);
    }

    private Integer hamming(PHashedImage left, PHashedImage right) {
        String leftHash = left.getHash();
        String rightHash = right.getHash();
        if (leftHash == null || rightHash == null) {
            return null;
        }
        int leftLen = leftHash.length();
        int rightLen = rightHash.length();
        if (leftLen != rightLen) {
            return Math.max(leftLen, rightLen);
        }
        return hamming.apply(leftHash, rightHash);
    }

    public PHashedImage getLeft() {
        return left;
    }

    public PHashedImage getRight() {
        return right;
    }

    public int getLev() {
        return lev;
    }

    public Boolean isSame() {
        if (lev == null) {
            return null;
        }
        return lev < 9 || (preProcessed && lev < 10);
    }

    public boolean isTrue() {
        Boolean same = isSame();
        return same != null && same;
    }
}
