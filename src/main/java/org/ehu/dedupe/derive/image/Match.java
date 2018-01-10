package org.ehu.dedupe.derive.image;

public class Match {
    private final PHashedImage left;
    private final PHashedImage right;
    private final Integer hamming;

    public Match(PHashedImage left, PHashedImage right, Integer hamming) {
        this.left = left;
        this.right = right;
        this.hamming = hamming;
    }

    public PHashedImage getLeft() {
        return left;
    }

    public PHashedImage getRight() {
        return right;
    }

    public int getHammingDistance() {
        return hamming;
    }
}
