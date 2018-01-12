package org.ehu.dedupe.derive.strings;

import org.apache.commons.collections.CollectionUtils;
import org.ehu.dedupe.derive.Result;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class CosineResult implements Result<BigDecimal> {
    private final Set<String> words1;
    private final Set<String> words2;
    private DocFrequency docFrequency;

    public CosineResult(Set<String> words1, Set<String> words2, DocFrequency docFrequency) {
        this.words1 = words1;
        this.words2 = words2;
        this.docFrequency = docFrequency;
    }

    @Override
    public BigDecimal process() {
        Set<String> words1 = getWords1();
        Set<String> words2 = getWords2();
        Collection<String> intersection = CollectionUtils.intersection(words1, words2);

        if (intersection.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            int size1 = words1.size();
            int size2 = words2.size();

            double dotProduct = 0.0;
            double magnitude1 = 0.0;
            double magnitude2 = 0.0;

            for (String word : intersection) {

                double tf2 = (double) Collections.frequency(words2, word) / size2;
                double idf = idf(word);
                double tf1 = (double) Collections.frequency(words2, word) / size1;
                double tfidf1 = idf * tf1;
                double tfidf2 = idf * tf2;

                dotProduct += tfidf1 * tfidf2;

                magnitude1 += Math.pow(tfidf1, 2);
                magnitude2 += Math.pow(tfidf2, 2);

                words1.remove(word);
                words2.remove(word);

            }
            magnitude1 += complete(words1, size1);
            magnitude2 += complete(words2, size2);

            magnitude1 = Math.sqrt(magnitude1);
            magnitude2 = Math.sqrt(magnitude2);
            double val = dotProduct / (magnitude1 * magnitude2);
            return new BigDecimal(String.valueOf(val), MathContext.DECIMAL32);
        }
    }

    public Set<String> getWords1() {
        return words1;
    }

    public Set<String> getWords2() {
        return words2;
    }


    private double complete(Set<String> words, int size1) {
        double magnitude = 0.0;
        for (String word : words) {
            double idf = idf(word);
            double tf1 = (double) 1 / size1;
            double tfidf1 = idf * tf1;
            magnitude += Math.pow(tfidf1, 2);
        }
        return magnitude;
    }

    private double idf(String word) {
        return Math.log((double) docFrequency.docFrequency.keySet().size() / (double) docFrequency.docFrequency.getOrDefault(word, 0));
    }
}
