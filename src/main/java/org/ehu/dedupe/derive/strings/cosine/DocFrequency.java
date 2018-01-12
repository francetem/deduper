package org.ehu.dedupe.derive.strings.cosine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocFrequency {
    Map<String, Integer> docFrequency = new HashMap<String, Integer>();

    public DocFrequency() {
    }

    void documentCount(Set<String> words) {
        for (String word : words) {
            if (docFrequency.containsKey(word)) {
                docFrequency.put(word, docFrequency.get(word) + 1);
            } else {
                docFrequency.put(word, 1);
            }
        }
    }
}